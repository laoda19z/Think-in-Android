package net.onest.record.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.AssessmentReport;
import net.onest.record.util.DbUtil;



public class AssessmentReportService {
	private DbUtil dbUtil;
	private List<AssessmentReport> list;
	public AssessmentReportService() {
		try {
			dbUtil=DbUtil.getInstance();
			list=new ArrayList<>();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//判断数据库中是否有测评数据
	public boolean isExist() {
		String sql="select * from report";
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
	//查询获取记录
	public List<AssessmentReport> getlist(String sql){
		try {
			ResultSet rs=dbUtil.queryDate(sql);
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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
