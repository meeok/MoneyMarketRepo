package com.newgen.controller;

import com.newgen.api.execute.Api;
import com.newgen.api.generateXml.RequestXml;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;
import com.newgen.utils.LogGen;
import com.newgen.utils.XmlParser;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class OmoApiController extends Commons implements Constants {
	
	private static final Logger logger = LogGen.getLoggerInstance(OmoApiController.class);
    private String outputXml;
    private final XmlParser xmlParser = new XmlParser();
    private final IFormReference ifr;
    
    public OmoApiController(IFormReference ifr) {
        this.ifr = ifr;
    }
    
  //get account output xml
    public void setFecthActDtlsInputXml(String accountNumber)
    {
          if (accountNumber.startsWith("1"))
              outputXml = Api.executeCall(fetchCaaAcctServiceName, RequestXml.fetchCaaRequestXml(accountNumber));
          else if (accountNumber.startsWith("2"))
              outputXml = Api.executeCall(fetchOdaAcctServiceName, RequestXml.fetchOdaRequestXml(accountNumber));
          else if (accountNumber.startsWith("3"))
              outputXml = Api.executeCall(fetchSbaAcctServiceName, RequestXml.fetchSbaRequestXml(accountNumber));
	  
    }
    
    public String fetchAcctDetails(String accountNumber){
		String retMsg ="";  
		logger.info("account Number-- "+ accountNumber );
        try {
            logger.info("Fetch account details call");
            
            if (!accountNumber.isEmpty()) {
            	setFecthActDtlsInputXml(accountNumber);
                logger.info("fetch account outputXml-- " + outputXml);
                
                if (!isEmpty(outputXml)) {
                	 xmlParser.setInputXML(outputXml);
                     //get the status tag of the HostTransactiontag
                     String hosttxntag = xmlParser.getValueOf("HostTransaction");
                     logger.info("hosttxntag: "+hosttxntag);
                     xmlParser.setInputXML(hosttxntag);
                     String status = xmlParser.getValueOf(apiStatus);
                     logger.info("Status >>>>: " + status);
                     xmlParser.setInputXML(outputXml);

                    if (isSuccess(status)) {
                    	return outputXml;
                    } else if (isFailed(status)) {
                        String errCode = xmlParser.getValueOf("ErrorCode");
                        String errDesc = xmlParser.getValueOf("ErrorDesc");
                        String errType = xmlParser.getValueOf("ErrorType");
                        logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
                        retMsg= apiFailed;
                    }
                } else retMsg = apiNoResponse;
            }
           
        } catch (Exception e){
            logger.info("exception occurred-- "+e.getMessage());
        }
        return retMsg;
		
	}
    
    public String fetchSingleAcctDetails(){
		String retMsg ="";  
		String accountNumber = getOmoCustAcctNo(ifr).trim();
		logger.info("account Number-- "+ accountNumber );
        try {
            logger.info("Fetch omo account details call");
            clearFields(ifr, new String[]{omoCustName, omoCustCif, omoCustCurr,omoCustSolid});
            if (!accountNumber.isEmpty()) {
            	setFecthActDtlsInputXml(accountNumber);
                logger.info("fetch account outputXml-- " + outputXml);
                if (!isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);
                    String status = xmlParser.getValueOf(apiStatus);
                    if (isSuccess(status)) {
                        String currency = xmlParser.getValueOf("currencyCode");
                        logger.info("currency- "+ currency);
                        String email = xmlParser.getValueOf("EmailAddr");
                        logger.info("email- "+ email);
                        String sol = xmlParser.getValueOf("SOL");
                        logger.info("sol- "+ sol);
                        String cusDetails = xmlParser.getValueOf("PersonName");
                        xmlParser.setInputXML(cusDetails);
                        String name = xmlParser.getValueOf("Name");
                        logger.info("name- "+ name);
                       
                        setFields(ifr, new String[]{omoCustName, omoCustCif, omoCustCurr,omoCustSolid}, new String[]{name, "",currency, sol});
                     
                    } else if (isFailed(status)) {
                        String errCode = xmlParser.getValueOf("ErrorCode");
                        String errDesc = xmlParser.getValueOf("ErrorDesc");
                        String errType = xmlParser.getValueOf("ErrorType");
                        logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
                        return "ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";
                    }
                } else return apiNoResponse;
            }
           
        } catch (Exception e){
            logger.info("exception occurred-- "+e.getMessage());
        }
        return "";
	}
    
    public String tokenValidation(String otp){
        logger.info("Welcome to token validation call");
        logger.info("otp>>>>"+otp);
        if (!otp.isEmpty()){
        	Commons.disableFields(ifr,new String[]{omoToken});
        	Commons.setVisible(ifr,new String[]{omoPostBtn,omoTranId});
            Commons.enableFields(ifr,new String[]{omoPostBtn});
            
           /* outputXml = Api.executeCall(tokenValidationServiceName,RequestXml.tokenValidationXml(Commons.getLoginUser(ifr),otp));
            logger.info("outputXml-- "+outputXml);

            if (!outputXml.isEmpty()) {
                xmlParser.setInputXML(outputXml);
                String status = xmlParser.getValueOf("a:Authenticated");
                logger.info("token status-- "+status);
                if (status.equalsIgnoreCase(True)) {
                    Commons.disableFields(ifr,new String[]{omoToken});
                    Commons.enableFields(ifr,new String[]{omoPostBtn});
                }
                else if (status.equalsIgnoreCase(False)){
                    logger.info("token errMgs-- "+ xmlParser.getValueOf("a:Message"));
                    return xmlParser.getValueOf("a:Message");
                }
            }
                else return apiNoResponse;
       */ }
        return "token validated successfully";
    }
    
    
    public String getPostTxn(String debitAcct, String debitSol, String amount, String transParticulars, String partTranRemarks, String creditAcct, String creditSol){
        logger.info("Welcome to post transaction call");
        try {
            if (Integer.parseInt(amount) > 0) {
            	String rqstXml = RequestXml.postTransactionXml(transType, transSubTypeC, debitAcct, debitSol, debitFlag, amount, currencyNgn, 
            			transParticulars, partTranRemarks, Commons.getCurrentDate(), creditAcct, creditSol, creditFlag, Commons.getLoginUser(ifr));
            	logger.info("Request XML-- "+rqstXml);
            	outputXml = Api.executeCall(postServiceName, rqstXml);
                logger.info("outputXml-- "+outputXml);
                if (!Commons.isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);
                    String status = xmlParser.getValueOf(apiStatus);

                    if (isSuccess(status)) {
                        String txnId = xmlParser.getValueOf("TrnId");
                        if (!Commons.isEmpty(txnId.trim())) {
                            return apiSuccess +":" +txnId.trim();
                            }
                        
                    } else if (isFailed(status)) {
                        String errCode = xmlParser.getValueOf("ErrorCode");
                        String errDesc = xmlParser.getValueOf("ErrorDesc");
                        String errType = xmlParser.getValueOf("ErrorType");
                        logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
                        return apiFailed + " ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";
                    }
                } else return apiNoResponse;
            }
        } catch (Exception e){
            return e.getMessage();
        }
        return "";
    }
    private boolean isSuccess(String data){
        return data.equalsIgnoreCase(apiSuccess);
    }
    private boolean isFailed(String data){
        return data.equalsIgnoreCase(apiFailed) || data.equalsIgnoreCase(apiFailure);
    }
    

}
