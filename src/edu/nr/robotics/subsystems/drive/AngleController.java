package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDOutput;

public class AngleController implements PIDOutput {
	
	public AngleController() {
	}

	@Override
	public void pidWrite(double output) {
		Drive.getInstance().tankDrive(-output, output);
	}

}
