package com.spider.bean;


/**
 * 
* @author hangzhu
* @version Jul 1, 2017 11:24:25 PM 
* @description:需要的数据对象
 */
public class LinkTypeData
{
	private int id;     
	private String linkHref;//链接的地址
	private String linkText;//链接的标题
	private String summary;//摘要
	private String content;//内容
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getLinkHref()
	{
		return linkHref;
	}
	public void setLinkHref(String linkHref)
	{
		this.linkHref = linkHref;
	}
	public String getLinkText()
	{
		return linkText;
	}
	public void setLinkText(String linkText)
	{
		this.linkText = linkText;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	@Override
	public String toString()
	{
		return "LinkTypeData [id=" + id + ", linkHref=" + linkHref
				+ ", linkText=" + linkText + ", summary=" + summary
				+ ", content=" + content + "]";
	}
	
	

}
