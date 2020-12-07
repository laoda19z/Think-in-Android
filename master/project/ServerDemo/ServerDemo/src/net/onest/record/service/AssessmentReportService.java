package net.onest.record.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.AssessmentReport;
import net.onest.util.DBUtil;



public class AssessmentReportService {
	private List<AssessmentReport> list;
	public AssessmentReportService() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		conn = DBUtil.getConn();
		list=new ArrayList<>();
	}
	
	//判断数据库中是否有测评数据
	public boolean isExist(String sql) {
		boolean s=false;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			s=rs.next();
			return rs.next();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return s;
	}
	//查询获取记录
	public List<AssessmentReport> getlist(String sql){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			list=new ArrayList<>();
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				int bodyScore=rs.getInt("bodyscore");
				int childId=rs.getInt("childId");
				int downScore=rs.getInt("downscore");
				int overallScore=rs.getInt("overallscore");
				int upScore=rs.getInt("upscore");
				int assessmentReportId=rs.getInt("reportId");
				String time=rs.getString("evaluation_time");
				AssessmentReport report=new AssessmentReport(bodyScore,childId,downScore,overallScore,upScore,assessmentReportId,time);
				list.add(report);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return list;
	}
}
