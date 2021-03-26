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
	String cpCustomerDetailsSection ="cp_custDetails_section";
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
	String cpPmWinRefNoLocal = "cp_pmwu_ref";


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
	String cpPmLabel = "CPPMA";
	String cpSmLabel = "CPSMA";
	String cpRefNoDateFormat ="ddMMyyyy";


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
	String cpSetupWindowEvent = "setupWin";

	//config
	String logPath = "nglogs/NGF_Logs/MoneyMarket/";
	String configPath = "/was/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/HO-IBPSUTIL01Cell01/MoneyMarketW.ear/MoneyMarketW.war/config/moneymarket.properties";
	String mailFromField ="MAILFROM";
	String processDefIdField = "PROCESSDEFID";
	
	
	/*************TREASURY BILLS STARTS HERE********************/
	//treasury events
	public static final String tbCategoryChange = "tbCategoryChange";
	
	// treasury bills control ids
	public static final String tbSelectMarketLocal = "tb_select_market";
	public static final String tbRemarksLocal = "tb_remarks";
	public static final String tbDecisionLocal = "tb_decision";
	public static final String tbLandMsgLocal = "tb_landingMsg";
	public static final String tbPrimaryMarket = "tb_primary";
	public static final String tbSecondaryMarket = "tb_secondary";
	public static final String tbCategoryLocal = "tb_category";
	
	public static final String tbSetupWindowBtn = "tb_setupWin_btn";
	public static final String tbUpdateCutoffTimeBtn = "tb_updateCutoff_btn";
	public static final String tbSetReDiscountRateBtn = "tb_rediscRate_btn";
	public static final String tbLandingMsgSubmitBtn="tb_landMsgSubmit_btn";
	public static final String tbUpdateLocal = "tb_updateMsg";
	public static final String tbOpenDateLocal = "tb_open_window_date";
	public static final String tbCloseDateLocal = "tb_close_window_date";
	public static final String tbPmMinPriAmtLocal = "tb_mp_amount";
	public static final String tbUniqueRef = "tb_pmwu_ref";
	
	public static final String tbCustSchemeCode ="";
	//public static final String 
	
	// tb sections
	public static final String tbMarketSection = "tb_market_section";
	public static final String tbLandingMsgSection = "tb_setupmsg_section";
	public static final String tbTreasuryPriSection = "tb_treasuryPm_section";
	public static final String tbTreasurySecSection = "tb_treasurySec_section";
	public static final String tbPrimaryBidSection = "tb_pmBid_section";
	public static final String tbBranchSection = "tb_branch_section";
	public static final String tbTerminationSection = "tb_termination_section";
	public static final String tbProofOfInvestSection = "tb_poi_section";
	public static final String tbDecisionSection = "tb_dec_section";
	public static final String tbTreasuryOpsSection ="tb_treasuryOps_section";
	public static final String tbTreasurySecReportSection ="tb_secReport_section";
	public static final String tbPostSection = "tb_post_section";
	public static final String tbSetupSection="tb_setup_section";
	public static final String tbCutOffTimeSection = "tb_cutoff_section";
	
	//public static final String tbBranchPriSection = "tb_branchPm_section";
	//public static final String tbBranchSecSection = "tb_BranchSec_section";
	//public static final String tbTreasuryOpsSecSection = "tb_treasuryOpsSec_section";
	//public static final String tbTreasuryOpsPriSection = "tb_treasuryOpsPm_section";
	
	// categories dropdwon 
	public static final String tbCategorySetup = "Setup";
	public static final String tbCategoryBid = "Bid";
	public static final String tbCategoryReDiscountRate = "Re-discount Rate";
	public static final String tbCategoryCutOff = "Cut off time modification";
	public static final String tbCategoryReport = "Report";
	
	//schecodes not allowed
	public static final String SA231 ="SA231";
	public static final String SA310 ="SA310";
	public static final String SA340 ="SA340";
	public static final String SA327 ="SA327";
	
	String[] allTbSections = {tbMarketSection,tbLandingMsgSection ,tbTreasuryPriSection, tbTreasurySecSection,
			tbPrimaryBidSection, tbBranchSection, tbTerminationSection,tbProofOfInvestSection , tbDecisionSection ,
			tbTreasuryOpsSection ,tbTreasurySecReportSection ,tbPostSection };
	
	/*************TREASURY BILLS ENDS HERE********************/

}
