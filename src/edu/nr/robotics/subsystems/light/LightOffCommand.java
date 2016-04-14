package edu.nr.robotics.subsystems.light;

import edu.nr.lib.NRCommand;

public class LightOffCommand extends NRCommand {
	
	public LightOffCommand() {
		requires(Light.getInstance());
	}
	
	@Override
	public void onStart() {
		Light.getInstance().setLightOff();
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
