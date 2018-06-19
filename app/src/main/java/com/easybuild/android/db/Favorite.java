package com.easybuild.android.db;

import org.litepal.crud.DataSupport;

public class Favorite extends DataSupport
{
    private String userID;
    private String type;
    private String favorite_id;

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getFavorite_id()
    {
        return favorite_id;
    }

    public void setFavorite_id(String favorite_id)
    {
        this.favorite_id = favorite_id;
    }
}
