package com.wong.elapp.pojo.vo;

/**
 * 这个类是用户登录时的账号密码的json类的实体类
 */
public class LoginParam {
    String username;
    String password;

    public LoginParam(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
