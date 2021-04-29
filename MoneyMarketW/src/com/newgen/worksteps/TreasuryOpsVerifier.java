package com.newgen.worksteps;

import com.newgen.controller.CpController;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.Constants;
import com.newgen.utils.LogGen;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TreasuryOpsVerifier extends Commons implements IFormServerEventHandler, Constants, CommonsI {
    private static Logger logger = LogGen.getLoggerInstance(TreasuryOpsVerifier.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        clearDecHisFlag(ifr);
        if (!isEmpty(getProcess(ifr))) showSelectedProcessSheet(ifr);
        if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
        //else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbFormLoadActivity(ifr);

    }

    @Override
    public String setMaskedValue(String s, String s1) {
        return s1;
    }

    @Override
    public JSONArray executeEvent(FormDef formDef, IFormReference iFormReference, String s, String s1) {
        return null;
    }

    @Override
    public String executeServerEvent(IFormReference ifr, String control, String event, String data) {

        try{
            switch (event){
                case cpApiCallEvent:{
                    switch (control) {
                        case cpTokenEvent: return CpController.tokenController(ifr);
                        case cpPostEvent:{
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                                if (cpCheckWindowStateById(ifr, getCpPmWinRefNoBr(ifr))) return CpController.postTranController(ifr);
                                else return cpValidateWindowErrorMsg;
                            }
                            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                                if (cpCheckWindowStateById(ifr, getCpSmWinRefNoBr(ifr))) return CpController.postTranController(ifr);
                                else return cpValidateWindowErrorMsg;
                            }
                        }
                    }
                }
                case formLoad:
                case onLoad:
                case onClick:{
                    switch (control){

                    }
                }
                case onChange:
                case custom:
                case onDone:{
                    switch (control){

                    }
                }
                break;
                case decisionHistory: {
                    if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                        setCpDecisionHistory(ifr);
                    else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess))
                        setTbDecisionHistory(ifr);
                }
                break;
                case sendMail:
            }
        }
        catch (Exception e){
            logger.error("Exception occurred-- "+ e.getMessage());
        }
        return null;
    }

    @Override
    public JSONArray validateSubmittedForm(FormDef formDef, IFormReference iFormReference, String s) {
        return null;
    }

    @Override
    public String executeCustomService(FormDef formDef, IFormReference iFormReference, String s, String s1, String s2) {
        return null;
    }

    @Override
    public String getCustomFilterXML(FormDef formDef, IFormReference iFormReference, String s) {
        return null;
    }

    @Override
    public String generateHTML(EControl eControl) {
        return null;
    }

    @Override
    public String introduceWorkItemInWorkFlow(IFormReference iFormReference, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return null;
    }

    @Override
    public void cpSendMail(IFormReference ifr) {

    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});

        if (getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
          if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
              setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              enableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              setVisible(ifr,new String[]{cpDecisionSection,cpTerminationSection,cpTermMandateTbl,cpTermSpecialRateLocal, getCpTermIsSpecialRate(ifr) ? cpTermSpecialRateValueLocal : cpTermSpecialRateLocal});
              if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypeFull)){
                  setVisible(ifr,new String[]{cpTermAmountDueLocal});
              }
              else if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)){
                  setVisible(ifr,new String[]{cpTermAmountDueLocal,cpTermAdjustedPrincipalLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermPartialOptionLocal});
              }
          }

        }

    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReject});
    }
}
