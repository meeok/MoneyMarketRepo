package com.newgen.worksteps;

import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UtilityTemp extends Commons implements IFormServerEventHandler , Constants {
    private static final Logger logger = LogGen.getLoggerInstance(UtilityTemp.class);
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
        try {
            String wiName = getWorkItemNumber(ifr);

            List<List<String>> resultSet = new DbConnect(ifr, Query.getCpPmBidsToProcessQuery()).getData();
            for (List<String> result : resultSet) {
                String id = result.get(0);
                logger.info("id-- " + id);
                String tenor = result.get(1);
                logger.info("tenor-- " + tenor);
                String rate = result.get(2);
                logger.info("rate-- " + rate);
                String rateType = result.get(3);
                logger.info("rateType-- " + rateType);
                String groupIndex = getCpGroupIndex(wiName, tenor, rateType, rate);
                new DbConnect(ifr, Query.getCpPmUpdateBidsQuery(id, wiName, groupIndex)).saveQuery();
            }
        } catch (Exception e){
            logger.info("Exception occurred in fetching details --"+ e.getMessage());
        }
    }
    private String getCpGroupIndex(String wiName,String tenor,String rateType, String rate){
        String strPattern = "^0+(?!$)";
        String groupLabel = "CPPMG";
        String pRateLabel = "P";
        String bRateLabel = "B";
        String [] wiNameArray = wiName.split("-");
        String wiNameTrim = wiNameArray[1];
        wiNameTrim = wiNameTrim.replaceAll(strPattern,"");
        logger.info("wiNameTrim-- "+ wiNameTrim);
        logger.info("tenor-- "+ tenor);
        logger.info("rateType-- "+ rateType);
        logger.info("rate-- "+ rate);
        String groupIndex;

        if (isPRate(rateType))
             groupIndex = groupLabel + wiNameTrim + tenor + pRateLabel + rate;
        else
            groupIndex = groupLabel + wiNameTrim + tenor + bRateLabel;
        logger.info("groupIndex-- "+ groupIndex);
        return groupIndex;
    }

    private boolean isPRate(String rateType){
        String pRateType = "Personal";
        return rateType.equalsIgnoreCase(pRateType);
    }
}
