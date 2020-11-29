package com.java.bms.common.bean;

import java.util.Date;

public class CongressVO {
    private long congressId;
    private int organizerId;
    private String title;
    private String describe;
    private Date time;
    private String place;
    private long image;

    public long getCongressId() {
        return congressId;
    }

    public void setCongressId(long congressId) {
        this.congressId = congressId;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getImage() {
        return image;
    }

    public void setImage(long image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "congressVO{" +
                "congressId=" + congressId +
                ", organizerId=" + organizerId +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", time=" + time +
                ", place='" + place + '\'' +
                ", image=" + image +
                '}';
    }
}
