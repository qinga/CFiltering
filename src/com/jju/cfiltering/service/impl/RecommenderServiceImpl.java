package com.jju.cfiltering.service.impl;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.dao.RecommenderDao;
import com.jju.cfiltering.service.RecommenderService;
import com.jju.cfiltering.util.BooksUtil;

public class RecommenderServiceImpl implements RecommenderService{
	private RecommenderDao recommenderDao;
	//基于项目的协同过滤
	@Override
	public JSONObject findItem(Long userID) {
		List<RecommendedItem> recommendations = null;
		List<Object[]> result = null;
		//获取系统的历史评分数据，构建用户-项目评分矩阵
		DataModel model = recommenderDao.findDataModel();
		try {
			//根据皮尔森相似度计算书籍的最近邻居书籍
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
			//用户喜好书籍的邻居项目产生
			Recommender recommender = new GenericItemBasedRecommender(model, similarity);
			//产生10条最近邻居书籍的推荐
			recommendations = recommender.recommend(userID, 10);
			for( int i = 0; i < recommendations.size(); i++) {
				System.out.println( recommendations.get(i).getItemID() + ":::::"  + recommendations.get(i).getValue());
			}
			//获取推荐的书籍
			result = recommenderDao.findDetailBookByID(recommendations);
			JSONObject recommder = BooksUtil.parseRecommenderBook(result);
			return recommder;
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	//基于用户的协同过滤推荐
	public JSONObject findUserItem(Long userID) {
			// step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
			List<RecommendedItem> recommendations = null;
			List<Object[]> result = null;
			try {
				//构造用户-书籍评分矩阵
				DataModel model = recommenderDao.findDataModel();
				//使用皮尔森相似度计算用户相似度
				UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
				//计算用户的“邻居”，这里将与该用户最近距离为 3 的用户设置为该用户的“邻居”。
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);
				//采用 CachingRecommender 为 RecommendationItem 进行缓存
				Recommender recommender = new CachingRecommender(
						new GenericUserBasedRecommender(model, neighborhood, similarity)
							);
				//得到推荐的结果，size是推荐结果的数目
				recommendations = recommender.recommend(userID, 10);
				//查询数据库，根据推荐结果查询数据库。
				result = recommenderDao.findDetailBookByID(recommendations);
				JSONObject userRecomder = BooksUtil.parseRecommenderBook(result);
				return userRecomder;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		return null;
	}
	
	
	@Override
	public JSONObject findTopNRecommer() {
		List<Object[]> result = recommenderDao.findTopNRecommender();
		JSONObject TopNRecomder = BooksUtil.parseRecommenderBook(result);
		return TopNRecomder;
	}
	
	public RecommenderDao getRecommenderDao() {
		return recommenderDao;
	}
	public void setRecommenderDao(RecommenderDao recommenderDao) {
		this.recommenderDao = recommenderDao;
	}
	
}
