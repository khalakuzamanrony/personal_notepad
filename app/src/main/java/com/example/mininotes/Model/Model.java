package com.example.mininotes.Model;

import java.io.Serializable;

public class Model implements Serializable {
    String title, note, index, time;


    public Model(String title, String note, String index, String time) {
        this.title = title;
        this.note = note;
        this.index = index;
        this.time = time;
    }

    public Model() {
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
