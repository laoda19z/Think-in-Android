package net.onest.record.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.Relation;
import net.onest.record.util.DbUtil;



public class RelationService {
	private DbUtil dbUtil;
	private List<Relation> list;
	public RelationService() {
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
	//查询获取记录
	public List<Relation> getlist(String sql){
		try {
			ResultSet rs=dbUtil.queryDate(sql);
			while(rs.next()) {
				int userid=rs.getInt("userid");
				int childid=rs.getInt("childid");
				Relation r=new Relation(userid,childid);
				list.add(r);
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
