package com.jju.cfiltering.entity;

public class BooksImg {

	private Integer id;
	private String imgAddr;
	private String imgDesc;
	private Integer priority;
	
	//表达多对一
	
	private Books books;

	public BooksImg() {}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImgAddr() {
		return imgAddr;
	}

	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}

	public String getImgDesc() {
		return imgDesc;
	}

	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Books getBooks() {
		return books;
	}

	public void setBooks(Books books) {
		this.books = books;
	}
	@Override
	public String toString() {
		return "BooksImg [id=" + id + ", imgAddr=" + imgAddr + ", imgDesc=" + imgDesc + ", priority=" + priority
				+ ", books=" + books + "]";
	}
}
