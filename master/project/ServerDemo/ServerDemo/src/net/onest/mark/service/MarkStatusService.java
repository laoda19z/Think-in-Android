package net.onest.mark.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.onest.util.DBUtil;



public class MarkStatusService {
	private int username;
	private String year;
	private String month;
	private int child;
	private String day;
	
	public MarkStatusService() {
		super();
	}

	public MarkStatusService(int username, String year, String month, int child, String day) {
		super();
		this.username = username;
		this.year = year;
		this.month = month;
		this.child = child;
		this.day = day;
	}

	public int getUsername() {
		return username;
	}

	public void setUsername(int username) {
		this.username = username;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getChild() {
		return child;
	}

	public void setChild(int child) {
		this.child = child;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	
	//���������������ѯ���ݵ�
	public boolean JudgeStatus(int username,int child,String year,String month,String day) throws ClassNotFoundException, SQLException {
		//ͨ�����ݲ�ѯ
//		MyUtil util=new MyUtil();
		String markdates=year+"-"+month+"-"+day;
		System.out.println("markdates"+markdates);
		System.out.println("username"+username);
		System.out.println("child"+child);
		String sql="select * from mark where markdate='"+markdates+"' and userId="+username+" and childId="+child+"";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		conn = DBUtil.getConn();
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		
		return rs.next();
	}
	
	

}
