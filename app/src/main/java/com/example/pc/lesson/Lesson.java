package com.example.pc.lesson;

/**
 * Created by PC on 2017/4/17.
 */

public class Lesson {
    private String lessonname;
    private String learningperiod;
    private String credit;
    private String units;
    private String lessons;

    public String getLessonname() {
        return lessonname;
    }

    public void setLessonname(String lessonname) {
        this.lessonname = lessonname;
    }


    public Lesson() {
    }

    public Lesson(String lessonname, String learningperiod, String credit, String units, String lessons) {
        this.lessonname = lessonname;
        this.learningperiod = learningperiod;
        this.credit = credit;
        this.units = units;
        this.lessons = lessons;
    }

    public String getLearningperiod() {
        return learningperiod;
    }

    public void setLearningperiod(String learningperiod) {
        this.learningperiod = learningperiod;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getLessons() {
        return lessons;
    }

    public void setLessons(String lessons) {
        this.lessons = lessons;
    }


}
