package edu.nr.robotics.subsystems.hood;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class HoodBottomCommand extends CommandGroup {
    
	
    public  HoodBottomCommand() {
        addSequential(new HoodPositionCommand(RobotMap.HOOD_BOTTOM_POSITION));
        requires(Hood.getInstance());
    }

	public HoodBottomCommand(double speed) {
        addSequential(new HoodPositionCommand(RobotMap.HOOD_BOTTOM_POSITION, speed));
        requires(Hood.getInstance());
	}
	
	@Override
	public boolean isFinished() {
		return Hood.Position.BOTTOM.isAtPosition();
	}
}
