package com.newgen.utils;

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
	String wiNameFormLocal ="winame";

	// commercial Paper process ids
	String cpSelectMarketLocal = "cp_select_market";
	String cpRemarksLocal = "cp_remarks";
	String cpDecisionLocal = "cp_decision";
	String cpLandMsgLocal = "cp_landingMsg";
	String cpPrimaryMarket = "primary";
	String cpSecondaryMarket = "secondary";
	String cpCategoryLocal = "cp_category";
	String cpCategorySetup = "Setup";
	String cpCategoryBid = "Bid";
	String cpCategoryReDiscountRate = "Re-discount Rate";
	String cpCategoryCutOff = "Cut off time modification";
	String cpCategoryReport = "Report";
	String cpCategoryMandate = "Mandate";
	String cpSetupWindowBtn = "cp_setupWin_btn";
	String cpUpdateCutoffTimeBtn = "cp_updateCutoff_btn";
	String cpSetReDiscountRateBtn = "cp_rediscRate_btn";
	String cpLandingMsgSubmitBtn="cp_landMsgSubmit_btn";
	String cpUpdateLocal = "cp_updateMsg";
	String cpOpenDateLocal = "cp_open_window_date";
	String cpCloseDateLocal = "cp_close_window_date";
	String cpPmMinPriAmtLocal = "cp_mp_amount";
	String cpPmWinRefNoLocal = "cp_pmwu_ref";
	String cpPmWinRefNoBranchLocal = "cp_pmwinref_br";
	String cpPmMinPriAmtBranchLocal ="cp_pm_mpBr";
	String cpCustomerNameLocal ="cp_custAcctName";
	String cpCustomerAcctNoLocal ="cp_custAcctNum";
	String cpCustomerEmailLocal = "cp_custAcctEmail";
	String cpLienStatusLocal ="cp_lien_status";
	String cpPmRateTypeLocal = "cp_rate_type";
	String cpPmPersonalRateLocal ="cp_personal_rate";
	String cpPmPrincipalLocal ="cp_principalAmt";
	String cpPmTenorLocal ="cp_tenor";
	String cpPmCustomerIdLocal ="cp_pm_custId";
	String cpPmInvestmentTypeLocal ="cp_investment_type";
	String cpPmReqTypeLocal ="cp_request_type";
	String cpPmReqFreshLabel ="Fresh Mandate";
	String cpPmReqFreshValue ="freshMandate";
	String cpAcctValidateBtn ="cp_acctValidateBtn";
	String cpTxnIdLocal ="cp_tsnId";
	String cpDebitPrincipalBtn ="cp_debitPrincipal_btn";
	String cpTokenLocal ="cp_token";



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
	String decReturnLabel ="Return to Initiator";
	String decReturn ="Return";
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
	String cpIdLabel ="CP";
	String cpRefNoDateFormat ="ddMMyyyy";
	String windowOpenFlag ="N";
	String windowCloseFlag ="Y";
	String windowOpen = "opened";
	String windowClosed = "closed";
	String windowInactiveMessage = "No Window is currently open, Try again later";
	String invalidSchemeCode1 = "SA231";
	String invalidSchemeCode2 = "SA310";
	String invalidSchemeCode3 = "SA340";
	String invalidSchemeCode4 = "SA327";
	String cpInvalidAccountErrorMessage ="This account is not valid for CP processing";
	String rateTypeBank ="Bank";
	String rateTypePersonal ="Personal";
	String minPrincipalErrorMsg ="Customer principal cannot be less and window minimum principal";
	String tenorErrorMsg ="Tenor must be between 7 to 270 days. Please enter a valid number";
	String cpValidateWindowErrorMsg ="This CP window has been closed. Kindly wait till the next window to initiate";
	String tbDocumentName ="Proof of Investment for TB";
	String cpDocumentName ="Proof of Investment for CP";
	String cpEmailMsg = "Update email of customer on account maintenance workflow";
	String cpPmInvestmentPrincipal ="Principal";
	String cpPostSuccessMsg = "Posting Done Successfully";
	String currencyNgn ="NGN";
	String apiSuccess ="success";
	String cpApiLimitErrorMsg ="Transaction above your limit to Post. Kindly enhance your limit";
	String exceptionMsg ="Exception occurred contact IBPS support";

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
	String cpOnSelectMarket = "onChangeMarket";
	String cpApiCallEvent = "cpApiCall";
	String cpOnChangeRateType ="onChangeRate";
	String validateWindowEvent ="validateWindow";
	String cpCheckPrincipalEvent ="onChangePrincipal";
	String cpCheckTenorEvent="onChangeTenor";
	String cpTokenEvent ="cpTokenEvent";
	String cpPostEvent ="cpPostEvent";
	String cpPostFlag ="cp_postFlag";

	//config
	String logPath = "nglogs/NGF_Logs/MoneyMarket/";
	String configPath = "/was/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/HO-IBPSUTIL01Cell01/MoneyMarketW.ear/MoneyMarketW.war/config/moneymarket.properties";
	String mailFromField ="MAILFROM";
	String processDefIdField = "PROCESSDEFID";
	
	
	/*************TREASURY BILLS STARTS HERE********************/
	//treasury events
	String tbCategoryddChange = "tbCategoryChange";
	String tbOnClickUpdateMsg = "tbOnClickUpdateMsg";
	
	// treasury bills control ids
	
	
	String tbPrimaryMarket = "tb_primary";
	String tbSecondaryMarket = "tb_secondary";
	String tbSetupWindowBtn = "tb_setupWin_btn";
	String tbUpdateCutoffTimeBtn = "tb_updateCutoff_btn";
	String tbSetReDiscountRateBtn = "tb_rediscRate_btn";
	String tbLandingMsgSubmitBtn="tb_landMsgSubmit_btn";
	String tbPmMinPriAmtLocal = "tb_mp_amount";
	
	String tbCustSchemeCode ="";
	//String 
	
	//tbMarketSection - Market
	String tbMarketTypedd = "tb_select_market";
	String tbCategorydd = "tb_category";
	
	//tbLandingMsgSection- Setup Landing Message
	String tbLandMsgtbx = "tb_landingMsg";
	String tbUpdateLandingMsgcbx = "tb_updateMsg";
	
	// tb_open_window_date - Treasury Primary SetUp
	String tbUniqueReftbx = "tb_pmwu_ref";
	String tbPriOpenDate= "tb_open_window_date";
	String tbPriCloseDate = "tb_close_window_date";
	
	//Decision
	String tbDecisiondd = "tb_decision";
	String tbRemarkstbx = "tb_remarks";
	
	//Customer Details --
	
	//Branch Prmy View tbBranchSection
	
	
	
	//sections
	String tbMarketSection = "tb_market_section";
	String tbLandingMsgSection = "tb_setupmsg_section";
	String tbTreasuryPriSetupSection = "tb_treasuryPm_section";
	String tbBranchPriSection = "tb_branch_section";
	String tbBranchSecSection = "tb_sec_br_section";
	String tbTreasurySecSection = "tb_treasurySec_section";
	String tbPrimaryBidSection = "tb_pmBid_section";
	
	String tbTerminationSection = "tb_termination_section";
	String tbProofOfInvestSection = "tb_poi_section";
	String tbDecisionSection = "tb_dec_section";
	String tbTreasuryOpsSection ="tb_treasuryOps_section";
	String tbTreasurySecReportSection ="tb_secReport_section";
	String tbPostSection = "tb_post_section";
	//String tbSetupSection="tb_setup_section";
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
	
	//schemecodes not allowed
	String SA231 ="SA231";
	String SA310 ="SA310";
	String SA340 ="SA340";
	String SA327 ="SA327";
	
	//hidden fields
	String tbLandingMsgApprovedFlg ="tb_landingMsgApprovedFlg";
	
	String yesFlag  ="Y";
	String noFlag ="N";
	
	
	String[] allTbSections = {tbMarketSection,tbLandingMsgSection ,tbTreasuryPriSetupSection, tbTreasurySecSection,
			tbPrimaryBidSection, tbBranchPriSection, tbBranchSecSection,tbTerminationSection,tbProofOfInvestSection , tbDecisionSection ,
			tbTreasuryOpsSection ,tbTreasurySecReportSection ,tbPostSection };
	
	/*************TREASURY BILLS ENDS HERE********************/

}
