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

	//general process Ids
	String selectProcessLocal = "g_select_market";
	String processTabName = "tab2";
	String dashboardTab = "0";
	String commercialTab = "1";
	String treasuryTab = "2";
	String omoTab = "3";
	String moneyMarketSection = "g_moneyMarket_section";
	String landMsgLabelSection = "landingMsgLabelSection";
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
	String utilityFlagLocal = "cp_utilityFlag";
	String downloadFlagLocal ="downloadFlag";
	String utilityWs = "Utility_Initiation";


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
	String cpRediscountRateSection = "rediscountratesection";

    // end of cp sections

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
	String cpCategoryModifyCutOffTime = "Cut off time modification";
	String cpCategoryUpdateLandingMsg = "Update Landing Message";
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
	String cpSmWinRefNoBranchLocal = "cp_sec_id";
	String cpPmMinPriAmtBranchLocal ="cp_pm_mpBr";
	String cpCustomerNameLocal ="cp_custAcctName";
	String cpCustomerAcctNoLocal ="cp_custAcctNum";
	String cpCustomerEmailLocal = "cp_custAcctEmail";
	String cpCustomerSolLocal = "";
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
	String cpAllocSummaryTbl ="table88";
	String cpAllocationReqTbl ="table89";
	String cpBidReportTbl ="table90";
	String cpAllocTenorCol = "Tenor";
	String cpAllocRateCol = "Rate";
	String cpAllocTotalAmountCol = "Total Amount";
	String cpAllocRateTypeCol = "Rate Type";
	String cpAllocCountCol = "Count";
	String cpAllocStatusCol = "Status";
	String cpAllocGroupIndexCol = "Group Index";
	String cpDownloadBtn ="cp_downloadReport_btn";
	String cpUpdateBtn ="cp_upDateReport_btn";
	String cpViewGroupBtn ="cp_viewGroup_btn";
	String cpViewReportBtn ="cp_viewReport_btn";
	String cpAllocBankRateLocal ="cp_bank_rate";
	String cpAllocDefaultAllocLocal ="cp_default_ap";
	String cpAllocCpRateLocal ="cp_cpRate";
	String cpBidCustIdCol ="Customer ID";
	String cpBidAcctNoCol ="Account Number";
	String cpBidAcctNameCol ="Account Name";
	String cpBidTenorCol ="Tenor";
	String cpBidCpRateCol ="CP Rate";
	String cpBidBankRateCol = "Bank Rate";
	String cpBidPersonalRateCol ="Personal Rate";
	String cpBidMaturityDateCol = "Maturity Date";
	String cpBidDefAllocCol = "Default Allocation percentage";
	String cpBidNewAllocCol = "New Allocation Percentage";
	String cpBidTotalAmountCol = "Total Amount";
	String cpBidStatusCol ="Status";
	String cpBidStatusBidCol ="Bid Status";
	String cpPmAllocFlagLocal = "cp_pm_allocflag";
	String cpSmCutOffTimeLocal = "cp_secCuttoff";
	String cpSmCpBidTbl = "table93";
	String cpSmSetupLocal = "cp_sec_setUp";
	String cpSmWinRefLocal = "cp_sm_winref";
	String cpSmMinPrincipalLocal = "cp_sec_miniPrincipalAmt";
	String cpSmIFrameLocal = "cp_downloadBid_frame";
	String cpSmCustIdLocal = "cp_sec_custId";
	String cpSmMaturityDateBrLocal ="cp_sec_maturityDate";
	String cpSmInstructionTypeLocal = "cp_sec_instructionType";
	String cpSmConcessionRateLocal ="cp_sec_concessionRate";
	String cpSmConcessionRateValueLocal ="cp_sec_concessionValue";
	String cpApplyBtn = "apply_btn";
	String cpSmInvestmentIdLocal = "cp_sec_investmentID";
	String cpSmPrincipalBrLocal = "cp_sec_principal_br";
	String cpSmBidInvestmentIdCol = "Investment ID";
	String cpSmBidIssuerCol = "CP Issuer";
	String cpSmBidDescCol = "CP Description";
	String cpSmBidMaturityDateCol = "Maturity Date";
	String cpSmBidDtmCol = "DTM (Days to Maturity)";
	String cpSmBidStatusCol = "Status";
	String cpSmBidAvailableAmountCol = "Available Amount";
	String cpSmBidRateCol = "Rate";
	String cpSmBidAmountSoldCol = "Amount Sold";
	String cpSmBidMandatesCol = "Mandates";
	String cpSmBidReminderCol = "Remainder";
	String cpSmInvestmentBrTbl = "table91";
	String cpSmMinPrincipalBrLocal = "cp_sec_miniPrincipalAmt_brch";
	String cpInvestBtn = "invest_btn";
	String cpReDiscountRateLess90Local = "cp_less90_prim";
	String cpReDiscountRate90To180Local = "cp_91_180days_prim";
	String cpReDiscountRate181To270Local = "cp_181_270days_prim";
	String cpReDiscountRate271To364Local = "cp_271_364days_prim";



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
	String cpSmIdInvestmentLabel = "CPSMI";
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
	String minPrincipalErrorMsg ="Customer principal cannot be less than window minimum principal";
	String cpSmMinPrincipalErrorMsg ="Customer principal cannot be less than window minimum principal or greater than available investment amount";
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
	String statusAwaitingTreasury = "Awaiting Treasury";
	String statusAwaitingMaturity = "Awaiting Maturity";
	String bidSuccess = "Successful";
	String bidFailed = "Failed";
	String defaultAllocation = "100";
	String rateBidTblCol = "rate";
	String tenorBidTblCol = "tenor";
	String rateTypeBidTblCol = "ratetype";
	String smDefaultCutOffTime = "2 PM";
	String smMinPrincipal = "1000000";
	String smSetupNew = "New";
	String smSetupUpdate = "Update";
	String smStatusOpen= "Open";
	String smStatusClosed= "Closed";
	String smStatusMature= "Matured";
	String yes = "YES";
	String no = "NO";
	String cpSmMaturityDateErrMsg = "Maturity date differs from selected bid maturity date, please amend.";


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
	String cpViewReportEvent ="cpViewReport";
	String cpDownloadEvent = "cpDownloadGrid";
	String cpGetPmGridEvent = "cpPmGrid";
	String cpViewGroupBidEvent = "viewGroupBids";
	String cpUpdateBidEvent = "updateBids";
	String cpSmSetupEvent = "smSetup";
	String cpSmCpUpdateEvent = "smCpBidUpdate";
	String cpSmApplyEvent ="smApply";
	String cpSmConcessionRateEvent = "smConcession";
	String cpSmCheckMaturityDateEvent = "checkMaturityDate";
	String cpSmInvestEvent = "cpSmInvest";
	String cpUpdateCutOffTimeEvent = "updateCutOffTime";
	String cpUpdateReDiscountRateEvent = "updateRediscountRate";

	//config
	String logPath = "nglogs/NGF_Logs/MoneyMarket/";
	String configPath = "/was/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/HO-IBPSUTIL01Cell01/MoneyMarketW.ear/MoneyMarketW.war/config/moneymarket.properties";
	String configPath2 = "/was/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/HO-IBPSAPP01Cell01/MoneyMarketW_war.ear/MoneyMarketW.war/config/moneymarket.properties";
	String mailFromField ="MAILFROM";
	String processDefIdField = "PROCESSDEFID";
	
	
	/*************TREASURY BILLS STARTS HERE********************/
	//treasury events
	String tbCategoryddChange = "tbCategoryChange";
	String tbOnClickUpdateMsg = "tbOnClickUpdateMsg";
	String tbMarketTypeddChange = "tbMarketTypeddChange";
	String tbValidateCustomer = "tbValidateCustomer";
	String tbCustAcctNoChange ="tbCustAcctNoChange";
	String tbUpDateLndingMsgFlg ="tbUpDateLndingMsgFlg";
	String tbOnDone ="tbOnDone";
	String tbSetupMarket ="tbSetupMarket";
	String tbBrnchPriRollovrddChange ="tbBrnchPriRollovrddChange";
	String tbBrcnhPriRateTypeddChange ="tbBrcnhPriRateTypeddChange";
	String tbBrnchPriPrncplAmtChange = "tbBrnchPriPrncplAmtChange";
	String tbTokenChange ="tbTokenChange";
	String tbPost ="tbPost";
	String tbOndone ="tbOndone";
	String tbLienCustFaceValue = "tbLienCustFaceValue";
	String tbViewPriBidSmryReport ="tbViewPriBidSmryReport";
	String tbDownloadPriBidSmryReport ="tbDownloadPriBidSmryReport";
	String tbViewPriCustomerBids ="tbViewPriCustomerBids";
	String tbupdatePriCustomerBids ="tbUpdatePriCustomerBids";
	String tbGetPmGrid = "tbGetPmGrid";
	String tbDownload= "tbDownload";
	String tbCheckUnallocatedBids ="tbCheckUnallocatedBids";
	String tbVerificationAmtChanged = "tbVerificationAmtChanged";
	String tbUpdateSmIssuedBids ="tbUpdateSmIssuedBids";
	
	// treasury bills control ids
	String tbPrimaryMarket = "primary";
	String tbSecondaryMarket = "secondary";
	String tbSetupWindowBtn = "tb_setupWin_btn";
	String tbUpdateCutoffTimeBtn = "tb_updateCutoff_btn";
	String tbSetReDiscountRateBtn = "tb_rediscRate_btn";
	String tbLandingMsgSubmitBtn="tb_landMsgSubmit_btn";
	String tbPmMinPriAmtLocal = "tb_mp_amount";
	String tbMaturityDte ="tb_maturity_date";
	//tbMarketSection - Market
	String tbMarketTypedd = "tb_select_market";
	String tbCategorydd = "tb_category";
	String tbAssigndd ="assign";
	//tbLandingMsgSection- Setup Landing Message
	String tbLandMsgtbx = "tb_landingMsg";
	String tbUpdateLandingMsgcbx = "tb_updateMsg";
	// tb_open_window_date - Treasury Primary SetUp
	String tbUniqueReftbx = "tb_pmwu_ref";
	String tbPriOpenDate= "tb_open_window_date";
	String tbPriCloseDate = "tb_close_window_date";
	String tbPriSetupbtn ="tb_pmSetup_btn";
	//Decision
	String tbDecisiondd = "tb_decision";
	String tbRemarkstbx = "tb_remarks";
	//Customer Details --
	String tbCustAcctNo ="tb_custAcctNum";
	String tbCustAcctEmail ="tb_custAcctEmail";
	String tbCustAcctName ="tb_custAcctName";
	String tbCustAcctLienStatus ="tb_lien_status";
	String tbCustSchemeCode ="tb_schemecode";
	String tbValidatebtn ="tb_validate_btn";
	String tbFetchMandatebtn ="tb_fetchmandate_btn";
	String tbLienPrincipalbtn ="tb_LienPrincipal";
	String tb_BrnchPri_LienID ="tb_BrnchPri_LienID";
	//branch Primary
	String tbBrnchPriTenordd ="tb_pm_tenor";
	String tbBrnchPriRollovrdd="tb_rollover_type";
	String tbBrnchPriTermTypedd="tb_termtype";
	String tbBrnchCustPriRefNo="tb_pm_custId";
	String tbBrcnhPriRateTypedd="tb_rate_type";
	String tbBrcnhPriPersonalRate="tb_pm_personal";
	String tbBrnchPriWindownUnqNo="tb_uniqueNum";
	String tbBrnchPriRqsttype ="tb_request_type";
	String tbBrnchPriPrncplAmt ="tb_pm_mpBr";
	
	//Post section --tb_post_section
	String tbtoken = "tb_token";
	String tbTranID = "tb_trn";
	
	
	//Branch Prmy View tbBranchSection
	
	//tb_Primary Bid
	String tbViewPriBidReportbtn = "tb_viewReport_btn";
	String tbViewPriBidDwnldBidSmrybtn="tb_downloadBidSmry_btn";
	String tbPriBidReportTable ="table69"; //cmplx tb name =  tb_bidReport 
	String tbPriBidAllocationTable ="table68";
	//String tbPriBidAllocationbtn ="tb_allocation_btn";
	String tbPriBidAllocatebtn ="tb_allocate_btn";
	String tbPriBidApprovebtn ="tb_allocation_btn";
	String tbPriBidCustRqstTable ="table67";
	String tbPriBidViewCustRqstbtn="tb_viewCustPriBids";
	String tbPriBidUpdateCustBid ="tb_updateCustPriBids";
	String tbPriBidBulkAllbtn ="tb_allocation_btn";
	String tbPriBidBlkCbnRate ="tb_bulk_cbnrate";
	String tbPriBidBlkBankRate ="tb_bulk_bankrate";
	//String tbPriBidBlkDefaultAll ="tb_bulk_default_all";
	String tbPriBidBlkNewAll ="tb_bulk_new_all";
	//String tbPriBidApprovebtn ="tb_allocation_btn";
	
	
	//sections
	String tbMarketSection = "tb_market_section";
	String tbLandingMsgSection = "tb_setupmsg_section";
	String tbPriSetupSection = "tb_treasuryPm_section";
	String tbBranchPriSection = "tb_pm_br_section";
	String tbBrnchPriCusotmerDetails ="tb_custdetails_section";
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
	String tbSecRediscountRate ="tb_rediscount_section";
	
	//String tbBranchPriSection = "tb_pm_br_section";
	//String tbBranchSecSection = "tb_BranchSec_section";
	//String tbTreasuryOpsSecSection = "tb_treasuryOpsSec_section";
	//String tbTreasuryOpsPriSection = "tb_treasuryOpsPm_section";
	
	// categories dropdwon 
	String tbCategorySetup = "Setup";
	String tbCategoryBid = "Bid";
	String tbCategoryReDiscountRate = "Re-discount Rate";
	String tbCategoryCutOff = "Cutoff time modification";
	String tbCategoryReport = "Report";
	String tbCategoryMandate = "Mandate";
	String tbPoolManager ="Pool_Manager";
	String tbPoolManagerLabel ="Pool Manager";
	
	
	// roll over dropdown
	String tbBrnchPriRoPrincipal ="Principal";
	String tbBrnchPriRoPrncpalInterest ="Principal plus Interest";
	String tbBrnchPriRoTermteatMaturity ="Terminate at Maturity";
	
	//Rate Type dropdown
	String tbBrnchPriRtBanKRate ="Bank";
	String tbBrnchPriRtPersonal ="Personal";
	//schemecodes not allowed
	String SA231 ="SA231";
	String SA310 ="SA310";
	String SA340 ="SA340";
	String SA327 ="SA327";
	
	//other constants
	double tbBnkMinPrincipalAmt = 100000;
	double tbPrsnlMinPrincipalAmt = 50000000;
	String tbSuccess ="success";
	
	//assigndd
	String tbTreasuryUtility ="Treasury_Utility";
	String tbTreasuryVerifier ="Treasury_Verifier";
	String tbTreasuryUtilityLabel ="Treasury Utility";
	String tbTreasuryVerifierLable ="Treasury Verifier";
	
	//table69 ( Bid Report ) column names
	String tbBidRptRqstTypeCol = "Request Type";
	String tbBidRptTenorCol ="Tenor";
	String tbBidRptRateCol="Rate";
	String tbBidRptRateTypeCol="Rate Type";
	String tbBidRptTtlAmtCol ="Total Amount";
	String tbBidRptTxnCoutnCol = "Transaction Count";
	String tbBidRptStatusCol ="Status";
	
	//table67 ( Customer Request ) column names
	String tbBidCustRefNocol ="RefNumber";
	String tbBidAcctNoCol ="AccountNo";
	String tbBidAcctNamecol ="AccountName";
	String tbBidTenorCol ="Tenor";
	String tbBidRateCol ="tbRate";
	String tbBidRateTypeCol ="RateType";
	String tbBidCBNRateCol="CBNRtae";
	String tbCidBankRateCol ="BankRate";
	String tbBidPersonalRateCol ="PersonalRate";
	String tbBidPrincipalCol ="Principal";
	String tbBidMaturityDteCol ="MaturityDate";
	String tbBidDefaultAllCol ="DefaultAllocation";
	String tbBidNewAllCol ="NewAllocation";
	String tbBidTotalAmtCol ="TotalAmt";
	String tbStausCol ="Status";
	String tbBidStausCol ="BidStatus";
	String tbBidWorkItemNoCol ="WorkItemNo";
	
	
	//Secondary Setup
	String tbSecUniqueReftbx = "tb_secwu_ref";
	String tbVerificationAmtttbx ="tb_veriAmt";
	String tbSecCuttOfftime = "tb_sec_cuttoff";
	//String tbsSmDefaultCutOffTime = "3 PM";
	double tbSmMinVerificationAmt = 100000;
	String tbSmIssuedTBillTbl="table85";
	String tbSmUploadTbills ="button93";
	String tbSmSetupdd ="tb_setup";
	String tbUpdteSmMatDte = "tb_maturityDateUpdate";
	String tbUpdteSmTBillsAmt ="tb_amtUpdate";
	String tbUpdteSmRate = "tb_rateUpdate";
	String tbUpdateSmIssuedBidsbtn ="tb_UpdateSmIssuedBids_btn";
	
	//Primary re discount rate
	String tbRdrlessEqualto90tbx = "tb_less90";
	String tbRdr91to180 ="tb_91to180";
	String tbRdr181to270 = "tb_181to270";
	String tbRdr271to364days = "tb_271to364days";
	
	//Document names
	String customers_instruction ="customers_instruction";
	
	//return messages
	
	//hidden fields
	

	String tbLandingMsgApprovedFlg ="tb_landingMsgApprovedFlg";
	String tbRediscoutApprovedFlg ="tb_RediscoutApprovedFlg";
	String tbCutoffApproveFlg="tb_CutoffApproveFlg";
	String tbSetupApprovedFlg ="tb_SetupApprovedFlg";
	String tbInvalidAccountErrorMessage ="This account is not valid for CP processing";
	String tbSecBidStatusClosed = "Closed";
	String tbSecBidStatusOpen = "Open";
	String tbSecBidStatusMatureBids = "Mature-Bids";
	String tbSecBidStatusCancelledBids = "Cancelled-Bids";
	
	
	/*String minPrincipalErrorMsg ="Customer principal cannot be less and window minimum principal";
	String tenorErrorMsg ="Tenor must be between 7 to 270 days. Please enter a valid number";
	String cpValidateWindowErrorMsg ="This CP window has been closed. Kindly wait till the next window to initiate";
	String tbDocumentName ="Proof of Investment for TB";
	String cpDocumentName ="Proof of Investment for CP";
	String cpEmailMsg = "Update email of customer on account maintenance workflow";
	String cpPmInvestmentPrincipal ="Principal";
	String cpPostSuccessMsg = "Posting Done Successfully";
	String currencyNgn ="NGN";
	String apiSuccess ="success";
	String cpApiLimitErrorMsg ="Transaction above your limit to Post. Kindly enhance your limit";*/
	String tbWindowInactiveMessage = " Market Window is currently not open, Try again later";
	String tbWindowActiveMessage = " Market Window is currently open, You cannot setup another window";
	String yesFlag  ="Y";
	String noFlag ="N";
	String tbBidRqstTypelbl ="Fresh Mandate";
	String tbBidRqstType ="Fresh Mandate";
	String tbBidRllovrMndtelbl ="Roll Over Mandate";
	String tbBidRllovrMndte ="RollOverMandate";
	
	String[] allTbSections = {tbBrnchPriCusotmerDetails,tbMarketSection,tbLandingMsgSection ,tbPriSetupSection, tbTreasurySecSection,
			tbPrimaryBidSection,tbBranchPriSection, tbBranchSecSection,tbTerminationSection,tbProofOfInvestSection , tbDecisionSection ,
			tbTreasuryOpsSection,tbTreasurySecReportSection,tbPostSection,tbSecRediscountRate };
	
	/*************TREASURY BILLS ENDS HERE********************/

}
