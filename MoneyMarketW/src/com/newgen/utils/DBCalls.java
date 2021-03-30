package com.newgen.utils;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.newgen.iforms.custom.IFormReference;

public class DBCalls {
	 private static Logger logger = LogGen.getLoggerInstance(DbConnect.class);
    private IFormReference ifr;
    private String qryStr;
    private List<List<String>> dbResultList = new ArrayList<List<String>>();
    private int dbResultInt;
    
    
    public DBCalls() {
    }
    
    //insert setup details
    public int insertSetupDetails(String refid, String winame, String process, String markettype, String landingmessage,String opendate, String closedate){
    	logger.info("insertSetupDetails>>>");
    	setQryStr("insert into MM_SETUP_TBL(refid,winame,process,markettype,landingmessage values('"+refid+"','"+winame+"','"+process+"','"+markettype+"','"+landingmessage+"'");
    	logger.info("insertSetupDetails>>>" +getQryStr());
    	return ifr.saveDataInDB(getQryStr());
    	//setDbResultInt(ifr.getDataFromDB(getQryStr()));
    }
  //insert setup details
    public int insertSetupDetails(String refid, String winame, String process, String markettype, String landingmessage,Date opendate, Date closedate,char closeflag){
    	logger.info("insertSetupDetails>>>" + winame);
    	setQryStr("insert into MM_SETUP_TBL(refid,winame,process,markettype,landingmessage values('"+refid+"','"+winame+"','"+process+"','"+markettype+"','"+landingmessage+"'");
    	logger.info("insertSetupDetails>>>"+ winame+">>> " +getQryStr());
    	return ifr.saveDataInDB(getQryStr());
    			//setDbResultInt(ifr.getDataFromDB(getQryStr()));
    }
    
    //insert without flag -- keep flag at default value 'N'
    public int insertSetupDetails(String refid, String winame, String process, String markettype, String landingmessage,
    		Date opendate, Date closedate){
    	logger.info("insertSetupDetails>>>");
    	setQryStr("insert into MM_SETUP_TBL(refid,winame,process,markettype,landingmessage values('"+refid+"','"+winame+"','"+process+"','"+markettype+"','"+landingmessage+"'");
    	logger.info("insertSetupDetails>>>" +getQryStr());
    	return ifr.saveDataInDB(getQryStr());
    			//setDbResultInt(ifr.getDataFromDB(getQryStr()));
    }
    public int upDateSetUpLandingMsg(String refid, String winame, String process, String markettype, String landingmsg)
	{
		qryStr = "UPDATE MM_SETUP_TBL SET landingmessage='"+landingmsg+"' WHERE refid= '"+refid+"' and winame ='"+winame+"' and process ='"+process+"'";
		logger.info("qryStr>>>>"+qryStr);
		return  ifr.saveDataInDB(qryStr);
	}
  //check if market has been set
    public boolean isMarketSet(String markettype, String process){
    	logger.info("isMarketSet>>>");
    	setQryStr("select refid from MM_SETUP_TBL where marketype ='"+markettype+"' and process='"+process+"' and closeflag='N'");
    	logger.info("insertSetupDetails>>>" +getQryStr());
    	try {
    		setDbResultList(ifr.getDataFromDB(getQryStr()));
    	}
    	catch(Exception e)
		{
			logger.info("Exception>>>>"+e.toString());
		}
	
    	
    	if(getDbResultList().size()>0)
    		return true;
    	else
    		return false;
    }
    
    //send mail
    public void sendMail(IFormReference ifr, String mailBody, String toMail)
	{
		String wi_name = (String) ifr.getValue("WorkItemName");
		
		qryStr =  "INSERT INTO wfmailqueuetable (MAILFROM, MAILTO, MAILCC, MAILSUBJECT,MAILMESSAGE, MAILCONTENTTYPE, mailpriority,"
		 		+ " insertedby,MAILACTIONTYPE,  insertedtime, processdefid, processinstanceid, workitemid, activityid, MAILSTATUS) "
		 		+ "VALUES ('FirstBank@firstbanknigeria.com', '"+toMail+"', '',"
		 		+ "'FirstBank Nigeria Ltd','"+mailBody+"','text/html;charset=UTF-8',1,'System', "
		 				+ "'TRIGGER', SYSDATE,20,'" + wi_name + "',1,10,'N')";
	    logger.info("dbQuery: "+ qryStr);
	    try
		{
	    	int resp = ifr.saveDataInDB(qryStr);
			logger.info(" sendMail resp> >>>"+resp);
		}
		catch(Exception e)
		{
			logger.info("Exception>>>>"+e.toString());
		}
	
	}
    
    //getters and setters
	String getQryStr() {
		return qryStr;
	}
	void setQryStr(String qryStr) {
		this.qryStr = qryStr;
	}

	List<List<String>> getDbResultList() {
		return dbResultList;
	}

	void setDbResultList(List<List<String>> dbResultList) {
		this.dbResultList = dbResultList;
	}

	private int getDbResultInt() {
		return dbResultInt;
	}

	private void setDbResultInt(int dbResultInt) {
		this.dbResultInt = dbResultInt;
	}

}
