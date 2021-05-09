package com.newgen.api.customService;

import com.newgen.controller.CpController;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;

public class CpServiceHandler implements Constants {

    public CpServiceHandler(IFormReference ifr) {
        this.ifr = ifr;
    }

    private final IFormReference ifr;
    public  String validateAccount (){
        CpController cpController = new CpController(ifr);
        return cpController.fetchAcctDetails();
    }

    public String validateLien (){
        CpController cpController = new CpController(ifr);
        return cpController.fetchLien();
    }

    public String validateToken(){
        CpController cpController = new CpController(ifr);
        return cpController.tokenValidation(Commons.getCpOtp(ifr));
    }

    public String postTransactionBrPmBids(){
        return null;
    }
}
