package com.newgen.worksteps;

import com.newgen.api.customService.CpServiceHandler;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BranchVerifier extends Commons implements IFormServerEventHandler , CommonsI {
    private static final Logger logger = LogGen.getLoggerInstance(BranchVerifier.class);
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
                        case cpTokenEvent: return new CpServiceHandler(ifr).validateTokenTest();
                        case cpPostEvent:{
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                                if (cpCheckWindowStateById(ifr, getCpPmWinRefNoBr(ifr))) return new CpServiceHandler(ifr).postTransactionTest();
                                else return cpValidateWindowErrorMsg;
                            }
                            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                                if (cpCheckWindowStateById(ifr, getCpSmWinRefNoBr(ifr))) return new CpServiceHandler(ifr).postTransactionTest();
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
                        case generateTemplateEvent:{
                            return GenerateDocument.generateDoc(ifr,data);
                        }
                    }
                }
                case onChange:{
                    switch (control){
                        case cpCheckDecisionEvent:{
                            cpCheckDecision(ifr);
                            break;
                        }
                    }
                }
                break;
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
                case sendMail:{}
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
        if (getPrevWs(ifr).equalsIgnoreCase(branchInitiator) || getPrevWs(ifr).equalsIgnoreCase(branchException)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)){
                if (getCpDecision(ifr).equalsIgnoreCase(decApprove)){
                    if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                        message = "We wish to inform you that your primary market  commercial paper  bid of " + getCpPmCusRefNo(ifr) + " has been debited for the principal sum. <br>" +
                                " Thank you for choosing FirstBank.<br> ";
                        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                    }
                }
                else if (getCpDecision(ifr).equalsIgnoreCase(decReturn)){
                        message = "A request for "+getCpMarket(ifr)+" Market Commercial paper with unique reference number "+getCpPmCusRefNo(ifr)+" has been rejected by Money_Market_Branch_Verifier due to "+getCpRemarks(ifr)+". ";
                        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                }
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)){
                if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                    if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                        if (getCpTermIsSpecialRate(ifr))
                            message = "A Termination request for "+getCpMarket(ifr)+" Market Commercial Paper with number "+getCpTermCusId(ifr)+" has been  approved by Money_Market_Branch_Verifier and is now pending in your queue for approval of special rate: "+getCpTermSpecialRateValue(ifr)+". Workitem No. "+getWorkItemNumber(ifr)+".";
                         else
                            message = "A Termination request for "+getCpMarket(ifr)+" Market Commercial Paper with number "+getCpTermCusId(ifr)+" has been  approved by Money_Market_Branch_Verifier and is now pending in your queue for approval. Workitem No. "+getWorkItemNumber(ifr)+".";

                        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                    }
                    else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                        message = "A Termination request for Commercial paper with number "+getCpTermCusId(ifr)+" has been rejected by Money_Market_Branch_Verifier and is now pending in your queue. Workitem No. "+getWorkItemNumber(ifr)+".";
                        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                    }
                }
                else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)){
                    if (getCpDecision(ifr).equalsIgnoreCase(decApprove)){
                        message = "A lien setup or removal request for "+getCpMarket(ifr)+" Market Commercial Paper with number "+getCpLienMandateId(ifr)+" has been  approved by Money_Market_Branch_Verifier and is now pending in your queue for approval. Workitem No. "+getWorkItemNumber(ifr)+".";
                        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                    }
                   else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                        message = "A lien setup or removal request for "+getCpMarket(ifr)+" Market Commercial Paper with number "+getCpLienMandateId(ifr)+" has been  rejected by Money_Market_Branch_Verifier and is now pending in your queue. Workitem No. "+getWorkItemNumber(ifr)+".";
                        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                    }
                }
            }
        }
    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
        setVisible(ifr,cpMarketSection);
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
                else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)){
                    setVisible(ifr,new String[]{cpLienSection});
                }
            }
        }
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)){
                setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
                setVisible(ifr,new String[]{cpBranchSecSection,cpCustomerDetailsSection,cpDecisionSection,landMsgLabelLocal,
                        cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal, (getCpSmConcessionRateValue(ifr).equalsIgnoreCase(empty)) ? empty : cpSmConcessionRateValueLocal});
                setInvisible(ifr, new String[]{cpAcctValidateBtn,cpApplyBtn});
                disableFields(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal, cpSmInvestmentTypeLocal});
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
                else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)){
                    setVisible(ifr,new String[]{cpLienSection});
                }
            }
        }
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReject});
    }
    private void setupCpPmBid(IFormReference ifr){
        String rate =  getCpPmRateType(ifr).equalsIgnoreCase(rateTypePersonal) ? getFieldValue(ifr,cpPmPersonalRateLocal) : empty;
        new DbConnect(ifr,Query.getSetupCpPmBidQuery(
                getCurrentDateTime(),getCpPmCusRefNo(ifr), getCpPmWinRefNoBr(ifr),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getFieldValue(ifr,cpCustomerAcctNoLocal),getFieldValue(ifr,cpCustomerNameLocal),getFieldValue(ifr,cpCustomerEmailLocal),String.valueOf(getCpPmCustomerPrincipal(ifr)),getFieldValue(ifr,cpPmTenorLocal),getCpPmRateType(ifr),rate,getCpPmInvestmentType(ifr)
                )).saveQuery();
    }
    private void cpCheckDecision(IFormReference ifr){
        String decision = getCpDecision(ifr);

        if (decision.equalsIgnoreCase(decReturn)){
            disableFields(ifr,new String[]{cpTokenLocal,cpPostBtn});
            undoMandatory(ifr,new String[]{cpTokenLocal,cpPostBtn});
        }
        else {
            enableFields(ifr,new String[]{cpTokenLocal,cpPostBtn});
            setMandatory(ifr,new String[]{cpTokenLocal,cpPostBtn});
        }
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
