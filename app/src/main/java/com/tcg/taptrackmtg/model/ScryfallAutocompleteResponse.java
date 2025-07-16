package com.tcg.taptrackmtg.model;

import java.util.List;

public class ScryfallAutocompleteResponse {

    private String object;
    private int total_values;
    private List<String> data;

    // Getters y setters
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getTotal_values() {
        return total_values;
    }

    public void setTotal_values(int total_values) {
        this.total_values = total_values;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
