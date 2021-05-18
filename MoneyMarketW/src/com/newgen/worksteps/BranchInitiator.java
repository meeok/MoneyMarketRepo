package com.newgen.worksteps;

import com.newgen.controller.CpController;
import com.newgen.utils.*;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BranchInitiator extends Commons implements IFormServerEventHandler, CommonsI {
     private static final Logger logger = LogGen.getLoggerInstance(BranchInitiator.class);
     Query query = new Query();

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
                            else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess))
                                tbBackToDashboard(ifr);
                            clearFields(ifr,new String[] {selectProcessLocal});
                            break;
                        }
                        case cpSmApplyEvent:{cpSmInvestmentApply(ifr,Integer.parseInt(data));}
                        break;
                        case  cpSearchTermMandateEvent: {
                            cpFetchMandatesForTermination(ifr,getCpMarket(ifr));
                            break;
                        }
                        case cpSelectTermMandateEvent:{
                            return cpSelectMandateForTermination(ifr,Integer.parseInt(data));
                        }
                        case cpSelectTermSpecialRateEvent:{
                            cpSelectTermSpecialRate(ifr);
                            break;
                        }

                        case  cpCalculateTermEvent:{
                            cpCalculateTermination(ifr);
                            break;
                        }
                        case generateTemplateEvent:{
                            return GenerateDocument.generateDoc(ifr,data);
                        }
                        case cpPoiSearchEvent:{
                            return cpSearchPoi(ifr);
                        }
                        case cpPoiProcessEvent:{
                            return cpPoiProcess(ifr,Integer.parseInt(data));
                        }
                        //****************Treasurry Starts here *********************//
                        case tbValidateCustomer:{
                        	return tbValidateCustomer(ifr);
                        }
                        case tbSmApplyBid:{
                        	try {
                        		int selectedrow = Integer.parseInt(data);
                        		logger.info("selectedrow>>"+selectedrow);
                        		tbSmApplyBid(ifr,selectedrow);
                        		return"";
                        	}
                        	catch(Exception ex) {
                        		logger.info("tbSmApplyBid Exception>>"+ex.toString());
                        		return "Updates failed for row Number :"+data;
                        	}
                        
                        }
                        case tbConcesionaryRateClicked:{
                        	tbConcesionaryRateClicked(ifr);
                        }
                        case tbGetCustInvestmentDetails:{
                        	if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(proofofinvestmentVal)) {
                        		return tbGetCustDetailsForPoi(ifr);
                        	}
                        	else if(getFieldValue(ifr,tbMandateTypedd).equalsIgnoreCase(terminationVal)) {
                        		return tbGetCustDetailsForTermination(ifr);
                        	}
                        	
                        }
                        case tbPopulatePOIFields:{
                        	try {
                        		int selectedrow = Integer.parseInt(data);
                        		logger.info("selectedrow>>"+selectedrow);
                        		tbPopulatePOIFields(ifr,selectedrow);
                        		return"";
                        	}
                        	catch(Exception ex) {
                        		logger.info("tbSmApplyBid Exception>>"+ex.toString());
                        		return "Cannot fetch details for row Number :"+data;
                        	}
                        	
                        }
                        case tbTerminate:{
                        	try {
                        		int selectedrow = Integer.parseInt(data);
                        		logger.info("selectedrow>>"+selectedrow);
                        		return tbGetTermDetails(ifr,selectedrow);
                        	}
                        	catch(Exception ex) {
                        		logger.info("tbSmApplyBid Exception>>"+ex.toString());
                        		return "Cannot fetch details for row Number :"+data;
                        	}
                        	
                        }
                        case tbSpecialRateClicked:{
                        	tbSpecialRateClicked(ifr);
                        }
                        break;
                       
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
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket) || getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                                setVisible(ifr,new String[]{cpCategoryLocal});
                                enableFields(ifr,new String[]{cpCategoryLocal});
                                setMandatory(ifr,new String[]{cpCategoryLocal});
                            }
                            else {
                                setInvisible(ifr,new String[]{cpCategoryLocal});
                                undoMandatory(ifr,new String[]{cpCategoryLocal});
                                hideCpSections(ifr);
                            }
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
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                                return cpPmCheckPrincipal(ifr);
                            }
                            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                                return cpSmCheckPrincipal(ifr,Integer.parseInt(data));
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
                        case cpSmConcessionRateEvent:{ cpSmSetConcessionRate(ifr);}
                        break;
                        case cpSmCheckMaturityDateEvent:{return cpSmCheckMaturityDate(ifr,Integer.parseInt(data));}
                        case cpMandateTypeEvent:{
                            cpSelectMandateType(ifr);
                            break;
                        }
                        case cpSelectTermTypeEvent:{
                            cpSelectTerminationType(ifr);
                            break;
                        }
                        case cpLienEvent:{
                          return  cpValidateLienMandate(ifr);
                        }
                      //****************Treasury Starts here *********************//
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
    	                	return tbCategoryddChange(ifr);
    	                }
    	                case tbValidateSmBidAmount:{
    	                	return tbValidateSmBidAmount(ifr);
    	                }
    	                case tbMandateTypeChanged:{
                        	tbMandateTypeChanged(ifr);
                        }
                        break;
    	                case tbTerminateTypeChanged:{
    	                	tbTerminateTypeChanged(ifr);
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
                                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                                    if (cpCheckWindowStateById(ifr, getCpPmWinRefNoBr(ifr)))
                                        setFields(ifr, cpPmCustomerIdLocal, cpGenerateCustomerId(ifr));
                                    else return cpValidateWindowErrorMsg;
                                }
                                else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                                    if (cpCheckWindowStateById(ifr, getCpSmWinRefNoBr(ifr)))
                                         setFields(ifr, cpSmCustIdLocal, cpGenerateCustomerId(ifr));
                                    else return cpValidateWindowErrorMsg;
                                }
                        }
                        
                        //****************Treasurry on Change Starts here *********************//
                        case tbOnDone:{
                        	return tbOnDone(ifr); 
                        }
                       
                        //****************Treasury on Change Starts here *********************//
    	                
                        
                        //****************Treasury on Change Ends here *********************//

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
        setDropDown(ifr,cpCategoryLocal,new String[]{cpCategoryBid,cpCategoryMandate});
    }
   

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decSubmit,decDiscard});
    }

    private String cpSelectCategory(IFormReference ifr) {
        disableField(ifr,cpCategoryLocal);
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
                if (isCpWindowActive(ifr)) {
                    if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                        setVisible(ifr, new String[]{cpBranchPriSection, cpCustomerDetailsSection, landMsgLabelLocal});
                        setMandatory(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal});
                        enableFields(ifr, new String[]{cpCustomerAcctNoLocal, cpPmTenorLocal, cpPmPrincipalLocal, cpPmRateTypeLocal});
                        setDropDown(ifr, cpPmReqTypeLocal, new String[]{cpPmReqFreshLabel}, new String[]{cpPmReqFreshValue});
                        setFields(ifr, new String[]{cpPmReqTypeLocal, cpPmInvestmentTypeLocal}, new String[]{cpPmReqFreshValue, cpPmInvestmentPrincipal});
                        setCpPmWindowDetails(ifr);
                    }
                    else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                        setVisible(ifr,new String[]{cpBranchSecSection,landMsgLabelLocal});
                        setMandatory(ifr,new String[]{cpSmInstructionTypeLocal});
                        setCpSmWindowDetails(ifr);
                        setCpSmInvestmentGrid(ifr);
                    }
                } else return windowInactiveMessage;
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryMandate)){
                setVisible(ifr,cpMandateTypeSection);
                enableField(ifr,cpMandateTypeLocal);
                setMandatory(ifr,cpMandateTypeLocal);
            }
            else {
                hideCpSections(ifr);
            }
        return null;
    }

    private void setCpSmInvestmentGrid(IFormReference ifr){
        logger.info("query-- "+ new Query().getCpSmInvestmentsQuery(commercialProcessName,getCpMarket(ifr)));
        resultSet = new DbConnect(ifr,new Query().getCpSmInvestmentsQuery(commercialProcessName,getCpMarket(ifr))).getData();
        logger.info("resultSet-- "+ resultSet);
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
    private void cpSmInvestmentApply (IFormReference ifr, int rowIndex){
        clearFields(ifr,new String[]{cpCustomerAcctNoLocal,cpCustomerNameLocal,cpCustomerEmailLocal,cpLienStatusLocal,cpSmInvestmentIdLocal,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal,cpSmConcessionRateValueLocal});
        String id = ifr.getTableCellValue(cpSmInvestmentBrTbl,rowIndex,0);
        setFields(ifr, new String[]{cpSmInvestmentIdLocal},new String[]{id});
        setVisible(ifr,new String[]{cpCustomerDetailsSection,cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal});
        setMandatory(ifr,new String[]{cpSmMaturityDateBrLocal,cpSmPrincipalBrLocal,cpSmConcessionRateLocal});
        enableField(ifr,cpCustomerAcctNoLocal);

    }
    private void cpSmSetConcessionRate(IFormReference ifr){
        clearFields(ifr,cpSmConcessionRateValueLocal);
        if (getCpSmConcessionRate(ifr).equalsIgnoreCase(yes)) {
            setVisible(ifr, cpSmConcessionRateValueLocal);
            setMandatory(ifr, cpSmConcessionRateValueLocal);
            enableField(ifr,cpSmConcessionRateValueLocal);
        }
        else{
            setInvisible(ifr, cpSmConcessionRateValueLocal);
            undoMandatory(ifr, cpSmConcessionRateValueLocal);
            disableField(ifr,cpSmConcessionRateValueLocal);
        }
    }
    private String cpPmCheckPrincipal (IFormReference ifr){
        if (getCpPmCustomerPrincipal(ifr) < getCpPmWinPrincipalAmt(ifr)) {
            clearFields(ifr,cpPmPrincipalLocal);
            return minPrincipalErrorMsg;
        }
        return empty;
    }
    private String cpSmCheckPrincipal(IFormReference ifr,int rowIndex){
        String availableAmount = ifr.getTableCellValue(cpSmInvestmentBrTbl,rowIndex,6);
        logger.info("available amount-- "+availableAmount);

        if (Double.parseDouble(getCpSmPrincipalBr(ifr)) < Double.parseDouble(getCpSmWindowMinPrincipal(ifr)) ||
                Double.parseDouble(getCpSmPrincipalBr(ifr)) > Double.parseDouble(availableAmount)) {
            clearFields(ifr,cpSmPrincipalBrLocal);
            return cpSmMinPrincipalErrorMsg;
        }
        return empty;
    }
    private String cpSmCheckMaturityDate(IFormReference ifr ,int rowIndex){
        String maturityDateInvestmentTbl = ifr.getTableCellValue(cpSmInvestmentBrTbl,rowIndex,3).trim();
        String maturityDate = getFieldValue(ifr,cpSmMaturityDateBrLocal);
        if (!isDateEqual(maturityDate,maturityDateInvestmentTbl)) {
            clearFields(ifr,cpSmMaturityDateBrLocal);
            return cpSmMaturityDateErrMsg + " Investment Date: " + maturityDateInvestmentTbl + "";
        }
        return empty;
    }

    private void cpSelectMandateType(IFormReference ifr){
        if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
            setVisible(ifr,new String[]{cpTerminationSection});
            setMandatory(ifr,new String[]{cpTermMandateLocal});
            enableFields(ifr,new String[]{cpTermMandateLocal,cpSearchMandateTermBtn});
            setInvisible(ifr,new String[]{cpProofOfInvestSection,cpLienSection});
        }
        else if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypePoi)){
            setVisible(ifr,new String[]{cpProofOfInvestSection});
            enableFields(ifr,new String[]{cpPoiSearchBtn,cpPoiMandateLocal});
            setMandatory(ifr,new String[]{cpPoiSearchBtn,cpPoiMandateLocal});
            setInvisible(ifr,new String[]{cpTerminationSection,cpLienSection});
        }
        else if ( getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeLien)){
            setVisible(ifr,new String[]{cpLienSection});
            enableFields(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setMandatory(ifr, new String[]{cpLienTypeLocal, cpLienMandateIdLocal});
            setInvisible(ifr,new String[]{cpTerminationSection,cpProofOfInvestSection});
        }
        else { setInvisible(ifr,new String[]{cpTerminationSection,cpProofOfInvestSection,cpLienSection});}
    }
    private void cpFetchMandatesForTermination(IFormReference ifr, String marketType){
        clearTable(ifr,cpTermMandateTbl);
        resultSet = new DbConnect(ifr,Query.getBidForTerminationQuery(commercialProcessName,marketType,getCpMandateToTerminate(ifr))).getData();
        logger.info("result set mandates-- "+ resultSet);
        for (List<String> result : resultSet){
            String date = result.get(0);
            String custId = result.get(1);
            String amount = result.get(2);
            String accountNo = result.get(3);
            String accountName = result.get(4);
            String maturityDate = result.get(5);
            String status = result.get(6);
            String winId = result.get(7);
            String dtm = String.valueOf(getDaysToMaturity(maturityDate));
            setTableGridData(ifr,cpTermMandateTbl,new String[]{cpTermMandateDateCol,cpTermMandateRefNoCol,cpTermMandateAmountCol,cpTermMandateAcctNoCol,cpTermMandateCustNameCol,cpTermMandateDtmCol,cpTermMandateStatusCol,cpTermMandateWinRefCol},
                    new String [] {date,custId,amount,accountNo,accountName,dtm,status,winId});
        }
        setVisible(ifr,new String[]{cpTermMandateTbl,cpSelectMandateTermBtn});
        enableFields(ifr,new String[]{cpSelectMandateTermBtn});
    }
    private String cpSelectMandateForTermination(IFormReference ifr, int rowIndex){
        String custId = ifr.getTableCellValue(cpTermMandateTbl,rowIndex,1);
        String winId = ifr.getTableCellValue(cpTermMandateTbl,rowIndex,7);
        String dtm = ifr.getTableCellValue(cpTermMandateTbl,rowIndex,5);
        String rate;
        String errMsg = "No Re-Discount rate set by Treasury for this bid.Termination cancelled. Contact Treasury Department.";
        setInvisible(ifr, new String[]{cpTerminationTypeLocal});
        undoMandatory(ifr, new String[]{cpTerminationTypeLocal});
        disableFields(ifr, new String[]{cpTerminationTypeLocal});
        clearFields(ifr,new String[]{cpTermCustIdLocal,cpTerminationTypeLocal,cpTermDtmLocal});

        if (isCpLien(ifr,custId))
            return cpLienErrMsg;

        resultSet = new DbConnect(ifr,Query.getCpReDiscountedRateForTermQuery(winId)).getData();

        if (Long.parseLong(dtm) <= 90){
            rate = resultSet.get(0).get(0);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRateLess90Local});
                setFields(ifr, cpReDiscountRateLess90Local,rate);
            }
            else  return errMsg;
        }
        else if (Long.parseLong(dtm) >= 91 && Long.parseLong(dtm) <= 180){
            rate   = resultSet.get(0).get(1);
            if (isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRate91To180Local});
                setFields(ifr, cpReDiscountRate91To180Local,rate );
            }
            else return errMsg;
        }
        else if (Long.parseLong(dtm) >= 181 && Long.parseLong(dtm) <= 270){
            rate = resultSet.get(0).get(2);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRate181To270Local});
                setFields(ifr, cpReDiscountRate181To270Local,rate );
            }
            else return errMsg;
        }
        else if (Long.parseLong(dtm) >= 271 && Long.parseLong(dtm) <= 364){
            rate = resultSet.get(0).get(3);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpReDiscountRateSection, cpReDiscountRate271To364Local});
                setFields(ifr, cpReDiscountRate271To364Local, rate);
            }
            else return errMsg;
        }
        setVisible(ifr, new String[]{cpTerminationTypeLocal});
        setMandatory(ifr, new String[]{cpTerminationTypeLocal});
        enableFields(ifr, new String[]{cpTerminationTypeLocal});
        setFields(ifr,new String[]{cpTermCustIdLocal,cpTermDtmLocal},new String[]{custId,dtm});
        return null;
    }
    private void cpSelectTerminationType (IFormReference ifr){
        if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypeFull)){
            setVisible(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn,cpTermAdjustedPrincipalLocal,cpTermAmountDueLocal});
            setMandatory(ifr, new String[]{cpTermCalculateBtn});
            enableFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn});
        }
        else if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)){
            setVisible(ifr, new String[]{cpTermSpecialRateLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermCalculateBtn,cpTermAdjustedPrincipalLocal,cpTermAmountDueLocal});
            setMandatory(ifr, new String[]{cpTermPartialOptionLocal,cpTermPartialAmountLocal});
            enableFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermCalculateBtn});
        }
        else {
            setInvisible(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermAdjustedPrincipalLocal,cpTermAmountDueLocal});
            undoMandatory(ifr, new String[]{cpTermPartialOptionLocal,cpTermPartialAmountLocal});
            disableFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermCalculateBtn,cpTermPartialOptionLocal,cpTermPartialAmountLocal});
            clearFields(ifr, new String[]{cpTermSpecialRateLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal});
        }
    }
    private void cpSelectTermSpecialRate (IFormReference ifr){
        clearFields(ifr,new String[]{cpTermSpecialRateValueLocal});
        if (getFieldValue(ifr,cpTermSpecialRateLocal).equalsIgnoreCase(True)){
            setVisible(ifr,cpTermSpecialRateValueLocal);
            setMandatory(ifr,cpTermSpecialRateValueLocal);
            enableFields(ifr,cpTermSpecialRateValueLocal);
        }
        else {
            setInvisible(ifr,cpTermSpecialRateValueLocal);
            undoMandatory(ifr,cpTermSpecialRateValueLocal);
            disableFields(ifr,cpTermSpecialRateValueLocal);
        }
    }
    private void cpCalculateTermination(IFormReference ifr){
        try {
            resultSet = new DbConnect(ifr, Query.getCpBidDtlForTerminationQuery(getCpTermCustId(ifr), getCpMarket(ifr))).getData();
            logger.info("details to calc termination-- " + resultSet);
            String maturityDate = resultSet.get(0).get(1).trim();
            logger.info("maturityDate -- " + maturityDate);
            boolean isLeapYear = isLeapYear(maturityDate);
            logger.info("isLeapYear-- " + isLeapYear);
            String reDiscountRate = empty;
            double dtm = Integer.parseInt(getCpTermDtm(ifr));
            logger.info("dtm -- "+ dtm);
            if (getCpTermIsSpecialRate(ifr))
                reDiscountRate = getFieldValue(ifr, cpTermSpecialRateValueLocal);
            else if (dtm <= 90)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRateLess90Local);
            else if (dtm <= 180)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRate91To180Local);
            else if (dtm <= 270)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRate181To270Local);
            else if (dtm <= 364)
                reDiscountRate = getFieldValue(ifr, cpReDiscountRate271To364Local);

            logger.info("reDiscountRate -- "+ reDiscountRate);
            if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypeFull)) {
                String principal = resultSet.get(0).get(0);
                logger.info("principal -- "+ principal);
                double principalValue = Double.parseDouble(principal);
                double reDiscountRateValue = getPercentageValue(reDiscountRate);
                logger.info("reDiscountRateValue-- "+reDiscountRateValue);
                double amountDue = principalValue - (principalValue * reDiscountRateValue * (dtm / 365));
                logger.info("amountDue-- "+amountDue);
                if (isLeapYear) {
                    amountDue = amountDue + (principalValue * reDiscountRateValue * (dtm / 366));
                    logger.info("LeapYear amountDue-- "+amountDue);
                }
                setFields(ifr, new String[]{cpTermAmountDueLocal}, new String[]{String.valueOf(amountDue)});

            } else if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)) {
                String principal = getCpTermPartialAmt(ifr);
                logger.info("partial principal-- "+principal);
                double principalValue = Double.parseDouble(principal);
                logger.info("partial principalValue-- "+principalValue);
                double reDiscountRateValue = getPercentageValue(reDiscountRate);
                logger.info("reDiscountRateValue-- "+reDiscountRateValue);

                if (isLeapYear) {
                    double adjustedPrincipal = (principalValue * 366 * 365 * 100 * 100) / ((365 * 366 * 100 * 100) -
                            (reDiscountRateValue * 100 * dtm * 366) + (reDiscountRateValue * 100 * dtm * 365));
                    logger.info("adjustedPrincipal-- "+adjustedPrincipal);
                    double amountDue = adjustedPrincipal - (adjustedPrincipal * reDiscountRateValue * dtm / 365) + (adjustedPrincipal * reDiscountRateValue * dtm / 366);
                    logger.info("amountDue-- "+amountDue);
                    setFields(ifr, new String[]{cpTermAmountDueLocal, cpTermAdjustedPrincipalLocal}, new String[]{String.valueOf(amountDue), String.valueOf(adjustedPrincipal)});
                } else {
                    double adjustedPrincipal = (principalValue * 366 * 100) / ((366 * 100) - (dtm * reDiscountRateValue));
                    logger.info("adjustedPrincipal-- "+adjustedPrincipal);
                    double amountDue = adjustedPrincipal - (adjustedPrincipal * reDiscountRateValue * dtm / 365);
                    logger.info("amountDue-- "+amountDue);
                    setFields(ifr, new String[]{cpTermAmountDueLocal, cpTermAdjustedPrincipalLocal}, new String[]{String.valueOf(amountDue), String.valueOf(adjustedPrincipal)});
                }
            }
            disableFields(ifr,cpTermCalculateBtn);
        }
        catch (Exception e){
            logger.info("Exception occurred-- "+ e.getMessage());
            e.printStackTrace();
        }
    }
    private void cpPartialTermOption(IFormReference ifr){

    }

    private String cpValidateLienMandate(IFormReference ifr){
        if (!doesCpIdExist(ifr,getCpLienMandateId(ifr),getCpMarket(ifr))) {
            clearFields(ifr, cpLienMandateIdLocal);
            return "CP ID does not exist. Check and enter the correct ID.";
        }
        return null;
    }

    private String cpSearchPoi(IFormReference ifr){
        clearTable(ifr,cpPoiTbl);
        resultSet = new DbConnect(ifr,Query.getCpPoiMandateSearchQuery(getCpMarket(ifr),getCpPoiMandate(ifr))).getData();
        if (isEmpty(resultSet)) {
            clearFields(ifr,cpPoiMandateLocal);
            return "No Details found for this Mandate";
        }
        for (List<String> result : resultSet){
            String date = result.get(0);
            String id = result.get(1);
            String amount = result.get(2);
            String accountNo = result.get(3);
            String accountName = result.get(4);
            String status = result.get(5);

            setTableGridData(ifr,cpPoiTbl,new String[]{cpPoiDateCol,cpPoiIdCol,cpPoiAmountCol,cpPoiAcctNoCol,cpPoiAcctNameCol,cpPoiStatusCol},
                    new String[]{date,id,amount,accountNo,accountName,status});
        }
        setVisible(ifr,new String[]{cpPoiGenerateBtn,cpPoiTbl});
        enableFields(ifr,new String[]{cpPoiGenerateBtn});

        return null;
    }

    private String cpPoiProcess (IFormReference ifr, int rowIndex){
        String id = ifr.getTableCellValue(cpPoiTbl,rowIndex,1);
        resultSet = new DbConnect(ifr,Query.getCpPoiDtlQuery(id)).getData();

        if (!isEmpty(resultSet)) {
            String reqDate = resultSet.get(0).get(0);
            String custId = resultSet.get(0).get(1);
            String principal = resultSet.get(0).get(2);
            String accountNo = resultSet.get(0).get(3);
            String accountName = resultSet.get(0).get(4);
            String principalMaturity = resultSet.get(0).get(5);
            String interest = resultSet.get(0).get(6);
            String maturityDate = resultSet.get(0).get(7);
            String tenor = resultSet.get(0).get(8);
            String rate = resultSet.get(0).get(9);

            setFields(ifr, new String[]{cpPoiCustEffectiveDateLocal,cpPoiCustIdLocal,cpPoiCustAmountInvestedLocal,cpPoiCustAcctNoLocal,cpPoiCustNameLocal,cpPoiCustPrincipalAtMaturityLocal,cpPoiCustInterestLocal,cpPoiCustMaturityDateLocal,cpPoiCustTenorLocal,cpPoiCustRateLocal,cpPoiDateLocal},
                    new String[]{reqDate,custId,principal,accountNo,accountName,principalMaturity,interest,maturityDate,tenor,rate,getCurrentDate()});
            return apiSuccess;
        }
        return "Error in processing proof of investment. Contact iBPS support.";
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
        setMandatory(ifr,new String [] {tbMarketTypedd});// {tbCategorydd,tbDecisiondd,tbRemarkstbx});
        setDropDown(ifr,tbCategorydd,new String[]{tbCategoryBid,tbCategoryMandate});
        hideField(ifr,tbMarketUniqueRefId);
        
    }
    /*
     * if bid setup has been done for selected market display corresponding fields
     * hide/clear category dropdown
     */
    private String tbMarketTypeddChange(IFormReference ifr){
    	String retMsg ="";
    	clearFields(ifr, new String[] {tbMandateTypedd,tbCategorydd});
    	hideFields(ifr,new String[] {tbMandateTypedd,});
    	if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		hideFields(ifr,new String[] {tb_SmCustBidRemark,tbBidRStatus,tbBidRequestDte});//-------
    		setTbPriWindownUnqNo(ifr,getTbActiveWindowwithRefid(ifr));
    		setTbMarketUniqueRefId(ifr,getTbActiveWindowwithRefid(ifr));
    		setVisible(ifr, new String[]{tbCategorydd});
    		setMandatory(ifr,new String [] {tbCategorydd});// {tbCategorydd,tbDecisiondd,tbRemarkstbx});
    		if(!isEmpty(getTbPriWindownUnqNo(ifr))){
    			setVisible(ifr, new String[]{tbMarketUniqueRefId});
    			//disableFields(ifr, new String[]{tbMarketUniqueRefId});
    		}
    		else {
    			hideFields(ifr, new String[]{tbMarketUniqueRefId});
    			//clearFields(ifr,tbMarketTypedd);
    			//retMsg = getTbMarket(ifr)+tbWindowInactiveMessage;
    		}
    	}
    	else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
    		setTbBrnchSmWindownUnqNo(ifr,getTbActiveWindowwithRefid(ifr));
    		setTbMarketUniqueRefId(ifr,getTbActiveWindowwithRefid(ifr));
    		setVisible(ifr, new String[]{tbCategorydd});
    		setMandatory(ifr,new String [] {tbCategorydd});
    		if(!isEmpty(getTbMarketUniqueRefId(ifr))){
    			setVisible(ifr, new String[]{tbMarketUniqueRefId});
    			//disableFields(ifr, new String[]{tbMarketSection});
    		}
    		else {
    			
    			hideFields(ifr, new String[]{tbMarketUniqueRefId,tbCategorydd});
    			//clearFields(ifr,tbMarketTypedd);
    			//retMsg = getTbMarket(ifr)+tbWindowInactiveMessage;
    			//hide or disable all fields
    		}
    	}
    	else {
    		clearFields(ifr, new String[]{tbMarketUniqueRefId,tbCategorydd});
    		hideFields(ifr, new String[]{tbCategorydd});
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
    /*
     * Secondary - processes treasury bills requested by a customer
     * Bid - displays ALL open BIDs setup by treasury in order of maturity date(ascending order of date) 
     */
    private String tbCategoryddChange(IFormReference ifr){
    	String retMsg = "";
    	setVisible(ifr, new String[] {tbDecisionSection});
    	hideFields(ifr, new String[] {tbBrnchCusotmerDetails,tbBranchPriSection,tbDecisionSection,tbMandateTypedd});
    	enableFields(ifr, new String[] {tbDecisionSection});
    	undoMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo,tbMandateTypedd});
    	
    	//if rediscount rate is set, get rates from db and show rediscount rate 
    	if(getFieldValue(ifr,tbRediscoutApprovedFlg).equalsIgnoreCase(yesFlag)) {// set
			setVisible(ifr, new String[]{tbRediscountRate});
			tbPorpulateRDRFields(ifr); ////porpulate rediscount rate fields
            disableFields(ifr,new String[]{tbRediscountRate});
		}
		else {//already set - disable rediscount rate field
            hideFields(ifr,new String[]{tbRediscountRate});
		}
    	if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
	    	if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)){		
	    		 if(isTbWindowOpen(ifr,getTbPriWindownUnqNo(ifr))){//check if market is is open
		    		setVisible(ifr, new String[] {tbBrnchCusotmerDetails,tbBranchPriSection,tbDecisionSection});
		    		setMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
		    		setTbBrnchPriRqsttype(ifr,tbBidRqstType);
		    		
		    		//tbGenerateCustRefNo(ifr, getTbMarket(ifr));)
		    	}
		    	else {
		    		setTbBrnchPriRqsttype(ifr,"");
		    		hideFields(ifr, new String[] {tbBrnchCusotmerDetails,tbBranchPriSection,tbDecisionSection});
		    		undoMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
		    		retMsg ="No window is opened";
		    	}
	    	}
	    	else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryMandate)){	
	    		setVisible(ifr, new String[] {tbMandateTypedd,tbDecisionSection});
	    		//disableFields(ifr, new String[] {tbBrnchCusotmerDetails,tbBranchPriSection});
	    		enableFields(ifr,new String[] {tbMandateTypedd,tbDecisionSection});
	    		setMandatory(ifr, new String[] {tbMandateTypedd});
	    		
	    	}
	    	else {
	    		clearFields(ifr, new String[] {tbMandateTypedd});
	    		hideFields(ifr, new String[] {tbMandateTypedd});
	    	}
    	}
    	
    	//secondary market
    	else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
    		if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)){
    			logger.info(isTbWindowOpen(ifr,getTbBrnchSmWindownUnqNo(ifr)));
    			 if(isTbWindowOpen(ifr,getTbBrnchSmWindownUnqNo(ifr))){//check if market is open for bidding
    				 logger.info("market is open");
		    		setVisible(ifr, new String[] {tbBranchSecSection,tbDecisionSection,tbBrnchCusotmerDetails});//
		    		disableFields(ifr,new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
		    		setMandatory(ifr, new String[] {tbSmBidAmount,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
		    		hideFields(ifr, new String[] {tbSmPrincipalAtMaturity,tbSmIntstMaturityNonLpYr,tbSmIntrsyMaturityLpYr,tbSmResidualIntrst});
		    		//get issued bids and insert into the openbid table
		    		retMsg =tbPorpulateSmOpenBidTbl(ifr);
    			 }
		    	else { //window is not open
		    		setTbBrnchPriRqsttype(ifr,"");
		    		hideFields(ifr, new String[] {tbBrnchCusotmerDetails,tbBranchSecSection,tbDecisionSection});
		    		undoMandatory(ifr, new String[] {tbSmBidAmount,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
		
		    	}
    	}
    	}
    	return retMsg;
    	//logger.info("tbOnDone1>>>");
    	//tbOnDone(ifr);
    	//logger.info("tbOnDone2>>>");
    }
    
    /*
     * porpulate rediscount rate Fields
     * 
     */
    private void tbPorpulateRDRFields(IFormReference ifr){
    	String retMsg ="";
		String rdrQry = new Query().getReDiscounteQuery(getTbMarketUniqueRefId(ifr));
    	logger.info("getReDiscounteQuery>>"+ rdrQry);
        List<List<String>> rdrDbr = new DbConnect(ifr, rdrQry).getData();
        logger.info("getReDiscounteQuery save db result>>>"+rdrDbr);
        int dbrSize = rdrDbr.size();
        if(dbrSize>0) {
    		String tb90 = rdrDbr.get(0).get(0);
    		String tb180 = rdrDbr.get(0).get(1);
    		String tb270 = rdrDbr.get(0).get(2);
    		String tb364 = rdrDbr.get(0).get(3);
    		
            setFields(ifr,new String[]{tbRdrlessEqualto90tbx,tbRdr91to180,tbRdr181to270,tbRdr271to364days},
                    new String[]{tb90,tb180,tb270,tb364});
        }
        else
        	logger.info("getReDiscounteQuery returned no fields: no rediscount rate set for this market id: >>>"+getTbMarketUniqueRefId(ifr));	
    }
    
    /*
     * get issued bids and insert into the openbid table
     */
    private String tbPorpulateSmOpenBidTbl(IFormReference ifr){
    	String retMsg ="";
		String qry = new Query().tbGetSmIssuedBidsQuery(getTbBrnchSmWindownUnqNo(ifr));
    	logger.info("tbGetSmIssuedBidsQuery>>"+ qry);
        List<List<String>> dbr = new DbConnect(ifr, qry).getData();
        logger.info("getTbPmBidUpdateBankQuery save db result>>>"+dbr);
        int dbrSize = dbr.size();
        if(dbrSize>0) {
        	for(List<String> ls : dbr)
        	{
        		String MaturityDate = ls.get(0);
        		String Tenor = ls.get(1);
        		String Status = ls.get(2);
        		String TBillAmount = ls.get(3);
        		String tbRate = ls.get(4);
        		String Mandates = ls.get(5);
        		String totalAmountSold = ls.get(6);
        		String insertionorderid = ls.get(7);
        		String DaysToMaturity = String.valueOf(getDaysToMaturity(MaturityDate)); 
        		String AvailableAmount = convertDoubleToString(convertStringToDouble(TBillAmount) - convertStringToDouble(totalAmountSold));
        		
                setTableGridData(ifr,tbSmOpenBidsTbl,new String[]{tbBidMaturityDteCol,tbBidTenorCol,tbStausCol,tbTBillAmountCol,tbBidRateCol,tbMandatesCol,tbAvailableAmountCol,tbSmInvestmentIdCol,tbDaysToMaturityCol},
                        new String[]{MaturityDate,Tenor,Status,TBillAmount,tbRate,Mandates,AvailableAmount,insertionorderid,DaysToMaturity});
        	}
        }
        else
        	retMsg ="There are no open bids";
        return retMsg;
    }
    /*
     * Minimum of N100,000 for bank Rate and Minimum of N50,000,000 for personal Rate
     * Validation for amount should be in thousands and comma used for separation)
     */
    private String tbValidatePrincipalAmt(IFormReference ifr){
    	//setTbBrnchInitRetMsg("");
    	String retMsg ="";
    	if(!isEmpty(getTbBrnchPriPrncplAmt(ifr))) {
    		//check if number is in thousands
    		try {
				long amt = Long.parseLong(getTbBrnchPriPrncplAmt(ifr));
				logger.info("getTbBrnchPriPrncplAmt>>>>"+ amt);
    		if(amt%1000 >0) {
    			retMsg ="Amount must be in thousands";
    		}
    		else if(getTbBrcnhPriRateTypedd(ifr).equalsIgnoreCase(tbBrnchPriRtPersonal)){
    			retMsg = ((amt<tbPrsnlMinPrincipalAmt || amt>tbPrsnlMaxPrincipalAmt)? "Principal Amount must be between "+String.format("%.0f",tbPrsnlMinPrincipalAmt)+" and "+String.format("%.0f",tbPrsnlMaxPrincipalAmt):"");
	    	}
	    	else if(getTbBrcnhPriRateTypedd(ifr).equalsIgnoreCase(tbBrnchPriRtBanKRate)) {
	    		retMsg = (Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)))<tbBnkMinPrincipalAmt ? "Principal Amount must be minimum of "+String.format("%.0f",tbBnkMinPrincipalAmt) :"";
				
				}
    		}
			catch(Exception ex) {
				retMsg ="Invalid Principal amount";
			}
	    	
	    	if(!isEmpty(retMsg))
	    		clearFields(ifr,tbBrnchPriPrncplAmt);
	    	}
   
    	return retMsg;
    }
    
    /*
     * generate customer unique ref for bid
     */
    private String tbOnDone(IFormReference ifr) {
    	logger.info("tbOnDone>>");
    	String retMsg="";
    	int tenor = Integer.parseInt(getFieldValue(ifr,tbBrnchPriTenordd));
    	logger.info("tenor>>" + tenor);
    	//if(getTbDecision(ifr).equalsIgnoreCase(decSubmit)) {
    	 if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid) ){//generate customer unique ref
    			setTbBrnchCustPriRefNo(ifr,tbGenerateCustRefNo(ifr));
    			setFields(ifr,new String[] {tbCustUniquerefId,tbBidRequestDte,tbMaturityDte}, new String[] {getTbBrnchCustPriRefNo(ifr),getCurrentDate(),getMaturityDate(tenor)});
    			logger.info(getTbBrnchCustPriRefNo(ifr)+", "+getCurrentDate()+", "+getMaturityDate(tenor));
    			logger.info(getFieldValue(ifr,tbCustUniquerefId)+", "+getFieldValue(ifr,tbBidRequestDte)+", "+getFieldValue(ifr,tbMaturityDte));
	    	}
    	}
    	 if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
     		if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid) ){//generate customer unique ref and get request date
     			setFields(ifr,new String[] {tbSmCustid,tbBidRequestDte}, new String[] {tbGenerateCustRefNo(ifr),getCurrentDateTime()});
     			
     			String avlamtqry = query.getSmAvailableAmtQuery(getFieldValue(ifr,tbSmInvestmentId));
     			logger.info("getSmAvailableAmtQuery>>>"+avlamtqry);
     			List<List<String>> avlamtdbr = new DbConnect(ifr,avlamtqry).getData();
     			logger.info("getSmAvailableAmtQuery dbesult>>>"+avlamtdbr);
     			if(avlamtdbr.size()>0) {
     				if(!avlamtdbr.get(0).get(0).equalsIgnoreCase(tbSecBidStatusOpen)) {
	     				if(convertStringToDouble(getTbSmBidAmount(ifr)) > convertStringToDouble(avlamtdbr.get(0).get(0))){
	         				clearFields(ifr,tbSmBidAmount);
	         				retMsg = "value cannot be greater than the available amount";
	         			}
	     				else { //update the table with bidamt and mandate
	     					String updateqry = query.updateTbIBMandateAndTAmt(getTbSmInvestmentId(ifr),convertStringToDouble(getTbSmBidAmount(ifr)));
	     	     			logger.info("updateTbIBMandateAndTAmt>>>"+updateqry);
	     	     			List<List<String>> aupdatedbr = new DbConnect(ifr,updateqry).getData();
	     	     			logger.info("updateTbIBMandateAndTAmt dbesult>>>"+aupdatedbr);
	     				}
     				}
     				else
     					retMsg = "Bid Status is close"; //update table----
     			}
     			else 
     				retMsg = "No record exists for this investment id. Contact Admin";
     		}
     	}
    	//logger.info("Validate retMsg>>"+retMsg);
    	 return retMsg;
    }
    /*
     * secondary market - update bid details for customer with issued bids details.
     */
    private void tbSmApplyBid(IFormReference ifr, int rowIndex) {
    	
    	// get minimum principal amount
    	String minPqry = new Query().getSmMinPrincipalQuery(getTbBrnchSmWindownUnqNo(ifr));
    	logger.info("getSmMinPrincipalQuery>>"+ minPqry);
        List<List<String>> minPdbr = new DbConnect(ifr, minPqry).getData();
        logger.info("getSmMinPrincipalQuery get db result>>>"+minPdbr);
        if(minPdbr.size()>0) 
        	setFields(ifr,tbSmMinPriAmt,minPdbr.get(0).get(0));
    	String retMsg ="";
    	String maturityDte = ifr.getTableCellValue(tbSmOpenBidsTbl,rowIndex,0);
    	logger.info("maturityDte>>>" + maturityDte);
    	String tenor = ifr.getTableCellValue(tbSmOpenBidsTbl,rowIndex,1);
    	logger.info("tenor>>>" + tenor);
    	String status = ifr.getTableCellValue(tbSmOpenBidsTbl,rowIndex,3);
    	logger.info("status>>>" + status);
    	String tBillAmt = ifr.getTableCellValue(tbSmOpenBidsTbl,rowIndex,2);
    	logger.info("TBillAmt>>>" + tBillAmt);
    	String rate = ifr.getTableCellValue(tbSmOpenBidsTbl,rowIndex,4);
    	logger.info("rate>>>" + rate);
    	String availableAmount = ifr.getTableCellValue(tbSmOpenBidsTbl,rowIndex,6);
    	logger.info("availableAmount>>>" + availableAmount);
    	String SmInvestmentId = ifr.getTableCellValue(tbSmOpenBidsTbl,rowIndex,7);
    	logger.info("SmInvestmentId>>>" + SmInvestmentId);
    	
    	//change to array
    	setFields(ifr,tbSmtenor,tenor);
    	setFields(ifr,tbSmRate,rate);
    	setFields(ifr,tbSmMaturityDte,maturityDte);
    	setFields(ifr,tbSmInvestmentId,SmInvestmentId);
	}
    
    /*
     * 
     */
	private String tbValidateSmBidAmount(IFormReference ifr) {
		return convertStringToDouble(tbSmBidAmount) < convertStringToDouble(tbSmMinimumPrincipal) ? "BID amount cannot be less than "+tbSmMinimumPrincipal:"";
	
		
	}
	private void tbConcesionaryRateClicked(IFormReference ifr) {
		if(getTbSmConcessionRate(ifr).equalsIgnoreCase(yes)) {
			setVisible(ifr,tbSmConcessionValue);
			enableField(ifr,tbSmConcessionValue);
		}
		else {
			clearFields(ifr,tbSmConcessionValue);
			hideField(ifr,tbSmConcessionValue);
		
		}
	}
	 /*
     * Search customer and populate termination table with customer investments
     */
    private String tbGetCustDetailsForTermination(IFormReference ifr){
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
                 String custid = ls.get(7);
                 String winRefId =ls.get(8);
                 try {
                	 daystoMaturity =  String.valueOf(getDaysToMaturity(maturityDte.substring(0,maturityDte.indexOf(" "))));
                 }
                 catch(Exception ex) {
                	 logger.info("Maturity date may be null contact Admin");
                 }

                 setTableGridData(ifr,tbTerminationMandateTbl,new String[]{tbDateCol,tbRefNoCol,tbAcctNoCol,tbCustNameCol,tbAmountCol,tbDaysToMaturityCol,tbStatusCol,tbMarketWinRefIDCol},
                         new String[]{date,custid,accountNo,accountName,principalamt,daystoMaturity,status,winRefId});
             }
             setVisible(ifr,new String[]{tbTerminationSection,tbPoiGenerateBtn,tbPoiCustDetailsTbl});
             enableFields(ifr,new String[]{tbTerminationSection,tbPoiGenerateBtn});
             
           //if rediscount rate is set, get rates from db and show rediscount rate 
         	if(getFieldValue(ifr,tbRediscoutApprovedFlg).equalsIgnoreCase(yesFlag)) {//get it from setup table or get it from the market id.. query
     			setVisible(ifr, new String[]{tbRediscountRate});
     			tbPorpulateRDRFields(ifr); ////porpulate rediscount rate fields
                 disableFields(ifr,new String[]{tbRediscountRate});
     		}
        }
        else {
        	 hideFields(ifr,new String[]{tbTerminationSection});
        	return "No Details found for this Mandate";
        	
        }
        return "";
    }
	 /*
     * Search customer and populate table with customer investments for POI
     */
    private String tbGetCustDetailsForPoi(IFormReference ifr){
        clearTable(ifr,tbPoiCustDetailsTbl);
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
     * generate proof of investment
     */
    private void tbPopulatePOIFields (IFormReference ifr, int rowIndex){
    	String custRefid = ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,0);
    	String acctno = ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,3);
    	clearFields(ifr,new String[] {tbPoiEffectiveDate,tbPoiCustRefid,tbPoiAmtInvested,tbPoiCustAcctNum,tbPoiActName,
        		tbPoiPrincipalAtMaturity,tbPoiIntrest,tbPoiMaturityDte,tbPoiTenor,tbPoiRate,tbPoiDte});
        String idqry = new Query().getTbCustMandateDetailsQuery(getTbMarket(ifr),custRefid, acctno);
        logger.info("getTbCustMandateDetailsQuery>>>"+idqry);
        List<List<String>> iddbr= new DbConnect(ifr,idqry).getData();
        logger.info("getTbCustMandateDetailsQuery db result>>>"+iddbr);
        if (iddbr.size()>0) {
    	
            String reqDate =ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,0);
            String custId = ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,1);
            String principal = ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,2);
            String accountNo = ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,3);
            String accountName = ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,4);
            String maturityDte  =ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,6);
            String interest = ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,7);
            String principalMaturity =ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,8);
            String tenor =ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,9);
            String rate =ifr.getTableCellValue(tbPoiCustDetailsTbl,rowIndex,10);
            


            String daystoMaturity =  String.valueOf(getDaysToMaturity(maturityDte));
            setFields(ifr, new String[]{tbPoiEffectiveDate,tbPoiCustRefid,tbPoiAmtInvested,tbPoiCustAcctNum,tbPoiActName,
            		tbPoiPrincipalAtMaturity,tbPoiIntrest,tbPoiMaturityDte,tbPoiTenor,tbPoiRate,tbPoiDte},
                    new String[]{reqDate,custId,principal,accountNo,accountName,principalMaturity,interest,maturityDte,
                    		tenor,rate,getCurrentDate()});
        }//
           
    }
    
    //mandate type changed
    private void tbMandateTypeChanged (IFormReference ifr){
    	hideFields(ifr, new String[] {tbProofOfInvestSection,tbTerminationSection});
    	setVisible(ifr, new String[] {tbSearchCustSection});
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
    
   /*
    * full termination calculation
    * Amount due to customer For Face Value Or Full Termination For leap year maturity date	Principal – 
    * (principal x Rediscount Rate x no.of days to maturity/365) + (pricipal x Rediscount Rate x no.of days to maturity/366)
    * Amount due to customer For Face Value Or Full Termination For non leap year maturity date	Principal –
    * (principal x Rediscount Rate x  no.of days to maturity/365)
    */
    private double tbGetCalcFullTermAmtDueCustomer(String maturityDte,double principal,int rediscountRate,int daysToMaturity ){
    	if(isLeapYear(maturityDte))
    		return ((principal * rediscountRate *(daysToMaturity/365)) + (principal * rediscountRate *(daysToMaturity/366)));
    	else
    		return (principal * rediscountRate *(daysToMaturity/365));
    }
    
    /*
     * Where terminate cash amount is selected for leap year maturity date
     * Adjusted Principal for cash amount for leap maturity date	
     * (Cash Amount*366 * 365*100*100) / 
     * {(365*366*100*100)-(Rediscount value*100*no of days to maturity in non-leap year*366)+
     * (Rediscount value*100* no of days to maturity in leap year *365)}	
     * 
     * Where terminate cash amount is selected for non leap year maturity date
     *  (Cash Amount*366 *100) / ((366*100) –(days to maturity * Rediscount value))
     */
    private double tbGetCashAmtAdjustPrncpal(String maturityDte,int daysToMaturity, double rediscountVal,int daysToMatNLyr,int daysToMatLyr,double cashamt){
    	if(isLeapYear(maturityDte))
    		return (cashamt*366 * 365*100*100) / 
    				((365*366*100*100)-(rediscountVal*100*daysToMatNLyr*366)+ (rediscountVal*100* daysToMatLyr *365));	
    	else
    		return(cashamt*366 *100) / 
    				((366*100)-(daysToMaturity *rediscountVal));
    }
    
    /*
     *Amount due customer for partial amount
     * Amount due to customer For Cash amount for leap year maturity date
     * When cash value is selected for leap year maturity date (Cash amount must not be above principal) 
     * Adjusted Principal – (Adjusted principal x Rediscount Rate x no.of days to maturity/365) +
     *  (Adjusted principal x Rediscount Rate x no.of days to maturity/366)	
     *  
     *  Amount due to customer For Cash amount for non leap year maturity date	
     *  When cash value is selected for leap year maturity date (Cash amount must not be above principal)
     *  Adjusted principal – (Adjusted principal x Rediscount Rate x  no.of days to maturity/365)
     */
    private double tbGetPartTermAmtDueCustomer(String maturityDte,int daysToMaturity, double rediscountVal,int daysToMatNLyr,int daysToMatLyr,double cashamt,
    		double rediscountRate, double principal){
    	double adjustedPrncpal =tbGetCashAmtAdjustPrncpal( maturityDte, daysToMaturity,  rediscountVal, daysToMatNLyr, daysToMatLyr, cashamt);
    	
    	if(isLeapYear(maturityDte))
    		return adjustedPrncpal - (adjustedPrncpal * rediscountRate * daysToMaturity/365) + 
    				(adjustedPrncpal * rediscountRate * daysToMaturity/366);	
    	else
    		return  adjustedPrncpal - (adjustedPrncpal * rediscountRate * daysToMaturity/365);
    }
    
    /*
     * 
     */
    private void tbTerminateTypeChanged(IFormReference ifr) {
    	//
		if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(tbTerminationTypeFull)) {
			setVisible(ifr, new String[] {tbTermbtn});
			hideFields(ifr, new String[] {tbTermVal,tbTermCashValue});
			undoMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
		}
		else if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(tbTerminationTypePartial)) {
			setVisible(ifr, new String[] {tbTermCashValue,tbTermbtn});
			setMandatory(ifr, new String[] {tbTermCashValue});
		}
		else {
			hideFields(ifr, new String[] {tbTermVal,tbTermCashValue,tbTermbtn});
			undoMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
			//clear all termfiels
			clearFields(ifr, new String[] {tbTermAdjustedPrncpal,tbTermCashValue,tbTermAmtDueCust,tbTermCashValue});
		}
	}
    /*
     *terminate 
     */
    private String tbTerminate(IFormReference ifr, int rowIdex) {
    	//get customer unique refid
		if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(cpTerminationTypeFull)) {
			setVisible(ifr, new String[] {tbTermbtn});
			hideFields(ifr, new String[] {tbTermVal,tbTermCashValue});
			undoMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
		}
		else if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(cpTerminationTypePartial)) {
			setVisible(ifr, new String[] {tbTermVal,tbTermCashValue,tbTermbtn});
			setMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
		}
		else {
			hideFields(ifr, new String[] {tbTermVal,tbTermCashValue,tbTermbtn});
			undoMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
			//clear all termfiels
			clearFields(ifr, new String[] {tbTermAdjustedPrncpal,tbTermCashValue,tbTermAmtDueCust,tbTermCashValue});
		}
		return null;
	}
    
    //termination button clicked....
	private String tbGetTermDetails(IFormReference ifr,int rowIndex) {
		
		String custRefid = ifr.getTableCellValue(tbTerminationMandateTbl,rowIndex,1);
        String winRefId = ifr.getTableCellValue(tbTerminationMandateTbl,rowIndex,5);
        String daysTomat = ifr.getTableCellValue(tbTerminationMandateTbl,rowIndex,5);
		// porpulate customer term details field
		if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(cpTerminationTypeFull)){
			
			 String idqry = new Query().getTbCustMandateDetailsQuery(getFieldValue(ifr,tbTermCustUniqId),getTbMarket(ifr));
		     logger.info("getTbCustMandateDetailsQuery>>>"+idqry);
		     List<List<String>> iddbr= new DbConnect(ifr,idqry).getData();
		     logger.info("getTbCustMandateDetailsQuery db result>>>"+iddbr);
		     if(iddbr.size()>0) {
		    	 String maturityDte= iddbr.get(0).get(0);
		    	 double principal= convertStringToDouble(iddbr.get(0).get(1));
		    	 int tenor= convertStringToInt(iddbr.get(0).get(2));
		     }
		     //check if re
		   /*  int rediscountRate = convertStringToInt(getFieldValue(ifr,tbTermtypedd));
		     if(iddbr.size()>0) {
		    	 String principal ="";
				
				
				double principal;
				int rediscountRate;,int daysToMaturity ;
				String amtDueCustomer = tbGetCalcFullTermAmtDueCustomer(maturityDte,principal,rediscountRate,daysToMaturity)
				setFieldValue(ifr,)
				setVisible(ifr, new String[] {tbTermbtn});
				hideFields(ifr, new String[] {tbTermVal,tbTermCashValue});
				undoMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
		     }
		}
		else if(getFieldValue(ifr,tbTermtypedd).equalsIgnoreCase(cpTerminationTypePartial)) {
			setVisible(ifr, new String[] {tbTermVal,tbTermCashValue,tbTermbtn});
			setMandatory(ifr, new String[] {tbTermVal,tbTermCashValue});
		}*/
		}
		return null;
	}
	
	private void getCustRediscountRate(String tenor) {
		
	}
	private void tbSpecialRateClicked(IFormReference ifr) {
		if(getFieldValue(ifr,tbSpecialRate).equalsIgnoreCase("true")) {
			setVisible(ifr,tbSpecialRateValue);
			setMandatory(ifr,tbSpecialRateValue);
		}
		else {
			hideField(ifr,tbSpecialRateValue);
			undoMandatory(ifr,tbSpecialRateValue);
		}
			
		
	}

    
    //**********************Treasury Ends here **********************//

}
