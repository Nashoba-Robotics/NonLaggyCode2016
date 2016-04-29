package edu.nr.robotics;

import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.commandgroups.AlignCommandGroup;
import edu.nr.robotics.subsystems.climb.Elevator;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.intakeroller.IntakeRoller;
import edu.nr.robotics.subsystems.loaderroller.LoaderRoller;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class RobotDiagram implements NamedSendable {

	public RobotDiagram() {
	}

	private ITable table;

	@Override
	public void initTable(ITable table) {
		this.table = table;
		if (table != null) {
			

			table.putString("~TYPE~", "robo-diagram");

			table.putBoolean("Auto Align Happening", false);
			table.putBoolean("All Systems Go", Math.abs(AndroidServer.getInstance().getTurnAngle()) < 0.5 && Shooter.getInstance().getSped() && Math.abs(Hood.distanceToAngle(AndroidServer.getInstance().getDistance()) - Hood.getInstance().get()) < RobotMap.HOOD_THRESHOLD);
			
			//Hood
			table.putBoolean("Hood Bottom", Hood.getInstance().isAtPosition(Hood.Position.BOTTOM));
			table.putBoolean("Hood Top", Hood.getInstance().isAtPosition(Hood.Position.TOP));
			table.putNumber("Hood Angle", Hood.getInstance().get());
			
	    	boolean hoodAtThreshold;
	    	if(!AndroidServer.getInstance().goodToGo()) { 
	    		hoodAtThreshold = false;
	    	} else {
	    		hoodAtThreshold = Math.abs(Hood.getInstance().get() - Hood.distanceToAngle(AndroidServer.getInstance().getDistance())) > RobotMap.HOOD_THRESHOLD;
	    	}
	    	
			table.putBoolean("Hood at Threshold", hoodAtThreshold);
			table.putNumber("Shot distance at angle", Hood.angleToDistance(Hood.getInstance().get()));
			
			//Intake Arm
			table.putBoolean("Intake Top Stop", IntakeArm.getInstance().get() > RobotMap.INTAKE_TOP_POS + RobotMap.INTAKE_ARM_THRESHOLD);
			table.putBoolean("Intake Top", IntakeArm.getInstance().get() > RobotMap.INTAKE_TOP_POS - RobotMap.INTAKE_ARM_THRESHOLD && IntakeArm.getInstance().get() < RobotMap.INTAKE_TOP_POS + RobotMap.INTAKE_ARM_THRESHOLD);
			table.putBoolean("Intake Top Intake", IntakeArm.getInstance().get() < RobotMap.INTAKE_TOP_POS - RobotMap.INTAKE_ARM_THRESHOLD && IntakeArm.getInstance().get() > RobotMap.INTAKE_INTAKE_POS + RobotMap.INTAKE_ARM_THRESHOLD);
			table.putBoolean("Intake Intake", IntakeArm.getInstance().get() > RobotMap.INTAKE_INTAKE_POS - RobotMap.INTAKE_ARM_THRESHOLD && IntakeArm.getInstance().get() < RobotMap.INTAKE_INTAKE_POS + RobotMap.INTAKE_ARM_THRESHOLD);
			//Note: this assumes that intake height and home height are the same.
			table.putBoolean("Intake Intake Home", IntakeArm.getInstance().get() < RobotMap.INTAKE_INTAKE_POS - RobotMap.INTAKE_ARM_THRESHOLD && IntakeArm.getInstance().get() > RobotMap.INTAKE_BOTTOM_POS + RobotMap.INTAKE_ARM_THRESHOLD);
			//This is since home and intake are the same height
			table.putBoolean("Intake Home", false);
			table.putBoolean("Intake Home Bottom", IntakeArm.getInstance().get() < RobotMap.INTAKE_HOME_POS - RobotMap.INTAKE_ARM_THRESHOLD && IntakeArm.getInstance().get() > RobotMap.INTAKE_BOTTOM_POS + RobotMap.INTAKE_ARM_THRESHOLD);
			table.putBoolean("Intake Bottom", IntakeArm.getInstance().get() > RobotMap.INTAKE_BOTTOM_POS - RobotMap.INTAKE_ARM_THRESHOLD && IntakeArm.getInstance().get() < RobotMap.INTAKE_BOTTOM_POS + RobotMap.INTAKE_ARM_THRESHOLD);
			//Note: includes bottom height as well
			table.putBoolean("Intake Bottom Stop", IntakeArm.getInstance().get() < RobotMap.INTAKE_BOTTOM_POS + RobotMap.INTAKE_ARM_THRESHOLD);
						
			table.putBoolean("Photo 1", IntakeRoller.getInstance().hasBall());
			table.putBoolean("Photo 2", LoaderRoller.getInstance().hasBall());
			table.putBoolean("Photo 3", Shooter.getInstance().hasBall());
			
			//Elevator
			table.putBoolean("Elevator Bottom Height", Elevator.getInstance().getEncoder() > RobotMap.ELEVATOR_EXTEND_DISTANCE * 0.1);
			table.putBoolean("Elevator Top Height", Elevator.getInstance().getEncoder() < RobotMap.ELEVATOR_EXTEND_DISTANCE * 0.9);
			table.putBoolean("Elevator Motor Running", Elevator.getInstance().isMoving());

			//Shooter
			table.putNumber("Shooter Speed", Shooter.getInstance().getScaledSpeed());
			
			table.putBoolean("Got Low", Hood.getInstance().isBotLimitSwitchClosed() && IntakeArm.getInstance().get() < RobotMap.INTAKE_INTAKE_POS + 0.02);
			

		}
	}

	@Override
	public ITable getTable() {
		return table;
	}

	@Override
	public String getSmartDashboardType() {
		return "Robot Diagram";
	}

	@Override
	public String getName() {
		return "Robot Diagram";
	}

}
