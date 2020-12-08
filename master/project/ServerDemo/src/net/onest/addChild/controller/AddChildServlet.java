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
		// ��ȡ�ͻ��˴��͵��û�id����Ӻ��ӵ���Ϣ
		String userId = request.getParameter("userId");
		String addName = request.getParameter("addName");
		String addSex = request.getParameter("addSex");
		String addGrade = request.getParameter("addGrade");
		String addAge = request.getParameter("addAge");
		
		System.out.println(userId+":"+addName+addAge+addSex+addGrade);
		//��ȡ��ǰ���û�id
		int UID = Integer.parseInt(userId);
		//ʵ������ǰ����ӵĺ��Ӷ���
		Child child = new Child();
		child.setChildAge(Integer.parseInt(addAge));
		child.setChildName(addName);
		child.setChildGrade(Integer.parseInt(addGrade));
		child.setChildSex(addSex);
		System.out.println(child.getChildName());
		//��ת���ɵĶ�����뵽���ݿ�
		AddChildService addChildService = new AddChildService();
		boolean b = addChildService.addChild(child);
		// ��ͻ��˴����û���Ϣ
		if(b) {
			//��ȡ������id
			int id = addChildService.findChildId(addName);
			child.setChildId(id);
			System.out.println(id);
			writer.write(id+"");
			//������ӵĺ��Ӹ��µ�
			UserServiceImpl userService = new UserServiceImpl();
			User user = userService.findUser(UID);
			List<Child> kids = user.getKids();
			kids.add(child);//���º�����ӵ����û��ĺ����б�������
			JSONObject job2 = new JSONObject();
			job2.put("kids",kids);
			String kidsToJson = job2.toString();
			//�������б�����ת����Json����Ȼ������û�id���뵽���ݿ���
			boolean m = userService.updateKids(UID, kidsToJson);
			if(m) {
				System.out.println("��Ӻ��ӳɹ�");
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
