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
 * Servlet implementation class MyLovesDeleteServlet
 */
@WebServlet("/MyLovesDeleteServlet")
public class MyLovesDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyLovesDeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "utf-8"));
        String result = reader.readLine();//得到的AS传来的对象字符串
        PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;
		int activityId = 0;
		con = DBUtil.getConn();
        reader.close();
        System.out.println("delete" + result);
        if(result != null) {
	        try {
	        	//通过传来的ID查找，并将数据存入love表
//	        	NewDbUtil util = new NewDbUtil();
				String sql = "delete from activity_love where activityId = '"+result+"'";
				pstm = con.prepareStatement(sql);
				pstm.executeUpdate();
//				util.updateData(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        OutputStream out = response.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
        		new OutputStreamWriter(out, "utf-8"));
        String json = "删除完成！";
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
