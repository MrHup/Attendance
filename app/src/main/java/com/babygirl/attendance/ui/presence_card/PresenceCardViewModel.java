package com.babygirl.attendance.ui.presence_card;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PresenceCardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PresenceCardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is presence card fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}