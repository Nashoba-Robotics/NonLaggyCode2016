package edu.nr.robotics.subsystems.light;

import edu.nr.lib.NRCommand;

public class LightOnCommand extends NRCommand {
	
	public LightOnCommand() {
		requires(Light.getInstance());
	}
	
	@Override
	public void onStart() {
		Light.getInstance().setLightOn();
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
