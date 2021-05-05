package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.controller.CpController;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.DbConnect;
import com.newgen.utils.GenerateDocument;
import com.newgen.utils.LogGen;
import com.newgen.utils.Query;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BranchVerifier extends Commons implements IFormServerEventHandler , CommonsI {
    private static final Logger logger = LogGen.getLoggerInstance(BranchVerifier.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
    	logger.info("3993");
        clearDecHisFlag(ifr);
        logger.info("44");
        if (!isEmpty(getProcess(ifr))) {
        	logger.info("111");
        	showSelectedProcessSheet(ifr);
        }
        if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
        	logger.info("122");
        	tbFormLoadActivity(ifr);
        }
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
                	switch(control) {
	                	case cpSmInvestEvent:{
	                        if (cpCheckWindowStateById(ifr, getCpSmWinRefNoBr(ifr))) return setupCpSmBid(ifr);
	                        else return cpValidateWindowErrorMsg;
	                    }
	                	case tbLienCustFaceValue:{
	                		return tbLienCustFaceValue(ifr);
	                	}
	                	 //****************Treasurry Starts here *********************//
                        case tbValidateCustomer:{
                        	return tbValidateCustomer(ifr);
                            
                        }
                       
                        
                        //****************Treasurry Ends here *********************//
                	
                        case generateTemplateEvent:{
                            return GenerateDocument.generateDoc(ifr,data);
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
                        case tbOndone:{
                        	return tbOndone(ifr);
                        }
                        case tbTokenChange:{
	                		tbTokenChange(ifr);
	                	}
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
                else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien) || getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeRemoveLien)){}
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
        //disableTbSections(ifr);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{tbRemarkstbx,tbDecisiondd}); 
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
            if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)) {
            	setVisible(ifr, new String[] {tbMarketSection,tbCategorydd,tbBrnchCusotmerDetails,tbBranchPriSection,
            			tbDecisionSection,tbFetchMandatebtn,tbLienPrincipalbtn,tb_BrnchPri_LienID});
            	disableFields(ifr, new String[] {tbMarketSection,tbCustAcctNo,tbCustAcctLienStatus,tbBranchPriSection});
            	setDecision(ifr,tbDecisiondd,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
            	setMandatory(ifr, new String[] {tbRemarkstbx,tbDecisiondd});//setInvisible(ifr, new String[]{});
              //  disableFields(ifr, new String[] {});
                enableFields(ifr,new String[] {tbDecisionSection,tbLienPrincipalbtn,tbValidatebtn});
               
            }
        } 
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)) {
        	if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)) {
	        	setVisible(ifr, new String[] {tbMarketSection,tbCategorydd,tbBrnchCusotmerDetails,tbBranchSecSection,
	        			tbDecisionSection,tbFetchMandatebtn,tbLienPrincipalbtn,tb_BrnchPri_LienID,tbPostSection});
	        	disableFields(ifr, new String[] {tbMarketSection,tbCustAcctNo,tbCustAcctLienStatus,tbBranchSecSection});
	        	setDecision(ifr,tbDecisiondd,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
	        	setMandatory(ifr, new String[] {tbRemarkstbx,tbDecisiondd});//setInvisible(ifr, new String[]{});
	          //  disableFields(ifr, new String[] {});
	            enableFields(ifr,new String[] {tbDecisionSection,tbLienPrincipalbtn,tbValidatebtn});
	        	
	        	setVisible(ifr, new String[] {tbBrnchCusotmerDetails,tbBranchSecSection,tbDecisionSection});
	    		disableFields(ifr,new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
	    		setMandatory(ifr, new String[] {tbSmBidAmount,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});	
	    		setFields(ifr,new String[] {tbSmIntrestAtMaturity,tbSmPrincipalAtMaturity}, new String[] {getSmInterestAtMat(ifr),tbGetSmPrincipalAtMat(ifr)});
        	 }
        }
       
        
    }
    
    /*
     * calculate Interest for maturity drate  in Non Leap year 
     * {(Principal * Tenor *concessionary rate)/365 *100)
     */
    private String getSmInterestAtMat(IFormReference ifr){
    	double bidamt =convertStringToDouble(getTbSmBidAmount(ifr));
    	double tenor = convertStringToDouble(getTbSmtenor(ifr));
    	double csrate = convertStringToDouble(getTbSmConcessionValue(ifr));
    	double rate = convertStringToDouble(getTbSmConcessionValue(ifr));
    	String maturityDte = getTbSmMaturityDte(ifr);
    	
    	double interest = tbCalcSmInterestAtMaturity( ifr, maturityDte, bidamt,  tenor,  csrate);
    	logger.info("interest at maturity >>>"+interest);
    	return String.valueOf(interest);
    }
    private String tbGetSmPrincipalAtMat(IFormReference ifr){
    	double bidamt =convertStringToDouble(getTbSmBidAmount(ifr));
    	double tenor = convertStringToDouble(getTbSmtenor(ifr));
    	double csrate = convertStringToDouble(getTbSmConcessionValue(ifr));
    	double rate = convertStringToDouble(getTbSmRate(ifr));
    	String maturityDte = getTbSmMaturityDte(ifr);
    	
    	double smprincipal = tbCalcSmPrincipalAtMaturity( ifr,  maturityDte,  bidamt,  tenor,  csrate, rate);
    	logger.info("Principal at maturity >>>"+smprincipal);
    	return String.valueOf(smprincipal);
    	
    }
    
    private void tbTokenChange(IFormReference ifr){
    	if(!isEmpty(getTbtoken(ifr))) {
    		setVisible(ifr,tbPostSection);
    	}
    }
    /*
     * 
     */
    private String tbPost(IFormReference ifr){
    	if(isTbWinValid(ifr)){
    		//post
    		if(getPostStatus(ifr).equalsIgnoreCase(tbSuccess)) {
        		setTbDecisiondd(ifr,decApprove);
        		disableFields(ifr,new String[] {tbDecisiondd});
        	}	
    	}
    	else { //window is closed
    		return "Cutoff time for window has elapsed";
		}
    	return "";
    }
    //check if docs are uploaded
    private String tbOndone(IFormReference ifr) {
    	return isTbDocUploaded(ifr,getWorkItemNumber(ifr),customers_instruction) ?"Kindly attach customers_instruction ":"";
  	}
    /*
     * Decision will automatically be populated as �approve� 
     * when posting status is successful
     */
    private String tbLienCustFaceValue(IFormReference ifr) {
    	setTbDecisiondd(ifr,decApprove);
    	disableFields(ifr, new String[] {tbLienPrincipalbtn,tbDecisiondd});
    	undoMandatory(ifr,tbRemarkstbx);
    	setTb_BrnchPri_LienID(ifr,"L234");
    	return "Customer Principal liened Succesfully";
  	}
    
   
    

    
    /***************************Treaury ends here ************************/
}
