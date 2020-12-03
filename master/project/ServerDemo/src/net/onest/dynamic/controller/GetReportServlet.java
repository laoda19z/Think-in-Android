package net.onest.dynamic.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import jxl.read.biff.BiffException;
import net.onest.dynamic.service.ReportServiceImpl;
import net.onest.entity.Report;

/**
 * Servlet implementation class GetReportServlet
 */
@WebServlet("/GetReportServlet")
public class GetReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetReportServlet() {
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str = reader.readLine();
		if (str == null) {
			System.out.println("获取数据失败");
		}else {
			System.out.println("客户端数据："+str);
			//创建输出流
			PrintWriter writer = response.getWriter();
			//将获取到的数据写进Json数据串
			JsonObject jsonObject =(JsonObject) new JsonParser().parse(str).getAsJsonObject();
			//创建服务对象
			ReportServiceImpl rs = new ReportServiceImpl();
			//创建Gson对象序列化报告类
			Gson gson = new Gson();
			String path = getServletContext().getRealPath("/");
			String filePath = path+"/data.xls";
			try {
				Report report = rs.createReport(jsonObject, filePath);
				String reportString = gson.toJson(report);
				//返回数据
				writer.write(reportString);
			} catch (BiffException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
