package com.newgen.utils;

import com.newgen.iforms.custom.IFormReference;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class Commons implements Constants {
    private static final Logger logger = LogGen.getLoggerInstance(Commons.class);
    public List<List<String>> resultSet;

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
        StringBuilder groupMail= new StringBuilder();
        try {
            resultSet = new DbConnect(ifr, new Query().getUsersInGroup(groupName)).getData();
            for (List<String> result : resultSet)
                groupMail.append(result.get(0)).append(endMail).append(",");
        } catch (Exception e){
            logger.error("Exception occurred in getUsersMailInGroup Method-- "+ e.getMessage());
            return null;
        }
        logger.info("getUsersMailsGroup method --mail of users-- "+ groupMail.toString().trim());
        return groupMail.toString().trim();
    }
    public void setCpDecisionHistory (IFormReference ifr){
        String marketType = getCpMarketName(ifr);
        String remarks = (String)ifr.getValue(cpRemarksLocal);
        String entryDate = (String)ifr.getValue(entryDateLocal);
        String exitDate = getCurrentDateTime();
        setDecisionHistory(ifr,getLoginUser(ifr),commercialProcessName,marketType,getCpDecision(ifr),remarks,getActivityName(ifr),entryDate,exitDate,getTat(entryDate,exitDate));
        ifr.setValue(decHisFlagLocal,flag);
    }
    public static String getCpMarket(IFormReference ifr){return  getFieldValue(ifr,cpSelectMarketLocal);}
    public static String getProcess(IFormReference ifr){
        return getFieldValue(ifr,selectProcessLocal);
    }
    public String getCurrentDateTime (String format){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
    public static String getCurrentDateTime (){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dbDateTimeFormat));
    }
    public static String  getCurrentDate (){
        return LocalDate.now().toString();
    }
    public static String getCpDecision (IFormReference ifr){ return getFieldValue(ifr,cpDecisionLocal);}
    public static String getWorkItemNumber (IFormReference ifr){
        return (String)ifr.getControlValue(wiNameLocal);
    }
    public static String getLoginUser(IFormReference ifr){
        return ifr.getUserName();
    }
    public static String getActivityName (IFormReference ifr){
        return ifr.getActivityName();
    }
    public static String getPrevWs (IFormReference ifr){return getFieldValue(ifr,prevWsLocal);}
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
            ifr.setTabStyle(processTabName,omoTab,visible,False);
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
        else {
            ifr.setTabStyle(processTabName,omoTab,visible,False);
            ifr.setTabStyle(processTabName,commercialTab,visible,False);
            ifr.setTabStyle(processTabName,treasuryTab,visible,False);
        }
    }
    public void showCommercialProcessSheet(IFormReference ifr){
        hideShowDashBoardTab(ifr,False);
        ifr.setTabStyle(processTabName,omoTab,visible,False);
        ifr.setTabStyle(processTabName,commercialTab,visible,True);
        ifr.setTabStyle(processTabName,treasuryTab,visible,False);
    }
    public static String getSol (IFormReference ifr){
        try { return new DbConnect(ifr, new Query().getSolQuery(getLoginUser(ifr))).getData().get(0).get(0); }
        catch (Exception e){ logger.error("Exception occurred in getSol Method-- "+e.getMessage());return  null;}
    }
    public void hideCpSections (IFormReference ifr){
        setInvisible(ifr,new String []{cpLienSection,cpMandateTypeSection, cpReDiscountRateSection,cpBranchPriSection,cpBranchSecSection,cpLandingMsgSection,cpMarketSection,cpPrimaryBidSection,cpProofOfInvestSection,
        cpTerminationSection,cpCutOffTimeSection,cpDecisionSection,cpTreasuryPriSection,cpTreasurySecSection,cpTreasuryOpsPriSection,cpUtilityFailedPostSection,cpPostSection,cpSetupSection,cpCustomerDetailsSection});
    }
    public void disableCpSections (IFormReference ifr){
        disableFields(ifr,new String []{cpBranchPriSection,cpBranchSecSection,cpLandingMsgSection,cpMarketSection,cpPrimaryBidSection,cpProofOfInvestSection, cpTerminationSection,
                cpCutOffTimeSection,cpTreasuryPriSection,cpTreasurySecSection,cpTreasuryOpsPriSection,cpUtilityFailedPostSection,cpPostSection});
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
    public  void setDecision (IFormReference ifr,String decisionLocal, String [] values){
        ifr.clearCombo(decisionLocal);
        for (String value: values) ifr.addItemInCombo(decisionLocal,value,value);
        clearFields(ifr,new String[]{decisionLocal});
    }
    public  void setDecision (IFormReference ifr,String decisionLocal,String [] labels, String [] values){
        ifr.clearCombo(decisionLocal);
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(decisionLocal,labels[i],values[i]);
        clearFields(ifr,new String[]{decisionLocal});
    }
    public void addToDropDown (IFormReference ifr,String local,String [] labels, String [] values){
        for (int i = 0; i < values.length; i++) ifr.addItemInCombo(local,labels[i],values[i]);
    }
    public static void disableFields(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setStyle(field,disable, True); }
    public static void disableFields(IFormReference ifr, String field) { ifr.setStyle(field,disable, True); }
    public static void clearFields(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setValue(field, empty); }
    public static void setVisible(IFormReference ifr, String[] fields) { for(String field: fields) ifr.setStyle(field,visible, True);}
    public static void setInvisible(IFormReference ifr, String [] fields ) { for(String field: fields) ifr.setStyle(field,visible, False); }
    public static void setInvisible(IFormReference ifr, String field ) { ifr.setStyle(field,visible, False); }
    public static void enableFields(IFormReference ifr, String [] fields) {for(String field: fields) ifr.setStyle(field,disable, False);}
    public static void enableFields(IFormReference ifr, String field) {ifr.setStyle(field,disable, False);}
    public static void setMandatory(IFormReference ifr, String []fields) { for(String field: fields) ifr.setStyle(field,mandatory, True);}
    public static void undoMandatory(IFormReference ifr, String [] fields) { for(String field: fields) ifr.setStyle(field,mandatory, False); }
    public static void clearTables(IFormReference ifr, String [] tables){for (String table: tables) ifr.clearTable(table);}
    public static String getFieldValue(IFormReference ifr, String local){return ifr.getValue(local).toString();}
    public static boolean isEmpty(String s) {return s == null || s.trim().isEmpty();}
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
        else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) return secondary;
        return empty;
    }
    public boolean compareDate(String startDate, String endDate){
      return  LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern(dbDateTimeFormat)).isBefore(LocalDateTime.parse(startDate,DateTimeFormatter.ofPattern(dbDateTimeFormat)));
    }
    public String getCpOpenDate(IFormReference ifr){return getFieldValue(ifr,cpOpenDateLocal);}
    public String getCpCloseDate(IFormReference ifr){return getFieldValue(ifr,cpCloseDateLocal);}
    public String getCpPmWinRefNoBr(IFormReference ifr){
        return getFieldValue(ifr,cpPmWinRefNoBranchLocal);
    }
    public String getCpPmWinRefNo(IFormReference ifr){
        return getFieldValue(ifr,cpPmWinRefNoLocal);
    }
    public String getCpSmWinRefNoBr(IFormReference ifr){
        return getFieldValue(ifr,cpSmWinRefNoBranchLocal);
    }
    public String getCpSmWinRefNo(IFormReference ifr){
        return getFieldValue(ifr,cpSmWinRefLocal);
    }
    public String getCpPmCusRefNo(IFormReference ifr){return getFieldValue(ifr,cpPmCustomerIdLocal);}
    public String getCpSmCusRefNo(IFormReference ifr){
        return getFieldValue(ifr,cpSmCustIdLocal);
    }
    public String getCpLandingMsg (IFormReference ifr){return getFieldValue(ifr,cpLandMsgLocal);}
    public String getPmMinPrincipal(IFormReference ifr){return getFieldValue(ifr,cpPmMinPriAmtLocal);}
    public String cpSetupPrimaryMarketWindow(IFormReference ifr){
        String winRefNo =  generateCpWinRefNo(cpPmLabel);
        logger.info("pmWinRefNo--"+ winRefNo);
        logger.info("Query for setup-- "+ new Query().getSetupMarketWindowQuery(winRefNo,getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpLandingMsg(ifr),getPmMinPrincipal(ifr),getCpOpenDate(ifr),getCpCloseDate(ifr)));
       int validate = new DbConnect(ifr,new Query().getSetupMarketWindowQuery(winRefNo,getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpLandingMsg(ifr),getPmMinPrincipal(ifr),getCpOpenDate(ifr),getCpCloseDate(ifr))).saveQuery();
       logger.info("validate-- "+validate);
       if (validate >= 0) {
           setFields(ifr,new String[]{cpPmWinRefNoLocal,windowSetupFlagLocal},new String[]{winRefNo,flag});
           disableFields(ifr, new String[]{cpTreasuryPriSection,cpCutOffTimeSection,cpMarketSection,cpSetupSection});
           logger.info("record saved just for checking");
           return apiSuccess;
       }
       else return "Unable to setup window. Kindly contact iBPS support.";
    }
    public String generateCpWinRefNo(String cpLabel) {
        return cpLabel + getCurrentDateTime(cpRefNoDateFormat);
    }
    public String getWindowSetupFlag (IFormReference ifr){return getFieldValue(ifr,windowSetupFlagLocal);}
    public String getCurrentWorkStep(IFormReference ifr){
        return ifr.getActivityName();
    }
    public boolean isCpWindowActive(IFormReference ifr){
        logger.info("check window query --"+ new Query().getCheckActiveWindowQuery(commercialProcessName,getCpMarket(ifr)));
        return Integer.parseInt(new DbConnect(ifr,
                new Query().getCheckActiveWindowQuery(commercialProcessName,getCpMarket(ifr))).getData().get(0).get(0)) > 0;
    }
    public void setCpPmWindowDetails (IFormReference ifr){
        DbConnect dbConnect = new DbConnect(ifr,new Query().getActiveWindowDetailsQuery(commercialProcessName,getCpMarket(ifr)));
        setFields(ifr, new String[]{cpPmWinRefNoBranchLocal,landMsgLabelLocal,cpPmMinPriAmtBranchLocal},
                new String[]{dbConnect.getData().get(0).get(0),dbConnect.getData().get(0).get(1),dbConnect.getData().get(0).get(2)});
    }
    public void setCpSmWindowDetails(IFormReference ifr){
      resultSet = new DbConnect(ifr,new Query().getActiveWindowDetailsQuery(commercialProcessName,getCpMarket(ifr))).getData();
        setFields(ifr, new String[]{cpSmWinRefNoBranchLocal,landMsgLabelLocal,cpSmMinPrincipalBrLocal},
                new String[]{resultSet.get(0).get(0),resultSet.get(0).get(1),resultSet.get(0).get(2)});
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
    public static String getUtilityFlag (IFormReference ifr){
        return getFieldValue(ifr,utilityFlagLocal);
    }
    private String getCpRandomId(){
        Random random = new Random();
        return String.valueOf(10000000000L + ((long)random.nextInt(900000000)*100) + random.nextInt(100));
    }
    public String getUserSol(IFormReference ifr){return getFieldValue(ifr,solLocal);}
    public boolean cpCheckWindowStateById(IFormReference ifr,String id){
        return Integer.parseInt(new DbConnect(ifr,
                new Query().getCheckActiveWindowByIdQuery(id)).getData().get(0).get(0)) > 0;
    }
    public void setupCpPmBid(IFormReference ifr){
        logger.info("query to setup bid-- "+   new Query().getSetupBidQuery(
                getCurrentDateTime(),getCpPmCusRefNo(ifr), getCpPmWinRefNoBr(ifr),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getFieldValue(ifr,cpCustomerAcctNoLocal),getFieldValue(ifr,cpCustomerNameLocal),getFieldValue(ifr,cpCustomerEmailLocal),String.valueOf(getCpPmCustomerPrincipal(ifr)),getFieldValue(ifr,cpPmTenorLocal),getCpPmRateType(ifr),
                getCpPmRateType(ifr).equalsIgnoreCase(rateTypePersonal) ? getFieldValue(ifr,cpPmPersonalRateLocal) : empty
        ));
        new DbConnect(ifr,
                new Query().getSetupBidQuery(
                        getCurrentDateTime(),getCpPmCusRefNo(ifr), getCpPmWinRefNoBr(ifr),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getFieldValue(ifr,cpCustomerAcctNoLocal),getFieldValue(ifr,cpCustomerNameLocal),getFieldValue(ifr,cpCustomerEmailLocal),String.valueOf(getCpPmCustomerPrincipal(ifr)),getFieldValue(ifr,cpPmTenorLocal),getCpPmRateType(ifr),
                        getCpPmRateType(ifr).equalsIgnoreCase(rateTypePersonal) ? getFieldValue(ifr,cpPmPersonalRateLocal) : empty
                )).saveQuery();
    }
    public String setupCpSmBid(IFormReference ifr){
        resultSet = new DbConnect(ifr,new Query().getCpSmInvestmentsSelectQuery(getSelectedCpSmInvestmentId(ifr))).getData();
        String tenor = resultSet.get(0).get(0);
        String rate = resultSet.get(0).get(1);
        int validate = new DbConnect(ifr,new Query().getSetupCpSmBidQuery(getCurrentDateTime(),getCpSmCusRefNo(ifr), getCpSmWinRefNoBr(ifr),
                getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpAcctNo(ifr),getCpAcctName(ifr),getCpAcctEmail(ifr),getCpSmPrincipalBr(ifr),
                tenor,rate,getCpSmMaturityDate(ifr))).saveQuery();

        logger.info("is saved -- "+ validate);

        if (validate >= 0){
            float availableAmount =   Float.parseFloat(resultSet.get(0).get(2)) - Float.parseFloat(getCpSmPrincipalBr(ifr)) ;
            float totalAmountSold = Float.parseFloat(resultSet.get(0).get(3)) + Float.parseFloat(getCpSmPrincipalBr(ifr));
            int mandates = Integer.parseInt(resultSet.get(0).get(4)) + 1;
            new DbConnect(ifr,new Query().getCpSmInvestmentsUpdateQuery(getSelectedCpSmInvestmentId(ifr),String.valueOf(availableAmount),String.valueOf(totalAmountSold),String.valueOf(mandates))).saveQuery();
            disableField(ifr,cpInvestBtn);
            return "Customer Bid has been invested, Thank You.";
        }
        return "Customer Bid cannot be invested right now. Contact IBPS support";
    }
    public static String getCpAcctNo(IFormReference ifr){return getFieldValue(ifr,cpCustomerAcctNoLocal);}
    public static String getCpAcctName(IFormReference ifr){return getFieldValue(ifr,cpCustomerNameLocal);}
    public static String getCpAcctEmail(IFormReference ifr){return getFieldValue(ifr,cpCustomerEmailLocal);}
    public static String getCpAcctSol(IFormReference ifr){return getFieldValue(ifr,cpCustomerSolLocal);}
    public static String getCpSmInvestmentId(IFormReference ifr){return getFieldValue(ifr,cpSmInvestmentIdLocal);}
    public static String getCpSmMaturityDate(IFormReference ifr){return getFieldValue(ifr,cpSmMaturityDateBrLocal);}
    public static void setTableGridData(IFormReference ifr, String tableLocal, String [] columns, String [] rowValues){
        JSONArray jsRowArray = new JSONArray();
        jsRowArray.add(setTableRows(columns,rowValues));
        ifr.addDataToGrid(tableLocal,jsRowArray);
    }
    private static JSONObject setTableRows(String [] columns, String [] rowValues){
        JSONObject jsonObject = new JSONObject();
        for (int i = 0 ; i < columns.length; i++)
            jsonObject.put(columns[i],rowValues[i]);

        return jsonObject;
    }
    public String getDownloadFlag (IFormReference ifr){
        return getFieldValue(ifr,downloadFlagLocal);
    }
    public boolean checkBidStatus(String rate, String cpRate){
        return Float.parseFloat(rate) <= Float.parseFloat(cpRate);
    }
    public boolean checkBidStatus(double rate, double cbnRate){
        return rate <= cbnRate;
    }
    public static String getMaturityDate(int tenor){
        return LocalDate.now().plusDays(tenor).toString();
    }
    public static boolean isLeapYear (){
        return LocalDate.now().isLeapYear();
    }
    public static boolean isLeapYear (String date){
            return LocalDate.parse(date).isLeapYear();
    }
    public static String getCpSmSetup(IFormReference ifr){return getFieldValue(ifr,cpSmSetupLocal);}
    public static long getDaysToMaturity(String maturityDate){
        return ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(maturityDate));
    }
    public String cpSetupSecondaryMarketWindow(IFormReference ifr, int rowCount){
        setCpSmCloseDate(ifr);
        int validate = new DbConnect(ifr,new Query().getSetupMarketWindowQuery(getFieldValue(ifr,cpSmWinRefLocal),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getCpLandingMsg(ifr),getFieldValue(ifr,cpSmMinPrincipalLocal),getCpOpenDate(ifr),getCpCloseDate(ifr))).saveQuery();
        if (validate >= 0){
            setCpSmInvestmentOnSetup(ifr,rowCount);
            setWindowSetupFlag(ifr);
            disableField(ifr,cpSetupWindowBtn);
           return apiSuccess;
        }
        else return "Unable to setup window. Kindly contact iBPS support.";
    }
    private String getCpSmInvestmentId(){
        return cpSmIdInvestmentLabel+getCpRandomId();
    }
    public String getSelectedCpSmInvestmentId(IFormReference ifr){
        return getFieldValue(ifr,cpSmInvestmentIdLocal);
    }
    private void setCpSmCloseDate(IFormReference ifr){
        String time = " 14:00:01";
        setFields(ifr,cpCloseDateLocal,LocalDate.now().toString() +time);
    }
    private void setCpSmInvestmentOnSetup(IFormReference ifr, int rowCount){
        for( int i = 0; i < rowCount; i++){
            String corporateName = ifr.getTableCellValue(cpSmCpBidTbl,i,0);
            String description = ifr.getTableCellValue(cpSmCpBidTbl,i,1);
            String maturityDate = ifr.getTableCellValue(cpSmCpBidTbl,i,2);
            String billAmount = ifr.getTableCellValue(cpSmCpBidTbl,i,3);
            String rate = ifr.getTableCellValue(cpSmCpBidTbl,i,4);
            String tenor = ifr.getTableCellValue(cpSmCpBidTbl,i,5);
            String dtm = ifr.getTableCellValue(cpSmCpBidTbl,i,6);
            String status = ifr.getTableCellValue(cpSmCpBidTbl,i,7);
            String guaranteedCp = ifr.getTableCellValue(cpSmCpBidTbl,i,9);

            new DbConnect(ifr,new Query().getSetSmInvestmentQuery(getCpSmInvestmentId(),getWorkItemNumber(ifr),commercialProcessName,getCpMarket(ifr),getFieldValue(ifr,cpSmWinRefLocal),
                    corporateName,description,maturityDate,billAmount,billAmount,rate,tenor,dtm,status,guaranteedCp,getCpOpenDate(ifr),getCpCloseDate(ifr),getCurrentDateTime())).saveQuery();
        }
    }
    public void setWindowSetupFlag (IFormReference ifr){
        setFields(ifr,windowSetupFlagLocal,flag);
    }
    public String getCpSmConcessionRate(IFormReference ifr){
        return getFieldValue(ifr,cpSmConcessionRateLocal);
    }
    public String getCpSmConcessionRateValue(IFormReference ifr){
        return getFieldValue(ifr,cpSmConcessionRateValueLocal);
    }
    public void clearTable(IFormReference ifr, String tableLocal){
        ifr.clearTable(tableLocal);
    }
    public String getCpSmPrincipalBr(IFormReference ifr){
        return getFieldValue(ifr,cpSmPrincipalBrLocal);
    }
    public String getCpSmWindowMinPrincipal(IFormReference ifr){
        return getFieldValue(ifr,cpSmMinPrincipalBrLocal);
    }
    public boolean isDateEqual (String date1, String date2){
        return LocalDate.parse(date1).isEqual(LocalDate.parse(date2));
    }
    public String getCpMandateType (IFormReference ifr){
        return getFieldValue(ifr,cpMandateTypeLocal);
    }
    public String getCpMandateToTerminate(IFormReference ifr){
        return getFieldValue(ifr,cpTermMandateLocal);
    }
    public static boolean isCpLien(IFormReference ifr, String id){
        return Integer.parseInt(new DbConnect(ifr,Query.getCpLienStatusQuery(id)).getData().get(0).get(0)) > 0;
    }
    public static String getCpTerminationType(IFormReference ifr){
        return getFieldValue(ifr,cpTerminationTypeLocal);
    }
    public static String getCpTermSpecialRateValue(IFormReference ifr){
        return getFieldValue(ifr,cpTermSpecialRateValueLocal);
    }
    public static String getCpTermSpecialRate(IFormReference ifr){
        return getFieldValue(ifr,cpTermSpecialRateLocal);
    }
    public static String getCpTermDtm(IFormReference ifr){
        return getFieldValue(ifr,cpTermDtmLocal);
    }
    public static String getCpTermCustId(IFormReference ifr){
        return getFieldValue(ifr,cpTermCustIdLocal);
    }
    public static String getCpTermPartialAmt(IFormReference ifr){
        return  getFieldValue(ifr,cpTermPartialAmountLocal);
    }
    public static boolean getCpTermIsSpecialRate(IFormReference ifr){
        return  getCpTermSpecialRate(ifr).equalsIgnoreCase(True);
    }
    public static boolean isEmpty(List<List<String>> resultSet){
        return  resultSet.size() == 0;
    }
    public static double getPercentageValue(String value){
        return Double.parseDouble(value) / 100;
    }
    public static String getCpPartialTermOption(IFormReference ifr){
        return getFieldValue(ifr,cpTermPartialOptionLocal);
    }
    public static  String getCpLienType (IFormReference ifr){
        return getFieldValue(ifr,cpLienTypeLocal);
    }
    public static String getCpLienMandateId(IFormReference ifr){
        return getFieldValue(ifr, cpLienMandateIdLocal);
    }
    public static boolean doesCpIdExist(IFormReference ifr, String id, String marketType){
        return Integer.parseInt(new DbConnect(ifr,Query.getCpCustIdExistQuery(id,marketType)).getData().get(0).get(0)) > 1;
    }
    public  static String getCpPoiMandate(IFormReference ifr){
        return getFieldValue(ifr,cpPoiMandateLocal);
    }
    public static String getCpPoiCusId(IFormReference ifr){
        return getFieldValue(ifr,cpPoiCustIdLocal);
    }



    /************************* COMMERCIAL PAPER CODE ENDS **************************/

    /******************  TREASURY BILL CODE BEGINS *********************************/
    public static void hideTbSections (IFormReference ifr){hideFields(ifr,allTbSections);}
    public static void disableTbSections (IFormReference ifr){disableFields(ifr,allTbSections);}
    public static void disableField(IFormReference ifr, String field) {ifr.setStyle(field,disable,True);}
    public static void clearFields(IFormReference ifr, String field) {ifr.setValue(field,empty);}
    public static void setVisible(IFormReference ifr, String field) { ifr.setStyle(field,visible,True);}
    public static void hideField(IFormReference ifr, String field) {ifr.setStyle(field,visible,False);}
    public static void hideFields(IFormReference ifr, String [] fields ) { for(String field: fields) ifr.setStyle(field,visible,False); }
    public static void enableField(IFormReference ifr, String field) {ifr.setStyle(field,disable,False);}
    public static void setMandatory(IFormReference ifr, String field) { ifr.setStyle(field,mandatory,True); }
    public static void undoMandatory(IFormReference ifr, String field) { ifr.setStyle(field,mandatory,False); }
    public static String getTbMarketName (IFormReference ifr){
        if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)) 
        	return primary;
        else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)) 
        	return secondary;
        return null;
    }
    public static String getTbMarket(IFormReference ifr){
    	return  (String) ifr.getValue(tbMarketTypedd);
    }
    public void setTbDecisionHistory (IFormReference ifr){
        String marketType = getTbMarketName(ifr);
        String remarks = (String)ifr.getValue(tbRemarkstbx);
        String entryDate = (String)ifr.getValue(entryDateLocal);
        String exitDate = getCurrentDateTime();
        setDecisionHistory(ifr,getLoginUser(ifr),treasuryProcessName,marketType,getCpDecision(ifr),remarks,getActivityName(ifr),entryDate,exitDate,getTat(entryDate,exitDate));
        ifr.setValue(decHisFlagLocal,flag);
    }
    public boolean getTbUpdateLandingMsg(IFormReference ifr){ 
    	return ((String) ifr.getValue(tbUpdateLandingMsgcbx)).equalsIgnoreCase("true")? true : false; 
    }
    public static void setTbUpdateLandingMsg(IFormReference ifr, String value){ ifr.setValue(tbUpdateLandingMsgcbx,value); }
    public static String getTbDecision (IFormReference ifr){return (String) ifr.getValue(tbDecisiondd);}
    public static void setTbDecisiondd (IFormReference ifr, String value){ifr.setValue(tbDecisiondd,value);}
    public static String getTbCategorydd (IFormReference ifr){return (String) ifr.getValue(tbCategorydd);}
    public static void setTbCategorydd  (IFormReference ifr, String value){ifr.setValue(tbCategorydd,value);}
    public static String getTbPriOpenDate(IFormReference ifr){return (String)ifr.getValue(tbPriOpenDate);}
    public static String getTbPriCloseDate(IFormReference ifr){return (String)ifr.getValue(tbPriCloseDate);}
    
    public static void setTbSetUpFlg(IFormReference ifr,String value){ifr.setValue(windowSetupFlagLocal,value);}
    public static String getTbSetUpFlg(IFormReference ifr){return (String)ifr.getValue(windowSetupFlagLocal);}
    public static void setTbCustSchemeCode(IFormReference ifr,String value){ifr.setValue(tbCustSchemeCode,value);}
    public static String getTbCustSchemeCode(IFormReference ifr){return (String)ifr.getValue(tbCustSchemeCode);}
    public static void setTbBrnchPriPrncplAmt(IFormReference ifr,String value){ifr.setValue(tbBrnchPriPrncplAmt,value);}
    public static String getTbBrnchPriPrncplAmt(IFormReference ifr){return (String)ifr.getValue(tbBrnchPriPrncplAmt);}
    
    public static String getTbMarketUniqueRefId(IFormReference ifr){return (String)ifr.getValue(tbMarketUniqueRefId);}
    public static void setTbMarketUniqueRefId(IFormReference ifr,String value){
    	if(isEmpty(getTbMarketUniqueRefId(ifr)))
    		ifr.setValue(tbMarketUniqueRefId,value);
    }
    public static String getTbLandingMsgApprovedFlg(IFormReference ifr){return getFieldValue(ifr,tbLandingMsgApprovedFlg);}
    public static void setTbLandingMsgApprovedFlg(IFormReference ifr, String value){ ifr.setValue(tbLandingMsgApprovedFlg,value);}
    public static String getTbLandingMsg (IFormReference ifr){return getFieldValue(ifr,tbLandMsgtbx);}
    
    public static String getDateWithoutTime() {
    	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
    	try {
    		return formatter.format(new Date());
    	}
    	catch (Exception e) {
			logger.info("getDateWithoutTime Exception>>>"+e.toString());
			return null;
		}
	}
    
    public static String tbGetSmDefaultCutoffDteTime() {
    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    	try {
    		String dte = formatter.format(new Date());
    		logger.info("dte>>"+dte +"Ading timg>>>"+dte+ " 15:00:00");
    		return (formatter.format(new Date()))+" 15:00:00";
    	}
    	catch (Exception e) {
			logger.info("getDateWithoutTime Exception>>>"+e.toString());
			return null;
		}
	}
    public static void clearDropDown(IFormReference ifr, String controlName){
        ifr.clearCombo(controlName);
    } 
    // tb window Primary setup
    public static String setUpTbMarketWindow(IFormReference ifr, String unqNo, String opendte, String closedte, String minimumPrincipal){
    	String qry = new Query().getSetupMarketWindowQuery(unqNo,  getWorkItemNumber(ifr), treasuryProcessName, getTbMarket(ifr), 
    			getTbLandingMsg(ifr),minimumPrincipal,opendte, closedte);
        logger.info("setUpTbMarketWindow Query>> "+qry);
        int insertVal = new DbConnect(ifr,qry).saveQuery();
        if (insertVal >= 0) {
        	setTbSetUpFlg(ifr,flag);
           logger.info("record saved successfully");
           return "";
       }
       else 
    	   return "Unable to setup window try again later";
    }
    
    public static String setUpTbMarketWindow(IFormReference ifr){
    	String qry = new Query().getSetupMarketWindowQuery(getTbMarketUniqueRefId(ifr) ,getWorkItemNumber(ifr), treasuryProcessName, getTbMarket(ifr), 
    			getTbLandingMsg(ifr),getTbPriOpenDate(ifr),getTbPriCloseDate(ifr));
        logger.info("setUpTbMarketWindow Query>> "+qry);
        int insertVal = new DbConnect(ifr,qry).saveQuery();
        if (insertVal >= 0) {
        	setTbSetUpFlg(ifr,flag);
           logger.info("record saved successfully");
           return "";
       }
       else 
    	   return "Unable to setup window try again later";
    }
    
    public Date tbConvertStringToDate(String str) {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    	try {
			return formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /*
     * check if window is open for a market
     * query gets the date of the specified market type then check if current date is before close
     * date --- if its before return true else return false
     */
    public boolean isTbWindowOpen(IFormReference ifr){
    	String qry = new Query().getWinCloseDateQuery(treasuryProcessName,getTbMarket(ifr));
        logger.info("isTbWindowOpen--"+ qry);
        List<List<String>> dbr = new DbConnect(ifr,qry).getData();
        logger.info("isTbWindowOpen db output>>"+dbr);
        String closedte = "";
        if(dbr.size()>0) {
        	closedte = dbr.get(0).get(0);
        	logger.info("isTbWindowOpen CloseDTe>>>"+closedte);
        	return tbConvertStringToDate(closedte).compareTo(new Date())>0 ? true:false;
        }
        return false;
    }
    
    /*
     * return true is window is set and open
     * else return false
     */
     public boolean isTbWindowClosed(IFormReference ifr,String refid){
     	String qry = new Query().getClosedMarketWinRefIDQuery(treasuryProcessName,getTbMarket(ifr),refid,getWorkItemNumber(ifr));
         logger.info("isTbWindowClosed query --"+ qry);
         List<List<String>> dbr = new DbConnect(ifr,qry).getData();
         logger.info("isTbWindowClosed db output>>"+dbr);
         return dbr.size()>0 ? true:false;
     }
     /*
      * return true is window is open
      * else return false
      */
      public boolean isTbWindowOpen(IFormReference ifr,String refid){
      	String qry = new Query().getWinCloseFlagById(refid);
         logger.info("getWinCloseFlagById query --"+ qry);
          List<List<String>> dbr = new DbConnect(ifr,qry).getData();
          logger.info("getWinCloseFlagById db output>>"+dbr);
          if(dbr.size()>0)
        	  return (dbr.get(0).get(0)).equalsIgnoreCase("N") ? true:false;
          return false;
      }
   /*
    * return true is if window is closed
    * else return false
    */
    public boolean isTbWindowActive(IFormReference ifr){
    	String qry = new Query().getCheckActiveWindowQuery(treasuryProcessName,getTbMarket(ifr));
        logger.info("check tb window query --"+ qry);
        List<List<String>> dbr = new DbConnect(ifr,qry).getData();
        logger.info("isTbWindowActive db output>>"+dbr);
        return dbr.size()>0 ? true:false;
    }
    //get refid from active window
    public static String getTbActiveWindowwithRefid(IFormReference ifr){
    	String qry = new Query().getCheckActiveWindowQueryRefid(treasuryProcessName,getTbMarket(ifr));
        logger.info("check tb window query --"+ qry);
        try{
        	return new DbConnect(ifr,qry).getData().get(0).get(0); 
        }
        catch(Exception ex){return ""; }
    }
    
    //primary branch validations
    /*
     * validate Account Schemecode
     * Secondary Market where private banking investment account with scheme code PB2010), system must bring out an additional field for ‘Remark’. This field must be a compulsory field.
     */
    public static String tbValidateCustomer(IFormReference ifr) {
    	tbFetchAccountDetails(ifr);
    	String retMsg = (getTbCustSchemeCode(ifr).equalsIgnoreCase(SA231) || getTbCustSchemeCode(ifr).equalsIgnoreCase(SA310) ||
    			getTbCustSchemeCode(ifr).equalsIgnoreCase(SA340) ||getTbCustSchemeCode(ifr).equalsIgnoreCase(SA327)) ?
    			"This account is not valid for TB processing":"";	
    	logger.info("retMsg1>>"+ retMsg);
    	if (isEmpty(retMsg))
    		retMsg = isEmpty(getTbCustAcctEmail(ifr)) ? "Update email of customer on account maintenance workflow":"";
    	logger.info("retMsg1>>"+ retMsg);
    	
    	//scheme code PB2010
    	if(getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
    		if(getTbCustSchemeCode(ifr).equalsIgnoreCase(PB2010)) {
    			setVisible(ifr, tb_SmCustBidRemark);
    			setMandatory(ifr, tb_SmCustBidRemark);
    		}
    		else {
    			clearFields(ifr, tb_SmCustBidRemark);
    			hideField(ifr,tb_SmCustBidRemark);
    			undoMandatory(ifr, tb_SmCustBidRemark);
    		}	
    		
    	}
    	return retMsg;
    }
    
    public static void tbFetchAccountDetails(IFormReference ifr) {
    	if(getTbCustAcctNo(ifr).equalsIgnoreCase("22")){
	    	setTbCustAcctName(ifr, "John Doe");
	    	setTbCustAcctLienStatus(ifr, "Yes");
	    	setTbCustAcctEmail(ifr, "someone@gmail.com");
	    	setTbCustSchemeCode(ifr,"SA931");
	    }
    	else if(getTbCustAcctNo(ifr).equalsIgnoreCase("33")){
	    	setTbCustAcctName(ifr, "Miranda");
	    	setTbCustAcctLienStatus(ifr, "No");
	    	setTbCustAcctEmail(ifr, "someone@gmail.com");
	    	setTbCustSchemeCode(ifr,"SA931");
	    }
    	else if(getTbCustAcctNo(ifr).equalsIgnoreCase("44")){
	    	setTbCustAcctName(ifr, "Mercy Lee");
	    	setTbCustAcctLienStatus(ifr, "No");
	    	setTbCustAcctEmail(ifr, "ogb@gmail.com");
	    	setTbCustSchemeCode(ifr,"SA931");
	    }
    	else if(getTbCustAcctNo(ifr).equalsIgnoreCase("55")){
	    	setTbCustAcctName(ifr, "Nabcy bbbe");
	    	setTbCustAcctLienStatus(ifr, "No");
	    	setTbCustAcctEmail(ifr, "ogb@gmail.com");
	    	setTbCustSchemeCode(ifr,"SA931");
	    }
    	//return isEmpty(getTbCustAcctEmail(ifr)) ? "Update email of customer on account maintenance workflow":null;
    }
  /*  
    o	IBPS fetches customers account number and email address from Finacle (if available)
    o	If email address is not available, a pop up will be displayed for user to update customerï¿½s email .ï¿½Update email of customer on account maintenance workflowï¿½
    o	User should be able to select email notification required from a dropdown.
    o	User select TENOR from the drop down (91 days, 182 days or 364 days)
    o	User select rollover type from the dropdown list ( Terminate at maturity, principal or principal+interest)
    o	Where user selects terminate at maturity ,user must select termination type which is mandatory
    o	User select rate type from the dropdown (bank rate or personal rate ). Minimum principal amount for personal rate is N50,000,000.00 while the minimum for bank rate is N100,000.
    o	Where user selects personal rate, field for personal rate is enabled for user to enter personal rate value
    o	User enters principal amount(validation for amount should be in thousands and comma used for separation)
    o	The unique reference for primary market is generated automatically 
    o	User then click on submit and request flows to  Money_Market_Branch_Verifier in the same branch to approve
    o	All transactions initiated by branch user must be approved by Money_Market_Branch_Verifier before the cut off time.  ï¿½Where request remains pending with branch_verifier after cut off time setup approved by Money_Market_Treasury_Verifier, request will not be processed. Error message will be displayed stated ï¿½Cutoff time for window has elapsedï¿½
    o	Branch initiation User should not initiate the request beyond open time and close time where window unique ref no has been generated from Money_Market_Treasury_Officer
*/
    public static void setTbCustAcctNo(IFormReference ifr, String value) {ifr.setValue(tbCustAcctNo,value);}
	public static String getTbCustAcctNo(IFormReference ifr) {return (String) ifr.getValue(tbCustAcctNo);}
	public static void setTbCustAcctName(IFormReference ifr, String value) {ifr.setValue(tbCustAcctName,value);}
	public static String getTbCustAcctName(IFormReference ifr) {return (String) ifr.getValue(tbCustAcctName);}
	public static void setTbCustAcctLienStatus(IFormReference ifr, String value) {ifr.setValue(tbCustAcctLienStatus,value);}
	public static String getTbCustAcctLienSatatus(IFormReference ifr) {return (String) ifr.getValue(tbCustAcctLienStatus);}
	public static void setTbCustAcctEmail(IFormReference ifr, String value) {ifr.setValue(tbCustAcctEmail,value);}
	public static String getTbCustAcctEmail(IFormReference ifr) {return (String) ifr.getValue(tbCustAcctEmail);}
	
	
	public static void setTbPriWindownUnqNo(IFormReference ifr, String value) {ifr.setValue(tbBrnchPriWindownUnqNo,value);}
	public static String getTbPriWindownUnqNo(IFormReference ifr) {return (String) ifr.getValue(tbBrnchPriWindownUnqNo);}
	
	public static void setTbBrnchPriRqsttype(IFormReference ifr, String value) {ifr.setValue(tbBrnchPriRqsttype,value);}
	public static String getTbBrnchPriRqsttype(IFormReference ifr) {return (String) ifr.getValue(tbBrnchPriRqsttype);}
	public static String getTbBrcnhPriRateTypedd(IFormReference ifr) {return (String) ifr.getValue(tbBrcnhPriRateTypedd);}
	public static String getTbBrnchPriRollovrdd(IFormReference ifr){return (String)ifr.getValue(tbBrnchPriRollovrdd);}
	public  String getTbtoken(IFormReference ifr) {return (String) ifr.getValue(tbtoken);}
	//public static void setTbtoken(IFormReference ifr, String value) {ifr.setValue(tbtoken,value);}
	public  String getTbTranID(IFormReference ifr) {return (String) ifr.getValue(tbTranID);}
	public  void setTbTranID(IFormReference ifr, String value) {ifr.setValue(tbTranID,value);}
	public  String getPostStatus(IFormReference ifr) {return tbSuccess;}
	public static void setTbBrnchCustPriRefNo(IFormReference ifr, String value) {ifr.setValue(tbBrnchCustPriRefNo,value);}
	public static String getTbBrnchCustPriRefNo(IFormReference ifr) {return (String) ifr.getValue(tbBrnchCustPriRefNo);}
	public static void setTbAssigndd(IFormReference ifr, String value) {ifr.setValue(tbAssigndd,value);}
	public static String getTbAssigndd(IFormReference ifr) {return (String) ifr.getValue(tbAssigndd);}
	public static void setTb_BrnchPri_LienID(IFormReference ifr, String value) {ifr.setValue(tb_BrnchPri_LienID,value);}
	public static String getTb_BrnchPri_LienID(IFormReference ifr) {return (String) ifr.getValue(tb_BrnchPri_LienID);}
	public static void setTbVerificationAmttbx(IFormReference ifr, String value) {ifr.setValue(tbVerificationAmtttbx,value);}
	public static String getTbVerificationAmttbx(IFormReference ifr) {return (String) ifr.getValue(tbVerificationAmtttbx);}
	
	public static void setTbBrnchSmWindownUnqNo(IFormReference ifr, String value) {ifr.setValue(tbBrnchSmWindownUnqNo,value);}
	public static String getTbBrnchSmWindownUnqNo(IFormReference ifr) {return (String) ifr.getValue(tbBrnchSmWindownUnqNo);}
	public static void setTbSmBidAmount(IFormReference ifr, String value) {ifr.setValue(tbSmBidAmount,value);}
	public static String getTbSmBidAmount(IFormReference ifr) {return (String) ifr.getValue(tbSmBidAmount);}
	public static void setTbSmMinPriAmt(IFormReference ifr, String value) {ifr.setValue(tbSmMinPriAmt,value);}
	public static String getTSmMinPriAmt(IFormReference ifr) {return (String) ifr.getValue(tbSmMinPriAmt);}
	
	public static void setTbSmInvestmentId(IFormReference ifr, String value) {ifr.setValue(tbSmInvestmentId,value);}
	public static String getTbSmInvestmentId(IFormReference ifr) {return (String) ifr.getValue(tbSmInvestmentId);}
	
	public static void setTbSmtenor(IFormReference ifr, String value) {ifr.setValue(tbSmtenor,value);}
	public static String getTbSmtenor(IFormReference ifr) {return (String) ifr.getValue(tbSmtenor);}
	public static void setTbSmRate(IFormReference ifr, String value) {ifr.setValue(tbSmRate,value);}
	public static String getTbSmRate(IFormReference ifr) {return (String) ifr.getValue(tbSmRate);}
	
	public static void setTbSmMaturityDte(IFormReference ifr, String value) {ifr.setValue(tbSmMaturityDte,value);}
	public static String getTbSmMaturityDte(IFormReference ifr) {return (String) ifr.getValue(tbSmMaturityDte);}
	public static void setTbSmConcessionValue(IFormReference ifr, String value) {ifr.setValue(tbSmConcessionValue,value);}
	public static String getTbSmConcessionValue(IFormReference ifr) {return (String) ifr.getValue(tbSmConcessionValue);}
	public static void setTbSmConcessionRate(IFormReference ifr, String value) {ifr.setValue(tbSmConcessionRate,value);}
	public static String getTbSmConcessionRate(IFormReference ifr) {return (String) ifr.getValue(tbSmConcessionRate);}
	public static void setTbSmInstructionType(IFormReference ifr, String value) {ifr.setValue(tbSmInstructionType,value);}
	public static String getTbSmInstructionType(IFormReference ifr) {return (String) ifr.getValue(tbSmInstructionType);}
	public static void setTbSmPrincipalAtMaturity(IFormReference ifr, String value) {ifr.setValue(tbSmPrincipalAtMaturity,value);}
	public static String getTbSmPrincipalAtMaturity(IFormReference ifr) {return (String) ifr.getValue(tbSmPrincipalAtMaturity);}
	
	
	public static void setTbSmIntstMaturityNonLpYr(IFormReference ifr, String value) {ifr.setValue(tbSmIntstMaturityNonLpYr,value);}
	public static String getTbSmIntstMaturityNonLpYr(IFormReference ifr) {return (String) ifr.getValue(tbSmIntstMaturityNonLpYr);}
	
	public static void setTbSmIntrsyMaturityLpYr(IFormReference ifr, String value) {ifr.setValue(tbSmIntrsyMaturityLpYr,value);}
	public static String getTbSmIntrsyMaturityLpYr(IFormReference ifr) {return (String) ifr.getValue(tbSmIntrsyMaturityLpYr);}
	public static void setTbSmResidualIntrst(IFormReference ifr, String value) {ifr.setValue(tbSmResidualIntrst,value);}
	public static String getTbSmResidualIntrst(IFormReference ifr) {return (String) ifr.getValue(tbSmResidualIntrst);}
	public static void setTbSmIntrestAtMaturity(IFormReference ifr, String value) {ifr.setValue(tbSmIntrestAtMaturity,value);}
	public static String getTbSmIntrestAtMaturity(IFormReference ifr) {return (String) ifr.getValue(tbSmIntrestAtMaturity);}
	
	//public static void setTbPoolManager(IFormReference ifr, String value) {ifr.setValue(tbPoolManager,value);}
	//public static String getTbPoolManager(IFormReference ifr) {return (String) ifr.getValue(tbPoolManager);}
	
	
	public static void setWiName(IFormReference ifr){
	    setFields(ifr,wiNameFormLocal,getWorkItemNumber(ifr));
	}





    
	//unsused
	private String tbMarketTypeddChange(IFormReference ifr){
    	String retMsg ="";
    	if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		if(isTbWindowActive(ifr)){
    			setVisible(ifr, new String[]{tbMarketSection, tbDecisionSection});
    			disableFields(ifr, new String[]{tbMarketSection});
    		}
    		else {
    			retMsg = getTbMarket(ifr)+tbWindowInactiveMessage;
    			//hide or disable all fields
    		}
    	}
    	return retMsg;
    }
	public String generateTbUniqueReference(IFormReference ifr) {
    	//generate ref. check if its in db
		logger.info("generateTbUniqueReference");
		String unqno ="";
    	 if (getTbMarket(ifr).equalsIgnoreCase(tbPrimaryMarket)){
    		 unqno ="TBPMA"+getDateWithoutTime();	
         }
         else if (getTbMarket(ifr).equalsIgnoreCase(tbSecondaryMarket)){
        	 unqno= "TBSEC"+getDateWithoutTime();	
         }
    	 logger.info("unqno>>"+unqno);
    	return unqno;
    }
	
	/*
	 * @param length is the length of the five digit serial no.
	 * Markettype represents market (Primary-P, Secondary- S or OMO-O)
	 * *Automatically generated PXXX12AB45678.
		Where; XXX represents SOL ID of processing branch.
		12 represents year of processing
		AB represents month of processing
		45678 represents five digit serial numbers for each TBills processed bank wide
		Reference number (unique identifier) must be a hypertext (hyperlink) which enables user explode on the transaction to view full transaction details.
		*/
	public String tbGenerateCustRefNo(IFormReference ifr) {
		Date date =new Date();
		String randNo = (getTbMarket(ifr).charAt(0)) + getUserSol(ifr) + 
				new SimpleDateFormat("yyyy").format(date)+new SimpleDateFormat("MMM").format(date) + 
				((int)(Math.random()*9000)+1000);
		
		String qry = new Query().getCustomerRefIdQuery(randNo);
		logger.info("getCustomerRefIdQuery>>"+qry);
		if(new DbConnect(ifr,qry).getData().size()>0)
			tbGenerateCustRefNo(ifr);
		//else save in db
		logger.info("randNo>>>"+randNo);
		return randNo.toUpperCase();
	}
	
	 //check if cutoff time has elapsed
	 public static boolean isTbWinValid(IFormReference ifr){
    	String qry = new Query().getWinCloseDateByIdQuery(getTbPriWindownUnqNo(ifr));
        logger.info("check tb window query --"+ qry);
        Date closeDte = null;
        List<List<String>> ls = new DbConnect(ifr,qry).getData();
        logger.info("getWinCloseDateById: >>"+ls);
        if(ls.size()>0) {
        	//compare dates
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				closeDte = formatter.parse(ls.get(0).get(0));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   			logger.info("Close Date - Date format>>>"+closeDte);
   			
   			logger.info("currDte>>"+new Date()); 
   			System.out.println(closeDte.compareTo(new Date()));
   			
        }
        return  closeDte.compareTo(new Date())>=0  ?true:false;
    }
    /*
     * check if a document has been uploaded
     */
    public boolean isTbDocUploaded(IFormReference ifr,String winame,String docName){
    	logger.info("isTbDocUploaded");
    	String qry = new Query().getUploadedDocQuery(winame, docName);
    	logger.info("isDocUploaded dbQuery: "+qry);
    	List<List<String>> dbResult = new DbConnect(ifr,qry).getData();
    	logger.info("isDocUploaded dbResult>> "+dbResult);
    	return dbResult.size() == 0 ? false:true;
    }
    
    public double convertStringToDouble(String s) {
    	try{
    		return Double.parseDouble(s);
    	}
    	catch(Exception ex) {
    		///retMsg = "parsing cbnrate error:>>>"+ ex.toString();
    		logger.info("parseDoubleValue Excepton:>>>"+ ex.toString());
    		return 0;
    	}
    }
    
    public String convertDoubleToString(double d) {
    	logger.info("converting double : "+d+" to String>>");
    	String strVal =  (d == 0) ? "":String.valueOf(d);
    	logger.info("converting double : "+d+" to String>: "+strVal);
    	return strVal;
    }
    
    public String getSmMinPrincipal(IFormReference ifr,String refid) {
    	String qry = new Query().getSmMinPrincipalQuery(refid);
    	logger.info("getSmMinPrincipalQuery>>"+ qry);
        List<List<String>> dbr = new DbConnect(ifr, qry).getData();
        logger.info("getTbPmUpdateFailedBidsQuery save db result>>>"+dbr);
        if(dbr.size()>0)
        	return dbr.get(0).get(0);
        return "";
    }
    //update available amount of seconday issued bills(IB)
    public void updateSmIBAvailableAmt(double bidamt) {
    	//double newAvailAmt = ()
    }
    
    //leap year calculator
    private boolean isLeapyear(String maturityDte) {
		try {
			int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(
					new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(maturityDte)));
			logger.info("year >>."+year);
	    	return ((year%4 ==0) && (year % 100!=0)) || (year%400 ==0) ? true:false;
		} catch (NumberFormatException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
    
    /*
     * Interest for maturity date  in Non Leap year ={(Principal * Tenor *concessionary rate)/365 *100)
     */
    public double tbCalcSmInterestAtMaturity(IFormReference ifr,String maturityDte,double bidamt, double tenor, double csrate) {
    	//check for leap year
    	if(isLeapyear(maturityDte)) {
    		return isEmpty(getTbSmConcessionValue(ifr)) ? ( bidamt* tenor)/(365*100):
    			(( bidamt* tenor*csrate)/(365*100))+(tenor/366);
    	}
    	else { //not leap year
    		return isEmpty(getTbSmConcessionValue(ifr)) ? ( bidamt* tenor)/(365*100):
    			( bidamt* tenor*csrate)/(365*100);
    	}
    }
    /*
     * Interest for maturity date  in Non Leap year ={(Principal * Tenor *concessionary rate)/365 *100)
     * Interest for maturity date in Leap year ={(Principal *concessionary rate* Tenor )/365*100 ) + Tenor/366
     */
    public double tbCalcSmPrincipalAtMaturity(IFormReference ifr, String maturityDte, double bidamt, double tenor, double csrate,double rate) {
   
    	double interest =  tbCalcSmInterestAtMaturity(ifr, maturityDte, bidamt,  tenor,  csrate);
    	return bidamt*(interest+(rate*tenor));
    }
    
    
    	

    
    
    /******************  TREASURY BILL CODE ENDS ***********************************/
}
