package com.newgen.worksteps;

import com.newgen.api.customService.CpServiceHandler;
import com.newgen.controller.CpController;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.utils.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TreasuryOpsVerifier extends Commons implements IFormServerEventHandler, Constants, CommonsI {
    private static final Logger logger = LogGen.getLoggerInstance(TreasuryOpsVerifier.class);
    @Override
    public void beforeFormLoad(FormDef formDef, IFormReference ifr) {
        clearDecHisFlag(ifr);
        if (!isEmpty(getProcess(ifr))) showSelectedProcessSheet(ifr);
        if (getProcess(ifr).equalsIgnoreCase(commercialProcess)) cpFormLoadActivity(ifr);
        //else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess)) tbFormLoadActivity(ifr);

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

        try{
            switch (event){
                case cpApiCallEvent:{
                    switch (control) {
                        case cpTokenEvent: return new CpServiceHandler(ifr).validateTokenTest();
                        case cpPostEvent:{
                            if (getCpMarket(ifr).equalsIgnoreCase(cpPrimaryMarket)) {
                               return new CpServiceHandler(ifr).postTransactionTest();
                            }
                            else if (getCpMarket(ifr).equalsIgnoreCase(cpSecondaryMarket)) {
                                if (cpCheckWindowStateById(ifr, getCpWinRefId(ifr))) return new CpServiceHandler(ifr).postTransactionTest();
                                else return cpValidateWindowErrorMsg;
                            }
                        }
                    }
                }
                case formLoad:
                case onLoad:
                case onClick:{
                    switch (control){

                    }
                }
                case onChange:
                case custom:
                case onDone:{
                    switch (control){

                    }
                }
                break;
                case decisionHistory: {
                    if (getProcess(ifr).equalsIgnoreCase(commercialProcess))
                        setCpDecisionHistory(ifr);
                    else if (getProcess(ifr).equalsIgnoreCase(treasuryProcess))
                        setTbDecisionHistory(ifr);
                }
                break;
                case sendMail:
            }
        }
        catch (Exception e){
            logger.error("Exception occurred-- "+ e.getMessage());
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

    @Override
    public void cpSendMail(IFormReference ifr) {
        if (getPrevWs(ifr).equalsIgnoreCase(treasuryOfficerVerifier) || getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
            if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
                if (getCpDecision(ifr).equalsIgnoreCase(decReject)){
                    message = "A request for Commercial Paper with number "+getCpTermCusId(ifr)+"  was not approved by Money_Market_TreasuryOps_Verifier  and discarded. Workitem No. "+getWorkItemNumber(ifr)+".";
                }
            }
        }

    }

    @Override
    public void cpFormLoadActivity(IFormReference ifr) {
        hideCpSections(ifr);
        hideShowLandingMessageLabel(ifr,False);
        hideShowBackToDashboard(ifr,False);
        clearFields(ifr,new String[]{cpRemarksLocal,cpDecisionLocal});

        if (getPrevWs(ifr).equalsIgnoreCase(branchVerifier)){
          if (getCpMandateType(ifr).equalsIgnoreCase(cpMandateTypeTerminate)){
              setMandatory(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              enableFields(ifr,new String[]{cpDecisionLocal,cpRemarksLocal,cpTokenLocal});
              setVisible(ifr,new String[]{cpMandateTypeSection,cpTerminationDetailsSection,cpPostSection,cpDecisionSection,cpTerminationSection,cpTermMandateTbl,cpTermSpecialRateLocal, getCpTermIsSpecialRate(ifr) ? cpTermSpecialRateValueLocal : cpTermSpecialRateLocal});
              if (getCpTerminationType(ifr).equalsIgnoreCase(cpTerminationTypePartial)){
                  setVisible(ifr,new String[]{cpTermAmountDueLocal,cpTermAdjustedPrincipalLocal,cpTermPartialOptionLocal,cpTermPartialAmountLocal,cpTermPartialOptionLocal});
              }
              setTerminationDetails(ifr);
          }
        }
        cpSetDecision(ifr);
    }

    @Override
    public void cpSetDecision(IFormReference ifr) {
        setDecision(ifr,cpDecisionLocal,new String[]{decApprove,decReject});
    }

    private void setTerminationDetails(IFormReference ifr){
        resultSet = new DbConnect(ifr,Query.getCpTermDetailsQuery(getCpTermCusId(ifr))).getData();
        logger.info("resultSet-- "+resultSet);
        double penaltyCharge;

        if (!isEmpty(resultSet)){
            String tenor = resultSet.get(0).get(0);
            logger.info("tenor-- "+tenor);
            String maturityDate = resultSet.get(0).get(1);
            logger.info("maturityDate-- "+maturityDate);
            String principal = resultSet.get(0).get(2);
            logger.info("principal-- "+principal);
            long daysDue = getDaysBetweenTwoDates(getCpTermBoDate(ifr),maturityDate);
            logger.info("daysDue-- "+daysDue);

            if (isLeapYear())
                penaltyCharge = Double.parseDouble(principal) *  getPercentageValue(getCpTermRate(ifr)) * ((double) daysDue / 365 + (double) daysDue / 366);
            else
                penaltyCharge = Double.parseDouble(principal) * getPercentageValue(getCpTermRate(ifr)) * ((double) daysDue / 366);

            logger.info("penaltyCharge-- "+penaltyCharge);

            setFields(ifr,new String[]{cpTermTenorLocal,cpTermMaturityDateLocal,cpTermPenaltyChargeLocal,cpTermNoDaysDueLocal},new String[]{tenor,maturityDate,String.valueOf(penaltyCharge),String.valueOf(daysDue)});
        }
    }
}
