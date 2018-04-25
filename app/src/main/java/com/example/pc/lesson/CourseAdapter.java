package com.example.pc.lesson;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC on 2017/4/15.
 */

public class CourseAdapter extends ArrayAdapter<Courseinfo>{
    private Context context;
    private int resource;
    private List<Courseinfo> courses;

    public CourseAdapter(Context context, int resource, List<Courseinfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.courses = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Courseinfo c = courses.get(position);

        convertView = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView weekTv = (TextView) convertView.findViewById(R.id.week_textView);
        TextView coursenameTv = (TextView) convertView.findViewById(R.id.coursename_textView);
        TextView learnperiodTv = (TextView) convertView.findViewById(R.id.learningperiod_textView);
        TextView courseperiodTv = (TextView) convertView.findViewById(R.id.courseperiod_textView);
        TextView classroomTv = (TextView) convertView.findViewById(R.id.classroom_textView);
        TextView classTv = (TextView) convertView.findViewById(R.id.class_textView);
        TextView stuNumTv = (TextView) convertView.findViewById(R.id.studentnum__textView);

        weekTv.setText(c.getWeek());
        coursenameTv.setText(c.getCoursename());
        learnperiodTv.setText(c.getLearningperiod());
        courseperiodTv.setText(c.getCourseperiod());
        classroomTv.setText(c.getClassroom());
        classTv.setText(c.getCalss());
        stuNumTv.setText(c.getStudentnum());
        return convertView;





    }
}
