package com.example.pc.lesson;

/**
 * Created by PC on 2017/4/15.
 */

public class Courseinfo {
    private String week;
    private String coursename;
    private String learningperiod;
    private String courseperiod;
    private String classroom;
    private String calss;
    private String studentnum;

    public Courseinfo() {
    }

    public Courseinfo(String week, String coursename, String learningperiod, String courseperiod, String classroom, String calss, String studentnum) {
        this.week = week;
        this.coursename = coursename;
        this.learningperiod = learningperiod;
        this.courseperiod = courseperiod;
        this.classroom = classroom;
        this.calss = calss;
        this.studentnum = studentnum;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getLearningperiod() {
        return learningperiod;
    }

    public void setLearningperiod(String learningperiod) {
        this.learningperiod = learningperiod;
    }

    public String getCourseperiod() {
        return courseperiod;
    }

    public void setCourseperiod(String courseperiod) {
        this.courseperiod = courseperiod;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getCalss() {
        return calss;
    }

    public void setCalss(String calss) {
        this.calss = calss;
    }

    public String getStudentnum() {
        return studentnum;
    }

    public void setStudentnum(String studentnum) {
        this.studentnum = studentnum;
    }

}
