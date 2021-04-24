package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.processMethods.CpController;
import com.newgen.utils.*;

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
                          return cpSetupWindow(ifr, Integer.parseInt(data));
                        }
                        case cpSmInvestEvent:{
                           return  setupCpSmBid(ifr);
                        }
                        case cpUpdateCutOffTimeEvent:{
                            return cpUpdateCutOffTime(ifr);
                        }
                        case cpUpdateReDiscountRateEvent:{
                            return cpUpdateReDiscountRate(ifr);
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
            else {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket) || getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                    setVisible(ifr,new String[]{cpMarketSection,cpDecisionSection,cpCategoryLocal});
                    setMandatory(ifr, new String[]{cpDecisionLocal,cpRemarksLocal});
                    disableFields(ifr,new String[]{cpMarketSection});
                    if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                        setVisible(ifr,new String[]{cpCutOffTimeSection,cpSetupSection,cpUpdateCutoffTimeBtn});
                    }
                   else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)){
                        setVisible(ifr,new String[]{cpRediscountRateSection,cpSetupSection,cpSetReDiscountRateBtn});
                    }
                   else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryUpdateLandingMsg)){
                       setVisible(ifr,new String[]{cpLandingMsgSection});
                    }
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

    private String cpSetupWindow(IFormReference ifr, int rowCount){
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
    private String cpUpdateCutOffTime(IFormReference ifr){
        String id = null;

        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket))
            id = getCpPmWinRefNo(ifr);
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket))
            id = getCpSmWinRefNo(ifr);

      int validate = new DbConnect(ifr, new Query().getUpdateCutoffTimeQuery(id,getCpCloseDate(ifr))).saveQuery();
        if (validate >=0 ) {
            setFields(ifr,cpDecisionLocal,decApprove);
            disableFields(ifr,new String[]{cpDecisionLocal,cpUpdateCutoffTimeBtn});
            return "Cut off time updated successfully. Kindly submit workitem";
        }
        return "Unable to update cut off time. Contact iBPS support";
    }
    private String cpUpdateReDiscountRate(IFormReference ifr){
        String id = null;

        String rediscount90 = getFieldValue(ifr,cpReDiscountRateLess90Local);
        String rediscount91180 = getFieldValue(ifr, cpReDiscountRate91To180Local);
        String rediscount181270 = getFieldValue(ifr,cpReDiscountRate181To270Local);
        String rediscount271364 = getFieldValue(ifr,cpReDiscountRate271To364Local);

        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket))
            id = getCpPmWinRefNo(ifr);
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket))
            id = getCpSmWinRefNo(ifr);

        int validate = new DbConnect(ifr, new Query().getUpdateReDiscountRateQuery(id,rediscount90,rediscount91180,rediscount181270,rediscount271364)).saveQuery();
        if (validate >=0 ) {
            setFields(ifr,cpDecisionLocal,decApprove);
            disableFields(ifr,new String[]{cpDecisionLocal,cpSetReDiscountRateBtn});
            return "Re-discount Rate updated successfully. Kindly submit workitem";
        }
        return "Unable to update Re-discount Rate. Contact iBPS support";
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
