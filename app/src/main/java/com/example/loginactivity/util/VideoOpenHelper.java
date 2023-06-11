package com.example.loginactivity.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.loginactivity.domain.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoOpenHelper extends SQLiteOpenHelper {

    //定义表
    public static final String CREATE_VIDEO = "create table if not exists VideoTable(" +
            "id integer primary key autoincrement," +
            "title text," + "content text," + "source text," +
            "time text," + "url text," + "star text," + "likes text," +
            "image text," + "is_read text);";
    public static final String CREATE_USER = "create table if not exists UsersTable(" + "id integer primary key autoincrement," + "phoneNumber text," + "password text," + "nikeName text);";
    private static final String DB_NAME = "main.db";
    private static final String TABLE_NAME = "VideoTable";
    private static final int VERSION = 2;
    private static VideoOpenHelper helper;
    private SQLiteDatabase mRDB;
    private SQLiteDatabase mWDB;

    public VideoOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    public long insert(Video video) {
        ContentValues values = new ContentValues();
        values.put("title", video.getTitle());
        values.put("content", video.getContent());
        values.put("source", video.getSource());
        values.put("time", video.getTime());
        values.put("url", video.getUrl());
        values.put("star", video.getStar());
        values.put("likes", video.getLikes());
        values.put("image", video.getImage());
        values.put("is_read", video.getIsRead());
        return mWDB.insert(TABLE_NAME, null, values);
    }

    public long updateById(Video video) {
        ContentValues values = new ContentValues();
        values.put("id", video.getId());
        values.put("title", video.getTitle());
        values.put("content", video.getContent());
        values.put("source", video.getSource());
        values.put("time", video.getTime());
        values.put("url", video.getUrl());
        values.put("star", video.getStar());
        values.put("likes", video.getLikes());
        values.put("image", video.getImage());
        values.put("is_read", video.getIsRead());
        return mWDB.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(video.getId())});
    }

    public long deleteById(int id) {
        return mWDB.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Video> queryAll() {
        List<Video> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Video video = new Video();
            video.setId(cursor.getLong(0));
            video.setTitle(cursor.getString(1));
            video.setContent(cursor.getString(2));
            video.setSource(cursor.getString(3));
            video.setTime(cursor.getString(4));
            video.setUrl(cursor.getString(5));
            video.setStar(cursor.getInt(6));
            video.setLikes(cursor.getInt(7));
            video.setImage(cursor.getInt(8));
            video.setIsRead(cursor.getInt(9));
            list.add(video);
        }
        return list;
    }

    public Video queryById(long id) {
        Cursor cursor = mRDB.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        Video video = new Video();
        while (cursor.moveToNext()) {
            video.setId(cursor.getLong(0));
            video.setTitle(cursor.getString(1));
            video.setContent(cursor.getString(2));
            video.setSource(cursor.getString(3));
            video.setTime(cursor.getString(4));
            video.setUrl(cursor.getString(5));
            video.setStar(cursor.getInt(6));
            video.setLikes(cursor.getInt(7));
            video.setImage(cursor.getInt(8));
            video.setIsRead(cursor.getInt(9));
        }
        return video;
    }

    public VideoOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static VideoOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new VideoOpenHelper(context);
        }
        return helper;
    }

    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = helper.getReadableDatabase();
        }
        return mRDB;
    }

    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = helper.getWritableDatabase();
        }
        return mWDB;
    }

    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }
        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VIDEO);
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // 执行升级表的语句
            db.execSQL("DROP TABLE IF EXISTS VideoTable");
            db.execSQL("DROP TABLE IF EXISTS UsersTable");
            onCreate(db);
        }
    }


}
