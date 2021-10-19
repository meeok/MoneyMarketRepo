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
    public static String vat;
    public static String custodyFee;
    public static String txnFee;
    public static String commission;
    static {
        try {
            logger.info("Start loading properties file");
            Properties properties = new Properties();
            InputStream in = new FileInputStream(configPath);
            properties.load(in);

            processDefId = properties.getProperty(processDefIdField);
            mailFrom = properties.getProperty(mailFromField);
            serverPort = properties.getProperty(serverPortField);
            serverIp = properties.getProperty(serverIpField);
            templatePort = properties.getProperty(templatePortField);
            vat = properties.getProperty(vatField);
            custodyFee = properties.getProperty(custodyFeeField);
            txnFee = properties.getProperty(txnField);
            commission = properties.getProperty(commissionField);
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
