package com.java.bms.other.DO;


/**
 * 不管什么类型的用户注册后都可以只有用户名和密码，其他信息可以之后填写
 * 所以设置这个UserVO类用来登录和注册
 */

public class UserDO {
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
