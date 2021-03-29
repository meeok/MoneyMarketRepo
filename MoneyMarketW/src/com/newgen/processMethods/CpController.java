package com.newgen.processMethods;

import com.newgen.api.*;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;

import java.util.Map;

public class CpController implements Constants {

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
        String txnId = PostTransaction.postTransaction();
        txnId = txnId.trim();
        if (Commons.isEmpty(txnId)) {
            Commons.setFields(ifr,new String[]{cpTxnIdLocal,cpDecisionLocal},new String[]{txnId,decApprove});
            Commons.disableFields(ifr,new String[]{cpDecisionLocal});
            return cpPostSuccessMsg;
        }
        return null;
    }

    public static String tokenController(){
        return TokenValidation.validateToken();
    }
    public  static  String  limitController (IFormReference ifr){
        float ngnLimit = Float.parseFloat(FetchLimit.fetchLimit().get(currencyNgn));
        if (Commons.getCpPmCustomerPrincipal(ifr) > ngnLimit)
            return cpApiLimitErrorMsg;
        return apiSuccess;
    }
}
