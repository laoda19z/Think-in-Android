package net.onest.test.controller;
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
import net.onest.entity.Report;
import net.onest.test.service.ReportServiceImpl;

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
		//璁剧疆缂栫爜鏂瑰紡
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//鑾峰彇瀹㈡埛绔紶杩囨潵鐨勬暟鎹�
		InputStream in = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str = reader.readLine();
		if (str == null) {
			System.out.println("鑾峰彇鏁版嵁澶辫触");
		}else {
			System.out.println("瀹㈡埛绔暟鎹細"+str);
			//鍒涘缓杈撳嚭娴�
			PrintWriter writer = response.getWriter();
			//灏嗚幏鍙栧埌鐨勬暟鎹啓杩汮son鏁版嵁涓�
			JsonObject jsonObject =(JsonObject) new JsonParser().parse(str).getAsJsonObject();
			//鍒涘缓鏈嶅姟瀵硅薄
			ReportServiceImpl rs = new ReportServiceImpl();
			//鍒涘缓Gson瀵硅薄搴忓垪鍖栨姤鍛婄被
			Gson gson = new Gson();
			String path = getServletContext().getRealPath("/");
			String filePath = path+"/data/data.xls";
			try {
				Report report = rs.createReport(jsonObject, filePath);
				String reportString = gson.toJson(report);
				//杩斿洖鏁版嵁
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
