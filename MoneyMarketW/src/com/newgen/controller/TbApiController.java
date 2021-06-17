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

public class TbApiController  extends Commons implements Constants{
	private static final Logger logger = LogGen.getLoggerInstance(TbApiController.class);
    private String outputXml;
    private final XmlParser xmlParser = new XmlParser();
    private final IFormReference ifr;

    public TbApiController(IFormReference ifr) {
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
    //check if schemecode is valid for treasury processing
    public boolean isValidschemeCode(String schemeCode) {
    	return  (schemeCode.equalsIgnoreCase(SA231) || schemeCode.equalsIgnoreCase(SA310) ||
    			schemeCode.equalsIgnoreCase(SA340) ||schemeCode.equalsIgnoreCase(SA327)) ? false: true;
    }
    
    //for secondary market scheme code PB2010
    public void setSMPB2010Fields(String schemecode) {
    	if(getTbCustSchemeCode(ifr).equalsIgnoreCase(PB2010)) {
			setVisible(ifr, tb_SmCustBidRemark);
			setMandatory(ifr, tb_SmCustBidRemark);
		}
		else {
			clearFields(ifr, tb_SmCustBidRemark);
			hideField(ifr,tb_SmCustBidRemark);
			undoMandatory(ifr, tb_SmCustBidRemark);
		}	
		
    }
    
	public String fetchAcctDetails(){
		String retMsg ="";
        try {
            logger.info("Fetch account details call");
            String accountNumber = getTbCustAcctNo(ifr).trim();
            logger.info("account Number-- "+ accountNumber );
            clearFields(ifr, new String[]{tbCustAcctEmail, tbCustAcctName, tbCustAcctLienStatus,tbCustSchemeCode,tbCustSolid});
            if (!accountNumber.isEmpty()) {
            	setFecthActDtlsInputXml(accountNumber);
                logger.info("fetch account outputXml-- " + outputXml);

                if (!isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);

                    String status = xmlParser.getValueOf(apiStatus);

                    if (isSuccess(status)) {
                        String schemeCode = xmlParser.getValueOf("SchmCode");
                        logger.info("schemeCode- "+ schemeCode);
                        
                        if(!isValidschemeCode(schemeCode)) {
                        	return "This account is not valid for TB processing";
                        }
                        String email = xmlParser.getValueOf("EmailAddr");
                        logger.info("email- "+ email);
                        String sol = xmlParser.getValueOf("SOL");
                        logger.info("sol- "+ sol);
                        String cusDetails = xmlParser.getValueOf("PersonName");
                        xmlParser.setInputXML(cusDetails);
                        String name = xmlParser.getValueOf("Name");
                        logger.info("name- "+ name);
                        if (isEmpty(email)) {
                            setFields(ifr, new String[]{tbCustAcctName,tbCustSchemeCode, tbCustSolid}, new String[]{name,schemeCode, sol});
                            enableFields(ifr, new String[]{tbCustAcctEmail});
                            retMsg = "Update email of customer on account maintenance workflow";
                        } else {
                            setFields(ifr, new String[]{tbCustAcctEmail, tbCustAcctName,tbCustSchemeCode, tbCustSolid}, new String[]{email, name,schemeCode, sol});
                            disableFields(ifr, new String[]{tbCustAcctEmail});
                        }
                        setVisible(ifr, tbFetchMandatebtn);
                        //scheme code PB2010
                        if(getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
                        	setSMPB2010Fields(schemeCode);
                        }
                        
                       //fetch lien status
                        fetchLien();
                        return retMsg;
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
	
	public String fetchLien(){
        try {
            logger.info("Fetch lien call");
            outputXml = Api.executeCall(fetchLienServiceName, RequestXml.fetchLienRequestXml(getTbCustAcctNo(ifr).trim()));
            logger.info("outputXml-- " + outputXml);
            if (!isEmpty(outputXml)) {
                xmlParser.setInputXML(outputXml);
                String status = xmlParser.getValueOf(apiStatus);
                if (isSuccess(status)) {
                    String lienId = xmlParser.getValueOf("LienId");
                    logger.info("lienId: " + lienId);
                    if (!isEmpty(lienId))
                        setFields(ifr, tbCustAcctLienStatus, yes);
                } else if (isFailed(status)) {
                    String errDetails = xmlParser.getValueOf("ErrorDetail");
                    logger.info("errDetails: " + errDetails);
                    xmlParser.setInputXML(errDetails);
                    String errCode = xmlParser.getValueOf("ErrorCode");
                    String errDesc = xmlParser.getValueOf("ErrorDesc");
                    String errType = xmlParser.getValueOf("ErrorType");
                    logger.info("errCode: " + errCode);
                    logger.info("errDesc: " + errDesc);
                    logger.info("errType: " + errType);
                    if (isNotLien(errCode, errDesc, errType))
                        setFields(ifr, tbCustAcctLienStatus, no);
                    else
                        return "Error Code: " + errCode + " Error Description: " + errDesc + " Error Type: " + errType + ".";
                }
            } else return apiNoResponse;
        } catch (Exception e ){
            logger.info("exception occurred-- "+ e.getMessage());
        }

        return  empty;
    }

    private boolean isSuccess(String data){
        return data.equalsIgnoreCase(apiSuccess);
    }
    private boolean isFailed(String data){
        return data.equalsIgnoreCase(apiFailed) || data.equalsIgnoreCase(apiFailure);
    }
    private boolean isSchemeCodeInvalid(String data){
        return   data.equalsIgnoreCase(invalidSchemeCode1) || data.equalsIgnoreCase(invalidSchemeCode2) || data.equalsIgnoreCase(invalidSchemeCode3) || data.equalsIgnoreCase(invalidSchemeCode4) ;
    }
    private boolean isNotLien(String errCode, String errMsg, String errType){
        String errMsgActual = "The record is not found.";
        String errTypeActual = "BE";
        String errCodeActual = "005";
        return errCode.equalsIgnoreCase(errCodeActual) && errMsg.equalsIgnoreCase(errMsgActual) && errType.equalsIgnoreCase(errTypeActual);
    }
    private boolean isValidationSuccess(String data){
        return data.equalsIgnoreCase(True);
    }
    private boolean isValidationFailed(String data){
        return data.equalsIgnoreCase(False);
    }

}
