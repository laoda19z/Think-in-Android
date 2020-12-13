package net.onest.test.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.entity.AssessmentReport;
import net.onest.test.service.ReportServiceImpl;

/**
 * Servlet implementation class GetReportByReportId
 */
@WebServlet("/GetReportByReportId")
public class GetReportByReportId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetReportByReportId() {
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
		//获取客户端数据
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str = reader.readLine();
		reader.close();
		if (str == null) {
			System.out.println("GetReportByReportId:"+"获取客户端数据失败");
		}else {
			int reportId = Integer.parseInt(str);
			//根据孩子ID获取多个报告
			ReportServiceImpl rs = new ReportServiceImpl();
			AssessmentReport report = rs.getAssessmentReportByReportId(reportId);
			//转换为Json字符对象
			Gson gson = new Gson();
			String reportsString = gson.toJson(report);
			//返回至客户端
			PrintWriter writer = response.getWriter();
			writer.write(reportsString);
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
