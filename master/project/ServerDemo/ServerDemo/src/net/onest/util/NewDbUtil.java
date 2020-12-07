package net.onest.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class NewDbUtil {
		private static String driver;
		private static String conn_str;
		private static String user;
		private static String pwd;
		private static Connection conn;
		private static NewDbUtil dbUtil;
		//静态代码块
		static {
			try {
				loadProperties("DBConfig.properties");
				getConnection();
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
		}

		private static void loadProperties(String pFile) throws IOException {
			Properties prop = new Properties();
			//获取配置文件对应的输入流
			InputStream pIn = DBUtil.class.getClassLoader().getResourceAsStream(pFile);
			//加载配置文件
			prop.load(pIn);
			//给数据库连接操作相关的属性赋值
			driver = prop.getProperty("DRIVER");
			conn_str = prop.getProperty("CONN_STR");
			user = prop.getProperty("USER");
			pwd = prop.getProperty("PWD");
			
			
		}
		
		private static void getConnection() throws SQLException, ClassNotFoundException {
			if(null == conn) {
				Class.forName(driver);
				conn = DriverManager.getConnection(conn_str, user, pwd);
			}
		}
		
		//1
		public NewDbUtil() throws ClassNotFoundException, SQLException{}
		
		//提供获取当前类的对象的接口
		public static NewDbUtil getInstance() throws ClassNotFoundException, SQLException {
			if(null == dbUtil) {
				dbUtil = new NewDbUtil();
			}
			return dbUtil;
		}
	
		public void closeDB() throws SQLException {
			if(null != conn && !conn.isClosed()) {
				conn.close();
			}
		}
		
		// 获取数据库的连接对象
		private void connectToDB() throws SQLException, ClassNotFoundException {
			if (null == conn || conn.isClosed()) {
				Class.forName(driver);
				conn = DriverManager.getConnection(conn_str, user, pwd);
			}
		}
		
		//查询数据，返回布尔值
		public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			ResultSet rs = psm.executeQuery();
			return rs.next();
		}
		
		//添加
		public int add(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			return psm.executeUpdate();
		}
		
		//查询数据，返回数据集
		public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			ResultSet rs =psm.executeQuery();
			return rs;
		}
		
		//更新数据，返回更新条数
		public int updateData(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			return psm.executeUpdate();
		}
}
