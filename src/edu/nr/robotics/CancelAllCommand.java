package edu.nr.robotics;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.subsystems.climb.Elevator;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.intakeroller.IntakeRoller;
import edu.nr.robotics.subsystems.loaderroller.LoaderRoller;
import edu.nr.robotics.subsystems.shooter.Shooter;

public class CancelAllCommand extends NRCommand {

	public CancelAllCommand() {
		requires(Drive.getInstance());
		requires(Shooter.getInstance());
		requires(IntakeArm.getInstance());
		requires(LoaderRoller.getInstance());
		requires(Elevator.getInstance());
		requires(Hood.getInstance());
		requires(IntakeRoller.getInstance());
	}
	
	@Override
	public void onStart() {
		IntakeArm.getInstance().disable();
		//Fix intake arm cancelling
		IntakeRoller.getInstance().setRollerSpeed(0);
		LoaderRoller.getInstance().setLoaderSpeed(0);
		Hood.getInstance().disablePID();
		Shooter.getInstance().setMotor(0);
		Elevator.getInstance().setMotorValue(0);
	}
}
