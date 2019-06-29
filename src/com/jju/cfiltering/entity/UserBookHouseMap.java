package com.jju.cfiltering.entity;

public class UserBookHouseMap {
	
	private Integer id;
	private String houseTime;
	
	//<<书与用户>>的表达（用户收藏）N-N关系
	//此中间表表达与书(Books)的多对一的关系
	private Books books;
	
	//<<书与用户>>的表达（用户收藏）N-N关系
	//此处表达中间表与（User)的多对一的关系
	private User user;
	public UserBookHouseMap() {
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

	public String getHouseTime() {
		return houseTime;
	}

	public void setHouseTime(String houseTime) {
		this.houseTime = houseTime;
	}

	public Books getBooks() {
		return books;
	}

	public void setBooks(Books books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return "UserBookHouseMap [id=" + id + ", houseTime=" + houseTime + ", books=" + books + ", user=" + user + "]";
	}
}
