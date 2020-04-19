package com.babygirl.attendance.ui.course_manager;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.babygirl.attendance.AddCourse;
import com.babygirl.attendance.AttendanceListStudent;
import com.babygirl.attendance.Course;
import com.babygirl.attendance.Courses;
import com.babygirl.attendance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseManagerFragment extends Fragment {

    private ArrayList<String> course_ids;
    private ArrayList<Course> courses;
    private CourseManagerViewModel mViewModel;

    // get current user, get the ids of all his courses
    // get the course_name, target_year of each of those courses
    private void get_IDs(){
        // get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uuid = "+" + user.getUid();
        Log.d("debug_querry","hi from course manager wit id = " + uuid);
        // QUERRY EXAMPLE for retrieving all keys from a root
        Query query2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uuid).child("interest_courses");
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static CourseManagerFragment newInstance() {
        return new CourseManagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_course_manager, container, false);
        Log.d("debug_querry","hi from course manager");
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
