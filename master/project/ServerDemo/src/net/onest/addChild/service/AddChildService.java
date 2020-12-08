package net.onest.addChild.service;

import net.onest.addChild.dao.AddChildDao;
import net.onest.entity.Child;

public class AddChildService {
	/**
	 * 添加新孩子
	 * */
	public boolean addChild(Child child) {
		AddChildDao addChild = new AddChildDao();
		return addChild.addChild(child);
	}
	
	/**
	 * 返回添加的孩子ID
	 * */
	public int findChildId(String childName) {
		AddChildDao addChild = new AddChildDao();
		return addChild.findChildId(childName);
	}
}
