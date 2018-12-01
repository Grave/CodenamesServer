package com.codejam.model;

import java.util.ArrayList;
import java.util.List;

public class WordList {
    private List<String> data;

    public WordList() {
        this.data = new ArrayList<>();
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
