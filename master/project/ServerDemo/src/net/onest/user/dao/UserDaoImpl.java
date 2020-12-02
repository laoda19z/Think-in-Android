package net.onest.user.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.onest.entity.User;
import net.onest.util.DBUtil;

public class UserDaoImpl {
	/**
	 * 查询用户列表
	 * @param s
	 * @return
	 */
	public List<User> searchUserList(String s){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		List<User> users = new ArrayList<User>();
		try {
			conn = DBUtil.getConn();
			String sql="select * from user where userId in ("+s+")";
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt(3));
				user.setUsername(rs.getString(1));
				user.setHeadImg(rs.getString(4));
				users.add(user);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return users;
	}
	/**查询用户
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
				user.setUsername(rs.getString(1));
				user.setHeadImg(rs.getString(4));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return user;
	}
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public boolean signUpUser(User user) {
		boolean b = false;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConn();
			User saveuser = searchUser(user.getUsername());
			if(saveuser.getUsername()=="") {
				String sql = "";
				sql = "insert into user(username,password) values(?,?)";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, user.getUsername());
				pstm.setString(2, user.getPassword());
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
	/**
	 * 查询用户id
	 * @param username
	 * @return
	 */
	public int searchUserId(String username) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int userid = 0;
		List<User> users = new ArrayList<User>();
		try {
			conn = DBUtil.getConn();
			String sql="select userId from user where username = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while(rs.next()) {
				userid = rs.getInt(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return userid;
	}
	/**
	 * 删除用户
	 * @param userid
	 * @return
	 */
	public boolean deleteUser(int userid) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean b = false;
		try {
			conn = DBUtil.getConn();
			String sql="delete from user where userId = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userid);
			int line = pstm.executeUpdate();
			if(line != 0) {
				b = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return b;
	}
}
