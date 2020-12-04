package net.onest.activity.service;

import java.util.List;

import net.onest.activity.dao.ActivityDaoImpl;
import net.onest.entity.ParentChildActivities;




public class ActivityServices {
	public List<ParentChildActivities> listTypes(){
		ActivityDaoImpl activityDaoImpl = new ActivityDaoImpl();
		return activityDaoImpl.findAll();
	}
}
