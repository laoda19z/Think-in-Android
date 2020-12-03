package net.onest.mark.service;

import java.io.File;
import java.sql.SQLException;

import com.google.gson.Gson;

import net.onest.entity.TotalMark;
import net.onest.util.MyUtil;


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
		MyUtil util=new MyUtil();
		//��ѯ����
		String sql="select * from mark where username='"+totalmark.getUsername()+"' and child="+totalmark.getChild()+" and markdate='"+totalmark.getDate()+"'";
		try {
			if(util.isExist(sql)==true) {
				//�Ѿ��򿨹�
			}
			else {
				//û�д򿨹�
				String sql1="insert into mark(username,markdate,sporttype,sporttime,impression,background,child) values('"+totalmark.getUsername()+"','"+totalmark.getDate()+"','"+totalmark.getSporttype()+"',"+totalmark.getMinutes()+",'"+totalmark.getImpression()+"','"+totalmark.getPicname()+"',"+totalmark.getChild()+")";
				util.addDataToTable(sql1);
				
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
