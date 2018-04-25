package com.example.pc.lesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by PC on 2017/4/14.
 */

public class PreActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_layout);

    }

    public void begin(View view) {

        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }

    public void courseAc(View view) {
        Intent intent = new Intent();
        intent.setClass(this,CourseActivity.class);
        startActivity(intent);
    }

    public void classAc(View view) {
        Intent intent = new Intent();
        intent.setClass(this,ClassesActivity.class);
        startActivity(intent);

    }
}
