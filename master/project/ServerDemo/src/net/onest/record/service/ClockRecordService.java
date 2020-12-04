package net.onest.record.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.ClockRecord;
import net.onest.record.util.DbUtil;


public class ClockRecordService {
	private DbUtil dbUtil;
	private List<ClockRecord> lists;
	public ClockRecordService() {
		try {
			dbUtil=DbUtil.getInstance();
			lists=new ArrayList<>();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//��ѯ��¼
	public List<ClockRecord> getlist(String sql){
		try {
			ResultSet rs=dbUtil.queryDate(sql);
			int backgroundPicNum=0;
			String uploadPic="";
			while(rs.next()) {
				int userId=rs.getInt("userid");
				int sportTime=rs.getInt("sporttime");
				String markDate=rs.getString("markdate");
				String sportImpressions=rs.getString("impression");
				String background=rs.getString("background");
				String sportType=rs.getString("sporttype");
				ClockRecord one=new ClockRecord(userId,sportTime,markDate,sportImpressions,background,sportType);
				lists.add(one);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}

	//�ж����ݿ����Ƿ��д�����
	public boolean isExist() {
		String sql="select * from mark";
		boolean s=false;
		try {
			s=dbUtil.isExist(sql);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
