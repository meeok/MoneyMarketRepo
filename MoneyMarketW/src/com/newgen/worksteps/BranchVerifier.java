package com.newgen.worksteps;

import com.newgen.api.customService.CpServiceHandler;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.controller.TbApiController;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.DbConnect;
import com.newgen.utils.GenerateDocument;
import com.newgen.utils.LogGen;
import com.newgen.utils.Query;

import com.newgen.utils.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BranchVerifier extends Commons implements IFormServerEventHandler , CommonsI {
    private static final Logger logger = LogGen.getLoggerInstance(BranchVerifier.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        clearDecHisFlag(ifr);
        if (!isEmpty(getProcess(ifr))) {
        	showSelectedProcessSheet(ifr);
        }
        if (isCpProcess(ifr)) cpFormLoadActivity(ifr);
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
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
                        case cpTokenEvent: return new CpServiceHandler(ifr).validateTokenTest();
                        case cpPostEvent:{
                            if (cpCheckWindowStateById(ifr, getCpWinRefId(ifr))) return new CpServiceHandler(ifr).postTransactionTest();
                            else return cpValidateWindowErrorMsg;
                        }
                    }
                }
                case formLoad:
                case onLoad:
                case onClick:{
                	switch(control) {
	                	case cpSmInvestEvent:{
	                        if (cpCheckWindowStateById(ifr, getCpWinRefId(ifr))) return setupCpSmBid(ifr);
	                        else return cpValidateWindowErrorMsg;
	                    }
	                	case tbLienCustFaceValue:{
	                		//check if lien has been placed on account
	                		/*if(getTbCustAcctLienSatatus(ifr).equalsIgnoreCase(yes))
	                			return "There is already a Lien on this account. Kindly remove before placing another lien";
	                		else*/
	                			return  new TbApiController(ifr).placeLien();
	                	}
	                	 //****************Treasurry Starts here *********************//
                        case tbValidateCustomer:{
                        	return new TbApiController(ifr).fetchAcctDetails();
                        }
                        case tbPostFaceValue:{
                        	return tbPost(ifr);  
                        }
                        
                        //****************Treasurry Ends here *********************//
                	
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
                        case cpSmReverseInvestmentEvent:{
                            if (isCpDecisionReject(ifr) || isCpDecisionReturn(ifr)) cpSmReverseInvestment(ifr);
                        }
                        break;
                        case validateWindowEvent:{
                            if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                                    if (cpCheckWindowStateById(ifr, getCpWinRefId(ifr)))
                                        if (isCpPrimaryMarket(ifr)) setupCpPmBid(ifr);
                                    else return cpValidateWindowErrorMsg;
                            }
                        }
                        break;
                        case tbOnDone:{
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
                        message = "We wish to inform you that your primary market  commercial paper  bid of " + getCpCustomerId(ifr) + " has been debited for the principal sum. <br>" +
                                " Thank you for choosing FirstBank.<br> ";
                        new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                    }
                }
                else if (getCpDecision(ifr).equalsIgnoreCase(decReturn)){
                        message = "A request for "+getCpMarket(ifr)+" Market Commercial paper with unique reference number "+getCpCustomerId(ifr)+" has been rejected by Money_Market_Branch_Verifier due to "+getCpRemarks(ifr)+". ";
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
        clearCpFlag(ifr);
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
                cpSmShowInvestment(ifr);
                setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
                setVisible(ifr,new String[]{cpSmTenorLocal,cpSmRateLocal,cpWindowDetailsSection,cpCustomerIdLocal,cpBranchSecSection,cpCustomerDetailsSection,cpDecisionSection,landMsgLabelLocal,
                        cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal, (getCpSmConcessionRateValue(ifr).equalsIgnoreCase(empty)) ? empty : cpSmConcessionRateValueLocal});
                setInvisible(ifr, new String[]{cpAcctValidateBtn,cpApplyBtn});
                disableFields(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal, cpSmInvestmentTypeLocal});
                setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
                if (getCpSmConcessionRate(ifr).equalsIgnoreCase(no)){
                    setVisible(ifr,new String[]{cpCustomerIncomeSection,cpChargesSection,cpPostSection,cpCommissionLocal,cpTxnFeeLocal,cpVatLocal});
                    enableFields(ifr,new String[]{cpTokenLocal});
                    setMandatory(ifr,new String[]{cpTokenLocal});
                    setCpSmCustomerIncome(ifr);
                    setFields(ifr,new String[]{cpCommissionLocal,cpTxnFeeLocal,cpVatLocal,cpCustodyFeeLocal},new String[]{LoadProp.commission,LoadProp.txnFee,LoadProp.vat,LoadProp.custodyFee});
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

        if (getPbFlag(ifr).equalsIgnoreCase(flag)) {
            new DbConnect(ifr, Query.getSetupCpPmBidQuery(
                    getCurrentDateTime(), getCpCustomerId(ifr), getCpWinRefId(ifr), getWorkItemNumber(ifr), commercialProcessName, getCpMarket(ifr), getFieldValue(ifr, cpCustomerAcctNoLocal), getFieldValue(ifr, cpCustomerNameLocal), getFieldValue(ifr, cpCustomerEmailLocal), String.valueOf(getCpPmCustomerPrincipal(ifr)), getFieldValue(ifr, cpPmTenorLocal), getCpPmRateType(ifr), rate, getCpPmInvestmentType(ifr),
                    getCpPmIssuerName(ifr), getCpCustodyFee(ifr), getCpPbBeneName(ifr), getCpPbBeneAcctNo(ifr))).saveQuery();
        }
        else {
            new DbConnect(ifr, Query.getSetupCpPmBidQuery(
                    getCurrentDateTime(), getCpCustomerId(ifr), getCpWinRefId(ifr), getWorkItemNumber(ifr), commercialProcessName, getCpMarket(ifr), getFieldValue(ifr, cpCustomerAcctNoLocal), getFieldValue(ifr, cpCustomerNameLocal), getFieldValue(ifr, cpCustomerEmailLocal), String.valueOf(getCpPmCustomerPrincipal(ifr)), getFieldValue(ifr, cpPmTenorLocal), getCpPmRateType(ifr), rate, getCpPmInvestmentType(ifr),
                    getCpPmIssuerName(ifr), getCpCustodyFee(ifr))).saveQuery();
        }
    }
    private void cpCheckDecision(IFormReference ifr){
        String decision = getCpDecision(ifr);

        if (decision.equalsIgnoreCase(decReturn)){
            disableFields(ifr,new String[]{cpTokenLocal,cpPostBtn,cpInvestBtn});
            undoMandatory(ifr,new String[]{cpTokenLocal,cpPostBtn});
        }
        else {
            enableFields(ifr,new String[]{cpTokenLocal,cpPostBtn,cpInvestBtn});
            setMandatory(ifr,new String[]{cpTokenLocal,cpPostBtn});
        }
    }
    private void cpSmReverseInvestment(IFormReference ifr){
        if (isCpFlagNotSet(ifr)) {
            new DbConnect(ifr, Query.getCpSmUpdateRefundInvestment(getCpSmInvestmentId(ifr), getCpSmPrincipalBr(ifr))).saveQuery();
            setCpFlag(ifr);
        }
    }
    private void cpSmShowInvestment(IFormReference ifr){
        clearTables(ifr,cpSmInvestmentBrTbl);
        resultSet = new DbConnect(ifr,Query.getCpSmInvestmentQuery(getCpSmInvestmentId(ifr))).getData();
        for (List<String> result : resultSet){
            String id = result.get(0);
            String corporateName = result.get(1);
            String description = result.get(2);
            String maturityDate = result.get(3);
            String dtm = result.get(4);
            String status = result.get(5);
            String availableAmount = result.get(6);
            String rate = result.get(7);
            String amountSold = result.get(8);
            String mandates = result.get(9);

            setTableGridData(ifr,cpSmInvestmentBrTbl,new String[]{cpSmBidInvestmentIdCol,cpSmBidIssuerCol,cpSmBidDescCol,cpSmBidMaturityDateCol,cpSmBidDtmCol,cpSmBidStatusCol,cpSmBidAvailableAmountCol,cpSmBidRateCol,cpSmBidAmountSoldCol,cpSmBidMandatesCol},
                    new String[]{id,corporateName,description,maturityDate,dtm,status,availableAmount,rate,amountSold,mandates});

        }
    }
    
    /*********************************  Treasury Starts here ****************/

    private void tbFormLoadActivity(IFormReference ifr){
    	hideTbSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        //disableTbSections(ifr);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{tbRemarkstbx,tbDecisiondd}); 
        setVisible(ifr, new String[] {tbMarketSection,tbCategorydd,tbDecisionSection});
        setMandatory(ifr, new String[] {tbRemarkstbx,tbDecisiondd});
        enableFields(ifr, new String[] {tbDecisionSection});
    	setDecision(ifr,tbDecisiondd,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});

        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
            if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)) {
            	setVisible(ifr, new String[] {tbCustodyFeeSection,tbBrnchCusotmerDetails,tbBranchPriSection,tbFetchMandatebtn,tbLienPrincipalbtn,tb_BrnchPri_LienID,tbAssigndd,tbBrcnhPriPersonalRate});
            	disableFields(ifr, new String[] {tbCustodyFeeSection,tbMarketSection,tbCustAcctNo,tbCustAcctLienStatus,tbBranchPriSection,tbCustPrincipalAmount});
            	setInvisible(ifr, new String[]{});
            	hideFields(ifr, new String[] {tbBidRequestDte,tbBidRStatus});
                enableFields(ifr,new String[] {tbDecisionSection,tbLienPrincipalbtn,tbValidatebtn});
                
                //set private banking control
                setTbPBApprovalCntrls(ifr);
            }
            //populate table with the customer details ...
            else  if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryMandate)) { 
            	if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(terminationVal)) {
            	}
            	else if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(tbMandateTypeLien)) {
            		setVisible(ifr, new String[] {tbLienSection});
            		disableFields(ifr, new String[] {tbLienSection});
            	}
            	/*
            	 * if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(proofofinvestmentVal)) {
                        		return tbGetCustDetailsForPoi(ifr);
                        	}
                        	else if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(terminationVal)) {
                        		return tbGetCustDetailsForTermination(ifr);
                        	}
            	 */
            	
            }
        } 
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)) {
        	if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)) {
	        	setVisible(ifr, new String[] {tbMarketSection,tbCategorydd,tbBranchSecSection,
	        			tbDecisionSection,tbFetchMandatebtn,tbLienPrincipalbtn,tb_BrnchPri_LienID,tbPostSection,tbBrnchCusotmerDetails});//tbBrnchCusotmerDetails
	        	disableFields(ifr, new String[] {tbMarketSection,tbCustAcctNo,tbCustAcctLienStatus,tbBranchSecSection});
	        	setDecision(ifr,tbDecisiondd,new String[]{decApprove,decReturnLabel}, new String[]{decApprove,decReturn});
	        	setMandatory(ifr, new String[] {tbRemarkstbx,tbDecisiondd});//setInvisible(ifr, new String[]{});
	          //  disableFields(ifr, new String[] {});
	            enableFields(ifr,new String[] {tbDecisionSection,tbLienPrincipalbtn,tbValidatebtn});
	        	
	        	setVisible(ifr, new String[] {tbBranchSecSection,tbDecisionSection});//tbBrnchCusotmerDetails
	    		disableFields(ifr,new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
	    		setMandatory(ifr, new String[] {tbSmBidAmount,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});	
	    		setFields(ifr,new String[] {tbSmIntrestAtMaturity,tbSmPrincipalAtMaturity}, new String[] {getSmInterestAtMat(ifr),tbGetSmPrincipalAtMat(ifr)});
	    		
	    		//display bid details
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
   
    private String tbPost(IFormReference ifr){
    	if(isTbWinValid(ifr)){
    		//post
    		 * 
    		if(getPostStatus(ifr).equalsIgnoreCase(tbSuccess)) {
        		setTbDecisiondd(ifr,decApprove);
        		disableFields(ifr,new String[] {tbDecisiondd});
        	}	
    	}
    	else { //window is closed
    		return "Cutoff time for window has elapsed";
		}
    	return "";
    }  */
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
    	setTb_BrnchPri_LienID(ifr,"01784");
    	setFields(ifr,tbCustAcctLienStatus,"Yes");
    	return "Customer Principal liened Succesfully";
  	}
    
   
    

    
    /***************************Treaury ends here ************************/
}
