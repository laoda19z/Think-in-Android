package net.onest.dynamic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.onest.entity.Comment;
import net.onest.entity.Dynamic;
import net.onest.util.DBUtil;

public class DynamicDaoImpl {
	/**
	 * 动态的总数
	 * @return
	 */
	public int countByPage() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.getConn();
			String sql="select count(*) from dynamic";
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
	 * 查看个人动态的总数
	 * @return
	 */
	public int countOwnerByPage(int userid) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.getConn();
			String sql="select count(*) from dynamic where userid = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userid);
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
	 * 按时间排序分页展示所有动态
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<Dynamic> findByPage(int pageNum,int pageSize){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Dynamic> lists = new ArrayList<>();
		try {
			conn = DBUtil.getConn();
			String sql = "select * from dynamic order by time desc limit ?,?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, (pageNum-1)*pageSize);
			pstm.setInt(2, pageSize);
			rs = pstm.executeQuery();
			while(rs.next()) {
				Dynamic dynamic = new Dynamic();
				dynamic.setDynamicId(rs.getInt(1));
				dynamic.setUserId(rs.getInt(2));
				Timestamp timestamp = rs.getTimestamp(3);
				Date date = new Date(timestamp.getTime());
				dynamic.setTime(date);
				dynamic.setLocation(rs.getString(4));
				dynamic.setImg(rs.getString(5));
				dynamic.setContent(rs.getString(6));
				List<Comment> comments = findDynamicComment(rs.getInt(1));
				dynamic.setComment(comments);
				lists.add(dynamic);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return lists;
	}
	/**
	 * 按时间排序展示个人动态
	 * @param pageNum
	 * @param pageSize
	 * @param userid
	 * @return
	 */
	public List<Dynamic> findOwnerByPage(int pageNum,int pageSize,int userid){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Dynamic> lists = new ArrayList<>();
		try {
			conn = DBUtil.getConn();
			String sql = "select * from dynamic where userid = ? order by time desc limit ?,?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userid);
			pstm.setInt(2, (pageNum-1)*pageSize);
			pstm.setInt(3, pageSize);
			rs = pstm.executeQuery();
			while(rs.next()) {
				Dynamic dynamic = new Dynamic();
				dynamic.setDynamicId(rs.getInt(1));
				dynamic.setUserId(rs.getInt(2));
				Timestamp timestamp = rs.getTimestamp(3);
				Date date = new Date(timestamp.getTime());
				dynamic.setTime(date);
				dynamic.setLocation(rs.getString(4));
				dynamic.setImg(rs.getString(5));
				dynamic.setContent(rs.getString(6));
				List<Comment> comments = findDynamicComment(rs.getInt(1));
				dynamic.setComment(comments);
				lists.add(dynamic);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return lists;
	}
	/**
	 * 发布动态
	 * @param img
	 * @return
	 */
	public int publishDynamic(Dynamic dynamic) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int b = 0;
		try {
			conn = DBUtil.getConn();
			int userid = dynamic.getUserId();
			Date date = dynamic.getTime();
			Timestamp time = new Timestamp(date.getTime()); 
			String location = dynamic.getLocation();
			String img = dynamic.getImg();
			String content = dynamic.getContent(); 
			String sql = "";
			sql = "insert into dynamic(userId,time,location,img,content) values(?,?,?,?,?)";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, userid);
			pstm.setTimestamp(2, time);
			pstm.setString(3, location);
			pstm.setString(4,img);
			pstm.setString(5, content);
			b = pstm.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return b;
	}
	/**
	 * 查找某条动态的评论
	 * @param dynamicId
	 * @return
	 */
	public List<Comment> findDynamicComment(int dynamicId){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Comment> lists = new ArrayList<>();
		try {
			conn = DBUtil.getConn();
			String sql = "select * from comment where dynamicId = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, dynamicId);
			rs = pstm.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setCommentId(rs.getInt(1));
				comment.setDynamicId(rs.getInt(2));
				comment.setPublisherId(rs.getInt(3));
				comment.setReceiverId(rs.getInt(4));
				comment.setContent(rs.getString(5));
				lists.add(comment);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return lists;
	}
	/**
	 * 发布评论
	 * @param comment
	 * @return
	 */
	public int publishComment(Comment comment) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int b = 0;
		try {
			conn = DBUtil.getConn();
			int dynamicId = comment.getDynamicId();
			int publisherId = comment.getPublisherId();
			int receiverId = comment.getReceiverId();
			String content = comment.getContent();
			String sql = "";
			sql = "insert into comment(dynamicId,publisherId,receiverId,content) values(?,?,?,?)";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, dynamicId);
			pstm.setInt(2, publisherId);
			pstm.setInt(3, receiverId);
			pstm.setString(4, content);
			b = pstm.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, pstm, conn);
		}
		return b;
	}
}
