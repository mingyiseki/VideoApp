package com.example.loginactivity.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.loginactivity.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserOpenHelper extends SQLiteOpenHelper {

    //定义表
    public static final String CREATE_STAR = "create table if not exists StarTable(" + "id integer primary key autoincrement," + "uid text," + "vid text);";
    public static final String CREATE_USER = "create table if not exists UsersTable(" + "id integer primary key autoincrement," + "phoneNumber text," + "password text," + "nikeName text);";
    public static final String CREATE_VIDEO = "create table if not exists VideoTable(" + "id integer primary key autoincrement," + "title text," + "content text," + "source text," + "time text," + "url text," + "star text," + "likes text," + "image text," + "is_read text);";
    private static final String DB_NAME = "main.db";
    private static final String TABLE_NAME = "UsersTable";
    private static final int VERSION = 2;
    private static UserOpenHelper helper;
    private SQLiteDatabase mRDB;
    private SQLiteDatabase mWDB;

    public UserOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    public long insert(User user) {
        ContentValues values = new ContentValues();
        values.put("phoneNumber", user.getPhoneNumber());
        values.put("password", user.getPassword());
        values.put("nikeName", user.getNikeName());
        return mWDB.insert(TABLE_NAME, null, values);
    }

    public long updateById(User user) {
        ContentValues values = new ContentValues();
        values.put("id", user.getId());
        values.put("phoneNumber", user.getPhoneNumber());
        values.put("password", user.getPassword());
        values.put("nikeName", user.getNikeName());
        return mWDB.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(user.getId())});
    }

    public long deleteById(int id) {
        return mWDB.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<User> queryAll() {
        List<User> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setPhoneNumber(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setNikeName(cursor.getString(3));
            list.add(user);
        }
        return list;
    }

    public User queryById(long id) {
        Cursor cursor = mRDB.query(TABLE_NAME, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        User user = new User();
        while (cursor.moveToNext()) {
            user.setId(cursor.getLong(0));
            user.setPhoneNumber(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setNikeName(cursor.getString(3));
        }
        return user;
    }

    public UserOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static UserOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new UserOpenHelper(context);
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
        db.execSQL(CREATE_STAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // 执行升级表的语句
            db.execSQL("DROP TABLE IF EXISTS VideoTable");
            db.execSQL("DROP TABLE IF EXISTS UsersTable");
            db.execSQL("DROP TABLE IF EXISTS StarTable");
            onCreate(db);
        }
    }


}
