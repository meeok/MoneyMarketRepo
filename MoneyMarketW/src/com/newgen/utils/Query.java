package com.newgen.utils;

public class Query {
    public String getSolQuery(String userName) {
        return "select sole_id from usr_0_fbn_usr_branch_mapping where upper(user_id) = upper('" + userName + "')";
    }
    public String getUsersInGroup(String groupName) {
        return "select username from pdbuser where userindex in (select userindex from pdbgroupmember where groupindex = (select groupindex from PDBGroup where GroupName='" + groupName + "'))";
    }
    public static String getMailQuery(String wiName, String sendMail, String copyMail, String mailSubject, String mailMessage) {
        return "insert into wfmailqueuetable (" +
                "mailfrom," +
                "mailto," +
                "mailcc," +
                "mailsubject," +
                "mailmessage," +
                "mailcontenttype," +
                "mailpriority," +
                "insertedby," +
                "mailactiontype," +
                "insertedtime," +
                "processdefid," +
                "processinstanceid," +
                "workitemid," +
                "activityid," +
                "mailstatus) " +
                "values (" +
                "'" + LoadProp.mailFrom + "'," +
                "'" + sendMail + "'," +
                "'" + copyMail + "'," +
                "'" + mailSubject + "'," +
                "'" + mailMessage + "'," +
                "'text/html;charset=UTF-8'," +
                "1," +
                "'System'," +
                "'TRIGGER'," +
                "SYSDATE," +
                "" + LoadProp.processDefId + "," +
                "'" + wiName + "'," +
                "1," +
                "1," +
                "'N')";
    }
    public String getSetupMarketWindowQuery(String refId, String wiName, String process, String marketType, String landingMessage, String minPrincipalAmount, String reDiscountRateLess90, String reDiscountRateLess180, String reDiscountRateLess270, String reDiscountRateLess364, String openDate, String closeDate) {
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,MINPRINCIPALAMOUNT,REDISCOUNTRATELESS90,REDISCOUNTRATELESS180,REDISCOUNTRATELESS270,REDISCOUNTRATELESS364,OPENDATE,CLOSEDATE)" +
                "values ('" + refId + "','" + wiName + "','" + process + "','" + marketType + "','" + landingMessage + "','" + minPrincipalAmount + "','" + reDiscountRateLess90 + "','" + reDiscountRateLess180 + "','" + reDiscountRateLess270 + "','" + reDiscountRateLess364 + "','" + openDate + "','" + closeDate + "')";
    }
    public String getSetupMarketWindowQuery(String refId, String wiName, String process, String marketType, String landingMessage,String minimumPrincipal, String openDate, String closeDate) {
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,MINPRINCIPALAMOUNT,OPENDATE,CLOSEDATE)" +
                " values ('" + refId + "','" + wiName + "','" + process + "','" + marketType + "','" + landingMessage + "','"+minimumPrincipal+"','" + openDate + "','" + closeDate + "')";
    }
    public String getSetupMarketWindowQuery(String refId, String wiName, String process, String marketType, String landingMessage, String openDate, String closeDate) {
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,MINPRINCIPALAMOUNT,OPENDATE,CLOSEDATE)" +
                "values ('" + refId + "','" + wiName + "','" + process + "','" + marketType + "','" + landingMessage + "','" + openDate + "','" + closeDate + "')";
    }
    public String getCheckActiveWindowQuery(String process, String marketType) {
        return "select count(*) from mm_setup_tbl where process = '" + process + "' and markettype ='" + marketType + "' and  closeflag = 'N'";
    }
    public String getCheckActiveWindowQueryRefid(String process, String marketType) {
        return "select REFID from mm_setup_tbl where process = '" + process + "' and markettype ='" + marketType + "' and  closeflag = 'N'";
    }
    public String getActiveWindowDetailsQuery(String process, String markType) {
        return "select refid, landingmessage, minprincipalamount from mm_setup_tbl where process = '" + process + "' and markettype ='" + markType + "' and  closeflag = 'N'";
    }
    public String getClosedMarketWinRefIDQuery(String process, String marketType,String refid){
        return "select REFID from mm_setup_tbl where process = '"+process+"' "
        		+ "and upper(markettype) = upper('"+marketType+"') and upper(refid) = upper('"+refid+"') and upper(closeflag) = ('Y')";
    }
    public String getClosedMarketWinRefIDQuery(String process, String marketType,String refid,String winame){
        return "select REFID from mm_setup_tbl where process = '"+process+"' "
        		+ "and upper(markettype) = upper('"+marketType+"') and upper(refid) = upper('"+refid+"') and upper(closeflag) = ('Y') and  upper(winame) =upper('"+winame+"')";
    }
    
    //get window close date of a refid
    public String getWinCloseDateByIdQuery(String winRefId){
        return "select CLOSEDATE from mm_setup_tbl where refid = '"+winRefId+"";
    }
    //get window close date of a market
    public String getWinCloseDateQuery(String process, String marketType){
        return "select CLOSEDATE from mm_setup_tbl where process = '"+process+"' and markettype ='"+marketType+"' and  closeflag = 'N'";
    }
    
    public String getWinCloseFlagById (String winRefId){
        return "select closeflag from mm_setup_tbl where refid = '"+winRefId+"'";
    }
    public String getWinDetailsByIdQuery(String winRefId) {
        return "select * from mm_setup_tbl where refid = '" + winRefId + "";
    }
   /* public String getWinCloseFlagById(String winRefId) {
        return "select closeflag from mm_setup_tbl where refid = '" + winRefId + "'";
    }*/
    public String getUpdateSetupQuery(String columnName, String value, String condition) {
        return "update mm_setup_tbl set " + columnName + " = " + value + " where condition = " + condition + "";
    }
    public String getCheckActiveWindowByIdQuery(String winRefId) {
        return "select COUNT(closeflag) from mm_setup_tbl where refid = '" + winRefId + "' and closeflag = 'N'";
    }
    public String getSetupBidQuery(String reqDate, String custRefId, String winRefId, String wiName, String process, String marketType, String custAcctNo, String custName, String custEmail, String custPrincipal, String tenor, String rateType, String rate) {
        return "insert into mm_bid_tbl (reqDate,custRefId,winRefId,bidwiname,process,marketType,custAcctNo,custName,custEmail,custPrincipal,tenor,rateType,rate) values ('" + reqDate + "','" + custRefId + "','" + winRefId + "','" + wiName + "', '" + process + "', '" + marketType + "', '" + custAcctNo + "','" + custName + "','" + custEmail + "','" + custPrincipal + "','" + tenor + "','" + rateType + "','" + rate + "')";
    }


    public String getCustomerRefIdQuery (String custrefId) {
        return "select * from MONEYMARKET_EXT where upper(refid) = upper('"+custrefId+"')";
    }

    public String getSetupCpSmBidQuery(String reqDate, String custRefId, String winRefId, String wiName, String process, String marketType, String custAcctNo, String custName, String custEmail, String custPrincipal, String tenor, String rate,String maturityDate) {
        return "insert into mm_bid_tbl (reqDate,custRefId,winRefId,bidwiname,process,marketType,custAcctNo,custName,custEmail,custPrincipal,tenor,rate,maturitydate) values ('" + reqDate + "','" + custRefId + "','" + winRefId + "','" + wiName + "', '" + process + "', '" + marketType + "', '" + custAcctNo + "','" + custName + "','" + custEmail + "','" + custPrincipal + "','" + tenor + "','" + rate + "', '"+maturityDate+"')";
    }
    public String getTBInsertSetupQuery() {
    return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,OPENDATE,CLOSEDATE,CLOSEFLAG) values (values)";
}
    /*public String getCustomerRefIdQuery(String custrefId) {
        return "select * from MONEYMARKET_EXT where upper(refid) = upper('" + custrefId + "')";
    }*/
    public String getCpPmBidsToProcessQuery () {
        return "select custrefid, tenor, rate, ratetype from mm_bid_tbl where process = 'Commercial Paper' and markettype= 'primary' and processflag ='N' and groupindexflag = 'N'";
    }
    public String getCpPmUpdateBidsQuery (String id, String utilityWiName, String groupIndex){
        return "update mm_bid_tbl set utilitywiname = '" + utilityWiName + "', groupindex = '" + groupIndex + "', groupindexflag = 'Y' , processflag = 'Y' where custrefid = '" + id + "'";
    }
    public String getCpPmSummaryBidsQuery(String utilityWiName){
        return "select tenor , rate , sum(custprincipal) as TotalAmount ,ratetype, count(*) as TransactionCount, groupindex from mm_bid_tbl where utilitywiname = '" + utilityWiName + "' group by tenor, rate ,ratetype,groupindex";
    }
    public String getCpPmGroupBidsQuery(String groupIndex){
        return "select custrefid,custacctno, custname, tenor, rate, custprincipal  from mm_bid_tbl where groupindex = '"+groupIndex+"'";
    }
    public String getCpPmBidRateTypeQuery(String id){
        return "select ratetype from mm_bid_tbl where custrefid = '"+id+"'";
    }
    public String getCpPmBidDetailByIdQuery(String id, String detail){
        return "select "+detail+" from mm_bid_tbl where custrefid = '"+id+"'";
    }
    public String getCpPmBidUpdateBankQuery(String id, String cpRate,String bankRate, String maturityDate, String allocationPercent, String bidStatus, String status){
        return "update mm_bid_tbl set cprate = '"+cpRate+"', rate = '"+bankRate+"', maturitydate = '"+maturityDate+"', allocationpercentage = '"+allocationPercent+"', bidstatus = '"+bidStatus+"', status = '"+status+"', failedflag = 'N', terminateflag = 'N' where custrefid = '"+id+"' ";
    }
    public String getCpPmBidUpdatePersonalQuery(String id, String cpRate, String maturityDate, String allocationPercent, String bidStatus, String status){
        return "update mm_bid_tbl set cprate = '"+cpRate+"', maturitydate = '"+maturityDate+"', allocationpercentage = '"+allocationPercent+"', bidstatus = '"+bidStatus+"', status = '"+status+"', failedflag = 'N', terminateflag = 'N' where custrefid = '"+id+"' ";
    }
    public String getCpPmUpdateFailedBidsQuery(String id, String bidStatus){
        return "update mm_bid_tbl set bidstatus = '"+bidStatus+"', cprate = '', allocationpercentage = '', maturitydate = ''  , status = 'Awaiting Reversal', failedflag = 'Y', terminateflag = 'Y' where custrefid = '"+id+"'";
    }
    public String getSetSmInvestmentQuery(String id, String wiName, String process, String marketType, String winRef,String corporateName,String description,String maturityDate,String billAmount,String availableAmount,String rate,String tenor,String dtm,String status,String guaranteedCp,String openDate, String closeDate, String date){
        return "insert into mm_sminvestments_tbl (investmentid,winame,process,marketType,windowrefno,corporateissuername,description,maturitydate,billamount,availableamount,rate,tenor,dtm,status,guaranteedcp,openDate,closeDate,investmentdate) values" +
                " ('"+id+"','"+wiName+"','"+process+"','"+marketType+"','"+winRef+"','"+corporateName+"','"+description+"','"+maturityDate+"','"+billAmount+"','"+availableAmount+"','"+rate+"','"+tenor+"','"+dtm+"', '"+status+"','"+guaranteedCp+"','"+openDate+"','"+closeDate+"','"+date+"')";
    }
    public String getCpSmInvestmentsQuery(String process, String marketType){
        return "select investmentid,corporateissuername, description, maturitydate,dtm,status, availableamount,rate, totalamountsold, mandates  from mm_sminvestments_tbl " +
                "where process = '"+process+"' and  markettype= '"+marketType+"' and closedflag = 'N' and maturedflag = 'N' and cancelledflag = 'N'";
    }
    public String getCpSmInvestmentsUpdateQuery (String id,String availableAmount, String totalAmountSold, String mandate ){
        return "update mm_sminvestments_tbl set availableamount = "+availableAmount+", totalamountsold = "+totalAmountSold+", mandates = "+mandate+" where investmentid = '"+id+"'";
    }
    public String getCpSmInvestmentsSelectQuery(String id){
        return "select tenor, rate,availableamount,totalamountsold,mandates  from mm_sminvestments_tbl where investmentid = '"+id+"'";
    }
    public static String getUpdateCutoffTimeQuery(String id, String closeDate){
        return "update mm_setup_tbl set closedate = '"+closeDate+"' where refid = '"+id+"'";
    }
    public String getTbPmBidSummaryQuery(String refid){
    	return "select tb_request_type,tb_pm_tenor, sum(tb_pm_mpBr) as TotalAmount,tb_rate_type, count(*) as TransactionCount,tb_pm_personal from moneymarket_ext " + 
    	"where tb_request_type is not null and tb_uniqueNum in (select refid from mm_setup_tbl where closeflag ='Y' and refid='"+refid+"') group by " + 
    	"tb_request_type,tb_pm_tenor,tb_rate_type,tb_pm_personal";
      }
    //with personal rate --UPDATE ADD WORKSTEP--
    public String getTbPmCustomerRqstyQuery(String refid,String rqstType, String tenor,String rateType,String personalRate){
    	return "select tb_request_type,tb_pm_custId, tb_custAcctNum,tb_custAcctName, tb_pm_tenor,tb_rate_type,tb_pm_personal,tb_pm_mpBr,winame,tb_rollover_type from moneymarket_ext " + 
    	"where tb_request_type is not null and tb_uniqueNum in (select refid from mm_setup_tbl where closeflag ='Y' and refid='"+refid+"') and tb_request_type ='"+rqstType+"'"
    			+ " and tb_pm_tenor ='"+tenor+"' and tb_rate_type='"+rateType+"' and tb_pm_personal = '"+personalRate+"'";
      }
    //query with bank rate
    public String getTbPmCustomerRqstyQuery(String refid,String rqstType, String tenor,String rateType){
    	return "select  tb_request_type,tb_pm_custId, tb_custAcctNum,tb_custAcctName, tb_pm_tenor,tb_rate_type,tb_pm_personal,tb_pm_mpBr,winame, tb_rollover_type,"
    			+ "tb_cbnRate,tb_bankrate,tb_maturity_date,tb_allocation_p,tb_bidStatus,tb_status from moneymarket_ext " + 
    	"where tb_request_type is not null and tb_uniqueNum in (select refid from mm_setup_tbl where closeflag ='Y' and refid='"+refid+"') and tb_request_type ='"+rqstType+"'"
    			+ " and tb_pm_tenor ='"+tenor+"' and tb_rate_type='"+rateType+"'";
      }
    
    public String getTbPmBidUpdateBankQuery(String wino, double cbnRate,double bankRate,String maturityDate, double allocation, String bidStatus, String status){
        return "update moneymarket_ext set tb_cbnRate = '"+cbnRate+"', tb_bankRate = '"+bankRate+"', tb_maturity_date = '"+maturityDate+"', tb_allocation_p = '"+allocation+"'"
        		+ ", tb_bidStatus = '"+bidStatus+"', tb_status = '"+status+"' where winame = '"+wino+"' ";
    }
    public String getTbPmBidUpdatePersonalQuery(String wino, double cbnRate,String maturityDate, double allocation, String bidStatus, String status){
    	  return "update moneymarket_ext set tb_cbnRate = '"+cbnRate+"', tb_maturity_date = '"+maturityDate+"', tb_allocation_p = '"+allocation+"'"
          		+ ", tb_bidStatus = '"+bidStatus+"', tb_status = '"+status+"' where winame = '"+wino+"' "; 
    }
    public String getTbPmUpdateFailedBidsQuery(String wino, String bidStatus){
        return "update  moneymarket_ext  set tb_bidStatus = '"+bidStatus+"' where winame = '"+wino+"'"; 
    }
    //get all bids that are have no allocation
    public String getTbPmNoBidAllocationQuery (String refid){
        return "select winame from moneymarket_ext  where refid = '"+refid+"' and Status ='Awaiting Treasury' and (tb_bidStatus is null or tb_bidstatus ="+""+")";
    }

   /* public String getMarketRefIdQuery (String winame) {
        return "select refid from MONEYMARKET_EXT where winame = '"+winame+"'";
    }*/
    /*
     * get attached documents
     * @param winame
     * @param document name
     */
    public String getUploadedDocQuery(String winame,String docName){
        return  "SELECT name " +
	            "FROM pdbdocument A, PDBDocumentContent B " +
	            "WHERE A.DocumentIndex=B.DocumentIndex " +
	            "AND B.ParentFolderIndex=(select FolderIndex from PDBFolder where name ='"+ winame + "') " +
	            "AND upper(A.Name) like '"+docName+"%'";
      }
    
    //get rediscount rate from the setup table for a market
    public static String getReDiscounteQuery(String id){
        return "select rediscountrateless90, rediscountrateless180, rediscountrateless270, rediscountrateless364 from mm_setup_tbl where refid = '"+id+"'";
    }
    
    /*
     * update rediscount rate
     */
    public String getUpdateRdRateQuery(String refid, String winame,String tb90, String tb180, String tb270, String tb364){
        return "update mm_setup_tbl set REDISCOUNTRATELESS90 = '"+tb90+"', REDISCOUNTRATELESS180 = '"+tb180+"',"
        		+ "REDISCOUNTRATELESS270 = '"+tb270+"', REDISCOUNTRATELESS364 = '"+tb364+"', where refid = '"+refid+"' and winame ='"+winame+"'";
    }
    //update minimum principal
    public String getUpdateminPrincipalQuery(String refid, String minPrincipal){
        return "update mm_setup_tbl set MINPRINCIPALAMOUNT = '"+minPrincipal+"' where refid = '"+refid+"'";
    }
    //get minimum principal
    public String getSmMinPrincipalQuery(String refid){
        return "select MINPRINCIPALAMOUNT from moneymarket_ext where refid = '"+refid+"'";
    }
    //get tb Secondary Available amt and status
    public String getSmAvailableAmtQuery(String invesmentid){
        return "(select tbillAmt - totalamountsold ), Status, from tb_SmIssuedBills where insertionorderid = '"+invesmentid+"'";
    }
    //get all colums in issued treasurybids workstep
    public String tbGetSmIssuedBidsQuery(String refid){
        return "select MaturityDate,Tenor,Status,TBillAmount,tbRate,Mandates,TOTALAMOUNTSOLD,insertionorderid from tb_SmIssuedBills where upper(status) = upper('Open')  and winame in (select winame from  mm_setup_tbl where refid ='"+refid+"')";
    }
    //update mandates
    public String updateTbIBMandateAndTAmt(String invesmentid,double bidAmt){
        return "update tb_SmIssuedBills set totalamountsold = totalamountsold + "+bidAmt+", mandate =mandate+1 where insertionorderid = '"+invesmentid+"'";
    }
    public static String getTbCustMandateDetailsQuery(String custId, String acctno,String marketType){
        return "select tb_bidRequestDte,tb_custAcctNum,tb_custUniquerefId, tb_custAcctNum, tb_custAcctName,tb_principalAmt,tb_bidStatus,"
        		+ " tb_sec_maturityDte, tb_sec_pm, tb_sec_intMaturity, tb_sec_maturityDate,tb_SmInvestmentId, tb_sec_tnor,tb_sec_rate,"
        		+ "tbMarketUniqueRefId from MONEYMARKET_EXT "
        		+ "where upper(g_select_market) =upper('tb_market') and upper(tb_select_market) = upper('"+marketType+"') and "
        				+ "upper(tb_pm_custId) = upper('"+custId+"') or upper(tb_custAcctNum) = upper('"+acctno+"')";	
    }
    public static String getTbCustMandateDetailsQuery(String custId,String marketType){
        return "select TB_MATURITY_DATE,tb_principalAmt,TB_PM_TENOR,tb_bidRequestDte,tb_custAcctNum,tb_custUniquerefId, tb_custAcctNum, tb_custAcctName,tb_bidStatus,"
        		+ " tb_sec_maturityDte, tb_sec_pm, tb_sec_intMaturity, tb_sec_maturityDate,tb_SmInvestmentId, tb_sec_tnor,tb_sec_rate,"
        		+ "tbMarketUniqueRefId from MONEYMARKET_EXT "
        		+ "where upper(g_select_market) =upper('tb_market') and upper(tb_select_market) = upper('"+marketType+"') and "
        				+ "upper(tb_pm_custId) = upper('"+custId+"')";	
    }
    //get details of a customers mandate using the customer unique id
    public static String getTbCustMandate(String marketType, String data){
        return "select tb_bidRequestDte,tb_custUniquerefId, tb_custAcctNum, tb_custAcctName,tb_principalAmt,tb_bidStatus,TB_MATURITY_DATE,tb_pm_custId,tb_uniqueNum from MONEYMARKET_EXT "
        		+ "where upper(g_select_market) =upper('tb_market') and upper(tb_select_market) = upper('"+marketType+"') "
        				+ "and (upper(tb_pm_custId) = upper('"+data+"') or upper(tb_custAcctNum) = upper('"+data+"'))";	
    }
    //
   
  
    ////-----------------------treasury end -------------------------------------///////////////
    public static String getUpdateReDiscountRateQuery(String id,String redicsountless90, String rediscount91180, String rediscount181270, String rediscount271364){
        return "update mm_setup_tbl set REDISCOUNTRATELESS90 = '"+redicsountless90+"', REDISCOUNTRATELESS180 = '"+rediscount91180+"', REDISCOUNTRATELESS270 = '"+rediscount181270+"', REDISCOUNTRATELESS364 = '"+rediscount271364+"' where  refid = '"+id+"'";
    }
    
  
    public static String getBidForTerminationQuery(String process, String marketType, String data){
        return "SELECT reqdate,custrefid,custprincipal,custacctno,custname,maturitydate,status,winrefid FROM MM_BID_TBL where process = '"+process+"' and markettype = '"+marketType+"' and  awaitingmaturityflag = 'Y' and (custrefid = '"+data+"' or custacctno = '"+data+"' )";
    }
    public static String getCpBidDtlForTerminationQuery(String id, String marketType){
        return "SELECT custprincipal,maturitydate FROM MM_BID_TBL where process = 'Commercial Paper' and markettype = '"+marketType+"' and custrefid = '"+id+"'";
    }
    public static String getCpReDiscountedRateForTermQuery(String id){
        return "select rediscountrateless90, rediscountrateless180, rediscountrateless270, rediscountrateless364 from mm_setup_tbl where refid = '"+id+"'";
    }
    public static String getCpLienStatusQuery(String id){
        return "select count (lienflag) from mm_bid_tbl where custrefid = '"+id+"' and lienflag = 'Y'";
    }
    public static String getCpCustIdExistQuery(String id, String marketType){
        return "select count(custRefId) from mm_bid_tbl where custrefid = '"+id+"' and process = 'Commercial Paper' and marketType = '"+marketType+"'";
    }

    public static String getCpLienProcessQuery(String id, String marketType, String flag){
        return "update mm_bid_tbl set lienFlag = '"+flag+"' where custrefid = '"+id+"' and process = 'Commercial Paper' and markettype = '"+marketType+"'";
    }
    public static String getCpPoiMandateSearchQuery(String marketType, String data){
        return "select reqdate, custrefid,custprincipal,custacctno,custname,status from mm_bid_tbl where process = 'Commercial Paper' and marketType = '"+marketType+"' and (custacctno = '"+data+"' or custrefid = '"+data+"')";
    }
    public static String getCpPoiDtlQuery(String id){
        return "select reqdate,custrefid,custprincipal,custacctno,custname,principalatmaturity,interest,maturitydate,tenor,rate from mm_bid_tbl where custrefid = '"+id+"'";
    }
}
