package com.newgen.Utils;

public interface Constants {
	String ProcessName = "MoneyMarketW";
	//WorkSteps
	String treasuryOfficerInitiator = "Treasury_Officer_Initiator";
	String treasuryOfficerVerifier = "Treasury_Officer_Verifier";
	String treasuryOfficerMaker = "Treasury_Officer_Maker";
	String treasuryOpsVerifier = "TreasuryOps_Verifier";
	String treasuryOpsMature = "TreasuryOps_Mature";
	String awaitingMaturityUtility = "AwaitingMaturity_Utility";
	String treasuryOpsMatureOnMaturity = "TreasuryOps_Mature_on_maturity";
	String branchInitiator = "Branch_Initiator";
	String branchVerifier = "Branch_Verifier";
	String branchException = "Branch_Exception";
	String rpcVerifier = "RPC_Verifier";
	String treasuryOpsFailed = "TreasuryOps_Failed";
	String awaitingMaturity = "AwaitingMaturity";
	String treasuryOpsSuccessful = "TreasuryOps_Successful";
	String discardWs = "Discard";
	String exit = "Exit";
	String query = "Query";
	// Please input workSteps between comment bracket

	// cp sections
	String cpBranchPriSection = "cp_branchPm_section";
	String cpBranchSecSection = "cp_BranchSec_section";
	String cpLandingMsgSection = "cp_landingMsg_section";
	String cpMarketSection = "cp_market_section";
	String cpPrimaryBidSection = "cp_primaryBid_section";
	String cpTerminationSection = "cp_termination_section";
	String cpProofOfInvestSection = "cp_poi_section";
	String cpDecisionSection = "cp_dec_section";
	String cpTreasuryPriSection = "cp_pmTreasury_section";
	String cpTreasurySecSection = "cp_secTreasury_section";
	String cpTreasuryOpsSecSection = "cp_treasuryOpsSec_section";
	String cpTreasuryOpsPriSection = "cp_treasuryOpsPm_section";
	String cpPostSection = "cp_post_section";
	String cpSetupSection="cp_setup_section";
	String cpCutOffTimeSection = "cp_cutoff_section";
	// end of cp sections

	//general process Ids
	String selectProcessLocal = "g_select_market";
	String processTabName = "tab2";
	String dashboardTab = "0";
	String commercialTab = "1";
	String treasuryTab = "2";
	String omoTab = "3";
	String moneyMarketSection = "g_moneyMarket_section";
	String solLocal = "g_sol";
	String loginUserLocal ="g_loginUser";
	String currWsLocal = "g_currWs";
	String prevWsLocal = "g_prevWs";
	String wiNameLocal ="WorkItemName";
	String decisionHisTable = "g_decisionHistory";
	String dhRowStaffId = "Staff ID";
	String dhRowProcess = "Process";
	String dhRowDecision = "Decision";
	String dhRowRemarks = "Remarks";
	String dhRowPrevWs = "Previous Workstep";
	String dhRowEntryDate = "Entry Date";
	String dhRowExitDate = "Exit Date";
	String dhRowTat = "TAT";
	String dhRowMarketType = "Market Type";
	String entryDateLocal = "EntryDateTime";
	String decHisFlagLocal = "g_decisionHistoryFlag";
	String landMsgLabelLocal = "g_landMsg";
	String goBackDashboardSection = "g_goBackDashboard_section";
	String windowSetupFlagLocal = "g_setupFlag";

	// commercial Paper process ids
	String cpSelectMarketLocal = "cp_select_market";
	String cpRemarksLocal = "cp_remarks";
	String cpDecisionLocal = "cp_decision";
	String cpLandMsgLocal = "cp_landingMsg";
	String cpPrimaryMarket = "cp_primary";
	String cpSecondaryMarket = "cp_secondary";
	String cpCategoryLocal = "cp_category";
	String cpCategorySetup = "Setup";
	String cpCategoryBid = "Bid";
	String cpCategoryReDiscountRate = "Re-discount Rate";
	String cpCategoryCutOff = "Cut off time modification";
	String cpCategoryReport = "Report";
	String cpSetupWindowBtn = "cp_setupWin_btn";
	String cpUpdateCutoffTimeBtn = "cp_updateCutoff_btn";
	String cpSetReDiscountRateBtn = "cp_rediscRate_btn";
	String cpLandingMsgSubmitBtn="cp_landMsgSubmit_btn";
	String cpUpdateLocal = "cp_updateMsg";
	String cpOpenDateLocal = "cp_open_window_date";
	String cpCloseDateLocal = "cp_close_window_date";
	String cpPmMinPriAmtLocal = "cp_mp_amount";

	//common variables
	String omoProcess = "omo_market";
	String omoProcessName = "OMO Auctions";
	String treasuryProcess = "tb_market";
	String treasuryProcessName = "Treasury Bills";
	String commercialProcess = "cp_market";
	String commercialProcessName = "Commercial Paper";
	String visible = "visible";
	String disable = "disable";
	String mandatory = "mandatory";
	String True = "true";
	String False = "false";
	String na = "N.A";
	String decDiscard = "Discard";
	String decSubmit = "Submit";
	String decApprove = "Approve";
	String decReject = "Reject";
	String dbDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	String flag = "Y";
	String endMail = "@firstbanknigeria.com";
	String groupName = "T_USERS";
	String empty = "";
	String mailSubject = "Money Market Workflow Notification";
	String primary = "Primary";
	String secondary = "Secondary";


	//eventName/controlName
	String formLoad = "formLoad";
	String onClick = "onClick";
	String onChange = "onChange";
	String onDone = "onDone";
	String onLoad = "onLoad";
	String custom = "custom";
	String sendMail = "sendMail";
	String onChangeProcess = "onChangeProcess";
	String decisionHistory = "decisionHistory";
	String goToDashBoard = "onClickGoBackToDashboard";
	String cpUpdateMsg = "onClickUpdateMsg";
	String cpOnSelectCategory = "onChangeCategory";
	String cpSetupWindow = "onClickSetup";

	//config
	String logPath = "nglogs/NGF_Logs/MoneyMarket/";
	String configPath = "/was/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/HO-IBPSUTIL01Cell01/MoneyMarketW.ear/MoneyMarketW.war/config/moneymarket.properties";
	String mailFromField ="MAILFROM";
	String processDefIdField = "PROCESSDEFID";
	
	
	/*************TREASURY BILLS STARTS HERE********************/
	//treasury events
	String tbCategoryChange = "tbCategoryChange";
	
	// treasury bills control ids
	String tbSelectMarketLocal = "tb_select_market";
	String tbRemarksLocal = "tb_remarks";
	String tbDecisionLocal = "tb_decision";
	String tbLandMsgLocal = "tb_landingMsg";
	String tbPrimaryMarket = "tb_primary";
	String tbSecondaryMarket = "tb_secondary";
	String tbCategoryLocal = "tb_category";
	
	String tbSetupWindowBtn = "tb_setupWin_btn";
	String tbUpdateCutoffTimeBtn = "tb_updateCutoff_btn";
	String tbSetReDiscountRateBtn = "tb_rediscRate_btn";
	String tbLandingMsgSubmitBtn="tb_landMsgSubmit_btn";
	String tbUpdateLocal = "tb_updateMsg";
	String tbOpenDateLocal = "tb_open_window_date";
	String tbCloseDateLocal = "tb_close_window_date";
	String tbPmMinPriAmtLocal = "tb_mp_amount";
	String tbUniqueRef = "tb_pmwu_ref";
	
	// tb sections
	String tbMarketSection = "tb_market_section";
	String tbLandingMsgSection = "tb_setupmsg_section";
	String tbTreasuryPriSection = "tb_treasuryPm_section";
	String tbTreasurySecSection = "tb_treasurySec_section";
	String tbPrimaryBidSection = "tb_pmBid_section";
	String tbBranchSection = "tb_branch_section";
	String tbTerminationSection = "tb_termination_section";
	String tbProofOfInvestSection = "tb_poi_section";
	String tbDecisionSection = "tb_dec_section";
	String tbTreasuryOpsSection ="tb_treasuryOps_section";
	String tbTreasurySecReportSection ="tb_secReport_section";
	String tbPostSection = "tb_post_section";
	String tbSetupSection="tb_setup_section";
	String tbCutOffTimeSection = "tb_cutoff_section";
	
	//String tbBranchPriSection = "tb_branchPm_section";
	//String tbBranchSecSection = "tb_BranchSec_section";
	//String tbTreasuryOpsSecSection = "tb_treasuryOpsSec_section";
	//String tbTreasuryOpsPriSection = "tb_treasuryOpsPm_section";
	
	// categories dropdwon 
	String tbCategorySetup = "Setup";
	String tbCategoryBid = "Bid";
	String tbCategoryReDiscountRate = "Re-discount Rate";
	String tbCategoryCutOff = "Cut off time modification";
	String tbCategoryReport = "Report";
	


	
	/*************TREASURY BILLS ENDS HERE********************/

}
