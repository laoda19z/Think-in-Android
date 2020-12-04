package net.onest.mark.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.onest.util.DBUtil;



public class MarkStatusService {
	private String username;
	private String year;
	private String month;
	private int child;
	private String day;
	
	public MarkStatusService() {
		super();
	}

	public MarkStatusService(String username, String year, String month, int child, String day) {
		super();
		this.username = username;
		this.year = year;
		this.month = month;
		this.child = child;
		this.day = day;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
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
	
	//这个方法是用来查询数据的
	public boolean JudgeStatus(String username,int child,String year,String month,String day) throws ClassNotFoundException, SQLException {
		//通过数据查询
//		MyUtil util=new MyUtil();
		String markdates=year+"-"+month+"-"+day;
		System.out.println("markdates"+markdates);
		System.out.println("username"+username);
		System.out.println("child"+child);
		String sql="select * from mark where markdate='"+markdates+"' and username='"+username+"' and child="+child+"";
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
