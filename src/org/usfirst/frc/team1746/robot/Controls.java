package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controls {
	ElectricalConstants electricalConstants = new ElectricalConstants();
	DriveTrain drive = new DriveTrain();
	
	Joystick xbox_driver;
	Joystick xbox_operator;
	
	public void init(){
		xbox_driver = new Joystick(electricalConstants.JOYSTICK_DRIVER);
		xbox_operator = new Joystick(electricalConstants.JOYSTICK_OPERATOR);
	}
	
	public boolean servoP(){
		return xbox_driver.getRawButton(2);
	}
	public boolean servoN(){
		return xbox_driver.getRawButton(3);
	}
	public boolean servoOff(){
		return xbox_driver.getRawButton(1);
	}
}
