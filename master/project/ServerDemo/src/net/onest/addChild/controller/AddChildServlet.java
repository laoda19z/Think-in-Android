package net.onest.addChild.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import net.onest.addChild.service.AddChildService;
import net.onest.entity.Child;
import net.onest.entity.User;
import net.onest.user.service.UserServiceImpl;

/**
 * Servlet implementation class AddChildServlet
 */
@WebServlet("/AddChild")
public class AddChildServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddChildServlet() {
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
		// 获取客户端传送的用户id和添加孩子的信息
		String userId = request.getParameter("userId");
		String addName = request.getParameter("addName");
		String addSex = request.getParameter("addSex");
		String addGrade = request.getParameter("addGrade");
		String addAge = request.getParameter("addAge");
		
		System.out.println(userId+":"+addName+addAge+addSex+addGrade);
		//获取当前的用户id
		int UID = Integer.parseInt(userId);
		//实例化当前所添加的孩子对象
		Child child = new Child();
		child.setChildAge(Integer.parseInt(addAge));
		child.setChildName(addName);
		child.setChildGrade(Integer.parseInt(addGrade));
		child.setChildSex(addSex);
		System.out.println(child.getChildName());
		//将转化成的对象插入到数据库
		AddChildService addChildService = new AddChildService();
		boolean b = addChildService.addChild(child);
		// 向客户端传送用户信息
		if(b) {
			//获取到孩子id
			int id = addChildService.findChildId(addName);
			child.setChildId(id);
			System.out.println(id);
			writer.write(id+"");
			//将新添加的孩子更新到
			UserServiceImpl userService = new UserServiceImpl();
			User user = userService.findUser(UID);
			List<Child> kids = user.getKids();
			kids.add(child);//将新孩子添加到该用户的孩子列表属性中
			JSONObject job2 = new JSONObject();
			job2.put("kids",kids);
			String kidsToJson = job2.toString();
			//将孩子列表属性转化成Json串，然后根据用户id存入到数据库中
			boolean m = userService.updateKids(UID, kidsToJson);
			if(m) {
				System.out.println("添加孩子成功");
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
