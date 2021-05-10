package com.newgen.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class SocketService {
    private final String propFile = System.getProperty("user.dir") + File.separator + "INTEGRATION_Properties" + File.separator + "Integration.properties";
    private final Properties prop = loadPropertyFile(propFile);
    private final String socketIP = prop.getProperty("SocketIP");
    private final int socketPort = Integer.parseInt(prop.getProperty("SocketPort"));
    private static final Logger logger = LogGen.getLoggerInstance(SocketService.class);

    public String executeIntegrationCall(String serviceName, String inputXml){
        String result = "";
        try
        {
            String  requestXml = serviceName + "~" + inputXml + "~";
            logger.info("Request Xml to Socket-- "+ requestXml);
            logger.info("Socket IP-- "+ socketIP);
            logger.info("Socket Port-- "+ socketPort);

            Socket socket = new Socket(socketIP, socketPort);
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
