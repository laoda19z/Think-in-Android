package net.onest.record.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.ClockRecord;
import net.onest.util.DBUtil;


public class ClockRecordService {

	private List<ClockRecord> lists;
	public ClockRecordService() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		conn = DBUtil.getConn();
		lists=new ArrayList<>();
	}
	//查询记录
	public List<ClockRecord> getlist(String sql){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				int userId=rs.getInt("userId");
				int sportTime=rs.getInt("sporttime");
				String markDate=rs.getString("markdate");
				String sportImpressions=rs.getString("impression");
				String background=rs.getString("background");
				String sportType=rs.getString("sporttype");
				int child=rs.getInt("childId");
				ClockRecord one=new ClockRecord(userId,sportTime,markDate,sportImpressions,background,sportType,child);
				lists.add(one);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}

	//判断数据库中是否有打卡数据
	public boolean isExist(String sql) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean s=false;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
