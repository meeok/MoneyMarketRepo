package com.newgen.controller;

import com.newgen.api.execute.Api;
import com.newgen.api.generateXml.RequestXml;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;
import com.newgen.utils.DbConnect;
import com.newgen.utils.LogGen;
import com.newgen.utils.Query;
import com.newgen.utils.XmlParser;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
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
    	String rqstxml ="";
          if (accountNumber.startsWith("1")) {
        	  rqstxml=RequestXml.fetchCaaRequestXml(accountNumber);
              outputXml = Api.executeCall(fetchCaaAcctServiceName,rqstxml );
          }
          else if (accountNumber.startsWith("2")) {
        	  rqstxml= RequestXml.fetchOdaRequestXml(accountNumber);
              outputXml = Api.executeCall(fetchOdaAcctServiceName,rqstxml);
          }
          else if (accountNumber.startsWith("3")) {
        	  rqstxml =RequestXml.fetchSbaRequestXml(accountNumber);
              outputXml = Api.executeCall(fetchSbaAcctServiceName,rqstxml );
          }
          logger.info("rqstxml>>>>"+rqstxml);
	  
    }
    //check if schemecode is valid for treasury processing
    public boolean isValidSchemeCode(String schemeCode) {
    	return  (schemeCode.equalsIgnoreCase(SA321) || schemeCode.equalsIgnoreCase(SA310) ||
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
		String accountNumber = getTbCustAcctNo(ifr).trim();
		logger.info("account Number-- "+ accountNumber );
        try {
            logger.info("Fetch account details call");
            clearFields(ifr, new String[]{tbCustAcctEmail, tbCustAcctName, tbCustAcctLienStatus,tbCustSchemeCode,tbCustSolid,tbCustAcctCurrency});
            if (!accountNumber.isEmpty()) {
            	setFecthActDtlsInputXml(accountNumber);
            	
                logger.info("fetch account outputXml-- " + outputXml);

                if (!isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);

                    String status = xmlParser.getValueOf(apiStatus);

                    if (isSuccess(status)) {
                        String schemeCode = xmlParser.getValueOf("SchmCode");
                        logger.info("schemeCode- "+ schemeCode);
                        logger.info(isValidSchemeCode(schemeCode));
                        
                        if(!isValidSchemeCode(schemeCode)) {
                        	return "This account is not valid for TB processing";
                        }
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
                        if (isEmpty(email)) {
                            setFields(ifr, new String[]{tbCustAcctName,tbCustSchemeCode, tbCustSolid,tbCustAcctCurrency}, new String[]{name,schemeCode, sol,currency});
                            enableFields(ifr, new String[]{tbCustAcctEmail});
                            retMsg = "Update email of customer on account maintenance workflow";
                        } else {
                            setFields(ifr, new String[]{tbCustAcctEmail, tbCustAcctName,tbCustSchemeCode, tbCustSolid,tbCustAcctCurrency}, new String[]{email, name,schemeCode, sol,currency});
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
		/*
		 * check if lien for that amount exist
		 */
		public String fetchLien(){
	        try {
	            logger.info("Fetch lien call");
	            String fetchLienRqtXml = RequestXml.fetchLienRequestXml(getTbCustAcctNo(ifr).trim());
	            logger.info("fetchLienRqtXml -- " + fetchLienRqtXml);
	            outputXml = Api.executeCall(fetchLienServiceName,fetchLienRqtXml );
	            logger.info("fetchLien outputXml-- " + outputXml);
	            if (!isEmpty(outputXml)) {
	                xmlParser.setInputXML(outputXml);
	                String status = xmlParser.getValueOf(apiStatus);
	                if (isSuccess(status)) {
	                    String lienId = xmlParser.getValueOf("LienId");
	                   // St
	                    logger.info("lienId: " + lienId);
	                    if (!isEmpty(lienId)) {
	                        setFields(ifr, tbCustAcctLienStatus, yes);
	                    }
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
	
	        return  "";
		
		}
		
		public String placeLien() {
			logger.info("Place lien call");
			String acctNumber=getTbCustAcctNo(ifr).trim();
			String amount = getTbBrnchPriPrncplAmt(ifr).trim();
			String currency =Commons.getTbCustAcctCurrency(ifr).trim();
			String startDate= Commons.getCurrentDate();
			String endDate="2099-12-31";
			String lienReason = tbLienRemarks; //+" "+Commons.getWorkItemNumber(ifr);
			//
			String lienId ="TBR"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmmss"));
			logger.info("lienId>>>"+lienId);
	        try {
	        	String requestXml = RequestXml.placeLienRequestXml(acctNumber,amount,currency,lienReason,lienId);
	        	logger.info("requestXml: "+requestXml);
	        	outputXml = Api.executeCall(placeLienServiceName,requestXml );
	            logger.info("outputXml-- " + outputXml);
	             if (!isEmpty(outputXml)) {
	                xmlParser.setInputXML(outputXml);
	                //get the status tag of the HostTransactiontag
	                String hosttxntag = xmlParser.getValueOf("HostTransaction");
	                logger.info("hosttxntag: "+hosttxntag);
	                xmlParser.setInputXML(hosttxntag);
	                String status = xmlParser.getValueOf(apiStatus);
	                logger.info("Status >>>>: " + status);
	                //set back to original output xml
	                xmlParser.setInputXML(outputXml);
	                if (isSuccess(status)) {
	                	if(xmlParser.getValueOf("responseCode").equalsIgnoreCase("000")) {
	                		String uniqueRef = xmlParser.getValueOf("uniqueref");
		                    logger.info("lienId: " + lienId);
		                    if (!isEmpty(lienId)) {
		                    	setTbDecisiondd(ifr,decApprove);
		                    	disableFields(ifr, new String[] {tbLienPrincipalbtn,tbDecisiondd});
		                    	undoMandatory(ifr,tbRemarkstbx);
		                    	setTb_BrnchPri_LienID(ifr,lienId);
		                    	setFields(ifr,tbCustAcctLienStatus,"Yes");
		                    	String saveMsg = saveLienDetails(Commons.getWorkItemNumber(ifr), getFieldValue(ifr,tbCustAcctLienStatus), getFieldValue(ifr,tb_BrnchPri_LienID) );
		                    	return "Customer Principal liened Succesfully " +" ; "+saveMsg;
		                    }
	                	}
	                 	else return xmlParser.getValueOf("error");
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
	                    return "Error Code: " + errCode + " Error Description: " + errDesc + " Error Type: " + errType + ".";
	                }
	            } else return apiNoResponse;
	        } catch (Exception e ){
	            logger.info("exception occurred-- "+ e.getMessage());
	        }

	        return  "";
		}
	
	public String placeLienOld() {
		logger.info("Place lien call");
		String acctNumber=getTbCustAcctNo(ifr).trim();
		String amount = getTbBrnchPriPrncplAmt(ifr).trim();
		String currency =Commons.getTbCustAcctCurrency(ifr).trim();
		String startDate= Commons.getCurrentDate();
		String endDate="2099-12-31";
		String remarks = tbLienRemarks +" "+Commons.getWorkItemNumber(ifr);
        try {
        	String requestXml = RequestXml.placeLienRequestXmlOld(acctNumber,amount,currency,startDate,endDate,remarks);
        	logger.info("requestXml: "+requestXml);
        	outputXml = Api.executeCall(placeLienServiceName,requestXml );
            logger.info("outputXml-- " + outputXml);
             if (!isEmpty(outputXml)) {
                xmlParser.setInputXML(outputXml);
                //get the status tag of the HostTransactiontag
                String hosttxntag = xmlParser.getValueOf("HostTransaction");
                logger.info("hosttxntag: "+hosttxntag);
                xmlParser.setInputXML(hosttxntag);
                String status = xmlParser.getValueOf(apiStatus);
                logger.info("Status >>>>: " + status);
                //set back to original output xml
                xmlParser.setInputXML(outputXml);
                if (isSuccess(status)) {
                	if(xmlParser.getValueOf("responseCode").equalsIgnoreCase("000")) {
                		String lienId = xmlParser.getValueOf("LienId");
                        logger.info("lienId: " + lienId);
                        if (!isEmpty(lienId)) {
                        	setTbDecisiondd(ifr,decApprove);
                        	disableFields(ifr, new String[] {tbLienPrincipalbtn,tbDecisiondd});
                        	undoMandatory(ifr,tbRemarkstbx);
                        	setTb_BrnchPri_LienID(ifr,lienId);
                        	setFields(ifr,tbCustAcctLienStatus,"Yes");
                        	String saveMsg = saveLienDetails(Commons.getWorkItemNumber(ifr), getFieldValue(ifr,tbCustAcctLienStatus), getFieldValue(ifr,tb_BrnchPri_LienID) );
                        	return "Customer Principal liened Succesfully " +" ; "+saveMsg;
                        }
                	}
                 	else return xmlParser.getValueOf("error");
                    
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
                    return "Error Code: " + errCode + " Error Description: " + errDesc + " Error Type: " + errType + ".";
                }
            } else return apiNoResponse;
        } catch (Exception e ){
            logger.info("exception occurred-- "+ e.getMessage());
        }

        return  "";
	}
	

	//save tranid
	public String saveTranid(String winame, String tranid)
	{
		String saveTranidQry =  new Query().tbSaveTranidQuery(winame,tranid);
		logger.info("saveTranidQry>> "+saveTranidQry);
		int tranidResult = new DbConnect(ifr,saveTranidQry).saveQuery();
		logger.info("tranidResult>> "+tranidResult);
		return tranidResult>0? "":"Tranid  not saved. Kindly Click on save to save LienId";
	}
	public String saveLienStatus(String winame, String lienStatus)
	{
		String saveLienQry =  new Query().tbSaveLienStatusQuery(winame,lienStatus);
		logger.info("saveLienQry>> "+saveLienQry);
		int lienResult = new DbConnect(ifr,saveLienQry).saveQuery();
		logger.info("lienResult>> "+lienResult);
		return lienResult>0? "":"Lien Status not saved. Kindly Click on save to save lien status";
	}
	
	//save lienid and status
	public String saveLienDetails(String winame,String lienStatus, String lienid)
	{
		String saveLienDetailsQry =  new Query().tbSaveLienDetailsQuery( winame, lienStatus ,  lienid);
		logger.info("saveLienQry>> "+saveLienDetailsQry);
		int lienDetailsResult = new DbConnect(ifr,saveLienDetailsQry).saveQuery();
		logger.info("lienResult>> "+lienDetailsResult);
		return lienDetailsResult>0? "":"Lien Details not saved. Kindly Click on save to save lien status";
	}
	
	//removing lien
	public String removeLien() {
		logger.info("Remove lien call");
		String lienId = getTb_BrnchPri_LienID(ifr);
		String acctNumber=getTbCustAcctNo(ifr).trim();
		String amount = getTbBrnchPriPrncplAmt(ifr).trim();
		String currency =Commons.getTbCustAcctCurrency(ifr).trim();
		String startDate= Commons.getCurrentDate();
		String endDate=Commons.getCurrentDate();
		String lienReason = tbLienRemarks;
        try {
        	String requestXml = RequestXml.removeLienRequestXml(acctNumber, amount, currency, lienReason,lienId);
        	logger.info("requestXml: "+requestXml);
        	outputXml = Api.executeCall(removeLienServiceName,requestXml );
            logger.info("outputXml-- " + outputXml);
             if (!isEmpty(outputXml)) {
                xmlParser.setInputXML(outputXml);
                //get the status tag of the HostTransactiontag
                String hosttxntag = xmlParser.getValueOf("HostTransaction");
                logger.info("hosttxntag: "+hosttxntag);
                xmlParser.setInputXML(hosttxntag);
                String status = xmlParser.getValueOf(apiStatus);
                logger.info("Status >>>>: " + status);
                //set back to original output xml
                xmlParser.setInputXML(outputXml);
                if (isSuccess(status)) {
                	if(xmlParser.getValueOf("responseCode").equalsIgnoreCase("000")) {
                		String resplienId = xmlParser.getValueOf("uniqueref");
                        logger.info("resplienId: " + resplienId);
                        if (!isEmpty(resplienId) && lienId.equalsIgnoreCase(resplienId)) {
                        	undoMandatory(ifr,tbRemarkstbx);
                        	setFields(ifr,tbCustAcctLienStatus,"No");
                        	hideField(ifr,tbUnlienBtn);
                        	setVisible(ifr,new String[] {tbPostbtn,tbtoken,tbTranID});
                        	disableFields(ifr,new String[] {tbPostbtn});
                        	String saveMsg = saveLienStatus(Commons.getWorkItemNumber(ifr), getFieldValue(ifr,tbCustAcctLienStatus));
                        	return "Customer Principal unliened Succesfully ; "+saveMsg;
                        }
                	}
                	else return xmlParser.getValueOf("error");
                	
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
                    return "Error Code: " + errCode + " Error Description: " + errDesc + " Error Type: " + errType + ".";
                }
            }
             else {
            	 return "Lien not removed contact Admin";
             } //return apiNoResponse;
        } catch (Exception e ){
            logger.info("exception occurred-- "+ e.getMessage());
            return "An Exception occured, contact administrator";
        }
        return  ""; 
	}
	
	public String getUserLimit(String postAmount) {
        logger.info("Welcome to get user limit call");
        try {
            if (Integer.parseInt(postAmount) > 0) {
                outputXml = Api.executeCall(fetchLimitServiceName, RequestXml.getUserLimitXml("SN022357"));//change on golive
                logger.info("outputXml limit call -- " + outputXml);
                String currency;
                String amount = empty;

                if (!Commons.isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);

                    String status = xmlParser.getValueOf(apiStatus);

                    if (isSuccess(status)) {
                        Set<Map<String, String>> resultSet = xmlParser.getXMLData(outputXml, "DATA");

                        for (Map<String, String> result : resultSet) {

                            currency = result.get("CRNCYCODE");
                            logger.info("limit currency-- " + currency);

                            if (currency.equalsIgnoreCase(currencyNgn)) {
                                amount = result.get("USERCASHDRLIM");
                                logger.info("limit amount" + amount);
                                break;
                            }
                        }

                        if (Integer.parseInt(postAmount) <= Integer.parseInt(amount))
                            return apiSuccess;
                       else return apiLimitErrMsg;
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
            logger.info("Exception occurred-- "+e.getMessage());
            return e.getMessage();
        }
        return null;
    }

    public String getSearchTxn(String startDate, String endDate, String acctNo, String amount, String debitCredit, String transParts ){
        logger.info("Welcome to search transaction call");
        try {
            outputXml = Api.executeCall(searchTranServiceName, RequestXml.searchRequestXml(startDate, endDate, acctNo, amount, debitCredit, transParts));
            logger.info("outputXml-- "+outputXml);
            final String noDuplicateMsg = "NO record exist for entered details";
            if (!Commons.isEmpty(outputXml)) {
                xmlParser.setInputXML(outputXml);
                String respFlag = xmlParser.getValueOf(apiStatus);
                if (isSuccess(respFlag)) {
                    String message = xmlParser.getValueOf("Success_1");
                    logger.info("message: " + message);
                    String txnIdApi = xmlParser.getValueOf("tranId");
                    logger.info("txnIdApi: " + txnIdApi);

                    if (!Commons.isEmpty(txnIdApi))
                        return "Duplicate record exist for this Transaction. Kindly Check Finacle";
                    else if (message.trim().equalsIgnoreCase(noDuplicateMsg))
                        return False;

                } else if (isFailed(respFlag)) {
                    String errCode = xmlParser.getValueOf("ErrorCode");
                    String errDesc = xmlParser.getValueOf("ErrorDesc");
                    String errType = xmlParser.getValueOf("ErrorType");
                    logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
                    return "ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";

                }
            } else {
                return apiNoResponse;
            }
        }catch (Exception e){
            return e.getMessage();
        }
        return null;
    }
    public String getPostTxn(String acct1, String sol1, String amount, String transParticulars, String partTranRemarks, String acct2, String sol2){
        logger.info("Welcome to post transaction call");
        String loginUser ="SN018198"; //Commons.getLoginUser(ifr) change after testing
        try {
            if (Integer.parseInt(amount) > 0) {
                outputXml = Api.executeCall(postServiceName, RequestXml.postTransactionXml(transType, transSubTypeC, acct1, sol1, debitFlag, amount, currencyNgn, transParticulars, partTranRemarks, Commons.getCurrentDate(), acct2, sol2, creditFlag, loginUser));
                logger.info("outputXml-- "+outputXml);
                if (!Commons.isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);
                    String status = xmlParser.getValueOf(apiStatus);

                    if (isSuccess(status)) {
                        String txnId = xmlParser.getValueOf("TrnId");
                        if (!Commons.isEmpty(txnId.trim())) {
                        	//save tranid
                        	String saveMsg = saveTranid(Commons.getWorkItemNumber(ifr), txnId.trim());
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
        return null;
    }
    
    public String tokenValidation(String otp){
        logger.info("Welcome to token validation call");
        if (!otp.isEmpty()){
        	Commons.disableFields(ifr,new String[]{tbtoken});
            Commons.enableFields(ifr,new String[]{tbPostbtn});
        	/*
            outputXml = Api.executeCall(tokenValidationServiceName,RequestXml.tokenValidationXml(Commons.getLoginUser(ifr),otp));
            logger.info("outputXml-- "+outputXml);

            if (!outputXml.isEmpty()) {
                xmlParser.setInputXML(outputXml);
                String status = xmlParser.getValueOf("a:Authenticated");
                logger.info("token status-- "+status);
                if (isValidationSuccess(status)) {
                    Commons.disableFields(ifr,new String[]{tbtoken});
                    Commons.enableFields(ifr,new String[]{tbPostbtn});
                }
                else if (isValidationFailed(status)){
                    logger.info("token errMgs-- "+ xmlParser.getValueOf("a:Message"));
                    return xmlParser.getValueOf("a:Message");
                }
            }
                else return apiNoResponse;
        */}
        return "token validated successfully";
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
