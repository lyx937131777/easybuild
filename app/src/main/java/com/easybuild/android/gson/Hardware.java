package com.easybuild.android.gson;

import org.litepal.crud.DataSupport;

import java.util.List;

public class Hardware extends DataSupport
{
    private _id _id;
    private String string1;
    private String string2;
    private String string3;
    private String img;
    private List<String> itemIDs;

    public com.easybuild.android.gson._id get_id()
    {
        return _id;
    }

    public void set_id(com.easybuild.android.gson._id _id)
    {
        this._id = _id;
    }

    public String getString1()
    {
        return string1;
    }

    public void setString1(String string1)
    {
        this.string1 = string1;
    }

    public String getString2()
    {
        return string2;
    }

    public void setString2(String string2)
    {
        this.string2 = string2;
    }

    public String getString3()
    {
        return string3;
    }

    public void setString3(String string3)
    {
        this.string3 = string3;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public List<String> getItemIDs()
    {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs)
    {
        this.itemIDs = itemIDs;
    }
}
