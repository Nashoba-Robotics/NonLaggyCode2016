package edu.nr.robotics.auton;

import edu.nr.lib.AngleGyroCorrectionSource;
import edu.nr.lib.AngleUnit;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonReturnToNormalFrontCommandGroup extends CommandGroup {
    
	
	/**
	 * Only works in autonomous because of the gyroscope method
	 */
    public  AutonReturnToNormalFrontCommandGroup() {
        AngleGyroCorrectionSource correction = new AngleGyroCorrectionSource();
        addSequential(new DriveAnglePIDCommand(0.0, AngleUnit.DEGREE));
    }
}
