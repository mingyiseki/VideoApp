package com.example.loginactivity.ui.home;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.MainActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.domain.Star;
import com.example.loginactivity.domain.Video;
import com.example.loginactivity.util.StarOpenHelper;
import com.example.loginactivity.util.TimeUtil;
import com.example.loginactivity.util.UserOpenHelper;
import com.example.loginactivity.util.VideoOpenHelper;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private Video video;
    private Intent intent;
    private Date beginTime;
    private VideoOpenHelper videoHelper;
    private UserOpenHelper userHelper;
    private StarOpenHelper starHelper;
    private static SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView title = findViewById(R.id.tv_detail_title);
        TextView from = findViewById(R.id.tv_detail_from);
        TextView time = findViewById(R.id.tv_detail_time);
        VideoView videoView = findViewById(R.id.vv_detail_sp);
        TextView likes = findViewById(R.id.tv_detail_likes);
        TextView star = findViewById(R.id.tv_detail_star);
        TextView content = findViewById(R.id.tv_detail_content);
        intent = getIntent();
        //接收news对象对象
        video = (Video) intent.getSerializableExtra("news");

        title.setText(video.getTitle());
        from.setText(video.getSource());
        time.setText(video.getTime());
        content.setText(video.getContent());
        likes.setText("点赞:" + video.getLikes());
        star.setText("收藏:" + video.getStar());

        //视频解码
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse(video.getUrl());

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();

        Button likesButton = findViewById(R.id.btn_detail_likes);
        Button starButton = findViewById(R.id.btn_detail_star);

        //从数据库获取资源
        //1.创建/打开数据库，数据库名为VideoTable.db
        if (videoHelper == null) {
            videoHelper = VideoOpenHelper.getInstance(this);
        }
        //从数据库获取资源
        //2.创建/打开数据库，数据库名为UserTable.db
        if (userHelper == null) {
            userHelper = UserOpenHelper.getInstance(this);
        }
        //从数据库获取资源
        //3.创建/打开数据库，数据库名为StarTable.db
        if (starHelper == null) {
            starHelper = StarOpenHelper.getInstance(this);
        }

        videoHelper.openReadLink();
        videoHelper.openWriteLink();

        userHelper.openReadLink();
        userHelper.openWriteLink();

        starHelper.openReadLink();
        starHelper.openWriteLink();

        //加载SharedPreferences
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");
        Log.d(TAG, "onCreate: uid" + uid);

        likesButton.setOnClickListener(v -> {
            //从数据库获取资源
            //创建/打开数据库，数据库名为StarTable.db
            if (videoHelper == null) {
                videoHelper = VideoOpenHelper.getInstance(this);
            }
            videoHelper.openReadLink();
            videoHelper.openWriteLink();

            int like = video.getLikes();
            video.setLikes(like + 1);
            videoHelper.updateById(video);
            Log.d(TAG, "onCreate: " + video.getLikes());
            likes.setText("点赞:" + video.getLikes());
        });
        starButton.setOnClickListener(v -> {
            //从数据库获取资源
            //创建/打开数据库，数据库名为StarTable.db
            if (videoHelper == null) {
                videoHelper = VideoOpenHelper.getInstance(this);
            }
            videoHelper.openReadLink();
            videoHelper.openWriteLink();
            //从数据库获取资源
            //创建/打开数据库，数据库名为StarTable.db
            if (starHelper == null) {
                starHelper = StarOpenHelper.getInstance(this);
            }
            starHelper.openReadLink();
            starHelper.openWriteLink();
            //收藏数+1
            int stars = video.getStar();
            video.setStar(stars + 1);
            videoHelper.updateById(video);
            Log.d(TAG, "onCreate: " + video.getStar());
            star.setText("收藏:" + video.getStar());
            //收藏关联关系
            Star star1 = new Star();
            star1.setUid(Integer.parseInt(uid));
            star1.setVid(Math.toIntExact(video.getId()));
            starHelper.insert(star1);

        });
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