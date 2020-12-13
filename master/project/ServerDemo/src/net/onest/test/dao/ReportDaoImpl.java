package net.onest.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Data;

import net.onest.entity.AssessmentReport;
import net.onest.entity.Report;
import net.onest.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReportDaoImpl {
	/**
	 * ��ӱ���
	 * @param report
	 * @return
	 */
	public int addReport(Report report) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int b = 0;
		try {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			String time = formatter.format(date);
			conn = DBUtil.getConn();
			String sqlInsert = "insert into report(childId,overallscore,upscore,downscore,bodyscore,evaluationtime)";
			String sqlValue = "values("+report.getChildId()+","+
					report.getOverallScore()+","+
					report.getUpScore()+","+
					report.getDownScore()+","+
					report.getBodyScore()+","+"'"+
					time+"')";
			pstm = conn.prepareStatement(sqlInsert+sqlValue);
			b = pstm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return b;
	}
	/**
	 * 根据孩子ID获取多个报告对象
	 * @param childId
	 * @return
	 */
	public List<AssessmentReport> getAssessmentReportsById(int childId){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<AssessmentReport> reports = new ArrayList<AssessmentReport>();
		try {
			
			conn = DBUtil.getConn();
			String str = "select * from report where childId = "+childId;
			pstm = conn.prepareStatement(str);
			rs = pstm.executeQuery();
			while (rs.next()) {
				int bodyScore=rs.getInt("bodyscore");
				int downScore=rs.getInt("downscore");
				int overallScore=rs.getInt("overallscore");
				int upScore=rs.getInt("upscore");
				int assessmentReportId=rs.getInt("reportId");
				String time=rs.getString("evaluationtime");
				AssessmentReport report=new AssessmentReport(bodyScore,childId,downScore,overallScore,upScore,assessmentReportId,time);
				reports.add(report);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return reports;
	}
	/**
	 * 根据报告ID获取单个报告对象
	 * @param assessmentReportId
	 * @return
	 */
	public AssessmentReport getAssessmentReportById(int assessmentReportId) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		AssessmentReport assessmentReport = null;
		try {
			
			conn = DBUtil.getConn();
			String str = "select * from report where reportId = "+assessmentReportId;
			pstm = conn.prepareStatement(str);
			rs = pstm.executeQuery();
			while (rs.next()) {
				int bodyScore=rs.getInt("bodyscore");
				int downScore=rs.getInt("downscore");
				int overallScore=rs.getInt("overallscore");
				int upScore=rs.getInt("upscore");
				int childId=rs.getInt("childId");
				String time=rs.getString("evaluationtime");
				assessmentReport=new AssessmentReport(bodyScore,childId,downScore,overallScore,upScore,assessmentReportId,time);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return assessmentReport;
	}
	
	

}
