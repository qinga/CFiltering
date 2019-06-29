package com.jju.cfiltering.dao;


import java.util.List;

import com.jju.cfiltering.entity.DynamicShare;
import com.jju.cfiltering.entity.User;

public interface SaveDyShareDao extends BaseDao<DynamicShare>{

	public void saveArticle(DynamicShare dynamicShare, User u);

	public List<Object[]> findDyShare();

	public List<Object[]> findPersonShare(Integer userId, Integer start, Integer size);

	public void saveGood(Integer shareID, Integer userID, Integer goodCount);

	public void saveOpPin(Integer shareID, Integer userID, String coments);


}
