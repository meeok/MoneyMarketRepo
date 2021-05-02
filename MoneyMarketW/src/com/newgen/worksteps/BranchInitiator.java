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
                      //****************Treasurry Starts here *********************//
    	                case tbMarketTypeddChange:{
    	                	tbMarketTypeddChange(ifr);
    	                }
    	                break;
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
                        break;
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
                setVisible(ifr, new String[]{cpRediscountRateSection, cpReDiscountRateLess90Local});
                setFields(ifr, cpReDiscountRateLess90Local,rate);
            }
            else  return errMsg;
        }
        else if (Long.parseLong(dtm) >= 91 && Long.parseLong(dtm) <= 180){
            rate   = resultSet.get(0).get(1);
            if (isEmpty(rate)) {
                setVisible(ifr, new String[]{cpRediscountRateSection, cpReDiscountRate91To180Local});
                setFields(ifr, cpReDiscountRate91To180Local,rate );
            }
            else return errMsg;
        }
        else if (Long.parseLong(dtm) >= 181 && Long.parseLong(dtm) <= 270){
            rate = resultSet.get(0).get(2);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpRediscountRateSection, cpReDiscountRate181To270Local});
                setFields(ifr, cpReDiscountRate181To270Local,rate );
            }
            else return errMsg;
        }
        else if (Long.parseLong(dtm) >= 271 && Long.parseLong(dtm) <= 364){
            rate = resultSet.get(0).get(3);
            if (!isEmpty(rate)) {
                setVisible(ifr, new String[]{cpRediscountRateSection, cpReDiscountRate271To364Local});
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
        resultSet = new DbConnect(ifr,Query.getCpPoiMandateSearchQuery(getCpMarket(ifr),getCpPoiMandate(ifr))).getData();
        if (isEmpty(resultSet)) {
            clearFields(ifr,cpPoiMandateLocal);
            return "No Details found for this Mandate Detail";
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
    	setDropDown(ifr,tbDecisiondd,new String[]{decApprove,decDiscard});
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
    	else
    		hideField(ifr,tbBrcnhPriPersonalRate);
    	
    	String retMsg ="";
    	retMsg = tbValidatePrincipalAmt(ifr);
    	logger.info("tbretmsg>>>>"+retMsg);
    	return retMsg;
    }
    private void tbCategoryddChange(IFormReference ifr){
    	if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)){
    		setVisible(ifr, new String[] {tbBrnchPriCusotmerDetails,tbBranchPriSection,tbDecisionSection});
    		setMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});
    		setTbBrnchPriRqsttype(ifr,tbBidRqstType);
    		tbGenerateCustRefNo(ifr, getTbMarket(ifr));
    	}
    	else {
    		setTbBrnchPriRqsttype(ifr,"");
    		hideFields(ifr, new String[] {tbBrnchPriCusotmerDetails,tbBranchPriSection,tbDecisionSection});
    		undoMandatory(ifr, new String[] {tbBrnchPriTenordd,tbBrnchPriRollovrdd,tbBrnchPriPrncplAmt,tbCustAcctNo});

    	}
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
    				retMsg = (Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)))<tbPrsnlMinPrincipalAmt ? "Principal Amount must be minimum of "+String.valueOf(tbPrsnlMinPrincipalAmt) :"";
    			}
    			catch(Exception ex) {retMsg ="Invalid Principal amount";}
    		}
    	}
    	else if(getTbBrcnhPriRateTypedd(ifr).equalsIgnoreCase(tbBrnchPriRtBanKRate)) {
    		if(!isEmpty(getTbBrnchPriPrncplAmt(ifr))) {
    			logger.info("getTbBrnchPriPrncplAmt>>>>"+Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)));
	    		try {
					clearFields(ifr,tbBrnchPriPrncplAmt);
					retMsg = (Double.parseDouble(getTbBrnchPriPrncplAmt(ifr)))<tbBnkMinPrincipalAmt ? "Principal Amount must be minimum of "+String.valueOf(tbBnkMinPrincipalAmt) :"";
				}
				catch(Exception ex) {retMsg ="Invalid Principal amount";}
			}
    	}
   
    	return retMsg;
    }

    
    //**********************Treasury Ends here **********************//

}
