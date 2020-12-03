package net.onest.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MyUtil {
	// ����SqlServer��������Ϣ
				private String driver;
				private String connStr;
				private String user;
				private String pwd;

				private Connection conn = null;

				public MyUtil() {
					// ���������ļ�����ʼ��SqlServer��������
					try {
						loadDBProperty("DBConfig.properties");
					} catch (ClassNotFoundException | IOException | SQLException e) {
						e.printStackTrace();
					}
				}

				/**
				 * �������ݿ������ļ�
				 * 
				 * @param pFile
				 * @throws IOException
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				private void loadDBProperty(String pFile) throws IOException, ClassNotFoundException, SQLException {
					// ����Properties����
					Properties prop = new Properties();
					// ���������ļ�
					prop.load(MyUtil.class.getClassLoader().getResourceAsStream(pFile));
					driver = prop.getProperty("DRIVER");
					connStr = prop.getProperty("CONN_STR");
					user = prop.getProperty("USER");
					pwd = prop.getProperty("PWD");
				}

				// ��ȡ���Ӷ���
				private void connectToDB() throws SQLException, ClassNotFoundException {
					if (null == conn || conn.isClosed()) {
						Class.forName(driver);
						conn = DriverManager.getConnection(connStr, user, pwd);
					}
				}

				/**
				 * ��ѯ����
				 * 
				 * @param sql ��ѯ���ݵ�sql���
				 * @return ��ѯ�������ݼ�
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
					// ���ӵ����ݿ�
					connectToDB();
					Statement stmt = conn.createStatement();
					// ִ�в�ѯ
					ResultSet rs = stmt.executeQuery(sql);
					return rs;
				}

				/**
				 * �ж������Ƿ����
				 * 
				 * @param sql ��ѯ��sql���
				 * @return �����򷵻�true�����򷵻�false
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
					// ���ӵ����ݿ�
					connectToDB();
					Statement stmt = conn.createStatement();
					// ִ�в�ѯ
					ResultSet rs = stmt.executeQuery(sql);
					return rs.next();
				}

				/**
				 * ��������
				 * 
				 * @param sql ִ�в����sql���
				 * @return �����¼������
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				public int addDataToTable(String sql) throws ClassNotFoundException, SQLException {
					// ���ӵ����ݿ�
					connectToDB();
					Statement stmt = conn.createStatement();
					return stmt.executeUpdate(sql);
				}

				/**
				 * �޸Ļ�ɾ������
				 * 
				 * @param sql ��������SQL���
				 * @return �޸Ļ�ɾ���ļ�¼����
				 * @throws SQLException 
				 * @throws ClassNotFoundException 
				 */
				public int updateData(String sql) throws ClassNotFoundException, SQLException {
					// ���ӵ����ݿ�
					connectToDB();
					Statement stmt = conn.createStatement();
					return stmt.executeUpdate(sql);
				}

				/**
				 * �ر����ݿ�����
				 * 
				 * @throws SQLException
				 */
				public void closeDB() throws SQLException {
					if (null != conn && !conn.isClosed()) {
						conn.close();
					}
				}


}
