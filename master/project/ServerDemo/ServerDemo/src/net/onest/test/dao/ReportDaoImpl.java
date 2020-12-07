package net.onest.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.onest.entity.Report;
import net.onest.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReportDaoImpl {
	/**
	 * 添加报告
	 * @param report
	 * @return
	 */
	public int addReport(Report report) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int b = 0;
		try {
			conn = DBUtil.getConn();
			String sqlInsert = "insert into report(childId,overallscore,upscore,downscore,bodyscore)";
			String sqlValue = "values("+report.getChildId()+","+
					report.getOverallScore()+","+
					report.getUpScore()+","+
					report.getDownScore()+","+
					report.getBodyScore()+")";
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
	

}
