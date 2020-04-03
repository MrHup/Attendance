package com.babygirl.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

////--------------------------------------------------------------
////-------------------DEBUG ACTIVITY-----------------------------
////--------------------------------------------------------------

public class Courses extends AppCompatActivity {
    TextView textView;
    public void show_text(String msg){
        // design stuff-----
        textView = (TextView) findViewById(R.id.textView2);

        TextPaint paint = textView.getPaint();
        float width = paint.measureText(msg);

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#F67280"),
                        Color.parseColor("#C06C84"),
                        Color.parseColor("#6C5B7B"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        textView.setText(msg);
    }


    // -----------------------------------------------------------

    public void logout(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                // user is now signed out
                startActivity(new Intent(Courses.this, MainActivity.class));
                Log.d("debug_firebase","Logout succesfull");
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            show_text("Hello "+ email);
        } else {
            Log.d("debug_firebase","Exception. User not identified has passed through login procedure.");
        }

        ImageButton button_logout = findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        Button to_qr = findViewById(R.id.qr_button);
        to_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Courses.this, QR_Reader.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        Button to_generator = findViewById(R.id.generator_button);
        to_generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Courses.this, QR_Generator.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("COURSE_ID", "jkLM1dbV");
                startActivity(i);
            }
        });

        Button to_attendance = findViewById(R.id.to_attendance_button);
        to_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Courses.this, AttendanceListStudent.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        // to test querries
        Button test_querries_button = findViewById(R.id.test_querries_button);
        test_querries_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug_querry","bruh1");
                // testing with querries
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                // QUERRY EXAMPLE for retrieving all with child value equal to
                Query query = FirebaseDatabase.getInstance().getReference().child("TestQuerry").orderByChild("val").equalTo("Bobby");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("debug_querry","bruh2");
                        Log.d("debug_querry",dataSnapshot.getValue() + "!");
                        if (dataSnapshot.exists()) {
                            Log.d("debug_querry","bruh3");
                            int i = 0;
                            for(DataSnapshot d : dataSnapshot.getChildren()) {
                                Log.d("debug_querry",d.getKey() + " : " + d.getValue());
                                i++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // GET REFERENCE EXAMPLE for retrieving all keys from a root
                DatabaseReference myRef = database.getReference("Users").child("+8O4ukqAF8cMQyf1BUSeid1Tvugs2").child("interest_courses");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            Log.d("debug_querry",key);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

                // QUERRY EXAMPLE for retrieving all keys from a root
                Query query2 = FirebaseDatabase.getInstance().getReference().child("Users").child("+8O4ukqAF8cMQyf1BUSeid1Tvugs2").child("interest_courses");
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("debug_querry",dataSnapshot.getValue() + "!");
                        if (dataSnapshot.exists()) {
                            int i = 0;
                            for(DataSnapshot d : dataSnapshot.getChildren()) {
                                Log.d("debug_querry",d.getKey() + " : " + d.getValue());
                                i++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
