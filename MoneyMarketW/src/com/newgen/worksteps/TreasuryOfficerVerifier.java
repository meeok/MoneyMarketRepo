package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.processMethods.CpController;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.DBCalls;
import com.newgen.utils.LogGen;
import com.newgen.utils.MailSetup;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TreasuryOfficerVerifier extends Commons implements IFormServerEventHandler, CommonsI {
    private static Logger logger = LogGen.getLoggerInstance(TreasuryOfficerVerifier.class);
    DBCalls dbc = new DBCalls();
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
    public String executeServerEvent(IFormReference ifr, String controlName, String eventName, String data) {
        try {
        	logger.info("ControlName>>" +controlName);
        	logger.info("eventName>>" +eventName);
            switch (eventName){
                case formLoad:{}
                break;
                case onLoad:{}
                break;
                case onClick:{
                    switch (controlName){
                        case cpSetupWindowEvent:{
                          return setupCpWindow(ifr, Integer.parseInt(data));
                        }
                        case cpSmInvestEvent:{
                           return  setupCpSmBid(ifr);
                        }
                    }
                }
                break;
                case cpApiCallEvent:{
                    switch (controlName) {
                        case cpTokenEvent: return CpController.tokenController(ifr);
                        case cpPostEvent: return CpController.postTranController(ifr);
                    }
                    break;
                }
                case onChange:{}
                break;
                case custom:{}
                break;
                case onDone:{
                	switch(controlName){
                	
                	/*** Treasury start****/
                	case tbUpDateLndingMsgFlg:{
                		tbUpDateLndingMsgFlg(ifr);
                		}
                	break;
                	
                	/*** Treasury end****/
                	
                	}
                	
                	
                }
                break;
                case decisionHistory:{
                	if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) setCpDecisionHistory(ifr);
                	else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) setTbDecisionHistory(ifr); 
                }
                break;
                case sendMail:{ if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpSendMail(ifr);
                			    else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbSendMail(ifr);}
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.info("Exception Occurred-- "+ e.getMessage());
        }
        return null;
    }

    

	@Override
    public void cpSendMail(IFormReference ifr) {
        String message;
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator)){
            if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                message = "Landing Message has been approved by the treasury officer verifier with ref No. "+getWorkItemNumber(ifr)+". Login to setup market.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            }
            else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                message = "Landing Message has been rejected by the treasury officer verifier with ref No. "+getWorkItemNumber(ifr)+". Login to make necessary corrections.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            }
    }
    }
    @Override
    public void cpFormLoadActivity(IFormReference ifr){
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        setGenDetails(ifr);
//        disableCpSections(ifr);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator)) {
            setVisible(ifr,new String[] {cpLandingMsgSection,cpDecisionSection,cpMarketSection});
            enableFields(ifr,new String[]{cpDecisionSection});
            setMandatory(ifr, new String[]{cpDecisionLocal,cpRemarksLocal});
        }
        else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerMaker)){
            if (isEmpty(getWindowSetupFlag(ifr))){
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){}
                else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                    setVisible(ifr, new String[]{cpLandingMsgSection,cpDecisionSection,cpMarketSection,cpTreasurySecSection,cpCutOffTimeSection,cpSmCutOffTimeLocal,cpSetupSection,cpSetupWindowBtn,cpSmCpBidTbl});
                    setInvisible(ifr,new String[]{cpOpenDateLocal,cpCloseDateLocal});
                    setMandatory(ifr,new String[] {cpDecisionLocal,cpRemarksLocal});
                    disableFields(ifr,new String[]{cpSmCpBidTbl,cpSmMinPrincipalLocal});
                }
            }
        }
        else if (getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
            if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)){
                    setVisible(ifr,new String[]{cpBranchSecSection,cpCustomerDetailsSection,cpDecisionSection,landMsgLabelLocal,
                            cpSmMaturityDateBrLocal,cpPostSection,cpTokenLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal, (getCpSmConcessionRateValue(ifr).equalsIgnoreCase(empty)) ? empty : cpSmConcessionRateValueLocal});
                    setInvisible(ifr, new String[]{cpAcctValidateBtn,cpApplyBtn,cpSmInvestmentBrTbl});
                    disableFields(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal,cpSmInstructionTypeLocal});
                    setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
                    enableFields(ifr,new String[]{cpTokenLocal});
                }
            }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr, cpDecisionLocal,new String[]{decApprove,decReject});
    }

    private String setupCpWindow (IFormReference ifr, int rowCount){
        if (isEmpty(getWindowSetupFlag(ifr))){
            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                return empty;
            }
            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                return cpSetupSecondaryMarketWindow(ifr,rowCount);
            }
        }
        return "Window already setup.";
    }

    /******************  TREASURY BILL CODE BEGINS *********************************/
    /*
     * set controls for task to be performed before the formloads
     */
    private void tbFormLoadActivity(IFormReference ifr){
        hideTbSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        setGenDetails(ifr);
        disableTbSections(ifr);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{tbRemarkstbx});
        //set controls for task to be performed
        //approving of landing message 
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator) || getTbUpdateLandingMsg(ifr)) { // for approval of landing page
            setVisible(ifr,new String[] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
            enableField(ifr,tbDecisionSection);
            setMandatory(ifr, new String[]{tbDecisiondd,tbRemarkstbx});
        }
        else {//Modification of Primary Market Cut-off Time 
        	
        }
        setDropDown(ifr,tbDecisiondd,new String[]{decApprove,decReject});
    }
    
    /*
     * update landingMsgApprovedFlg based on decision
     * set the message to be sent out as a mail
     */
    private void tbUpDateLndingMsgFlg(IFormReference ifr) {
    	logger.info("tbUpDateLndingMsgFlg");
    	 if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator)){
             if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
            	 setTbLandingMsgApprovedFlg(ifr,yesFlag);
             }
             else if (getTbDecision(ifr).equalsIgnoreCase(decReject)) {
            	 setTbLandingMsgApprovedFlg(ifr,noFlag);
             }
         }
	}
    
    public void tbSendMail(IFormReference ifr) {
        String message;
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator)){
            if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
                message = "Landing Message has been approved by the treasury officer verifier with ref No. "+getWorkItemNumber(ifr)+". Login to setup market.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            }
            else if (getTbDecision(ifr).equalsIgnoreCase(decReject)){
                message = "Landing Message has been rejected by the treasury officer verifier with ref No. "+getWorkItemNumber(ifr)+". Login to make necessary corrections.";
                new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
            }
    }
    }
    //*************** Treasury End *************************/

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
}
