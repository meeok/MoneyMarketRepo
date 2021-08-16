package com.newgen.worksteps;

import com.newgen.api.customService.CpServiceHandler;
import com.newgen.controller.CpController;
import com.newgen.controller.OmoApiController;
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

public class TreasuryOpsVerifier extends Commons implements IFormServerEventHandler, Constants, CommonsI {
    private static final Logger logger = LogGen.getLoggerInstance(TreasuryOpsVerifier.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        clearDecHisFlag(ifr);
        if (!isEmpty(getProcess(ifr))) showSelectedProcessSheet(ifr);
        if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
        else if (getProcess(ifr).equalsIgnoreCase(omoProcess)) omoFormLoadActivity(ifr);

        //else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbFormLoadActivity(ifr);

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
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                               return new CpServiceHandler(ifr).postTransactionTest();
                            }
                            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                                if (cpCheckWindowStateById(ifr, getCpSmWinRefNoBr(ifr))) return new CpServiceHandler(ifr).postTransactionTest();
                                else return cpValidateWindowErrorMsg;
                            }
                        }
                    }
                }
                case formLoad:
                case onLoad:
                case onClick:{
                    switch (control){
	                    case omoPostSettlementValue : {
	                    	return omoPostSettlementValue(ifr);
	                    }
                    }

                    
                }
                case onChange:{
                	switch(control) {
	                	case omoValidateToken:{
	                		logger.info("token>>>>"+getFieldValue(ifr,omoToken));
	                		return new OmoApiController(ifr).tokenValidation(getFieldValue(ifr,omoToken));
	                	}
	                	case omoDecChange:{
	                		omoDecChangeActivity(ifr);
	                	}
	                		
	                	
                	}
                }
                case custom:
                case onDone:{
                    switch (control){

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
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerVerifier) || getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
            if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                    message = "A request for Commercial Paper with number "+getCpTermCusId(ifr)+"  was not approved by Money_Market_TreasuryOps_Verifier  and discarded. Workitem No. "+getWorkItemNumber(ifr)+".";
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

        if (getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
          if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
              setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              enableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              setVisible(ifr,new String[]{cpMandateTypeSection,cpTerminationDetailsSection,cpPostSection,cpDecisionSection,cpTerminationSection,cpTermMandateTbl,cpTermSpecialRateLocal, getCpTermIsSpecialRate(ifr) ? cpTermSpecialRateValueLocal : cpTermSpecialRateLocal});
              if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)){
                  setVisible(ifr,new String[]{cpTermAmountDueLocal,cpTermAdjustedPrincipalLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermPartialOptionLocal});
              }
              setTerminationDetails(ifr);
          }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReject});
    }

    private void setTerminationDetails(IFormReference ifr){
        resultSet = new DbConnect(ifr,Query.getCpTermDetailsQuery(getCpTermCusId(ifr))).getData();
        logger.info("resultSet-- "+resultSet);
        double penaltyCharge;

        if (!isEmpty(resultSet)){
            String tenor = resultSet.get(0).get(0);
            logger.info("tenor-- "+tenor);
            String maturityDate = resultSet.get(0).get(1);
            logger.info("maturityDate-- "+maturityDate);
            String principal = resultSet.get(0).get(2);
            logger.info("principal-- "+principal);
            long daysDue = getDaysBetweenTwoDates(getCpTermBoDate(ifr),maturityDate);
            logger.info("daysDue-- "+daysDue);

            if (isLeapYear())
                penaltyCharge = Double.parseDouble(principal) *  getPercentageValue(getCpTermRate(ifr)) * ((double) daysDue / 365 + (double) daysDue / 366);
            else
                penaltyCharge = Double.parseDouble(principal) * getPercentageValue(getCpTermRate(ifr)) * ((double) daysDue / 366);

            logger.info("penaltyCharge-- "+penaltyCharge);

            setFields(ifr,new String[]{cpTermTenorLocal,cpTermMaturityDateLocal,cpTermPenaltyChargeLocal,cpTermNoDaysDueLocal},new String[]{tenor,maturityDate,String.valueOf(penaltyCharge),String.valueOf(daysDue)});
        }
    }
    
  //*************** OMO AUCTION START *************************/
  	private void omoFormLoadActivity(IFormReference ifr) {
		setGenDetails(ifr);
    	hideOmoSections(ifr);
    	hideFields(ifr, new String[] {goBackDashboardSection});
    	setDropDown(ifr,omoDecisiondd,new String[]{decApprove,decReject});
    	clearFields(ifr,new String[]{omoRemarkstbx});
    	if(getOmoCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)) {
        	setVisible(ifr,new String [] {omoDecisionSection,omoMarketSection,omoCustDetailsSection});
        	disableFields(ifr, new String[]{omoMarketSection,omoCustDetailsSection});
        	setVisible(ifr, new String[]{omoMarketSection, omoFetchMandate,omoDecisionSection});
        	enableField(ifr,omoFetchMandate);
        	setMandatory(ifr,new String [] {omoDecisiondd});
        	hideFields(ifr,new String[] {omoBankName,omoPostBtn,omoTranId});
		}
		else if(getOmoCategorydd(ifr).equalsIgnoreCase(tbCategoryMandate)) {
		}
    	
	}

	private void omoDecChangeActivity(IFormReference ifr) {
		if(getOmoDecision(ifr).equalsIgnoreCase(decApprove)) {
			setVisible(ifr, new String[]{omoPostSection});
			setMandatory(ifr,new String [] {omoToken});
		}
		else {
			hideFields(ifr, new String[]{omoPostSection});
		}
		omoDecChange(ifr);
	}
  	
  	 //post
  	private String omoPostSettlementValue(IFormReference ifr) throws Exception{
  		//verify token  --todo
  		//search txn  --todo
  		String retMsg ="";
  		String creditAcct ="";
  		String creditSol ="";
  		
  		Properties prop = new Properties();
		InputStream is = new FileInputStream(tbConfigfileName);
		prop.load(is);
		
		creditAcct = prop.getProperty("tbHOSuspenceAct");
		logger.info("Creditaccount>>"+creditAcct);
		creditSol = prop.getProperty("actSol");
		logger.info("credit sol>>"+creditSol);
		
  		String debitAcct = getOmoCustAcctNo(ifr);
  		logger.info("debit sol>>"+debitAcct);
  		String debitSol = getOmoCustSolid(ifr);
  		logger.info("debit sol>>"+debitSol);
  		String amount = getOmoSettlementValue(ifr);
  		logger.info("amount>>"+amount);
  		String transParticulars="NTB"+getOmoCustRefId(ifr);;
  		String partTranRemarks ="TB/"+ getWorkItemNumber(ifr).toUpperCase()+"/SettlementValue";
  		logger.info((isEmpty(debitAcct)||isEmpty(debitSol) || isEmpty(creditAcct)||isEmpty(creditSol)));
  		if(!(isEmpty(debitAcct)||isEmpty(debitSol) || isEmpty(creditAcct)||isEmpty(creditSol))) {
  			retMsg = new OmoApiController(ifr).getPostTxn(debitAcct, debitSol, amount, transParticulars, partTranRemarks, creditAcct, creditSol);
  	  		if(retMsg.substring(0, retMsg.indexOf(":")).equalsIgnoreCase(apiSuccess)) {
  	  			String tranid = retMsg.substring(retMsg.indexOf(":"))+1;
  	  			logger.info("trandid >>>"+tranid);
  	  			setFields(ifr,omoTranId,tranid);
  	      		disableFields(ifr,new String[] {omoDecisiondd,omoPostBtn});
  	      		
  	      		//post interest
  	      		if(getFieldValue(ifr,omoInterestAtMat).equalsIgnoreCase(yes)) {
  	      			//credit customer with residual amount and deduct from interest ...
  	      			
  	      			
  	      		}
  	  		}
  		}
  		else {
  			retMsg="An account is empty";
  			logger.info(retMsg);
  		}
  		
      	return retMsg;
  	}
  	 //*************** OMO AUCTION End *************************/

}
