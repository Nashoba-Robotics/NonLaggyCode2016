package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class HoodResetEncoderCommand extends NRCommand {

	@Override
	protected void onStart() {
		Hood.getInstance().resetEncoder();
	}
}
