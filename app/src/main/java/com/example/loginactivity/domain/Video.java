package com.example.loginactivity.domain;

import java.io.Serializable;

public class Video implements Serializable, Comparable {
    private Long id;
    private String title;
    private String content;
    private String source;
    private String time;
    private String url;
    private int star;
    private int likes;
    private int image;
    private int isRead;

    public Video(String title, String content, String source, String time, String url, int star, int likes, int image, int isRead) {
        this.title = title;
        this.content = content;
        this.source = source;
        this.time = time;
        this.url = url;
        this.star = star;
        this.likes = likes;
        this.image = image;
        this.isRead = isRead;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Video() {
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(Object o) {
        int compare = ((Video) o).getLikes() - this.getLikes();
        compare = compare + ((Video) o).getStar() * 10 - this.getStar() * 10;
        return compare;
    }

    @Override
    public String toString() {
        return "视频内容{" +
                "  \n编号=" + id +
                ", \n标题='" + title + '\'' +
                ", \n内容='" + content + '\'' +
                ", \nup者='" + source + '\'' +
                ", \n发行时间='" + time + '\'' +
                ", \n视频链接='" + url + '\'' +
                ", \n收藏=" + star +
                ", \n点赞=" + likes +
                ", \n图片编号=" + image +
                ", \n是否已读=" + isRead +
                "\n}";
    }
}
