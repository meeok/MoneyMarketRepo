package com.newgen.utils;

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
    public String getCheckActiveWindowQuery(String process, String marketType){
        return "select count(*) from mm_setup_tbl where process = '"+process+"' and markettype ='"+marketType+"' and  closeflag = 'N'";
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
    public String getTBInsertSetupQuery (){
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,OPENDATE,CLOSEDATE,CLOSEFLAG) values (values)";
    }
}
