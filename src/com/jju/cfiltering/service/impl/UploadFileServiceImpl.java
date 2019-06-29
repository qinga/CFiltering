package com.jju.cfiltering.service.impl;

import java.util.List;

import com.jju.cfiltering.dao.UploadFileDao;
import com.jju.cfiltering.entity.Author;
import com.jju.cfiltering.entity.BookDirection;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;
import com.jju.cfiltering.entity.BooksType;
import com.jju.cfiltering.entity.Publish;
import com.jju.cfiltering.service.UploadFileService;

public class UploadFileServiceImpl implements UploadFileService {

	private UploadFileDao uploadDao;
	//处理后台上传的书籍
	@Override
	public Boolean insertUploadFiles(Books book, Author author, Publish publish, BooksType booksType,
			BookDirection bookDirection, List<BooksImg> booksImg) {
		return uploadDao.insertUploadFiles(book,author,publish,booksType,bookDirection,booksImg);
	}
	
	public UploadFileDao getUploadDao() {
		return uploadDao;
	}
	public void setUploadDao(UploadFileDao uploadDao) {
		this.uploadDao = uploadDao;
	}
}
