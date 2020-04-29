package com.babygirl.attendance.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.babygirl.attendance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class SignUp extends AppCompatActivity {

    TextView mail;
    TextView password;
    TextView confirm;

    private FirebaseAuth mAuth; // declare instance of Firebase Auth

    // initialize the user in the realtime DB
    // aka make user folder
    // will contain interest_courses
    // and a bool to notify the app if he is prof or student

        public void make_user_folder(FirebaseUser user){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid=user.getUid();
        //get the is_prof value
        Boolean is_prof = getIntent().getExtras().getBoolean("is_prof");
        DatabaseReference myRef = database.getReference("Users/+"+uid+"/is_prof");
        myRef.setValue(is_prof);
        }



    public void signup(String email,String pass){
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("debug_firebase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            make_user_folder(user); //write in database

                            Intent i = new Intent(SignUp.this, Loading.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("debug_firebase", "createUserWithEmail:failure", task.getException());

                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {


                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(getApplicationContext(), "The given email address is badly formatted.", Toast.LENGTH_LONG).show();

                                    break;


                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(getApplicationContext(), "The email address is already in use.", Toast.LENGTH_LONG).show();
                                    break;


                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(getApplicationContext(), "The given password is weak.", Toast.LENGTH_LONG).show();
                                    break;

                                 default:
                                     Toast.makeText(getApplicationContext(), "Sign up failed.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        final TextView login_Email = findViewById(R.id.mail2);
        final TextView login_Password = findViewById(R.id.password2);
        final TextView login_Confirm = findViewById(R.id.password3);

        // Firebase init
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_signup2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if fields are empty
                if (login_Email.getText().toString().matches("") || login_Password.getText().toString().matches("") || login_Confirm.getText().toString().matches("")) {

                    Toast.makeText(getApplicationContext(), "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
                }
                //check if pass and pass confirmation match
                else if (login_Confirm.getText().toString().equals(login_Password.getText().toString())) {
                    signup(login_Email.getText().toString(),login_Password.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "The passwords do not match.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        //setting icons
        mail = (TextView) findViewById(R.id.mail2);
        Drawable[] mailIcon = mail.getCompoundDrawables();
        mailIcon[0].setAlpha(128);

        password = (TextView) findViewById(R.id.password2);
        Drawable[] lockIcon = password.getCompoundDrawables();
        lockIcon[0].setAlpha(128);

        confirm = (TextView) findViewById(R.id.password3);
        Drawable[] lockIcon2 = mail.getCompoundDrawables();
        lockIcon2[0].setAlpha(128);


        }
}
