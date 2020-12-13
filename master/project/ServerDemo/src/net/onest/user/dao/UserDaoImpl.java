package net.onest.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.onest.entity.Child;
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
	/**
	 * 查询评论的用户名
	 * @param s
	 * @return
	 */
	public List<User> searchCommentUserNameList(String s){
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
				user.setUsername(rs.getString(1));
				user.setPassword(rs.getString(2));
				user.setHeadImg(rs.getString(4));
				user.setPhoneNum(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setSex(rs.getString(7));
				user.setRealname(rs.getString(8));
				String kids = rs.getString(9);
				user.setKids(toChildren(kids));
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
	
	/**
	 *修改邮箱
	 *@param id,email
	 *@return 
	 */
	public boolean updateEmail(int id,String email) {
		boolean b = false;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DBUtil.getConn();
			pstm =  con.prepareStatement("update user set email = ? where userId = ?");
			pstm.setString(1,email);
			pstm.setInt(2,id);
			int m = pstm.executeUpdate();
			if(0 != m) {
				b = true;
			}
			System.out.println("修改邮箱成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}	
	
	/**
	 * 修改个人信息
	 * @param id,realName,sex,phone,email
	 * @return
	 */
	public boolean updateInfo(int id,String name,String sex,String phone,String email) {
		boolean b = false;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DBUtil.getConn();
			pstm =  con.prepareStatement("update user set realname = ? , sex = ? , phonenum = ? , email = ? where userId = ?");
			pstm.setString(1,name);
			pstm.setString(2, sex);
			pstm.setString(3, phone);
			pstm.setString(4,email);
			pstm.setInt(5,id);
			int m = pstm.executeUpdate();
			if(0 != m) {
				b = true;
			}
			System.out.println("修改信息成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 *修改昵称 
	 * @param id,newName
	 * @return
	 */
	public boolean updateName(int id,String newName) {
		boolean b = false;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DBUtil.getConn();
			pstm =  con.prepareStatement("update user set username = ? where userId = ?");
			pstm.setString(1,newName);
			pstm.setInt(2,id);
			int m = pstm.executeUpdate();
			if(0 != m) {
				b = true;
			}
			System.out.println("修改昵称成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}	
	/**
	 *修改电话号码 
	 * @param id,newPhone
	 */
	public boolean updatePhone(int id,String newPhone) {
		boolean b = false;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DBUtil.getConn();
			pstm =  con.prepareStatement("update user set phonenum = ? where userId = ?");
			pstm.setString(1,newPhone);
			pstm.setInt(2,id);
			int m = pstm.executeUpdate();
			if(0 != m) {
				b = true;
			}
			System.out.println("修改电话号码成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	/**
	 *修改密码
	 * @param id,newPwd
	 * @return
	 */
	public boolean updatePwd(int id,String newPwd) {
		boolean b = false;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DBUtil.getConn();
			pstm =  con.prepareStatement("update user set password = ? where userId = ?");
			pstm.setString(1,newPwd);
			pstm.setInt(2,id);
			int m = pstm.executeUpdate();
			if(0 != m) {
				b = true;
			}
			System.out.println("修改密码成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * 根据用户id查找用户信息
	 * */
	public User findUser(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		User user = new User();
		try {
			con = DBUtil.getConn();
			pstm = con.prepareStatement("select * from user where userId = ?");
			pstm.setInt(1,id);
			rs = pstm.executeQuery();
			while(rs.next()) {
				user.setPassword(rs.getString(2));
				user.setUserId(rs.getInt(3));
				user.setHeadImg(rs.getString(4));
				user.setPhoneNum(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setSex(rs.getString(7));
				user.setRealname(rs.getString(8));
				String kids = rs.getString(9);
				user.setKids(toChildren(kids));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public List<Child> toChildren(String json){
		List<Child> children = new ArrayList<Child>();
		if(json==null){
			return children;
		}else {
			JSONObject job = new JSONObject(json);
			JSONArray jArray = job.getJSONArray("kids");
			for(int i=0;i<jArray.length();i++) {
				Child child = new Child();
				JSONObject job2 = jArray.getJSONObject(i);
				child.setChildId(job2.getInt("childId"));
				child.setChildName(job2.getString("childName"));
				child.setChildSex("childSex");
				child.setChildAge(job2.getInt("childAge"));
				child.setChildGrade(job2.getInt("childGrade"));
				children.add(child);
			}
		}
		
		return children;
	}
	
	/**
	 * 根据用户id，更新孩子列表
	 * */
	public boolean updateKids(int id,String kidsList) {
		boolean b = false;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DBUtil.getConn();
			pstm =  con.prepareStatement("update user set kids = ? where userId = ?");
			pstm.setString(1,kidsList);
			pstm.setInt(2,id);
			int m = pstm.executeUpdate();
			if(0 != m) {
				b = true;
			}
			System.out.println("更新孩子列表成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	
	
}
