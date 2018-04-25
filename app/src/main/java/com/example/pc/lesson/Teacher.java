package com.example.pc.lesson;

public class Teacher {
    private String id;
    private String name;
    private String sex;
    private String protitle;
    private String dep;
    private String lessons;
    public Teacher() {
        super();
    }

    public Teacher(String id, String name, String sex, String protitle,
                   String part, String lessons) {
        super();
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.protitle = protitle;
        this.dep = part;
        this.lessons = lessons;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getProtitle() {
        return protitle;
    }
    public void setProtitle(String protitle) {
        this.protitle = protitle;
    }
    public String getDep() {
        return dep;
    }
    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getLessons() {
        return lessons;
    }

    public void setLessons(String lessons) {
        this.lessons = lessons;
    }


}
