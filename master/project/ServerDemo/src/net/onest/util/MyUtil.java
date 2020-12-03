package net.onest.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MyUtil {
	// 连接SqlServer的配置信息
				private String driver;
				private String connStr;
				private String user;
				private String pwd;

				private Connection conn = null;

				public MyUtil() {
					// 加载配置文件，初始化SqlServer配置属性
					try {
						loadDBProperty("DBConfig.properties");
					} catch (ClassNotFoundException | IOException | SQLException e) {
						e.printStackTrace();
					}
				}

				/**
				 * 加载数据库配置文件
				 * 
				 * @param pFile
				 * @throws IOException
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				private void loadDBProperty(String pFile) throws IOException, ClassNotFoundException, SQLException {
					// 创建Properties对象
					Properties prop = new Properties();
					// 加载配置文件
					prop.load(MyUtil.class.getClassLoader().getResourceAsStream(pFile));
					driver = prop.getProperty("DRIVER");
					connStr = prop.getProperty("CONN_STR");
					user = prop.getProperty("USER");
					pwd = prop.getProperty("PWD");
				}

				// 获取连接对象
				private void connectToDB() throws SQLException, ClassNotFoundException {
					if (null == conn || conn.isClosed()) {
						Class.forName(driver);
						conn = DriverManager.getConnection(connStr, user, pwd);
					}
				}

				/**
				 * 查询数据
				 * 
				 * @param sql 查询数据的sql语句
				 * @return 查询到的数据集
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
					// 连接到数据库
					connectToDB();
					Statement stmt = conn.createStatement();
					// 执行查询
					ResultSet rs = stmt.executeQuery(sql);
					return rs;
				}

				/**
				 * 判断数据是否存在
				 * 
				 * @param sql 查询的sql语句
				 * @return 存在则返回true，否则返回false
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
					// 连接到数据库
					connectToDB();
					Statement stmt = conn.createStatement();
					// 执行查询
					ResultSet rs = stmt.executeQuery(sql);
					return rs.next();
				}

				/**
				 * 插入数据
				 * 
				 * @param sql 执行插入的sql语句
				 * @return 插入记录的行数
				 * @throws ClassNotFoundException
				 * @throws SQLException
				 */
				public int addDataToTable(String sql) throws ClassNotFoundException, SQLException {
					// 连接到数据库
					connectToDB();
					Statement stmt = conn.createStatement();
					return stmt.executeUpdate(sql);
				}

				/**
				 * 修改或删除数据
				 * 
				 * @param sql 待操作的SQL语句
				 * @return 修改或删除的记录行数
				 * @throws SQLException 
				 * @throws ClassNotFoundException 
				 */
				public int updateData(String sql) throws ClassNotFoundException, SQLException {
					// 连接到数据库
					connectToDB();
					Statement stmt = conn.createStatement();
					return stmt.executeUpdate(sql);
				}

				/**
				 * 关闭数据库连接
				 * 
				 * @throws SQLException
				 */
				public void closeDB() throws SQLException {
					if (null != conn && !conn.isClosed()) {
						conn.close();
					}
				}


}
