package com.babygirl.attendance.ui.attendances;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AttendancesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AttendancesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is attendances fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}