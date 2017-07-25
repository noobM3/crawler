package com.spider.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.spider.bean.Book;
import com.spider.bean.LinkTypeData;
import com.spider.bean.Rule;
import com.spider.bean.RuleException;
import com.spider.sql.DangDang;
import com.spider.sql.dangdangoperations;
import com.spider.util.TextUtil;

public class ExtractServiceDangDang
{

	/**
	 * 依照相应的规则解析数据
	 * @param rule      连接的规则类
	 * @param response  登录返回的http Response,需要登录验证时返回的cookie
	 * @return          
	 */
	public static Elements extract(Rule rule, Response response)
	{

		// 进行对rule的必要校验
		validateRule(rule);

		Elements results = null;
		try
		{
			/**
			 * 解析rule
			 */
			String url = rule.getUrl();
			String[] params = rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int requestType = rule.getRequestMoethod();

			//模拟浏览器登录
			Connection conn = Jsoup.connect(url).userAgent("Mozilla/5.0 "
					+ "(Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko)"
					+ " Chrome/26.0.1410.64 Safari/537.31").ignoreHttpErrors(true);
			
			//设置cookies
			if(response != null){
				conn.cookies(response.cookies());
			}
			
			// 设置查询参数
			if (params != null)
			{
				for (int i = 0; i < params.length; i++)
				{
					conn.data(params[i], values[i]);//key1,name
				}
			}

			// 设置请求类型
			Document doc = null;
			switch (requestType)
			{
			case Rule.GET:
				doc = conn.timeout(100000).get();
				break;
			case Rule.POST:
				doc = conn.timeout(100000).post();
				break;
			}

			//处理返回数据
			results = new Elements();
			switch (type)
			{
			case Rule.CLASS:
				results = doc.getElementsByClass(resultTagName);
				break;
			case Rule.ID:
				Element result = doc.getElementById(resultTagName);
				results.add(result);
				break;
			case Rule.SELECTION:
				results = doc.select(resultTagName);
				break;
			default:
				//当resultTagName为空时默认去body标签
				if (TextUtil.isEmpty(resultTagName))
				{
					results = doc.getElementsByTag("body");
				}
			}


		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return results;//网页源码
	}
	
	/**
	 * 从网页中寻找超链接
	 * @param results
	 * @return
	 */
	public static List<LinkTypeData> searchHref(Elements results){
		
		List<LinkTypeData> datas = new ArrayList<LinkTypeData>();
		for (Element result : results)
		{
			Elements links = result.getElementsByTag("a");
			
			for (Element link : links)//<a title="" ..........>
			{	
				//必要的筛选
				String linkHref = link.attr("href"); //获取商品链接
//				String linkHref = "";
				String linkText = link.text();
				
				LinkTypeData data = new LinkTypeData();
				data.setLinkHref(linkHref);
				data.setLinkText(linkText);
				datas.add(data);
			}
		}

		return datas;
	}
	
	/**
	 * 从网页中寻找表格
	 * @param results
	 * @return
	 */
	public static List<LinkTypeData> searchTable(Elements results){
		
		List<LinkTypeData> datas = new ArrayList<LinkTypeData>();
		for (Element result : results)
		{
			Elements tables = result.getElementsByTag("table");  //筛析表格内容
			Element block1 = tables.get(0);  //选取第一个表格
	        Elements block2 = block1.select("td[class=tdleft]"); //依照css取得内容
	        for (Element e:block2) {
	            System.out.println(e.text().toString().trim());
	        }

		}
		
		return datas;
	}
	
	/**
	 * 当当网中搜索结果对应的多个书籍
	 * @param results  搜索结果的元素集
	 * @return  书籍信息列表
	 */
	public static List<Book> searchListInfo(Elements results,Response response){
		List<LinkTypeData> datas = searchHref(results);//datas中单个元素形如:<a title=".."......>
//		System.out.println("datas***************" + datas);
		List<Book> booklist = new ArrayList<Book>();
		int num = 0;  //展示图书数
		System.out.println("size:" + datas.size());
		for (int i = 0; i < datas.size() && num < 4; i++) //展示用，所以限制小一点
		{
			String url = datas.get(i).getLinkHref();  //获取超链接
			System.out.println(i);
//			System.out.println("url--------------" + url);
	        Rule rule = new Rule(url,  
	                new String[] {}, new String[] {},  
	                null, -1, Rule.GET);     //设置搜索规则
	        
	        /*处理返回数据*/
	        Elements e = ExtractServiceDangDang.extract(rule, response);//源代码
	        /*获取对应的内容*/
//	        if(i == 0) System.out.println(e);//可删除
	        Book b = ExtractServiceDangDang.searchInfo(e);
	        if(b != null)
	        {
	        	//将信息录入数据库 TODO
	        	dangdangoperations.insert(new DangDang(b.getName(), b.getPrice(), b.getAuthor(),
	        			b.getPublisher(), b.getTime(), b.getISBN(), url, b.getImage()));
	        	num++;
	        	b.setUrl(url);
	        	booklist.add(b);
	        }
	        
		}
		
		return booklist;
	}
	
	/**
	 * 当当网单一商品数据
	 * @param results
	 * @return 
	 */
	public static Book searchInfo(Elements results){
		
		Element result = results.get(0);//源代码
		String name = null;
		String price = null;
		String author = null;
		String publisher = null;
		String time = null;
		String url = null;
		String image = null;
		String ISBN = null;
		String descrip = null;
		
//		System.out.println("--------------" + result.getElementById("dd-price").text());
		/*从网页元素里读取数据
		 * 按照类型或id读取相应的数据*/
		/*需根据网页的不同样式来选择不同方式获取数据*/
		String kind = result.getElementsByClass("domain").get(0).text().toString();//图书 or 数字商品
		if (kind.equals("图书"))//------>纸质书
		{ 
			Elements infos = result.select("[class=t1]");

			name = result.getElementsByClass("name_info").text().toString();
//			System.out.println("name:" + name);

			author = infos.get(0).text().toString();
//			System.out.println("author:" + author);

			price = result.getElementById("dd-price").text().toString();
//			System.out.println("price:" + price);

			publisher = infos.get(1).text().toString();
//			System.out.println("publisher:" + publisher);

			time = infos.get(2).text().toString();
//			System.out.println("time:" + time);

			image = result.getElementById("largePic").attr("src");
//			System.out.println("img:" + image);

			Elements dElements = results.select("[class=key clearfix]");
			ISBN = dElements.select("li").get(9).text();
//			System.out.println("ISBN:" + ISBN);
			
//			//TODO 图书简介
//			Elements detailElements = results.select("span[id=content-all]");
//			if (detailElements == null)
//			{
//				detailElements = results.select("span[id=content-show]");
//			}
//			for (Element element : detailElements)
//			{
//				System.out.println("-----------------" + element);
//			}
//			for (Element element : deElements)
//			{
//				System.out.println("descrips=====" + element);
//				
//			}
//			for (Element element : descrips)
//			{
//				System.out.println("element----" + element.text());
//			}
//			System.out.println("descrips-------" + descrips.text());
//			descrip = result.getElementsByClass("descrip").text().toString();
//			System.out.println("descrip:" + descrip);
			
			/*对获取后的数据头部进行处理*/
			price = price.replaceFirst("\\?", "");
			author = author.replaceFirst("作者:", "");
			publisher = publisher.replaceFirst("出版社:", "");
			time = time.replaceFirst("出版时间:", "");
			ISBN = ISBN.replaceFirst("国际标准书号ISBN：", "");
			
			/*将数据封装在一个对象中*/
			Book book = new Book();
			book.setName(name);
			book.setPrice(price);
			book.setAuthor(author);
			book.setPublisher(publisher);
			book.setTime(time);
			book.setImage(image);
			book.setISBN(ISBN);
			return book;
		}
		return null;

	}
	
	
	
	/**
	 * 对传入的参数进行必要的格式校验
	 */
	private static void validateRule(Rule rule)
	{
		String url = rule.getUrl();
		if (TextUtil.isEmpty(url))
		{
			throw new RuleException("url不能为空！");
		}
		if (!(url.startsWith("http://") || url.startsWith("https://")))
		{
			throw new RuleException("url的格式不正确！");
		}

		if (rule.getParams() != null && rule.getValues() != null)
		{
			if (rule.getParams().length != rule.getValues().length)
			{
				throw new RuleException("参数的键值对个数不匹配！");
			}
		}

	}


}
