package net.onest.contact.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.contact.service.ContactServiceImpl;
import net.onest.entity.Contact;
import net.onest.entity.User;
import net.onest.user.service.UserServiceImpl;

/**
 * Servlet implementation class SearchMyContactServlet
 */
@WebServlet("/SearchMyContactServlet")
public class SearchMyContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchMyContactServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String user = request.getParameter("userid");
		if(user!=null&&!"".equals(user)) {
			int userid = Integer.parseInt(user);
			ContactServiceImpl contactServiceImpl = new ContactServiceImpl();
			List<Contact> contacts = contactServiceImpl.searchMyContact(userid);
			String contactidList = "";
			for(int i =0;i<contacts.size();++i) {
				if(i!=contacts.size()-1) {
					contactidList = contactidList+contacts.get(i).getContactid()+",";
				}else {
					contactidList = contactidList+contacts.get(i).getContactid();
				}
			}
			UserServiceImpl userServiceImpl = new UserServiceImpl();
			Gson gson = new Gson();
			List<User> myContact = new ArrayList<>();
			String resStr = "";
			if(contactidList!="") {
				 myContact = userServiceImpl.searchTrendUserInfo(contactidList);
			}
			resStr= gson.toJson(myContact);
			response.getWriter().write(resStr);
			System.out.println(resStr);
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
