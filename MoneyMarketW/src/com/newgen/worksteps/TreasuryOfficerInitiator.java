package com.newgen.worksteps;

import com.newgen.controller.OmoApiController;
import com.newgen.controller.TbApiController;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.LogGen;
import com.newgen.utils.MailSetup;
import com.newgen.utils.XmlParser;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

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
                        case omofetchAcctDetails:{
                        	omofetchAcctDetails(ifr,data); 
                        }
                        break;
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
                        case cpOnSelectMarket:{
                            if (isCpWindowActive(ifr)){
                                disableCpSections(ifr);
                                setFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal},new String[]{decDiscard,windowActiveErrMessage});
                                disableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                                return windowActiveErrMessage;
                            }
                        }
                        case omoSetupTypeChange:{
                        	omoSetupTypeChange(ifr);
                        	break;
                        }
                        case omoMandateTypeChanged:{
                        	omoMandateTypeChanged(ifr);
                        }
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
   
    private void tbFormLoad (IFormReference ifr){
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
    
    //single or bulk upload settings
    private void omoSetupTypeChange(IFormReference ifr) {
    	if(getFieldValue(ifr,omoSetupType).equalsIgnoreCase(omoSingleSetup)) {
    		setVisible(ifr, new String[]{omoCustDetailsSection});
    		hideFields(ifr, new String[]{omoBulkMandateSection});
    		ifr.clearTable(omoBulkMandateTbl);
    	}
    	else if(getFieldValue(ifr,omoSetupType).equalsIgnoreCase(omoBulkSetup)) {
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
