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
 * Servlet implementation class UserUpdateEmailServlet
 */
@WebServlet("/UserUpdateEmail")
public class UserUpdateEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserUpdateEmailServlet() {
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
		// ��ȡ�ͻ��˴��͵��û�id���µ绰����
		String userId = request.getParameter("userId");
		String newEmail = request.getParameter("newEmail");
		System.out.println(userId+":"+newEmail);
		// ��ͻ��˴����û���Ϣ
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		boolean b = userServiceImpl.updateEmail(Integer.parseInt(userId), newEmail);
		if (b) {
			writer.write("OK");
			System.out.println("�޸ĳɹ���");
		}else {
			writer.write("false");
			System.out.println("�޸�ʧ�ܣ�");
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
