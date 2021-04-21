package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.*;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import java.text.ParseException;
import java.util.List;

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
        	tbFormLoad(ifr);
        else if (getUtilityFlag(ifr).equalsIgnoreCase(flag))
                cpFormLoadActivity(ifr);
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
                        case cpSetupWindowEvent:{ return setupCpWindow(ifr);}
                        
                        /**** Treasury onClick Start ****/
                        case tbOnClickUpdateMsg:{tbUpdateLandingMsg(ifr);}
                        break;
                        case tbSetupMarket:{ 
                        	return tbSetupMarket(ifr);
                        }
                        case tbViewPriBidSmryReport:{
                        	tbViewPriBidSmryReport(ifr);
                        }  
                        break;
                        case tbDownloadPriBidSmryReport:{
                        	//tbDownloadPriBidSmryReport(ifr);
                        }
                        break;
                        /**** Treasury onClick End ****/
                        case cpViewReportEvent:{
                            viewReport(ifr);
                            break;
                        }
                        case cpDownloadEvent:{
                            setFields(ifr,downloadFlagLocal,flag);
                            break;
                        }
                    }
                }
                break;
                case onChange:{
                    switch (controlName){
                        case cpOnSelectCategory:{cpSelectCategory(ifr);}
                        break;
                        
                        /**** Treasury Onchange Start ****/
                        case tbCategoryddChange:{
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
                	switch (controlName){
                	
	                /**** Treasury onDOne Start ****/
                	case tbOnDone:{
                		return tbOnDone(ifr);
                		}
                
	                /**** Treasury onDone End  ****/
                	}
                }
                break;
                case decisionHistory:{
                	if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) setCpDecisionHistory(ifr);
                	else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) setTbDecisionHistory(ifr);
                }
                break;
                case sendMail:{
                	if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpSendMail(ifr);
                	else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbSendMail(ifr);
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
                if (compareDate(getCpOpenDate(ifr),getCpCloseDate(ifr))) return cpSetUpPrimaryMarketWindow(ifr);
                else return "Close date cannot be before Open date.";
            }
            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                return empty;
            }
        }
        return empty;
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
                enableFields(ifr,new String[] {cpPmMinPriAmtLocal,cpCloseDateLocal,cpSetupWindowBtn});
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
        setInvisible(ifr,new String[]{goBackDashboardSection});
     if (getUtilityFlag(ifr).equalsIgnoreCase(flag)){
           if(getDownloadFlag(ifr).equalsIgnoreCase(flag)){
               showCommercialProcessSheet(ifr);
               setVisible(ifr, new String[]{cpPrimaryBidSection,cpAllocationTbl});
               setInvisible(ifr, new String[]{cpViewReportBtn});
               disableFields(ifr, new String[]{cpDownloadBtn});
           }
           else {
               setGenDetails(ifr);
               setFields(ifr, new String[]{prevWsLocal, selectProcessLocal, cpSelectMarketLocal}, new String[]{utilityWs, commercialProcess, cpPrimaryMarket});
               showCommercialProcessSheet(ifr);
               setVisible(ifr, cpPrimaryBidSection);
           }

        }
       else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerVerifier)){
            if (isEmpty(getSetupFlag(ifr))) {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                    if (getCpDecision(ifr).equalsIgnoreCase(decReject)) {
                        setVisible(ifr, new String [] {cpLandingMsgSection,cpDecisionSection});
                        setMandatory(ifr,new String [] {cpDecisionLocal,cpRemarksLocal,cpLandMsgLocal});
                        enableFields(ifr,new String[] {cpLandingMsgSection,cpDecisionSection});
                    } else if (getCpDecision(ifr).equalsIgnoreCase(decApprove) && isEmpty(getWindowSetupFlag(ifr))) {
                        setVisible(ifr,new String [] {cpLandingMsgSection,cpMarketSection,cpCategoryLocal,cpDecisionSection});
                        setInvisible(ifr,new String[]{cpDecisionSection});
                        enableFields(ifr,new String[]{cpDecisionSection,cpCategoryLocal});
                        disableFields(ifr, new String[]{cpSelectMarketLocal,cpLandingMsgSection});
                        setMandatory(ifr,new String[] {cpCategoryLocal});
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

    private void viewReport(IFormReference ifr){
        List<List<String>> resultSet = new DbConnect(ifr,new Query().getCpPmBidGroupQuery(getWorkItemNumber(ifr))).getData();
        for (List<String> result : resultSet){
            String tenor = result.get(0);
            logger.info("tenor-- "+ tenor);
            String rate = result.get(1);
            logger.info("rate-- "+ rate);
            String totalAmount = result.get(2);
            logger.info("totalAmount-- "+ totalAmount);
            String rateType = result.get(3);
            logger.info("rateType-- "+ rateType);
            String count = result.get(4);
            logger.info("count-- "+ count);
            String groupIndex = result.get(5);
            logger.info("groupIndex-- "+ groupIndex);

            setTableData(ifr,cpAllocationTbl,new String[]{cpAllocTenorCol,cpAllocRateCol,cpAllocTotalAmountCol,cpAllocRateTypeCol,cpAllocCountCol,cpAllocStatusCol,cpAllocGroupIndexCol},
                    new String[]{tenor,rate,totalAmount,rateType,count, statusAwaitingTreasury,groupIndex});
        }
        setVisible(ifr,new String[]{cpAllocationTbl,cpDownloadBtn});
        setInvisible(ifr,new String[]{cpViewReportBtn});
    }

    
    /******************  TREASURY BILL CODE BEGINS *********************************/
    /*hide all sections except market, decision and lnading message
     * disable landing msg section
     */
    private void tbMarketTypeOnChange() {
    	
    }
    /*
     * Primary:  if market window is set and bids are done user shdnt be able to discard workitem
     */
    private void tbSetDecision(IFormReference ifr) {
        clearFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
      //  if()
        setDecision(ifr,cpDecisionLocal,new String [] {decSubmit,decDiscard});
    }
    /*
     * if Market window has been set ..
     */
    private void tbSetCategorydd(IFormReference ifr) {
    	if(getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		 clearFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
    	        if(!isTbWindowActive(ifr)) {//check for when window is closed
    	        	setDropDown(ifr,tbCategorydd,new String [] {tbCategoryReDiscountRate,tbCategoryCutOff,tbCategoryReport});
    	        }
    	        else {// setup not done
    	        	setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup});
    	        }
    	        	
    	}
       
      
      
    }
    private void tbFormLoad(IFormReference ifr) {
    	setGenDetails(ifr);
    	hideTbSections(ifr);
        hideFields(ifr, new String[] {goBackDashboardSection,tbPriSetupbtn});
        setDropDown(ifr,tbAssigndd,new String[]{tbTreasuryUtilityLabel,tbTreasuryVerifierLable},new String[]{tbTreasuryUtility,tbTreasuryVerifier});
        //disableTbSections(ifr);
        setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard}); //cannot discard if bids have been placed by bi
    	//setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup});
    	clearFields(ifr,new String[]{tbRemarkstbx});
    	
        //tb primary Market
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
        	if (!getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)) {//landing msg is not approved
        		clearDropDown(ifr,tbCategorydd);
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
                enableFields(ifr,new String[] {tbLandingMsgSection,tbDecisionSection,tbLandingMsgSection});
                setMandatory(ifr,new String [] {tbDecisiondd,tbRemarkstbx,tbLandMsgtbx});
                setTbUpdateLandingMsg(ifr,True);
                disableFields(ifr, new String[]{tbMarketTypedd,tbCategorydd});
        	}
        	if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))){ //(getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)
        		// ready to set Market;
        		setVisible(ifr,new String [] {tbMarketSection,tbLandingMsgSection,tbDecisionSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbPriOpenDate,tbPriCloseDate,tbCategorydd});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbUniqueReftbx,tbMarketTypedd,tbAssigndd});
                setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup,tbPoolManagerLabel},new String[]{tbCategorySetup,tbPoolManagerLabel});
                
        	}
        	else if((getTbSetUpFlg(ifr).equalsIgnoreCase(flag))){ //getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag) && 
        		// Market is already set
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbPriOpenDate,tbPriCloseDate,tbCategorydd});
                disableFields(ifr, new String[]{tbPriOpenDate,tbPriCloseDate,tbLandingMsgSection,tbPriSetupSection,tbMarketTypedd});
        	}
        	
        	//At cutoff time show select view report to view all bid requests for both fresh and rollover mandate
        	if(isTbWindowClosed(ifr,getTbUniqueRef(ifr))){
        		setVisible(ifr,new String [] {tbPrimaryBidSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbViewPriBidReportbtn,tbPrimaryBidSection,tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbPriSetupSection,tbMarketTypedd});
                hideFields(ifr, new String[] {tbPriBidAllocationTable,tbPriBidAllocationbtn,tbPriBidCustRqstTable,tbPriBidReportTable});
                
                //bid allocation
                setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup,tbCategoryBid},new String[]{tbCategorySetup,tbCategoryBid});
        	}
        	//
        	/*else if(getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag) && (getTbSetUpFlg(ifr).equalsIgnoreCase(flag)) && getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){ // ready to set Market(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbTreasuryPriSetupSection});
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbMarketSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbPriOpenDate,tbPriCloseDate,tbCategorydd});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbPriSetupSection,tbMarketTypedd,});
                hideFields(ifr,new String[] {tbDecisionSection});
        	}*/
        	
        } 
        
        //tb secondary market
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)) {
        }
    }
    /*
     * automatically populate primary market window unique reference in the below 
     * format TPMADATEYEAR for example TBPMA28052020 which will not be editable
     */
    private String tbCategoryChange(IFormReference ifr) throws ParseException{
    	logger.info("tbCategoryChange");
    	//primary Market
    	String retMsg ="";
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
            if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){ 
            	//check if a window is open
            	logger.info("!isTbWindowOpen(ifr)>>"+!isTbWindowOpen(ifr));
            	if(!isTbWindowOpen(ifr)) {
            		setTbUniqueRef(ifr,generateTbUniqueReference(ifr)); //set the unique reference
                	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
                	hideField(ifr,tbDecisionSection);
            	}
            	else if(getTbCategorydd(ifr).equalsIgnoreCase(tbPoolManager)){ 
            		enableField(ifr,tbAssigndd);
            	}
            	else { //market already set clear category dropdown and send msg
            		clearFields(ifr,tbCategorydd);
            		retMsg =tbWindowActiveMessage;
            		disableField(ifr,tbAssigndd );
            	}
            }
            else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)){
            	setVisible(ifr,new String [] {tbPrimaryBidSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbPriBidAllocationbtn,tbViewPriBidReportbtn,tbPrimaryBidSection,tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbPriSetupSection,tbMarketTypedd});
                hideFields(ifr, new String[] {tbPriBidAllocationTable,tbPriBidCustRqstTable,tbPriBidReportTable});
            }
            else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryCutOff)){
            	disableFields(ifr, new String[] {tbPriOpenDate,tbDecisiondd});
            	enableFields(ifr,new String[] {tbPriCloseDate});
            	setTbDecisiondd(ifr,decSubmit);
            }
            else {
            	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
            	setVisible(ifr,tbDecisionSection);
            	setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup});
            }
        }
        
        //secondary MArket
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){}
        return retMsg;
    }
    private void tbUpdateLandingMsg(IFormReference ifr){
        if (getTbUpdateLandingMsg(ifr)){ //true
        	clearDropDown(ifr,tbCategorydd);
            clearFields(ifr, new String[]{tbUniqueReftbx,tbPriOpenDate,tbPriCloseDate});
            disableFields(ifr, new String[]{tbPriSetupSection,tbCategorydd});
            setMandatory(ifr,tbLandMsgtbx);
            setVisible(ifr,tbLandingMsgSubmitBtn); setTbDecisiondd(ifr,decSubmit);
            setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
            setTbDecisiondd(ifr,decSubmit);
        }
        else {
        	setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection});
    		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection});
    		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbPriOpenDate,tbPriCloseDate});
            disableFields(ifr, new String[]{tbLandingMsgSection,tbUniqueReftbx,tbMarketTypedd});
        }
    }
    
    /*
     * save refid, opendate and close date into dbtbPriBidReportTable
     */
    private String validateDate(IFormReference ifr){
    	//primary market
    	String retMsg ="";
        if (isEmpty(getSetupFlag(ifr))){
            if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
                if (!compareDate(getTbPriOpenDate(ifr),getTbPriCloseDate(ifr)))
                	retMsg = "Close date cannot be before open date.";
                }
            else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
            }
        }
        logger.info("retMsg>>>"+retMsg);
        return retMsg;
    }
    private void tbSendMail(IFormReference ifr) {

    }
    
    
    /*
     * save market setup details into db if flag y
     * if market is already setup... fetch details from db and populate
     * check if market is set up -- save details in db
     */
    private String tbSetupMarket(IFormReference ifr) {
    	logger.info("tbSetupMarket>>");
    	String retMsg="";
    	if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){
			retMsg = validateDate(ifr);
			if(isEmpty(retMsg)) {
    			if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))) //market not set
    				retMsg = setUpTbMarketWindow(ifr);// set market
    		}
    	}
    	logger.info("Validate retMsg>>"+retMsg);
    	return retMsg;
    }
    private String tbOnDone(IFormReference ifr) {
    	logger.info("tbOnDone>>");
    	String retMsg="";
    	//if(getTbDecision(ifr).equalsIgnoreCase(decSubmit)) {
    		if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){
    		//	retMsg = validateDate(ifr);
    			if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))) //market not set
	    				retMsg = setUpTbMarketWindow(ifr);// set market
	    		
    		}
    	//}
    	logger.info("Validate retMsg>>"+retMsg);
    	return retMsg;
    }
    private void tbViewPriBidSmryReport(IFormReference ifr){
    	logger.info("tbViewRepor>>>");
    	String qry = new Query().getTbPmBidSummaryQuery(getTbUniqueRef(ifr));
    	logger.info("getTbPmBidSummaryQuery>>"+qry);
        List<List<String>> dbr = new DbConnect(ifr,qry).getData();
        logger.info("TbPmBidSummary>>"+dbr);
        if(dbr.size()>0)
        {
	        for (List<String> ls : dbr){
	        	String rqstType = ls.get(0);
	        	logger.info("rqstType-- "+ rqstType);
	            String tenor = ls.get(1);
	            logger.info("tenor-- "+ tenor);
	            String totalAmount = ls.get(2);
	            logger.info("totalAmount-- "+ totalAmount);
	            String rateType = ls.get(3);
	            logger.info("rateType-- "+ rateType);
	            String count = ls.get(4);
	            logger.info("count-- "+ count);
	            setTableData(ifr,tbPriBidReportTable,new String[]{tbBidRptRqstTypeCol,tbBidRptTenorCol,tbBidRptRateTypeCol,tbBidRptTtlAmtCol,tbBidRptTxnCoutnCol,tbBidRptStatusCol},
	                    new String[]{rqstType,tenor,rateType,totalAmount,count, statusAwaitingTreasury});
	        }
	        setVisible(ifr,new String[]{tbPriBidReportTable,tbViewPriBidReportbtn});
	        disableFields(ifr,new String[]{tbViewPriBidReportbtn});
	        enableFields(ifr,new String[]{tbViewPriBidDwnldBidSmrybtn});
        }
        else {//return a message of no bids for this window
        	}
     }
    
    //view 
    private void tbViewCustomerBids(IFormReference ifr, int rowIndex){
        ifr.clearTable(tbPriBidCustRqstTable);
        String refid = getTbUniqueRef(ifr);
        logger.info("refid>> "+ refid);
        String rqstType = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,0);
        logger.info("rqstType>> "+ rqstType);
        String tenorgrp = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,1);
        logger.info("tenor>> "+ tenorgrp);
        String personalRate = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,2);
        logger.info("personalRate>> "+ personalRate);
        String rateType = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,4);
        
        String qry = new Query().getTbPmCustomerRqstyQuery(refid, rqstType, tenorgrp, rateType, personalRate);
    	logger.info("getTbPmCustomerRqstyQuery>>"+qry);
        List<List<String>> dbr = new DbConnect(ifr,qry).getData();
        logger.info("getTbPmCustomerRqst>>"+dbr);
        if(dbr.size()>0)
        { 
        	for(List<String> ls : dbr){
        		 String id = ls.get(1);
                 String acctNo = ls.get(2);
                 String acctName = ls.get(3);
                 String tenor = ls.get(4);
                 String rate = ls.get(6);
                 String principal = ls.get(7);
                 String defaultAllocation ="100";
                 setTableData(ifr,tbPriBidCustRqstTable,new String[]{tbBidCustRefNocol,tbBidAcctNoCol, tbBidAcctNamecol ,tbBidTenorCol ,tbBidRateCol,tbBidTotalAmtCol,tbBidStausCol,tbBidDefaultAllCol},
                         new String[]{id,acctNo,acctName,tenor,rate,principal,statusAwaitingTreasury,defaultAllocation});
             }
             setVisible(ifr,new String[]{cpBidReportTbl,cpUpdateBtn});
        }
      
           
    }
        	
       
    
    
    /******************  TREASURY BILL CODE ENDS *********************************/
}
