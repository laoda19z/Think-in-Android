package net.onest.activity.service;

import java.util.List;

import net.onest.activity.dao.MyLoveDaoImpl;
import net.onest.entity.ParentChildActivities;



public class MyLoveServices {
	public List<ParentChildActivities> listTypes(){
		MyLoveDaoImpl myLoveDaoImpl = new MyLoveDaoImpl();
		return myLoveDaoImpl.findAll();
	}
}
