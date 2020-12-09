package net.onest.dynamic.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onest.dynamic.service.DynamicServiceImpl;

/**
 * Servlet implementation class DeleteDynamicServlet
 */
@WebServlet("/DeleteDynamicServlet")
public class DeleteDynamicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteDynamicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String dynamic = request.getParameter("dynamicid");
		if(dynamic!=null && !"".equals(dynamic)) {
			int dynamicid = Integer.parseInt(dynamic);
			DynamicServiceImpl dynamicServiceImpl = new DynamicServiceImpl();
			boolean b = dynamicServiceImpl.deleteDynamicImpl(dynamicid);
			if(b) {
				writer.write("true");
			}else {
				writer.write("false");
			}
		}else {
			writer.write("false");
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
