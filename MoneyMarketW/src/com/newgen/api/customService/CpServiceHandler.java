package com.newgen.api.customService;

import com.newgen.controller.CpController;
import com.newgen.iforms.custom.IFormReference;
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
    private CpController cpController = new CpController(getIfr());

    public  String validateAccount (){
        String fetchAcctCall = cpController.fetchAcctDetails();
        String fetchLienCall = cpController.fetchLien();
        return fetchAcctCall + "#" + fetchLienCall;
    }
}
