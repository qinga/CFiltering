package com.jju.cfiltering.service.impl;


import java.util.List;

import com.jju.cfiltering.dao.SaveDyShareDao;
import com.jju.cfiltering.entity.DynamicShare;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.SaveDyShareService;

public class SaveDyShareServiceImpl implements SaveDyShareService {

	private SaveDyShareDao sdd;
	
	public SaveDyShareDao getSdd() {
		return sdd;
	}
	public void setSdd(SaveDyShareDao sdd) {
		this.sdd = sdd;
	}
	@Override
	public void saveArticle(DynamicShare dynamicShare, User u) {
		sdd.saveArticle(dynamicShare, u);
	}
	@Override
	public List<Object[]> findDyShare() {
		return sdd.findDyShare();
	}
	@Override
	public List<Object[]> findPersonDyShare(Integer ...data) {
		if(data.length == 3) {
			return sdd.findPersonShare(data[0], data[1], data[2]);
		}else {
			return sdd.findPersonShare(data[0], null, null);
		}
	}
	@Override
	public void saveGood(Integer shareID, Integer userID, Integer goodCount) {
		sdd.saveGood(shareID, userID, goodCount);
	}
	@Override
	public void saveOpPin(Integer shareID, Integer userID, String coments) {
		sdd.saveOpPin(shareID, userID, coments);
	}
	

}
