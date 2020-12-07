package net.onest.record.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.record.service.AssessmentReportService;
import net.onest.record.service.ClockRecordService;


/**
 * Servlet implementation class FirstServlet
 */
@WebServlet("/FirstServlet")
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FirstServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		InputStream in=request.getInputStream();
		OutputStream out=response.getOutputStream();
		
		BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str=buffer.readLine();
		Gson gson=new Gson();
		int childId=0;
		if(str!=null) {
			childId=Integer.parseInt(str);
		}
		String sql="select * from mark where childId = "+childId;
		System.out.println(sql);
		String sql1="select * from report where childId = "+childId;
		
		boolean b=new ClockRecordService().isExist(sql);
		boolean c=new AssessmentReportService().isExist(sql1);
		if(b&&c) {
			out.write("OK".getBytes());
		}else {
			out.write("NO".getBytes());
		}
		out.flush();
		in.close();
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
