package com.easybuild.android.db;

import org.litepal.crud.DataSupport;

public class User extends DataSupport
{
    private String userID;
    private String password;
    private String nickname;
    private String phone_number;
    private String profile_photo;

    public String getProfile_photo()
    {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo)
    {
        this.profile_photo = profile_photo;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }
}