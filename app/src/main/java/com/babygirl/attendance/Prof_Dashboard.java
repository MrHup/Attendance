package com.babygirl.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Prof_Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_dashboard);

        Button goto_add_course = findViewById(R.id.goto_add_course_button);
        goto_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Prof_Dashboard.this, AddCourse.class));
            }
        });
    }
}
