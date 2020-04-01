package com.babygirl.attendance.ui.attendances;

import android.os.Bundle;
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
        return root;
    }
}