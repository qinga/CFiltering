package com.jju.cfiltering.dao;

import java.util.List;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.alibaba.fastjson.JSONObject;

public interface RecommenderDao {

	public DataModel findDataModel();

	public List<Object[]> findDetailBookByID(List<RecommendedItem> recommendations);

	public List<Object[]> findTopNRecommender();

}
