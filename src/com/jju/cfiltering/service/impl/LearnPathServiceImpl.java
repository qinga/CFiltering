package com.jju.cfiltering.service.impl;

import java.util.List;

import com.jju.cfiltering.dao.LearnPathDao;
import com.jju.cfiltering.service.LearnPathService;

public class LearnPathServiceImpl implements LearnPathService {
	
	private LearnPathDao learnPath;

	@Override
	public List<Object[]> findLearnPath(String topDirect) {
		return learnPath.getLearnPath(topDirect);
	}

	public LearnPathDao getLearnPath() {
		return learnPath;
	}

	public void setLearnPath(LearnPathDao learnPath) {
		this.learnPath = learnPath;
	}

	@Override
	public List<Object[]> findDirect(Integer directID) {
		return learnPath.findDirect(directID);
	}
	
	

}
