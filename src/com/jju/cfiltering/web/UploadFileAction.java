package com.jju.cfiltering.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.Author;
import com.jju.cfiltering.entity.BookDirection;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;
import com.jju.cfiltering.entity.BooksType;
import com.jju.cfiltering.entity.Publish;
import com.jju.cfiltering.service.UploadFileService;
import com.jju.cfiltering.util.CFileUtil;
import com.opensymphony.xwork2.ActionSupport;

public class UploadFileAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private static final Integer BOOKFLAG = 1;
	private static final Integer UNBOOKFLAG = 0;
	
	private Books book;
	private Author author;
	private Publish publish;
	private BooksType booksType;
	private BookDirection bookDirection;
	
	private List<File> upload;
	
	private UploadFileService uploadService;
	
	public String uploadFile() {
			String basePath = CFileUtil.getImgBasePath() 
					+ CFileUtil.getBooksImagePath();
			
			String tempPath = "/CFilteringUpload" + CFileUtil.getBooksImagePath().replace("\\", "/")+"/";
			//存储图片地址的list集合
			List<BooksImg> booksImg = new ArrayList<BooksImg>();
			File file = new File(basePath);
		        
			//不存在则创建
			if(!file.exists()){ 
				file.mkdirs(); 
			}
			String finalPath = null;
			String secondPath = null;
			//循环将文件上传到指定路径
			if(upload != null) {
				for(int i = 0; i< upload.size(); i++){
					try {
						//保存到数据库的图片地址
						secondPath = CFileUtil.getRandomFileName()+".jpg";
						finalPath = tempPath + secondPath;
						BooksImg b = new BooksImg();
						b.setImgAddr(finalPath);
						//设置为主图
						if(i==0) b.setPriority(1);
						booksImg.add(b);
						FileUtils.copyFile(upload.get(i), new File(file,secondPath));
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			};
			try {
				switch(book.getOpFlag()){
		        case "0":
		           book.setBeginBookFlag(UNBOOKFLAG);
		           book.setNewBookFlag(UNBOOKFLAG);
		           book.setSkillBoostFlag(UNBOOKFLAG);
		        	break;
		        case "1":
		            book.setBeginBookFlag(UNBOOKFLAG);
		            book.setNewBookFlag(BOOKFLAG);
			        book.setSkillBoostFlag(UNBOOKFLAG);
		            break;
		        case "2":
		        	book.setBeginBookFlag(UNBOOKFLAG);
		            book.setNewBookFlag(UNBOOKFLAG);
		            book.setSkillBoostFlag(BOOKFLAG);
		            break;
		        case "3":
		        	book.setBeginBookFlag(BOOKFLAG);
		            book.setSkillBoostFlag(UNBOOKFLAG);
		            book.setNewBookFlag(UNBOOKFLAG);
		            break;
	        };
		        book.setCreateTime(CFileUtil.formateDate(new Date()));
		        book.setLastEditTime(CFileUtil.formateDate(new Date()));
		        Boolean inserUploadFlag =  uploadService.insertUploadFiles(book,author,publish,booksType,bookDirection,booksImg);
		        Map<String,String> result = new HashMap<String,String>();
		        HttpServletResponse rs = ServletActionContext.getResponse();
		        rs.setContentType("application/json;charset=utf-8");
		        if(inserUploadFlag == true) {
		        	result.put("stats", "SUCCESS");
		        	result.put("info", "200");
		        	JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(result));
					rs.getWriter().println(JSONRESULT);
		        }else {
		        	result.put("stats", "ERROR");
		        	result.put("info", "500");
		        	JSONObject JSONRESULT = JSONObject.parseObject(JSON.toJSONString(result));
					rs.getWriter().println(JSONRESULT);
		        }
		        return null;
			}catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("添加书籍图片操作异常！！");
			}
		}

	public Books getBook() {
		return book;
	}

	public void setBook(Books book) {
		this.book = book;
	}
	
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public Publish getPublish() {
		return publish;
	}
	public void setPublish(Publish publish) {
		this.publish = publish;
	}
	
	public BooksType getBooksType() {
		return booksType;
	}
	public void setBooksType(BooksType booksType) {
		this.booksType = booksType;
	}

	public BookDirection getBookDirection() {
		return bookDirection;
	}

	public void setBookDirection(BookDirection bookDirection) {
		this.bookDirection = bookDirection;
	}
	public List<File> getUpload() {
		return upload;
	}


	public void setUpload(List<File> upload) {
		this.upload = upload;
	}
	

	public UploadFileService getUploadService() {
		return uploadService;
	}

	public void setUploadService(UploadFileService uploadService) {
		this.uploadService = uploadService;
	}
}
