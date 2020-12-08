package net.onest.addChild.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.onest.entity.Child;
import net.onest.util.DBUtil;

public class AddChildDao {
	/**
	 * 插入新孩子
	 * */
	public boolean addChild(Child child) {
		boolean b = false;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement("insert into child(name,age,grade,sex) values (?,?,?,?)");
			pstm.setString(1, child.getChildName());
			System.out.println(child.getChildName());
			pstm.setInt(2, child.getChildAge());
			pstm.setInt(3, child.getChildGrade());
			pstm.setString(4, child.getChildSex());
			int m  = pstm.executeUpdate();
			if(m!=0) {
				b = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return b;
	}
	
	/**
	 * 查询孩子的id，并返回
	 * */
	public int findChildId(String childName) {
		int childId = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement("select childId from child where name = ?");
			pstm.setString(1, childName);
			rs = pstm.executeQuery();
			while(rs.next()) {
				childId = rs.getInt(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return childId;
	}
}
