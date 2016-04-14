package edu.nr.robotics.subsystems.loaderroller;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;

/**
 *
 */
public class LoaderRollerJoystickCommand extends NRCommand {

    public LoaderRollerJoystickCommand() {
        requires(LoaderRoller.getInstance());
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	LoaderRoller.getInstance().setLoaderSpeed(-OI.getInstance().getLoaderRollerMoveValue());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return false;
    }
}
