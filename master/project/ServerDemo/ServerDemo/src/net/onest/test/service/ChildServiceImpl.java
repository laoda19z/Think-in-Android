package net.onest.test.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.Child;
import net.onest.test.dao.ChildDaoImpl;


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
