package net.onest.contact.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onest.contact.service.ContactServiceImpl;
import net.onest.entity.Contact;

/**
 * Servlet implementation class DeleteContactServlet
 */
@WebServlet("/DeleteContactServlet")
public class DeleteContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteContactServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String struserid = request.getParameter("userid");
		String strcontactid = request.getParameter("contactid");
		if(strcontactid!=""&&strcontactid!=null&&struserid!=""&&struserid!=null) {
			int userid = Integer.parseInt(struserid);
			int contactid = Integer.parseInt(strcontactid);
			Contact contact = new Contact();
			contact.setUserid(userid);
			contact.setContactid(contactid);
			ContactServiceImpl contactServiceImpl = new ContactServiceImpl();
			boolean b = contactServiceImpl.deleteContact(contact);
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
