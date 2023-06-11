package com.example.loginactivity.domain;

public class User {
    private Long id;
    //账号是手机号
    private String phoneNumber;
    private String password;
    private String nikeName;

    public User() {
    }

    public User(String phoneNumber, String password, String nikeName) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nikeName = nikeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", phoneNumber='" + phoneNumber + '\'' + ", password='" + password + '\'' + ", nikeName='" + nikeName + '\'' + '}';
    }
}
