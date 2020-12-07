package net.onest.contact.controller;

import java.io.IOException;
import java.io.PrintWriter;

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
 * 锟斤拷锟斤拷锟较碉拷锟�
 * 
 */
@WebServlet("/AddContactServlet")
public class AddContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddContactServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		String name = request.getParameter("contactusername");
		int userid = Integer.parseInt(request.getParameter("userid"));
		System.out.println("name:"+name+"userid:"+userid);
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		User user = userServiceImpl.searchUser(name);
		int contactid = userServiceImpl.searchUserId(name);
		if(contactid!=0) {
			ContactServiceImpl contactServiceImpl = new ContactServiceImpl();
			boolean b = contactServiceImpl.addContact(userid, contactid);
			if(b) {
				writer.write("OK"+"|"+contactid+"|"+user.getHeadImg());
			}else {
				writer.write("FALSE");
			}
		}else {
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
