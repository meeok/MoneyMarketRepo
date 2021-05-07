package com.newgen.api.customService;

import com.newgen.controller.CpController;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;

public class CpServiceHandler implements Constants {

    public IFormReference getIfr() {
        return ifr;
    }

    public void setIfr(IFormReference ifr) {
        this.ifr = ifr;
    }

    private IFormReference ifr;

    public CpServiceHandler(IFormReference ifr) {
        setIfr(ifr);
    }
    private final CpController cpController = new CpController(getIfr());

    public  String validateAccount (){
//        Commons.setFields(ifr, new String[]{cpCustomerNameLocal, cpCustomerEmailLocal,cpCustomerSolLocal}, new String[]{"kufre", "kelmorgan18@gmail.com","200"});
        String fetchAcctCall = cpController.fetchAcctDetails();
        String fetchLienCall = cpController.fetchLien();
        return fetchAcctCall + "#" + fetchLienCall;

 //       return null;
    }

    public String validateToken(){
        return cpController.tokenValidation(Commons.getCpOtp(ifr));
    }

    public String postTransaction(){
        return null;
    }
}
