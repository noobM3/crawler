<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.spider.bean.*" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
	<title>展示页面</title>
	<link rel="stylesheet" type="text/css" href="css/show.css"/>
	
</head>
<body background="background.gif">
<% ArrayList<Book> booklist = (ArrayList<Book>)request.getAttribute("booklist");%>
            <%
                for(Book book:booklist){
            %>
                <div class="show">
                	<img src="<%=book.getImage() %>" alt="<%=book.getName() %>"/>
                	<p>
	                	<span>书籍名称：<%=book.getName() %></span><br />
	                	<span>书籍价格：<%=book.getPrice() %></span><br />
	                	<span>作者：<%=book.getAuthor() %></span><br />
	                	<span>出版社：<%=book.getPublisher() %></span><br />
	                	<span>ISBN：<%=book.getISBN()%></span><br />
	                	<span>出版时间：<%=book.getTime() %></span><br />
	                	<span>购买链接：<a href="<%=book.getUrl() %>"><%=book.getUrl() %></a></span>

	                </p>
                </div>
            <%
                }
            
            %>
</body>
</html>