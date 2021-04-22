package com.newgen.processMethods;

import com.newgen.api.*;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;
import com.newgen.utils.LogGen;
import org.apache.log4j.Logger;

import java.util.Map;

public class CpController implements Constants {
    private static Logger logger = LogGen.getLoggerInstance(CpController.class);
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
}
