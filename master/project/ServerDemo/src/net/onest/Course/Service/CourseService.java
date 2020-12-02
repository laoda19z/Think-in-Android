package net.onest.zzz.sport.service;

import java.util.List;

import net.onest.zzz.sport.dao.CourseDao;
import net.onest.zzz.sport.entity.Course;


public class CourseService {
	public List<Course> listCourses(){
		CourseDao dao = new CourseDao();
		return dao.allCourses();
	}
}
