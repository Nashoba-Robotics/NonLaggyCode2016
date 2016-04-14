package edu.nr.lib.navx;

import edu.nr.lib.AngleUnit;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class NavX implements BaseNavX, LiveWindowSendable {
	
	private SerialPort serial_port;
	private IMUAdvanced imu;

	protected NavX() {
		serial_port = new SerialPort(57600, SerialPort.Port.kMXP);

		byte update_rate_hz = 100;
		// imu = new IMU(serial_port,update_rate_hz);
		imu = new IMUAdvanced(serial_port, update_rate_hz);
	}

	int fullRotationCount = 0;
	double lastYaw;

	/**
	 * Gets the current yaw of the robot in the given units
	 * @param unit the {@link AngleUnit} to return in
	 * @return the yaw
	 */
	@Override
	public double getYaw(AngleUnit unit) {
		if (imu != null && imu.isConnected()) {
			double currentYaw = imu.getYaw();
			if ((lastYaw < -90 || lastYaw > 90) && (currentYaw > 90 || currentYaw < -90)) {
				if (lastYaw < 0 && currentYaw > 0) {
					fullRotationCount--;
				} else if (lastYaw > 0 && currentYaw < 0) {
					fullRotationCount++;
				}
			}

			lastYaw = currentYaw;
			double valueDeg = currentYaw + 360 * fullRotationCount;
			if(unit == AngleUnit.DEGREE) {
				return valueDeg;
			}
			if(unit == AngleUnit.RADIAN) {
				return Math.toRadians(valueDeg);
			}
		}
		return 0;
	}
	
	@Override
	public double getYawAbsolute(AngleUnit unit) {
		if (imu != null && imu.isConnected()) {
			float yaw = imu.getYaw();
			if(unit == AngleUnit.DEGREE) {
				return yaw;
			}
			if(unit == AngleUnit.RADIAN) {
				return Math.toRadians(yaw);
			}
		}
		return 0;
	}

	/**
	 * Gets the current roll of the robot in the given units
	 * @param unit the {@link AngleUnit} to return in
	 * @return the roll
	 */
	@Override
	public double getRoll(AngleUnit unit) {
		if (imu != null && imu.isConnected()) {
			float roll = imu.getRoll();
			if(unit == AngleUnit.DEGREE) {
				return roll;
			}
			if(unit == AngleUnit.RADIAN) {
				return Math.toRadians(roll);
			}
		}
		return 0;
	}

	/**
	 * Gets the current pitch of the robot in the given units
	 * @param unit the {@link AngleUnit} to return in
	 * @return the pitch
	 */
	@Override
	public double getPitch(AngleUnit unit) {
		if (imu != null && imu.isConnected()) {
			if(unit == AngleUnit.DEGREE) {
				return imu.getPitch();
			}
			if(unit == AngleUnit.RADIAN) {
				return Math.toRadians(imu.getPitch());
			}
		}
		return 0;
	}

	// Singleton code
	private static NavX singleton;

	public static NavX getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new NavX();
		}
	}

	@Override
	public double getX() {
		return imu.getWorldLinearAccelX();
	}

	@Override
	public double getY() {
		return imu.getWorldLinearAccelY();
	}

	@Override
	public double getZ() {
		return imu.getWorldLinearAccelZ();
	}

	private ITable m_table;
	
	@Override
	public void initTable(ITable subtable) {
	    m_table = subtable;
	    updateTable();		
	}

	@Override
	public ITable getTable() {
	    return m_table;
	}

	@Override
	public String getSmartDashboardType() {
		return "NavX";
	}

	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putNumber("NavX Yaw", getYawAbsolute(AngleUnit.DEGREE));
			m_table.putNumber("NavX Roll", getRoll(AngleUnit.DEGREE));
			m_table.putNumber("NavX Pitch", getPitch(AngleUnit.DEGREE));
			
			m_table.putNumber("NavX X Accel", getX());
			m_table.putNumber("NavX Y Accel", getY());
			m_table.putNumber("NavX Z Accel", getZ());
		}
	}

	@Override
	public void startLiveWindowMode() {}

	@Override
	public void stopLiveWindowMode() {}
}
