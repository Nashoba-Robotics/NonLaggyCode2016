package edu.nr.robotics.auton;

import edu.nr.lib.AngleGyroCorrectionSource;
import edu.nr.lib.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.Robot.defense;
import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonFollowInstructionsShootCommand extends CommandGroup {
    
	
	
    public  AutonFollowInstructionsShootCommand() {
    	
    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());
    	GetGyro gyro = new GetGyro();
    	addSequential(gyro);
    	addSequential(new AutonForwardDefenseCommand((defense) Robot.getInstance().defensePicker.getSelected()));
		addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_INTAKE_POS, 0.05));
		addParallel(new HoodPositionCommand(45));
    	addSequential(new AutonTurnPositionCommand((Robot.position) Robot.getInstance().positionPicker.getSelected(), gyro));
		addSequential(new AutonAlignCommand());

    }
    
    public class GetGyro extends NRCommand {
    	
    	AngleGyroCorrectionSource gyro;
    	
    	protected void onStart() {
    		gyro = new AngleGyroCorrectionSource();
    	}

		public AngleGyroCorrectionSource getCorrection() {
			return gyro;
		}
    }
}
