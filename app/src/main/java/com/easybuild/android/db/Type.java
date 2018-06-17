package com.easybuild.android.db;

import org.litepal.crud.DataSupport;

public class Type extends DataSupport
{
    private String name;
    private int imageID;
    private String typeName;

    public Type(String name, int imageID, String typeName)
    {
        this.name = name;
        this.imageID = imageID;
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getImageID()
    {
        return imageID;
    }

    public void setImageID(int imageID)
    {
        this.imageID = imageID;
    }
}
