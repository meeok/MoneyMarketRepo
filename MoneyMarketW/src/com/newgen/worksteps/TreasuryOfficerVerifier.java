package com.newgen.worksteps;

import com.newgen.api.customService.CpServiceHandler;
import com.newgen.controller.TbApiController;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.*;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

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
        else if (getProcess(ifr).equalsIgnoreCase(omoProcess)) omoFormLoadActivity(ifr);
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
                case cpApiCallEvent:{
                    switch (controlName) {
                        case cpTokenEvent: return new CpServiceHandler(ifr).validateTokenTest();
                        case cpPostEvent: return new CpServiceHandler(ifr).postTransactionTest();
                    }
                    break;
                }
                case formLoad:{}
                break;
                case onLoad:{}
                break;
                case onClick:{
                	logger.info("onclick>>22");
                    switch (controlName){
                        case cpSetupWindowEvent:{
                          return cpSetupSmWindow(ifr, Integer.parseInt(data));
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
                        //****btreasury onclick start **********//
                        case tbPostFaceValue:{
                        	logger.info("post");
                        	return tbPostFaceValue(ifr); //to change when SIT start //tbPost(ifr);
                        	//return tbPost(ifr);  
                        }
                        case tbUnLienCustFaceValue:{
	                		//return tbUnLienCustFaceValue(ifr);
                        	return new TbApiController(ifr).removeLien();
	                	}
                        //****btreasury onclick End **********//	
                    }
                }
                break;
                case onChange:{
                	switch(controlName){
                	case tbValidateToken:{
                		return new TbApiController(ifr).tokenValidation(getFieldValue(ifr,tbtoken));
                	}
                	}
                
                }
                break;
                case custom:{}
                break;
                case onDone:{
                	switch(controlName){
                	case tbOnDone:{
                		return tbOnDone(ifr);
                	}
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
        else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerMaker)){
                if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                  if (getCpDecision(ifr).equalsIgnoreCase(decApprove)){
                      message = "Cutoff time has now been updated.";
                      new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
                  }
                  else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                      message = "Cutoff time was rejected.";
                      new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
                  }
                }
        }
        else if (getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
            if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                if (getCpDecision(ifr).equalsIgnoreCase(decApprove)){
                    message = "A Termination request for "+getCpMarket(ifr)+" Market Commercial Paper with number "+getCpTermCusId(ifr)+" has been  approved by Money_Market_Branch_Verifier and is now pending in your queue for approval. Workitem No. "+getWorkItemNumber(ifr)+".";
                    new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                }
                else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                    message = "A Termination request for Commercial paper with number "+getCpTermCusId(ifr)+" has been rejected by Money_Market_Treasury_Verifier and is now pending in your queue. Workitem No. "+getWorkItemNumber(ifr)+".";
                    new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                }
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
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                    setVisible(ifr, new String[]{cpLandingMsgSection, cpDecisionSection, cpMarketSection});
                    setMandatory(ifr,new String[] {cpDecisionLocal,cpRemarksLocal});
                }
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
                        setVisible(ifr,new String[]{cpReDiscountRateSection,cpSetupSection,cpSetReDiscountRateBtn});
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
                    disableFields(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal, cpSmInvestmentTypeLocal});
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

    private String cpSetupSmWindow(IFormReference ifr, int rowCount){
        if (isEmpty(getWindowSetupFlag(ifr))){
             if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
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

      int validate = new DbConnect(ifr,Query.getUpdateCutoffTimeQuery(id,getCpCloseDate(ifr))).saveQuery();
        if (validate >=0 ) {
            setFields(ifr,cpDecisionLocal,decApprove);
            disableFields(ifr,new String[]{cpDecisionLocal,cpUpdateCutoffTimeBtn});
            return "Cut off time updated successfully. Kindly submit workitem";
        }
        return "Unable to update cut off time. Contact iBPS support";
    }
    private String cpUpdateReDiscountRate(IFormReference ifr){
        String id = null;

        String reDiscount90 = getFieldValue(ifr,cpReDiscountRateLess90Local);
        String reDiscount91180 = getFieldValue(ifr, cpReDiscountRate91To180Local);
        String reDiscount181270 = getFieldValue(ifr,cpReDiscountRate181To270Local);
        String reDiscount271364 = getFieldValue(ifr,cpReDiscountRate271To364Local);

        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket))
            id = getCpPmWinRefNo(ifr);
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket))
            id = getCpSmWinRefNo(ifr);

        int validate = new DbConnect(ifr,Query.getUpdateReDiscountRateQuery(id,reDiscount90,reDiscount91180,reDiscount181270,reDiscount271364)).saveQuery();
        if (validate >=0 ) {
            setFields(ifr,cpDecisionLocal,decApprove);
            disableFields(ifr,new String[]{cpDecisionLocal,cpSetReDiscountRateBtn});

            String table = "<table>" +
                    "<tr>" +
                    "<th>Days to Maturity</th>" +
                    "<th>Re-discount rate</th>" +
                    "</tr>" +
                    "<tr>" +
                    "<td><= 90 days</td>" +
                    "<td>"+reDiscount90+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>91 - 180 days</td>" +
                    "<td>"+reDiscount91180+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>181 – 270 days</td>" +
                    "<td>"+reDiscount181270+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>271 – 364 days</td>" +
                    "<td>"+reDiscount271364+"</td>" +
                    "</tr>" +
                    "</table><br> ";

            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket))
                message = "A commercial paper re-discount for primary market has now been updated with the below details.<br> ";
            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket))
                message = "A commercial paper re-discount for secondary market has now been updated with the below details.<br> ";

            message += table;
            new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
            return "Re-discount Rate updated successfully. Kindly submit workitem";
        }
        return "Unable to update Re-discount Rate. Contact iBPS support";
    }

    /******************  TREASURY BILL CODE BEGINS *********************************/
    /*
     * if this workitem was created by Treasury utility workstep, update table with all failed debits
     * After approval of such workitems complete the workitem
     * set controls for task to be performed before the formloads
     */
    private void tbFormLoadActivity(IFormReference ifr){
    	logger.info("tbFormLoadActivity");
    	 hideTbSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        setGenDetails(ifr);
        disableTbSections(ifr);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{tbRemarkstbx});
     	if(getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
	        //set controls for task to be performed
	        //approving of landing message 
	        logger.info("getTbUpdateLandingMsg>>"+getTbUpdateLandingMsg(ifr));
	        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator) || getTbUpdateLandingMsg(ifr)) { // for approval of landing page
	          
	        	setVisible(ifr,new String[] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbValidatebtn});
	            enableFields(ifr,new String[] {tbDecisionSection,tbValidatebtn});
	            setMandatory(ifr, new String[]{tbDecisiondd,tbRemarkstbx});
	            hideFields(ifr,new String[]{tbMarketUniqueRefId});
	        }
	      
	        else if(getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){  //bid has been approved by branch verifier and customer's account has been liened.assign to verifier
	        	setVisible(ifr, new String[] {tbMarketSection,tbCategorydd,tbBrnchCusotmerDetails,tbBranchPriSection,
	        	tbDecisionSection,tbFetchMandatebtn,tbLienPrincipalbtn,tb_BrnchPri_LienID,tbPostSection,tbUnlienBtn}); 
	        	enableFields(ifr,new String[] {tbDecisionSection,tbLienPrincipalbtn,tbValidatebtn,tbFetchMandatebtn,tbPostSection});
	        	disableFields(ifr, new String[] {tbMarketSection,tbCustAcctNo,tbCustAcctLienStatus,tbBranchPriSection,tbTranID});
	        	setDecision(ifr,tbDecisiondd,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
	        	setMandatory(ifr, new String[] {tbRemarkstbx,tbDecisiondd,tbtoken});//setInvisible(ifr, new String[]{});
	        	hideFields(ifr,new String[] {tbPostbtn,tbtoken,tbTranID});
	          //  disableFields(ifr, new String[] {});
	        	//set private banking control
                setTbPBApprovalCntrls(ifr);
	           
	        }
	        else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryReDiscountRate)){// Approving rediscount rate
	 	    	   logger.info("here");
	 	    	   setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbRediscountRate});
	               disableFields(ifr, new String[]{tbLandingMsgSection,tbMarketSection,tbRediscountRate});
	               enableFields(ifr,new String[]{tbDecisionSection,tbDecisiondd,tbRemarkstbx});
	               setMandatory(ifr,new String [] {tbDecisiondd,tbDecisiondd,tbRemarkstbx});
	   		}
	     	
	        else {//Modification of Primary Market Cut-off Time 
	        	
	        }
	        setDropDown(ifr,tbDecisiondd,new String[]{decApprove,decReject});
     	}
     	
        //secondary Market
        else if(getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)) {
 	       hideField(ifr,tbAssigndd);
 	       setDecision(ifr,tbDecisiondd,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
 	       if (!getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)) {//landing msg is not approved
 	    	   setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
               //disableFields(ifr,new String[] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
               enableFields(ifr,new String[] {tbDecisionSection});
               setMandatory(ifr,new String [] {tbDecisiondd,tbRemarkstbx});
               hideFields(ifr,new String[]{tbMarketUniqueRefId});
 	       }
 	       else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){ //mApproving setup
 	    	   setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
 	    	   disableFields(ifr, new String[]{tbTreasurySecSection,tbLandingMsgSection,tbMarketSection});
 	    	   enableFields(ifr,new String[]{tbDecisionSection});
 	    	   hideFields(ifr,new String[]{tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
 	    	   setMandatory(ifr,new String [] {tbVerificationAmtttbx});
   			}
 	       else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryReDiscountRate)){// Approving rediscount rate
 	    	   logger.info("here");
 	    	   setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbRediscountRate});
               disableFields(ifr, new String[]{tbTreasurySecSection,tbLandingMsgSection,tbMarketSection,tbRediscountRate});
               enableFields(ifr,new String[]{tbDecisionSection,tbDecisiondd,tbRemarkstbx});
               setMandatory(ifr,new String [] {tbDecisiondd,tbDecisiondd,tbRemarkstbx});
   		}
   		
   		else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryCutOff)) {//market is set user can only update
       		setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
       		disableFields(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbMarketSection});
       		hideFields(ifr,new String[]{tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
	       	enableFields(ifr,new String[]{tbDecisionSection,tbDecisiondd,tbRemarkstbx});
	        setMandatory(ifr,new String [] {tbDecisiondd,tbDecisiondd,tbRemarkstbx});
            disableFields(ifr, new String[]{tbVerificationAmtttbx,tbSmIssuedTBillTbl,tbLandingMsgSection,tbSecUniqueReftbx,tbDecisionSection});
	    }
      }
    }
    
    /*
     * update landingMsgApprovedFlg based on decision
     * set the message to be sent out as a mail
     */
    private void tbUpDateLndingMsgFlg(IFormReference ifr) {
    	logger.info("tbUpDateLndingMsgFlg");
    	//if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator) ||getTbUpdateLandingMsg(ifr) ){
         if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
        	 setTbLandingMsgApprovedFlg(ifr,yesFlag);
         }
         else if (getTbDecision(ifr).equalsIgnoreCase(decReject)) {
        	 setTbLandingMsgApprovedFlg(ifr,noFlag);
         }
         //}
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
    
    private String tbOnDone(IFormReference ifr) {
    	logger.info("tbOnDone>>");
    	String retMsg="";
    	 if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		 if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator) ||getTbUpdateLandingMsg(ifr) ) {//approving or rejecitng landing message

        		 tbUpDateLndingMsgFlg(ifr);
    		 }
    		 else if(getPrevWs(ifr).equalsIgnoreCase(branchVerifier)) { //customers principal has been liened pending treasury verifiers approval
	    		 if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
	    			 setFields(ifr,tbBidRStatus,statusAwaitingTreasury);
	    			 logger.info(getFieldValue(ifr,tbBidRStatus));
	    		 }
	    		 else if (getTbDecision(ifr).equalsIgnoreCase(decReject)) {//discard workflow and unlien account
	    			 //todo ....
	    			 //unlien customer account before moving to exit
	    		 }
	    	    }
    		 
    	}
    	 
    	 //secondary Market
    	 else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
    		 logger.info(tbSecondaryMarket);
    		 //landing message approval
    		 if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerInitiator) ||getTbUpdateLandingMsg(ifr)) {//approving or rejecitng landing message
    			 logger.info("approving or rejecitng landing ");
        		 tbUpDateLndingMsgFlg(ifr);
    		 }
    		 
    		 //// Approving setup both new and update
    		 else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){ 
    			 logger.info("New market");
    			 if(getFieldValue(ifr,tbSmSetupdd).equalsIgnoreCase(smSetupNew)) { //new setup; Set up market
    				 if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
    					 String dte =getCurrentDateTime();
    					 logger.info("dte>>."+dte);
    					 retMsg = setUpTbMarketWindow(ifr,getTbMarketUniqueRefId(ifr),dte,getFieldValue(ifr,tbSecCuttOfftime),getFieldValue(ifr,tbVerificationAmtttbx));// set market
    					 updateApprovalFlg(ifr,tbSetupApprovedFlg,retMsg);
    				 }
    		         else if (getTbDecision(ifr).equalsIgnoreCase(decReject)) {
    		        	 setFields(ifr,tbSetupApprovedFlg,yesFlag);
    		         }
    	    			
    	    	}
    			 else { //Updating setup--- ****make setupdd mandatory at maker...//update setup table with minimum principal  
    				 if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
    					 String qry = new Query().getUpdateminPrincipalQuery(getTbMarketUniqueRefId(ifr),getFieldValue(ifr,tbVerificationAmtttbx));
	        			 logger.info("getUpdateminPrincipalQuery>>"+qry);
	        		     int dbr = new DbConnect(ifr,qry).saveQuery();
	        		     logger.info("dbr>>"+dbr);
	        		     if (dbr < 0) 
	        		    	 retMsg ="Unable to update Verification Amount on Setup table";
	        		     updateApprovalFlg(ifr,tbSetupApprovedFlg,retMsg);
    				 }
    				 else if (getTbDecision(ifr).equalsIgnoreCase(decReject)) { 
    					 setFields(ifr,tbSetupApprovedFlg,yesFlag);
    		         }
        		 }
    		}
    		 //updating rediscount rate
    		 else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryReDiscountRate)){
    			 if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
	    			 //update setuptable with details
	    			 String tb90 = getFieldValue(ifr,tbRdrlessEqualto90tbx);
	    			 String tb180 = getFieldValue(ifr,tbRdr91to180);
	    			 String tb270 = getFieldValue(ifr,tbRdr181to270);
	    			 String tb364 = getFieldValue(ifr,tbRdr271to364days);
	    			 
	    			 String qry = new Query().getUpdateRdRateQuery(getTbMarketUniqueRefId(ifr), getWorkItemNumber(ifr), tb90, tb180, tb270, tb364);
	    			 logger.info("getUpdateRdRateQuery>>"+qry);
	    		     int dbr = new DbConnect(ifr,qry).saveQuery();
	    		     logger.info("dbr>>"+dbr);
	    		     if (dbr < 0) 
	    		    	 retMsg ="Unable to update rediscount rate on Setup table";
	    		     updateApprovalFlg(ifr,tbRediscoutApprovedFlg,retMsg);
    			 }
    			 else if (getTbDecision(ifr).equalsIgnoreCase(decReject)) {
		        	 setFields(ifr,tbRediscoutApprovedFlg,yesFlag);
		         }
    		 }
    		 else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryCutOff)){
    			 if (getTbDecision(ifr).equalsIgnoreCase(decApprove)) {
	    			 //update setuptable with details
	    			 String cutofftime = getFieldValue(ifr,tbSecCuttOfftime);
	    			 String qry = new Query().getUpdateCutoffTimeQuery(getTbMarketUniqueRefId(ifr), cutofftime);
	    			 logger.info("getUpdateCutoffTimeQuery>>"+qry);
	    		     int dbr = new DbConnect(ifr,qry).saveQuery();
	    		     logger.info("dbr>>"+dbr);
	    		     if (dbr < 0) 
	    		    	 retMsg ="Unable to update cut off time on Setup table";
	    		     updateApprovalFlg(ifr,tbCutoffApproveFlg,retMsg);
    			 }
    		     else if (getTbDecision(ifr).equalsIgnoreCase(decReject)) {
		        	 setFields(ifr,tbRediscoutApprovedFlg,yesFlag);
		         }
    		 }
    		
    	 }
    	 
    	logger.info("Ondone Validate retMsg>>"+retMsg);
    	return retMsg;
    }
    private String tbUnLienCustFaceValue(IFormReference ifr) {
    	//setTbDecisiondd(ifr,decApprove);
    	//disableFields(ifr, new String[] {tbLienPrincipalbtn,tbDecisiondd});
    	undoMandatory(ifr,tbRemarkstbx);
    	setTb_BrnchPri_LienID(ifr,"L244");
    	setFields(ifr,tbCustAcctLienStatus,"No");
    	hideField(ifr,tbUnlienBtn);
    	setVisible(ifr,new String[] {tbPostbtn,tbtoken,tbTranID});
    	return "Customer Principal unliened Succesfully";
    	
  	}
    
    //post
  	private String tbPostFaceValue(IFormReference ifr) throws Exception{
  		//verify token  --todo
  		//search txn  --todo
  		String retMsg ="";
  		String acct2 ="";
  		String sol2 ="";
  		
  		Properties prop = new Properties();
		InputStream is = new FileInputStream(tbConfigfileName);
		prop.load(is);
		
		acct2 = prop.getProperty("tbHOSuspenceAct");
		logger.info("debit account>>"+acct2);
		sol2 = prop.getProperty("actSol");
		logger.info("debit sol>>"+sol2);
		
  		String acct1 = getTbCustAcctNo(ifr);
  		String sol1 = getTbCustSolid(ifr);
  		String amount = getTbBrnchPriPrncplAmt(ifr);
  		String transParticulars="NTB"+getTbBrnchPriTenordd(ifr)+getTbBrnchCustPriRefNo(ifr);;
  		String partTranRemarks ="TB/"+ getWorkItemNumber(ifr).toUpperCase()+"/FaceValue";
  		if(!(isEmpty(acct1)||isEmpty(sol1))) {
  			retMsg = new TbApiController(ifr).getPostTxn(acct1, sol1, amount, transParticulars, partTranRemarks, acct2, sol2);
  	  		if(retMsg.substring(0, retMsg.indexOf(":")).equalsIgnoreCase(apiSuccess)) {
  	  			String tranid = retMsg.substring(retMsg.indexOf(":"))+1;
  	  			logger.info("trandid >>>"+tranid);
  	  			setFields(ifr,tbTranID,tranid);
  	      		setTbDecisiondd(ifr,decApprove);
  	      		disableFields(ifr,new String[] {tbDecisiondd,tbPostbtn});
  	  		}
  		}
  		
      	return retMsg;
  	}
    
    //set approval flags
 /*   private void updateApprovalFlg(IFormReference ifr,String cntrlName,String retMsg) {
    	if(isEmpty(retMsg)) {
    		setFields(ifr,cntrlName,yesFlag);
    	}
    	else {//clear decision setup was not successful
    		clearFields(ifr,tbDecisiondd);
    		setFields(ifr,cntrlName,noFlag);
    	}
    }*/
    
    //*************** Treasury End *************************/
  	
  	//*************** OMO AUCTION START *************************/
  	private void omoFormLoadActivity(IFormReference ifr) {
		setGenDetails(ifr);
    	hideOmoSections(ifr);
    	setDropDown(ifr,omoDecisiondd,new String[]{decApprove,decReject});
    	clearFields(ifr,new String[]{omoRemarkstbx});
    	setVisible(ifr,new String [] {omoDecisionSection,omoMarketSection,omoCustDetailsSection});
    	disableFields(ifr, new String[]{omoMarketSection,omoCustDetailsSection});
    	setMandatory(ifr,new String [] {omoDecisiondd});
    	hideFields(ifr, new String[] {goBackDashboardSection});
    	setVisible(ifr, new String[]{omoMarketSection, omoDecisionSection});
    	hideFields(ifr,new String[] {omoBankName});
	}
  	 //*************** OMO AUCTION End *************************/

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
