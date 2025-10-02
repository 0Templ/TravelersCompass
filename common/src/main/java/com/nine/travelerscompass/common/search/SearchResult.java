package com.nine.travelerscompass.common.search;

import com.nine.travelerscompass.common.search.location.ILocationObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult {

    private static final int MAX_SIZE = 100_000;
    private static final int MAX_PER_KEY = 1000;

    private final List<ILocationObject> result = new ArrayList<>();
    private final Map<String, Integer> keyCounts = new HashMap<>();

    private static String extractKey(ILocationObject locationObject){
        return locationObject.descriptionId();
    }

    public void addAll(List<ILocationObject> locationObjects){
        for (var obj : locationObjects){
            add(obj);
        }
    }

    public boolean add(ILocationObject locationObject){
        if (result.size() > MAX_SIZE){
            return false;
        }
        String key = extractKey(locationObject);
        int current = keyCounts.getOrDefault(key, 0);
        if (current >= MAX_PER_KEY){
            return false;
        }
        keyCounts.put(key, current + 1);
        result.add(locationObject);
        return true;
    }

    public List<ILocationObject> get(){
        return result;
    }

}
