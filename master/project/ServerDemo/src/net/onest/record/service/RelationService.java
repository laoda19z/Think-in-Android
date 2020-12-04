package net.onest.record.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.Relation;
import net.onest.util.DBUtil;


public class RelationService {

	private List<Relation> list;
	public RelationService() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		list=new ArrayList<>();
	}
	//查询获取记录
	public List<Relation> getlist(String sql){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				int userid=rs.getInt("userid");
				int childid=rs.getInt("childid");
				Relation r=new Relation(userid,childid);
				list.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
