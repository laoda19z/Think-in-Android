package net.onest.mark.controller;

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

import net.onest.entity.JudgeMarkStatus;
import net.onest.entity.Status;
import net.onest.mark.service.MarkStatusService;


/**
 * Servlet implementation class judge
 */
@WebServlet("/judge")
public class judge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public judge() {
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
			System.out.println("查询今日打卡Servlet"+str);
			Gson gson=new Gson();
			JudgeMarkStatus judge=gson.fromJson(str, JudgeMarkStatus.class);
			MarkStatusService service=new MarkStatusService(judge.getUsername(),judge.getYear(),judge.getMonth(),judge.getChild(),judge.getDay());
			try {
				Boolean boo=service.JudgeStatus(judge.getUsername(),judge.getChild(), judge.getYear(), judge.getMonth(), judge.getDay());
				Status status=new Status(boo);
				String json=gson.toJson(status);
				System.out.println("测试的今日是否打卡"+json);
				writer.write(json);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else {
			System.out.println("judge Servlet得到为空");
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
