package com.babygirl.attendance.ui.student_attendances;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.babygirl.attendance.Course;
import com.babygirl.attendance.CourseAdapter;
import com.babygirl.attendance.R;
import com.babygirl.attendance.ui.course_manager.CourseManagerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentAttendancesFragment extends Fragment {
    RecyclerView recyclerView;

    private ArrayList<String> course_ids = new ArrayList<>();
    public ArrayList<Course> courses = new ArrayList<>();
    private CourseManagerViewModel mViewModel;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private View v;

    // get the course_name, target_year of each of those courses
    private void get_Courses(){
        for(String i_id : course_ids)
        {
            DatabaseReference userIdRef = db.getReference("Courses").child(i_id);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String DQRC = dataSnapshot.child("DQRC").getValue().toString();
                    String course_name = dataSnapshot.child("course_name").getValue().toString();
                    String instructor_name = dataSnapshot.child("instructor_name").getValue().toString();
                    String target_year = dataSnapshot.child("target_year").getValue().toString();

                    courses.add(new Course(DQRC, course_name, instructor_name, target_year));
                    Log.d("debug_querry", courses.get(courses.size() - 1).toString());

                    displayCourseList(v);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            userIdRef.addListenerForSingleValueEvent(eventListener);
        }
        Log.d("debug_recycler","before displayCourseList");

    }

    // get current user and get the ids respective to all his/hers courses
    private void get_IDs(){
        db = FirebaseDatabase.getInstance();
        // get firebase user
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uuid = "+" + user.getUid();
        // QUERRY EXAMPLE for retrieving all keys from a root
        Query query2 = db.getReference().child("Users").child(uuid).child("interest_courses");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("debug_querry",dataSnapshot.getValue() + "!");
                if (dataSnapshot.exists()) {
                    int i = 0;
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        course_ids.add(d.getKey());
                        i++;
                    }
                    get_Courses();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayCourseList(View root){
        recyclerView = root.findViewById(R.id.recyclerViewStudents1);

        CourseAdapter myAdapter = new CourseAdapter(getContext(),courses,false,true);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public static StudentAttendancesFragment newInstance() {
        return new StudentAttendancesFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //attendancesViewModel = ViewModelProviders.of(this).get(AttendancesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_attendace_for_student, container, false);
        v= root;


        get_IDs();

        return root;
    }



}
