package edu.nr.robotics.subsystems.climb;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ElevatorRetractCommand extends CommandGroup {

    public ElevatorRetractCommand() {
    	    	
    	addParallel(new ElevatorVoltageCommand(-1));
        addSequential(new ElevatorWaitForMotorStallTimeCommand(1));
        addParallel(new ElevatorOffCommand());
        
    }
}
