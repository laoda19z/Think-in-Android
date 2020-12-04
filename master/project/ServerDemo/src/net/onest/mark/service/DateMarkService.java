package net.onest.mark.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.DateMark;
import net.onest.util.MyUtil;


public class DateMarkService {
	//��Ҫ���ڲ�ѯ����
	//ͨ���˺������꣬������ѯ
	private String username;
	private String year;
	private String month;
	private int child;
	public DateMarkService() {
		super();
	}
	
	public DateMarkService(String username, String year, String month, int child) {
		super();
		this.username = username;
		this.year = year;
		this.month = month;
		this.child = child;
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

	//��ѯ����
	public List<DateMark> SearchByMonthAndYear(String username,String year,String month,int child){
		List<DateMark> dateList=new ArrayList<>();
		MyUtil util=new MyUtil();
		String condition=year+"-"+month;
		String sql="select markdate from mark where username='"+username+"' and child="+child+" and markdate like '"+condition+"%'";
		try {
			ResultSet rs=util.queryDate(sql);
			if(rs.next()) {
				DateMark markdates=new DateMark();
				String dates=rs.getString(1);
				markdates.setMarkdate(dates);
				dateList.add(markdates);
				while(rs.next()) {
					DateMark markdate=new DateMark();
					String date=rs.getString(1);
					markdate.setMarkdate(date);
					dateList.add(markdate);
				}
			}
			else {
				System.out.println("��ѯΪ0");
				DateMark markdate=new DateMark();
				String date="null";
				markdate.setMarkdate(date);
				dateList.add(markdate);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("dateList�ĳ���Ϊ"+dateList.size());
		return dateList;
	}
	

}