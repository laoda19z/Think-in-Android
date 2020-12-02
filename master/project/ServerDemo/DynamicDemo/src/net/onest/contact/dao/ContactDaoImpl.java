package net.onest.contact.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.onest.entity.Comment;
import net.onest.entity.Contact;
import net.onest.util.DBUtil;

public class ContactDaoImpl {
	/**
	 * 查找我的联系人好友
	 * @param userid
	 * @return
	 */
	public List<Contact> findMyContact(int userid){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Contact> lists = new ArrayList<>();
		try {
			conn = DBUtil.getConn();
			String sql = "select * from contact where userid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userid);
			rs = pstm.executeQuery();
			while(rs.next()) {
				Contact contact = new Contact();
				contact.setId(rs.getInt(1));
				contact.setUserid(rs.getInt(2));
				contact.setContactid(rs.getInt(3));		
				lists.add(contact);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return lists;
	}
	/**
	 * 添加联系人好友
	 * 添加好友应该是双向的，既数据库中应该添加两条数据
	 * @param 
	 * @return
	 */
	public boolean addContact(int userid,int contactid ) {
		boolean b = false;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConn();
			String sql = "";
			sql = "insert into contact(userid,contactid) values(?,?)";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userid);
			pstm.setInt(2, contactid);
			int m  = pstm.executeUpdate();
			pstm.setInt(2, userid);
			pstm.setInt(1, contactid);
			int n = pstm.executeUpdate();
			if(m!=0 && n!=0) {
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
