package day2hw1;

import java.util.ArrayList;

public class Course {
	
    String courseName;
	int courseId;
	String teacher;
	String courseDetails;
	String courseProgram;
	ArrayList<User> courseMember = new ArrayList<User>();
	
	public Course(String courseName, int courseId, String courseDetails, String teacher) {
		super();
		this.courseName = courseName;
		this.courseId = courseId;
		this.teacher = teacher;
		this.courseDetails = courseDetails;
	}
	
	public void courseInfo() {
		
		System.out.println(" Kurs üyeleri:");
		for(User user: courseMember) {
			System.out.println(user.getName());
		}
	}
	
	
	

}
