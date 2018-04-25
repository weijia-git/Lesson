package com.example.pc.lesson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PC on 2017/4/15.
 */

public class TeacherParser {
    public static Teacher parseTeacher(String teacherInfo){
        Teacher teacher = new Teacher();
        Map<String,String> teachersMap = new HashMap<String,String>();

        int index1 = teacherInfo.indexOf("系");
        int index2 = teacherInfo.indexOf("教师");

        teacherInfo = teacherInfo.substring(0,index1+1) + teacherInfo.substring(index2);

        index1 = teacherInfo.indexOf("性别");
        teacherInfo = teacherInfo.substring(0,index1-2) + teacherInfo.substring(index1);

        index1 = teacherInfo.indexOf("职称");
        teacherInfo = teacherInfo.substring(0,index1-2) + teacherInfo.substring(index1);



        int index = teacherInfo.indexOf("教师");
        int indexend = teacherInfo.indexOf("性别");
        String techname = teacherInfo.substring(index+3,indexend);
        index = teacherInfo.indexOf("性别");
        indexend = teacherInfo.indexOf("职称");
        String sex = teacherInfo.substring(index+3,indexend);
        index = teacherInfo.indexOf("职称");
        String protitle = teacherInfo.substring(index+3);
        index = teacherInfo.indexOf("部门");
        indexend = teacherInfo.indexOf("教师");
        String Dep = teacherInfo.substring(index+3,indexend);
        teacher = new Teacher();
        for (String id : teachersMap.keySet()) {
            if(teachersMap.get(id).equals(techname)){
                teacher.setId(id);
                break;
            }
        }
        teacher.setName(techname);
        teacher.setProtitle(protitle);
        teacher.setSex(sex);
        teacher.setDep(Dep);
//		System.out.println(teacher.getId() + teacher.getName() + teacher.getProtitle()+teacher.getSex()+teacher.getPart());

        return teacher;
    }
}
