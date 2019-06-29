package com.jju.cfiltering.service;

import java.util.List;

public interface LearnPathService {

	public List<Object[]> findLearnPath(String topDirect);

	public List<Object[]> findDirect(Integer directID);

}
