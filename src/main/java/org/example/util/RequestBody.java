package org.example.util;

import java.util.List;

public class RequestBody {
    private List<List<Integer>> data;

    public RequestBody(List<List<Integer>> data) {
        this.data = data;
    }

    public List<List<Integer>> getData() {
        return data;
    }

    public void setData(List<List<Integer>> data) {
        this.data = data;
    }
}
