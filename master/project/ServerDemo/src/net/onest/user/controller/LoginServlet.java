package net.onest.user.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import net.onest.entity.User;
import net.onest.user.service.UserServiceImpl;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		// ��ȡ�ͻ��˴��͵��û��ǳƺ�����
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// ��ͻ��˴����û���Ϣ
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		User user2 = userServiceImpl.searchUser(username);
		System.out.println(user2.toString());
		// �ж������Ƿ���ȷ
		if (password.equals(user2.getPassword())) {// ������ȷ
			Gson gson = new Gson();
			String json = gson.toJson(user2);
			System.out.println(json);
			writer.write(json);
		}else {
			writer.write("false");
			System.out.println("false");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
