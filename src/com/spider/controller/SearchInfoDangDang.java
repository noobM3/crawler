package com.spider.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.select.Elements;

import com.spider.bean.Book;
import com.spider.bean.LinkTypeData;
import com.spider.bean.Rule;
import com.spider.service.ExtractServiceDangDang;

public class SearchInfoDangDang extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchInfoDangDang() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Book> booklist = new ArrayList<>();
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("name");
		System.out.println(name);
		String url = "http://search.dangdang.com";
		
		/*在建立一次http握手后,保存cookie,以免每次都需建立连接*/
		Connection con = Jsoup.connect(url).userAgent("Mozilla/5.0 "
				+ "(Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko)"
				+ " Chrome/26.0.1410.64 Safari/537.31");
		Response r = con.ignoreContentType(true).method(Method.GET).execute();
		
//		System.out.println(r);--->r的内存地址
	    Rule rule1 = new Rule(url,  
	                new String[] {"key"}, new String[] {name},  
	                "paging", Rule.CLASS, Rule.GET);
	    Elements results1 = ExtractServiceDangDang.extract(rule1, r);//-->网页源代码
//	    System.out.println("----------------------" + results1);
	    List<LinkTypeData> datas = ExtractServiceDangDang.searchHref(results1);
//	    System.out.println("datas:----------------" + datas);
	    String[] urls = new String[6];//存储6页的链接，用于抓取
	    for (int i = 0; i < urls.length; i++)
		{
			urls[i] = datas.get(i+1).getLinkHref();
			urls[i] = new StringBuffer(urls[i]).insert(0, "http://search.dangdang.com").toString();
//			System.out.println("urls:-----" + urls[i]);
		}
	    
	    for(int i = 0; i < urls.length; i++)//对不同图书列表页面进行抓取
		{
			/* 当当网商品爬取 */
			Rule rule = new Rule(urls[i], new String[]
			{ "key" }, new String[]
			{ name }, "pic", Rule.CLASS, Rule.GET);

			/* 处理返回数据 */
			Elements results = ExtractServiceDangDang.extract(rule, r);// <a
																		// title=".......>——->获取的是每个图书的链接
//			System.out.println("--------------results:" + results);
			/* 获取对应的内容 */
			// System.out.println("here1");
			booklist.addAll(ExtractServiceDangDang.searchListInfo(results, r));
			// System.out.println("here2");
			for (int j = 0; j < booklist.size(); j++)
			{
				System.out.println(booklist.get(j).toString());
			}

		}
        
		//将数据返回到显示页面
		request.setAttribute("booklist", booklist);
		//内部跳转到showDangDang.jsp
		request.getRequestDispatcher("showDangDang.jsp").forward(request, response);
	}

}
