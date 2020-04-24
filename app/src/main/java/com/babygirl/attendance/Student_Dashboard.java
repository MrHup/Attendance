package com.babygirl.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Dashboard extends AppCompatActivity {
    private ArrayList<Attendance> attendances;
    private String course_id;
    RecyclerView recyclerView;

    public void getAttendancesFromCourse()
    {
        // gets through intent a course
        // gets all attendances in that course
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_mail = user.getEmail();


        DatabaseReference userIdRef = FirebaseDatabase.getInstance().getReference("Courses").child(course_id);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot attendancesSnapshot = dataSnapshot.child("attendances");
                Iterable<DataSnapshot> attendances_list = attendancesSnapshot.getChildren();

                for(DataSnapshot attendance: attendances_list){
                    Attendance currentAttendance = attendance.getValue(Attendance.class);
                    Log.d("debug_querry",currentAttendance.getUser_mail());
                    attendances.add(currentAttendance);
                }
                displayCourseList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userIdRef.addListenerForSingleValueEvent(eventListener);
    }

    private void displayCourseList(){
        recyclerView = findViewById(R.id.studentsAttendances);

        AttendanceAdapter myAdapter = new AttendanceAdapter(getApplicationContext(),attendances);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        this.attendances = new ArrayList<>();
        this.course_id = getIntent().getExtras().getString("COURSE_ID");
        getAttendancesFromCourse();
    }
}
