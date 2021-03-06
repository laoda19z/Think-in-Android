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
 * Servlet implementation class UserUpdateInfoServlet
 */
@WebServlet("/UserUpdateInfo")
public class UserUpdateInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserUpdateInfoServlet() {
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
		String newRealName = request.getParameter("newRealName");
		String newSex = request.getParameter("newSex");
		String newPhone = request.getParameter("newPhone");
		String newEmail = request.getParameter("newEmail");
		System.out.println(userId+":"+newRealName+":"+newSex+":"+newPhone+":"+newEmail);
		// 向客户端传送用户信息
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		boolean b = userServiceImpl.updateInfo(Integer.parseInt(userId), newRealName, newSex, newPhone, newEmail);
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
