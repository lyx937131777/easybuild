package com.easybuild.android.gson;

public class CPU
{
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
    //一级缓存
    private String CacheL1;
    private String CacheL2;
    private String CacheL3;
    private String TDP;
    //上市时间
    private String Released;

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
