package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain {
	
	Victor leftFront;
	Victor leftBack;
	Victor rightFront;
	Victor rightBack;
	
	RobotDrive myRobot;
	
	Encoder encoderLeft;
	Encoder encoderRight;
	private Controls m_controls;
	ElectricalConstants eConstants = new ElectricalConstants();
	
	public DriveTrain(Controls controls) {
		m_controls = controls;
	}

	public void init(){
		leftFront = new Victor(eConstants.MOTOR_DRIVE_LEFT_FRONT);
		leftBack = new Victor(eConstants.MOTOR_DRIVE_LEFT_BACK);
		rightFront = new Victor(eConstants.MOTOR_DRIVE_RIGHT_FRONT);
		rightBack = new Victor(eConstants.MOTOR_DRIVE_RIGHT_BACK);
		
		myRobot = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
	}
	
	public void teleopDrive(){
		myRobot.arcadeDrive(m_controls.xbox_driver);
	}
	
	int sumEncoderErrors;
	double leftMotorSpeed = 0; //<======== Need to figure out how to get this?
	double MOTOR_INCREMENT_RATE = .01;
	
	
//	public void drivePID(double desiredLeftMotorSpeed){
//		double P = .02;
//		double encoderError = encoderLeft.get() - encoderRight.get();
//		double rightMotorSpeed;
//		if(desiredLeftMotorSpeed >= 0){
//			if(leftMotorSpeed < desiredLeftMotorSpeed){
//				leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
//			}else if(leftMotorSpeed > desiredLeftMotorSpeed){
//				leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
//			}else{
//				leftMotorSpeed = desiredLeftMotorSpeed;
//			}
//		} else{
//			if(leftMotorSpeed > desiredLeftMotorSpeed){
//				leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
//			}else if(leftMotorSpeed < desiredLeftMotorSpeed){
//				leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
//			}else{
//				leftMotorSpeed = desiredLeftMotorSpeed;
//			}
//		}
//		
//		rightMotorSpeed = leftMotorSpeed + P*encoderError;
//		myRobot.setLeftRightMotorOutputs(leftMotorSpeed, rightMotorSpeed);
//	}
//	public void resetEncoders(){
//		encoderLeft.reset();
//		encoderRight.reset();
//	}
//	public double leftEncoderTicks(){
//		return encoderLeft.get();
//	}
//	public double rightEncoderTicks(){
//		return encoderRight.get();
//	}
//	public void driveRotate(String direction){
//		if(direction.equalsIgnoreCase("left")){		
//			myRobot.setLeftRightMotorOutputs(.5, -.5);
//		} else if(direction.equalsIgnoreCase("right")){
//			myRobot.setLeftRightMotorOutputs(-.5, .5);
//		}
//	}
	
	
	
}
