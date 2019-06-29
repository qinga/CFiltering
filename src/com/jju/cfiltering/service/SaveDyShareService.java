package com.jju.cfiltering.service;

import java.util.List;

import com.jju.cfiltering.entity.DynamicShare;
import com.jju.cfiltering.entity.User;

public interface SaveDyShareService {


	public void saveArticle(DynamicShare dynamicShare, User u);

	public List<Object[]> findDyShare();

	public List<Object[]> findPersonDyShare(Integer ...data);

	public void saveGood(Integer shareID, Integer userID, Integer goodCount);

	public void saveOpPin(Integer shareID, Integer userID, String coments);


}
