package com.babygirl.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

    public void getAttendancesFromCourse(final String name, final String date)
    {
        // gets through intent a course
        // gets all attendances in that course
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userIdRef = FirebaseDatabase.getInstance().getReference("Courses").child(course_id);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot attendancesSnapshot = dataSnapshot.child("attendances");
                Iterable<DataSnapshot> attendances_list = attendancesSnapshot.getChildren();

                for(DataSnapshot attendance: attendances_list){
                    Attendance currentAttendance = attendance.getValue(Attendance.class);
                    Log.d("debug_querry",currentAttendance.getUser_mail());

                    boolean isFoundName = true;
                    if(!name.equals("")){
                        isFoundName = currentAttendance.getUser_mail().indexOf(name) !=-1? true: false;
                    }
                    boolean isFoundDate = true;
                    if(!date.equals("")){
                        isFoundDate = currentAttendance.getDate().indexOf(date) !=-1? true: false;
                    }

                    if(isFoundDate && isFoundName){
                        attendances.add(currentAttendance);
                    }

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
        getAttendancesFromCourse("","");

        Button button = findViewById(R.id.filter_button);
        final TextView name_text = findViewById(R.id.student_editText);
        final TextView date_text = findViewById(R.id.date_editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendances = new ArrayList<>();
                getAttendancesFromCourse(name_text.getText().toString(),date_text.getText().toString());
            }
        });
    }
}
