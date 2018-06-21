package com.easybuild.android.gson;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

public class items extends DataSupport
{
    private _id _id;
    private String title;
    private String img;
    private String price;
    private String itemID;
    private String comments;
    private String type;
    private String source;
    private String prices;

    private String ueserID;

    private GPU gpu;
    private CPU cpu;

    @SerializedName("case")
    private Hardware mycase;

    private Hardware power;
    private Hardware cooler_water;
    private Hardware cooler_wind;
    private Hardware hdd;
    private Hardware ssd;
    private Hardware memory;
    private Hardware motherboard;

    public String getUeserID()
    {
        return ueserID;
    }

    public void setUeserID(String ueserID)
    {
        this.ueserID = ueserID;
    }

    public Hardware getMycase()
    {
        return mycase;
    }

    public void setMycase(Hardware mycase)
    {
        this.mycase = mycase;
    }

    public Hardware getPower()
    {
        return power;
    }

    public void setPower(Hardware power)
    {
        this.power = power;
    }

    public Hardware getCooler_water()
    {
        return cooler_water;
    }

    public void setCooler_water(Hardware cooler_water)
    {
        this.cooler_water = cooler_water;
    }

    public Hardware getCooler_wind()
    {
        return cooler_wind;
    }

    public void setCooler_wind(Hardware cooler_wind)
    {
        this.cooler_wind = cooler_wind;
    }

    public Hardware getHdd()
    {
        return hdd;
    }

    public void setHdd(Hardware hdd)
    {
        this.hdd = hdd;
    }

    public Hardware getSsd()
    {
        return ssd;
    }

    public void setSsd(Hardware ssd)
    {
        this.ssd = ssd;
    }

    public Hardware getMemory()
    {
        return memory;
    }

    public void setMemory(Hardware memory)
    {
        this.memory = memory;
    }

    public Hardware getMotherboard()
    {
        return motherboard;
    }

    public void setMotherboard(Hardware motherboard)
    {
        this.motherboard = motherboard;
    }

    public String getPrices()
    {
        return prices;
    }

    public void setPrices(String prices)
    {
        this.prices = prices;
    }


    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

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
