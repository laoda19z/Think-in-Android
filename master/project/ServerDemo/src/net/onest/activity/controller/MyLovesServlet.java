package net.onest.activity.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import net.onest.activity.service.MyLoveServices;
import net.onest.entity.ParentChildActivities;
import net.onest.entity.ParentChildActivitiesInfo;

/**
 * Servlet implementation class MyLovesServlet
 */
@WebServlet("/MyLovesServlet")
public class MyLovesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyLovesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MyLoveServices myLoveDaoImpl = new MyLoveServices();
		List<ParentChildActivities> activities = myLoveDaoImpl.listTypes();
		
		ParentChildActivitiesInfo activitiesInfo = new ParentChildActivitiesInfo();
		activitiesInfo.setActivities(activities);
		System.out.println("myloveservlet");
		//把对象转换成JSON串
		String json = URLEncoder.encode(convertToJson(activitiesInfo),"utf8");
		//把JSON返回客户端
		OutputStream out = response.getOutputStream();
		out.write(json.getBytes());
		out.flush();
		out.close();
	}

	private String convertToJson(ParentChildActivitiesInfo activitiesInfo) {
		// TODO Auto-generated method stub
		String json = null;
		List<ParentChildActivities> activities = activitiesInfo.getActivities();
		JSONArray jArray = new JSONArray();
		System.out.println("myloveservlet");
		for(ParentChildActivities activity : activities) {
			JSONObject jActivity = new JSONObject();
			jActivity.put("name", activity.getActivityName());
			jActivity.put("id", activity.getActivityId());
			jActivity.put("time", activity.getActivityTime());
			jActivity.put("img", activity.getActivityImg());
			jActivity.put("peoNum", activity.getActivityPeoNum());
			jActivity.put("content", activity.getActivityContent());
			jActivity.put("district", activity.getActivityDistrict());
			jActivity.put("pay", activity.getActivityPay());
			
			jArray.put(jActivity);
		}
		JSONObject jActivities = new JSONObject();
		jActivities.put("myLoves", jArray);
		json = jActivities.toString();
		
		return json;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
