package com.jju.cfiltering.entity;

import java.util.HashSet;
import java.util.Set;

public class BooksType {

	private Integer id;
	//与书籍类型表示多对多的关系。。不需要拆分。
	private Set<Books> books = new HashSet<Books>();
	private String name;
	//与书籍方向表示多对一的关系 N-1
	private BookDirection bookDirection;
	
	public BooksType() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Books> getBooks() {
		return books;
	}

	public void setBooks(Set<Books> books) {
		this.books = books;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BookDirection getBookDirection() {
		return bookDirection;
	}

	public void setBookDirection(BookDirection bookDirection) {
		this.bookDirection = bookDirection;
	}

	@Override
	public String toString() {
		return "BooksType [id=" + id + ", books=" + books + ", name=" + name + ", bookDirection=" + bookDirection + "]";
	}
}
