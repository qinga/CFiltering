package com.jju.cfiltering.entity;


public class UserLearnPath {
	private Integer id;
	private String createTime;
	private String lastEditTime;
	
	private User user;
	private LearnPath learnPath;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LearnPath getLearnPath() {
		return learnPath;
	}
	public void setLearnPath(LearnPath learnPath) {
		this.learnPath = learnPath;
	}
}
