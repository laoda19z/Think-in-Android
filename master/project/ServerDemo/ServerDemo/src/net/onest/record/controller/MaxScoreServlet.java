package net.onest.record.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.entity.AssessmentReport;
import net.onest.record.service.AssessmentReportService;



/**
 * Servlet implementation class MaxScoreServlet
 */
@WebServlet("/MaxScoreServlet")
public class MaxScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MaxScoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//查询出测评记录中综合成绩最好的记录
		InputStream in=request.getInputStream();
		OutputStream out=response.getOutputStream();
		
		BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str=buffer.readLine();
		int childId=Integer.parseInt(str);
		System.out.println(str+"maxscore");
		
		String sql="select * from report where overallscore in(select max(overallscore) from report) and childId="+childId;
		System.out.println(sql);
		List<AssessmentReport> list=new AssessmentReportService().getlist(sql);
		if(list!=null) {
			Gson gson=new Gson();
			String json=gson.toJson(list);
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
