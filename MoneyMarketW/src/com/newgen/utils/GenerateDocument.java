package com.newgen.utils;

import com.newgen.iforms.custom.IFormReference;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GenerateDocument implements Constants{
    private static Logger logger = LogGen.getLoggerInstance(GenerateDocument.class);

    public static String generateDoc (IFormReference ifr, String sessionId) {
            String docName = empty;
            if (Commons.getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
                docName = tbDocumentName;
            } else if (Commons.getProcess(ifr).equalsIgnoreCase(commercialProcess)) {
                docName = cpDocumentName;
            }
            String sTemplateName = docName;
            String sProcessName = ifr.getProcessName();
            String sActivityName = ifr.getActivityName();
            String cabinetName = ifr.getCabinetName();
            String responseXML = "";
            int portNo = 6089;
            String serverIp = ifr.getServerIp();
            String serverPort = "9443";
            logger.info("processname : "+sProcessName);
            logger.info("sActivityName : "+sActivityName);
            logger.info("sessionId : "+sessionId);
            logger.info("cabinate name : "+cabinetName);
            logger.info("serverIP : "+serverIp);
            logger.info("serverPort : "+serverPort);

          String requestXml = new StringBuilder().append("WI_NAME=").append(Commons.getWorkItemNumber(ifr)).append("~~JTS_IP=127.0.0.1~~JTS_PORT=3333~~SESSION_ID=").append(sessionId).append("~~SERVER_IP=").append(serverIp).append("~~SERVER_PORT=").append(serverPort).append("~~SERVER_NAME=WebSphere~~CABINET_NAME=").append(cabinetName).append("~~PROCESS_NAME=").append(sProcessName).append("~~TEMPLATE_NAME=").append(sTemplateName).append("~~ACTIVITY_NAME=").append(sActivityName).toString();
            logger.info("request xml is : "+requestXml);

            try {
                responseXML = callSocketServer(portNo, requestXml,ifr);
                logger.info("SocketCall : sResponseXML : " + responseXML);
            } catch (Exception e) {
                responseXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<message>\n"
                        + "<ErrorCode>1</ErrorCode>\n"
                        + "<ErrorDesc>Error while generating template.</ErrorDesc>\n"
                        + "</message>";
            }
            return responseXML;
    }

    private static String callSocketServer(int iPortNo, String requestXml,IFormReference ifr) {
        final String SS_EXEC_ERROR_MSG="Error from Call Client Socket Server while Web-Service execution";
        final String SS_CONN_ERROR_MSG="Could not connect to Call Client Socket Server";
        final String USER_DIR = System.getProperty("user.dir");
        final String FILE_SEP = System.getProperty("file.separator");
        String responseXml = "";
        String sTemp = "";
        try
        {
            logger.info("from call SocketServer try block");
            String serverIp = ifr.getServerIp();
            String tempResponseXml = "";
            Socket client = new Socket(serverIp, iPortNo);
            client.setSoTimeout(300000);

            try
            {
                DataOutputStream outData = new DataOutputStream(client.getOutputStream());
                byte[] dataByteArr = requestXml.getBytes("UTF-8");
                outData.writeInt(dataByteArr.length);
                outData.write(dataByteArr);
                DataInputStream in = new DataInputStream(client.getInputStream());
                int dataLength = in.readInt();
                byte[] data = new byte[dataLength];
                in.readFully(data);
                tempResponseXml = new String(data, "UTF-8");
                logger.info("CommonMethods : tempResponseXml : "+tempResponseXml);
                in.close();
            }
            catch (UnknownHostException e)
            {
                tempResponseXml = SS_CONN_ERROR_MSG;
            }
            catch (IOException e)
            {
                tempResponseXml = SS_CONN_ERROR_MSG;
            }
            catch (Exception e)
            {
                tempResponseXml = SS_EXEC_ERROR_MSG;
            }

            if(tempResponseXml==null || tempResponseXml.length()==0)
            {
                responseXml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        +"<message>\n"
                        + "<MainCode>-1</MainCode>\n"
                        + "<EDESC>No Response Received from Call Client Socket Server.</EDESC>\n"
                        + "</message>";
            }
            else
            {
                if (tempResponseXml.equals(SS_EXEC_ERROR_MSG)|| tempResponseXml.equals(SS_CONN_ERROR_MSG))
                {
                    responseXml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                            +"<message>\n"
                            + "<MainCode>-1</MainCode>\n"
                            + "<EDESC>"+tempResponseXml+"</EDESC>\n"
                            + "</message>";
                }
                else
                {
                    responseXml=tempResponseXml;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            responseXml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                    +"<message>\n"
                    + "<MainCode>-1</MainCode>\n"
                    + "<EDESC>Not able to Connect with Socket Server.</EDESC>\n"
                    + "</message>";
        }

        return sTemp+responseXml;
    }
}
