package com.newgen.worksteps;

import com.newgen.processMethods.CpController;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.LogGen;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BranchInitiator extends Commons implements IFormServerEventHandler, CommonsI {
     private static final Logger logger = LogGen.getLoggerInstance(BranchInitiator.class);

    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        beforeFormLoadActivity(ifr);
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
                case formLoad:
                case onLoad:
                case cpApiCallEvent:{
                  String resp = CpController.fetchAccountDetailsController(ifr);
                  if (isEmpty(resp)) return CpController.fetchLienController(ifr);
                  else if (resp.equalsIgnoreCase(cpEmailMsg)) return cpEmailMsg + "#" + CpController.fetchLienController(ifr);
                  else return resp;
                }
                case onClick:{
                    switch (control){
                        case goToDashBoard:{
                            backToDashboard(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                                cpBackToDashboard(ifr);
                            else  if (getProcess(ifr).equalsIgnoreCase(treasuryProcess))
                                tbBackToDashboard(ifr);
                            clearFields(ifr,new String[] {selectProcessLocal});
                            break;
                        }
                        
                        //****************Treasurry Starts here *********************//
                        case tbValidateCustomer:{
                        	return tbValidateCustomer(ifr);
                        }
                        
                        //****************Treasurry Ends here *********************//
                    }
                }
                break;
                case onChange:{
                    switch (control){
                        case onChangeProcess: {
                            selectProcessSheet(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbFormLoad(ifr);
                            break;
                        }
                        case cpOnSelectMarket:{
                            setVisible(ifr,new String[]{cpCategoryLocal});
                            enableFields(ifr,new String[]{cpCategoryLocal});
                            setMandatory(ifr,new String[]{cpCategoryLocal});
                        }
                        break;
                        case cpOnSelectCategory:{return cpSelectCategory(ifr);}
                        case cpOnChangeRateType:{
                            if (getCpPmRateType(ifr).equalsIgnoreCase(rateTypePersonal)){
                                setVisible(ifr,new String[]{cpPmPersonalRateLocal});
                                setMandatory(ifr,new String[]{cpPmPersonalRateLocal});
                                enableFields(ifr,new String[]{cpPmPersonalRateLocal});
                            }
                            else{
                                clearFields(ifr,cpPmPersonalRateLocal);
                                undoMandatory(ifr,cpPmPersonalRateLocal);
                                setInvisible(ifr,cpPmPersonalRateLocal);
                            }
                        }
                        break;
                        case cpCheckPrincipalEvent:{
                            if (getCpPmCustomerPrincipal(ifr) < getCpPmWinPrincipalAmt(ifr)) {
                                clearFields(ifr,cpPmPrincipalLocal);
                                return minPrincipalErrorMsg;
                            }
                        }
                        break;
                        case  cpCheckTenorEvent:{
                            if (getCpPmTenor(ifr) < 7 || getCpPmTenor(ifr) > 270){
                                clearFields(ifr,cpPmTenorLocal);
                                return tenorErrorMsg;
                            }
                            break;
                        }
                        
                      //****************Treasurry Starts here *********************//
    	                case tbMarketTypeddChange:{
    	                	return tbMarketTypeddChange(ifr);
    	                }
    	                case tbCustAcctNoChange:{
    	                	tbFetchAccountDetails(ifr);
    	                }
    	                break;
    	                case tbBrnchPriRollovrddChange:{
    	                	tbBrnchPriRollovrddChange(ifr);
    	                }
    	                break;
    	                case tbBrcnhPriRateTypeddChange:{
    	                	return tbBrcnhPriRateTypeddChange(ifr);
    	                }
    	                case tbBrnchPriPrncplAmtChange:{
    	                	return tbValidatePrincipalAmt(ifr);
    	                }
    	                case tbCategoryddChange:{
    	                	tbCategoryddChange(ifr);
    	                }
    	                break;
                        //****************Treasurry Ends here *********************//
                    }
                    
                    
                }
                break;
                case custom:
                case onDone:{
                    switch (control){
                        case validateWindowEvent:{
                            if (cpCheckWindowStateById(ifr)){
                                setFields(ifr,cpPmCustomerIdLocal,cpGenerateCustomerId(ifr));
                            }
                            else return cpValidateWindowErrorMsg;
                        }
                        
                        //****************Treasurry on Change Starts here *********************//
                        case tbOndone:{
                        	return tbOnDone(ifr);
                        }
                        
                        //****************Treasurry on Change Ends here *********************//

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

                }
            }
        }
        catch (Exception e){
            logger.error("Exception occurred in executeServerEvent-- "+ e.getMessage());
        }
        return null;
    }

   
	private String cpSelectCategory(IFormReference ifr) {
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
                if (isCpWindowActive(ifr)) {
                    setVisible(ifr,new String[]{cpBranchPriSection,cpCustomerDetailsSection});
                    setMandatory(ifr,new String[]{cpCustomerAcctNoLocal,cpPmTenorLocal,cpPmPrincipalLocal,cpPmRateTypeLocal});
                    enableFields(ifr,new String[]{cpCustomerAcctNoLocal,cpPmTenorLocal,cpPmPrincipalLocal,cpPmRateTypeLocal});
                    setDropDown(ifr,cpPmReqTypeLocal,new String[]{cpPmReqFreshLabel},new String[]{cpPmReqFreshValue});
                    setFields(ifr,cpPmReqTypeLocal,cpPmReqFreshValue);
                    setFields(ifr,new String[]{cpPmReqTypeLocal, cpPmInvestmentTypeLocal},new String[]{cpPmReqFreshValue,cpPmInvestmentPrincipal});
                    setCpPmWindowDetails(ifr);
                } else return windowInactiveMessage;
            }
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


    private void cpBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {cpSelectMarketLocal,cpDecisionLocal,cpRemarksLocal});
        clearFields(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
    }

    @Override
    public void cpSendMail(IFormReference ifr) {

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
    public void cpFormLoadActivity(IFormReference ifr) {
        cpSetDecision(ifr);
        setVisible(ifr, new String[]{cpDecisionSection, cpMarketSection});
        enableFields(ifr,new String[]{cpSelectMarketLocal});
        setMandatory(ifr,new String [] {cpSelectMarketLocal,cpDecisionLocal,cpRemarksLocal});
        setDropDown(ifr,cpCategoryLocal,new String[]{cpCategoryBid,cpCategoryMandate,cpCategoryReport});
    }
   

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decSubmit,decDiscard});

    }
    
    //**********************Treasury Starts here **********************//
  
    private void tbBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {tbMarketTypedd,tbLandMsgtbx,tbDecisiondd,tbRemarkstbx});
        clearFields(ifr,new String [] {tbMarketTypedd,tbLandMsgtbx,tbDecisiondd,tbRemarkstbx});
		undoMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
    }

    private void tbFormLoad(IFormReference ifr) {
    	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
        setVisible(ifr, new String[]{tbMarketSection, tbDecisionSection});
        enableFields(ifr,new String[]{tbMarketTypedd});
        setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx});
        setDropDown(ifr,tbCategorydd,new String[]{tbCategoryBid,tbCategoryReport,tbCategoryMandate});
        
    }
    /*
     * if setup has been done for selected market display corresponding fields
     */
    
    private String tbMarketTypeddChange(IFormReference ifr){
    	String retMsg ="";
    	setTbPriWindownUnqNo(ifr,getTbActiveWindowwithRefid(ifr));
    	if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		if(!isEmpty(getTbPriWindownUnqNo(ifr))){
    			setVisible(ifr, new String[]{tbCategorydd});
    			//disableFields(ifr, new String[]{tbMarketSection});
    		}
    		else {
    			clearFields(ifr,tbMarketTypedd);
    			retMsg = getTbMarket(ifr)+tbWindowInactiveMessage;
    			//hide or disable all fields
    		}
    	}
    	return retMsg;
    }
    private void tbBrnchPriRollovrddChange(IFormReference ifr){
    	if(getTbBrnchPriRollovrdd(ifr).equalsIgnoreCase(tbBrnchPriRoTermteatMaturity))
    		setMandatory(ifr,tbBrnchPriTermTypedd);
    }
    
    private String tbBrcnhPriRateTypeddChange(IFormReference ifr){
    	logger.info("tbBrcnhPriRateTypeddChange>>>>");
    	if(getTbBrcnhPriRateTypedd(ifr).equalsIgnoreCase(tbBrnchPriRtPersonal)){
    		setVisible(ifr,tbBrcnhPriPersonalRate);
    		setMandatory(ifr,tbBrcnhPriPersonalRate);
    	}
    	else {
    		hideField(ifr,tbBrcnhPriPersonalRate);
    		clearFields(ifr,tbBrcnhPriPersonalRate);
    	}
    	
    	String retMsg ="";
    	retMsg = tbValidatePrincipalAmt(ifr);
    	logger.info("tbretmsg>>>>"+retMsg);
    	return retMsg;
    }
    private void tbCategoryddChange(IFormReference ifr){
    	if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)){
    		//check if market is setup
    		setVisible(ifr, new String[] {tbBrnchPriCusotmerDetails,tbBranchPriSection,tbDecisionSection});
    		setMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
    		setTbBrnchPriRqsttype(ifr,tbBidRqstType);
    		//tbGenerateCustRefNo(ifr, getTbMarket(ifr));)
    	}
    	else {
    		setTbBrnchPriRqsttype(ifr,"");
    		hideFields(ifr, new String[] {tbBrnchPriCusotmerDetails,tbBranchPriSection,tbDecisionSection});
    		undoMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});

    	}
    	//logger.info("tbOnDone1>>>");
    	//tbOnDone(ifr);
    	//logger.info("tbOnDone2>>>");
    }
    
    /*
     * Minimum of N100,000 for bank Rate and Minimum of N50,000,000 for personal Rate
     * Validation for amount should be in thousands and comma used for separation)
     */
    private String tbValidatePrincipalAmt(IFormReference ifr){
    	//setTbBrnchInitRetMsg("");
    	String retMsg ="";
    	logger.info("tbValidatePrincipalAmt");
    	logger.info("getTbBrcnhPriRateTypedd(ifr)>>>" +getTbBrcnhPriRateTypedd(ifr));
    	logger.info("tbBrnchPriRtPersonal>>>" +tbBrnchPriRtPersonal);
    	logger.info("Principal Amoun>>>" +tbPrsnlMinPrincipalAmt);
    	logger.info("Principal Amount>>>"+getTbBrnchPriPrncplAmt(ifr));
    	logger.info("check if empty>>>"+(!isEmpty(getTbBrnchPriPrncplAmt(ifr))));
    	if(getTbBrcnhPriRateTypedd(ifr).equalsIgnoreCase(tbBrnchPriRtPersonal)){
    		if(!isEmpty(getTbBrnchPriPrncplAmt(ifr))) {
    			logger.info("getTbBrnchPriPrncplAmt>>>>"+Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)));
    			try {
    				retMsg = (Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)))<tbPrsnlMinPrincipalAmt ? "Principal Amount must be minimum of "+String.valueOf(tbPrsnlMinPrincipalAmt):"";
    			}
    			catch(Exception ex) {retMsg ="Invalid Principal amount";}
    		}
    	}
    	else if(getTbBrcnhPriRateTypedd(ifr).equalsIgnoreCase(tbBrnchPriRtBanKRate)) {
    		if(!isEmpty(getTbBrnchPriPrncplAmt(ifr))) {
    			logger.info("getTbBrnchPriPrncplAmt>>>>"+Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)));
	    		try {
					retMsg = (Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)))<tbBnkMinPrincipalAmt ? "Principal Amount must be minimum of "+String.valueOf(tbBnkMinPrincipalAmt) :"";
				}
				catch(Exception ex) {retMsg ="Invalid Principal amount";}
			}
    	}
    	if(!isEmpty(retMsg))
    		clearFields(ifr,tbBrnchPriPrncplAmt);
   
    	return retMsg;
    }
    
    /*
     * generate customer unique ref for bid
     */
    private String tbOnDone(IFormReference ifr) {
    	logger.info("tbOnDone>>");
    	String retMsg="";
    	//if(getTbDecision(ifr).equalsIgnoreCase(decSubmit)) {
    	 if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid) ){//generate customer unique ref
    			setTbBrnchCustPriRefNo(ifr,tbGenerateCustRefNo(ifr));
	    	}
    	}
    	
    	//logger.info("Validate retMsg>>"+retMsg);
    	return retMsg;
    }
    
    //**********************Treasury Ends here **********************//

}
