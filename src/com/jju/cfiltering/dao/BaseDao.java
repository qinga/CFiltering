package com.jju.cfiltering.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;


public interface BaseDao<T> {
	//增或修改
	public void saveOrUpdate(T t);
	//增
	public void save(T t);
	//删
	public void delete(T t);
	//删
	public void delete(Serializable id);
	//改
	public void update(T t);
	//查 根据id查询
	public T getById(Serializable id);
	//查 符合条件的总记录数
	public Integer getTotalCount(DetachedCriteria dc);
	//查 查询分页列表数据
	public List<T> getPageList(DetachedCriteria dc,Integer start,Integer pageSize);
	
}
