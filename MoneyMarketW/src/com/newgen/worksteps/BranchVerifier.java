package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.controller.CpController;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.LogGen;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BranchVerifier extends Commons implements IFormServerEventHandler , CommonsI {
    private static Logger logger = LogGen.getLoggerInstance(BranchVerifier.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        clearDecHisFlag(ifr);
        if (!isEmpty(getProcess(ifr))) showSelectedProcessSheet(ifr);
        if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbFormLoadActivity(ifr);
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
                        case cpSmInvestEvent:{
                            if (cpCheckWindowStateById(ifr, getCpSmWinRefNoBr(ifr))) return setupCpSmBid(ifr);
                            else return cpValidateWindowErrorMsg;
                        }
                    }
                }
                case onChange:
                case custom:
                case onDone:{
                    switch (control){
                        case validateWindowEvent:{
                            if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                                    if (cpCheckWindowStateById(ifr, getCpPmWinRefNoBr(ifr)))
                                        setupCpPmBid(ifr);
                                    else return cpValidateWindowErrorMsg;
                                }
                                else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                                    if (!cpCheckWindowStateById(ifr, getCpSmWinRefNoBr(ifr)))
                                         return cpValidateWindowErrorMsg;
                                }
                            }
                        }
                        break;
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
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
                setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
                setDropDown(ifr,cpPmReqTypeLocal,new String[]{cpPmReqFreshLabel},new String[]{cpPmReqFreshValue});
                setFields(ifr,cpPmReqTypeLocal,cpPmReqFreshValue);
                setVisible(ifr,new String[]{cpBranchPriSection,cpCustomerDetailsSection,cpPostSection,cpDecisionSection,landMsgLabelLocal});
                setInvisible(ifr, new String[]{cpAcctValidateBtn});
                enableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
                setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)){
                setVisible(ifr,new String[]{cpDecisionSection});
                setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                enableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                cpSetDecision(ifr);
                if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                    setVisible(ifr,new String[]{cpTerminationSection,cpTermMandateTbl,cpTermSpecialRateLocal, getCpTermIsSpecialRate(ifr) ? cpTermSpecialRateValueLocal : cpTermSpecialRateLocal});
                    if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypeFull)){
                        setVisible(ifr,new String[]{cpTermAmountDueLocal});
                    }
                    else if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)){
                        setVisible(ifr,new String[]{cpTermAmountDueLocal,cpTermAdjustedPrincipalLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermPartialOptionLocal});
                    }
                }
                else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeSetupLien) || getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeRemoveLien)){}
            }
        }
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)){
                setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
                setVisible(ifr,new String[]{cpBranchSecSection,cpCustomerDetailsSection,cpDecisionSection,landMsgLabelLocal,
                        cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal, (getCpSmConcessionRateValue(ifr).equalsIgnoreCase(empty)) ? empty : cpSmConcessionRateValueLocal});
                setInvisible(ifr, new String[]{cpAcctValidateBtn,cpApplyBtn});
                disableFields(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal,cpSmInstructionTypeLocal});
                setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                if (getCpSmConcessionRate(ifr).equalsIgnoreCase(no)){
                    enableFields(ifr,new String[]{cpTokenLocal});
                    setMandatory(ifr,new String[]{cpTokenLocal});
                    setVisible(ifr,new String[]{cpPostSection});
                }
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)){
                setVisible(ifr,new String[]{cpDecisionSection});
                setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                enableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                cpSetDecision(ifr);
                if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                    setVisible(ifr,new String[]{cpTerminationSection,cpTermMandateTbl,cpTermSpecialRateLocal, getCpTermIsSpecialRate(ifr) ? cpTermSpecialRateValueLocal : cpTermSpecialRateLocal});
                    if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypeFull)){
                        setVisible(ifr,new String[]{cpTermAmountDueLocal});
                    }
                    else if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)){
                        setVisible(ifr,new String[]{cpTermAmountDueLocal,cpTermAdjustedPrincipalLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermPartialOptionLocal});
                    }
                }
                else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeSetupLien) || getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeRemoveLien)){}
            }
        }
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReject});
    }
    
    /*********************************  Treasury Starts here ****************/

    private void tbFormLoadActivity(IFormReference ifr){
        hideTbSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        disableTbSections(ifr);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{tbRemarkstbx,tbDecisiondd}); 
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
            if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)) {
            	setVisible(ifr, new String[] {tbBrnchPriCusotmerDetails,tbBranchPriSection,tbDecisionSection});
            	setDecision(ifr,tbDecisiondd,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
                setInvisible(ifr, new String[]{cpAcctValidateBtn});
            }
        } else {}
       
        
    }
    
    /***************************Treaury ends here ************************/
}
