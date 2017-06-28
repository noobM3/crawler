package com.spider.bean;


public class Book {
	private String name;     //书名
	private String price;    //价格
	private String author;   //作者
	private String publisher;//出版社
	private String time;     //出版时间
	private String ISBN;     //ISBN号
	private String url;      //购买链接
	private String image;    //图片链接
    private String descrip;  //图书简介
	
	public Book(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	public String getISBN()
	{
		return ISBN;
	}

	public void setISBN(String iSBN)
	{
		ISBN = iSBN;
	}

	
	
	public String getDescrip()
	{
		return descrip;
	}

	public void setDescrip(String descrip)
	{
		this.descrip = descrip;
	}

	@Override
	public String toString(){
		return "name: " + name + " price: " + price + " author: "
				+ author + " publisher: " + publisher + " time: "
				+ time + " ISBN: " +  ISBN + " url: " + url + " image: " + image;
	}
	
	

}
