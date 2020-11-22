package com.java.bms.bean;

public class User {
    private String username;
    private String password;
    private Integer id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasword() {
        return password;
    }

    public void setPassword(String pasword) {
        this.password = pasword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", pasword='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
