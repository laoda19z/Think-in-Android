package net.onest.mark.service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import net.onest.entity.TotalMark;
import net.onest.util.DBUtil;


public class UploadService {
	private String json;

	public UploadService(String json) {
		super();
		this.json = json;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	//��������Ĵ�
	public boolean updateJson(String json) {
		Gson gson=new Gson();
		TotalMark totalmark=gson.fromJson(json,TotalMark.class);
		//ͨ��mark����������
//		MyUtil util=new MyUtil();
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		//��ѯ����
		String sql="select * from mark where username='"+totalmark.getUsername()+"' and child="+totalmark.getChild()+" and markdate='"+totalmark.getDate()+"'";
		try {
			conn = DBUtil.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			if(rs.next()==true) {
				//�Ѿ��򿨹�
			}
			else {
				//û�д򿨹�
				String sql1="insert into mark(username,markdate,sporttype,sporttime,impression,background,child) values('"+totalmark.getUsername()+"','"+totalmark.getDate()+"','"+totalmark.getSporttype()+"',"+totalmark.getMinutes()+",'"+totalmark.getImpression()+"','"+totalmark.getPicname()+"',"+totalmark.getChild()+")";
				pstm = conn.prepareStatement(sql1);
				int n = pstm.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
