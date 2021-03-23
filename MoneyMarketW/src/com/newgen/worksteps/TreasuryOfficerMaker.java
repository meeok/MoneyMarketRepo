package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.reusableObject.Commons;
import com.newgen.reusableObject.CommonsI;
import com.newgen.utils.DbConnect;
import com.newgen.utils.LogGen;
import com.newgen.utils.Query;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TreasuryOfficerMaker extends Commons implements IFormServerEventHandler, CommonsI {
    private Logger logger = LogGen.getLoggerInstance(TreasuryOfficerMaker.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        clearDecHisFlag(ifr);
        if(!isEmpty(getProcess(ifr)))showSelectedProcessSheet(ifr);
        if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
            cpFormLoadActivity(ifr);
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess))
        	tbFormLoadActivity(ifr);
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
            switch (eventName){
                case formLoad:{}
                break;
                case onLoad:{}
                break;
                case onClick:{
                    switch (controlName){
                        case cpUpdateMsg:{cpUpdateLandingMsg(ifr);}
                        break;
                        case cpSetupWindow:{ return setupCpWindow(ifr);}
                        
                        /**** Treasury onClick Start ****/
                        
                        /**** Treasury onClick End ****/
                    }
                }
                break;
                case onChange:{
                    switch (controlName){
                        case cpOnSelectCategory:{cpSelectCategory(ifr);}
                        break;
                        
                        /**** Treasury Onchange Start ****/
                        case tbCategoryChange:{
                        	tbCategoryChange(ifr);
                        }
                        break;
                        /**** Treasury Onchange End  ****/
                    }
                }
                break;
                case custom:{}
                break;
                case onDone:{
                	
                /**** Treasury onDOne Start ****/
               
                /**** Treasury onDone End  ****/
                }
                break;
                case decisionHistory:{
                	if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) setTbDecisionHistory(ifr);
                	else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) setTbDecisionHistory(ifr); }
                break;
                case sendMail:{
                	if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpSendMail(ifr);
                	if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbSendMail(ifr);
                	}
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.info("Exception Occurred-- "+ e.getMessage());
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

    private String setupCpWindow (IFormReference ifr){
        if (isEmpty(getSetupFlag(ifr))){
            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                if (compareDate(getCpOpenDate(ifr),getCpCloseDate(ifr))){}
                else {
                    return "Close date cannot be before open date.";
                }
            }
            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){}
        }

        return null;
    }
    private void cpUpdateLandingMsg(IFormReference ifr){
        if (getCpUpdateMsg(ifr).equalsIgnoreCase(True)){
            cpSetDecisionValue(ifr,decSubmit);
            ifr.setValue(cpRemarksLocal,"Kindly approve landing message update.");
            setInvisible(ifr, new String[]{cpSetupSection,cpDecisionSection});
            undoMandatory(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
            setMandatory(ifr,new String[]{cpLandMsgLocal});
            enableFields(ifr,new String[]{cpLandMsgLocal,cpLandingMsgSubmitBtn});
            setVisible(ifr,new String[]{cpLandingMsgSubmitBtn});
        }
        else {
            clearFields(ifr, new String[]{cpDecisionLocal,cpRemarksLocal,cpLandMsgLocal});
            setVisible(ifr, new String[]{cpSetupSection,cpDecisionSection});
            setMandatory(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
            undoMandatory(ifr,new String[]{cpLandMsgLocal});
            disableFields(ifr,new String[]{cpLandMsgLocal});
        }
    }
    private void cpSelectCategory(IFormReference ifr){
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategorySetup)){
                setVisible(ifr, new String [] {cpTreasuryPriSection,cpSetupSection,cpSetupWindowBtn,cpCutOffTimeSection});
                setMandatory(ifr,new String[] {cpOpenDateLocal, cpPmMinPriAmtLocal,cpCloseDateLocal});
                enableFields(ifr,new String[] {cpOpenDateLocal, cpPmMinPriAmtLocal,cpCloseDateLocal,cpSetupWindowBtn});
            }
        }
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){}
    }
    @Override
    public void cpSendMail(IFormReference iFormReference) {

    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        setGenDetails(ifr);
        hideShowBackToDashboard(ifr,False);
        disableCpSections(ifr);
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerVerifier)){
            if (isEmpty(getSetupFlag(ifr))) {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                    if (getCpDecision(ifr).equalsIgnoreCase(decReject)) {
                        setVisible(ifr, new String [] {cpLandingMsgSection,cpDecisionSection});
                        setMandatory(ifr,new String [] {cpDecisionLocal,cpRemarksLocal,cpLandMsgLocal});
                        enableFields(ifr,new String[] {cpLandingMsgSection,cpDecisionSection});
                    } else if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                        setVisible(ifr,new String [] {cpLandingMsgSection,cpDecisionSection,cpMarketSection,cpCategoryLocal});
                        enableFields(ifr,new String[]{cpDecisionSection,cpCategoryLocal});
                        disableFields(ifr, new String[]{cpSelectMarketLocal});
                        setMandatory(ifr,new String[] {cpDecisionLocal,cpRemarksLocal,cpCategoryLocal});
                        setCpCategory(ifr, new String[]{cpCategorySetup});
                    }
                } else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {}
            }
            else {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){}
                else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){}
            }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        clearFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
        setDecision(ifr,cpDecisionLocal,new String [] {decSubmit,decDiscard});
    }
    
    /******************  TREASURY BILL CODE BEGINS *********************************/
    
    private void tbFormLoadActivity(IFormReference ifr) {
    	//hide all sections except market, decision and lnading message
    	//disable landing msg section
    	setGenDetails(ifr);
    	String[] tbSections = {tbMarketSection,tbLandingMsgSection ,tbTreasuryPriSection, tbTreasurySecSection,
    		tbPrimaryBidSection, tbBranchSection, tbTerminationSection,tbProofOfInvestSection , tbDecisionSection ,
    		tbTreasuryOpsSection ,tbTreasurySecReportSection ,tbPostSection };
    	hideTbSections(ifr);
        hideField(ifr,goBackDashboardSection);
        disableTbSections(ifr);
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerVerifier)){
            if (isEmpty(getSetupFlag(ifr))) {
                if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
                    if (getTbDecision(ifr).equalsIgnoreCase(decReject)) {
                        setVisible(ifr, new String [] {tbLandingMsgSection,tbDecisionSection});
                        setMandatory(ifr,new String [] {tbDecisionLocal,tbRemarksLocal,tbLandMsgLocal});
                        enableFields(ifr,new String[] {tbLandingMsgSection,tbDecisionSection});
                        hideField(ifr,tbCategoryLocal);
                    } else if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) { //landing msg has been set up
                        setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbCategoryLocal});
                        enableFields(ifr,new String[]{tbDecisionSection,tbCategoryLocal});
                        disableFields(ifr, new String[]{tbSelectMarketLocal,tbLandingMsgSection});
                        setMandatory(ifr,new String[] {tbDecisionLocal,tbRemarksLocal,tbCategoryLocal});
                        porpulateCombo(ifr,tbCategoryLocal , new String[]{tbCategorySetup});
                    }
                } 
                else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)) {
                	
                	
                }
            }
            else { //code for when when setup has already been done
                if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){}
                else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){}
            }
        }
        tbSetDecision(ifr);
    }
    
    private void tbSetDecision(IFormReference ifr) {
        clearFields(ifr,new String[]{tbDecisionLocal,tbRemarksLocal});
        setDecision(ifr,tbDecisionLocal,new String [] {decSubmit,decDiscard});
    }
    private void tbCategoryChange(IFormReference ifr) throws ParseException{
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
            if (getTbCategory(ifr).equalsIgnoreCase(tbCategorySetup)){ //set up market
                setVisible(ifr, new String [] {tbTreasuryPriSection,tbSetupSection,tbSetupWindowBtn,tbCutOffTimeSection});
                setMandatory(ifr,new String[] {tbOpenDateLocal, tbPmMinPriAmtLocal,tbCloseDateLocal});
                enableFields(ifr,new String[] {tbOpenDateLocal, tbPmMinPriAmtLocal,tbCloseDateLocal,tbSetupWindowBtn});
                
                //set the unique reference
                setTbUniqueRef(ifr);
            }
        }
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){}
    }
    private void tbUpdateLandingMsg(IFormReference ifr){
        if (getTbUpdateMsg(ifr).equalsIgnoreCase(True)){
        	
            setTbDecisionValue(ifr,decSubmit);
            ifr.setValue(tbRemarksLocal,"Kindly approve landing message update.");
            setInvisible(ifr, new String[]{tbSetupSection,tbDecisionSection});
            undoMandatory(ifr,new String[]{tbRemarksLocal,tbDecisionLocal});
            setMandatory(ifr,tbLandMsgLocal);
            enableFields(ifr,new String[]{tbLandMsgLocal,tbLandingMsgSubmitBtn});
            setVisible(ifr,tbLandingMsgSubmitBtn);
        }
        else {
            clearFields(ifr, new String[]{tbDecisionLocal,tbRemarksLocal,tbLandMsgLocal});
            setVisible(ifr, new String[]{tbSetupSection,tbDecisionSection});
            setMandatory(ifr,new String[]{tbRemarksLocal,tbDecisionLocal});
            undoMandatory(ifr,tbLandMsgLocal);
            disableField(ifr,tbLandMsgLocal);
        }
    }
    
    private String setupTbWindow (IFormReference ifr){
        if (isEmpty(getSetupFlag(ifr))){
            if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
                if (compareDate(getTbOpenDate(ifr),getTbCloseDate(ifr))){}
                else {
                    return "Close date cannot be before open date.";
                }
            }
            else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
            	
            }
        }

        return null;
    }
    private void tbSendMail(IFormReference ifr) {

    }
    private String generateTbUniqueReference(IFormReference ifr) {
    	//generate ref. check if its in db
    	 if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		 return "TBPMA"+getDateWithoutTime();	
         }
         else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
        	 return "TBSEC"+getDateWithoutTime();	
         }
    	 return null;
    }
    private void setTbUniqueRef(IFormReference ifr) {
    	if(isEmpty(getTbUniqueRef(ifr))){
    		ifr.setValue(tbUniqueRef,generateTbUniqueReference(ifr));
    	}
    }
    
    //onDone
   /* private String tbOnDone(IFormReference ifr) {
    	String retMsg ="";
    	if(getTbDecision(ifr).equalsIgnoreCase(decSubmit)) {
    		
    		//check if market is set up -- save details in db
    		 if (getTbCategory(ifr).equalsIgnoreCase(tbCategorySetup)){ //setupdone
    			 String cols ="REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,OPENDATE,CLOSEDATE,CLOSEFLAG";
    			 String vals = "+"'"getTbUniqueRef"";
    			 DbConnect dbConnect = new DbConnect(ifr, new Query().getInsertSetupQuery(cols,vals));
    			 
    		 }
    	}
    	return retMsg;
    		
    }
    try {
        DbConnect dbConnect = new DbConnect(ifr, new Query().getUsersInGroup(groupName));
        for (int i = 0; i < dbConnect.getData().size(); i++){
            groupMail = dbConnect.getData().get(i).get(0)+endMail+","+groupMail; }
    } catch (Exception e){
        logger.error("Exception occurred in getUsersMailInGroup Method-- "+ e.getMessage());
        return null;
    }
    logger.info("getUsersMailsGroup method --mail of users-- "+groupMail.trim());
    return groupMail.trim();*/
  
    
    /******************  TREASURY BILL CODE ENDS *********************************/
}
