package com.newgen.utils;

import com.newgen.iforms.custom.IFormReference;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.text.ParseException;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Random;

public class Commons implements Constants {
    private static final Logger logger = LogGen.getLoggerInstance(Commons.class);

    /************************* COMMERCIAL PAPER CODE BEGINS **************************/
    private String getTat (String entryDate, String exitDate){
        SimpleDateFormat sdf = new SimpleDateFormat(dbDateTimeFormat);
        try {
            Date d1 = sdf.parse(entryDate);
            Date d2 = sdf.parse(exitDate);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            logger.info("getTat method -- tat-- "+ difference_In_Days + " days, " + difference_In_Hours + " hrs, " + difference_In_Minutes + " mins, " + difference_In_Seconds + " sec");

            return  difference_In_Days + " days, " + difference_In_Hours + " hrs, " + difference_In_Minutes + " min, " + difference_In_Seconds + " sec";
        }
        catch (ParseException e) {
            e.printStackTrace();
            logger.error("Exception occurred in getTat method-- "+ e.getMessage());
        }
        return null;
    }
    private static void setDecisionHistory(IFormReference ifr, String staffId, String process, String marketType, String decision,
                                    String remarks, String prevWs, String entryDate, String exitDate, String tat){
        JSONArray jsRowArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(dhRowStaffId,staffId);
        jsonObject.put(dhRowProcess,process);
        jsonObject.put(dhRowMarketType,marketType);
        jsonObject.put(dhRowDecision,decision);
        jsonObject.put(dhRowRemarks,remarks);
        jsonObject.put(dhRowPrevWs,prevWs);
        jsonObject.put(dhRowEntryDate,entryDate);
        jsonObject.put(dhRowExitDate,exitDate);
        jsonObject.put(dhRowTat,tat);
        jsRowArray.add(jsonObject);

        ifr.addDataToGrid(decisionHisTable, jsRowArray);
    }
    public String getUsersMailsInGroup(IFormReference ifr, String groupName){
        String groupMail= "";
        try {
            DbConnect dbConnect = new DbConnect(ifr, new Query().getUsersInGroup(groupName));
            for (int i = 0; i < dbConnect.getData().size(); i++){
                groupMail = dbConnect.getData().get(i).get(0)+endMail+","+groupMail; }
        } catch (Exception e){
            logger.error("Exception occurred in getUsersMailInGroup Method-- "+ e.getMessage());
            return null;
        }
        logger.info("getUsersMailsGroup method --mail of users-- "+groupMail.trim());
        return groupMail.trim();
    }
    public void setCpDecisionHistory (IFormReference ifr){
        String marketType = getCpMarketName(ifr);
        String remarks = (String)ifr.getValue(cpRemarksLocal);
        String entryDate = (String)ifr.getValue(entryDateLocal);
        String exitDate = getCurrentDateTime();
        setDecisionHistory(ifr,getLoginUser(ifr),commercialProcessName,marketType,getCpDecision(ifr),remarks,getActivityName(ifr),entryDate,exitDate,getTat(entryDate,exitDate));
        ifr.setValue(decHisFlagLocal,flag);
    }
    public String getCpMarket(IFormReference ifr){return  getFieldValue(ifr,cpSelectMarketLocal);}
    public String getProcess(IFormReference ifr){
        return getFieldValue(ifr,selectProcessLocal);
    }
    public String getCurrentDateTime (String format){
        return new SimpleDateFormat(format).format(new Date());
    }
    public String getCurrentDateTime (){
        return new SimpleDateFormat(dbDateTimeFormat).format(new Date());
    }
    public String getCpDecision (IFormReference ifr){ return getFieldValue(ifr,cpDecisionLocal);}
    public String getWorkItemNumber (IFormReference ifr){
        return (String)ifr.getControlValue(wiNameLocal);
    }
    public String getLoginUser(IFormReference ifr){
        return ifr.getUserName();
    }
    public String getActivityName (IFormReference ifr){
        return ifr.getActivityName();
    }
    public String getPrevWs (IFormReference ifr){return getFieldValue(ifr,prevWsLocal);}
    public void setGenDetails(IFormReference ifr){
        setFields(ifr,new String[]{solLocal,loginUserLocal}, new String[]{getSol(ifr),getLoginUser(ifr)});
    }
    public void hideProcess(IFormReference ifr){
        ifr.setTabStyle(processTabName,commercialTab,visible,False);
        ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        ifr.setTabStyle(processTabName,omoTab,visible,False);
    }
    public void hideShowDashBoardTab(IFormReference ifr,String state){ifr.setTabStyle(processTabName,dashboardTab,visible,state);}
    public void selectProcessSheet(IFormReference ifr){
        hideShowBackToDashboard(ifr,True);
        hideShowDashBoardTab(ifr, False);
        if(getProcess(ifr).equalsIgnoreCase(commercialProcess)) {
            ifr.setTabStyle(processTabName, commercialTab, visible, True);
            ifr.setTabStyle(processTabName, treasuryTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
            ifr.setTabStyle(processTabName, treasuryTab, visible, True);
            ifr.setTabStyle(processTabName, commercialTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(omoProcess)){
            ifr.setTabStyle(processTabName,omoTab,visible,True);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        }
        else {
            ifr.setTabStyle(processTabName,omoTab,visible,False);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
            hideShowBackToDashboard(ifr,False);
            hideShowDashBoardTab(ifr, True);
        }
    }
    public void hideShowBackToDashboard(IFormReference ifr, String state){
        hideShow(ifr,new String[]{goBackDashboardSection},state);
    }
    public void showSelectedProcessSheet(IFormReference ifr){
        logger.info("showSelectedProcessMethod -- selected process -- "+getProcess(ifr));
        hideShowDashBoardTab(ifr,False);
        if(getProcess(ifr).equalsIgnoreCase(commercialProcess)) {
            ifr.setTabStyle(processTabName, commercialTab, visible, True);
            ifr.setTabStyle(processTabName, treasuryTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) {
            ifr.setTabStyle(processTabName, treasuryTab, visible, True);
            ifr.setTabStyle(processTabName, commercialTab, visible, False);
            ifr.setTabStyle(processTabName, omoTab, visible, False);
        }
        else if (getProcess(ifr).equalsIgnoreCase(omoProcess)){
            ifr.setTabStyle(processTabName,omoTab,visible,True);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        }
    }
    public String getSol (IFormReference ifr){
        try { return new DbConnect(ifr, new Query().getSolQuery(getLoginUser(ifr))).getData().get(0).get(0); }
        catch (Exception e){ logger.error("Exception occurred in getSol Method-- "+e.getMessage());return  null;}
    }
    public void hideCpSections (IFormReference ifr){
        setInvisible(ifr,new String []{cpBranchPriSection,cpBranchSecSection,cpLandingMsgSection,cpMarketSection,cpPrimaryBidSection,cpProofOfInvestSection,
        cpTerminationSection,cpCutOffTimeSection,cpDecisionSection,cpTreasuryPriSection,cpTreasurySecSection,cpTreasuryOpsPriSection,cpTreasuryOpsSecSection,cpPostSection,cpSetupSection,cpCustomerDetailsSection});
    }
    public void disableCpSections (IFormReference ifr){
        disableFields(ifr,new String []{cpBranchPriSection,cpBranchSecSection,cpLandingMsgSection,cpMarketSection,cpPrimaryBidSection,cpProofOfInvestSection, cpTerminationSection,
                cpDecisionSection,cpCutOffTimeSection,cpTreasuryPriSection,cpTreasurySecSection,cpTreasuryOpsPriSection,cpTreasuryOpsSecSection,cpPostSection,cpSetupSection});
    }
    public void hideShowLandingMessageLabel(IFormReference ifr,String state){ifr.setStyle(landMsgLabelLocal,visible,state);}
    public void hideShow (IFormReference ifr, String[] fields,String state) { for(String field: fields) ifr.setStyle(field,visible,state);}
    public static void setFields (IFormReference ifr, String [] locals,String [] values){
        for (int i = 0; i < locals.length; i++) ifr.setValue(locals[i], values[i]);
    }
    public static void setFields (IFormReference ifr, String local,String value){
        ifr.setValue(local,value);
    }
    public void setDropDown (IFormReference ifr,String local, String [] values){
        ifr.clearCombo(local);
        for (String value: values) ifr.addItemInCombo(local,value,value);
        clearFields(ifr,local);
    }
    public void setDropDown (IFormReference ifr,String local,String [] labels, String [] values){
        ifr.clearCombo(local);
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(local,labels[i],values[i]);
    }
    public void setDecision (IFormReference ifr,String decisionLocal, String [] values){
        ifr.clearCombo(decisionLocal);
        for (String value: values) ifr.addItemInCombo(decisionLocal,value,value);
        clearFields(ifr,new String[]{decisionLocal});
    }
    public void setDecision (IFormReference ifr,String decisionLocal,String [] labels, String [] values){
        ifr.clearCombo(decisionLocal);
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(decisionLocal,labels[i],values[i]);
        clearFields(ifr,new String[]{decisionLocal});
    }
    public void addToDropDown (IFormReference ifr,String local,String [] labels, String [] values){
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(local,labels[i],values[i]);
    }
    public static void disableFields(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setStyle(field,disable, True); }
    public static void clearFields(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setValue(field, empty); }
    public static void setVisible(IFormReference ifr, String[] fields) { for(String field: fields) ifr.setStyle(field,visible, True);}
    public static void hideShowFields(IFormReference ifr, String[] fields, String [] states) {for (int i = 0; i < fields.length; i++) ifr.setStyle(fields[i],visible,states[i]); }
    public static void setInvisible(IFormReference ifr, String [] fields ) { for(String field: fields) ifr.setStyle(field,visible, False); }
    public static void setInvisible(IFormReference ifr, String field ) { ifr.setStyle(field,visible, False); }
    public static void enableFields(IFormReference ifr, String [] fields) {for(String field: fields) ifr.setStyle(field,disable, False);}
    public static void setMandatory(IFormReference ifr, String []fields) { for(String field: fields) ifr.setStyle(field,mandatory, True);}
    public static void undoMandatory(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setStyle(field,mandatory, False); }
    public static void clearTables(IFormReference ifr, String [] tables){for (String table: tables) ifr.clearTable(table);}
    public static String getFieldValue(IFormReference ifr, String local){return ifr.getValue(local).toString();}
    public boolean isEmpty(String s) {return s == null || s.trim().isEmpty();}
    public void backToDashboard(IFormReference ifr){
        hideProcess(ifr);
        hideShowDashBoardTab(ifr,True);
        hideShowBackToDashboard(ifr,False);
    }
    public void clearDecHisFlag (IFormReference ifr){clearFields(ifr,new String[]{decHisFlagLocal});}
    public String getSetupFlag(IFormReference ifr){return getFieldValue(ifr,windowSetupFlagLocal);}
    public void setCpCategory(IFormReference ifr, String [] values){
        ifr.clearCombo(cpCategoryLocal);
        for (String value: values)
            ifr.addItemInCombo(cpCategoryLocal,value,value);
    }
    public String getCpCategory(IFormReference ifr){return getFieldValue(ifr,cpCategoryLocal);}
    public void cpSetDecisionValue (IFormReference ifr, String value){ifr.setValue(cpDecisionLocal,value);}
    public String getCpUpdateMsg (IFormReference ifr){return getFieldValue(ifr,cpUpdateLocal);}
    public String getCpMarketName (IFormReference ifr){
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) return primary;
        else if (getProcess(ifr).equalsIgnoreCase(cpSecondaryMarket)) return secondary;
        return empty;
    }
    public boolean compareDate(String startDate, String endDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dbDateTimeFormat);
        try {
             Date start = simpleDateFormat.parse(startDate);
             Date end = simpleDateFormat.parse(endDate);
             if (start.compareTo(end) < 0)
                 return true;
        } catch (Exception e){logger.error("Exception occurred in compareDate method-- "+ e.getMessage());return false;}
        return false;
    }
    public String getCpOpenDate(IFormReference ifr){return getFieldValue(ifr,cpOpenDateLocal);}
    public String getCpCloseDate(IFormReference ifr){return getFieldValue(ifr,cpCloseDateLocal);}
    public String getCpPmWinRefNo(IFormReference ifr){
        return null;
    }
    public String getCpSmWinRefNo(IFormReference ifr){
        return null;
    }
    public String getCpPmCusRefNo(IFormReference ifr){return null;}
    public String getCpSmCusRefNo(IFormReference ifr){
        return null;
    }
    public String getCpLandingMsg (IFormReference ifr){return getFieldValue(ifr,cpLandMsgLocal);}
    public String getPmMinPrincipal(IFormReference ifr){return getFieldValue(ifr,cpPmMinPriAmtLocal);}
    public String cpSetUpPrimaryMarketWindow(IFormReference ifr){
        String winRefNo =  generateCpWinRefNo(cpPmLabel);
        logger.info("pmWinRefNo--"+ winRefNo);
        logger.info("Query for setup-- "+ new Query().getSetupMarketWindowQuery(winRefNo,getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpLandingMsg(ifr),getPmMinPrincipal(ifr),empty,empty,empty,empty,getCpOpenDate(ifr),getCpCloseDate(ifr)));
       int validate = new DbConnect(ifr,new Query().getSetupMarketWindowQuery(winRefNo,getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpLandingMsg(ifr),getPmMinPrincipal(ifr),empty,empty,empty,empty,getCpOpenDate(ifr),getCpCloseDate(ifr))).saveQuery();
       if (validate >= 0) {
           setFields(ifr,new String[]{cpPmWinRefNoLocal,windowSetupFlagLocal},new String[]{winRefNo,flag});
           logger.info("record saved just for checking");
           return "success";
       }
       else return "Unable to setup window try again later";
    }
    public void cpSetUpSecondaryMarketWindow(IFormReference ifr){
        String winRefNo =  generateCpWinRefNo(cpSmLabel);
       int validate = new DbConnect(ifr,new Query().getSetupMarketWindowQuery(winRefNo,getWorkItemNumber(ifr),commercialProcessName,secondary,getCpLandingMsg(ifr),getPmMinPrincipal(ifr),empty,empty,empty,empty,getCpOpenDate(ifr),getCpCloseDate(ifr))).saveQuery();
       if (validate >= 0) {
           setFields(ifr,new String[]{cpPmWinRefNoLocal,windowSetupFlagLocal},new String[]{winRefNo,flag});
           logger.info("record saved just for checking");
       }
    }
    private String generateCpWinRefNo(String cpLabel) {
        return cpLabel + new SimpleDateFormat(cpRefNoDateFormat).format(new Date());
    }
    public String getWindowSetupFlag (IFormReference ifr){return getFieldValue(ifr,windowSetupFlagLocal);}
    public String getCurrentWorkStep(IFormReference ifr){
        return ifr.getActivityName();
    }
    public Boolean isCpWindowActive(IFormReference ifr){
        logger.info("check window query --"+ new Query().getCheckActiveWindowQuery(commercialProcessName,getCpMarket(ifr)));
        return Integer.parseInt(new DbConnect(ifr,
                new Query().getCheckActiveWindowQuery(commercialProcessName,getCpMarket(ifr))).getData().get(0).get(0)) > 0;
    }
    public void setCpPmWindowDetails (IFormReference ifr){
        DbConnect dbConnect = new DbConnect(ifr,new Query().getActiveWindowDetailsQuery(commercialProcessName,getCpMarket(ifr)));
        setFields(ifr, new String[]{cpPmWinRefNoBranchLocal,landMsgLabelLocal,cpPmMinPriAmtBranchLocal},
                new String[]{dbConnect.getData().get(0).get(0),dbConnect.getData().get(0).get(1),dbConnect.getData().get(0).get(2)});
    }
    public static String getCpPmRateType (IFormReference ifr){
        return getFieldValue(ifr,cpPmRateTypeLocal);
    }
    public static float getCpPmWinPrincipalAmt(IFormReference ifr){return Float.parseFloat(getFieldValue(ifr,cpPmMinPriAmtBranchLocal));}
    public static float getCpPmCustomerPrincipal(IFormReference ifr){return Float.parseFloat(getFieldValue(ifr,cpPmPrincipalLocal));}
    public static int getCpPmTenor (IFormReference ifr){
        return Integer.parseInt(getFieldValue(ifr,cpPmTenorLocal));
    }
    public String cpGenerateCustomerId (IFormReference ifr){
        return cpIdLabel+getUserSol(ifr)+getCpRandomId();
    }
    private String getCpRandomId(){
        Random random = new Random();
        return String.valueOf(10000000000L + ((long)random.nextInt(900000000)*100) + random.nextInt(100));
    }
    public String getUserSol(IFormReference ifr){return getFieldValue(ifr,solLocal);}
    public Boolean cpCheckWindowStateById(IFormReference ifr){
        return Integer.parseInt(new DbConnect(ifr,
                new Query().getCheckActiveWindowByIdQuery(getCpPmWinRefNo(ifr))).getData().get(0).get(0)) > 0;
    }




    /************************* COMMERCIAL PAPER CODE ENDS **************************/

    /******************  TREASURY BILL CODE BEGINS *********************************/
    public void hideTbSections (IFormReference ifr){hideFields(ifr,allTbSections);}
    public void disableTbSections (IFormReference ifr){disableFields(ifr,allTbSections);}
    public void disableField(IFormReference ifr, String field) {ifr.setStyle(field,disable,True);}
    public void clearFields(IFormReference ifr, String field) {ifr.setValue(field,empty);}
    public void setVisible(IFormReference ifr, String field) { ifr.setStyle(field,visible,True);}
    public void hideField(IFormReference ifr, String field) {ifr.setStyle(field,visible,False);}
    public void hideFields(IFormReference ifr, String [] fields ) { for(String field: fields) ifr.setStyle(field,visible,False); }
    public void enableField(IFormReference ifr, String field) {ifr.setStyle(field,disable,False);}
    public void setMandatory(IFormReference ifr, String field) { ifr.setStyle(field,mandatory,True); }
    public void undoMandatory(IFormReference ifr, String field) { ifr.setStyle(field,mandatory,False); }
    public String getTbMarketName (IFormReference ifr){
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) 
        	return primary;
        else if (getProcess(ifr).equalsIgnoreCase(tbSecondaryMarket)) 
        	return secondary;
        return null;
    }
    public String getTbMarket(IFormReference ifr){
    	return  (String) ifr.getValue(tbSelectMarketLocal);
    }
    public void setTbDecisionHistory (IFormReference ifr){
        String marketType = getCpMarketName(ifr);
        String remarks = (String)ifr.getValue(tbRemarksLocal);
        String entryDate = (String)ifr.getValue(entryDateLocal);
        String exitDate = getCurrentDateTime();
        setDecisionHistory(ifr,getLoginUser(ifr),treasuryProcessName,marketType,getCpDecision(ifr),remarks,getActivityName(ifr),entryDate,exitDate,getTat(entryDate,exitDate));
        ifr.setValue(decHisFlagLocal,flag);
    }
    public void porpulateCombo(IFormReference ifr, String comboCntrlName, String [] values){
        ifr.clearCombo(comboCntrlName);
        for (String value: values)
            ifr.addItemInCombo(comboCntrlName,value,value);
    }
    public boolean getTbUpdateLocal(IFormReference ifr){ return (boolean) ifr.getValue(tbUpdateLocal); }
    public String getTbDecision (IFormReference ifr){return (String) ifr.getValue(tbDecisionLocal);}
    public String getTbCategory(IFormReference ifr){return (String) ifr.getValue(tbCategoryLocal);}
    public String getTbUpdateMsg (IFormReference ifr){return (String) ifr.getValue(tbUpdateLocal);}
    public void setTbDecisionValue (IFormReference ifr, String value){ifr.setValue(tbDecisionLocal,value);}
    public String getTbOpenDate(IFormReference ifr){return (String)ifr.getValue(tbOpenDateLocal);}
    public String getTbCloseDate(IFormReference ifr){return (String)ifr.getValue(tbCloseDateLocal);}
    public String getTbUniqueRef(IFormReference ifr){return (String)ifr.getValue(tbUniqueRef);}
    public String getDateWithoutTime() {
    	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
    	try {
    		return formatter.format(new Date());
    	}
    	catch (Exception e) {
			logger.info("getDateWithoutTime Exception>>>"+e.toString());
			return null;
		}
	}
    
    //testing branch
    
    
    
    /******************  TREASURY BILL CODE ENDS ***********************************/
}
