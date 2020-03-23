package com.babygirl.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // depending on if the user is already logged in or not, move him to the respective activity
    public boolean is_logged(){
        // get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            return true;

        return false;
    }

    ImageView whiteBorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whiteBorder = findViewById(R.id.whiteBorder);
        whiteBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_logged()){
                    Log.d("debug_fuck","User logged in");
                    Intent i = new Intent(MainActivity.this, Courses.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else {
                    Log.d("debug_fuck","User not logged in");
                    Intent i = new Intent(MainActivity.this, Activity2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });

    }
}
