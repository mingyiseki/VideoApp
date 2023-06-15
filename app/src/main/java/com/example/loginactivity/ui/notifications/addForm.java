package com.example.loginactivity.ui.notifications;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.BroadcastReceiver.MyReceiver;
import com.example.loginactivity.MainActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.domain.User;
import com.example.loginactivity.domain.Video;
import com.example.loginactivity.util.UserOpenHelper;
import com.example.loginactivity.util.VideoOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class addForm extends AppCompatActivity {
    private EditText title;
    private EditText content;
    private EditText url;
    private EditText image;
    private Button addVideo;
    private VideoOpenHelper videoHelper;
    private MyReceiver myReceiver;
    private UserOpenHelper userHelper;
    private static SharedPreferences sharedPreferences;
    private String userNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);


        title = findViewById(R.id.et_add_title);
        content = findViewById(R.id.et_add_content);
        url = findViewById(R.id.et_add_url);
        image = findViewById(R.id.et_add_image);
        addVideo = findViewById(R.id.btn_addVideoSuccess);

        if (userHelper == null) {
            userHelper = UserOpenHelper.getInstance(getApplication());
        }
        userHelper.openReadLink();
        userHelper.openWriteLink();

        //加载SharedPreferences
        sharedPreferences = getApplication().getSharedPreferences("config", MODE_PRIVATE);
        userNickName = "";
        String uid = sharedPreferences.getString("uid", "");
        boolean flag = true;
        for (User user : userHelper.queryAll()) {
            if (String.valueOf(user.getId()).equals(uid)) {
                userNickName = user.getNikeName();
                flag = false;
            }
        }
        if (flag) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intentSuccess = new Intent(addForm.this, MainActivity.class);
            startActivity(intentSuccess);
        }

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从数据库获取资源
                //1.创建/打开数据库，数据库名为VideoTable.db
                if (videoHelper == null) {
                    videoHelper = VideoOpenHelper.getInstance(getApplication());
                }
                videoHelper.openReadLink();
                videoHelper.openWriteLink();

                //时间
                String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                //黑色
                int blackColor = getResources().getColor(R.color.bg_black);

                Video video = new Video(title.getText().toString(), content.getText().toString(), userNickName, time, url.getText().toString(), 0, 0, Integer.parseInt(image.getText().toString()), blackColor);
                if (videoHelper.insert(video) > 0) {
                    //发送自定义广播
                    IntentFilter intentFilter = new IntentFilter("broadcast");
                    myReceiver = new MyReceiver();
                    Intent intent = new Intent("broadcast");
                    //直接发送广播
                    intent.putExtra("broadcast", "有人投稿了新视频，请查看");
                    registerReceiver(myReceiver, intentFilter);
                    sendBroadcast(intent);
                    Log.d(TAG, "添加数据:" + video);

                    Intent intentSuccess = new Intent(addForm.this, MainActivity.class);
                    startActivity(intentSuccess);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(myReceiver);
    }
}