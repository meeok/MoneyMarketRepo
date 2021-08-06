package com.newgen.worksteps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.LogGen;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.utils.MailSetup;
import com.newgen.utils.XmlParser;

public class TreasuryUpload extends Commons implements IFormServerEventHandler {
    private Logger logger = LogGen.getLoggerInstance(TreasuryUpload.class);


	@Override
	public void beforeFormLoad(FormDef arg0, IFormReference ifr) {
		 clearDecHisFlag(ifr);
	        if(!isEmpty(getProcess(ifr)))showSelectedProcessSheet(ifr);
	        if (getProcess(ifr).equalsIgnoreCase(omoProcess))
	        	omoFormLoad(ifr);
	}


	@Override
	public String executeCustomService(FormDef arg0, IFormReference arg1, String arg2, String arg3, String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray executeEvent(FormDef arg0, IFormReference arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public String executeServerEvent(IFormReference ifr, String control, String event, String data) {
		try {
			switch (event){
			case onDone:{
				switch (control) {
				case omoOnDone:{
					//check if document has been uploaded
					return checkDocumentUploaded(ifr);
				}
			}
			}
			break;
			}
		
			
		}
		catch(Exception e){ 
			e.printStackTrace();
			logger.info("Exception Occurred-- "+ e.getMessage());
	      }
		 
		return null;
	}



	@Override
	public String generateHTML(EControl arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomFilterXML(FormDef arg0, IFormReference arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String introduceWorkItemInWorkFlow(IFormReference arg0, HttpServletRequest arg1, HttpServletResponse arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setMaskedValue(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray validateSubmittedForm(FormDef arg0, IFormReference arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/******* omo Auction Starts *****************/

	private void omoFormLoad(IFormReference ifr) {
		setGenDetails(ifr);
    	hideOmoSections(ifr);
    	setDropDown(ifr,omoDecisiondd,new String[]{decApprove});
    	clearFields(ifr,new String[]{omoRemarkstbx});
    	setVisible(ifr,new String [] {omoDecisionSection,omoMarketSection,omoCustDetailsSection});
    	disableFields(ifr, new String[]{omoMarketSection,omoCustDetailsSection});
    	setMandatory(ifr,new String [] {omoDecisiondd});
    	hideFields(ifr, new String[] {goBackDashboardSection});
    	setVisible(ifr, new String[]{omoMarketSection, omoDecisionSection});
    	hideFields(ifr,new String[] {omoBankName});
	}
	

	private String checkDocumentUploaded(IFormReference ifr) {
		if(!isDocUploaded(ifr,getWorkItemNumber(ifr), suppDocName))
			return "Kindly Attach Documents";
		else
			setFields(ifr,omoDecisiondd,decApprove);
		return "";
		
	}
	
	

}
