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

public class CpController implements Constants {
    private static final Logger logger = LogGen.getLoggerInstance(CpController.class);
    private String outputXml;
    private final XmlParser xmlParser = new XmlParser();
    private final IFormReference ifr;

    public CpController(IFormReference ifr) {
        this.ifr = ifr;
    }
    public String getUserLimit(String postAmount) {
        logger.info("Welcome to get user limit call");
        try {
            if (Integer.parseInt(postAmount) > 0) {
                outputXml = Api.executeCall(fetchLimitServiceName, RequestXml.getUserLimitXml("SN022357"));
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
        try {
            if (Integer.parseInt(amount) > 0) {
                outputXml = Api.executeCall(postServiceName, RequestXml.postTransactionXml(transType, transSubTypeC, acct1, sol1, debitFlag, amount, currencyNgn, transParticulars, partTranRemarks, Commons.getCurrentDate(), acct2, sol2, creditFlag, Commons.getLoginUser(ifr)));
                logger.info("outputXml-- "+outputXml);
                if (!Commons.isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);
                    String status = xmlParser.getValueOf(apiStatus);

                    if (isSuccess(status)) {
                        String txnId = xmlParser.getValueOf("TrnId");
                        if (!Commons.isEmpty(txnId.trim()))
                            return txnId.trim();
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
            return e.getMessage();
        }
        return null;
    }

    public String fetchAcctDetails(){
        try {
            logger.info("Welcome to fetch account details call");
            String accountNumber = Commons.getCpAcctNo(ifr).trim();
            logger.info("account Number-- "+ accountNumber );
            Commons.clearFields(ifr, new String[]{cpCustomerSolLocal, cpCustomerEmailLocal, cpCustomerNameLocal});
            if (!accountNumber.isEmpty()) {
                if (accountNumber.startsWith("1"))
                    outputXml = Api.executeCall(fetchCaaAcctServiceName, RequestXml.fetchCaaRequestXml(accountNumber));
                else if (accountNumber.startsWith("2"))
                    outputXml = Api.executeCall(fetchOdaAcctServiceName, RequestXml.fetchOdaRequestXml(accountNumber));
                else if (accountNumber.startsWith("3"))
                    outputXml = Api.executeCall(fetchSbaAcctServiceName, RequestXml.fetchSbaRequestXml(accountNumber));

                logger.info("fetch account outputXml-- " + outputXml);

                if (!Commons.isEmpty(outputXml)) {
                    xmlParser.setInputXML(outputXml);

                    String status = xmlParser.getValueOf(apiStatus);

                    if (isSuccess(status)) {
                        String schemeCode = xmlParser.getValueOf("SchmCode");
                        logger.info("schemeCode- "+ schemeCode);
                        if (isSchemeCodeInvalid(schemeCode)) {
                            Commons.clearFields(ifr, new String[]{cpCustomerAcctNoLocal, cpCustomerNameLocal, cpCustomerEmailLocal, cpLienStatusLocal});
                            return cpInvalidAccountErrorMessage;
                        }

                        String email = xmlParser.getValueOf("EmailAddr");
                        logger.info("email- "+ email);
                        String sol = xmlParser.getValueOf("SOL");
                        logger.info("sol- "+ sol);
                        String cusDetails = xmlParser.getValueOf("PersonName");
                        xmlParser.setInputXML(cusDetails);
                        String name = xmlParser.getValueOf("Name");
                        logger.info("name- "+ name);


                        if (Commons.isEmpty(email)) {
                            Commons.setFields(ifr, new String[]{cpCustomerNameLocal, cpCustomerSolLocal}, new String[]{name, sol});
                            Commons.enableFields(ifr, new String[]{cpCustomerEmailLocal});
                            return cpCusMailErrMsg;
                        } else {
                            Commons.setFields(ifr, new String[]{cpCustomerEmailLocal, cpCustomerNameLocal, cpCustomerSolLocal}, new String[]{email, name, sol});
                            Commons.disableFields(ifr, new String[]{cpCustomerEmailLocal});
                        }
                    } else if (isFailed(status)) {
                        String errCode = xmlParser.getValueOf("ErrorCode");
                        String errDesc = xmlParser.getValueOf("ErrorDesc");
                        String errType = xmlParser.getValueOf("ErrorType");
                        logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
                        return "ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";
                    }
                } else return apiNoResponse;
            }
            else return "Please enter account number";
        } catch (Exception e){
            logger.info("exception occurred-- "+e.getMessage());
        }
        return empty;
    }
    public String fetchLien(){
        try {
            logger.info("Welcome to fetch lien call");
            outputXml = Api.executeCall(fetchLienServiceName, RequestXml.fetchLienRequestXml(Commons.getCpAcctNo(ifr).trim()));
            logger.info("outputXml-- " + outputXml);
            if (!Commons.isEmpty(outputXml)) {
                xmlParser.setInputXML(outputXml);
                String status = xmlParser.getValueOf(apiStatus);
                if (isSuccess(status)) {
                    String lienId = xmlParser.getValueOf("LienId");
                    logger.info("lienId: " + lienId);
                    if (!Commons.isEmpty(lienId))
                        Commons.setFields(ifr, cpLienStatusLocal, yes);
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
                        Commons.setFields(ifr, cpLienStatusLocal, no);
                    else
                        return "Error Code: " + errCode + " Error Description: " + errDesc + " Error Type: " + errType + ".";
                }
            } else return apiNoResponse;
        } catch (Exception e ){
            logger.info("exception occurred-- "+ e.getMessage());
        }

        return  empty;
    }

    public String tokenValidation(String otp){
        logger.info("Welcome to token validation call");
        if (!otp.isEmpty()){
            outputXml = Api.executeCall(tokenValidationServiceName,RequestXml.tokenValidationXml(Commons.getLoginUser(ifr),otp));
            logger.info("outputXml-- "+outputXml);

            if (!outputXml.isEmpty()) {
                xmlParser.setInputXML(outputXml);
                String status = xmlParser.getValueOf("a:Authenticated");
                logger.info("token status-- "+status);
                if (isValidationSuccess(status)) {
                    Commons.disableFields(ifr,new String[]{cpTokenLocal});
                    Commons.setVisible(ifr,new String[]{cpPostBtn});
                    Commons.enableFields(ifr,new String[]{cpPostBtn});
                }
                else if (isValidationFailed(status)){
                    logger.info("token errMgs-- "+ xmlParser.getValueOf("a:Message"));
                    return xmlParser.getValueOf("a:Message");
                }
            }
                else return apiNoResponse;
        }
        return null;
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
