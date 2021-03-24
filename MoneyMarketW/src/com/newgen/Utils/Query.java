package com.newgen.Utils;

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
                "values ('"+refId+"','"+wiName+"','"+process+"','"+marketType+"','"+landingMessage+"','"+minPrincipalAmount+"',"+reDiscountRateLess90+","+reDiscountRateLess180+","+reDiscountRateLess270+","+reDiscountRateLess364+","+openDate+","+closeDate+")";
    }
    public String getUpdateSetupQuery(String columnName,String value,String condition){
        return "update mm_setup_tbl set "+columnName+" = "+value+" where condition = "+condition+"";
    }
    public String getTBInsertSetupQuery (){
        return "insert into mm_setup_tbl (REFID,WINAME,PROCESS,MARKETTYPE,LANDINGMESSAGE,OPENDATE,CLOSEDATE,CLOSEFLAG) values (values)";
    }
}
