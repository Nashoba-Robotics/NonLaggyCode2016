package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.Position;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.NavX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FieldCentric implements SmartDashboardSource {

	// NOTE: X is forward, Y is side-to-side

	private static FieldCentric singleton;
	private final double initialTheta;
	private double initialGyro = 0;
	private double x = 0, y = 0, dis = 0, lastEncoderDistance = 0;
	private long lastUpdateTime;

	public static FieldCentric getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new FieldCentric(Math.PI / 2);
		}
	}

	public FieldCentric(double initialTheta) {
		lastUpdateTime = System.currentTimeMillis();
		this.initialTheta = initialTheta;
	}

	/**
	 * Updates the model based on the current Drive values
	 */
	public void update() {
		if (System.currentTimeMillis() - lastUpdateTime > 500) {
			System.err.println("WARNING: FieldCentric not being called often enough: ("
					+ (System.currentTimeMillis() - lastUpdateTime) / 1000f + "s)");
		}

		double angle = getAngle(AngleUnit.RADIAN);

		
		double ave = Drive.getInstance().getEncoderAverageDistance();
		double delta_x_r = ave - lastEncoderDistance;
		double deltax = delta_x_r * Math.sin(angle);
		double deltay = delta_x_r * Math.cos(angle);
		x += deltax;
		y += deltay;
		dis += delta_x_r;

		lastEncoderDistance = ave;

		lastUpdateTime = System.currentTimeMillis();
	}

	/**
	 * Gets the distance the robot has moved since FieldCentric was reset
	 * 
	 * @return the distance in meters
	 */
	public double getDistance() {
		return dis;
	}

	/**
	 * Gets the x distance the robot has moved since FieldCentric was reset
	 * 
	 * @return the x distance in meters
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y distance the robot has moved since FieldCentric was reset
	 * 
	 * @return the y distance in meters
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the Position change the robot has moved since FieldCentric was reset
	 * 
	 * @return the Position, where the values are in meters
	 */
	public Position getPosition() {
		return new Position(x, y);
	}

	/**
	 * Gets the angle used for current coordinate calculations
	 * @param unit the AngleUnit to return in
	 * @return the angle in the given units
	 */
	public double getAngle(AngleUnit unit) {
		// Gyro is reversed (clockwise causes an increase in the angle)
		double val = ((NavX.getInstance().getYaw(AngleUnit.RADIAN)) - initialGyro) * -1 + initialTheta;
		if(unit == AngleUnit.RADIAN)
			return val;
		if(unit == AngleUnit.DEGREE)
			return Math.toDegrees(val);
		return 0;
	}

	/**
	 * Resets the model
	 */
	public void reset() {
		x = 0;
		y = 0;
		dis = 0;
		lastEncoderDistance = Drive.getInstance().getEncoderAverageDistance();
		initialGyro = NavX.getInstance().getYaw(AngleUnit.RADIAN);
	}

	@Override
	public void smartDashboardInfo() {
		SmartDashboard.putNumber("NavX Yaw", (NavX.getInstance().getYaw(AngleUnit.DEGREE)));
	}
}
