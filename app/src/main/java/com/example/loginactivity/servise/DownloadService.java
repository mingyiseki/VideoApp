package com.example.loginactivity.servise;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.example.loginactivity.BroadcastReceiver.MyReceiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DownloadService extends Service {
    private String text;
    private String title;
    private MyReceiver myReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 在这里执行下载操作
        // 这可以是使用线程、异步任务等方式来下载内容title
        text = intent.getStringExtra("text");
        title = intent.getStringExtra("title");
        //下载文件
        String path = createTxtFile(text);
        //发送自定义广播
        IntentFilter intentFilter = new IntentFilter("broadcast");
        myReceiver = new MyReceiver();
        Intent intentReceiver = new Intent("broadcast");
        //直接发送广播
        intentReceiver.putExtra("broadcast", "请前往路径: " + path + " 查看新闻内容详情");
        registerReceiver(myReceiver, intentFilter);
        sendBroadcast(intentReceiver);

        return START_STICKY; // 指示系统在服务被终止后重新启动服务
    }

    private String createTxtFile(String text) {
        String path = "";
        try {
            // 指定文件保存路径和文件名
            File directory = new File(Environment.getExternalStorageDirectory(), "/video");
            System.out.println(Environment.getExternalStorageDirectory());
            System.out.println(directory.getPath());
            path = directory.getPath();
            if (!directory.exists()) {
                directory.mkdirs(); // 如果目录不存在，则创建目录
            }
            File file = new File(directory, title);

            // 创建文件并写入内容
            FileWriter writer = new FileWriter(file);
            writer.append(text);
            writer.flush();
            writer.close();

            // 执行下载
            startDownload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    private void startDownload(File file) {
        try {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            Uri uri = Uri.fromFile(file);

            DownloadManager.Request request;

            request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

            downloadManager.enqueue(request);
        } catch (Exception e) {
            return;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // 如果服务不提供绑定功能，返回null即可
        return null;
    }
}