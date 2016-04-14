package edu.nr.lib.navx;

import edu.nr.lib.AngleUnit;

public class TestNavX implements BaseNavX {

	//Saved as degrees
	private double roll, pitch, yaw, x, y, z;
	
	public void setRoll(double roll) {
		this.roll = roll;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public TestNavX(double yaw, double pitch, double roll, AngleUnit unit, double x, double y, double z) {
		setPitch(pitch, unit);
		setRoll(roll, unit);
		setYaw(yaw, unit);
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public TestNavX() {
		setPitch(0, AngleUnit.DEGREE);
		setRoll(0, AngleUnit.DEGREE);
		setYaw(0, AngleUnit.DEGREE);
		setX(0);
		setY(0);
		setZ(0);
	}
	
	@Override
	public double getRoll(AngleUnit unit) {
		if(unit == AngleUnit.RADIAN) {
			return Math.toRadians(roll);
		}
		return roll;
	}
	
	@Override
	public double getPitch(AngleUnit unit) {
		if(unit == AngleUnit.RADIAN) {
			return Math.toRadians(pitch);
		}
		return pitch;
	}
	
	@Override
	public double getYaw(AngleUnit unit) {
		if(unit == AngleUnit.RADIAN) {
			return Math.toRadians(yaw);
		}
		return yaw;
	}
	
	@Override
	public double getYawAbsolute(AngleUnit unit) {
		return getYaw(unit);
	}

	public void setRoll(double roll, AngleUnit unit) {
		if(unit == AngleUnit.RADIAN) {
			this.roll = Math.toDegrees(roll);
		} else {
			this.roll = roll;
		}
	}

	public void setPitch(double pitch, AngleUnit unit) {
		if(unit == AngleUnit.RADIAN) {
			this.pitch = Math.toDegrees(pitch);
		} else {
			this.pitch = pitch;
		}
	}

	public void setYaw(double yaw, AngleUnit unit) {
		if(unit == AngleUnit.RADIAN) {
			this.yaw = Math.toDegrees(yaw);
		} else {
			this.yaw = yaw;
		}
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return z;
	}
}
