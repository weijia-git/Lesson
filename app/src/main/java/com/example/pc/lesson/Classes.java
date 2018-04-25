package com.example.pc.lesson;

/**
 * Created by PC on 2017/4/17.
 */

public class Classes {
    private String name;
    private String grade;
    private String major;
    private String lessons;

    public Classes() {
    }

    public Classes(String name, String grade, String major, String lessons) {
        this.name = name;
        this.grade = grade;
        this.major = major;
        this.lessons = lessons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLessons() {
        return lessons;
    }

    public void setLessons(String lessons) {
        this.lessons = lessons;
    }

    @Override
    public String toString() {
        return "Classes{" +
                "name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", major='" + major + '\'' +
                ", lessons='" + lessons + '\'' +
                '}';
    }
}
