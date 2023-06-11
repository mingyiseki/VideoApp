package com.example.loginactivity.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 当广播接收者接收广播成功，此方法会自动回调执行");
        if ("loginSuccess".equals(intent.getAction())) {
            String nickName = intent.getStringExtra("nickName");
            Toast.makeText(context, "登录成功," + nickName + "欢迎您的访问", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "接收到登录成功,用户id: " + nickName);
        }
        if ("registerSuccess".equals(intent.getAction())) {
            String nickName = intent.getStringExtra("nickName");
            Toast.makeText(context, "注册成功," + nickName + "欢迎您的访问", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "接收到注册成功,用户id: " + nickName);
        }
    }
}

