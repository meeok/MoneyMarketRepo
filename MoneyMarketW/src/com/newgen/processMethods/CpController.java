package com.newgen.processMethods;

import com.newgen.api.FetchAccountDetails;
import com.newgen.api.FetchLien;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;

import java.util.Map;

public class CpController implements Constants {

    public static String fetchAccountDetailsController(IFormReference ifr){
        Map<String, String> getData = FetchAccountDetails.fetchAccountDetails();
        String name = getData.get("name");
        String email = getData.get("email");
        String schemeCode = getData.get("schemeCodePass");

        if (schemeCode.equalsIgnoreCase(invalidSchemeCode1) || schemeCode.equalsIgnoreCase(invalidSchemeCode2) || schemeCode.equalsIgnoreCase(invalidSchemeCode3) || schemeCode.equalsIgnoreCase(invalidSchemeCode4))
            return cpInvalidAccountErrorMessage;
        else Commons.setFields(ifr,new String[]{cpPmCustomerNameLocal,cpPmCustomerEmailLocal}, new String[]{name,email});
        return null;
    }
    public static  String fetchLienController (IFormReference ifr){
        String status = FetchLien.fetchLienCall();
        Commons.setFields(ifr, new String[]{cpPmLienStatusLocal},new String[]{status});
        return null;
    }
}
