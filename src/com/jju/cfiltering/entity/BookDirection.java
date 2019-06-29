package com.jju.cfiltering.entity;

import java.util.HashSet;
import java.util.Set;

public class BookDirection {

	private Integer id;
	private String name;
	private String title;
	//方向与书籍类型表示一对多的关系。1-N关系
	private Set<BooksType> booksType = new HashSet<BooksType>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Set<BooksType> getBooksType() {
		return booksType;
	}

	public void setBooksType(Set<BooksType> booksType) {
		this.booksType = booksType;
	}

	@Override
	public String toString() {
		return "BookDirection [id=" + id + ", name=" + name + ", title=" + title + ", booksType=" + booksType + "]";
	}
}
