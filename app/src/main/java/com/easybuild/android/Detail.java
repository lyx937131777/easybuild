package com.easybuild.android;

public class Detail
{
    private String detail_name;
    private String detail_attribute;
    private String chooser1;
    private String chooser2;
    private String chooser3;

    public Detail(String detail_name, String detail_attribute, String chooser1, String chooser2,
                  String chooser3)
    {
        this.detail_name = detail_name;
        this.detail_attribute = detail_attribute;
        this.chooser1 = chooser1;
        this.chooser2 = chooser2;
        this.chooser3 = chooser3;
    }

    public String getDetail_attribute()
    {
        return detail_attribute;
    }

    public void setDetail_attribute(String detail_attribute)
    {
        this.detail_attribute = detail_attribute;
    }

    public String getDetail_name()
    {
        return detail_name;
    }

    public void setDetail_name(String detail_name)
    {
        this.detail_name = detail_name;
    }

    public String getChooser1()
    {
        return chooser1;
    }

    public void setChooser1(String chooser1)
    {
        this.chooser1 = chooser1;
    }

    public String getChooser2()
    {
        return chooser2;
    }

    public void setChooser2(String chooser2)
    {
        this.chooser2 = chooser2;
    }

    public String getChooser3()
    {
        return chooser3;
    }

    public void setChooser3(String chooser3)
    {
        this.chooser3 = chooser3;
    }
}
