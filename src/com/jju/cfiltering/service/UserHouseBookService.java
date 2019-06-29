package com.jju.cfiltering.service;


import com.alibaba.fastjson.JSONObject;

public interface UserHouseBookService {

	public Boolean saveUserHouBook(Integer bookID, Integer dbID);

	public JSONObject findHouseBook(Integer dbID, Integer start, Integer size, String type);

	public Boolean removeHouseBook(Integer dbID, Integer bookID);

	public Boolean updateScore(Integer dbID, Integer bookID, Integer time);

	public Boolean updateStarPinScore(Integer dbID, Integer bookID, Integer starCount, String userComment);

	public void insertAutoPin(Integer id);

}
