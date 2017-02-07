package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain {
	ElectricalConstants electricalConstants = new ElectricalConstants();
	Controls controls = new Controls();
	
	Victor drive_leftFront;
	Victor drive_leftBack;
	Victor drive_rightFront;
	Victor drive_rightBack;
	
	RobotDrive myRobot;
	
	Encoder drive_encoderLeft;
	Encoder drive_encoderRight;
	
	public void init(){
		drive_leftFront = new Victor(electricalConstants.MOTOR_DRIVE_LEFT_FRONT);
		drive_leftBack = new Victor(electricalConstants.MOTOR_DRIVE_LEFT_BACK);
		drive_rightFront = new Victor(electricalConstants.MOTOR_DRIVE_RIGHT_FRONT);
		drive_rightBack = new Victor(electricalConstants.MOTOR_DRIVE_RIGHT_BACK);
		
		
		myRobot = new RobotDrive(drive_leftFront, drive_leftBack, drive_rightFront, drive_rightBack);
	}
	
	public void teleopDrive(){
		myRobot.arcadeDrive(controls.xbox_driver);
	}
	
}
