package com.babygirl.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Loading extends AppCompatActivity {
    private FirebaseAuth mAuth; // declare instance of Firebase Auth

    public void logout(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(Loading.this, MainActivity.class));
                        Log.d("debug_firebase","Logout succesfull");
                        finish();
                    }
                });
    }

    // user type centralized
    private void is_user_prof(){ // check if user is a prof or a student
        FirebaseUser user = mAuth.getCurrentUser();
        String uuid = user.getUid();
        Log.d("debug_firebase","+"+uuid);
        final String path = "Users/+"+uuid+"/is_prof";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(path);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(Boolean.class);
                Log.d("debug_firebase","Value is: " + value);
                if(value == null){
                    // logout the user
                    logout();
                }
                if(value){
                    Log.d("debug_firebase","user is prof, going to dashboard");
                    Intent i = new Intent(Loading.this, Prof_Dashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else{
                    Log.d("debug_firebase","user is student, going to Courses");
                    Intent i = new Intent(Loading.this, AttendanceListStudent.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("debug_firebase","some error at reading user is_prof");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mAuth = FirebaseAuth.getInstance();

        is_user_prof();
    }
}
