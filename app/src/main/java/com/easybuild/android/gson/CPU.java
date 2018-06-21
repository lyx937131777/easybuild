package com.easybuild.android.gson;

import org.litepal.crud.DataSupport;

import java.util.List;

public class CPU extends DataSupport
{
    private _id _id;
    //型号
    private String Name;
    //架构
    private String Codename;
    //核心数
    private String Cores;
    //线程数
    private String Threads;
    //接口
    private String Socket;
    //工艺
    private String Process;
    //核心频率
    private String Clock;
    private String Multi;
    //一级缓存
    private String CacheL1;
    private String CacheL2;
    private String CacheL3;
    private String TDP;
    //上市时间
    private String Released;

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

    public String getMulti()
    {
        return Multi;
    }

    public void setMulti(String multi)
    {
        Multi = multi;
    }

    public com.easybuild.android.gson._id get_id()
    {
        return _id;
    }

    public void set_id(com.easybuild.android.gson._id _id)
    {
        this._id = _id;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getCodename()
    {
        return Codename;
    }

    public void setCodename(String codename)
    {
        Codename = codename;
    }

    public String getCores()
    {
        return Cores;
    }

    public void setCores(String cores)
    {
        Cores = cores;
    }

    public String getThreads()
    {
        return Threads;
    }

    public void setThreads(String threads)
    {
        Threads = threads;
    }

    public String getSocket()
    {
        return Socket;
    }

    public void setSocket(String socket)
    {
        Socket = socket;
    }

    public String getProcess()
    {
        return Process;
    }

    public void setProcess(String process)
    {
        Process = process;
    }

    public String getClock()
    {
        return Clock;
    }

    public void setClock(String clock)
    {
        Clock = clock;
    }

    public String getCacheL1()
    {
        return CacheL1;
    }

    public void setCacheL1(String cacheL1)
    {
        CacheL1 = cacheL1;
    }

    public String getCacheL2()
    {
        return CacheL2;
    }

    public void setCacheL2(String cacheL2)
    {
        CacheL2 = cacheL2;
    }

    public String getCacheL3()
    {
        return CacheL3;
    }

    public void setCacheL3(String cacheL3)
    {
        CacheL3 = cacheL3;
    }

    public String getTDP()
    {
        return TDP;
    }

    public void setTDP(String TDP)
    {
        this.TDP = TDP;
    }

    public String getReleased()
    {
        return Released;
    }

    public void setReleased(String released)
    {
        Released = released;
    }
}
