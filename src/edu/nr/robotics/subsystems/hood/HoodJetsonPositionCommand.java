package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.nr.lib.network.AndroidServer;

public class HoodJetsonPositionCommand extends NRCommand {

	double val = 0;
	
	boolean goodToGo = true;
	
	
	public HoodJetsonPositionCommand() {
		requires(Hood.getInstance());
	}
	
	@Override
	protected void onStart() {
    	if(!AndroidServer.getInstance().goodToGo()) { 
    		System.out.println("Android connection not good to go");
    		goodToGo = false;
    		return;
    	}
		val = Hood.distanceToAngle(AndroidServer.getInstance().getDistance());
		Hood.getInstance().enable();
		Hood.getInstance().setSetpoint(val);
	}

	@Override
	protected void onEnd(boolean interrupted) {
		System.out.println("Just finished Hood Jetson check");
    	goodToGo = true;
	}

	@Override
	protected boolean isFinishedNR() {
		if(!goodToGo)
			return true;
		return Math.abs(Hood.getInstance().get() - val) < 0.25;
	}

}
