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
		
		System.out.println("Hood jetson check started");
	}

	@Override
	protected void onEnd(boolean interrupted) {
		System.out.println("Just finished Hood Jetson check");
    	goodToGo = true;
	}

	@Override
	protected boolean isFinishedNR() {
		
		System.out.println("Checking hood finish? Error: " + (Hood.getInstance().get() - val));
		
		if(!goodToGo)
			return true;
		return false;
	}

	public void setAngleAgain() {
		val = Hood.distanceToAngle(AndroidServer.getInstance().getDistance());
		Hood.getInstance().setSetpoint(val);		
	}

	public double getSetpoint() {
		return Hood.getInstance().getSetpoint();
	}

}
