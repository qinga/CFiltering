package com.jju.cfiltering.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 *
 */
public class Author {
	
	private Integer id;
	private String name;
	private String nationality;
	
	//表达与书的关系1-N的关系
	
	private Set<Books> books = new HashSet<Books>();

	public Author() {
		
	}

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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Set<Books> getBooks() {
		return books;
	}

	public void setBooks(Set<Books> books) {
		this.books = books;
	}
	
	
	
}
