package com.jju.cfiltering.service;

import com.jju.cfiltering.entity.Manager;
import com.jju.cfiltering.entity.User;

public interface LoginService {

	public User persistLogin(User user);
	public Manager persistLogin(Manager manager);
	public User getUserByName(String userName);
}
