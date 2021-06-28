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
    private static final Logger logger = LogGen.getLoggerInstance(TreasuryOfficerMaker.class);


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
    public JSONArray executeEvent(FormDef formDef, IFormReference ifr, String event, String data) {

        logger.info("called executeEvent event"+ event);

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
                        case cpSetupWindowEvent:{ return cpSetupPmWindow(ifr);}
                        
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
                        case tbViewPriCustomerBids:{
                        	try {
                        		int selectedrow = Integer.parseInt(data);
                        		logger.info("selectedrow>>"+selectedrow);
                        		tbViewPriCustomerBids(ifr,selectedrow);
                        	}
                        	catch(Exception ex) {
                        		logger.info("tbViewPriCustomerBids Exception>>"+ex.toString());
                        	}
                        }
                        break;
                        case tbupdatePriCustomerBids:{
                        	try {
                        		int selectedrow = Integer.parseInt(data);
                        		logger.info("selectedrow>>"+selectedrow);
                        		return tbupdatePriCustomerBids(ifr,selectedrow);
                        	}
                        	catch(Exception ex) {
                        		logger.info("tbupdatePriCustomerBids Exception>>"+ex.toString());
                        		return "Updates failed for row Number :"+data;
                        	}
                        }
                        case tbUpdateSmIssuedBids:{
                        	try {
                        		int selectedrow = Integer.parseInt(data);
                        		logger.info("selectedrow>>"+selectedrow);
                        		return tbUpdateSmIssuedBids(ifr,selectedrow);
                        	}
                        	catch(Exception ex) {
                        		logger.info("tbupdatePriCustomerBids Exception>>"+ex.toString());
                        		return "Updates failed for row Number :"+data;
                        	}
                        	
                        }
                        case tbDownload:{
                            setFields(ifr,downloadFlagLocal,flag);
                            break;
                        }
                       
                        
                        /**** Treasury onClick End ****/
                        case cpViewReportEvent:{
                            viewReport(ifr);
                            break;
                        }
                        case cpDownloadEvent:{
                            setFields(ifr,downloadFlagLocal,flag);
                            disableField(ifr,cpDownloadBtn);
                            break;
                        }
                        case cpViewGroupBidEvent:{
                            viewCpGroupBids(ifr,Integer.parseInt(data));
                            break;
                        }
                        case cpUpdateBidEvent:{
                            updateCpPmBids(ifr,Integer.parseInt(data));
                            break;
                        }
                        
                    }
                }
                break;
                case onChange:{
                    switch (controlName){
                        case cpOnSelectCategory:{cpSelectCategory(ifr);}
                        break;
                        	case cpSmSetupEvent:{
                        		cpSmInvestmentSetup(ifr);
                            break;
                        }

                        /**** Treasury Onchange Start ****/
                        case tbCategoryddChange:{
                        	tbCategoryChange(ifr);
                        }
                        break;
                        case tbVerificationAmtChanged:{
                        	return tbCheckVerificationAmt(ifr);
                        }
                        /**** Treasury Onchange End  ****/
                    }
                }
                break;
                case custom:{
                    switch (controlName){
                        case cpGetPmGridEvent:{
                        return getCpPmBidGrid(ifr,Integer.parseInt(data));
                        }
                        /********** Treasury Start **********/
                        case tbGetPmGrid:{
                        	try {
                        		int gridcnt = Integer.parseInt(data);
                        		logger.info("selectedrow>>"+gridcnt);
                        		return tbGetPmGrid(ifr,gridcnt);
                        	}
                        	catch(Exception ex) {
                        		logger.info("tbGetPmGrid Exception>>"+ex.toString());
                        	}
                           
                         }
                        case tbSetBidAllocationCloseFlg:{
                        	setBidAllocationCloseFlg(ifr);
                        }
                        /********** Treasury End **********/
                    }
                }
                break;
                case onDone:{
                	switch (controlName){
                        case cpSmCpUpdateEvent:{return updateCpSmDetails(ifr,Integer.parseInt(data));}
                        case cpPmProcessSuccessBidsEvent:{
                            updateCpPmSuccessBids(ifr);
                            break;
                        }

	                /**** Treasury onDOne Start ****/
                	case tbOnDone:{
                		logger.info("data>>" + data);
                		return tbOnDone(ifr,data);
                		}
                	case tbCheckUnallocatedBids:{
                		return tbCheckUnallocatedBids(ifr);
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

    @Override
    public void cpSendMail(IFormReference ifr) {
        if (!isEmpty(getWindowSetupFlag(ifr))){
                if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                    message = "Commercial Paper time for "+getCpMarket(ifr)+" market has now been modified and requires your approval.";
                    new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                }
                else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)){
                    message = "Commercial Paper re-discount rate for "+getCpMarket(ifr)+" market has now been set and requires your approval.";
                    new MailSetup(ifr,getWorkItemNumber(ifr),getUsersMailsInGroup(ifr,groupName),empty,mailSubject,message);
                }
        }
    }
    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        setInvisible(ifr,new String[]{goBackDashboardSection});
        clearFields(ifr,new String[]{cpRemarksLocal});
     if (getUtilityFlag(ifr).equalsIgnoreCase(flag)){
           if(getDownloadFlag(ifr).equalsIgnoreCase(flag)){
               showCommercialProcessSheet(ifr);
               setVisible(ifr, new String[]{cpPrimaryBidSection, cpAllocSummaryTbl,cpAllocBankRateLocal,cpAllocCpRateLocal,cpAllocDefaultAllocLocal
               ,cpViewGroupBtn});
               enableFields(ifr, new String[]{cpAllocDefaultAllocLocal,cpAllocCpRateLocal,cpAllocBankRateLocal});
               setMandatory(ifr, new String[]{cpAllocDefaultAllocLocal,cpAllocCpRateLocal,cpAllocBankRateLocal});
               setInvisible(ifr, new String[]{cpViewReportBtn,cpDownloadBtn});
               setFields(ifr,new String[]{cpPmAllocFlagLocal},new String[]{flag});
           }
           else {
               setGenDetails(ifr);
               setFields(ifr, new String[]{prevWsLocal, selectProcessLocal, cpSelectMarketLocal}, new String[]{utilityWs, commercialProcess, cpPrimaryMarket});
               enableFields(ifr,cpViewReportBtn);
               showCommercialProcessSheet(ifr);
               setVisible(ifr, cpPrimaryBidSection);
           }
        }
       else if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerVerifier)){
            if (isEmpty(getWindowSetupFlag(ifr))) {
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                    if (getCpDecision(ifr).equalsIgnoreCase(decReject)) {
                        setVisible(ifr, new String [] {cpLandingMsgSection,cpDecisionSection});
                        setMandatory(ifr,new String [] {cpDecisionLocal,cpRemarksLocal,cpLandMsgLocal});
                        enableFields(ifr,new String[] {cpLandingMsgSection,cpDecisionSection});
                    } else if (getCpDecision(ifr).equalsIgnoreCase(decApprove)) {
                        setVisible(ifr,new String [] {cpLandingMsgSection,cpMarketSection,cpCategoryLocal,cpDecisionSection});
                        setInvisible(ifr,new String[]{cpDecisionSection});
                        enableFields(ifr,new String[]{cpDecisionSection,cpCategoryLocal});
                        disableFields(ifr, new String[]{cpSelectMarketLocal,cpLandingMsgSection});
                        setMandatory(ifr,new String[] {cpCategoryLocal});
                        setCpCategory(ifr, new String[]{cpCategorySetup});
                    }
                } else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                    if (getCpDecision(ifr).equalsIgnoreCase(decApprove)){
                        setVisible(ifr,new String [] {cpLandingMsgSection,cpMarketSection,cpCategoryLocal,cpDecisionSection});
                        enableFields(ifr,new String[]{cpDecisionSection,cpCategoryLocal});
                        disableFields(ifr, new String[]{cpSelectMarketLocal,cpLandingMsgSection});
                        setMandatory(ifr,new String[] {cpCategoryLocal,cpDecisionLocal,cpRemarksLocal});
                        setCpCategory(ifr, new String[]{cpCategorySetup});
                    }
                    else if (getCpDecision(ifr).equalsIgnoreCase(decReject)){

                    }
                }
            }
            else{
                if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                        setVisible(ifr,new String[]{cpMarketSection,cpTreasuryPriSection,cpCategoryLocal,cpDecisionSection});
                        setMandatory(ifr,new String[] {cpCategoryLocal,cpDecisionLocal,cpRemarksLocal});
                        setCpCategory(ifr,new String[]{cpCategoryUpdateLandingMsg,cpCategoryReDiscountRate,cpCategoryModifyCutOffTime});
                }
                else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                        setVisible(ifr,new String[]{cpMarketSection,cpTreasurySecSection,cpCategoryLocal,cpDecisionSection});
                        setMandatory(ifr,new String[] {cpCategoryLocal,cpDecisionLocal,cpRemarksLocal});
                        setCpCategory(ifr,new String[]{cpCategoryUpdateLandingMsg,cpCategoryReDiscountRate,cpCategoryModifyCutOffTime});
                }
            }
        }
         else if(getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerMaker))  {
             if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                 if (!isEmpty(getWindowSetupFlag(ifr))){
                     setVisible(ifr,new String[]{cpMarketSection,cpTreasuryPriSection,cpCategoryLocal,cpDecisionSection});
                     setMandatory(ifr,new String[] {cpCategoryLocal,cpDecisionLocal,cpRemarksLocal});
                     setCpCategory(ifr,new String[]{cpCategoryUpdateLandingMsg,cpCategoryReDiscountRate,cpCategoryModifyCutOffTime});
                 }
             }
             else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                 if (!isEmpty(getWindowSetupFlag(ifr))){
                     setVisible(ifr,new String[]{cpMarketSection,cpTreasurySecSection,cpCategoryLocal,cpDecisionSection});
                     setMandatory(ifr,new String[] {cpCategoryLocal,cpDecisionLocal,cpRemarksLocal});
                     setCpCategory(ifr,new String[]{cpCategoryUpdateLandingMsg,cpCategoryReDiscountRate,cpCategoryModifyCutOffTime});
                 }
             }
         }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        clearFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
        setDecision(ifr,cpDecisionLocal,new String [] {decSubmit,decDiscard});
    }
    private String cpSetupPmWindow(IFormReference ifr){
        if (isEmpty(getWindowSetupFlag(ifr))){
            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                if (compareDate(getCpOpenDate(ifr),getCpCloseDate(ifr))) return cpSetupPrimaryMarketWindow(ifr);
                else return "Close date cannot be before Open date.";
            }
        }
        return empty;
    }
    private void cpUpdateLandingMsg(IFormReference ifr){
        if (getCpUpdateMsg(ifr).equalsIgnoreCase(True)){
           setFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal},new String[]{decSubmit,"Kindly approve landing message update."});
           disableFields(ifr, new String[]{cpDecisionLocal,cpRemarksLocal});
            setInvisible(ifr, new String[]{cpSetupSection});
            undoMandatory(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
            setMandatory(ifr,new String[]{cpLandMsgLocal});
            enableFields(ifr,new String[]{cpLandMsgLocal});
        }
        else {
            clearFields(ifr, new String[]{cpDecisionLocal,cpRemarksLocal});
            setVisible(ifr, new String[]{cpSetupSection,cpDecisionSection});
            setMandatory(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
            undoMandatory(ifr,new String[]{cpLandMsgLocal});
            disableFields(ifr,new String[]{cpLandMsgLocal});
        }
    }
    private void cpSelectCategory(IFormReference ifr){
        disableField(ifr,cpCategoryLocal);
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategorySetup)){
                setVisible(ifr, new String [] {cpTreasuryPriSection,cpSetupSection,cpSetupWindowBtn,cpCutOffTimeSection});
                setMandatory(ifr,new String[] {cpOpenDateLocal, cpPmMinPriAmtLocal,cpCloseDateLocal});
                enableFields(ifr,new String[] {cpPmMinPriAmtLocal,cpCloseDateLocal,cpSetupWindowBtn});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                setVisible(ifr,new String[]{cpCutOffTimeSection});
                enableFields(ifr,new String[] {cpCloseDateLocal});
                undoMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpReDiscountRateSection,cpLandingMsgSection});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)){
                setVisible(ifr,new String[]{cpReDiscountRateSection});
                setMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                enableFields(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpCutOffTimeSection,cpLandingMsgSection});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryUpdateLandingMsg)){
                setVisible(ifr,new String[]{cpLandingMsgSection});
                enableFields(ifr,new String[]{cpLandMsgLocal});
                setMandatory(ifr,new String[]{cpLandMsgLocal});
                undoMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpReDiscountRateSection,cpCutOffTimeSection});
            }
            else {
                setInvisible(ifr,new String[]{cpReDiscountRateSection,cpCutOffTimeSection,cpLandingMsgSection});
            }
        }
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategorySetup)){
                setVisible(ifr,new String[]{cpTreasurySecSection,cpCutOffTimeSection,cpSmCutOffTimeLocal});
                setInvisible(ifr,new String[]{cpOpenDateLocal,cpCloseDateLocal});
                setFields(ifr, new String[]{cpSmCutOffTimeLocal, cpSmMinPrincipalLocal}, new String[]{smDefaultCutOffTime,smMinPrincipal});
                enableFields(ifr,new String[]{cpSmSetupLocal});
                setMandatory(ifr,new String[]{cpSmSetupLocal,cpSmMinPrincipalLocal});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                setVisible(ifr,new String[]{cpCutOffTimeSection});
                enableFields(ifr,new String[] {cpCloseDateLocal});
                undoMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpReDiscountRateSection,cpLandingMsgSection});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)){
                setVisible(ifr,new String[]{cpReDiscountRateSection});
                setMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                enableFields(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpCutOffTimeSection,cpLandingMsgSection});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryUpdateLandingMsg)){
                setVisible(ifr,new String[]{cpLandingMsgSection});
                enableFields(ifr,new String[]{cpLandMsgLocal});
                setMandatory(ifr,new String[]{cpLandMsgLocal});
                undoMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate91To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpReDiscountRateSection,cpCutOffTimeSection});
            }
            else {
                setInvisible(ifr,new String[]{cpReDiscountRateSection,cpCutOffTimeSection,cpLandingMsgSection});
            }
        }
    }
    private void viewReport(IFormReference ifr){
        resultSet = new DbConnect(ifr,new Query().getCpPmSummaryBidsQuery(getWorkItemNumber(ifr))).getData();
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


            setTableGridData(ifr, cpAllocSummaryTbl,new String[]{cpAllocTenorCol,cpAllocRateCol,cpAllocTotalAmountCol,cpAllocRateTypeCol,cpAllocCountCol,cpAllocStatusCol,cpAllocGroupIndexCol},
                    new String[]{tenor,rate,totalAmount,rateType,count, statusAwaitingTreasury,groupIndex});
        }
        setVisible(ifr,new String[]{cpAllocSummaryTbl,cpDownloadBtn});
        setInvisible(ifr,new String[]{cpViewReportBtn});
    }
    private String getCpPmBidGrid(IFormReference ifr, int rowCount){
        StringBuilder output = new StringBuilder(empty);
        for (int i = 0; i < rowCount; i++){
          String tenor =  ifr.getTableCellValue(cpAllocSummaryTbl,i,0);
          String rate =  ifr.getTableCellValue(cpAllocSummaryTbl,i,1);
          String totalAmount =  ifr.getTableCellValue(cpAllocSummaryTbl,i,2);
          String rateType =  ifr.getTableCellValue(cpAllocSummaryTbl,i,3);
          String count =  ifr.getTableCellValue(cpAllocSummaryTbl,i,4);
          String status =  ifr.getTableCellValue(cpAllocSummaryTbl,i,5);
          output.append("{\"tenor\": \"").append(tenor).append("\", \"rate\": \"").append(rate).append("\", \"totalAmount\": \"").append(totalAmount).append("\", \"rateType\": \"").append(rateType).append("\", \"count\": \"").append(count).append("\", \"status\": \"").append(status).append("\"}$");
        }
        logger.info("output from grid: "+output.toString());
        return output.toString().trim();
    }
    private void viewCpGroupBids(IFormReference ifr, int rowIndex){
        ifr.clearTable(cpBidReportTbl);
        String groupIndex = ifr.getTableCellValue(cpAllocSummaryTbl,rowIndex,6);
        logger.info("group index: "+ groupIndex);
        resultSet = new DbConnect(ifr, new Query().getCpPmGroupBidsQuery(groupIndex)).getData();
        for(List<String> result : resultSet){
            String id = result.get(0);
            String acctNo = result.get(1);
            String acctName = result.get(2);
            String tenor = result.get(3);
            String rate = result.get(4);
            String principal = result.get(5);
            float principal1 = Float.parseFloat(principal);
            principal = String.format("%.2f",principal1);
            logger.info("principal: "+ principal);
            setTableGridData(ifr,cpBidReportTbl,new String[]{cpBidCustIdCol,cpBidAcctNoCol,cpBidAcctNameCol,cpBidTenorCol,cpBidPersonalRateCol,cpBidTotalAmountCol,cpBidStatusCol,cpBidDefAllocCol},
                    new String[]{id,acctNo,acctName,tenor,rate,principal,statusAwaitingTreasury,defaultAllocation});
        }
        setVisible(ifr,new String[]{cpBidReportTbl,cpUpdateBtn});
    }
    private void updateCpPmBids(IFormReference ifr, int rowIndex){
        String bankRate = getFieldValue(ifr,cpAllocBankRateLocal);
        String personalRate = empty;
        String cpRate = getFieldValue(ifr,cpAllocCpRateLocal);
        String defaultAlloc = getFieldValue(ifr,cpAllocDefaultAllocLocal);
        String id = ifr.getTableCellValue(cpBidReportTbl,rowIndex,0);
        String rateType = new DbConnect(ifr,new Query().getCpPmBidDetailByIdQuery(id,rateTypeBidTblCol)).getData().get(0).get(0);
        String tenor = new DbConnect(ifr,new Query().getCpPmBidDetailByIdQuery(id,tenorBidTblCol)).getData().get(0).get(0);

        if (rateType.equalsIgnoreCase(rateTypePersonal))
            personalRate = new DbConnect(ifr,new Query().getCpPmBidDetailByIdQuery(id,rateBidTblCol)).getData().get(0).get(0);

        if (rateType.equalsIgnoreCase(rateTypeBank)){
            if (checkBidStatus(bankRate,cpRate)){
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,4,cpRate);
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,5,bankRate);
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,7,getMaturityDate(Integer.parseInt(tenor)));
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,9,defaultAlloc);
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,11,bidSuccess);
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,12,statusAwaitingMaturity);
                new DbConnect(ifr, new Query().getCpPmBidUpdateBankQuery(id,cpRate,bankRate,getMaturityDate(Integer.parseInt(tenor)),defaultAlloc,bidSuccess,statusAwaitingMaturity)).saveQuery();
            }
            else {
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,11,bidFailed);
                new DbConnect(ifr, new Query().getCpPmUpdateFailedBidsQuery(id,bidFailed)).saveQuery();
            }
        }
        else if (rateType.equalsIgnoreCase(rateTypePersonal)){
            if (checkBidStatus(personalRate,cpRate)){
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,4,cpRate);
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,7,getMaturityDate(Integer.parseInt(tenor)));
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,9,defaultAlloc);
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,11,bidSuccess);
                ifr.setTableCellValue(cpBidReportTbl,rowIndex,12,statusAwaitingMaturity);
                new DbConnect(ifr,new Query().getCpPmBidUpdatePersonalQuery(id,cpRate,getMaturityDate(Integer.parseInt(tenor)),defaultAlloc,bidSuccess,statusAwaitingMaturity)).saveQuery();
            }
            else {
                ifr.setTableCellValue(cpBidReportTbl, rowIndex, 11, bidFailed);
                new DbConnect(ifr, new Query().getCpPmUpdateFailedBidsQuery(id, bidFailed)).saveQuery();
            }
        }
    }
    private String updateCpSmDetails (IFormReference ifr, int rowCount){

        for (int i= 0; i< rowCount; i++){
            String maturityDate = ifr.getTableCellValue(cpSmCpBidTbl,i,2);
            String cpBillAmount = ifr.getTableCellValue(cpSmCpBidTbl,i,3);
            String minimumPrincipal = getFieldValue(ifr,cpSmMinPrincipalLocal);
            long dayToMaturity =  getDaysToMaturity(maturityDate);
            logger.info("days to maturity-- "+dayToMaturity);

            if (Float.parseFloat(cpBillAmount) < Float.parseFloat(minimumPrincipal))
                return "CP Bill Amount cannot be less than the minimum principal Amount. Correct row No. "+i+".";

            if (dayToMaturity > 270)
                return "Number of days to maturity Cannot be more 270. Please correct Maturity Date Column. Days to Maturity: "+dayToMaturity+"";

            if (dayToMaturity < 1)
                return "Number of days to maturity Cannot be less than 1. Please correct Maturity Date Column. Days to Maturity: "+dayToMaturity+"";


            ifr.setTableCellValue(cpSmCpBidTbl,i,6,String.valueOf(dayToMaturity));
            ifr.setTableCellValue(cpSmCpBidTbl,i,7,smStatusOpen);
        }
        setFields(ifr,new String[]{cpSmWinRefLocal}, new String[]{generateCpWinRefNo(cpSmLabel)});
        return empty;
    }
    private void cpSmInvestmentSetup(IFormReference ifr){
        disableFields(ifr,cpSmSetupLocal);
        if (getCpSmInvestmentSetupType(ifr).equalsIgnoreCase(smSetupNew)){
            if (isEmpty(getWindowSetupFlag(ifr))) {
                setVisible(ifr, new String[]{cpSmCpBidTbl, cpUploadExcelBtn, cpFileNameLocal});
                enableFields(ifr, new String[]{cpUploadExcelBtn, cpSmCpBidTbl});
            }
        }
        else if (getCpSmInvestmentSetupType(ifr).equalsIgnoreCase(smSetupUpdate)) {
            setVisible(ifr,new String[]{cpSmCpBidTbl,cpUploadExcelBtn,cpFileNameLocal});
            setInvisible(ifr,new String[]{cpUploadExcelBtn,cpFileNameLocal});
            disableFields(ifr,new String[]{cpSmCpBidTbl});
        }
        else {
            setInvisible(ifr,new String[]{cpUploadExcelBtn,cpFileNameLocal,cpSmCpBidTbl});
        }
    }
    private void updateCpPmSuccessBids(IFormReference ifr){
       validate =  new DbConnect(ifr,Query.getCpUpdatePmSuccessBidQuery(getWorkItemNumber(ifr))).saveQuery();
       if (validate >= 0){
          resultSet = new DbConnect(ifr,Query.getCpPmAllocatedBids(getWorkItemNumber(ifr))).getData();
          for (List<String> result : resultSet){
              String id = result.get(0);
              String principal = result.get(1);
              String tenor = result.get(2);
              String rate = result.get(3);
              String maturityDate = result.get(4);
              String interest = String.valueOf(((Double.parseDouble(principal) * Double.parseDouble(tenor) * getPercentageValue(rate)) / 365) * 100);
              if (isLeapYear(maturityDate))
                  interest = String.valueOf(Double.parseDouble(interest) + (Double.parseDouble(tenor) / 366));

              String principalAtMaturity = String.valueOf(Double.parseDouble(principal) * (Double.parseDouble(interest) + (getPercentageValue(rate) * Double.parseDouble(tenor))));
              new DbConnect(ifr, Query.getCpPmUpdateAllocatedBids(interest,principalAtMaturity,id)).saveQuery();
          }
       }
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
    	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard}); //cannot discard if bids have been placed by bi
    	//setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup});
    	clearFields(ifr,new String[]{tbRemarkstbx});
    	//setVisible(ifr, new String[] {tbCategorydd});
    	disableFields(ifr, new String[]{tbMarketUniqueRefId});
    	setVisible(ifr,new String [] {tbDecisionSection});
    	hideFields(ifr, new String[] {goBackDashboardSection,tbDecisiondd,tbRemarkstbx});//,tbPriSetupbtn
    	
    	
        //tb primary Market
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
        	disableField(ifr,tbAssigndd);
        	
            //setDropDown(ifr,tbAssigndd,new String[]{tbTreasuryUtilityLabel,tbTreasuryVerifierLable},new String[]{tbTreasuryUtility,tbTreasuryVerifier});
            //disableTbSections(ifr);
            
        	if (!getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)) {//landing msg is not approved
        		clearDropDown(ifr,tbCategorydd);
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
                enableFields(ifr,new String[] {tbLandingMsgSection,tbDecisionSection,tbLandingMsgSection});
                setMandatory(ifr,new String [] {tbDecisiondd,tbRemarkstbx,tbLandMsgtbx});
                setTbUpdateLandingMsg(ifr,True);
                disableFields(ifr, new String[]{tbMarketTypedd,tbCategorydd});
        	}
        	else if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))){ //flaghas not been set
        		// ready to set Market;
        		logger.info("flag is not set");
        		setVisible(ifr,new String [] {tbMarketSection,tbLandingMsgSection,tbDecisionSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx,tbAssigndd,tbMarketUniqueRefId});
        		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbMarketSection,tbCategorydd,tbAssigndd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbPriOpenDate,tbPriCloseDate,tbCategorydd,tbAssigndd});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbMarketUniqueRefId,tbMarketTypedd,tbDecisiondd});
                setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup},new String[]{tbCategorySetup});
                setFields(ifr,tbCategorydd,tbCategorySetup);
                hideFields(ifr,new String[]{tbMarketUniqueRefId});
                try {
					tbCategoryChange(ifr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.info("e.printStackTrace()");
				}
                
              //setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup,tbPoolManagerLabel},new String[]{tbCategorySetup,tbPoolManagerLabel});
        	} 
        	else if((getTbSetUpFlg(ifr).equalsIgnoreCase(flag))){ 
        		// Market is already set
        		logger.info("isTbWindowClosed>>>"+isTbWindowClosed(ifr,getTbMarketUniqueRefId(ifr))+getTbMarketUniqueRefId(ifr));
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx,tbAssigndd});
        		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbPriOpenDate,tbPriCloseDate,tbCategorydd});
                disableFields(ifr, new String[]{tbMarketUniqueRefId,tbPriOpenDate,tbPriCloseDate,tbLandingMsgSection,tbPriSetupSection,tbMarketTypedd,tbAssigndd});
                setDropDown(ifr,tbCategorydd,new String[]{tbCategoryReDiscountRate,tbCategoryCutOff,tbPoolManagerLabel},new String[]{tbCategoryReDiscountRate,tbCategoryCutOff,tbPoolManager});
        	}
        	
        	//At cutoff time show select view report to view all bid requests for both fresh and rollover mandate
        	if(isTbWindowClosed(ifr,getTbMarketUniqueRefId(ifr))){
        		logger.info("test7");
        		setVisible(ifr,new String [] {tbPrimaryBidSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx,tbAssigndd});
        		enableFields(ifr,new String[]{tbViewPriBidReportbtn,tbPrimaryBidSection,tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbPriSetupSection,tbMarketTypedd,tbAssigndd,tbMarketUniqueRefId});
                hideFields(ifr, new String[] {tbPriBidAllocationTable,tbPriBidCustRqstTable,tbPriBidReportTable});
                //bid allocation
                setDropDown(ifr,tbCategorydd,new String[]{tbCategoryBid,tbCategoryReDiscountRate,tbPoolManagerLabel},new String[]{tbCategoryBid,tbCategoryReDiscountRate,tbPoolManager});
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
        	
        	hideFields(ifr, new String[] {tbAssigndd});
        	
        	if (!getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)) {//landing msg is not approved
        		clearDropDown(ifr,tbCategorydd);
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
                enableFields(ifr,new String[] {tbLandingMsgSection,tbDecisionSection});
                setMandatory(ifr,new String [] {tbDecisiondd,tbRemarkstbx,tbLandMsgtbx});
                //disableFields(ifr, new String[]{tbMarketTypedd});
                hideFields(ifr, new String[] {tbCategorydd});
        	}
        	else if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))){// ready to set Market;
        		setVisible(ifr,new String [] {tbMarketSection,tbLandingMsgSection,tbDecisionSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbCategorydd});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbMarketTypedd,tbSecUniqueReftbx});
                setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup},new String[]{tbCategorySetup});
                setFields(ifr,tbCategorydd,tbCategorySetup);
                tbSetupCatSelected(ifr);
        	}
        	else if(getTbSetUpFlg(ifr).equalsIgnoreCase(flag)){// Market is set. Can perform rediscount rate and modifications etc
        		logger.info("market is set for secondary");
        		setVisible(ifr,new String [] {tbMarketSection,tbLandingMsgSection,tbDecisionSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbCategorydd});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbMarketTypedd,tbMarketUniqueRefId});
                setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup,tbCategoryReDiscountRate,tbCategoryCutOff},new String[]{tbCategorySetup,tbCategoryReDiscountRate,tbCategoryCutOff});
        	}
        	/*else if (getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)) {//landing msg is approved
        		clearDropDown(ifr,tbCategorydd);
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
                enableFields(ifr,new String[] {tbDecisionSection,tbMarketSection,tbCategorydd});
                setMandatory(ifr,new String [] {tbDecisiondd,tbRemarkstbx,tbLandMsgtbx});
                disableFields(ifr, new String[]{tbMarketTypedd});
        	}
        	if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))){ //(getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)
        		// ready to set Market;
        		setVisible(ifr,new String [] {tbMarketSection,tbLandingMsgSection,tbDecisionSection,tbTreasurySecSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbPriOpenDate,tbPriCloseDate,tbCategorydd});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbUniqueReftbx,tbMarketTypedd,tbAssigndd});
                setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup,tbPoolManagerLabel},new String[]{tbCategorySetup,tbPoolManagerLabel});
        	}*/
        }
    }
    /*
     * automatically populate primary market window unique reference in the below 
     * format TPMADATEYEAR for example TBPMA28052020 which will not be editable
     */
    private String tbCategoryChange(IFormReference ifr) throws ParseException{
    	logger.info("tbCategoryChange");
    	logger.info(getTbCategorydd(ifr));
    	//primary Market
    	String retMsg ="";
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
            if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){ 
            	//check if a window is open
            	logger.info("!isTbWindowOpen(ifr)>>"+!isTbWindowOpen(ifr));
            	if(!isTbWindowOpen(ifr)) {
            		setTbMarketUniqueRefId(ifr,generateTbUniqueReference(ifr)); //set the unique reference
                	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
                	hideField(ifr,tbDecisionSection);
                	setVisible(ifr,tbMarketUniqueRefId);
            	}
            	else if(getTbCategorydd(ifr).equalsIgnoreCase(tbPoolManager)){ 
            		enableField(ifr,tbAssigndd);
                    setDropDown(ifr,tbAssigndd,new String[]{tbTreasuryUtilityLabel,tbTreasuryVerifierLable},new String[]{tbTreasuryUtility,tbTreasuryVerifier});
            	}
            	else { //market already set clear category dropdown and send msg
            		clearFields(ifr,tbCategorydd);
            		retMsg =tbWindowActiveMessage;
            		disableField(ifr,tbAssigndd );
            	}
            }
            else if(getTbCategorydd(ifr).equalsIgnoreCase(tbPoolManager)){ 
        		enableField(ifr,tbAssigndd);
        	}
           else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)){
            	setVisible(ifr,new String [] {tbPrimaryBidSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection,tbCategorydd,tbUpdateLandingMsgcbx});
        		enableFields(ifr,new String[]{tbViewPriBidReportbtn,tbPrimaryBidSection,tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {});
                disableFields(ifr, new String[]{tbAssigndd,tbLandingMsgSection,tbPriSetupSection,tbMarketTypedd,tbMarketUniqueRefId});
                hideFields(ifr, new String[] {tbSettlementDte,tbCalculateMaturityDtebtn,tbPriBidAllocationTable,tbPriBidCustRqstTable,tbPriBidReportTable});
            }
            else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryReDiscountRate)){
    			setDropDown(ifr,tbDecisiondd,new String[]{decSubmit});
    			//setFields(ifr,decSubmit,decSubmit);
    			setVisible(ifr,new String [] {tbPriSetupSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbRediscountRate});
    			//check if rediscount rate has already been set
    			if(!getFieldValue(ifr,tbRediscoutApprovedFlg).equalsIgnoreCase(yesFlag)) {// not set
    				disableFields(ifr, new String[]{tbDecisionSection,tbLandingMsgSection,tbSecUniqueReftbx,tbPriSetupSection});
                    enableFields(ifr,new String[]{tbRediscountRate,tbDecisionSection});
                    setMandatory(ifr,new String [] {tbRdrlessEqualto90tbx,tbRdr91to180,tbRdr181to270,tbRdr271to364days,tbDecisiondd});
    			}
    			else {//already set - disable rediscount rate field
    				clearFields(ifr, new String[] {tbDecisiondd});
    				disableFields(ifr, new String[]{tbRediscountRate,tbDecisionSection,tbDecisionSection,tbLandingMsgSection,tbSecUniqueReftbx,tbPriSetupSection});
    			}
                
    		}
            else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryCutOff)){
            	disableFields(ifr, new String[] {tbPriOpenDate,tbDecisiondd});
            	enableFields(ifr,new String[] {tbPriCloseDate});
            	setTbDecisiondd(ifr,decSubmit);
            }
            else {
            	tbFormLoad(ifr); //remove the set gendetails...
            	/*setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
            	setVisible(ifr,tbDecisionSection);
            	setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup});*/
            }
        }
        
        //secondary MArket
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
        	hideField(ifr,tbSmUploadTbills); 
        	//if(!isTbWindowClosed(ifr,getFieldValue(ifr,tbSecUniqueReftbx))){//window is not closed
        		if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){
        			//check if market is set
        			//setDropDown(ifr,tbSmSetupdd,new String[]{smSetupUpdate,smSetupNew});
        			//if(isTbWindowOpen(ifr)) { // enable this to check if market is still open for modifications or order ....
        			 if((getTbSetUpFlg(ifr).equalsIgnoreCase(flag))){//maket is set up and window is open
        				logger.info(isTbWindowOpen(ifr));
        				logger.info("update bid");
                		/*setTbMarketUniqueRefId(ifr,generateTbUniqueReference(ifr)); //set the unique reference
                    	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
                    	hideField(ifr,tbDecisionSection);
                    	setVisible(ifr,tbMarketUniqueRefId);*/
                	//}
        			//if(getTbActiveWindowwithRefid(ifr).equalsIgnoreCase(getFieldValue(ifr,tbSecUniqueReftbx))) {//market is set user can only update
        				//logger.info("getTbActiveWindowwithRefid(ifr).equalsIgnoreCase(getFieldValue(ifr,tbSecUniqueReftbx))>>>>>>>>>>>"+getTbActiveWindowwithRefid(ifr).equalsIgnoreCase(getFieldValue(ifr,tbSecUniqueReftbx)));
                    	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
                    	setFields(ifr,new String[] {tbSmSetupdd,tbDecisiondd}, new String[] {smSetupUpdate,decSubmit});
                		setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
                		enableFields(ifr,new String[]{tbMarketSection,tbDecisionSection,tbVerificationAmtttbx,tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
                		setMandatory(ifr,new String [] {tbVerificationAmtttbx});
                        disableFields(ifr, new String[]{tbSmIssuedTBillTbl,tbLandingMsgSection,tbSecUniqueReftbx,tbDecisionSection,tbMarketUniqueRefId});
                     
        			}
        			else {//new bid setup
        				logger.info("new bid");
        				setTbMarketUniqueRefId(ifr,generateTbUniqueReference(ifr)); //set the unique reference
        				logger.info(">>"+getTbMarketUniqueRefId(ifr));
        				setFields(ifr, tbSecUniqueReftbx,getTbMarketUniqueRefId(ifr));
                    	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
                    	setFields(ifr,tbSmSetupdd,smSetupNew);
                    	hideField(ifr,tbDecisionSection);
                		setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
                		enableFields(ifr,new String[]{tbSmIssuedTBillTbl,tbDecisionSection});
                        setFields(ifr, new String[]{tbSecCuttOfftime, tbVerificationAmtttbx}, new String[]{tbGetSmDefaultCutoffDteTime(),String.valueOf(tbSmMinVerificationAmt)});
                		setMandatory(ifr,new String [] {tbVerificationAmtttbx});
                        disableFields(ifr, new String[]{tbAssigndd,tbMarketUniqueRefId,tbLandingMsgSection,tbSecUniqueReftbx,tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
        			}
        		}
        		else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryReDiscountRate)){
        			setDropDown(ifr,tbDecisiondd,new String[]{decSubmit});
        			//setFields(ifr,decSubmit,decSubmit);
        			setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbRediscountRate});
                    disableFields(ifr, new String[]{tbDecisionSection,tbLandingMsgSection,tbSecUniqueReftbx,tbTreasurySecSection});
                    enableFields(ifr,new String[]{tbRediscountRate,tbDecisionSection});
                    setMandatory(ifr,new String [] {tbRdrlessEqualto90tbx,tbRdr91to180,tbRdr181to270,tbRdr271to364days,tbDecisiondd});
        		}
        		
        		else if(getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryCutOff)) {//market is set user can only update
                	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit});
                	setFields(ifr,new String[] {tbSmSetupdd,tbDecisiondd}, new String[] {smSetupUpdate,decSubmit});
            		setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
            		enableFields(ifr,new String[]{tbSecCuttOfftime});
            		hideFields(ifr,new String[]{tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
            		setMandatory(ifr,new String [] {tbDecisiondd,tbSecCuttOfftime});
                    disableFields(ifr, new String[]{tbVerificationAmtttbx,tbSmIssuedTBillTbl,tbLandingMsgSection,tbSecUniqueReftbx,tbDecisionSection});
                    //make invisible grids visible.
    			}
        		
        	/*}
        	else { //window is closed
        		setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
                disableFields(ifr, new String[]{tbDecisionSection,tbLandingMsgSection,tbSecUniqueReftbx,tbTreasurySecSection});
        	}*/
        }
        		
        return retMsg;
   }
    //when setup is selected by default by the system after the approval of landing page
    private void tbSetupCatSelected(IFormReference ifr) {

		//check if market is set
		//setDropDown(ifr,tbSmSetupdd,new String[]{smSetupUpdate,smSetupNew});
		if(isTbWindowOpen(ifr)) {
			logger.info("update bid");
    		/*setTbMarketUniqueRefId(ifr,generateTbUniqueReference(ifr)); //set the unique reference
        	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
        	hideField(ifr,tbDecisionSection);
        	setVisible(ifr,tbMarketUniqueRefId);*/
    	//}
		//if(getTbActiveWindowwithRefid(ifr).equalsIgnoreCase(getFieldValue(ifr,tbSecUniqueReftbx))) {//market is set user can only update
			//logger.info("getTbActiveWindowwithRefid(ifr).equalsIgnoreCase(getFieldValue(ifr,tbSecUniqueReftbx))>>>>>>>>>>>"+getTbActiveWindowwithRefid(ifr).equalsIgnoreCase(getFieldValue(ifr,tbSecUniqueReftbx)));
        	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
        	setFields(ifr,new String[] {tbSmSetupdd,tbDecisiondd}, new String[] {smSetupUpdate,decSubmit});
    		setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
    		enableFields(ifr,new String[]{tbVerificationAmtttbx,tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
    		setMandatory(ifr,new String [] {tbVerificationAmtttbx});
            disableFields(ifr, new String[]{tbSmIssuedTBillTbl,tbLandingMsgSection,tbSecUniqueReftbx,tbDecisionSection,tbMarketUniqueRefId});
         
		}
		else {//new bid setup
			logger.info("new bid");
			setTbMarketUniqueRefId(ifr,generateTbUniqueReference(ifr)); //set the unique reference
			setFields(ifr, tbSecUniqueReftbx,getTbMarketUniqueRefId(ifr));
        	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
        	setFields(ifr,tbSmSetupdd,smSetupNew);
        	hideField(ifr,tbDecisionSection);
    		setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
    		enableFields(ifr,new String[]{tbSmIssuedTBillTbl,tbDecisionSection});
            setFields(ifr, new String[]{tbSecCuttOfftime, tbVerificationAmtttbx}, new String[]{tbGetSmDefaultCutoffDteTime(),String.valueOf(tbSmMinVerificationAmt)});
    		setMandatory(ifr,new String [] {tbVerificationAmtttbx});
            disableFields(ifr, new String[]{tbAssigndd,tbMarketUniqueRefId,tbLandingMsgSection,tbSecUniqueReftbx,tbUpdteSmMatDte,tbUpdteSmRate,tbUpdteSmTBillsAmt,tbUpdateSmIssuedBidsbtn});
		}
    }
    //@not used
	private void tbSmSetupChanged(IFormReference ifr) {
		//is window active
		if(isTbWindowActive(ifr)){  //window is active its not new
			//setTbSecUniqueReftbx(ifr,generateTbUniqueReference(ifr)); //set the unique reference
	    	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
	    	//hideField(ifr,tbDecisionSection);
			setVisible(ifr,new String [] {tbTreasurySecSection,tbLandingMsgSection,tbDecisionSection,tbMarketSection});
			enableFields(ifr,new String[]{tbSmIssuedTBillTbl,tbDecisionSection});
	        setFields(ifr, new String[]{tbSecCuttOfftime, tbVerificationAmtttbx}, new String[]{tbGetSmDefaultCutoffDteTime(),String.valueOf(tbSmMinVerificationAmt)});
			setMandatory(ifr,new String [] {});
	        disableFields(ifr, new String[]{tbSmIssuedTBillTbl,tbLandingMsgSection,tbSecUniqueReftbx});
		}
		//else {// new setup
	}
		
    private void tbUpdateLandingMsg(IFormReference ifr){
        if (getTbUpdateLandingMsg(ifr)){ //true
        	clearDropDown(ifr,tbCategorydd);
            clearFields(ifr, new String[]{tbMarketUniqueRefId,tbPriOpenDate,tbPriCloseDate});
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
            disableFields(ifr, new String[]{tbLandingMsgSection,tbMarketUniqueRefId,tbMarketTypedd});
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
    //on done check if all approved bids have been allocated befor submitting
    private String tbCheckUnallocatedBids(IFormReference ifr) {
    	//get the refid  of the workitem
    	String qry = new Query().getTbPmNoBidAllocationQuery(getTbMarketUniqueRefId(ifr));
    	logger.info("getTbPmNoBidAllocationQuery>>"+qry);
        List<List<String>> dbr = new DbConnect(ifr,qry).getData();
        logger.info("getTbPmNoBidAllocationQuery>>"+dbr);
        return dbr.size()>0 ? "All Approved Bids have not been allocated.Do you want to set all as failed bids?" : "";
    }
    /*
     * save market setup details into db if flag N
     * if market is already setup... fetch details from db and populate
     * check if market is set up -- save details in db
     */
    private String tbOnDone(IFormReference ifr,String data) {
    	logger.info("tbOnDone>>"); 
    	String retMsg="";
    	 if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){
    		//	retMsg = validateDate(ifr);
    			if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))) //market not set
	    				retMsg = setUpTbMarketWindow(ifr,getTbMarketUniqueRefId(ifr),getTbPriOpenDate(ifr),getTbPriCloseDate(ifr),"");// set market
    		}
    		else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryBid)){
    			//check if maturity date is calculated in summary grid and update all customer's maturity date
    			retMsg = tbUpdateCustMaturityDte(ifr);
    			if(isEmpty(retMsg)) {
    				//check if all bids are set --- get bdStatus
    				retMsg =tbCheckUnallocatedBids(ifr);
    			}
    		}
    		else if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategoryReDiscountRate)){
        		//update the database with rediscount rates
        			retMsg =setUpRediscountrate(ifr);
        		}
    	}
    	 else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
    		 //verify bid inputs
    		 if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup) && getFieldValue(ifr,tbSmSetupdd).equalsIgnoreCase(smSetupNew)){
    			 logger.info("secondary market");
    			 retMsg =tbUpdateSmIssuedSmBid(ifr,data);
    		 }
    	 }
    	 
    	logger.info("Ondone Validate retMsg>>"+retMsg);
    	return retMsg;
    }
    
    private void setBidAllocationCloseFlg(IFormReference ifr)
    {
    	setFields(ifr,tbBidAllocationCloseFlg,"N");
    }
    
    /*
     * check if maturity date is calculated in summary 
     * grid and update all customer's maturity date
     */
    private String tbUpdateCustMaturityDte(IFormReference ifr) {
    	String retMsg ="";
    	String settlementDte = getFieldValue(ifr,tbSettlementDte);
    	if(!isEmpty(settlementDte)) {
    		logger.info("settlementDte>>>."+settlementDte);
        	String matDte91 = tbDBDteFormat(getMaturityDate(settlementDte,91));
        	logger.info("matDte91>>>."+matDte91);
        	String matDte182 = tbDBDteFormat(getMaturityDate(settlementDte,182));
        	logger.info("matDte182>>>."+matDte182);
        	String matDte364 = tbDBDteFormat(getMaturityDate(settlementDte,364));
        	logger.info("matDte364>>>."+matDte364);
        	
        	String refid = getTbMarketUniqueRefId(ifr);
        	//update maturity date of customers with succesful bids 
        	String qry91 = new Query().getTbPmUpdateMaturityDteQuery(refid, bidSuccess,"91", matDte91);
    		logger.info("getTbPmCustRqstQuery91>>"+qry91);
    	    int dbr91 = new DbConnect(ifr,qry91).saveQuery();
    	    logger.info("dbr getTbPmCustRqstQuery91>>"+dbr91);
    	    if (dbr91< 0) 
    	    	retMsg = "Could not calculate maturity date for 91 days tenor";
    	    
    	    //update for 182days
    	    String qry182 = new Query().getTbPmUpdateMaturityDteQuery(refid, bidSuccess,"182", matDte182);
    		logger.info("getTbPmCustRqstQuery182>>"+qry182);
    	    int dbr182 = new DbConnect(ifr,qry182).saveQuery();
    	    logger.info("dbr getTbPmCustRqstQuery182>>"+dbr182);
    	    if (dbr182< 0) 
    	    	retMsg = "Could not calculate maturity date for 182 days tenor";
    	    
    	    //update for 364days
    	    String qry364 = new Query().getTbPmUpdateMaturityDteQuery(refid, bidSuccess,"364", matDte364);
    		logger.info("getTbPmCustRqstQuery364>>"+qry364);
    	    int dbr364 = new DbConnect(ifr,qry364).saveQuery();
    	    logger.info("dbr getTbPmCustRqstQuery364>>"+dbr364);
    	    if (dbr364< 0) 
    	    	retMsg = "Could not calculate maturity date for 364 days tenor";
    	}
    	
    	else 
    		retMsg ="Settlement date cannot be empty";
	     
    	return retMsg;
    }
    
    
    
    /*
     * setup - save redicount rate into setup table
     */
    private String setUpRediscountrate(IFormReference ifr) {
    	String retMsg ="";
    	if (getTbDecision(ifr).equalsIgnoreCase(decSubmit)) {
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
    	return retMsg;
    }
    
    /*
     * Setup treasury Bills for secondary
     */
    private String tbUpdateSmIssuedSmBid(IFormReference ifr, String rowCount){
    	String retMsg ="";
    	int rwcnt = 0;
    	try {
    		rwcnt = Integer.parseInt(rowCount);
    		logger.info("rwcnt>>"+rwcnt);
    	}
    	catch(Exception ex) {
    		logger.info("tbSetupSMMarket parse grid count error>>>"+ ex.toString());
    		retMsg ="tbSetupSMMarket parse grid count error>>>"+ ex.toString();
    	}
    	if(isEmpty(retMsg)) {
	        for (int i= 0; i< rwcnt; i++){
	            String maturityDate = ifr.getTableCellValue(tbSmIssuedTBillTbl,i,0);
	            logger.info("maturityDate>>"+maturityDate);
	            String tBillAmount = ifr.getTableCellValue(tbSmIssuedTBillTbl,i,1);
	            logger.info("tBillAmount>>"+tBillAmount);
	            String veriAmt = getFieldValue(ifr,tbVerificationAmtttbx);
	            logger.info("veriAmt>>"+veriAmt);
	            long daysToMaturity =0;
	            try {
	            	daysToMaturity =  getDaysToMaturity(maturityDate);
	            	logger.info("daysToMaturity-- "+daysToMaturity);
	            }
	            catch(Exception ex) {
	        		logger.info("Invalid date format for maturity date>>>"+ ex.toString());
	        		retMsg ="Invalid date format for maturity date";
	        	}
	            if (Float.parseFloat(tBillAmount) < Float.parseFloat(veriAmt)) { //check for exception
	                retMsg = "Treaury Bill Amount cannot be less than the Verification Amount. Correct row No. "+i+".";
	                break;  //exit loop and report error
	            }
	            else //update row{
	            	ifr.setTableCellValue(tbSmIssuedTBillTbl,i,6,String.valueOf(smStatusOpen));
	            	ifr.setTableCellValue(tbSmIssuedTBillTbl,i,9,String.valueOf(daysToMaturity));
	        }
    	}
        logger.info("tbUpdateSmIssuedSmBid>>>retmsg"+retMsg);
        return retMsg;
    }
    private void tbViewPriBidSmryReport(IFormReference ifr){
    	logger.info("tbViewRepor>>>");
    	ifr.clearTable(tbPriBidReportTable);
    	String refid = getTbMarketUniqueRefId(ifr);
    	String qry = new Query().getTbPmBidSummaryQuery(getTbMarketUniqueRefId(ifr));
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
	            String personalrate = ls.get(5);
	            logger.info("personalrate-- "+ personalrate);
	            setTableGridData(ifr,tbPriBidReportTable,new String[]{tbBidRptRqstTypeCol,tbBidRptRateCol,tbBidRptTenorCol,tbBidRptRateTypeCol,tbBidRptTtlAmtCol,tbBidRptTxnCoutnCol,tbBidRptStatusCol},
	                    new String[]{rqstType,personalrate,tenor,rateType,totalAmount,count, statusAwaitingTreasury});
	        }
	        setVisible(ifr,new String[]{tbSettlementDte,tbPriBidReportTable,tbViewPriBidReportbtn,tbViewPriBidDwnldBidSmrybtn,tbPriBidViewCustRqstbtn,tbPmTotalAllocationAmt});
	        disableFields(ifr,new String[]{tbViewPriBidReportbtn,tbPmTotalAllocationAmt});
	        enableFields(ifr,new String[]{tbSettlementDte,tbViewPriBidDwnldBidSmrybtn,tbPriBidViewCustRqstbtn});
	        setMandatory(ifr,new String[]{tbSettlementDte});
	        setTbPmtotalBidAllAmount(ifr,refid);
        }
        else {//return a message of no bids for this window
        	}
     }
    
    //view customer request details
    private void tbViewPriCustomerBids(IFormReference ifr, int rowIndex){
        ifr.clearTable(tbPriBidCustRqstTable);
        String refid = getTbMarketUniqueRefId(ifr);
        logger.info("refid>> "+ refid);
        String rqstType = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,0);
        logger.info("rqstType>> "+ rqstType);
        String tenorgrp = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,1);
        logger.info("tenor>> "+ tenorgrp);
        String personalRate = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,2);
        logger.info("personalRate>> "+ personalRate);
        String rateType = ifr.getTableCellValue(tbPriBidReportTable,rowIndex,4);
        
        String qry = rateType.equalsIgnoreCase(tbBrnchPriRtBanKRate) ? 
        		new Query().getTbPmCustomerRqstyQuery(refid, rqstType, tenorgrp, rateType) :
        		new Query().getTbPmCustomerRqstyQuery(refid, rqstType, tenorgrp, rateType, personalRate) ;
        		
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
                 String ratetype = ls.get(5);
                 String rate = ls.get(6);
                 String principal = ls.get(7);
                 String wino = ls.get(8);
                 String defaultAllocation ="100";
                 String cbnRate = ls.get(10);
                 logger.info("cbnRate>>>"+cbnRate);
                 String bankRate = ls.get(11);
                 String matDte = ls.get(12);
                 String newAll = ls.get(13);
                 String bidStatus = ls.get(14);
                 String status = ls.get(15);
                 setTableGridData(ifr,tbPriBidCustRqstTable,new String[]{tbBidCustRefNocol,tbBidWorkItemNoCol,tbBidAcctNoCol, tbBidAcctNamecol ,tbBidTenorCol ,tbBidPersonalRateCol,
                		 tbBidTotalAmtCol,tbBidStausCol,tbBidDefaultAllCol,tbBidRateTypeCol,tbBidCBNRateCol,tbCidBankRateCol,tbBidMaturityDteCol,tbBidNewAllCol, tbBidStausCol,tbStausCol},
                         new String[]{id,wino,acctNo,acctName,tenor,rate,principal,statusAwaitingTreasury,defaultAllocation,ratetype,
                        		 cbnRate,bankRate,matDte,newAll,bidStatus,status});
             }
             setVisible(ifr,new String[]{tbSettlementDte,tbCalculateMaturityDtebtn,tbPriBidCustRqstTable,tbPriBidUpdateCustBid,tbPriBidBulkAllbtn,tbPriBidBulkAllbtn,tbPriBidBlkCbnRate,tbPriBidBlkBankRate});//tbPriBidBlkNewAll
             setTbPmtotalBidAllAmount(ifr,refid);
             setMandatory(ifr,new String[]{tbSettlementDte});
        }    
    }
    //write general update without selecting index
    //update the grid table with bulk data --update external table with the updates
    private String tbupdatePriCustomerBids(IFormReference ifr, int rowIndex){
		logger.info("tbupdatePriCustomerBids>>>");
    	String retMsg ="";
    	
    	String gridcbnRate = getFieldValue(ifr,tbPriBidBlkCbnRate);//ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,6);
        String gridbankRate =getFieldValue(ifr,tbPriBidBlkBankRate);//ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,7);
        String newallocation = getFieldValue(ifr,tbPriBidBlkNewAll);//ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,9);
        String gridpersonalRate =ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,8); 
        
        if(gridpersonalRate.equalsIgnoreCase(rateTypeBank)) {
        	if(isEmpty(gridcbnRate) || isEmpty(gridbankRate))
        		retMsg = "CBN rate and Bank Rate Cannot be empty";
        }
        else {
        	if(isEmpty(gridcbnRate))
        		retMsg = "CBN rate Cannot be empty";
        }
    	
        if(isEmpty(retMsg)) {
	    	logger.info("gridcbnRate>>>"+gridcbnRate);
	    	double cbnRate = 0;
	    	try{cbnRate = Double.parseDouble(gridcbnRate);}
	    	catch(Exception ex) {
	    		retMsg = "parsing cbnrate error:>>>"+ ex.toString();
	    		logger.info(retMsg);
	    	}
	    	logger.info("cbnRate>>>"+cbnRate);
	    	
	   
	        logger.info("gridbankRate>>>"+gridbankRate);
	        double bankRate = 0;
	    	try{bankRate = Double.parseDouble(gridbankRate);}
	    	catch(Exception ex) {logger.info("parsing cbnrate error:>>>"+ ex.toString());}
	    	logger.info("bankRate>>>"+bankRate);
	    	
	    	
	         logger.info("newallocation>>>"+newallocation);
	         double allocation = 100;
	         try {allocation = Double.parseDouble(newallocation);}
	         catch(Exception ex) {logger.info("parsing allocation error:>>>"+ ex.toString());}
	         logger.info("allocation>>>"+allocation);
	        
	    	
	    	String wino = ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,0);
	    	logger.info("wino>>>"+wino);
	    	
	    	String tenor = ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,4);
	    	logger.info("tenor>>>"+tenor);
	    	//logger.info("tenor2>>>"+tenor.substring(0, tenor.indexOf(" ")));
	    	
	    	//update table and db
	        logger.info("gridpersonalRate>>>"+gridpersonalRate);
	        double personalRate = 0;
	    	try{personalRate = Double.parseDouble(gridpersonalRate);}
	    	catch(Exception ex) {
	    		retMsg = "parsing personal rate  error:>>>"+ ex.toString();
	    		logger.info(retMsg);
	    	}
	    	logger.info("personalRate>>>"+personalRate);
	    	
	       
	       // String defaultAll =ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,10);
	        String maturityDate =tbDBDteFormat(getMaturityDate(Integer.parseInt(tenor)));//.substring(0, tenor.indexOf(" "))));
	        logger.info("maturityDate>>>"+maturityDate);
	        String rateType =ifr.getTableCellValue(tbPriBidCustRqstTable,rowIndex,15);
	        logger.info("rateType>>>"+rateType);
	        //logger.info("(checkBidStatus(bankRate,cbnRate)>>>"+checkBidStatus(bankRate,cbnRate));
	        
	        if (rateType.equalsIgnoreCase(rateTypePersonal)) {
	        	if (checkBidStatus(personalRate,cbnRate)) {
	        		logger.info(checkBidStatus(personalRate,cbnRate));
	        		//update gridview
	            	ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,6,gridcbnRate);
	               // ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,7,gridbankRate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,11,maturityDate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,9,newallocation);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,14,bidSuccess);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,13,statusAwaitingMaturity);
	                
	                //update DB table
	        		String qry = new Query().getTbPmBidUpdatePersonalQuery(wino, cbnRate, maturityDate, allocation, bidSuccess, statusAwaitingMaturity);
	        		logger.info("getTbPmBidUpdatePersonalQuery>>"+qry);
	        		int dbr = new DbConnect(ifr, qry).saveQuery();
	                logger.info("getTbPmBidUpdatePersonalQuery save db result>>>"+dbr);
	                
	        	}
	        	else {
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,14,bidFailed);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,13,"");
	                String qry = new Query().getTbPmUpdateFailedBidsQuery(wino,bidFailed,cbnRate);
	            	logger.info("getTbPmUpdateFailedBidsQuery>>"+ qry);
	                int dbr = new DbConnect(ifr, qry).saveQuery();
	                logger.info("getTbPmUpdateFailedBidsQuery save db result>>>"+dbr);
	                if(dbr<0)
	                	retMsg ="Update bidfailed Status failed for rowno: "+ String.valueOf(rowIndex);
	            }
	        	
	        }
	        else if (rateType.equalsIgnoreCase(rateTypeBank)){
	            if (checkBidStatus(bankRate,cbnRate)){
	            	logger.info("1");
	            	//update gridview
	            	ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,6,gridcbnRate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,7,gridbankRate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,11,maturityDate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,9,newallocation);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,14,bidSuccess);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,13,statusAwaitingMaturity);
	                
	            	//update db
	            	String qry = new Query().getTbPmBidUpdateBankQuery(wino, cbnRate, bankRate, maturityDate, allocation, bidSuccess, statusAwaitingMaturity);
	            	logger.info("getTbPmBidUpdateBankQuery>>"+ qry);
	                int dbr = new DbConnect(ifr, qry).saveQuery();
	                logger.info("getTbPmBidUpdateBankQuery save db result>>>"+dbr);
	                if(dbr<0)
	                	retMsg ="Update failed";
	            }
	            else {
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,14,bidFailed);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,13,"");
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,6,gridcbnRate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,7,gridbankRate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,11,maturityDate);
	                ifr.setTableCellValue(tbPriBidCustRqstTable,rowIndex,9,newallocation);
	                
	                String qry = new Query().getTbPmUpdateFailedBidsQuery(wino,bidFailed,cbnRate,bankRate);
	            	logger.info("getTbPmUpdateFailedBidsQuery>>"+ qry);
	                int dbr = new DbConnect(ifr, qry).saveQuery();
	                logger.info("getTbPmUpdateFailedBidsQuery save db result>>>"+dbr);
	                if(dbr<0)
	                	retMsg ="Update failed";
	            }
	        }
        }
        //get total bid...
        //tbViewPriCustomerBids(ifr, rowIndex); // last resort if bid status is not updating.......
        setTbPmtotalBidAllAmount(ifr,getTbMarketUniqueRefId(ifr));
        return retMsg;   
    }
    
    //download primary bid report
    private String tbGetPmGrid(IFormReference ifr, int rowCount){
        StringBuilder output = new StringBuilder(empty);
        for (int i = 0; i < rowCount; i++){
        	String reqstType =  ifr.getTableCellValue(tbPriBidReportTable,i,0);
	        String tenor =  ifr.getTableCellValue(tbPriBidReportTable,i,1);
	        String rate =  ifr.getTableCellValue(tbPriBidReportTable,i,2);
	        String totalAmount =  ifr.getTableCellValue(tbPriBidReportTable,i,3);
	        String rateType =  ifr.getTableCellValue(tbPriBidReportTable,i,4);
	        String count =  ifr.getTableCellValue(tbPriBidReportTable,i,5);
	        String status =  ifr.getTableCellValue(tbPriBidReportTable,i,6);
	        output.append("{\"reqstType\": \"").append(reqstType).append("\", \"tenor\": \"").append(tenor).append("\", \"rate\": \"").append(rate).append("\", \"totalAmount\": \"").append(totalAmount).append("\", \"rateType\": \"").append(rateType).append("\", \"count\": \"").append(count).append("\", \"status\": \"").append(status).append("\"}$");
	      }
        logger.info("output from grid: "+output.toString());
        return output.toString().trim();
    }
    
    private String tbCheckVerificationAmt(IFormReference ifr) {
    	String retMsg ="";
    	if(!isEmpty(getTbVerificationAmttbx(ifr))) {
        	try {
        		retMsg = Double.parseDouble(getTbVerificationAmttbx(ifr))<tbSmMinVerificationAmt ? "Amount cannot be less than 100,000":"";
        		setTbVerificationAmttbx(ifr,String.valueOf(tbSmMinVerificationAmt));
        	}
        	catch(Exception ex) {
        		retMsg = "Invalid Amount entered";
        		logger.info("tbCheckVerificationAmt Exception>>>" + ex.toString());
        		setTbVerificationAmttbx(ifr,String.valueOf(tbSmMinVerificationAmt));
        	}
    	}
    	return retMsg;
    }
    private String tbUpdateSmIssuedBids(IFormReference ifr, int rowIndex) {
    	logger.info("tbUpdateSmIssuedBids-- ");
    	String retMsg="";
    	//check if fields are empty...
    	
    	String maturityDate = getFieldValue(ifr,tbUpdteSmMatDte); 
    	logger.info("maturityDate-- "+maturityDate);
    	double tBillAmount = convertStringToDouble(getFieldValue(ifr,tbUpdteSmTBillsAmt));
    	logger.info("tBillAmount>>>"+tBillAmount);
    	double veriAmt = convertStringToDouble(getFieldValue(ifr,tbVerificationAmtttbx));
    	logger.info("veriAmt>>>"+veriAmt);
    	
    	/* try {
    		 tBillAmount = Double.parseDouble(getFieldValue(ifr,tbUpdteSmTBillsAmt));
    		 logger.info("tBillAmount>>>"+tBillAmount);
         }
         catch(Exception ex) {
         	logger.info("tBillAmount parse error>>>"+ex.toString());
         }
    	 double veriAmt = 0;
    	 try {
    		 veriAmt = Double.parseDouble(getFieldValue(ifr,tbVerificationAmtttbx));
    		 logger.info("veriAmt>>>"+veriAmt);
         }
         catch(Exception ex) {
         	logger.info("veriAmt parse error>>>"+ex.toString());
         }*/
    	String rate =getFieldValue(ifr,tbUpdteSmRate);
        String staus = ifr.getTableCellValue(tbSmIssuedTBillTbl,rowIndex,6);
        String totalAmountSold = ifr.getTableCellValue(tbSmIssuedTBillTbl,rowIndex,4);
        logger.info("totalAmountSold>>>"+totalAmountSold);
       
        //long dayToMaturity =  getDaysToMaturity(maturityDate);
        double amtsold = convertStringToDouble(getFieldValue(ifr,totalAmountSold));
        /*double amtsold =0;
        try {
        	amtsold = Double.parseDouble(totalAmountSold);
        	logger.info("amtsold>>>"+amtsold);
        }
        catch(Exception ex) {
        	logger.info("amtsold parse error>>>"+ex.toString());
        }*/
        /*double availableAmount = 0;
        try{
        	availableAmount= Double.parseDouble(ifr.getTableCellValue(tbSmIssuedTBillTbl,rowIndex,5));
        }
        catch(Exception ex) {
        	logger.info("availableAmount parse error>>>"+ex.toString());
        }
        logger.info("availableAmount>>>"+availableAmount);
        */
    	
        if(!staus.equalsIgnoreCase(smStatusOpen)) { //open and can be updated
        	if (tBillAmount < veriAmt) {
        		retMsg = "Treaury Bill Amount cannot be less than the Verification Amount. on row No. "+rowIndex+".";
        	}
        	else if (tBillAmount < amtsold) { //
        		retMsg = "Treaury Bill Amount cannot be less than total Amount Sold. on row No. "+rowIndex+".";
        	}
        		
        	else {//update table 
        		if(!isEmpty(maturityDate))
        			ifr.setTableCellValue(tbSmIssuedTBillTbl,rowIndex,0,maturityDate);
        		if(!isEmpty(getFieldValue(ifr,tbUpdteSmTBillsAmt)))
        			ifr.setTableCellValue(tbSmIssuedTBillTbl,rowIndex,1,convertDoubleToString(tBillAmount));
        		if(!isEmpty(getFieldValue(ifr,tbUpdteSmTBillsAmt)))
        			ifr.setTableCellValue(tbSmIssuedTBillTbl,rowIndex,5,(convertDoubleToString(tBillAmount-amtsold)));
        		if(!isEmpty(rate))
        			ifr.setTableCellValue(tbSmIssuedTBillTbl,rowIndex,2,rate);
                  
        	}
        }
        else //not opened
        	retMsg = "Sorry this is closed and cannot be updated";
        	

        return retMsg;
    	
    }
    
    //get the total bid allocation amount
    private double tbTotalBidAlltAmt(IFormReference ifr,String refid) {
    	String tAlQry = new Query().getTbPmTotalAllctnAmtQuery(refid);
    	logger.info("getTbPmTotalAllctnAmtQuery>>"+tAlQry);
    	List<List<String>> tAlDbr = new DbConnect(ifr,tAlQry).getData();
    	if(tAlDbr.size()>0) {
    		return convertStringToDouble(tAlDbr.get(0).get(0));
    	}
    	return 0;
    }
    
    //set primary total bid allocation amount
    private void setTbPmtotalBidAllAmount(IFormReference ifr,String refid) {
   	 Double totalBidAmt = tbTotalBidAlltAmt(ifr,refid);
   	 logger.info("totalBidAmt>>>>"+totalBidAmt);
        setFields(ifr,tbPmTotalAllocationAmt,String.valueOf(totalBidAmt));
      
   }
    
    /******************  TREASURY BILL CODE ENDS *********************************/
}
