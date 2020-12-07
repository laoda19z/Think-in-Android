package net.onest.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.onest.entity.Child;
import net.onest.util.DBUtil;



public class ChildDaoImpl {
	/**
	 * 根据ID获取信息
	 * @param childId
	 * @return
	 */
	public Child getChildById(int childId) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Child child = new Child();
		try {
			conn = DBUtil.getConn();
			String sql = "select * from child where childId = "+childId;
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				child.setChildId(rs.getInt("childId"));
				child.setChildAge(rs.getInt("age"));
				child.setChildGrade(rs.getInt("grade"));
				child.setChildSex(rs.getString("sex"));
				child.setChildName(rs.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return child;
	}

}
