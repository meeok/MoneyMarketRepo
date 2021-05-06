package com.newgen.controller;

import com.newgen.api.customService.FetchAccountDetails;
import com.newgen.api.customService.FetchLien;
import com.newgen.api.customService.PostTransaction;
import com.newgen.api.customService.TokenValidation;
import com.newgen.api.execute.Api;
import com.newgen.api.generateXml.RequestXml;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;
import com.newgen.utils.LogGen;
import com.newgen.utils.XmlParser;
import org.apache.log4j.Logger;

import java.util.Map;

public class CpController implements Constants {
    private static final Logger logger = LogGen.getLoggerInstance(CpController.class);
    private static String outputXml;
    private static final XmlParser xmlParser = new XmlParser();
    private final IFormReference ifr;

    public CpController(IFormReference ifr) {
        this.ifr = ifr;
    }

    public static String fetchAccountDetailsController(IFormReference ifr){
        Map<String, String> getData = FetchAccountDetails.fetchAccountDetails();
        String name = getData.get("name");
        String email = getData.get("email");
        //String email = empty;
        String schemeCode = getData.get("schemeCodePass");
        if (schemeCode.equalsIgnoreCase(invalidSchemeCode1) || schemeCode.equalsIgnoreCase(invalidSchemeCode2) || schemeCode.equalsIgnoreCase(invalidSchemeCode3) || schemeCode.equalsIgnoreCase(invalidSchemeCode4)) {
            Commons.clearFields(ifr,new String[]{cpCustomerAcctNoLocal,cpCustomerNameLocal, cpCustomerEmailLocal});
            return cpInvalidAccountErrorMessage;
        }
        else if (Commons.isEmpty(email)){
            Commons.setFields(ifr,new String[]{cpCustomerNameLocal, cpCustomerEmailLocal}, new String[]{name,email});
            Commons.enableFields(ifr,new String[]{cpCustomerEmailLocal});
            Commons.setMandatory(ifr,new String[]{cpCustomerEmailLocal});
            return cpEmailMsg;
        }
        else Commons.setFields(ifr,new String[]{cpCustomerNameLocal, cpCustomerEmailLocal}, new String[]{name,email});
        return null;
    }
    public static  String fetchLienController (IFormReference ifr){
        String status = FetchLien.fetchLienCall();
        Commons.setFields(ifr, new String[]{cpLienStatusLocal},new String[]{status});
        return empty;
    }
    public static String postTranController(IFormReference ifr){
        String resp = limitController(ifr);
        logger.info("resp -- "+ resp);
        if (resp.equalsIgnoreCase(apiSuccess)) {
            String txnId = PostTransaction.postTransaction();
            txnId = txnId.trim();
            if (!Commons.isEmpty(txnId)) {
                Commons.setVisible(ifr,new String[]{cpTxnIdLocal});
                Commons.setFields(ifr, new String[]{cpTxnIdLocal, cpDecisionLocal,cpPostFlag}, new String[]{txnId, decApprove,flag});
                Commons.disableFields(ifr, new String[]{cpDecisionLocal,cpDebitPrincipalBtn,cpTxnIdLocal});
                if (Commons.getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket))
                    Commons.setVisible(ifr,new String[]{cpSetupSection,cpInvestBtn});

                return cpPostSuccessMsg;
            }
        }
        else return resp;

        return null;
    }

    public static String tokenController(IFormReference ifr){
        String resp =  TokenValidation.validateToken();

        if (resp.equalsIgnoreCase(apiSuccess)){
            Commons.disableFields(ifr,new String[]{cpTokenLocal});
            Commons.setVisible(ifr,new String[]{cpDebitPrincipalBtn});
            Commons.enableFields(ifr,new String[]{cpDebitPrincipalBtn});
        }
        else return resp;

        return null;
    }
    public  static  String  limitController (IFormReference ifr){
        try {
             return apiSuccess;
        }
        catch (Exception e){
            logger.error("Exception occurred-- "+ e.getMessage());
            return exceptionMsg;
        }
    }


    public String getUserLimit(){
        outputXml = Api.executeCall(fetchLimitServiceName, RequestXml.getUserLimitXml("SN022357"));
        return null;
    }

    public String getSearchTxn(String startDate, String endDate, String acctNo, String amount, String debitCredit, String transParts ){
        try {
            outputXml = Api.executeCall(searchTranServiceName, RequestXml.searchRequestXml(startDate, endDate, acctNo, amount, debitCredit, transParts));
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
    public String getPostTxn(String acct1, String sol1, String amount,String transParticulars, String partTranRemarks, String todayDate, String acct2, String sol2){
        try {
            outputXml = Api.executeCall(postServiceName, RequestXml.postTransactionXml(transType, transSubTypeC, acct1, sol1, debitFlag, amount, currencyNgn, transParticulars, partTranRemarks, Commons.getCurrentDate(), acct2, sol2, creditFlag,Commons.getLoginUser(ifr)));
            if (!Commons.isEmpty(outputXml)){
                xmlParser.setInputXML(outputXml);
                String status = xmlParser.getValueOf(apiStatus);

                if (isSuccess(status)){
                    String txnId = xmlParser.getValueOf("TrnId");
                    if (!Commons.isEmpty(txnId.trim()))
                        return txnId;
                }
                else if (isFailed(status)){
                    String errCode = xmlParser.getValueOf("ErrorCode");
                    String errDesc = xmlParser.getValueOf("ErrorDesc");
                    String errType = xmlParser.getValueOf("ErrorType");
                    logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
                    return "ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";
                }
            }
            else return apiNoResponse;
        } catch (Exception e){
            return e.getMessage();
        }
        return null;
    }

    public String fetchAcctDetails(){
        Commons.clearFields(ifr,new String[]{cpCustomerSolLocal,cpCustomerEmailLocal,cpCustomerNameLocal,cpCustomerAcctNoLocal});
        String accountNumber = Commons.getCpAcctNo(ifr).trim();
        if (accountNumber.startsWith("1"))
            outputXml = Api.executeCall(fetchCaaAcctServiceName,RequestXml.fetchCaaRequestXml(accountNumber));
        else  if (accountNumber.startsWith("2"))
            outputXml = Api.executeCall(fetchOdaAcctServiceName,RequestXml.fetchOdaRequestXml(accountNumber));
        else if (accountNumber.startsWith("3"))
            outputXml = Api.executeCall(fetchSbaAcctServiceName,RequestXml.fetchSbaRequestXml(accountNumber));

        logger.info("fetch account outputXml-- "+outputXml);

        if (!Commons.isEmpty(outputXml)){
            xmlParser.setInputXML(outputXml);

            String status = xmlParser.getValueOf(apiStatus);

            if (isSuccess(status)){
                String schemeCode = xmlParser.getValueOf("SchmCode");
                if (isSchemeCodeInvalid(schemeCode)) {
                    Commons.clearFields(ifr,new String[]{cpCustomerAcctNoLocal,cpCustomerNameLocal, cpCustomerEmailLocal,cpLienStatusLocal});
                    return cpInvalidAccountErrorMessage;
                }

              String email = xmlParser.getValueOf("EmailAddr");
              String sol = xmlParser.getValueOf("CustId");
              String cusDetails = xmlParser.getValueOf("PersonName");
              xmlParser.setInputXML(cusDetails);
              String name = xmlParser.getValueOf("Name");


              if (Commons.isEmpty(email)){
                  Commons.setFields(ifr,new String[]{cpCustomerNameLocal,cpCustomerSolLocal},new String[]{name,sol});
                  Commons.enableFields(ifr,new String[]{cpCustomerEmailLocal});
                  return cpCusMailErrMsg;
              }
              else {
                  Commons.setFields(ifr,new String[]{cpCustomerEmailLocal,cpCustomerNameLocal,cpCustomerSolLocal},new String[]{email,name,sol});
              }
            }
            else if (isFailed(status)){
                String errCode = xmlParser.getValueOf("ErrorCode");
                String errDesc = xmlParser.getValueOf("ErrorDesc");
                String errType = xmlParser.getValueOf("ErrorType");
                logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
                return "ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";
            }
        }
        else return apiNoResponse;
        return null;
    }
    public String fetchLien(){
        outputXml = Api.executeCall(fetchLienServiceName,RequestXml.fetchLienRequestXml(Commons.getCpAcctNo(ifr).trim()));
        if (!Commons.isEmpty(outputXml)){

            xmlParser.setInputXML(outputXml);

            String status = xmlParser.getValueOf(apiStatus);

            if (isSuccess(status)){
              String lienId = xmlParser.getValueOf("LienId");
                logger.info("lienId: "+lienId);
                if (!Commons.isEmpty(lienId))
                   Commons.setFields(ifr,cpLienStatusLocal,yes);
            }
            else if(isFailed(status)){
                String errDetails = xmlParser.getValueOf("ErrorDetail");
                logger.info("errDetails: "+errDetails);
                xmlParser.setInputXML(errDetails);
                String errCode = xmlParser.getValueOf("ErrorCode");
                String errDesc = xmlParser.getValueOf("ErrorDesc");
                String errType = xmlParser.getValueOf("ErrorType");
                logger.info("errCode: "+errCode);
                logger.info("errDesc: "+errDesc);
                logger.info("errType: "+errType);
                if (isNotLien(errCode,errDesc,errType))
                    Commons.setFields(ifr,cpLienStatusLocal,no);

                else return "Error Code: "+ errCode + " Error Description: " + errDesc + " Error Type: " + errType+ ".";

            }
        }
        else return apiNoResponse;

        return  null;
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
}
