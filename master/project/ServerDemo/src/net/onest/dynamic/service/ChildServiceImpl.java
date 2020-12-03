package net.onest.dynamic.service;

import java.sql.SQLException;

import net.onest.dynamic.dao.ChildDaoImpl;
import net.onest.entity.Child;



public class ChildServiceImpl {
	
	/**
	 * 根据孩子ID获取孩子数据
	 * @param childId
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Child getChildById(int childId){
		ChildDaoImpl childDaoImpl  = new ChildDaoImpl();
		
		return childDaoImpl.getChildById(childId);
	}

}
