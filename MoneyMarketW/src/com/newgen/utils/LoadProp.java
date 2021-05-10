package com.newgen.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class LoadProp implements  Constants {
    private static final Logger logger = LogGen.getLoggerInstance(LoadProp.class);
    public static String processDefId;
    public static String mailFrom;
    public static String serverPort;
    public static String serverIp;
    public static String templatePort;
    static {
        try {
            logger.info("Start loading properties file");
            Properties properties = new Properties();
            InputStream in = new FileInputStream(configPath2);
            properties.load(in);

            processDefId = properties.getProperty(processDefIdField);
            mailFrom = properties.getProperty(mailFromField);
            serverPort = properties.getProperty(serverPortField);
            logger.info("serverPort-- "+serverPort);
            serverIp = properties.getProperty(serverIpField);
            logger.info("serverIp-- "+serverIp);
            templatePort = properties.getProperty(templatePortField);
            logger.info("templatePort-- "+templatePort);

        }
        catch  (UnsupportedEncodingException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - Unsupported Exception -- "+ ex.getMessage() );
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - FileNotFoundException Exception-- "+ ex.getMessage());
        }
        catch (IOException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - IOException Exception-- "+ ex.getMessage());
        }
    }
}
