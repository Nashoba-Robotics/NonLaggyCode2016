package edu.nr.lib;

import edu.nr.lib.navx.BaseNavX;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AngleGyroCorrectionSource extends AngleGyroCorrection implements PIDSource {

	PIDSourceType type;
	
	public AngleGyroCorrectionSource(double angle, BaseNavX navx, AngleUnit unit) {
		super(angle, navx, unit);
		type = PIDSourceType.kDisplacement;
	}
	
	public AngleGyroCorrectionSource(double angle, AngleUnit unit) {
		super(angle, unit);
	}
	
	public AngleGyroCorrectionSource(BaseNavX navx) {
		super(navx);
	}
	
	public AngleGyroCorrectionSource() {
		super();
	}
	
	public AngleGyroCorrectionSource(AngleUnit unit) {
		super(unit);
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
		return super.getAngleErrorDegrees();
	}

	public AngleUnit getUnit() {
		return unit;
	}
}
