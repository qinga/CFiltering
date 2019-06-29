package com.jju.cfiltering.entity;

public class UserBookScoreMap {
	private Integer id;
	private float score;
	//<<书与用户>>的表达（用户对书籍的评分）
		//此中间表达与书（Books）的多对一关系。
	private Books books;
	private User user;
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}


