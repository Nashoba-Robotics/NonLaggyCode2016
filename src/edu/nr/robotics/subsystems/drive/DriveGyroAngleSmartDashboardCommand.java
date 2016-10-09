package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveGyroAngleSmartDashboardCommand extends NRCommand {	
	
	public static double turn_p = RobotMap.TURN_P;
	public static double turn_i = RobotMap.TURN_I;
	public static double turn_d = RobotMap.TURN_D;

	@Override
	protected void onStart() {
		System.out.println("Starting gyro angle smart dashboard command. Angle: " + SmartDashboard.getNumber("Gyro Angle"));
		new DriveAnglePIDCommand(new Angle(Unit.DEGREE,SmartDashboard.getNumber("Gyro Angle"))).start();
	}
    
}
