package com.example.loginactivity.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.MainActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.domain.Video;
import com.example.loginactivity.util.TimeUtil;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private Video video;
    private Intent intent;
    private Date beginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView title = findViewById(R.id.tv_detail_title);
        TextView from = findViewById(R.id.tv_detail_from);
        TextView time = findViewById(R.id.tv_detail_time);
        VideoView videoView = findViewById(R.id.vv_detail_sp);
        TextView content = findViewById(R.id.tv_detail_content);
        intent = getIntent();
        //接收news对象对象
        video = (Video) intent.getSerializableExtra("news");

        title.setText(video.getTitle());
        from.setText(video.getSource());
        time.setText(video.getTime());
        content.setText(video.getContent());

        //视频解码
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri =Uri.parse(video.getUrl());

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("", "start");
        beginTime = new Date();
    }

    @Override
    public void onBackPressed() {
        boolean isOverTime = new TimeUtil().isOverTime(beginTime, 30);
        int second = new TimeUtil().getSecond(beginTime);
        intent.setClass(this, MainActivity.class);
        intent.putExtra("isOverTime", isOverTime);
        intent.putExtra("news", video);
        setResult(1, intent);
        DetailActivity.this.finish();
        Log.v("发送", "" + isOverTime + second);
    }

}