package com.babygirl.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView mail;
    TextView password;
    private FirebaseAuth mAuth; // declare instance of Firebase Auth

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
                if(value){
                    Log.d("debug_firebase","user is prof, going to dashboard");
                    Intent i = new Intent(LoginActivity.this, Prof_Dashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else{
                    Log.d("debug_firebase","user is student, going to Courses");
                    Intent i = new Intent(LoginActivity.this, Courses.class);
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

    public void login(String mail, String pass){
        Log.d("debug_fuck", mail + " and " + pass);
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("debug_firebase", "signInWithEmail:success");
                            is_user_prof();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("debug_firebase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Email or password is incorrect. Please try again",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email_tv = (EditText) findViewById(R.id.mail);
                EditText password_tv = (EditText) findViewById(R.id.password);
                if(!email_tv.getText().toString().equals("") && !password_tv.getText().toString().equals("")) {
                    Log.d("debug_fuck", email_tv.getText().toString() + " attempted to login");
                    login(email_tv.getText().toString(), password_tv.getText().toString());
                }else{ // deal with input exceptions
                    Toast.makeText(getApplicationContext(), "Please fill all the required fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mail = (TextView) findViewById(R.id.mail);
        Drawable[] mailIcon = mail.getCompoundDrawables();
        mailIcon[0].setAlpha(128);

        password = (TextView) findViewById(R.id.password);
        Drawable[] lockIcon = password.getCompoundDrawables();
        lockIcon[0].setAlpha(128);
    }
}
