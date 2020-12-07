package net.onest.Course.Service;
import java.util.List;
import net.onest.entity.*;
import net.onest.Course.Dao.*;



public class CourseService {
	public List<Course> listCourses(){
		CourseDao dao = new CourseDao();
		return dao.allCourses();
	}
}
