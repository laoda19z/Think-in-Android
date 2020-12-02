package net.onest.zzz.sport.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import net.onest.zzz.sport.entity.Course;
import net.onest.zzz.sport.service.CourseService;



/**
 * Servlet implementation class CourseServlet
 */
@WebServlet("/Course")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		CourseService cs = new CourseService();
		List<Course> courses = cs.listCourses();
		String json = toJson(courses);
		System.out.println(json);
		OutputStream out = response.getOutputStream();
		out.write(json.getBytes());
		out.flush();
		out.close();
	}
	private String toJson(List<Course> list) {
		String json = null;
		JSONArray jArray = new JSONArray();
		for(Course course:list) {
			JSONObject jCourse = new JSONObject();
			jCourse.put("id",course.getCourseId());
			jCourse.put("name",course.getCourseName());
			jCourse.put("num",course.getCoursePeoNum());
			jCourse.put("content",course.getCourseContent());
			jCourse.put("time",course.getCourseTime());
			jCourse.put("type",course.getCourseType());
			jArray.put(jCourse);
		}
		JSONObject jCourses = new JSONObject();
		jCourses.put("courses",jArray);
		json = jCourses.toString();
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