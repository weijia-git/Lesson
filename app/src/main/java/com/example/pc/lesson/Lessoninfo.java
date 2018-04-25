package com.example.pc.lesson;

/**
 * Created by PC on 2017/4/17.
 */

public class Lessoninfo {
    private String teachername;
    private String classnum;
    private String studentnum;
    private String subject;
    private String test;
    private String classes;
    private String courseperiod;
    private String place;

    public Lessoninfo() {
    }

    public Lessoninfo(String teachername, String classnum, String studentnum, String subject, String test, String classes, String courseperiod, String place) {
        this.teachername = teachername;
        this.classnum = classnum;
        this.studentnum = studentnum;
        this.subject = subject;
        this.test = test;
        this.classes = classes;
        this.courseperiod = courseperiod;
        this.place = place;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getClassnum() {
        return classnum;
    }

    public void setClassnum(String classnum) {
        this.classnum = classnum;
    }

    public String getStudentnum() {
        return studentnum;
    }

    public void setStudentnum(String studentnum) {
        this.studentnum = studentnum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getCourseperiod() {
        return courseperiod;
    }

    public void setCourseperiod(String courseperiod) {
        this.courseperiod = courseperiod;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Lessoninfo{" +
                "classnum='" + classnum + '\'' +
                ", studentnum='" + studentnum + '\'' +
                ", subject='" + subject + '\'' +
                ", test='" + test + '\'' +
                ", classes='" + classes + '\'' +
                ", courseperiod='" + courseperiod + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
