package com.jju.cfiltering.entity;

import java.util.HashSet;
import java.util.Set;

public class DynamicShare {

	private Integer id;
	private String title;
	private String contents;
	private String publishTime;
	private String lastEditTime;
	private Integer checkStats;
	private String imageAddr;
	private String articleType;
	private Integer skimCount;
	

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	//用于接收前台参数，不需要持久化到数据库字段
	private String articleArr;

	//和用户的多对一的关系N-1
	private User user;
	
	//和（用户对动态分享评论）的一对多关系
	private Set<UserDynamicShareMap> userDynamicShareMap = new HashSet<UserDynamicShareMap>();

	
	public DynamicShare() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(String lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public Integer getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(Integer checkStats) {
		this.checkStats = checkStats;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<UserDynamicShareMap> getUserDynamicShareMap() {
		return userDynamicShareMap;
	}

	public void setUserDynamicShareMap(Set<UserDynamicShareMap> userDynamicShareMap) {
		this.userDynamicShareMap = userDynamicShareMap;
	}
	public String getArticleArr() {
		return articleArr;
	}

	public void setArticleArr(String articleArr) {
		this.articleArr = articleArr;
	}
	public String getImageAddr() {
		return imageAddr;
	}

	public void setImageAddr(String imageAddr) {
		this.imageAddr = imageAddr;
	}

	public Integer getSkimCount() {
		return skimCount;
	}

	public void setSkimCount(Integer skimCount) {
		this.skimCount = skimCount;
	}


	
}
