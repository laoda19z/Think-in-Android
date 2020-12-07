package net.onest.mark.service;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.DateMark;
import net.onest.entity.ReturnMarkPic;
import net.onest.util.DBUtil;


public class DateMarkService {
	//查询数据
		public List<DateMark> SearchByMonthAndYear(int username,String year,String month,int child){
			List<DateMark> dateList=new ArrayList<>();
			
			String condition=year+"-"+month;
			
			String sql="select markdate from mark where userId="+username+" and childId="+child+" and markdate like '"+condition+"%'";
			Connection conn = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int count = 0;
			try {
				conn = DBUtil.getConn();
				pstm = conn.prepareStatement(sql);
				rs = pstm.executeQuery();
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
					System.out.println("查询为0");
					DateMark markdate=new DateMark();
					String date="null";
					markdate.setMarkdate(date);
					dateList.add(markdate);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("dateList的长度为"+dateList.size());
			return dateList;
		}
		

}
