package net.onest.dynamic.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.dynamic.service.DynamicServiceImpl;
import net.onest.entity.Dynamic;
import net.onest.entity.User;
import net.onest.user.service.UserServiceImpl;
import net.onest.util.Page;

/**
 * Servlet implementation class ShowOwnerDynamicServlet
 */
@WebServlet("/ShowOwnerDynamicServlet")
public class ShowOwnerDynamicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowOwnerDynamicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String currpage = request.getParameter("page");
		int userid = Integer.parseInt(request.getParameter("userid"));
		int pageNum = 1,pageSize = 5;
		DynamicServiceImpl dynamicServiceImpl = new DynamicServiceImpl();
		if(currpage!=null && !"".equals(currpage)) {
			pageNum = Integer.parseInt(currpage);
		}
		System.out.println(pageNum);
		Page<Dynamic> page = dynamicServiceImpl.showDynamicImpl(pageNum, pageSize,userid);
		List<Dynamic> dynamics = page.getList();
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		String info = "";
		for(int i =0;i<dynamics.size();++i) {
			if(i!=dynamics.size()-1) {
				info = info + dynamics.get(i).getUserId()+",";
			}else {
				info = info + dynamics.get(i).getUserId();
			}
		}
		System.out.println(info);
		if(!"".equals(info)) {
			List<User> users = userServiceImpl.searchTrendUserInfo(info);
			Gson gson = new Gson();
			String userstr = gson.toJson(users);
			String dynamicstr = gson.toJson(dynamics);
			Map<String, String> map = new HashMap<String, String>();
			map.put("users", userstr);
			map.put("dynamic", dynamicstr);
			System.out.println(gson.toJson(map));
			response.getWriter().write(gson.toJson(map));
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
