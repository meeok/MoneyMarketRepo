package com.newgen.api;

import java.util.HashMap;
import java.util.Map;

public class FetchLimit {
    public static Map<String ,String> fetchLimit(){
        Map<String,String > limit = new HashMap<>();

        limit.put("NGN","2000000000");
        limit.put("USD","2000000000");
        limit.put("GBP","2000000000");
        limit.put("EUR","2000000000");

        return limit;
    }
}
