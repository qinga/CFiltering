package com.jju.cfiltering.dao;

import com.jju.cfiltering.entity.Manager;
import com.jju.cfiltering.entity.User;

public interface LoginDao {

	public User persistLogin(User user);
	
	public Manager persistLogin(Manager manager);

	public User getUserByName(String userName);

}
