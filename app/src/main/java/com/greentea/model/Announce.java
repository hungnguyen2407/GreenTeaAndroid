package com.greentea.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author Nguyen Hung
 * @version 1.1
 * Cai dat cac thuoc tinh va phuong thuc cua thong bao
 */
public class Announce implements Serializable {
    private int id;
    private String header;
    private String group;
    private String content;
    private String img;
    private String date;

    public Announce(String header, String group, String content, String img) {
        this.header = header;
        this.group = group;
        this.content = content;
        this.img = img;
        this.date = new Date(System.currentTimeMillis()).toString();
    }

    public Announce(JSONObject announceJSON)
    {
        try {
            id = announceJSON.getInt("postId");
            header = announceJSON.getString("title");
            group = announceJSON.getString("classId");
            content = announceJSON.getString("content");
            date = announceJSON.getString("date");
//            img = announceJSON.getString("img");
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
