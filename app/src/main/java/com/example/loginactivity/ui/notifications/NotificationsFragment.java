package com.example.loginactivity.ui.notifications;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginactivity.R;
import com.example.loginactivity.databinding.FragmentNotificationsBinding;
import com.example.loginactivity.domain.Star;
import com.example.loginactivity.domain.User;
import com.example.loginactivity.login.LoginActivity;
import com.example.loginactivity.util.StarOpenHelper;
import com.example.loginactivity.util.UserOpenHelper;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private UserOpenHelper userHelper;
    private StarOpenHelper starHelper;
    private static SharedPreferences sharedPreferences;
    private TextView textView;
    private TextView loginText;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //根据ID获取到图片按钮对象
        final ImageButton btnImage = binding.loginBtnImage;
        //为图片按钮设置点击事件
        btnImage.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        //根据ID获取到图片按钮对象
        final ImageButton btnAddVideo = binding.btnAddVideo;
        //为图片按钮设置点击事件
        btnAddVideo.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), addForm.class);
            startActivity(intent);
        });

        //为图片按钮设置长按事件
        btnImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("long", R.id.rb_register);
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });

        //加载SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        if (!uid.equals("")) {
            Log.d(TAG, "onCreateView: " + uid);

            if (userHelper == null) {
                userHelper = UserOpenHelper.getInstance(getActivity());
            }
            userHelper.openReadLink();
            userHelper.openWriteLink();

            if (starHelper == null) {
                starHelper = StarOpenHelper.getInstance(getActivity());
            }
            starHelper.openReadLink();
            starHelper.openWriteLink();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: " + uid);
                    if (!uid.equals("") && uid != null && !uid.equals("0")) {
                        User user = userHelper.queryById(Long.parseLong(uid));
                        List<Star> stars = starHelper.queryAll();
                        Long userId = user.getId();
                        int starCount = 0;
                        for (Star star : stars) {
                            if (star.getUid() == userId) {
                                starCount++;
                            }
                        }
                        textView = view.findViewById(R.id.text_notifications);
                        textView.setText("欢迎您的使用~" + "\n用户名:" + user.getNikeName() + "\n手机号:" + user.getPhoneNumber() + "\n收藏数:" + starCount);
                        loginText = view.findViewById(R.id.loginText);
                        loginText.setText("已登录~\n可切换账号");

                    }
                }
            }, 500);
        }
    }


    private FragmentNotificationsBinding binding;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //这里是登录页面
        TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //登录/注册
        TextView textView2 = binding.loginText;
        notificationsViewModel.getText2().observe(getViewLifecycleOwner(), textView2::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}