package net.onest.dynamic.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.dynamic.service.ChildServiceImpl;
import net.onest.entity.Child;


/**
 * Servlet implementation class GetChildServlet
 */
@WebServlet("/GetChildServlet")
public class GetChildServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetChildServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置编码方式
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//获取客户端传过来的数据
		InputStream in = request.getInputStream();
		PrintWriter writer = response.getWriter();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str = reader.readLine();
		if (str == null) {
			System.out.println("获取数据失败");
		}else {
			System.out.println("孩子编号："+str);
			int childId = Integer.parseInt(str);
			ChildServiceImpl cs = new ChildServiceImpl();
			//获取孩子对象数据
			Child child = cs.getChildById(childId);
			//将孩子类的数据序列化
			Gson gson = new Gson();
			String data = gson.toJson(child);
			System.out.println(data);
			writer.write(data);
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
