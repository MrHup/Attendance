package com.babygirl.attendance.ui.course_manager;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.babygirl.attendance.AddCourse;
import com.babygirl.attendance.Attendance;
import com.babygirl.attendance.AttendanceListStudent;
import com.babygirl.attendance.Course;
import com.babygirl.attendance.CourseAdapter;
import com.babygirl.attendance.Courses;
import com.babygirl.attendance.R;
import com.babygirl.attendance.ui.student_attendances.StudentAttendancesFragment;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CourseManagerFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressBar progressBar;

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
        Log.d("debug_querry","hi from course manager wit id = " + uuid);
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
                    Log.d("debug_progress","before changing vis");
                    if(progressBar != null){
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayCourseList(View root){
        recyclerView = root.findViewById(R.id.recyclerView);

        CourseAdapter myAdapter = new CourseAdapter(getContext(),courses,true,false);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public static CourseManagerFragment newInstance() {
        Log.d("debug_fragments","new fragment");
        return new CourseManagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_course_manager, container, false);
        v= root;
        progressBar = root.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        get_IDs();

        final ImageButton addCourse = root.findViewById(R.id.button_add_course);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getContext(), AddCourse.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CourseManagerViewModel.class);
    }

}
