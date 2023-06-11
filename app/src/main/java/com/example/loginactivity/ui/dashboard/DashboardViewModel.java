package com.example.loginactivity.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("收藏夹\n" + "请先登录账号\n" + "才能启动收藏功能");
    }

    public LiveData<String> getText() {
        return mText;
    }
}