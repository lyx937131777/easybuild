package com.easybuild.android.gson;

import org.litepal.crud.DataSupport;

import java.util.List;

public class GPU extends DataSupport
{
    private _id _id;
    //型号
    private String Name;
    //架构
    private String Chip;
    //上市时间
    private String Released;
    //接口
    private String Bus;
    //显存大小
    private String Memory_Size;
    //显存类型
    private String Memory_Type;
    //显存位宽
    private String Memory_Bus;
    //核心频率
    private String GPU_Clock;
    //着色单元
    private String Shaders;
    private String TMUs;
    private String ROPs;
    private String TDP;
    private String Multiplier;

    private String img;

    private List<String> itemIDs;

    public List<String> getItemIDs()
    {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs)
    {
        this.itemIDs = itemIDs;
    }
    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public com.easybuild.android.gson._id get_id()
    {
        return _id;
    }

    public void set_id(com.easybuild.android.gson._id _id)
    {
        this._id = _id;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getChip()
    {
        return Chip;
    }

    public void setChip(String chip)
    {
        Chip = chip;
    }

    public String getReleased()
    {
        return Released;
    }

    public void setReleased(String released)
    {
        Released = released;
    }

    public String getBus()
    {
        return Bus;
    }

    public void setBus(String bus)
    {
        Bus = bus;
    }

    public String getMemory_Size()
    {
        return Memory_Size;
    }

    public void setMemory_Size(String memory_Size)
    {
        Memory_Size = memory_Size;
    }

    public String getMemory_Type()
    {
        return Memory_Type;
    }

    public void setMemory_Type(String memory_type)
    {
        Memory_Type = memory_type;
    }

    public String getMemory_Bus()
    {
        return Memory_Bus;
    }

    public void setMemory_Bus(String memory_bus)
    {
        Memory_Bus = memory_bus;
    }

    public String getGPU_Clock()
    {
        return GPU_Clock;
    }

    public void setGPU_Clock(String GPU_Clock)
    {
        this.GPU_Clock = GPU_Clock;
    }

    public String getShaders()
    {
        return Shaders;
    }

    public void setShaders(String shaders)
    {
        Shaders = shaders;
    }

    public String getTMUs()
    {
        return TMUs;
    }

    public void setTMUs(String TMUs)
    {
        this.TMUs = TMUs;
    }

    public String getROPs()
    {
        return ROPs;
    }

    public void setROPs(String ROPs)
    {
        this.ROPs = ROPs;
    }

    public String getTDP()
    {
        return TDP;
    }

    public void setTDP(String TDP)
    {
        this.TDP = TDP;
    }

    public String getMultiplier()
    {
        return Multiplier;
    }

    public void setMultiplier(String multiplier)
    {
        Multiplier = multiplier;
    }
}
