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
	String utilityWs = "Utility_Initiation";
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
	String branchNameLocal = "g_branchName";

	// cp sections
	String cpBranchPriSection = "cp_branchPm_section";
	String cpBranchSecSection = "cp_BranchSec_section";
	String cpLandingMsgSection = "cp_landingMsg_section";
	String cpMarketSection = "cp_market_section";
	String cpPrimaryBidSection = "cp_primaryBid_section";
	String cpTerminationSection = "cp_termination_section";
	String cpTerminationDetailsSection = "cp_terminationdetails_section";
	String cpProofOfInvestSection = "cp_poi_section";
	String cpDecisionSection = "cp_dec_section";
	String cpTreasuryPriSection = "cp_pmTreasury_section";
	String cpTreasurySecSection = "cp_secTreasury_section";
	String cpUtilityFailedPostSection = "cp_utilityFailedPost_section";
	String cpTreasuryOpsPriSection = "cp_treasuryOpsPm_section";
	String cpPostSection = "cp_post_section";
	String cpSetupSection="cp_setup_section";
	String cpCutOffTimeSection = "cp_cutoff_section";
	String cpCustomerDetailsSection ="cp_custDetails_section";
	String cpReDiscountRateSection = "rediscountratesection";
	String cpMandateTypeSection = "cp_mandatetypesection";
	String cpLienSection = "cpLienSection";
	String cpServiceSection = "cpServiceSection";
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
	String cpCustomerSolLocal = "CPCUSTSOL";
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
	String cpPostBtn ="cp_post_btn";
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
	String cpSmInvestmentTypeLocal = "cp_sec_investmentType";
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
	String cpReDiscountRate91To180Local = "cp_91_180days_prim";
	String cpReDiscountRate181To270Local = "cp_181_270days_prim";
	String cpReDiscountRate271To364Local = "cp_271_364days_prim";
	String cpMandateTypeLocal = "cp_mandatetype";
	String cpMandateTypeTerminate = "Termination";
	String cpMandateTypePoi = "POI";
	String cpMandateTypeLien = "Lien";
    String cpTermMandateLocal = "CP_TERMMANDATE";
	String cpTermMandateDateCol = "Date";
	String cpTermMandateRefNoCol = "Reference Number";
	String cpTermMandateAmountCol = "Amount";
	String cpTermMandateAcctNoCol = "Account Number";
	String cpTermMandateCustNameCol = "Customer Name";
	String cpTermMandateDtmCol = "Number of Days to Maturity";
	String cpTermMandateStatusCol = "Status";
	String cpTermMandateWinRefCol = "winref";
	String cpTermMandateTbl = "table94";
	String cpSelectMandateTermBtn = "cpTerminateMandateBtn";
	String cpSearchMandateTermBtn = "cpTermMandateSearchBtn";
	String cpTerminationTypeLocal = "cp_term_type";
	String cpTermSpecialRateLocal = "cp_special_rate";
	String cpTermSpecialRateValueLocal = "cp_special_rate_value";
	String cpTerminationTypeFull = "fullTerm";
	String cpTerminationTypePartial = "partTerm";
	String cpTermPartialAmountLocal = "CP_TERMPARTIALAMT";
	String cpTermPartialOptionLocal = "CP_TERMPTOPTION";
	String cpTermCustIdLocal = "CP_TERMCUSTID";
	String cpTermAmountDueLocal = "CP_TERMAMTDUE";
	String cpTermAdjustedPrincipalLocal = "CP_TERMADPRINCIPAL";
	String cpTermCalculateBtn = "calculateTermBtn";
	String cpTermDtmLocal = "CPTERMDTM";
	String cpTerminateBtn = "cpTerminateBtn";
	String cpLienTypeLocal = "CPLIENTYPE";
	String cpLienTypeSet = "SET";
	String cpLienTypeRemove = "REMOVE";
	String cpLienMandateIdLocal = "CPLIENMANDATEID";
	String cpPoiTbl = "table56";
	String cpPoiSearchBtn = "cpPoiSearchBtn";
	String cpPoiGenerateBtn = "cpPoiGenerateBtn";
	String cpPoiCustNameLocal = "cp_poi_acctName";
	String cpPoiCustAcctNoLocal = "cp_poi_acctNumb";
	String cpPoiCustEffectiveDateLocal = "cp_poi_effectiveDate";
	String cpPoiCustInterestLocal = "cp_poi_interest";
	String cpPoiCustMaturityDateLocal = "cp_poi_maturityDate";
	String cpPoiCustPrincipalAtMaturityLocal = "cp_poi_principalMaturity";
	String cpPoiCustTenorLocal = "cp_poi_tenor";
	String cpPoiCustIdLocal = "cp_poi_refCode";
	String cpPoiCustRateLocal = "cp_poi_rate";
	String cpPoiDateLocal = "POI_DATE";
	String cpPoiCustAmountInvestedLocal = "cp_poi_amtInvested";
	String cpPoiMandateLocal = "CPPOIMANDATE";
	String cpPoiDateCol = "Date";
	String cpPoiIdCol = "Reference Number";
	String cpPoiAmountCol = "Amount";
	String cpPoiAcctNoCol = "Account Number";
	String cpPoiAcctNameCol = "Customer Name";
	String cpPoiStatusCol = "Status";
	String cpTermIssueDateLocal = "CPTERMISSUEDATE";
	String cpTermBoDateLocal = "CPTERMBODATE";
	String cpTermTenorLocal = "CPTERMTENOR";
	String cpTermMaturityDateLocal = "CPTERMMATURITYDATE";
	String cpTermNoDaysDueLocal = "CPTERMNODAYSDUE";
	String cpTermPenaltyChargeLocal = "CPTERMPCHARGE";
	String cpTermRateLocal ="CPTERMRATE";
	String cpUploadExcelBtn = "cpUploadExcelBtn";
	String cpFileNameLocal = "CPFILENAME";
	String cpFetchMandateBtn = "cpFetchMandateBtn";
	String cpCheckLienBtn = "cpCheckLienBtn";

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
	String windowActiveErrMessage = "A Window for this market is currently open, Kindly wait till it elapse to open a new window";
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
	String tbTemplateName ="TB_POI";
	String cpTemplateName ="CP_POI";
	String cpEmailMsg = "Update email of customer on account maintenance workflow";
	String cpPmInvestmentPrincipal ="Principal";
	String cpPostSuccessMsg = "Posting Done Successfully";
	String currencyNgn ="NGN";
	//String apiSuccess ="success";
	String cpCusMailErrMsg ="Update email of customer on account maintenance workflow";
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
	String smSetupUpdate = "UpDate";
	String smStatusOpen= "Open";
	String smStatusClosed= "Closed";
	String smStatusMature= "Matured";
	String yes = "YES";
	String no = "NO";
	String cpSmMaturityDateErrMsg = "Maturity date differs from selected bid maturity date, please amend.";
	String cpLienErrMsg ="Commercial paper is Lien, kindly remove Lien on Commercial paper and try again.";
	String cpLienSetFlag = "Y";
	String cpLienRemoveFlag = "N";
	String apiSuccess = "SUCCESS";
	String apiFailed = "FAILED";
	String apiFailure = "FAILURE";
	String apiStatus = "Status";
	String debitFlag = "D";
	String creditFlag = "C";
	String transType = "T";
	String transSubTypeC = "CI";
	String transSubTypeB = "BI";
	String apiNoResponse = "No Response Found";
	String apiLimitErrMsg = "This transaction is above your limit to Post. Kindly increase your limit and try again";

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
	String cpMandateTypeEvent = "mandateType";
	String cpSearchTermMandateEvent ="searchTermMandate";
	String cpSelectTermMandateEvent = "selectTermMandate";
	String cpSelectTermSpecialRateEvent = "selectSpecialRateTerm";
	String cpSelectTermTypeEvent = "selectTermType";
	String cpCalculateTermEvent = "calculateTermination";
	String cpPartialTermOptionEvent = "partialTermOption";
	String cpLienEvent = "lienEvent";
	String generateTemplateEvent = "TemplateGeneration";
	String cpPoiSearchEvent="poiSearch";
	String cpPoiProcessEvent="poiProcess";
	String cpFetchMandateEvent = "cpFetchMandate";
	String cpValidateAcctEvent = "cpValidateAcct";
	String cpValidateLienEvent = "cpValidateLien";
	String cpPmProcessSuccessBidsEvent = "cpPmProcessSuccessBids";
	String cpCheckDecisionEvent = "cpCheckDecision";


	//config
	String logPath = "nglogs/NGF_Logs/MoneyMarket/";
	String configPath = "/was/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/HO-IBPSUTIL01Cell01/MoneyMarketW.ear/MoneyMarketW.war/config/moneymarket.properties";
	String configPath2 = "/was/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/HO-IBPSAPP01Cell01/MoneyMarketW_war.ear/MoneyMarketW.war/config/moneymarket.properties";
	String mailFromField ="MAILFROM";
	String processDefIdField = "PROCESSDEFID";
	String serverPortField = "SERVERPORT";
	String serverIpField = "SERVERIP";
	String templatePortField = "TEMPLATEPORT";

	//API SERVICENAME

	String postServiceName = "postRequestToFinacle";
	String fetchOdaAcctServiceName ="CURRENTACCOUNT";
	String fetchCaaAcctServiceName ="SPECIALACCOUNT";
	String fetchSbaAcctServiceName ="SAVINGACCOUNT";
	String fetchLienServiceName = "FETCHLIEN";
	String fetchLimitServiceName = "CIGETUSERLIMIT";
	String searchTranServiceName = "CISEARCHTRANSACTION";
	String tokenValidationServiceName = "TOKENVALIDATION";
	String placeLienServiceName = "placeLien";
	String removeLienServiceName = "REMOVELIEN";

	
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
	String tbPostFaceValue ="tbPostFaceValue";
	String tbSetBidAllocationCloseFlg ="tbSetBidAllocationCloseFlg";
	String tbApplyNSCustodyFee = "tbApplyNSCustodyFee";
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
	String tbSmApplyBid ="tbSmApplyBid";
	String tbValidateSmBidAmount ="tbValidateSmBidAmount";
	String tbConcesionaryRateClicked ="tbConcesionaryRateClicked";
	String tbGetCustInvestmentDetails = "tbGetCustInvestmentDetails";
	String tbMandateTypeChanged ="tbMandateTypeChanged";
	String tbPopulatePOIFields = "tbPopulatePOIFields";
	String tbGetCustDetailsForTermination="tbGetCustDetailsForTermination";
	String tbTerminateTypeChanged ="tbTerminateTypeChanged";
	String tbTerminate = "tbTerminate";
	String tbUnLienCustFaceValue ="tbUnLienCustFaceValue";
	String tbSpecialRateClicked ="tbSpecialRateClicked";
	String tbLienTypeChange="tbLienTypeChange";
	String tbGetMaturityDte= "tbGetMaturityDte";
	
	// treasury bills control ids
	String tbPrimaryMarket = "primary";
	String tbSecondaryMarket = "Secondary";
	String tbSetupWindowBtn = "tb_setupWin_btn";
	String tbUpdateCutoffTimeBtn = "tb_updateCutoff_btn";
	String tbSetReDiscountRateBtn = "tb_rediscRate_btn";
	String tbLandingMsgSubmitBtn="tb_landMsgSubmit_btn";
	//String tbPmMinPriAmtLocal = "tb_mp_amount";
	String tbMaturityDte ="tb_maturity_date";
	
	//tbMarketSection - Market
	String tbMarketTypedd = "tb_select_market";
	String tbCategorydd = "tb_category";
	String tbAssigndd ="assign";
	String tbMandateTypedd = "tb_mandateTypedd";
	String terminationVal = "Termination";
	String proofofinvestmentVal ="POI";
	String proofofinvestmentLbl ="Proof of Investment";
	String tbMandateTypeLien = "Lien";

	
	//tbLandingMsgSection- Setup Landing Message
	String tbLandMsgtbx = "tb_landingMsg";
	String tbUpdateLandingMsgcbx = "tb_updateMsg";
	
	// tb_open_window_date - Treasury Primary SetUp
	//String tbUniqueReftbx = "tb_pmwu_ref";
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
	String tbCustAcctLienStatus ="tb_CustAcctLienStatus";
	String tbCustSchemeCode ="tb_schemecode";
	String tbCustSolid ="tb_custSolid";
	String tbValidatebtn ="tb_validate_btn";
	String tbFetchMandatebtn ="tb_fetchmandate_btn";
	String tbLienPrincipalbtn ="tb_LienPrincipal";
	String tb_BrnchPri_LienID ="tb_BrnchPri_LienID";
	String tb_SmCustBidRemark = "tb_custBidRemark";
	String tbCustAcctCurrency = "tb_custAcctCurrency";
	String tbLienRemarks ="TBills Principal Amount";
	//String tbLienEndDte ="2099-12-31";
	
	String tbBidRequestDte ="tb_bidRequestDte";
	String tbBidRStatus ="tb_bidStatus";

	String tbCustUniquerefId ="tb_custUniquerefId";
	String tbCustPrincipalAmount ="tb_principalAmt";//"tb_custPrincipalAmount";
	String tbMarketUniqueRefId ="tb_marketUniqueRefId";
	
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
	
	//lien activities
	String tbLienType="TBLIENTYPE";
	String tbLiencustRefId ="TBLIENMANDATEID";
	String tbLienStatus ="tb_lien_status"; //lien on customer treasury bills
	String tbLienTypeSet="Set";
	String tbLienTypeRemove ="Remove";
	
	//Post section --tb_post_section
	String tbtoken = "tb_token";
	String tbTranID = "tb_trn";
	String tbRemoveLien ="";
	
	//Branch Prmy View tbBranchSection
	
	//tb_Primary Bid
	String tbViewPriBidReportbtn = "tb_viewReport_btn";
	String tbViewPriBidDwnldBidSmrybtn="tb_downloadBidSmry_btn";
	String tbPriBidReportTable ="table69"; //cmplx tb name =  tb_bidReport 
	String tbPriBidAllocationTable ="table68";
	
	//String tbPriBidAllocationbtn ="tb_allocation_btn";
	String tbPriBidAllocatebtn ="tb_allocate_btn";
	String tbPriBidApprovebtn ="tb_allocation_btn";
	String tbPriBidCustRqstTable = "table67";//"table105";//
	String tbPriBidViewCustRqstbtn="tb_viewCustPriBids";
	String tbPriBidUpdateCustBid ="tb_updateCustPriBids";
	String tbPriBidBulkAllbtn ="tb_allocation_btn";
	String tbPriBidBlkCbnRate ="tb_bulk_cbnrate";
	String tbPriBidBlkBankRate ="tb_bulk_bankrate";
	//String tbPriBidBlkDefaultAll ="tb_bulk_default_all";
	String tbPriBidBlkNewAll ="tb_bulk_new_all";
	//String tbPriBidApprovebtn ="tb_allocation_btn";
	String tbPmTotalAllocationAmt ="tb_pmTotalAllocationAmt";
	String tbCalculateMaturityDtebtn="tb_calc_maturityDte";
	String tbSettlementDte = "tb_settlement_dte";
	String tbBidAllocationCloseFlg ="tb_bidAllocationCloseFlg";

	
	
	//sections
	String tbSearchCustSection ="tb_searchCust_section";
	String tbMarketSection = "tb_market_section";
	String tbLandingMsgSection = "tb_setupmsg_section";
	String tbPriSetupSection = "tb_treasuryPm_section";
	String tbBranchPriSection = "tb_pm_br_section";
	String tbBrnchCusotmerDetails ="tb_custdetails_section";
	String tbBranchSecSection = "tb_sec_br_section";
	String tbTreasurySecSection = "tb_treasurySec_section";
	String tbPrimaryBidSection = "tb_pmBid_section";
	
	String tbTerminationSection = "tb_termination_section";
	String tbProofOfInvestSection = "tb_poi_section";
	String tbDecisionSection = "tb_dec_section";
	String tbTreasuryOpsSection ="tb_treasuryOps_section";
	String tbTreasurySecReportSection ="tb_secReport_section";
	String tbPostSection = "tb_post_section";
	String tbCutOffTimeSection = "tb_cutoff_section";
	String tbRediscountRate ="tb_rediscount_section";
	String tbCustBidSection ="tb_cust_bid_section";
	String tbUtilityFailedPostingSection ="tb_utilityFailedPosting_section";
	String tbLienSection ="tbLienSection";
	String tbPBCustDetailsSection = "tbPBCustDetailsSection";
	String tbUpdatePriMaturityDte ="tbUpdatePriMaturityDte";
	String tbCustodyFeeSection = "tbCustodyFeeSection";
	
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
	String tbPoolManager ="PoolManager";
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
	String PB2010 ="PB2010";
	
	//other constants
	double tbBnkMinPrincipalAmt = 100000;
	double tbPrsnlMinPrincipalAmt = 100000;//50000000;
	double tbPrsnlMaxPrincipalAmt = 50000000;
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
	String tbBidCBNRateCol="CBNRate";
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

	
	
	//Secondary bid Setup
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

	
	//Secondary branch initiating bids
	String tbSmCustid = "tb_sec_custId";
	String tbSmUniqueRefNo = "tb_sec_id";
	String tbSmMinimumPrincipal = "tb_mp_amount";
	String tbSmOpenBidsTbl = "table95";
	String tbSmBidAmount = "tb_sec_pa";
	String tbBrnchSmWindownUnqNo="tb_sec_id";
	String tbSmInvestmentId = "tb_SmInvestmentId";
	String tbSmMinPriAmt = "tb_mp_amount";  //--------------to be deleted
	String tbSmtenor = "tb_sec_tenor";
	String tbSmRate = "tb_sec_rate";
	String tbSmMaturityDte = "tb_sec_maturityDate";
	String tbSmConcessionValue ="tb_sec_concessionValue";
	String tbSmConcessionRate="tb_sec_concessionRate";
	String tbSmInstructionType ="tb_sec_instrtype";
	
	String tbSmPrincipalAtMaturity ="tb_sec_pm";
	String tbSmIntstMaturityNonLpYr ="tb_sec_intMaturityNonLpYr"; //del
	String tbSmIntrsyMaturityLpYr ="tb_sec_intMaturityLpYr";  //del
	String tbSmIntrestAtMaturity ="tb_intMaturity";//""tb_sec_interestAtMaturity";
	String tbSmResidualIntrst ="tb_sec_residual_int";
	String tbSmdaysMaturity ="tb_sec_daysMaturity";
	
	//table85 ( Customer Request ) column names
	//tbBidMaturityDteCol, tbBidRateCol,tbBidTenorCol,tbStausCol from table 67
	String tbTBillAmountCol ="TBillAmount";
	String tbTotalAmountSoldCol ="TotalAmountSold";
	String tbAvailableAmountCol="AvailableAmount";
	String tbMandatesCol = "tbBidStausCol";
	String tbBidSmInvestmentIDCol ="SmInvestmentId";
	String tbDaysToMaturityCol = "DaysToMaturity";
	String tbSmInvestmentIdCol = "SmInvestmentId";
	String tbMaturityDteCol ="Maturity Date";
	
	//Primary re discount rate
	String tbRdrlessEqualto90tbx = "tb_less90";
	String tbRdr91to180 ="tb_91to180";
	String tbRdr181to270 = "tb_181to270";
	String tbRdr271to364days = "tb_271to364days";
	
	//tbProofOfInvestSection section control ids
	String tbCustAcctOrRefID = "textbox593"; //for searching
	String tbPoiCustRefid = "tb_poi_unique_num";
	String tbPoiActName = "tb_poi_acctName";
	String tbPoiAmtInvested ="tb_poi_amtInvested";
	String tbPoiCustAcctNum="tb_poi_custAcctNum";
	String tbPoiCustDetailsTbl ="table75";
	String tbPoiEffectiveDate = "tb_poi_effectiveDate";
	String tbPoiIntrest = "tb_poi_interest";
	String tbPoiMaturityDte = "tb_poi_maturityDate";
	String tbPoiPrincipalAtMaturity = "tb_poi_principalMaturity";
	String tbPoiRate ="tb_poi_rate";
	String tbPoiTenor ="tb_poi_tenor";
	String tbPoiRefCode = "tb_poi_refCode";
	String tbPoiDte = "tb_poi_date";
	String tbPoiGenerateBtn = "tbPoiGenerateBtn";
	String tbPoiSeacrchCustBtn = "tbPoiSeacrchCustBtn";
	
	//	String tbCustAcctORRefID = "textbox421"; //for searchinf volumn names
	String tbDateCol = "Date";
	String tbRefNoCol ="ReferenceNumber";
	String tbAmountCol="Amount";
	String tbAcctNoCol ="AccountNumber";
	String tbCustNameCol = "CustomerName";
	String tbStatusCol = "Status";
	String tbMaturityDateCol ="MaturityDate";
	String tbInterestAtMaturityCol ="InterestAtMaturity";
	String tbInvestmentIdCol ="InvestmentId";
	String tbTenorCol ="tenor";
	String tbRateCol ="rate";
	String tbMarketWinRefIDCol ="MarketRefId";
	String tbMarketUniqueIdCol ="MarketUniqueId";
	String tbPrincipalAtMaturityCol ="PrincipalAtMaturity";
	String tbMaturityDtCol ="Maturity Date";
//	String tbDaysToMaturityCol ="DaysToMaturity";
	//tb_sec_maturityDte, tb_sec_pm, tb_sec_maturityDate,tb_SmInvestmentId, tb_sec_tnor,tb_sec_rate
	
	//termination fields
	String tbSearchCustForTerminationBtn ="tb_searchCustForTerminationBtn";
	String tbTerminationMandateTbl="table99";
	String tbTermtypedd = "tb_termination_type";
	String tbTermVal = "tb_termination_val";//cash or face val
	String tbTermAdjustedPrncpal = "tb_adjustedPrncpal";
	String tbTermAmtDueCust ="tb_termAmtDueCust";
	String tbTermCashValue ="tb_termCashValue";
	String tbTermbtn = "tb_terminate_btn";
	String tbTermCustUniqId = "tb_termCustUniqId";//will get this from the populated customer field
	String tbTermRediscountRate ="tb_rediscount_rate";
	String tbSpecialRateValue = "tb_special_rate_val";
	String tbSpecialRate = "tb_special_rate";
	String termCustRefId ="tb_TermCustomerRefID";
	String tbTermPenaltyCharge ="tb_term_penalty_charge";
	String tbTermDate = "tb_termination_date";
	
	//private banking Beneficiary details section 
	String tbPBCustAcctNo = "tb_PBCustAcctNo";
	String tbPBCustAcctName ="tb_PBCustAcctName";
	
	
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
	
	
	String tbUnlienBtn ="UnLienPrincipal";
	String tbPostbtn="tb_post_btn";
	
	String tbTerminationTypeFull = "fullTerm";
	String tbTerminationTypePartial = "partTerm";
	//tbPost
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
	
	String dbDteFormat = "dd-MMM-yy";
	
	String[] allTbSections = {tbCustodyFeeSection,tbUtilityFailedPostingSection,tbSearchCustSection,tbCustBidSection,tbBrnchCusotmerDetails,tbMarketSection,tbLandingMsgSection ,tbPriSetupSection, tbTreasurySecSection,
			tbPrimaryBidSection,tbBranchPriSection, tbBranchSecSection,tbTerminationSection,tbProofOfInvestSection , tbDecisionSection ,
			tbTreasuryOpsSection,tbLienSection,tbTreasurySecReportSection,tbPostSection,tbRediscountRate,tbPBCustDetailsSection};
	
	//custody feee section
	String tbApplyNSCustodyfeeRbtn="tb_applyNSCustodyfee";
	String tbCustodyFeeRate="tb_custody_fee_rate";
	String tbStandardCustodyRate="0.10";
	
	//accounting Enteries for tbills details for posting 
	String actSol = "999";
	String acctCurr="NGN";
	String tbHOSuspenceAct ="99934389027101";
	String tbCBNAct = "99910100000101";
	String tbCommissionOnNTBAcct = "99955081000101";
	String tbIncomeOnNTBSaleAcct  ="99955081000401";
	String tbCusotdyTxnFeeSuspenceAcct ="99934206000501";
	String tbStandardCustodyFee = "0.01";

	
	
	/*************TREASURY BILLS ENDS HERE********************/
	
	
	
	
	/*************OMO AUCTION STARTS HERE********************/
	//sections
	String omoMarketSection = "omo_market_section";
	String omoCustDetailsSection ="omo_cust_details_section";
	String omoBulkMandateSection ="omo_bulk_setup_section";
	String omoDecisionSection="tb_dec_section";
	String omoLienSection ="omo_lien_section";
	
	//events
	String omoSetupTypeChange = "omoSetupTypeChange";
	String omoMandateTypeChanged ="omoMandateTypeChanged";
	String omoOnDone ="omoOnDone";
	String omofetchAcctDetails ="omofetchAcctDetails";

	
	//MARKET SECTION
	String omoMarketTypedd ="omo_select_market";
	String omoCategorydd ="omo_category";
	String omoMarketUniqueRefId="omo_marketUniqueRefId";
	String omoMandateTypedd="omo_mandateTypedd";
	String omoSetupType="omo_setup_type";
	String omoSingleSetup ="Single";
	String omoBulkSetup ="Bulk";
	
	//customer details section
	String omoBankName="OMO_BANK_NAME";
	String omoCustAcctNo ="OMO_CUST_ACCT_NO";
	String omoCustName ="OMO_CUST_NAME";
	String omoCustCif ="OMO_CUST_CIF";
	String omoCustCurr ="OMO_CUST_CURR";
	String omoCustRefId ="OMO_CUST_REFID";
	String omoCustSolid ="OMO_CUST_SOLID";
	String omoDealDate ="OMO_DEAL_DTE";
	String omoFbnCustomer  ="OMO_FBN_CUSTOMER";
	String omoInterest ="OMO_INTEREST";
	String omoInterestAtMat ="OMO_INTEREST_AT_MATURITY";
	String omoMaturityDte ="OMO_MATURITY_DTE";
	String omoRate ="OMO_RATE";
	String omoSettlementDte ="OMO_SETTLEMENT_DTE";
	String omoSettlementValue="OMO_SETTLEMENT_VALUE";
	String omoStatus="OMO_STATUS";
	String omoFetchAcctDetailsBtn="omo_fetch_acct_details";
	
	//bulk setup section
	String omoBulkMandateTbl = "table106";
	String omoFetchBulkAcctDtlsBtn="omo_fetch_bulk_acct_dtls_btn";
	String omoUploadMandateBtn = "omo_upload_mandate_btn";
	//colums on table106
	String omoCustRefNoCol = "CustRefNo";
	String omoAcctNoCol= "ACNo";
	String omoCustomerNameCol ="CustomerName";
	String omoBankNameCol ="FBNCustomer";
	String omoCustNameCol ="BankName";
	String omoDealDteCol ="DealDate";
	String omoSettlementDteCol ="SettlementDate";
	String omoPrincipalCol ="Principal";
	String omoRateCol = "Rate";
	String omoSettlementValueCol ="SettlementValue";
	String omoMaturityDteCol ="MaturityDte";
	String omoInterestCol = "Interest";
	String omoInterestAtMaturityCol ="InterestAtMaturity";
	String omoDTMCol ="DTM";
	String omoACDtlsFetchedFlgCol ="ACDtlsFetchedFlg";
	String omoCustodyFeeRateCol ="CustodyFeeRate";
	String omoCustSolidCol ="Solid";
	String omoCustCifCol ="CIF";
	String omoCustCurrCol ="Currency";
	//String omoFetchAcctDtlsStatusCol ="FetchAcctDtlsStatus";
	
	//Decision
	String omoDecisiondd = "omo_decision";
	String omoRemarkstbx = "omo_remarks";
	
	//lien activities
	String omoLienType="omo_lien_type";
	String omoLiencustRefId ="omo_lien_cust_refid";
	String omoLienStatus ="omo_lien_status";
	
	/*************OMO AUCTION ENDS HERE********************/

}
