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
                        case cpSetupWindowEvent:{ return setupCpWindow(ifr);}
                        
                        /**** Treasury onClick Start ****/
                        case tbOnClickUpdateMsg:{tbUpdateLandingMsg(ifr);}
                        break;
                        case tbSetupMarket:{ return tbSetupMarket(ifr);}
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
                            if (getCpSmSetup(ifr).equalsIgnoreCase(smSetupNew) || getCpSmSetup(ifr).equalsIgnoreCase(smSetupUpdate))
                                setVisible(ifr,new String[]{cpSmCpBidTbl, cpSmIFrameLocal});
                            else setInvisible(ifr,new String[]{cpSmCpBidTbl, cpSmIFrameLocal});

                            break;
                        }

                        /**** Treasury Onchange Start ****/
                        case tbCategoryddChange:{
                        	tbCategoryChange(ifr);
                        }
                        break;
                        /**** Treasury Onchange End  ****/
                    }
                }
                break;
                case custom:{
                    switch (controlName){
                        case cpGetPmGridEvent:{
                        return getCpPmBidGrid(ifr,Integer.parseInt(data));
                        }
                    }
                }
                break;
                case onDone:{
                	switch (controlName){
                        case cpSmCpUpdateEvent:{return updateCpSmDetails(ifr,Integer.parseInt(data));}
                	
	                /**** Treasury onDOne Start ****/
                	case tbOnDone:{
                		return tbOnDone(ifr);
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
    public void cpSendMail(IFormReference iFormReference) {

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
        }
         else if(getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerMaker))  {
             if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                 if (!isEmpty(getWindowSetupFlag(ifr))){
                     setVisible(ifr,new String[]{cpMarketSection,cpTreasuryPriSection,cpCategoryLocal,cpDecisionSection});
                     setMandatory(ifr,new String[] {cpCategoryLocal,cpDecisionLocal,cpRemarksLocal});
                     setCpCategory(ifr,new String[]{cpCategoryUpdateLandingMsg,cpCategoryReDiscountRate,cpCategoryModifyCutOffTime});
                 }
             }
             else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){}
         }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        clearFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal});
        setDecision(ifr,cpDecisionLocal,new String [] {decSubmit,decDiscard});
    }

    private String setupCpWindow (IFormReference ifr){
        if (isEmpty(getSetupFlag(ifr))){
            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
                if (!compareDate(getCpOpenDate(ifr),getCpCloseDate(ifr))) return cpSetupPrimaryMarketWindow(ifr);
                else return "Close date cannot be before Open date.";
            }
            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)){
                return empty;
            }
        }
        return empty;
    }
    private void cpUpdateLandingMsg(IFormReference ifr){
        if (getCpUpdateMsg(ifr).equalsIgnoreCase(True)){
            cpSetDecisionValue(ifr,decSubmit);
            ifr.setValue(cpRemarksLocal,"Kindly approve landing message update.");
            setInvisible(ifr, new String[]{cpSetupSection,cpDecisionSection});
            undoMandatory(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
            setMandatory(ifr,new String[]{cpLandMsgLocal});
            enableFields(ifr,new String[]{cpLandMsgLocal,cpLandingMsgSubmitBtn});
            setVisible(ifr,new String[]{cpLandingMsgSubmitBtn});
        }
        else {
            clearFields(ifr, new String[]{cpDecisionLocal,cpRemarksLocal,cpLandMsgLocal});
            setVisible(ifr, new String[]{cpSetupSection,cpDecisionSection});
            setMandatory(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});
            undoMandatory(ifr,new String[]{cpLandMsgLocal});
            disableFields(ifr,new String[]{cpLandMsgLocal});
        }
    }
    private void cpSelectCategory(IFormReference ifr){
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategorySetup)){
                setVisible(ifr, new String [] {cpTreasuryPriSection,cpSetupSection,cpSetupWindowBtn,cpCutOffTimeSection});
                setMandatory(ifr,new String[] {cpOpenDateLocal, cpPmMinPriAmtLocal,cpCloseDateLocal});
                enableFields(ifr,new String[] {cpPmMinPriAmtLocal,cpCloseDateLocal,cpSetupWindowBtn});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryModifyCutOffTime)){
                setVisible(ifr,new String[]{cpCutOffTimeSection});
                enableFields(ifr,new String[] {cpCloseDateLocal});
                undoMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate90To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpRediscountRateSection,cpLandingMsgSection});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryReDiscountRate)){
                setVisible(ifr,new String[]{cpRediscountRateSection});
                setMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate90To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                enableFields(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate90To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpCutOffTimeSection,cpLandingMsgSection});
            }
            else if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryUpdateLandingMsg)){
                setVisible(ifr,new String[]{cpLandingMsgSection});
                enableFields(ifr,new String[]{cpLandMsgLocal});
                setMandatory(ifr,new String[]{cpLandMsgLocal});
                undoMandatory(ifr,new String[] {cpReDiscountRateLess90Local, cpReDiscountRate90To180Local,cpReDiscountRate181To270Local,cpReDiscountRate271To364Local});
                setInvisible(ifr,new String[]{cpRediscountRateSection,cpCutOffTimeSection});
            }
            else {
                setInvisible(ifr,new String[]{cpRediscountRateSection,cpCutOffTimeSection,cpLandingMsgSection});
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

            ifr.setTableCellValue(cpSmCpBidTbl,i,6,String.valueOf(dayToMaturity));
            ifr.setTableCellValue(cpSmCpBidTbl,i,7,smStatusOpen);
        }
        setFields(ifr,new String[]{cpSmWinRefLocal}, new String[]{generateCpWinRefNo(cpSmLabel)});
        return empty;
    }


    
    /******************  TREASURY BILL CODE BEGINS *********************************/
    /*hide all sections except market, decision and lnading message
     * disable landing msg section
     */
    private void tbMarketTypeOnChange() {
    	
    }
    private void tbFormLoad(IFormReference ifr) {
    	setGenDetails(ifr);
    	hideTbSections(ifr);
        hideFields(ifr, new String[] {goBackDashboardSection,tbPriSetupbtn});
        //disableTbSections(ifr);
        setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
    	setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup});
    	clearFields(ifr,new String[]{tbRemarkstbx});
    	
        //tb primary Market
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) {
        	if(getTbLandingMsgApprovedFlg(ifr).equalsIgnoreCase(yesFlag)){ // ready to set Market(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbTreasuryPriSetupSection});
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection,tbPriSetupSection,tbCategorydd});
        		enableFields(ifr,new String[]{tbUpdateLandingMsgcbx,tbDecisionSection,tbMarketSection,tbCategorydd});
        		setMandatory(ifr,new String [] {tbCategorydd,tbDecisiondd,tbRemarkstbx,tbPriOpenDate,tbPriCloseDate,tbCategorydd});
                disableFields(ifr, new String[]{tbLandingMsgSection,tbUniqueReftbx,tbMarketTypedd});
        	}
        	else { //landing msg is not approved
        		clearDropDown(ifr,tbCategorydd);
        		setVisible(ifr,new String [] {tbLandingMsgSection,tbDecisionSection,tbMarketSection});
                enableFields(ifr,new String[] {tbLandingMsgSection,tbDecisionSection,tbLandingMsgSection});
                setMandatory(ifr,new String [] {tbDecisiondd,tbRemarkstbx,tbLandMsgtbx});
                setTbUpdateLandingMsg(ifr,True);
                disableFields(ifr, new String[]{tbMarketTypedd,tbCategorydd});
        	}
        } 
        
        //tb secondary market
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)) {
        }
    }
    /*
     * automatically populate primary market window unique reference in the below 
     * format TPMADATEYEAR for example TBPMA28052020 which will not be editable
     */
    private void tbCategoryChange(IFormReference ifr) throws ParseException{
    	//primary Market
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
            if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){ 
            	setTbUniqueRef(ifr,generateTbUniqueReference(ifr)); //set the unique reference
            	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
            	hideField(ifr,tbDecisionSection);
            }
            else {
            	setDropDown(ifr,tbDecisiondd,new String[]{decSubmit,decDiscard});
            	setVisible(ifr,tbDecisionSection);
            	setDropDown(ifr,tbCategorydd,new String[]{tbCategorySetup});
            }
        }
        
        //secondary MArket
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){}
    }
    private void tbUpdateLandingMsg(IFormReference ifr){
        if (getTbUpdateLandingMsg(ifr)){ //true
        	clearDropDown(ifr,tbCategorydd);
            clearFields(ifr, new String[]{tbUniqueReftbx,tbPriOpenDate,tbPriCloseDate});
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
            disableFields(ifr, new String[]{tbLandingMsgSection,tbUniqueReftbx,tbMarketTypedd});
        }
    }
    
    /*
     * save refid, opendate and close date into db
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
    private String tbOnDone(IFormReference ifr) {
    	logger.info("tbOnDone>>");
    	String retMsg="";
    	//if(getTbDecision(ifr).equalsIgnoreCase(decSubmit)) {
    		if (getTbCategorydd(ifr).equalsIgnoreCase(tbCategorySetup)){
    		//	retMsg = validateDate(ifr);
    			if(isEmpty(retMsg)) {
	    			if(!(getTbSetUpFlg(ifr).equalsIgnoreCase(flag))) //market not set
	    				retMsg = setUpTbMarketWindow(ifr);// set market
	    		}
    		}
    	//}
    	logger.info("Validate retMsg>>"+retMsg);
    	return retMsg;
    }
    
    /******************  TREASURY BILL CODE ENDS *********************************/
}
