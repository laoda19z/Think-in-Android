package net.onest.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mysql.fabric.xmlrpc.base.Data;

import net.onest.entity.Report;
import net.onest.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReportDaoImpl {
	/**
	 * Ìí¼Ó±¨¸æ
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
	
	

}
