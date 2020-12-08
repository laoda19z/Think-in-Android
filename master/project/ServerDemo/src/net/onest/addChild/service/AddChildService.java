package net.onest.addChild.service;

import net.onest.addChild.dao.AddChildDao;
import net.onest.entity.Child;

public class AddChildService {
	/**
	 * ����º���
	 * */
	public boolean addChild(Child child) {
		AddChildDao addChild = new AddChildDao();
		return addChild.addChild(child);
	}
	
	/**
	 * ������ӵĺ���ID
	 * */
	public int findChildId(String childName) {
		AddChildDao addChild = new AddChildDao();
		return addChild.findChildId(childName);
	}
}
