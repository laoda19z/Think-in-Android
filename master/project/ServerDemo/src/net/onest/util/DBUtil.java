package net.onest.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @clsName Dbutil
 * @descripition 数据库工具类，用手打开或关闭连接
 * @author dell
 *
 */
public class DBUtil {
	
	static {//只在加载时执行一次
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取数据库连接
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
	 * 关闭数据库连接
	 * @param rs  Resultset结果集
	 * @param pste 
	 * @param conn  Connection数据库连接对象
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
