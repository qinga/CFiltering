package com.jju.cfiltering.service;

import com.alibaba.fastjson.JSONObject;

public interface RecommenderService {

	public JSONObject findItem(Long userID);

	public JSONObject findUserItem(Long userID);

	public JSONObject findTopNRecommer();

}
