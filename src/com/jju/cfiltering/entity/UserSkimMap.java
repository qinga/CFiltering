package com.jju.cfiltering.entity;

public class UserSkimMap {
	private Integer id;
	private Double skimScore;
	
	private User user;
	private Books books;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getSkimScore() {
		return skimScore;
	}
	public void setSkimScore(Double skimScore) {
		this.skimScore = skimScore;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Books getBooks() {
		return books;
	}
	public void setBooks(Books books) {
		this.books = books;
	}
	
	
	
	

}
