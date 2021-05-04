package com.newgen.api.execute;

import com.newgen.utils.SocketService;



public class Api {
    public static String executeCall(String serviceName, String requestXml){
        return new SocketService().executeIntegrationCall(serviceName,requestXml);
    }
}
