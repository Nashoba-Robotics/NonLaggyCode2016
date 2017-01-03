package edu.nr.robotics.auton;

import edu.nr.lib.AngleGyroCorrectionSource;
import edu.nr.lib.AngleUnit;
import edu.nr.lib.GetGyroCommand;
import edu.nr.lib.NRCommand;
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
    	GetGyroCommand gyro = new GetGyroCommand();
    	addSequential(gyro);
    	addSequential(new AutonForwardDefenseCommand((defense) Robot.getInstance().defensePicker.getSelected()));
		addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_INTAKE_POS, 0.05));
		addParallel(new HoodPositionCommand(45));
    	addSequential(new AutonTurnPositionCommand((Robot.position) Robot.getInstance().positionPicker.getSelected(), gyro));
		addSequential(new AutonAlignCommand());

    }
}
