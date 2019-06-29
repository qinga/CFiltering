package com.jju.cfiltering.entity;

public class UserBookCommentsMap {

	private Integer id;
	private String comments;
	private String commentsTime;
	//<<书与用户>>的表达（用户对书籍的评分）
	//此中间表达与书（Books）的多对一关系。
	private Books books;
	private User user;
	public UserBookCommentsMap() {
		
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCommentsTime() {
		return commentsTime;
	}
	public void setCommentsTime(String commentsTime) {
		this.commentsTime = commentsTime;
	}
	public Books getBooks() {
		return books;
	}
	public void setBooks(Books books) {
		this.books = books;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
