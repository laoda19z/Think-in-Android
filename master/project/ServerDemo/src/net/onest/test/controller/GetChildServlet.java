package net.onest.test.controller;

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

import net.onest.entity.Child;
import net.onest.test.service.ChildServiceImpl;

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
		//璁剧疆缂栫爜鏂瑰紡
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//鑾峰彇瀹㈡埛绔紶杩囨潵鐨勬暟鎹�
		InputStream in = request.getInputStream();
		PrintWriter writer = response.getWriter();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str = reader.readLine();
		if (str == null) {
			System.out.println("鑾峰彇鏁版嵁澶辫触");
		}else {
			System.out.println("瀛╁瓙缂栧彿锛�"+str);
			int childId = Integer.parseInt(str);
			ChildServiceImpl cs = new ChildServiceImpl();
			//鑾峰彇瀛╁瓙瀵硅薄鏁版嵁
			Child child = cs.getChildById(childId);
			//灏嗗瀛愮被鐨勬暟鎹簭鍒楀寲
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
