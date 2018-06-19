package com.easybuild.android.gson;

import org.json.JSONObject;

public class items
{
    private _id _id;
    private String title;
    private String img;
    private String price;
    private String itemID;
    private String comments;
    private String type;

    private GPU gpu;
    private CPU cpu;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getItemID()
    {
        return itemID;
    }

    public void setItemID(String itemID)
    {
        this.itemID = itemID;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public GPU getGpu()
    {
        return gpu;
    }

    public void setGpu(GPU gpu)
    {
        this.gpu = gpu;
    }

    public CPU getCpu()
    {
        return cpu;
    }

    public void setCpu(CPU cpu)
    {
        this.cpu = cpu;
    }

    public _id get_id()
    {
        return _id;
    }

    public void set_id(_id _id)
    {
        this._id = _id;
    }
}
