package net.onest.dynamic.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.dynamic.service.DynamicServiceImpl;
import net.onest.entity.Comment;
import net.onest.entity.Dynamic;
import net.onest.entity.User;
import net.onest.user.dao.UserDaoImpl;
import net.onest.user.service.UserServiceImpl;
import net.onest.util.Page;

/**
 * 分页展示动态
 */
@WebServlet("/ShowDynamicServlet")
public class ShowDynamicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowDynamicServlet() {
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
		String currpage = request.getParameter("page");
		int pageNum = 1,pageSize = 8;
		DynamicServiceImpl dynamicServiceImpl = new DynamicServiceImpl();
		if(currpage!=null && !"".equals(currpage)) {
			pageNum = Integer.parseInt(currpage);
		}
		Page<Dynamic> page = dynamicServiceImpl.showDynamicImpl(pageNum, pageSize);
		List<Dynamic> dynamics = page.getList();
		for(Dynamic d :dynamics) {
			System.out.println(d.getContent());
		}
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		String info = "";
		for(int i =0;i<dynamics.size();++i) {
			if(i!=dynamics.size()-1) {
				info = info + dynamics.get(i).getUserId()+",";
			}else {
				info = info + dynamics.get(i).getUserId();
			}
		}
		List<Integer> commentUserList = new ArrayList<>();
		for(Dynamic dyna:dynamics) {
			List<Comment> comments = dyna.getComment();
			for(Comment comm:comments) {
				commentUserList.add(comm.getPublisherId());
				commentUserList.add(comm.getReceiverId());
			}
		}
		String commuserinfo = "";
		for(int j = 0;j<commentUserList.size();++j) {
			if(j!=commentUserList.size()-1) {
				commuserinfo+=commentUserList.get(j)+",";
			}else {
				commuserinfo+=commentUserList.get(j);
			}
		}
		List<User> commUsers = userServiceImpl.searchCommentUserInfo(commuserinfo);
		List<User> users = userServiceImpl.searchTrendUserInfo(info);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String userstr = gson.toJson(users);
		String dynamicstr = gson.toJson(dynamics);
		String commuser = gson.toJson(commUsers);
		Map<String, String> map = new HashMap<String, String>();
		map.put("users", userstr);
		map.put("dynamic", dynamicstr);
		map.put("commusers",commuser);
		writer.write(gson.toJson(map));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
