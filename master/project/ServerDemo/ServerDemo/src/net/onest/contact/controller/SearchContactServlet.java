package net.onest.contact.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.contact.service.ContactServiceImpl;
import net.onest.entity.User;

/**
 * Servlet implementation class SearchContactServlet
 * 鎼滅储鑱旂郴浜�
 */
@WebServlet("/SearchContactServlet")
public class SearchContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchContactServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String contactName = request.getParameter("contactName");
		ContactServiceImpl contactServiceImpl = new ContactServiceImpl();
	    User user = contactServiceImpl.searchContact(contactName);
	    if(user.getUserId()==0) {
	    	response.getWriter().write("false");
	    }else {
	    	Gson gson = new Gson();
		    String result = gson.toJson(user);
		    response.getWriter().write(result);
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
