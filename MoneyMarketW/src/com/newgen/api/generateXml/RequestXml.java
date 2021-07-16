package com.newgen.api.generateXml;

public class RequestXml {

    public static String getUserLimitXml (String user){
        return "<USERID>"+user.toUpperCase()+"</USERID>";
    }
    public static String searchRequestXml (String startDate, String endDate, String acctNo, String amount, String debitCredit, String transParts){

        return "<executeFinacleScriptRequest>"
                + "<ExecuteFinacleScriptInputVO>"
                + "<requestId>FI_tranSearch.scr</requestId>"
                + "</ExecuteFinacleScriptInputVO>"
                + "<executeFinacleScript_CustomData>"
                + "<startDate>"+startDate+"</startDate>"
                + "<acctNum>"+acctNo+"</acctNum>"
                + "<amount>"+amount+"</amount>"
                + "<endDate>"+endDate+"</endDate>"
                + "<chalType>ALL</chalType>"
                + "<tranType>"+debitCredit+"</tranType>"
                + "<tranPart>"+transParts+"</tranPart>"
                + "</executeFinacleScript_CustomData>"
                + "</executeFinacleScriptRequest>";
    }
    
    /*
     * new search api to be implemented
     * https://ig-ffrprepjee1.nigeria.firstbank.local:9443/customFI/getReqDet?appCode=RTGSTRANSEARCH&FIData=
     * <chalType>ALL</chalType><acctNum>2012050787</acctNum><fromDate>09-05-2021</fromDate><toDate>23-06-2021</toDate>
     * <tranRMK>NEWGEN_FB-0000000000001533-FT</tranRMK><amount>100</amount><tranType>D</tranType>
     */
    public static String searchRequestXmlnew (String startDate, String endDate, String acctNo, String amount, String debitCredit, String transParts){
    	//to do change to comments
        return "<executeFinacleScriptRequest>"
                + "<ExecuteFinacleScriptInputVO>"
                + "<requestId>FI_tranSearch.scr</requestId>"
                + "</ExecuteFinacleScriptInputVO>"
                + "<executeFinacleScript_CustomData>"
                + "<startDate>"+startDate+"</startDate>"
                + "<acctNum>"+acctNo+"</acctNum>"
                + "<amount>"+amount+"</amount>"
                + "<endDate>"+endDate+"</endDate>"
                + "<chalType>ALL</chalType>"
                + "<tranType>"+debitCredit+"</tranType>"
                + "<tranPart>"+transParts+"</tranPart>"
                + "</executeFinacleScript_CustomData>"
                + "</executeFinacleScriptRequest>";
    }

    public static String postTransactionXml(
            String transType, String tranSubType,String acct1, String sol1, String Debit, String amount, String currency,String transParticulars,
            String partTranRemarks, String todayDateTime, String acct2, String sol2, String Credit, String logInUser)
    {


        return
                "<XferTrnAddRequest>" +
                        "<XferTrnAddRq>" +
                        "<XferTrnHdr>" +
                        "<TrnType>"+transType+"</TrnType>" +
                        "<TrnSubType>"+tranSubType+"</TrnSubType>" +
                        "</XferTrnHdr>" +
                        "<XferTrnDetail>" +
                        "<PartTrnRec>" +
                        "<SerialNum>1</SerialNum>" +
                        "<AcctId>" +
                        "<AcctId>"+acct1+"</AcctId><BankInfo><BranchId>"+sol1+"</BranchId></BankInfo>" +
                        "</AcctId>" +
                        "<CreditDebitFlg>"+Debit+"</CreditDebitFlg>" +
                        "<TrnAmt>" +
                        "<amountValue>"+amount+"</amountValue><currencyCode>"+currency+"</currencyCode>" +
                        "</TrnAmt>" +
                        "<TrnParticulars>"+transParticulars+"</TrnParticulars>" +  //narration
                        "<PartTrnRmks>"+partTranRemarks+"</PartTrnRmks>" + //distinct
                        "<ValueDt>"+todayDateTime+"T00:00:00.000</ValueDt>" +
                        "</PartTrnRec>" +
                        "<PartTrnRec>" +
                        "<SerialNum>2</SerialNum>" +
                        "<AcctId>" +
                        "<AcctId>"+acct2+"</AcctId><BankInfo><BranchId>"+sol2+"</BranchId></BankInfo>" +
                        "</AcctId>" +
                        "<CreditDebitFlg>"+Credit+"</CreditDebitFlg>" +
                        "<TrnAmt>" +
                        "<amountValue>"+amount+"</amountValue><currencyCode>"+currency+"</currencyCode>" +
                        "</TrnAmt>" +
                        "<TrnParticulars>"+transParticulars+"</TrnParticulars>" +
                        "<PartTrnRmks>"+partTranRemarks+"</PartTrnRmks>" +
                        "<ValueDt>"+todayDateTime+"T00:00:00.000</ValueDt>" +
                        "</PartTrnRec>" +
                        "</XferTrnDetail>" +
                        "</XferTrnAddRq>" +
                        "<XferTrnAdd_CustomData>" +
                        "<TRAN isMultiRec=\"Y\">" +
                        "<SRLNUM>1</SRLNUM><TRANCODE>005</TRANCODE><REMARKS2>"+ logInUser +"</REMARKS2>" +
                        "</TRAN>" +
                        "<TRAN isMultiRec=\"Y\">" +
                        "<SRLNUM>2</SRLNUM><TRANCODE>005</TRANCODE><REMARKS2>"+ logInUser +"</REMARKS2>" +
                        "</TRAN>" +
                        "</XferTrnAdd_CustomData>" +
                        "</XferTrnAddRequest>";
    }



    public static String fetchLienRequestXml (String acctNumber){
        return "<ngXmlRequest>"
                +		"<AcctLienInqRequest>"
                +			"<AcctLienInqRq>"
                +				"<AcctId>"
                +					"<AcctId>"+acctNumber+"</AcctId>"
                +				"</AcctId>"
                +				"<ModuleType>ULIEN</ModuleType>"
                +			"</AcctLienInqRq>"
                +		"</AcctLienInqRequest>"
                +	"</ngXmlRequest>";


    }

    public static String placeLienRequestXml (String acctNumber, String amount, String currency, String startDate, String endDate, String remarks){

        return "<ngXmlRequest>"
                +		"<AcctLienAddRequest>"
                +			"<AcctLienAddRq>"
                +				"<AcctId>"
                +					"<AcctId>"+acctNumber+"</AcctId>"
                +				"</AcctId>"
                +				"<ModuleType>ULIEN</ModuleType>"
                +				"<LienDtls>"
                +					"<NewLienAmt>"
                +						"<amountValue>"+amount+"</amountValue>"
                +						"<currencyCode>"+currency+"</currencyCode>"
                +					"</NewLienAmt>"
                +					"<LienDt>"
                +						"<StartDt>"+startDate+"T00:00:00.000</StartDt>"
                +						"<EndDt>"+endDate+"T00:00:00.000</EndDt>"
                +					"</LienDt>"
                +					"<ReasonCode>999</ReasonCode>"
                +					"<Rmks>"+remarks+"</Rmks>"
                +				"</LienDtls>"
                +			"</AcctLienAddRq>"
                +		"</AcctLienAddRequest>"
                +	"</ngXmlRequest>";


    }

    public static String removeLienRequestXml (String acctNumber, String lienId, String amount, String currency, String startDate, String endDate, String remarks){
        return "<ngXmlRequest>"
                +		"<AcctLienModRequest>"
                +			"<AcctLienModRq>"
                +				"<AcctId>"
                +					"<AcctId>"+acctNumber+"</AcctId>"
                +				"</AcctId>"
                +				"<ModuleType>ULIEN</ModuleType>"
                +				"<LienDtls>"
                +					"<LienId>"+lienId+"</LienId>"
                +					"<NewLienAmt>"
                +						"<amountValue>"+amount+"</amountValue>"
                +						"<currencyCode>"+currency+"</currencyCode>"
                +					"</NewLienAmt>"
                +					"<LienDt>"
                +						"<StartDt>"+startDate+"T00:00:00.000</StartDt>"
                +						"<EndDt>"+endDate+"T00:00:00.000</EndDt>"
                +					"</LienDt>"
                +					"<ReasonCode>999</ReasonCode>"
                +					"<Rmks>"+remarks+"</Rmks>"
                +				"</LienDtls>"
                +			"</AcctLienModRq>"
                +		"</AcctLienModRequest>"
                +	"</ngXmlRequest>";

    }

    public static String fetchOdaRequestXml (String acctNumber){
        return "<ODAcctInqRequest>"
                +		"<ODAcctInqRq>"
                +			"<ODAcctId>"
                +				"<AcctId>"+acctNumber+"</AcctId>"
                +				"<AcctType>"
                +					"<SchmType>ODA</SchmType>"
                +				"</AcctType>"
                +			"</ODAcctId>"
                +		"</ODAcctInqRq>"
                +	"</ODAcctInqRequest>";


    }

    public static String fetchSbaRequestXml (String acctNumber){
        return "<SBAcctInqRequest>"
                +		"<SBAcctInqRq>"
                +			"<SBAcctId>"
                +				"<AcctId>"+acctNumber+"</AcctId>"
                +				"<AcctType>"
                +					"<SchmType>SBA</SchmType>"
                +				"</AcctType>"
                +			"</SBAcctId>"
                +		"</SBAcctInqRq>"
                +	"</SBAcctInqRequest>";


    }

    public static String fetchCaaRequestXml (String acctNumber){
        return "<CAAcctInqRequest>"
                +		"<CAAcctInqRq>"
                +			"<CAAcctId>"
                +				"<AcctId>"+acctNumber+"</AcctId>"
                +			"</CAAcctId>"
                +		"</CAAcctInqRq>"
                +	"</CAAcctInqRequest>";
    }
    public static String tokenValidationXml(String userName, String otp){
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:ent=\"http://schemas.datacontract.org/2004/07/EntrustWrapper.Model\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:AuthMethod>" +
                "         <!--Optional:-->" +
                "         <tem:request>" +
                "            <!--Optional:-->" +
                "            <ent:CustID>"+userName+"</ent:CustID>" +
                "            <!--Optional:-->" +
                "            <ent:PassCode>"+otp+"</ent:PassCode>" +
                "         </tem:request>" +
                "      </tem:AuthMethod>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";
    }
}
