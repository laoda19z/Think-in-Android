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
//	private DbUtil dbUtil;
	private List<AssessmentReport> list;
	public AssessmentReportService() {
		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		conn = DBUtil.getConn();
		list=new ArrayList<>();
	}
	
	//�ж����ݿ����Ƿ��в�������
	public boolean isExist() {
		String sql="select * from report";
		boolean s=false;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			return rs.next();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return s;
	}
	//��ѯ��ȡ��¼
	public List<AssessmentReport> getlist(String sql){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				int bodyScore=rs.getInt("body_score");
				int childId=rs.getInt("child_id");
				int downScore=rs.getInt("down_score");
				int overallScore=rs.getInt("overall_score");
				int upScore=rs.getInt("up_score");
				int assessmentReportId=rs.getInt("report_id");
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
