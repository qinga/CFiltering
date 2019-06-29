package com.jju.cfiltering.dao;

import java.util.List;

public interface LearnPathDao {

	public List<Object[]> getLearnPath( String topDirect);

	public List<Object[]> findDirect(Integer directID);

}
