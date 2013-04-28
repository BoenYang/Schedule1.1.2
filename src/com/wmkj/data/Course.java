package com.wmkj.data;

public class Course {

	public int id;
	public String courseName="";
	public String courseTime="";
	public String courseTeacher="";
	public String coursePlan="";
	public String coursePlace="";
	public int start;
	public int end;
	public int internel;

	public Course(int id) {
		this.id = id;
	}

	public Course(String name, String t_name) {
		this.courseName = name;
		this.courseTeacher = t_name;
	}

	public void setCourseTime(int days, int time) {
		this.courseTime = days + "-" + time;
	}

	public void setCourseTime(String str) {
		courseTime = str;
	}

	public void updateCoursePlan(int start, int end) {
		if (start == end) {
			char ch[] = new char[coursePlan.length()];
			ch = coursePlan.toCharArray();
			ch[start - 1] = '1';
			String string = new String(ch);
			coursePlan = string;
			return;
		}

		char ch[] = new char[courseTime.length()];
		ch = coursePlan.toCharArray();
		for (int i = start - 1; i < end; i++) {
			ch[i] = '1';
		}
		coursePlan = new String(ch);
	}

	public void updateCoursePlan(int start, int end, int internal) {
		this.start = start;
		this.end = end;
		this.internel = internal;
		if (internal == 0) {
			updateCoursePlan(start, end);
		}
		char ch[] = new char[courseTime.length()];
		ch = coursePlan.toCharArray();
		for (int i = start - 1; i < end; i += internal) {
			ch[i] = '1';
		}
		coursePlan = new String(ch);
	}

	@Override
	public String toString() {
		return id + ":"+courseName + "  " + courseTime + "  "+coursePlace + "\n" + coursePlan  ;
	}
	
	

	public boolean equals(Course course) {
		if (coursePlace == course.coursePlace && courseTime == course.courseTime) {
			return true;
		}
		return false;
	}

}
