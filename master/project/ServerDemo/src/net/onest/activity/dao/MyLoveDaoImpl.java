package net.onest.activity.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.ParentChildActivities;
import net.onest.util.DBUtil;




public class MyLoveDaoImpl {
		public List<ParentChildActivities> findAll(){
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			List<ParentChildActivities> activities = new ArrayList<>();
			try {
				con = DBUtil.getConn();
				pstm = con.prepareStatement("select * from activity_love");
				rs = pstm.executeQuery();
				while(rs.next()) {
					ParentChildActivities activity = new ParentChildActivities();
					activity.setActivityId(rs.getInt(1));
					activity.setActivityName(rs.getString(2));
					activity.setActivityTime(rs.getString(3));
					activity.setActivityPeoNum(rs.getInt(4));
					activity.setActivityContent(rs.getString(5));
					activity.setActivityPay(rs.getString(6));
					activity.setActivityDistrict(rs.getString(7));
					activity.setActivityImg(rs.getString(8));
					activities.add(activity);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return activities;
		}
}
