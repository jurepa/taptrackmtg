package com.tcg.empires.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ScryfallCardDetailList implements Serializable {

    private String object;

    private List<ScryfallDetailCard> data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<ScryfallDetailCard> getData() {
        return data;
    }

    public void setData(List<ScryfallDetailCard> data) {
        this.data = data;
    }
}
