package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.Commons;
import com.newgen.utils.Constants;
import com.newgen.utils.DbConnect;
import com.newgen.utils.Query;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilityTemp extends Commons implements IFormServerEventHandler , Constants {
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        startUtility(ifr);
        setFields(ifr,utilityFlagLocal,flag);
    }

    @Override
    public String setMaskedValue(String s, String s1) {
        return null;
    }

    @Override
    public JSONArray executeEvent(FormDef formDef, IFormReference iFormReference, String s, String s1) {
        return null;
    }

    @Override
    public String executeServerEvent(IFormReference iFormReference, String s, String s1, String s2) {
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

    private void startUtility (IFormReference ifr){
        String wiName = getWorkItemNumber(ifr);
        DbConnect dbConnect = new DbConnect(ifr,new Query().getCpPmBidsToProcessQuery());
        for (int i = 0; i < dbConnect.getData().size(); i++){
            String id = dbConnect.getData().get(i).get(0);
            String tenor = dbConnect.getData().get(i).get(0);
            String rate = dbConnect.getData().get(i).get(1);
            String rateType = dbConnect.getData().get(i).get(2);
            String groupIndex = getCpGroupIndex(wiName,tenor,rateType,rate);
            new DbConnect(ifr,new Query().getCpPmUpdateBidsQuery(id,wiName,groupIndex)).saveQuery();
        }
    }
    private String getCpGroupIndex(String wiName,String tenor,String rateType, String rate){
        String strPattern = "^0+(?!$)";
        String groupLabel = "CPPMG";
        String pRateLabel = "P";
        String bRateLabel = "B";
        String [] wiNameArray = wiName.split("-");
        return new StringBuilder().append(groupLabel).append(wiNameArray[1].replaceAll(strPattern, "")).append(tenor).append(isPRate(rateType) ? pRateLabel : bRateLabel).append(isPRate(rateType) ? rate : "").toString();
    }

    private boolean isPRate(String rateType){
        String pRateType = "Personal";
        return rateType.equalsIgnoreCase(pRateType);
    }
}
