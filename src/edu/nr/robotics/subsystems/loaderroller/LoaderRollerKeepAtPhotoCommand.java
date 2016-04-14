package edu.nr.robotics.subsystems.loaderroller;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.subsystems.intakeroller.IntakeRoller;
import edu.nr.robotics.subsystems.shooter.Shooter;

public class LoaderRollerKeepAtPhotoCommand extends NRCommand {

	public LoaderRollerKeepAtPhotoCommand() {
		requires(LoaderRoller.getInstance());
	}
	
	@Override
	protected void onExecute() {
		
		if(Shooter.getInstance().hasBall()) {
			LoaderRoller.getInstance().setLoaderSpeed(-0.4);
		} else if(!LoaderRoller.getInstance().hasBall() && IntakeRoller.getInstance().hasBall()) {
			LoaderRoller.getInstance().setLoaderSpeed(0.4);
		} else {
			LoaderRoller.getInstance().setLoaderSpeed(0);
		}
	}
	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
}
