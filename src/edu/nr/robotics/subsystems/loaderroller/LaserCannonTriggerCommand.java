package edu.nr.robotics.subsystems.loaderroller;

import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.intakeroller.IntakeRoller;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class LaserCannonTriggerCommand extends CommandGroup {

	public LaserCannonTriggerCommand() {
		addParallel(new LoaderRollerIntakeCommand());
		addSequential(new WaitCommand(3));
		addSequential(new LoaderRollerNeutralCommand());
	}
	
	@Override
	public void initialize() {
		System.out.println("Shooter button pressed. Photo 1: " 
				+ IntakeRoller.getInstance().hasBall() 
				+ " Photo 2: " + LoaderRoller.getInstance().hasBall() 
				+ " Photo 3: " + IntakeRoller.getInstance().hasBall()
				+ " Shooter Speed: " + Shooter.getInstance().getScaledSpeed() 
				+ " Hood angle: " + Hood.getInstance().get());
	}

}
