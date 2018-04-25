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
 * Created by PC on 2017/4/17.
 */

public class LessoninfoAdapter extends ArrayAdapter<Lessoninfo> {
    private Context context;
    private int resource;
    private List<Lessoninfo> lessoninfos;

    public LessoninfoAdapter(Context context, int resource, List<Lessoninfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.lessoninfos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Lessoninfo l = lessoninfos.get(position);

        convertView = LayoutInflater.from(context).inflate(resource,parent,false);

        TextView teacherNameTv = (TextView)convertView.findViewById(R.id.teacherName_textView);
        TextView classnumTv = (TextView)convertView.findViewById(R.id.classnum_textView);
        TextView stunumTv = (TextView)convertView.findViewById(R.id.studentnum__textView);
        TextView subjectTv = (TextView)convertView.findViewById(R.id.subject_textView);
        TextView testTv = (TextView)convertView.findViewById(R.id.test_textView);
        TextView classesTv = (TextView)convertView.findViewById(R.id.classes_textView);
        TextView courseperiodTv = (TextView)convertView.findViewById(R.id.courseperiod_textView);
        TextView placeTv = (TextView)convertView.findViewById(R.id.place_textView);

        teacherNameTv.setText(l.getTeachername());
        classnumTv.setText(l.getClassnum());
        stunumTv.setText(l.getStudentnum());
        subjectTv.setText(l.getSubject());
        testTv.setText(l.getTest());
        classesTv.setText(l.getClasses());
        courseperiodTv.setText(l.getCourseperiod());
        placeTv.setText(l.getPlace());

        return convertView;

    }
}
