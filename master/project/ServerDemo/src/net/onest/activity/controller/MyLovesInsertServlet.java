package net.onest.activity.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onest.util.DBUtil;
import net.onest.util.NewDbUtil;



/**
 * Servlet implementation class MyLovesInsertServlet
 */
@WebServlet("/MyLovesInsertServlet")
public class MyLovesInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyLovesInsertServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 收藏部分的更新数据库
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "utf-8"));
        String result = reader.readLine();//得到的AS传来的对象字符串
        reader.close();
        System.out.println(result);
        if(result != null) {
	        try {
	        	//通过传来的ID查找，并将数据存入love表
//	        	NewDbUtil util = new NewDbUtil();
	        	Connection con = null;
        		PreparedStatement pstm = null;
        		ResultSet rs = null;
        		int activityId = 0;
        		String activityName = "";
        		String activityTime = "";
        		int  activityPeoNum = 0;
        		String activityContent = "";
        		String activityPay = "";
        		String activityDistrict = "";
        		String activityImg = "";
   
    			con = DBUtil.getConn();
    			pstm = con.prepareStatement("select * from activity where activityId = '" +result+ "'");
    			rs = pstm.executeQuery();
    			while(rs.next()) {		
    				activityId = rs.getInt(1);
    				activityName = rs.getString(2);
    				activityTime = rs.getString(3);
    				activityPeoNum = rs.getInt(4);
    				activityContent = rs.getString(5);
    				activityPay = rs.getString(6);
    				activityDistrict = rs.getString(7);
    				activityImg = rs.getString(8);
    			}
				System.out.println("111111111111111111111111");
				String sql = "insert into activity_love(activityId,activityName,activityTime,activityPeoNum,activityContent,activityPay,activityDistrict,activityImg)"
											+ " values('"+activityId+"','"+activityName+"','"+activityTime+"','"+activityPeoNum+"','"+activityContent+"','"+activityPay+"','"+activityDistrict+"','"+activityImg+"')";//sql中的更新语句
//				util.updateData(sql);
				pstm.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        OutputStream out = response.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
        		new OutputStreamWriter(out, "utf-8"));
        String json = "收藏传输完成！";
        out.write(json.getBytes());
        out.flush();
		out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
