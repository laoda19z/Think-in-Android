package net.onest.mark.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.entity.DateMark;
import net.onest.entity.NeedSearchDate;
import net.onest.mark.service.DateMarkService;


/**
 * Servlet implementation class Servlet
 */
@WebServlet("/date")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Servlet() {
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
		if(str!=null&&!str.equals("")) {
			System.out.println("查询日期页更新"+str);
			//利用传进来的JSON串得到用户名，打卡年，打卡月
			Gson gson=new Gson();
			NeedSearchDate need=gson.fromJson(str,NeedSearchDate.class);
			//利用得到的参数进行查询
			DateMarkService service=new DateMarkService();
			List<DateMark> dateList=new ArrayList<>();
			dateList=service.SearchByMonthAndYear(need.getUsername(),need.getYear(),need.getMonth(),need.getChild());
			//将查询的结果变为JSON返回给客户端
			String jsonString=gson.toJson(dateList);
			System.out.println("返回日期的测试串"+jsonString);
			writer.write(jsonString);
		}else {
			System.out.println("测试");
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
