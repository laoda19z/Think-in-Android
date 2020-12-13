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
 * Servlet implementation class GetReportsByChildIdServlet
 */
@WebServlet("/GetReportsByChildIdServlet")
public class GetReportsByChildIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetReportsByChildIdServlet() {
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
		PrintWriter writer = response.getWriter();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str = reader.readLine();
		if (str == null) {
			System.out.println("GetReportsByChildIdServlet:"+"获取客户端数据失败");
		}else {
			System.out.println("GetReportsByChildIdServlet:"+str);
			int childId = Integer.parseInt(str);
			//根据孩子ID获取多个报告
			ReportServiceImpl rs = new ReportServiceImpl();
			List<AssessmentReport> reports = rs.getReportsByChildId(childId);
			//转换为Json字符对象
			Gson gson = new Gson();
			String reportsString = gson.toJson(reports);
			//返回至客户端
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
