package com.newgen.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class SocketService {
    private final String propFile = System.getProperty("user.dir") + File.separator + "INTEGRATION_Properties" + File.separator + "Integration.properties";
    private final Properties prop = loadPropertyFile(propFile);
    private final String SocketIP = prop.getProperty("SocketIP");
    private final String tmpSocketPort = prop.getProperty("SocketPort");
    private final int SocketPort = Integer.parseInt(tmpSocketPort);

    public String executeIntegrationCall(String serviceName, String inputXml){
          String result = "";
          String  requestXml = serviceName + "~" + inputXml + "~";
        try
        {
            Socket socket = new Socket(SocketIP, SocketPort);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.write(requestXml.getBytes(StandardCharsets.UTF_16LE));
            dataOutputStream.flush();

            try
            {
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                byte[] readBuffer = new byte[1000000];
                int num = in.read(readBuffer);
                if (num > 0) {
                    byte[] arrayBytes = new byte[num];
                    System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
                    result = new String(arrayBytes, StandardCharsets.UTF_16LE);
                    System.out.println("Result===>>" + result);
                }
            } catch (IOException localSocketException)
            {
                localSocketException.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Properties loadPropertyFile(String filePath) {
        Properties propObj = null;
        try {
            propObj = new Properties();
            propObj.load(new FileInputStream(filePath));
        } catch (IOException e) {
            propObj = null;
        }
        return propObj;
    }
}
