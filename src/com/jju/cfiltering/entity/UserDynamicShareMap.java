package com.jju.cfiltering.entity;

public class UserDynamicShareMap {


	private Integer id;
	private String comments;
	private Integer thumbsUp;
	private String commnetTime;
	
	private DynamicShare dynamicShare;
	private User user;
	
	public UserDynamicShareMap() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getThumbsUp() {
		return thumbsUp;
	}
	public void setThumbsUp(Integer thumbsUp) {
		this.thumbsUp = thumbsUp;
	}
	public String getCommnetTime() {
		return commnetTime;
	}
	public void setCommnetTime(String commnetTime) {
		this.commnetTime = commnetTime;
	}
	public DynamicShare getDynamicShare() {
		return dynamicShare;
	}
	public void setDynamicShare(DynamicShare dynamicShare) {
		this.dynamicShare = dynamicShare;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
