package net.onest.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @clsName Dbutil
 * @descripition ���ݿ⹤���࣬���ִ򿪻�ر�����
 * @author dell
 *
 */
public class DBUtil {
	
	static {//ֻ�ڼ���ʱִ��һ��
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ���ݿ�����
	 * @return
	 */
	public static Connection getConn() {
		try {
			return (Connection) DriverManager.getConnection("jdbc:mysql://39.103.131.145:3306/ledongyun?useUnicode=true&characterEncoding=UTF-8&useSSL=false","root","mysql");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * �ر����ݿ�����
	 * @param rs  Resultset�����
	 * @param pste 
	 * @param conn  Connection���ݿ����Ӷ���
	 */
	public static void close(ResultSet rs,PreparedStatement pste,Connection conn) {
		
			try {
				if(rs!=null)
					rs.close();
				if(pste!=null)
					pste.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
}
