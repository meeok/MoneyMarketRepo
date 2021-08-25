package com.newgen.worksteps;

import com.newgen.controller.OmoApiController;
import com.newgen.controller.TbApiController;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.DbConnect;
import com.newgen.utils.LogGen;
import com.newgen.utils.MailSetup;
import com.newgen.utils.Query;
import com.newgen.utils.XmlParser;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TreasuryOfficerInitiator extends Commons implements IFormServerEventHandler, CommonsI {
    private Logger logger = LogGen.getLoggerInstance(TreasuryOfficerInitiator.class);
    
    //omo auction
    private final XmlParser xmlParser = new XmlParser();
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        try {
        	beforeFormLoadActivity(ifr);
        }
        catch (Exception e){ logger.error("Exception-- "+ e.getMessage());}
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
        try {
            switch (event){
                case formLoad:{}
                break;
                case onLoad:{}
                break;
                case onClick:{
                    switch (control){
                        case goToDashBoard:{
                            backToDashboard(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                                cpBackToDashboard(ifr);
                           else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess))
                                tbBackToDashboard(ifr);
                           else if (getProcess(ifr).equalsIgnoreCase(omoProcess))
                        	   omoBackToDashboard(ifr);
                            clearFields(ifr,new String[] {selectProcessLocal});
                            break;
                        }
                        //-----  Omo onclick starts------//
                        case omofetchAcctDetails:{
                        	omofetchAcctDetails(ifr,data); 
                        }
                        break;
                        case omoFetchSingleActDtls:{
                        	
                        }
                        break;
                        case omoSpecialRateClicked: {
                        	omoSpecialRateClicked(ifr);
                        }
                        break;
                        //-------omo onclick ends -------//
                    }
                }
                break;
                case onChange:{
                    switch (control){
                        case onChangeProcess: {
                            selectProcessSheet(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
                            else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) 
                            	tbFormLoad(ifr);
                            else if (getProcess(ifr).equalsIgnoreCase(omoProcess)) 
                            	omoFormLoad(ifr);
                            break;
                        }
                        //omo onchnage starts here---------//
                        case omoTerminateTypeChanged:{
                        	 omoTermTypeChanged(ifr);
                        }
                        case tbGetCustInvestmentDetails:{
                        	if(getFieldValue(ifr,omoMandateTypedd).equalsIgnoreCase(proofofinvestmentVal)) {
                        		return omoGetCustDetailsForPoi(ifr);
                        	}
                        	else if(getFieldValue(ifr,omoMandateTypedd).equalsIgnoreCase(terminationVal)) {
                        		return omoGetCustDetailsForTermination(ifr);
                        	}
                        	
                        }
                        //------omo onchange ends here----//
                        case cpOnSelectMarket:{
                            if (isCpWindowActive(ifr)){
                                disableCpSections(ifr);
                                setFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal},new String[]{decDiscard,windowActiveErrMessage});
                                disableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                                return windowActiveErrMessage;
                            }
                        }
                        /********** TREASURY START **********************/
                        case tbMarketTypeddChange:{
                        	//check if market has been set
                        	return tbMarketTypeddChange(ifr);
                        }
                        case tbDecisionddChange:{
                        	tbDecChange(ifr);
                        }
                        
                        /********** TREASURY END **********************/

                        /********** OMO Start **********************/
                        case omoSetupTypeChange:{
                        	omoSetupTypeChange(ifr);
                        	break;
                        }
                        case omoMandateTypeChanged:{
                        	omoMandateTypeChanged(ifr);
                        }
                        case omoCategoryChange:{
                        	omoCategoryChange(ifr);
                        }
                        case omoFbnCustChange:{
                        	omoFbnCustChange(ifr);
                        }
                        case omoDecChange:{
	                		omoDecChange(ifr);
	                	}
                        /********** OMO End **********************/
                    }
                }
                break;
                case custom:{}
                break;
                case onDone:{
                	switch(control) {
                		case omoOnDone:{
                			return createWorkitems(ifr,data);
                		
                		}
                	
                	}
                }
                break;
                case decisionHistory:{
                   if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                       setCpDecisionHistory(ifr);
                   else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) 
                	   setTbDecisionHistory(ifr);
                }
                break;
                case sendMail:{
                    if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                        cpSendMail(ifr);
                    else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
                    	//tbSendMail(ifr);
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.info("Exception Occurred-- "+ e.getMessage());
        }
        return null;
    }


	private void omoTermTypeChanged(IFormReference ifr) {
		clearFields(ifr, new String[] {tbTermAdjustedPrncpal,tbTermCashValue,tbTermAmtDueCust,tbTermCashValue,tbTermPenaltyCharge});
    	hideFields(ifr, new String[] {tbTermVal,tbTermCashValue,tbTermbtn});
		undoMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
		if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(tbTerminationTypeFull)) {
			setVisible(ifr, new String[] {tbTermbtn});
			hideFields(ifr, new String[] {tbTermVal,tbTermCashValue,tbTermAdjustedPrncpal});
			undoMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
		}
		else if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(tbTerminationTypePartial)) {
			setVisible(ifr, new String[] {tbTermCashValue,tbTermbtn});
			setMandatory(ifr, new String[] {tbTermCashValue});
		}
		/*else {
			
			//clear all termfiels
			//clearFields(ifr, new String[] {tbTermAdjustedPrncpal,tbTermCashValue,tbTermAmtDueCust,tbTermCashValue});
		}*/
	}
	
	   private void omoSpecialRateClicked(IFormReference ifr) {
   		logger.info("special rate clicked: "+getFieldValue(ifr,tbSpecialRate));
   		if(getFieldValue(ifr,tbSpecialRate).equalsIgnoreCase("true")) {
   			setVisible(ifr,tbSpecialRateValue);
   			setMandatory(ifr,tbSpecialRateValue);
   		}
   		else {
   			hideField(ifr,tbSpecialRateValue);
   			undoMandatory(ifr,tbSpecialRateValue);
   		}
   		
   	}

	@Override
    public void cpSendMail(IFormReference ifr){
        if (getCpDecision(ifr).equalsIgnoreCase(decSubmit)) {
            message = "A window open request for Commercial Paper has been Initiated with ref number " + getWorkItemNumber(ifr) + ".";
            new MailSetup(ifr, getWorkItemNumber(ifr), getUsersMailsInGroup(ifr, groupName), empty, mailSubject, message);
        }
    }
    @Override
    public void cpFormLoadActivity(IFormReference ifr){
        cpSetDecision(ifr);
        setVisible(ifr, new String[]{cpLandingMsgSection, cpDecisionSection, cpMarketSection});
        enableFields(ifr,new String[]{cpLandMsgLocal,cpSelectMarketLocal});
        setMandatory(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
    }
    public void beforeFormLoadActivity(IFormReference ifr){
        hideProcess(ifr);
        hideCpSections(ifr);
        hideTbSections(ifr);
        logger.info("1111");
        hideOmoSections(ifr);
        logger.info("1111");
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        setGenDetails(ifr);
        clearFields(ifr,new String [] {selectProcessLocal});
        setMandatory(ifr, new String[]{selectProcessLocal});
        setFields(ifr, new String[]{currWsLocal,prevWsLocal},new String[]{getCurrentWorkStep(ifr),na});
        setWiName(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decSubmit,decDiscard});
    }


    private void cpBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
        clearFields(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
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
    
    /******************  TREASURY BILL CODE BEGINS *********************************/
    
    private void tbBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {tbMarketTypedd,tbLandMsgtbx,tbDecisiondd,tbRemarkstbx});
        clearFields(ifr,new String [] {tbMarketTypedd,tbLandMsgtbx,tbDecisiondd,tbRemarkstbx});
    }
    private String tbMarketTypeddChange(IFormReference ifr){
    	String retMsg ="";
    	if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		retMsg = tbGetWindowActiveWiname(ifr,getTbMarket(ifr));
    		if(isEmpty(retMsg)){
    			enableFields(ifr,new String[]{tbLandingMsgSection,tbDecisionSection});
    		}
    		else {
    			retMsg = getTbMarket(ifr)+tbWindowActiveMessage +" WorkItem No is: "+retMsg;
    			//hide or disable all fields
    			disableFields(ifr, new String[]{tbDecisionSection,tbLandingMsgSection});
    		}
    	}
    	return retMsg;
    }
   
    private void tbFormLoad (IFormReference ifr){
    	hideTbSections(ifr);
    	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
    	setVisible(ifr, new String[]{tbLandingMsgSection, tbDecisionSection, tbMarketSection});
    	setMandatory(ifr,new String [] {tbMarketTypedd,tbCategorydd,tbLandMsgtbx,tbDecisiondd,tbRemarkstbx});
    	enableFields(ifr,new String[]{tbLandingMsgSection,tbDecisionSection,tbMarketSection});
    	hideFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbCategorydd,tbAssigndd,tbMarketUniqueRefId});
        //setDropDown(ifr,tbAssigndd,new String[]{tbTreasuryUtilityLabel,tbTreasuryVerifierLable},new String[]{tbTreasuryUtility,tbTreasuryVerifier});
    }
    
    public void tbSendMail(IFormReference ifr){
        String message = "A window open request for "+treasuryProcessName+" has been Initiated with ref number "+getWorkItemNumber(ifr)+".";
        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
    }
    
    /*
     * Search customer and populate table with customer investments for POI
     * --todo ensure bidding is successful and at awaiting mat.before performing premature termination
     * --raise a flag when termination is successful and use new principal if its partial termination..
     */
    private String omoGetCustDetailsForPoi(IFormReference ifr){
    	clearTable(ifr,omoPoiCustDetailsTbl);
        String idqry = new Query().getTbCustMandate(getTbMarket(ifr), getFieldValue(ifr,tbCustAcctOrRefID));
        logger.info("getTbCustMandateDetailsQuery>>>"+idqry);
        List<List<String>> iddbr= new DbConnect(ifr,idqry).getData();
        logger.info("getTbCustMandateDetailsQuery db result>>>"+iddbr);
        if (iddbr.size()>0) {
        	 for (List<String> ls : iddbr){
                 String date = ls.get(0);
                 String refNo = ls.get(1);
                 String accountNo = ls.get(2);
                 String accountName = ls.get(3);
                 String principalamt = ls.get(4);
                 String status = ls.get(5);
                 String maturityDte = ls.get(6);
                 String custid = ls.get(7);
                 
                 setTableGridData(ifr,tbPoiCustDetailsTbl,new String[]{tbDateCol,tbRefNoCol,tbAcctNoCol,tbCustNameCol,tbAmountCol,tbStatusCol},
                         new String[]{date,custid,accountNo,accountName,principalamt,status});
             }
        	 disableFields(ifr,new String[]{tbPoiCustDetailsTbl});
        	 setVisible(ifr,new String[]{tbProofOfInvestSection});
             enableFields(ifr,new String[]{tbProofOfInvestSection});
        }
        else {
       	 hideFields(ifr,new String[]{tbProofOfInvestSection});
       	return "No Details found for this Mandate";
       	
       }
        return "";
    }
    
    /*
     * Search customer and populate termination table with customer investments
     */
    private String omoGetCustDetailsForTermination(IFormReference ifr){
        clearTable(ifr,tbTerminationMandateTbl);
        String idqry = new Query().getTbCustMandate(getTbMarket(ifr), getFieldValue(ifr,tbCustAcctOrRefID));
        logger.info("getTbCustMandateDetailsQuery>>>"+idqry);
        List<List<String>> iddbr= new DbConnect(ifr,idqry).getData();
        logger.info("getTbCustMandateDetailsQuery db result>>>"+iddbr);
        if (iddbr.size()>0) {
        	 for (List<String> ls : iddbr){
        		 String daystoMaturity ="";
        		 String date = ls.get(0);
                 String refNo = ls.get(1);
                 String accountNo = ls.get(2);
                 String accountName = ls.get(3);
                 String principalamt = ls.get(4);
                 String status = ls.get(5);
                 String maturityDte = ls.get(6);
                 logger.info("maturityDte>>"+maturityDte);
                 String custid = ls.get(7);
                 String winRefId =ls.get(8);
                 try {
                	 daystoMaturity =  String.valueOf(getDaysToMaturity(maturityDte.substring(0,maturityDte.indexOf(" "))));
                	 logger.info("maturityDte>>"+maturityDte);
                 }
                 catch(Exception ex) {
                	 logger.info("Maturity date may be null contact Admin");
                 }

                 setTableGridData(ifr,tbTerminationMandateTbl,new String[]{tbDateCol,tbRefNoCol,tbAcctNoCol,tbCustNameCol,tbAmountCol,tbDaysToMaturityCol,tbStatusCol,tbMarketWinRefIDCol,tbMaturityDtCol},
                         new String[]{date,custid,accountNo,accountName,principalamt,daystoMaturity,status,winRefId,maturityDte});
             }
             setVisible(ifr,new String[]{tbTerminationSection,tbPoiGenerateBtn,tbPoiCustDetailsTbl});
             enableFields(ifr,new String[]{tbTerminationSection,tbPoiGenerateBtn});
             clearFields(ifr, new String[] {tbTermAdjustedPrncpal,tbTermCashValue,tbTermAmtDueCust,tbTermCashValue});
             hideFields(ifr, new String[] {termCustRefId,tbPoiGenerateBtn,tbTermAdjustedPrncpal,tbTermCashValue,tbTermAmtDueCust,tbTermCashValue,tbTermRediscountRate});
             disableFields(ifr, new String[] {tbTermPenaltyCharge,tbTermDate});
            // disableFields(ifr,new String[]{termCustRefId,tbPoiGenerateBtn});
          
        }
        else {
        	 hideFields(ifr,new String[]{tbTerminationSection});
        	return "No Details found for this Mandate";
        	
        }
        return "";
    }
    
    /******************  OMO AUCTION CODE ENDS ***********************************/
    private void omoBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {omoMarketTypedd,omoDecisiondd,omoRemarkstbx});
        clearFields(ifr,new String [] {omoMarketTypedd,omoDecisiondd,omoRemarkstbx});
    }
    private void omoFormLoad (IFormReference ifr){
    	setDropDown(ifr,omoDecisiondd,new String[]{decSubmit,decDiscard});
       // setDropDown(ifr,omoCategorydd,new String[]{tbCategorySetup,tbCategoryMandate},new String[]{tbCategorySetup,tbCategoryMandate});
    	setVisible(ifr, new String[]{omoMarketSection, omoDecisionSection});
    	setMandatory(ifr,new String [] {omoCategorydd,omoDecisionSection,omoRemarkstbx});
    	//enableFields(ifr,new String[]{tbLandingMsgSection,tbDecisionSection,tbMarketSection});
    }
    
    private void omoFbnCustChange(IFormReference ifr) {
    	if(getOmoFbnCustomer(ifr).equalsIgnoreCase(yes)) {
    		setVisible(ifr,new String[]{omoFetchAcctDetailsBtn,omoCustCif,omoCustCurr,omoCustSolid});
    		clearFields(ifr,omoBankName);
    		hideFields(ifr,new String[] {omoBankName});
    	}
    	else if(getOmoFbnCustomer(ifr).equalsIgnoreCase(no)) {
    		clearFields(ifr,omoBankName);
    		setVisible(ifr,new String[]{omoFetchAcctDetailsBtn});
    		hideFields(ifr,new String[] {omoBankName,omoFetchAcctDetailsBtn,omoCustCif,omoCustCurr,omoCustSolid});
    		enableFields(ifr, new String[]{omoCustName});

    	}
    	else {
    		clearFields(ifr,omoBankName);
    		hideFields(ifr,new String[] {omoBankName,omoFetchAcctDetailsBtn});
    	}
		
	}
    
    //single or bulk upload settings
    private void omoSetupTypeChange(IFormReference ifr) {
    	logger.info(getOmoSetupType(ifr));
    	if(getOmoSetupType(ifr).equalsIgnoreCase(omoSingleSetup)) {
    		logger.info(getOmoSetupType(ifr));
    		setVisible(ifr, new String[]{omoCustDetailsSection});
    		hideFields(ifr, new String[]{omoBulkMandateSection});
    		disableFields(ifr, new String[]{omoCustName,omoCustCif,omoCustCurr,omoCustSolid,
    				});

    		ifr.clearTable(omoBulkMandateTbl);
    	}
    	else if(getOmoSetupType(ifr).equalsIgnoreCase(omoBulkSetup)) {
    		setVisible(ifr, new String[]{omoBulkMandateSection});
    		hideFields(ifr, new String[]{omoCustDetailsSection});
    		omoClearCustDtlsField(ifr);
    	}
    	else{
    		hideFields(ifr, new String[]{omoCustDetailsSection,omoBulkMandateSection});
    		ifr.clearTable(omoBulkMandateTbl);
    	}	
		
	}
    
    public void omoSendMail(IFormReference ifr){
        String message = "A window open request for "+treasuryProcessName+" has been Initiated with ref number "+getWorkItemNumber(ifr)+".";
        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
    }
    
  //mandate type changed
    private void omoMandateTypeChanged (IFormReference ifr){
    	if(getFieldValue(ifr,omoMandateTypedd).equalsIgnoreCase(tbMandateTypeLien)) {
    		setVisible(ifr, new String[] {omoLienSection});
    		//enableFields(ifr, new String[] {omoLiencustRefId,omoLienType});
    		setMandatory(ifr, new String[] {omoLiencustRefId});
    		//hideFields(ifr, new String[] {tbLienStatus});
    	}
    	else
    	{
        	setVisible(ifr, new String[] {tbSearchCustSection});
        	undoMandatory(ifr, new String[] {tbLiencustRefId});
        	hideFields(ifr, new String[] {tbLienSection});
    	}
    	
    	/*if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(proofofinvestmentVal)) {
    		setVisible(ifr, new String[] {tbProofOfInvestSection});
    		enableFields(ifr,new String[] {tbProofOfInvestSection});
    	}
    	else if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(terminationVal)) {
    		setVisible(ifr, new String[] {tbTerminationSection});
    		enableFields(ifr,new String[] {tbTerminationSection});
    		//enableFields(ifr,new String[] {});
    	}
    	/*else {
    		hideFields(ifr, new String[] {tbProofOfInvestSection});
    	}*/
    	
    }

    
	private void omoCategoryChange(IFormReference ifr) {
		logger.info(getOmoCategorydd(ifr).equalsIgnoreCase(tbCategorySetup));
		if(getOmoCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)) {
			setVisible(ifr, new String[] {omoSetupType});
			hideFields(ifr, new String[] {omoMandateTypedd});
		}
		else if(getOmoCategorydd(ifr).equalsIgnoreCase(tbCategoryMandate)) {
			setVisible(ifr, new String[] {omoMandateTypedd});
			hideFields(ifr, new String[] {omoSetupType});
		}
		else {
			hideFields(ifr, new String[] {omoSetupType,omoMandateTypedd});
			clearFields(ifr, new String[] {omoSetupType,omoMandateTypedd});
		}
	}
    
    //populate table with account details
	private String omofetchAcctDetails(IFormReference ifr,String gridCnt) {
		String retMsg="";
		String fbnCustomer ="";
		if(getOmoSetupType(ifr).equalsIgnoreCase(omoBulkSetup)) {
			retMsg = omoFetchBulkAcctDetails(ifr, gridCnt);
		}
		else if(getOmoSetupType(ifr).equalsIgnoreCase(omoSingleSetup)) {
			retMsg = new OmoApiController(ifr).fetchSingleAcctDetails();
		}
		return retMsg;
	}
	
	//fetch bulk account details
	private String omoFetchBulkAcctDetails(IFormReference ifr,String gridCnt){
		String retMsg="";
		if(getOmoSetupType(ifr).equalsIgnoreCase(omoBulkSetup)) {
			int rowCount = 0;
			try {
				rowCount = Integer.parseInt(gridCnt);
	    		logger.info("rwcnt>>"+rowCount);
	    	}
	    	catch(Exception ex) {
	    		logger.info("omo bulk set up parse grid count error>>>"+ ex.toString());
	    		retMsg ="Kindly upload Mandates";
	    	}
			//fetch account details for each row
			if(rowCount>0) {
				for(int i=0; i< rowCount; i++) {
					String acctNo = ifr.getTableCellValue(omoBulkMandateTbl, i, 1);
					String fbnCustomer = ifr.getTableCellValue(omoBulkMandateTbl, i, 2);
					if(fbnCustomer.equalsIgnoreCase(yes)) {
						String outputxml = new OmoApiController(ifr).fetchAcctDetails(acctNo);
						if(!(outputxml.equalsIgnoreCase(apiFailed) || outputxml.equalsIgnoreCase(apiNoResponse))){ //successful
							xmlParser.setInputXML(outputxml);
							String currency = xmlParser.getValueOf("currencyCode");
			                logger.info("currency- "+ currency);
			                String email = xmlParser.getValueOf("EmailAddr");
			                logger.info("email- "+ email);
			                String sol = xmlParser.getValueOf("SOL");
			                logger.info("sol- "+ sol);
			                String cusDetails = xmlParser.getValueOf("PersonName");
			                xmlParser.setInputXML(cusDetails);
			                String name = xmlParser.getValueOf("Name");
			                logger.info("name- "+ name);
							ifr.setTableCellValue(omoBulkMandateTbl, i, 4, name);
							ifr.setTableCellValue(omoBulkMandateTbl, i, 15, sol);
							ifr.setTableCellValue(omoBulkMandateTbl, i, 17, currency);
							ifr.setTableCellValue(omoBulkMandateTbl, i, 16, "");//cif
							ifr.setTableCellValue(omoBulkMandateTbl, i, 18, apiSuccess);
						}
						else {//fetch acct details failed
							ifr.setTableCellValue(omoBulkMandateTbl, i, 18, outputxml);
						}
					}
				}
			}
			else //table is empty
				retMsg ="Kindly upload Mandates";
			
		}
		return retMsg;
	}
	
	 /*
     * generate customer unique ref for bid and save in db
     */
	private String omoDecChangeActivities(IFormReference ifr) {
		omoDecChange(ifr);
	  	if(getOmoDecision(ifr).equalsIgnoreCase(decSubmit)) {
	  		if(getOmoCategorydd(ifr).equalsIgnoreCase(tbCategorySetup) && isEmpty(getOmoCustRefId(ifr)) ){//generate customer unique ref
	     			setFields(ifr,omoCustRefId,omoGenerateCustRefNo(ifr));
	     			return tbSaveGeneratedId(ifr, getWorkItemNumber(ifr), getTbBrnchCustPriRefNo(ifr));
	 	    	}
	  		 
	  	}
	  	
	  	return"";
		
	}
	
	private String createWorkitems(IFormReference ifr, String gridcnt) {
		int mandateCount = 0;
		if(getOmoSetupType(ifr).equalsIgnoreCase(omoBulkSetup)) {
			try {
				mandateCount = Integer.parseInt(gridcnt);
			}
			catch(Exception ex) {
				logger.info("bulk gridcount exception");
				return ("No Mandate uploaded. Kindly upload customer mandates");
			}
			//insert details from table into omo_import table
		}
		return "";
	}
    
    
    /******************  OMO AUCTION  CODE BEGINS *********************************/
}
