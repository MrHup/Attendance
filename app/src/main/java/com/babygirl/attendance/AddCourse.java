package com.babygirl.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.babygirl.attendance.ui.course_manager.CourseManagerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;

public class AddCourse extends AppCompatActivity {
    private String course_id;
    private String course_name;
    private String full_name;
    private String target_year;
    private String DQRC;

    private String getAlphaNumeric(int len) {

        char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        char[] c = new char[len];
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < len; i++) {
            c[i] = ch[random.nextInt(ch.length)];
        }

        return new String(c);
    }

    private String extract_name_curr_user(){
        // get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            return email.replaceAll("((@.*)|[^a-zA-Z])+", " ").trim();
        }
        return "";
    }
    private void write_data(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        database.getReference("Courses/"+course_id+"/course_name").setValue(course_name);
        database.getReference("Courses/"+course_id+"/instructor_name").setValue(full_name);
        database.getReference("Courses/"+course_id+"/target_year").setValue(target_year);
        database.getReference("Courses/"+course_id+"/DQRC").setValue(DQRC);

        // set reference to the course
        DatabaseReference myRef = database.getReference("Users/+"+uid+"/interest_courses/"+course_id);
        myRef.setValue(true);

        Log.d("debug_firebase","Course created successfully");
    }
    private void addCourse(){
        // get full name of current user
        full_name = extract_name_curr_user();
        // get course_name
        course_name = ((EditText)findViewById(R.id.course_name_edit)).getText().toString();
        // get target year
        target_year = ((EditText)findViewById(R.id.target_year_edit)).getText().toString();
        // generate course_id
        course_id = getAlphaNumeric(6);
        // get DQRC
        DQRC = course_id + "_" + getAlphaNumeric(6);

        if(!course_name.equals("") && !target_year.equals("")) {
            // create course entry
            // save reference to the course for current user
            write_data();

            Intent i = new Intent(this, CourseManagerFragment.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            i.putExtra("id",14);
            startActivity(i);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all the required fields",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Button add_course_button = findViewById(R.id.create_course_button);
        add_course_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourse();
            }
        });

    }
}
