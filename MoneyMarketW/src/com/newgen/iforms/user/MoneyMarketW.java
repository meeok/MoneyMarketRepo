package com.newgen.iforms.user;

import com.newgen.iforms.custom.IFormListenerFactory;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.Utils.Constants;
import com.newgen.Utils.LogGen;
import com.newgen.Worksteps.TreasuryOfficerMaker;
import com.newgen.Worksteps.TreasuryOfficerInitiator;
import com.newgen.Worksteps.TreasuryOfficerVerifier;
import com.newgen.Worksteps.TreasuryOpsVerifier;
import com.newgen.Worksteps.TreasuryOpsMature;
import com.newgen.Worksteps.AwaitingMaturity;
import com.newgen.Worksteps.TreasuryOpsMatureOnMaturity;
import com.newgen.Worksteps.BranchInitiator;
import com.newgen.Worksteps.BranchVerifier;
import com.newgen.Worksteps.BranchException;
import com.newgen.Worksteps.RpcVerifier;
import com.newgen.Worksteps.TreasuryOpsFailed;
import com.newgen.Worksteps.TreasuryOpsSuccessful;
import com.newgen.Worksteps.Exit;
import org.apache.log4j.Logger;

public class MoneyMarketW implements IFormListenerFactory, Constants {
private Logger logger = LogGen.getLoggerInstance(MoneyMarketW.class);
	@Override
	public IFormServerEventHandler getClassInstance(IFormReference ifr) {
		// TODO Auto-generated method stub
		IFormServerEventHandler objActivity  = null;
		String processName = ifr.getProcessName();
		logger.info("processName: "+processName);
		String activityName =ifr.getActivityName();
		logger.info("activityName: "+activityName);
		try {
		if (processName.equalsIgnoreCase(ProcessName)){
			if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOfficerInitiator))
				objActivity = new TreasuryOfficerInitiator();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOfficerMaker))
				objActivity = new TreasuryOfficerMaker();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOfficerVerifier))
				objActivity = new TreasuryOfficerVerifier();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOpsVerifier))
				objActivity = new TreasuryOpsVerifier();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOpsMature))
				objActivity = new TreasuryOpsMature();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(awaitingMaturityUtility))
				objActivity = new AwaitingMaturity();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOpsMatureOnMaturity))
				objActivity = new TreasuryOpsMatureOnMaturity();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(branchInitiator))
				objActivity = new BranchInitiator();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(branchVerifier))
				objActivity = new BranchVerifier();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(branchException))
				objActivity = new BranchException();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(rpcVerifier))
				objActivity = new RpcVerifier();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOpsFailed))
				objActivity = new TreasuryOpsFailed();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(awaitingMaturity))
				objActivity = new AwaitingMaturity();

			else if (activityName != null && activityName.trim().equalsIgnoreCase(treasuryOpsSuccessful))
				objActivity = new TreasuryOpsSuccessful();

			else if (activityName != null && (activityName.trim().equalsIgnoreCase(discardWs) || activityName.trim().equalsIgnoreCase(query) || activityName.trim().equalsIgnoreCase(exit)))
				objActivity = new Exit();
		}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return objActivity;
	}

}
