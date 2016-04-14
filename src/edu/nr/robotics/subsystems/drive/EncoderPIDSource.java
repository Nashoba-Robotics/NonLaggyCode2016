package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class EncoderPIDSource implements PIDSource {

	PIDSourceType pidType = PIDSourceType.kDisplacement;
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		pidType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidType;
	}

	@Override
	public double pidGet() {
		if(pidType == PIDSourceType.kDisplacement)
			return Drive.getInstance().getEncoderAverageDistance();
		return Drive.getInstance().getEncoderAverageSpeed();
	}

}
