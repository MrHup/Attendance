package com.babygirl.attendance.ui.attendances;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.babygirl.attendance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AttendancesFragment extends Fragment {

    private AttendancesViewModel attendancesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        attendancesViewModel =
                ViewModelProviders.of(this).get(AttendancesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_attendace_for_student, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        attendancesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        /// START USER PROCESSING HERE
        // get user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
           Log.d("debug_firebase","Error at getting user in fragment attendances!");
        }
        String user_id = "+"+user.getUid();
        // fetch current user's interest_courses
        Query query2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("interest_courses");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        Log.d("debug_querry",d.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return root;
    }
}