package edu.nr.lib;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AngleGyroCorrectionSource extends AngleGyroCorrection implements PIDSource {

	PIDSourceType type;
	
	public AngleGyroCorrectionSource(Angle angle, NavX navx) {
		super(angle, navx);
		type = PIDSourceType.kDisplacement;
	}
	
	public AngleGyroCorrectionSource(NavX navx) {
		super(navx);
	}
	
	public AngleGyroCorrectionSource() {
		super();
	}
	
	public AngleGyroCorrectionSource(Angle angle) {
		super(angle);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		type = pidSource;
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return type;
	}

	@Override
	public double pidGet() {
		return super.getAngleError().get(Unit.DEGREE);
	}
}
