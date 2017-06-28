package com.spider.sql;

public final class DangDang{
        private String Id;
        private String name;
        private String price;
        private String author;
        private String publisher;
        private String time;
        private String ISBN; 
        private String url;
        private String image;

        public DangDang(String name, String price, String author, String publisher, String time, String ISBN,String url, String image) {
            this.Id = null; //default
            this.name = name;
            this.price = price;
            this.author = author;
            this.publisher = publisher;
            this.time = time;
            this.ISBN = ISBN;
            this.url = url;
            this.image = image;
        }

		public String getId() {
			return Id;
		}

		public void setId(String id) {
			Id = id;
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
		
		
		public String getISBN()
		{
			return ISBN;
		}

		public void setISBN(String iSBN)
		{
			ISBN = iSBN;
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


		
}