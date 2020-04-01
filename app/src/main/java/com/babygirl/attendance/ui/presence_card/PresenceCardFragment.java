package com.babygirl.attendance.ui.presence_card;

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

public class PresenceCardFragment extends Fragment {

    private PresenceCardViewModel presenceCardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        presenceCardViewModel =
                ViewModelProviders.of(this).get(PresenceCardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_presence_card, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        presenceCardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}