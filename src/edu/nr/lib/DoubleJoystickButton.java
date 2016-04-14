package edu.nr.lib;

import edu.wpi.first.wpilibj.buttons.Button;

public class DoubleJoystickButton extends Button {

	Button m_buttonNumberOne;
	Button m_buttonNumberTwo;
	
	public DoubleJoystickButton(Button buttonNumberOne, Button buttonNumberTwo) {
		m_buttonNumberOne = buttonNumberOne;
		m_buttonNumberTwo = buttonNumberTwo;
	}
	
	@Override
	public boolean get() {
		return m_buttonNumberOne.get() && m_buttonNumberTwo.get();
	}
}
