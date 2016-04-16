package edu.nr.robotics.auton;

import edu.nr.lib.AngleGyroCorrectionSource;
import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.robotics.Robot;
import edu.nr.robotics.Robot.defense;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonFollowInstructionsShootCommand extends CommandGroup {
    
	
	
    public  AutonFollowInstructionsShootCommand() {
    	
    	GetGyro gyro = new GetGyro();
    	addSequential(gyro);
    	addSequential(new AutonForwardDefenseCommand(defense.LowBar));
        addSequential(new AutonTurnPositionCommand(Robot.position.One, gyro));

    	//addSequential(new AutonForwardDefenseCommand((defense) Robot.getInstance().defensePicker.getSelected()));
        //addSequential(new AutonTurnPositionCommand((Robot.position) Robot.getInstance().positionPicker.getSelected(), gyro));
		addSequential(new AutonAlignCommand());

    }
    
    public class GetGyro extends NRCommand {
    	
    	AngleGyroCorrectionSource gyro;
    	
    	protected void onStart() {
    		gyro = new AngleGyroCorrectionSource(AngleUnit.DEGREE);
    	}

		public AngleGyroCorrectionSource getCorrection() {
			return gyro;
		}
    }
}
