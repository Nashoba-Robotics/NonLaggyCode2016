package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ElevatorResetCommand extends CommandGroup {

	public ElevatorResetCommand() {
    	addSequential(new WaitForElevatorStoppedForTwoSeconds());
        addParallel(new ElevatorVoltageCommand(-0.6));
        addSequential(new ElevatorWaitForMotorStallTimeCommand(1));
        addParallel(new ElevatorVoltageCommand(RobotMap.ELEVATOR_UP_SPEED * 0.6));
        addSequential(new ElevatorWaitUntilChangedByCommand(RobotMap.ELEVATOR_RESET_UP_DISTANCE));
        addSequential(new ElevatorResetEncoderCommand());
        addParallel(new ElevatorOffCommand());
    }
	
	public class WaitForElevatorStoppedForTwoSeconds extends NRCommand {

		long startTime = -1;
		
		long resetTime = -1;
			
		boolean isFinished = false;
		
		public WaitForElevatorStoppedForTwoSeconds() {
			requires(Elevator.getInstance());
		}
		
		@Override
		protected void onStart() {
			resetTime = System.currentTimeMillis();
		}
		
		@Override
		protected void onExecute() {
	    	Elevator.getInstance().setMotorValue(OI.getInstance().getElevatorMoveValue());

			if(startTime != -1) {
				if(OI.getInstance().getElevatorMoveValue() == 0) {
					if(System.currentTimeMillis() - startTime > 3000) {
						isFinished = true;
					}
				} else {
					startTime = System.currentTimeMillis();
				}
			} else {
				if(OI.getInstance().getElevatorMoveValue() != 0) {
					startTime = System.currentTimeMillis();
				}
			}
		}
		
		@Override
		protected void onEnd() {
			isFinished = false;
			startTime = -1;
		}
		
		@Override
		protected boolean isFinishedNR() {
			if (startTime == -1 && System.currentTimeMillis() - resetTime > 10000) {
				NRCommand.cancelCommand(this.getGroup());
			}
			return isFinished || (startTime == -1 && System.currentTimeMillis() - resetTime > 10000);
		}

	}
}
