package net.onest.record.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.entity.ClockRecord;
import net.onest.record.service.ClockRecordService;


/**
 * Servlet implementation class SportImpressionsServlet
 */
@WebServlet("/SportImpressionsServlet")
public class SportImpressionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SportImpressionsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//查询出打卡记录中感想最多的记录
		InputStream in=request.getInputStream();
		OutputStream out=response.getOutputStream();
		
		BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str=buffer.readLine();
		System.out.println(str+"impression");
//		int userid=Integer.parseInt(str);
		int userid = 1;
		String sql="select * from mark where length(impression) = (select max(length(impression)) from mark) && child="+userid;
		System.out.println(sql);
		List<ClockRecord> list=new ArrayList<>();
		list=new ClockRecordService().getlist(sql);
		//转为json串发给客户端
		if(list!=null) {
			Gson gson=new Gson();
			String json=URLEncoder.encode(gson.toJson(list),"utf8");
			out.write(json.getBytes());
		}
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
