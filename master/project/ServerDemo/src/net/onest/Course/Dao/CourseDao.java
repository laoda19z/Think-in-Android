package net.onest.zzz.sport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.onest.zzz.sport.entity.Course;
import net.onest.zzz.sport.util.DbUtil;


public class CourseDao {
	public List<Course> allCourses(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Course> courses = new ArrayList<Course>();
		try {
			con = DbUtil.getCon();
			pstm = con.prepareStatement("select * from course");
			rs = pstm.executeQuery();
			while(rs.next()) {
				Course course = new Course();
				course.setCourseId(rs.getInt(1));
				course.setCourseName(rs.getString(2));
				course.setCourseContent(rs.getString(3));
				course.setCourseTime(rs.getString(4));
				course.setCoursePeoNum(rs.getInt(5));
				course.setCourseType(rs.getString(6));
				courses.add(course);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courses;
	}
}
