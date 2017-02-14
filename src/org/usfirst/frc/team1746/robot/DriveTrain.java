package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain {
	ElectricalConstants electricalConstants = new ElectricalConstants();
	Controls controls = new Controls();
	
	Victor leftFront;
	Victor leftBack;
	Victor rightFront;
	Victor rightBack;
	
	RobotDrive myRobot;
	
	Encoder encoderLeft;
	Encoder encoderRight;
	
	public void init(){
		leftFront = new Victor(electricalConstants.MOTOR_DRIVE_LEFT_FRONT);
		leftBack = new Victor(electricalConstants.MOTOR_DRIVE_LEFT_BACK);
		rightFront = new Victor(electricalConstants.MOTOR_DRIVE_RIGHT_FRONT);
		rightBack = new Victor(electricalConstants.MOTOR_DRIVE_RIGHT_BACK);
		
		myRobot = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
	}
	
	public void teleopDrive(){
		myRobot.arcadeDrive(controls.xbox_driver);
	}
	
	int sumEncoderErrors;
	double leftMotorSpeed = 0; //<======== Need to figure out how to get this?
	double MOTOR_INCREMENT_RATE = .01;
	
	
	public void drivePID(double desiredLeftMotorSpeed){
		double P = .02;
		double encoderError = encoderLeft.get() - encoderRight.get();
		double rightMotorSpeed;
		if(desiredLeftMotorSpeed >= 0){
			if(leftMotorSpeed < desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
			}else if(leftMotorSpeed > desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
			}else{
				leftMotorSpeed = desiredLeftMotorSpeed;
			}
		} else{
			if(leftMotorSpeed > desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
			}else if(leftMotorSpeed < desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
			}else{
				leftMotorSpeed = desiredLeftMotorSpeed;
			}
		}
		
		rightMotorSpeed = leftMotorSpeed + P*encoderError;
		myRobot.setLeftRightMotorOutputs(leftMotorSpeed, rightMotorSpeed);
	}
	public void resetEncoders(){
		encoderLeft.reset();
		encoderRight.reset();
	}
	public double leftEncoderTicks(){
		return encoderLeft.get();
	}
	public double rightEncoderTicks(){
		return encoderRight.get();
	}
	public void driveRotate(String direction){
		if(direction.equalsIgnoreCase("left")){		
			myRobot.setLeftRightMotorOutputs(.5, -.5);
		} else if(direction.equalsIgnoreCase("right")){
			myRobot.setLeftRightMotorOutputs(-.5, .5);
		}
	}
	
	
	
}
