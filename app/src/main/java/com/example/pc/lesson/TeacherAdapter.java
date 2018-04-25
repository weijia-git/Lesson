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
 * Created by PC on 2017/4/14.
 */

public class TeacherAdapter extends ArrayAdapter<Teacher> {

    private Context context;
    private int resource;
    private List<Teacher> teachers;

    public TeacherAdapter(Context context, int resource, List<Teacher> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.teachers = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Teacher t = teachers.get(position);

        convertView = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView depTv = (TextView) convertView.findViewById(R.id.dep_textView);
        TextView nameTv = (TextView) convertView.findViewById(R.id.name_textView);
        TextView sexTv = (TextView) convertView.findViewById(R.id.sex_textView);
        TextView protitleTv = (TextView) convertView.findViewById(R.id.protitle_textView);



        depTv.setText("部门："+t.getDep());
        nameTv.setText("教师："+t.getName());
        sexTv.setText("性别："+t.getSex());
        protitleTv.setText("职称："+t.getProtitle());
        return convertView;




    }
}
