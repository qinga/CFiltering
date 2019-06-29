package com.jju.cfiltering.entity;

import java.util.HashSet;
import java.util.Set;

public class User {
	
	private Integer id;
	private String name;
	private String email;
	private String password;
	private String phone;
	private String profile_img;
	private Integer gender;
	private String register_time;
	
	//<<用户与书>>的表达（用户浏览）1-N关系
	//此处表达与中间表(UserSkimMap)的一对多的关系
	private Set<UserSkimMap> userSkimMap = new HashSet<UserSkimMap>();

	//<<书与用户>>的表达（用户收藏）N-N关系
	//此处表达与中间表（UserBookHouseMap）的一对多的关系
	private Set<UserBookHouseMap> userBookHouseMap =new HashSet<UserBookHouseMap>();
	
	//<<用户与学习路线>>的表达（用户学习路线）N-N关系
	//此处表达与中间表（UserLearnPath）的一对多的关系
	private Set<UserLearnPath> userLearnPath = new HashSet<UserLearnPath>();
	//评论
	private Set<UserBookCommentsMap> userBookCommentsMap = new HashSet<UserBookCommentsMap>();
	//评分
	private Set<UserBookScoreMap> userBookScoreMap = new HashSet<UserBookScoreMap>();
	//表达用户的动态分享，1-N关系
	private Set<DynamicShare> dynamicShare = new HashSet<DynamicShare>();
	
	private Set<UserDynamicShareMap> userDynamicShareMap = new HashSet<UserDynamicShareMap>();

	
	
	public User() {
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



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProfile_img() {
		return profile_img;
	}



	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}



	public Integer getGender() {
		return gender;
	}



	public void setGender(Integer gender) {
		this.gender = gender;
	}



	public String getRegister_time() {
		return register_time;
	}



	public void setRegister_time(String register_time) {
		this.register_time = register_time;
	}

	public Set<UserSkimMap> getUserSkimMap() {
		return userSkimMap;
	}



	public void setUserSkimMap(Set<UserSkimMap> userSkimMap) {
		this.userSkimMap = userSkimMap;
	}



	public Set<UserBookHouseMap> getUserBookHouseMap() {
		return userBookHouseMap;
	}



	public void setUserBookHouseMap(Set<UserBookHouseMap> userBookHouseMap) {
		this.userBookHouseMap = userBookHouseMap;
	}



	public Set<UserLearnPath> getUserLearnPath() {
		return userLearnPath;
	}



	public void setUserLearnPath(Set<UserLearnPath> userLearnPath) {
		this.userLearnPath = userLearnPath;
	}

	public Set<UserBookCommentsMap> getUserBookCommentsMap() {
		return userBookCommentsMap;
	}

	public Set<UserBookScoreMap> getUserBookScoreMap() {
		return userBookScoreMap;
	}



	public void setUserBookScoreMap(Set<UserBookScoreMap> userBookScoreMap) {
		this.userBookScoreMap = userBookScoreMap;
	}

	public void setUserBookCommentsMap(Set<UserBookCommentsMap> userBookCommentsMap) {
		this.userBookCommentsMap = userBookCommentsMap;
	}



	public Set<DynamicShare> getDynamicShare() {
		return dynamicShare;
	}



	public void setDynamicShare(Set<DynamicShare> dynamicShare) {
		this.dynamicShare = dynamicShare;
	}



	public Set<UserDynamicShareMap> getUserDynamicShareMap() {
		return userDynamicShareMap;
	}



	public void setUserDynamicShareMap(Set<UserDynamicShareMap> userDynamicShareMap) {
		this.userDynamicShareMap = userDynamicShareMap;
	}

	
}
