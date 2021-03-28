package com.newgen.api;

import java.util.HashMap;
import java.util.Map;

public class FetchAccountDetails {

    public static Map<String,String> fetchAccountDetails(){
        Map<String,String> data = new HashMap<>();
        data.put("name","Kufre Godwin Udoko");
        data.put("email","kelmorgan18@gmail.com");
        data.put("schemeCodePass","SA200");
        data.put("schemeCode1","SA310");
        data.put("schemeCode2","SA340");
        data.put("schemeCode3","SA327");
        data.put("schemeCode4","SA231");
        return data;
    }
}
