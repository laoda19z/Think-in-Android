package com.zzz.cakedemo.cake.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zzz.cakedemo.service.UserPageServiceImpl;

/**
 * Servlet implementation class DeleteCakeServlet
 */
@WebServlet("/DeleteUserPageServlet")
public class DeleteUserPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("userid");
		if(id!=null&&!"".equals(id)) {
			int userid = Integer.parseInt(id);
			UserPageServiceImpl userServiceImpl = new UserPageServiceImpl();
			boolean b = userServiceImpl.deleteUserById(userid);
		}
		request.getRequestDispatcher("UserPageServlet").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
