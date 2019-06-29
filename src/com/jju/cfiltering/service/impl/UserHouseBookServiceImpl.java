package com.jju.cfiltering.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.dao.UserHouseBookDao;
import com.jju.cfiltering.service.UserHouseBookService;

public class UserHouseBookServiceImpl implements UserHouseBookService {

	private UserHouseBookDao userHouseBook;
	@Override
	public Boolean saveUserHouBook(Integer bookID, Integer dbID) {
			return userHouseBook.saveHouBook(bookID, dbID);
	}
	public UserHouseBookDao getUserHouseBook() {
		return userHouseBook;
	}
	public void setUserHouseBook(UserHouseBookDao userHouseBook) {
		this.userHouseBook = userHouseBook;
	}
	@Override
	public JSONObject findHouseBook(Integer dbID, Integer start, Integer size, String type) {
		return userHouseBook.findHouseBook(dbID, start, size, type);
	}
	@Override
	public Boolean removeHouseBook(Integer dbID, Integer bookID) {
		// TODO Auto-generated method stub
		return userHouseBook.removeHouseBook(dbID, bookID);
	}
	@Override
	public Boolean updateScore(Integer dbID, Integer bookID, Integer time) {
		// TODO Auto-generated method stub
		return userHouseBook.updateScore(dbID, bookID, time);
	}
	@Override
	public Boolean updateStarPinScore(Integer dbID, Integer bookID, Integer starCount, String userComment) {
		return userHouseBook.updateStartPinScore(dbID, bookID, starCount, userComment);
	}
	@Override
	public void insertAutoPin(Integer id) {
		// TODO Auto-generated method stub
		 userHouseBook.insertAutoPin(id);
	}
}
