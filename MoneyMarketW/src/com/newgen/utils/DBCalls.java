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
    	logger.info("insertSetupDetails>>>");
    	setQryStr("insert into MM_SETUP_TBL(refid,winame,process,markettype,landingmessage values('"+refid+"','"+winame+"','"+process+"','"+markettype+"','"+landingmessage+"'");
    	logger.info("insertSetupDetails>>>" +getQryStr());
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
  //check if market has been set
    public boolean isMarketSet(String markettype, String process){
    	logger.info("isMarketSet>>>");
    	setQryStr("select refid from MM_SETUP_TBL where marketype ='"+markettype+"' and process='"+process+"' and closeflag='N'");
    	logger.info("insertSetupDetails>>>" +getQryStr());
    	setDbResultList(ifr.getDataFromDB(getQryStr()));
    	if(getDbResultList().size()>0)
    		return true;
    	else
    		return false;
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
