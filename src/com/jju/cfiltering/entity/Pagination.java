package com.jju.cfiltering.entity;

public class Pagination {

	private String bookName;
	private String authorName;
	private String publishName;
	private Integer NewBookFlag;
	private Integer SkillBookFlag;
	private Integer beginBookFlag;
	private Integer orginalBookFlag;
	private Integer bookFlag;
	private Integer pageNumber;
	private Integer pageSize;
	private Integer typeId;
	private String conditFlag;
	
	public String getConditFlag() {
		return conditFlag;
	}
	public void setConditFlag(String conditFlag) {
		this.conditFlag = conditFlag;
	}
	public Integer getOrginalBookFlag() {
		return orginalBookFlag;
	}
	public void setOrginalBookFlag(Integer orginalBookFlag) {
		this.orginalBookFlag = orginalBookFlag;
	}
	public Integer getNewBookFlag() {
		return NewBookFlag;
	}
	public void setNewBookFlag(Integer newBookFlag) {
		NewBookFlag = newBookFlag;
	}
	public Integer getSkillBookFlag() {
		return SkillBookFlag;
	}
	public void setSkillBookFlag(Integer skillBookFlag) {
		SkillBookFlag = skillBookFlag;
	}
	public Integer getBeginBookFlag() {
		return beginBookFlag;
	}
	public void setBeginBookFlag(Integer beginBookFlag) {
		this.beginBookFlag = beginBookFlag;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getPublishName() {
		return publishName;
	}
	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
	public Integer getBookFlag() {
		return bookFlag;
	}
	public void setBookFlag(Integer bookFlag) {
		this.bookFlag = bookFlag;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
}
