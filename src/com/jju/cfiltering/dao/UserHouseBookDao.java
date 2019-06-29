package com.jju.cfiltering.dao;


import com.alibaba.fastjson.JSONObject;

public interface UserHouseBookDao {

	public Boolean saveHouBook(Integer bookID, Integer dbID);

	public JSONObject findHouseBook(Integer dbID, Integer start, Integer size, String type);

	public Boolean removeHouseBook(Integer dbID, Integer bookID);

	public Boolean updateScore(Integer dbID, Integer bookID, Integer time);

	public Boolean updateStartPinScore(Integer dbID, Integer bookID, Integer starCount, String userComment);

	public void insertAutoPin(Integer id);

}
