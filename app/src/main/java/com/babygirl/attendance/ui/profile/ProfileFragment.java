package com.babygirl.attendance.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.babygirl.attendance.Courses;
import com.babygirl.attendance.Loading;
import com.babygirl.attendance.MainActivity;
import com.babygirl.attendance.Prof_Dashboard;
import com.babygirl.attendance.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment  {

    private String extract_name_curr_user(){
        // get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            return email.replaceAll("((@.*)|[^a-zA-Z])+", " ").trim();
        }
        return "";
    }

    public ImageButton button;


    public void logout(){
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Log.d("debug_firebase","Logout succesfull");
                        getActivity().finish();

                    }
                });
    }

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //logout button
        button = (ImageButton) root.findViewById(R.id.button_logout3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });

        //user icon
        String name = extract_name_curr_user();
        String[] parts = name.split("\\ ");
        String first_name = parts[0].substring(0, 1);
        String last_name = parts[1].substring(0, 1);
        Button profile_icon = root.findViewById(R.id.profile_icon);
        profile_icon.setText(first_name+last_name);

        //user name
        TextView user_name = root.findViewById(R.id.user_name);
        user_name.setText("Hello,"+"\n"+name.toUpperCase());
        return root;
    }



}