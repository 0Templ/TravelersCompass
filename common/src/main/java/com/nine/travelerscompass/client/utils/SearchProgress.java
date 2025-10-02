package com.nine.travelerscompass.client.utils;

public class SearchProgress {

    public int progress;

    public int total;

    public SearchProgress(int p, int t){
        this.progress = p;
        this.total = t;
    }

    public void update(int p, int t){
        this.progress = p;
        this.total = t;
    }
}
