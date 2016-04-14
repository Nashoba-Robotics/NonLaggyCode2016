package edu.nr.robotics.commandgroups;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.hood.HoodBottomCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GetLowCommandGroup extends CommandGroup {
    
    public  GetLowCommandGroup() {
        addParallel(new HoodBottomCommand());
        addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_INTAKE_POS));
    }
}
