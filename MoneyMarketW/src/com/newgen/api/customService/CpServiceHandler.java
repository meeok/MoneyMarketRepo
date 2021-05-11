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
    public String validateAccountTest(){
        String email = "kelmorgan18@gmail.com";
        String sol = "191";
        String name = "Kufre Godwin Udoko";
        Commons.setFields(ifr, new String[]{cpCustomerEmailLocal, cpCustomerNameLocal, cpCustomerSolLocal}, new String[]{email, name, sol});
        return null;
    }

    public String validateLien (){
        CpController cpController = new CpController(ifr);
        return cpController.fetchLien();
    }
    public String validateLienTest (){
        Commons.setFields(ifr, cpLienStatusLocal, no);
        return null;
    }

    public String validateToken(){
        CpController cpController = new CpController(ifr);
        return cpController.tokenValidation(Commons.getCpOtp(ifr));
    }

    public String validateTokenTest(){
        Commons.disableFields(ifr,new String[]{cpTokenLocal});
        Commons.setVisible(ifr,new String[]{cpPostBtn});
        Commons.enableFields(ifr,new String[]{cpPostBtn});
        return null;
    }

    public String postTransactionTest(){
        Commons.setVisible(ifr,new String[]{cpTxnIdLocal});
        Commons.setFields(ifr, new String[]{cpTxnIdLocal, cpDecisionLocal,cpPostFlag}, new String[]{"M20", decApprove,flag});
        Commons.disableFields(ifr, new String[]{cpDecisionLocal, cpPostBtn,cpTxnIdLocal});
        if (Commons.getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket))
            Commons.setVisible(ifr,new String[]{cpSetupSection,cpInvestBtn});
        return null;
    }
}
