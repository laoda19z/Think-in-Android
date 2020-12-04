package net.onest.test.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonObject;

import net.onest.entity.Report;
import net.onest.test.service.ReportServiceImpl;

/**
 * Servlet implementation class GetScoreServlet
 */
@WebServlet("/GetScoreServlet")
public class GetScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetScoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String sex = request.getParameter("sex");
		String height = request.getParameter("height");
		String weight = request.getParameter("weight");
		String sitandreach = request.getParameter("sitandreach");
		String kicknumber = request.getParameter("kicknumber");
		System.out.println(sex+height+weight+sitandreach+kicknumber);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", "1");
		jsonObject.addProperty("xb", sex);
		jsonObject.addProperty("hw", height+"&"+weight);
		jsonObject.addProperty("tqq", sitandreach);
		jsonObject.addProperty("tjz", kicknumber);
		ReportServiceImpl rs = new ReportServiceImpl();
		try {
			String path = getServletContext().getRealPath("/");
			String filePath = path+"/data.xls";
			Report report = rs.createReport(jsonObject,filePath);
			PrintWriter writer = response.getWriter();
			writer.write(report.toString());
			if (rs.addReport(report)) {
				System.out.println("添加成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
