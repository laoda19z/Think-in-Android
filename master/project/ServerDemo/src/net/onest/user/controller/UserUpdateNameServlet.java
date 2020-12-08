package net.onest.user.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onest.user.service.UserServiceImpl;

/**
 * Servlet implementation class UserUpdateNameServlet
 */
@WebServlet("/UserUpdateName")
public class UserUpdateNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserUpdateNameServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		// 获取客户端传送的用户id和新电话号码
		String userId = request.getParameter("userId");
		String newName = request.getParameter("newName");
		System.out.println(userId+":"+newName);
		// 向客户端传送用户信息
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		boolean b = userServiceImpl.updateName(Integer.parseInt(userId), newName);
		if (b) {
			writer.write("OK");
			System.out.println("修改成功！");
		}else {
			writer.write("false");
			System.out.println("修改失败！");
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
