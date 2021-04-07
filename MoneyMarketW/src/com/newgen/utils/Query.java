package com.newgen.utils;

import com.newgen.iforms.custom.IFormReference;

public class Query {
    public String getSolQuery(String userName){
        return "select sole_id from usr_0_fbn_usr_branch_mapping where upper(user_id) = upper('"+userName+"')";
    }
    public String getUsersInGroup (String groupName){
        return "select username from pdbuser where userindex in (select userindex from pdbgroupmember where groupindex = (select groupindex from PDBGroup where GroupName='"+groupName+"'))";
    }
    public String getMailQuery(String wiName, String sendMail, String copyMail, String mailSubject, String mailMessage){
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
                "'"+LoadProp.mailFrom+"'," +
                "'"+sendMail+"'," +
                "'"+copyMail+"'," +
                "'"+mailSubject+"'," +
                "'"+mailMessage+"'," +
                "'text/html;charset=UTF-8'," +
                "1," +
                "'System'," +
                "'TRIGGER'," +
                "SYSDATE," +
                ""+LoadProp.processDefId+"," +
                "'"+wiName+"'," +
                "1," +
                "1," +
                "'N')";
    }
    public String getSetupMarketWindowQuery (String refId, String wiName,String process,String marketType,String landingMessage,String minPrincipalAmount,String reDiscountRateLess90,String reDiscountRateLess180,String reDiscountRateLess270,String reDiscountRateLess364,String openDate,String closeDate) {
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,MINPRINCIPALAMOUNT,REDISCOUNTRATELESS90,REDISCOUNTRATELESS180,REDISCOUNTRATELESS270,REDISCOUNTRATELESS364,OPENDATE,CLOSEDATE)" +
                "values ('"+refId+"','"+wiName+"','"+process+"','"+marketType+"','"+landingMessage+"','"+minPrincipalAmount+"','"+reDiscountRateLess90+"','"+reDiscountRateLess180+"','"+reDiscountRateLess270+"','"+reDiscountRateLess364+"','"+openDate+"','"+closeDate+"')";
    }
    public String getSetupMarketWindowQuery (String refId, String wiName,String process,String marketType,String landingMessage,String openDate,String closeDate) {
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,OPENDATE,CLOSEDATE)" +
                "values ('"+refId+"','"+wiName+"','"+process+"','"+marketType+"','"+landingMessage+"','"+openDate+"','"+closeDate+"')";
    }
    public String getCheckActiveWindowQuery(String process, String marketType){
        return "select count(*) from mm_setup_tbl where process = '"+process+"' and markettype ='"+marketType+"' and  closeflag = 'N'";
    }
    public String getCheckActiveWindowQueryRefid(String process, String marketType){
        return "select REFID from mm_setup_tbl where process = '"+process+"' and markettype ='"+marketType+"' and  closeflag = 'N'";
    }
    public String getActiveWindowDetailsQuery(String process, String markType){
        return "select refid, landingmessage, minprincipalamount from mm_setup_tbl where process = '"+process+"' and markettype ='"+markType+"' and  closeflag = 'N'";
    }
    public String getWinDetailsByIdQuery(String winRefId){
        return "select * from mm_setup_tbl where refid = '"+winRefId+"";
    }
    public String getWinCloseFlagById (String winRefId){
        return "select closeflag from mm_setup_tbl where refid = '"+winRefId+"'";
    }

    public String getUpdateSetupQuery(String columnName,String value,String condition){
        return "update mm_setup_tbl set "+columnName+" = "+value+" where condition = "+condition+"";
    }

    public String getCheckActiveWindowByIdQuery(String winRefId){
        return  "select COUNT(closeflag) from mm_setup_tbl where refid = '"+winRefId+"' and closeflag = 'N'";
    }

    public String getSetupBidQuery(String reqDate,String custRefId, String winRefId,String wiName, String process, String marketType,String custAcctNo, String custName, String custEmail, String custPrincipal, String tenor, String rateType, String rate ){
        return "insert into mm_bid_tbl (reqDate,custRefId,winRefId,bidwiname,process,marketType,custAcctNo,custName,custEmail,custPrincipal,tenor,rateType,rate) values ('"+reqDate+"','"+custRefId+"','"+winRefId+"','"+wiName+"', '"+process+"', '"+marketType+"', '"+custAcctNo+"','"+custName+"','"+custEmail+"','"+custPrincipal+"','"+tenor+"','"+rateType+"','"+rate+"')";
    }

    public String getTBInsertSetupQuery (){
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,OPENDATE,CLOSEDATE,CLOSEFLAG) values (values)";
    }


    public String getCustomerRefIdQuery (String custrefId) {
        return "select * from MONEYMARKET_EXT where upper(refid) = upper('"+custrefId+"')";
    }

    public String getCpPmBidsToProcessQuery(){
        return "select custrefid, tenor, rate, ratetype from mm_bid_tbl where process = 'Commercial Paper' and markettype= 'primary' and processflag ='N' and groupindexflag = 'N'";
    }

    public String getCpPmUpdateBidsQuery(String id,String utilityWiName, String groupIndex){
        return "update mm_bid_tbl set utilitywiname = '"+utilityWiName+"', groupindex = '"+groupIndex+"', groupindexflag = 'Y' , processflag = 'Y' where custrefid = '"+id+"'";
    }

    public String getCpPmBidGroupQuery(String utilityWiName){
      return "select tenor , rate , sum(custprincipal) as TotalAmount ,ratetype, count(*) as TransactionCount, groupindex from mm_bid_tbl where utilitywiname = '"+utilityWiName+"' group by tenor, rate ,ratetype,groupindex";

    }
}
