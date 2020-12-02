package net.onest.dynamic.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.dynamic.service.DynamicServiceImpl;
import net.onest.entity.Comment;

/**
 * 发表评论
 */
@WebServlet("/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		//获取数据
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		String json = reader.readLine();
		Gson gson = new Gson();
		Comment comment = gson.fromJson(json, Comment.class);
		System.out.println(json);
		DynamicServiceImpl dynamicServiceImpl = new DynamicServiceImpl();
		boolean b = dynamicServiceImpl.publishCommentImpl(comment);
		if(b) {
			writer.write("OK");
		}else{
			writer.write("FALSE");
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
