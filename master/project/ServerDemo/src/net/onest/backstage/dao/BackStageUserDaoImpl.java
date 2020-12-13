package net.onest.backstage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.onest.entity.User;
import net.onest.util.DBUtil;

/**
 * ��̨�����û���DAO��
 * @author dell
 *
 */
public class BackStageUserDaoImpl {
	/**
	 * ��ѯ�û�����
	 * @return
	 */
	public int countByPage() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.getConn();
			String sql="select count(*) from user";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				count = rs.getInt(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return count;
	}
	/**
	 * ��ѯ�����û�
	 * @return
	 */
	public List<User> searchAllUser(int pageNum,int pageSize) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		List<User> users = new ArrayList<User>();
		try {
			conn = DBUtil.getConn();
			String sql="select * from user limit ?,?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, (pageNum-1)*pageSize);
			pstm.setInt(2, pageSize);
			rs = pstm.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt(3));
				user.setUsername(rs.getString(1));
				user.setHeadImg(rs.getString(4));
				user.setPhoneNum(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setSex(rs.getString(7));
				user.setRealname(rs.getString(8));
				users.add(user);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return users;
	}
	/**
	 * ����û�
	 *
	 */
	public boolean addUser(User user) {
		boolean b = false;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConn();
			User saveuser = searchUser(user.getUsername());
			System.out.println(saveuser.toString());
			if(saveuser.getUsername()==null) {
				String sql = "";
				sql = "insert into user(username,password,headImg) values (?,?,?)";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, user.getUsername());
				pstm.setString(2, user.getPassword());
				pstm.setString(3, "headers/tx.png");
				int m  = pstm.executeUpdate();
				if(m!=0) {
					b = true;
				}
			}else {
				b = false;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return b;
	}
	/**��ѯ�û�
	 * @param userName
	 * @return
	 */
	public User searchUser(String userName) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		User user = new User();
		try {
			conn = DBUtil.getConn();
			String sql="select * from user where username = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, userName);
			rs = pstm.executeQuery();
			while(rs.next()) {
				user.setUserId(rs.getInt(3));
				user.setPassword(rs.getString(2));
				user.setHeadImg(rs.getString(4));
				user.setPhoneNum(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setSex(rs.getString(7));
				user.setRealname(rs.getString(8));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return user;
	}
	/**
	 * ɾ���û�
	 */
	public boolean deleteUser(int id) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean b = false;
		try {
			conn = DBUtil.getConn();
			String sql="delete from user where userId = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			int result = pstm.executeUpdate();
			if(result>0) {
				b = true;
				return b;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return b;
	}
}
