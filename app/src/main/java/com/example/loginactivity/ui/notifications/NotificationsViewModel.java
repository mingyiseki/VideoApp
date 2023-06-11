package com.example.loginactivity.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mText2;


    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这里是用户登录界面" + "\n请先进行用户登录");
        mText2 = new MutableLiveData<>();
        mText2.setValue("登录/注册");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getText2() {
        return mText2;
    }
}