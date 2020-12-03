package net.onest.mark.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.entity.MarkPicEntity;
import net.onest.entity.ReturnMarkPic;
import net.onest.util.MyUtil;


/**
 * Servlet implementation class markpic
 */
@WebServlet("/markpic")
public class markpic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public markpic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		InputStream in=request.getInputStream();
		BufferedReader reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
		PrintWriter writer=response.getWriter();
		//读取字符信息
		String str=reader.readLine();
		if(str!=null) {
			System.out.println("markPic中获得"+str);
			//首先先转为对象
			Gson gson=new Gson();
			MarkPicEntity picentity=gson.fromJson(str, MarkPicEntity.class);
			//通过对象查询
			String sqldate=picentity.getUsername()+"."+picentity.getChild()+"."+picentity.getDate();
			System.out.println("sqldate测试"+sqldate);
			String sql="select background,sporttype,sporttime,impression from mark where username='"+picentity.getUsername()+"' and child="+picentity.getChild()+" and markdate='"+picentity.getDate()+"'";
			MyUtil util=new MyUtil();
			try {
				ResultSet rs=util.queryDate(sql);
				//发送数据
				while(rs.next()) {
					String backpic=rs.getString(1);
					String type=rs.getString(2);
					int time=rs.getInt(3);
					String imp=rs.getString(4);
					String filepic="file/"+backpic;
					//转json串
					ReturnMarkPic returnpic=new ReturnMarkPic(filepic,type,time,imp);
					//设置加密
					String json = URLEncoder.encode(gson.toJson(returnpic),"utf-8");
					writer.write(json);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//返回数据
		}
		else {
			System.out.println("markpic为空");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
