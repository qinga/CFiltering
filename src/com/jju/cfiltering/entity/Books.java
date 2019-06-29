package com.jju.cfiltering.entity;

import java.util.HashSet;
import java.util.Set;

public class Books {

	private Integer id;
	private String name;
	private String createTime;
	private String lastEditTime;
	private String bookVersion;
	private String content;

	//书籍类型（小白，新手，其它）暂时性的判断变量
	private String opFlag;

	//该书籍的描述
	private String bookDesc;

	//描述该书籍是否是新上线书籍
	private Integer newBookFlag;

	//描述该书籍是否是技能提升类书籍
	private Integer skillBoostFlag;
	
	private Integer beginBookFlag;
	//---------------------------------------------------------		
	//表达书籍与书本图片的关系,1-N关系;
	private Set<BooksImg> booksImg = new HashSet<BooksImg>();
	//<<书与作者>>的表达 N-1关系
	private Author author;
	//---------------------------------------------------------	
	//<<书与出版社>>的表达 N-1关系
	private Publish publish;
	//<<书与用户>>的表达（用户浏览）1-N关系
	//此处表达与中间表UserSkimMap的关系，为一对的关系
	private Set<UserSkimMap> userSkimMap = new HashSet<UserSkimMap>();

	//<<书籍与书籍类型>>的关系 N-N关系（直接多对多表达，不拆分）
	private Set<BooksType> booksType = new HashSet<BooksType>();
	//----------------------------------------------------------	
	//<<书与用户>>的表达（用户收藏）N-N关系
	//此处表达与中间表（UserBookHouseMap）的一对多的关系
	private Set<UserBookHouseMap> userBookHouseMap = new HashSet<UserBookHouseMap>();
	
	//<<书与用户>>的表达（用户对书籍的评分）
	//此处表达用户与中间表（UserBookScoreMap）的一对多关系
	private Set<UserBookCommentsMap> userBookCommentsMap = new HashSet<UserBookCommentsMap>();
	
	//<<书与用户>>的表达（用户对书籍的评分）
	//此处表达用户与中间表（UserBookScoreMap）的一对多关系
	private Set<UserBookScoreMap> userBookScoreMap = new HashSet<UserBookScoreMap>();
	public Books() {}


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

	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getLastEditTime() {
		return lastEditTime;
	}


	public void setLastEditTime(String lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public String getBookVersion() {
		return bookVersion;
	}


	public void setBookVersion(String bookVersion) {
		this.bookVersion = bookVersion;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getOpFlag() {
		return opFlag;
	}


	public void setOpFlag(String opFlag) {
		this.opFlag = opFlag;
	}
	public String getBookDesc() {
		return bookDesc;
	}


	public void setBookDesc(String bookDesc) {
		this.bookDesc = bookDesc;
	}

	public Integer getNewBookFlag() {
		return newBookFlag;
	}


	public void setNewBookFlag(Integer newBookFlag) {
		this.newBookFlag = newBookFlag;
	}


	public Integer getSkillBoostFlag() {
		return skillBoostFlag;
	}


	public void setSkillBoostFlag(Integer skillBoostFlag) {
		this.skillBoostFlag = skillBoostFlag;
	}
	
	public Integer getBeginBookFlag() {
		return beginBookFlag;
	}


	public void setBeginBookFlag(Integer beginBookFlag) {
		this.beginBookFlag = beginBookFlag;
	}


	public Set<BooksImg> getBooksImg() {
		return booksImg;
	}


	public void setBooksImg(Set<BooksImg> booksImg) {
		this.booksImg = booksImg;
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


	public Set<UserSkimMap> getUserSkimMap() {
		return userSkimMap;
	}


	public void setUserSkimMap(Set<UserSkimMap> userSkimMap) {
		this.userSkimMap = userSkimMap;
	}


	public Set<BooksType> getBooksType() {
		return booksType;
	}


	public void setBooksType(Set<BooksType> booksType) {
		this.booksType = booksType;
	}


	public Set<UserBookHouseMap> getUserBookHouseMap() {
		return userBookHouseMap;
	}


	public void setUserBookHouseMap(Set<UserBookHouseMap> userBookHouseMap) {
		this.userBookHouseMap = userBookHouseMap;
	}


	public Set<UserBookCommentsMap> getUserBookCommentsMap() {
		return userBookCommentsMap;
	}


	public void setUserBookCommentsMap(Set<UserBookCommentsMap> userBookCommentsMap) {
		this.userBookCommentsMap = userBookCommentsMap;
	}
	public Set<UserBookScoreMap> getUserBookScoreMap() {
		return userBookScoreMap;
	}
	public void setUserBookScoreMap(Set<UserBookScoreMap> userBookScoreMap) {
		this.userBookScoreMap = userBookScoreMap;
	}

	
	
	
}
