package net.onest.record.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbUtil {
		private static String driver;
		private static String conn_str;
		private static String user;
		private static String pwd;
		private static Connection conn;
		private static DbUtil dbUtil;
		//��̬�����
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
			//��ȡ�����ļ���Ӧ��������
			InputStream pIn = DbUtil.class.getClassLoader().getResourceAsStream(pFile);
			//���������ļ�
			prop.load(pIn);
			//�����ݿ����Ӳ�����ص����Ը�ֵ
			driver = prop.getProperty("driver");
			conn_str = prop.getProperty("connStr");
			user = prop.getProperty("user");
			pwd = prop.getProperty("pwd");
		}
		
		private static void getConnection() throws SQLException, ClassNotFoundException {
			if(null == conn) {
				Class.forName(driver);
				conn = DriverManager.getConnection(conn_str, user, pwd);
			}
		}
		
		private DbUtil() throws ClassNotFoundException, SQLException{}
		
		//�ṩ��ȡ��ǰ��Ķ���Ľӿ�
		public static DbUtil getInstance() throws ClassNotFoundException, SQLException {
			if(null == dbUtil) {
				dbUtil = new DbUtil();
			}
			return dbUtil;
		}
	
		//�ر����ݿ�����
		public void closeDB() throws SQLException {
			if(null != conn && !conn.isClosed()) {
				conn.close();
			}
		}
		
		// ��ȡ���ݿ�����Ӷ���
		private void connectToDB() throws SQLException, ClassNotFoundException {
			if (null == conn || conn.isClosed()) {
				Class.forName(driver);
				conn = DriverManager.getConnection(conn_str, user, pwd);
			}
		}
		
		//��ѯ���ݣ����ز���ֵ
		public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			ResultSet rs = psm.executeQuery();
			return rs.next();
		}
		
		//���
		public int add(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			return psm.executeUpdate();
		}
		
		//��ѯ���ݣ��������ݼ�
		public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			ResultSet rs =psm.executeQuery();
			return rs;
		}
		
		//�������ݣ����ظ�������
		public int updateData(String sql) throws ClassNotFoundException, SQLException {
			connectToDB();
			PreparedStatement psm= conn.prepareStatement(sql);
			return psm.executeUpdate();
		}
}
