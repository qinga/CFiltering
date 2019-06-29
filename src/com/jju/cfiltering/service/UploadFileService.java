package com.jju.cfiltering.service;

import java.util.List;

import com.jju.cfiltering.entity.Author;
import com.jju.cfiltering.entity.BookDirection;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;
import com.jju.cfiltering.entity.BooksType;
import com.jju.cfiltering.entity.Publish;

public interface UploadFileService {

	public Boolean insertUploadFiles(Books book, Author author, Publish publish, BooksType booksType, BookDirection bookDirection,
			List<BooksImg> booksImg);
}
