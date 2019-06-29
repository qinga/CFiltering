package com.jju.cfiltering.entity;

import java.util.HashSet;
import java.util.Set;

public class Publish {

	private Integer id;
	private String name;
	
	//表达与书籍的关系 n-n的关系
	
	private Set<Books> books = new HashSet<Books>();
	
	public Publish() {}

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

	public Set<Books> getBooks() {
		return books;
	}

	public void setBooks(Set<Books> books) {
		this.books = books;
	}
	
}
