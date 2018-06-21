package com.easybuild.android;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable
{
    private String title;
    private String date;
    private String author;
    private String Content;
    private int imgID;

    public Article(String title, String date, String author, String content, int imgID)
    {
        this.title = title;
        this.date = date;
        this.author = author;
        Content = content;
        this.imgID = imgID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getContent()
    {
        return Content;
    }

    public void setContent(String content)
    {
        Content = content;
    }

    public int getImgID()
    {
        return imgID;
    }

    public void setImgID(int imgID)
    {
        this.imgID = imgID;
    }
}
