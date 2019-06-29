package com.jju.cfiltering.entity;

import java.util.Set;

public class LearnPath {

	private Integer id;
	private String name;
	
	private Integer beginnerBookFlag;
	private String videoPath;
	
	private String learnPathDetail;
	private Integer useCount;
	private String markDirect;
	private String pathImg;
	
	//与学习路线表示1对多的关系 1-N
	private Set<UserLearnPath> userLearnPath;
	

	
	public LearnPath() {
		
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
	
	public Integer getBeginnerBookFlag() {
		return beginnerBookFlag;
	}
	public void setBeginnerBookFlag(Integer beginnerBookFlag) {
		this.beginnerBookFlag = beginnerBookFlag;
	}
	public String getMarkDirect() {
		return markDirect;
	}
	public void setMarkDirect(String markDirect) {
		this.markDirect = markDirect;
	}
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	public String getLearnPathDetail() {
		return learnPathDetail;
	}
	public void setLearnPathDetail(String learnPathDetail) {
		this.learnPathDetail = learnPathDetail;
	}
	
	public Set<UserLearnPath> getUserLearnPath() {
		return userLearnPath;
	}
	public String getPathImg() {
		return pathImg;
	}
	public void setPathImg(String pathImg) {
		this.pathImg = pathImg;
	}
	public Integer getUseCount() {
		return useCount;
	}
	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}
	public void setUserLearnPath(Set<UserLearnPath> userLearnPath) {
		this.userLearnPath = userLearnPath;
	}
}
