package edu.nr.robotics;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	public static final double OVER_DISTANCE_25 = 15; //The distance to go over obstacles 2 and 5 completely in auto
	public static final double ONTO_DISTANCE_25 = 7; //The distance to go onto obstacles 2 and 5 completely in auto

	public static final double OVER_DISTANCE_134 = 18; //The distance to go over obstacles 1, 3, and 4 completely in auto
	public static final double ONTO_DISTANCE_134 = 10; //The distance to go onto obstacles  1, 3, and 4 completely in auto
	
	public static final long LIGHTS_BLINK_PERIOD = 200; //in milliseconds

	public static final int TALON_RIGHT_A = 8;
	public static final int TALON_RIGHT_B = 9;
	public static final int TALON_LEFT_A = 4;
	public static final int TALON_LEFT_B = 3;

	public static final double MAX_SPEED = 11; // Feet per second

	
	public static final int INTAKE_PHOTO_GATE = 9; //Label is D - Banner 1
	public static final int LOADER_PHOTO_GATE = 7; //Label is C - Banner 2
	public static final int SHOOTER_PHOTO_GATE = 8; //Label is B - Banner 3
	public static final int SHOOTER_RATE_PORT = 6; //Label 

	public static final int LIGHTS_SPIKE = 0;
	
	public static final int INTAKE_ARM_TALON = 11;
	public static final int INTAKE_ARM_POT = 0;
	public static final int HOOD_TALON = 6;
	public static final int ELEVATOR_TALON = 2;
	
	public static final int LOADER_ROLLER_TALON     = 10;

	public static final int INTAKE_ROLLER_TALON     = 1;
	public static final int SHOOTER_TALON_A = 7;
	public static final int SHOOTER_TALON_B = 5;
	
	public static final double SHOOTER_FAST_SPEED = 0.90; //out of 1.0
	public static final double SHOOTER_SLOW_SPEED = 0.3; //out of 1.0
	public static final double SHOOTER_MAX_SPEED = 210; //In rotations per second
	public static final double CLOSE_SHOT_POSITION = 27.9;
	public static final double LONG_SHOT_POSITION = 55;
	public static final double SHOOTER_RAMP_RATE = 5;
	public static final double HOOD_BOTTOM_POSITION = 0;
	public static final double HOOD_TOP_POSITION = 75.46;
	public static final double HOOD_TICK_TO_ANGLE_MULTIPLIER = 0.031934;
	public static final double HOOD_THRESHOLD = 2;
	public static final double INTAKE_ARM_THRESHOLD = 0.003;
	public static final double INTAKE_OFFSET = 0.004;
	public static final double INTAKE_TOP_POS = 0.655 + INTAKE_OFFSET;
	public static final double INTAKE_INTAKE_POS = 0.522 + INTAKE_OFFSET;
	public static final double INTAKE_HOME_POS = 0.522 + INTAKE_OFFSET; //home == intake
	public static final double INTAKE_BOTTOM_POS = 0.494 + INTAKE_OFFSET;
	public static final double INTAKE_ARM_TICK_TO_ANGLE_MULTIPLIER = -0.00222;
	public static final double TURN_THRESHOLD = 0.4;
	public static final double SHOOTER_THRESHOLD = 0.05;
	
	public static final double ELEVATOR_UP_SPEED = 1;
	public static final double ELEVATOR_THRESHOLD = 1000;

	public static final double ELEVATOR_EXTEND_DISTANCE = -112000;
	public static final double ELEVATOR_RESET_UP_DISTANCE = 12000;
	public static final double ELEVATOR_UNLATCH_DISTANCE = 5650;

	public static final double TURN_P = 0.023;
	public static final double TURN_I = 0.035;
	public static final double TURN_D = 0.00055;
	public static final double HOOD_HANG_SHOT = 29;
	public static final double DRIVE_TURN_P = 0.1;
	public static final double DRIVE_TURN_I = 0.00007;
	public static final double DRIVE_TURN_D = 1;
	public static final double DRIVE_TURN_F = 0.03;
	public static final double DRIVE_P = 0.03;
	public static final double DRIVE_I = 0;
	public static final double DRIVE_D = 0;
	public static final double DRIVE_F = 0.02;
	public static final double AUTON_ONE_ALIGN_ANGLE = 50;
	public static final double AUTON_TWO_ALIGN_ANGLE = 40;
	public static final double AUTON_THREE_ALIGN_ANGLE = 15;
	public static final double AUTON_FOUR_ALIGN_ANGLE = -10;
	public static final double AUTON_FIVE_ALIGN_ANGLE = -35;
	
	public static final double INTAKE_ARM_P = 42.18;
	public static final double INTAKE_ARM_I = 0.04218;
	public static final double INTAKE_ARM_D = 0;
}
