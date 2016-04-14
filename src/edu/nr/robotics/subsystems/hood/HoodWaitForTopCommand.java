package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodWaitForTopCommand extends NRCommand {

	public HoodWaitForTopCommand() {
		requires(Hood.getInstance());
	}
	
	@Override
	public boolean isFinishedNR() {
		return Hood.getInstance().isTopLimitSwitchClosed();
	}

}
