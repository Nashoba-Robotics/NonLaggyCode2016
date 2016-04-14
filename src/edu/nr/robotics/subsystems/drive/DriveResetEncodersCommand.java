package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class DriveResetEncodersCommand extends NRCommand {

	@Override
	protected void onStart() {
		Drive.getInstance().resetEncoders();
	}
}
