package edu.nr.robotics.commandgroups;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmHomeHeightCommandGroup;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PrepareLongShotCommandGroup extends CommandGroup {
    
    public  PrepareLongShotCommandGroup() {
        addParallel(new HoodPositionCommand(RobotMap.LONG_SHOT_POSITION));
        addParallel(new IntakeArmHomeHeightCommandGroup());
    }
}
