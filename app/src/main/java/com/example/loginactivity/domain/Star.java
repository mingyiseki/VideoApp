package com.example.loginactivity.domain;

public class Star {
    Long id;
    //用户
    int uid;
    //视频
    int vid;

    public Star() {
    }

    public Star(int uid, int vid) {
        this.uid = uid;
        this.vid = vid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    @Override
    public String toString() {
        return "Star{" + "id=" + id + ", uid=" + uid + ", vid=" + vid + '}';
    }
}
