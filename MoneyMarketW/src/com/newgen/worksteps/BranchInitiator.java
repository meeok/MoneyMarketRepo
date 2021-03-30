package com.newgen.worksteps;

import com.newgen.processMethods.CpController;
import com.newgen.utils.Commons;
import com.newgen.utils.CommonsI;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.LogGen;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BranchInitiator extends Commons implements IFormServerEventHandler, CommonsI {
     private static final Logger logger = LogGen.getLoggerInstance(BranchInitiator.class);

    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        beforeFormLoadActivity(ifr);
    }

    @Override
    public String setMaskedValue(String s, String s1) {
        return s1;
    }

    @Override
    public JSONArray executeEvent(FormDef formDef, IFormReference iFormReference, String s, String s1) {
        return null;
    }

    @Override
    public String executeServerEvent(IFormReference ifr, String control, String event, String data) {
        try {
            switch (event){
                case formLoad:
                case onLoad:
                case cpApiCallEvent:{
                  String resp = CpController.fetchAccountDetailsController(ifr);
                  if (isEmpty(resp)) return CpController.fetchLienController(ifr);
                  else if (resp.equalsIgnoreCase(cpEmailMsg)) return cpEmailMsg + "#" + CpController.fetchLienController(ifr);
                  else return resp;
                }
                case onClick:{
                    switch (control){
                        case goToDashBoard:{
                            backToDashboard(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                                cpBackToDashboard(ifr);
                            clearFields(ifr,new String[] {selectProcessLocal});
                            break;
                        }
                    }
                }
                break;
                case onChange:{
                    switch (control){
                        case onChangeProcess: {
                            selectProcessSheet(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
                            if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbFormLoad(ifr);
                            break;
                        }
                        case cpOnSelectMarket:{
                            setVisible(ifr,new String[]{cpCategoryLocal});
                            enableFields(ifr,new String[]{cpCategoryLocal});
                            setMandatory(ifr,new String[]{cpCategoryLocal});
                        }
                        break;
                        case cpOnSelectCategory:{return cpSelectCategory(ifr);}
                        case cpOnChangeRateType:{
                            if (getCpPmRateType(ifr).equalsIgnoreCase(rateTypePersonal)){
                                setVisible(ifr,new String[]{cpPmPersonalRateLocal});
                                setMandatory(ifr,new String[]{cpPmPersonalRateLocal});
                                enableFields(ifr,new String[]{cpPmPersonalRateLocal});
                            }
                            else{
                                clearFields(ifr,cpPmPersonalRateLocal);
                                undoMandatory(ifr,cpPmPersonalRateLocal);
                                setInvisible(ifr,cpPmPersonalRateLocal);
                            }
                        }
                        break;
                        case cpCheckPrincipalEvent:{
                            if (getCpPmCustomerPrincipal(ifr) < getCpPmWinPrincipalAmt(ifr)) {
                                clearFields(ifr,cpPmPrincipalLocal);
                                return minPrincipalErrorMsg;
                            }
                        }
                        break;
                        case  cpCheckTenorEvent:{
                            if (getCpPmTenor(ifr) < 7 || getCpPmTenor(ifr) > 270){
                                clearFields(ifr,cpPmTenorLocal);
                                return tenorErrorMsg;
                            }
                            break;
                        }
                    }
                }
                break;
                case custom:
                case onDone:{
                    switch (control){
                        case validateWindowEvent:{
                            if (cpCheckWindowStateById(ifr)){
                                setFields(ifr,cpPmCustomerIdLocal,cpGenerateCustomerId(ifr));
                            }
                            else return cpValidateWindowErrorMsg;
                        }

                    }
                }
                break;
                case decisionHistory:{
                    if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                        setCpDecisionHistory(ifr);
                    else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess))
                        setTbDecisionHistory(ifr);
                }
                break;
                case sendMail:{
                    if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                        cpSendMail(ifr);

                }
            }
        }
        catch (Exception e){
            logger.error("Exception occurred in executeServerEvent-- "+ e.getMessage());
        }
        return null;
    }

    private String cpSelectCategory(IFormReference ifr) {
        if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)){
            if (getCpCategory(ifr).equalsIgnoreCase(cpCategoryBid)) {
                if (isCpWindowActive(ifr)) {
                    setVisible(ifr,new String[]{cpBranchPriSection,cpCustomerDetailsSection});
                    setMandatory(ifr,new String[]{cpCustomerAcctNoLocal,cpPmTenorLocal,cpPmPrincipalLocal,cpPmRateTypeLocal});
                    enableFields(ifr,new String[]{cpCustomerAcctNoLocal,cpPmTenorLocal,cpPmPrincipalLocal,cpPmRateTypeLocal});
                    setDropDown(ifr,cpPmReqTypeLocal,new String[]{cpPmReqFreshLabel},new String[]{cpPmReqFreshValue});
                    setFields(ifr,cpPmReqTypeLocal,cpPmReqFreshValue);
                    setFields(ifr,new String[]{cpPmReqTypeLocal, cpPmInvestmentTypeLocal},new String[]{cpPmReqFreshValue,cpPmInvestmentPrincipal});
                    setCpPmWindowDetails(ifr);
                } else return windowInactiveMessage;
            }
        }
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


    private void cpBackToDashboard(IFormReference ifr) {
        undoMandatory(ifr,new String [] {cpSelectMarketLocal,cpDecisionLocal,cpRemarksLocal});
        clearFields(ifr,new String [] {cpSelectMarketLocal,cpLandMsgLocal,cpDecisionLocal,cpRemarksLocal});
    }

    @Override
    public void cpSendMail(IFormReference ifr) {

    }
    public void beforeFormLoadActivity(IFormReference ifr){
        hideProcess(ifr);
        hideCpSections(ifr);
        hideTbSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        setGenDetails(ifr);
        clearFields(ifr,new String [] {selectProcessLocal});
        setMandatory(ifr, new String[]{selectProcessLocal});
        setFields(ifr, new String[]{currWsLocal,prevWsLocal},new String[]{getCurrentWorkStep(ifr),na});
    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        cpSetDecision(ifr);
        setVisible(ifr, new String[]{cpDecisionSection, cpMarketSection});
        enableFields(ifr,new String[]{cpSelectMarketLocal});
        setMandatory(ifr,new String [] {cpSelectMarketLocal,cpDecisionLocal,cpRemarksLocal});
        setDropDown(ifr,cpCategoryLocal,new String[]{cpCategoryBid,cpCategoryMandate,cpCategoryReport});
    }
    public void tbFormLoad(IFormReference ifr) {
        cpSetDecision(ifr);
        setVisible(ifr, new String[]{cpDecisionSection, cpMarketSection});
        enableFields(ifr,new String[]{cpSelectMarketLocal});
        setMandatory(ifr,new String [] {cpSelectMarketLocal,cpDecisionLocal,cpRemarksLocal});
        setDropDown(ifr,cpCategoryLocal,new String[]{cpCategoryBid,cpCategoryMandate,cpCategoryReport});
    }


    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decSubmit,decDiscard});

    }
}
