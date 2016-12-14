package com.roc.core.user;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/07/26
 * Time: 11:10 pm
 * Desc: 用户对象类
 */
public class UserDTO extends BaseDTO {

    public static final String Fid              = "fid";
    public static final String User_Name        = "user_name";
    public static final String Nick_Name        = "nick_name";
    public static final String Password_Hash    = "user_password_hash";
    public static final String Auth_Key         = "auth_key";
    public static final String Email            = "email";

    public UserDTO() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = UserDTO.Fid;
        this.isAutoIncrease = true;
    }

    private int fid;
    private String user_name;
    private String nick_name;
    private String password_hash;
    private String auth_key;
    private String email;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
