package com.babygirl.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

        /*public void make_user_folder(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        //get the is_prof value
        Boolean is_prof = getIntent().getExtras().getBoolean("is_prof");

        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("123");}*/



    public void signup(String email,String pass){
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("debug_firebase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            startActivity(new Intent(SignUp.this, Courses.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("debug_firebase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

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
                if(login_Confirm.getText().toString().equals(login_Password.getText().toString())) {
                    signup(login_Email.getText().toString(),login_Password.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "The passwords do not match",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        mail = (TextView) findViewById(R.id.mail2);
        Drawable[] mailIcon = mail.getCompoundDrawables();
        mailIcon[0].setAlpha(128);

        password = (TextView) findViewById(R.id.password2);
        Drawable[] lockIcon = password.getCompoundDrawables();
        lockIcon[0].setAlpha(128);

        confirm = (TextView) findViewById(R.id.password3);
        Drawable[] lockIcon2 = mail.getCompoundDrawables();
        lockIcon2[0].setAlpha(128);

        /*findViewById(R.id.button_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_user_folder();

            }
        });*/

        }
}
