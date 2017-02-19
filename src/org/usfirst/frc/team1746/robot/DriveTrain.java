package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Class setup
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	ElectricalConstants eConstants = new ElectricalConstants();
	
	private Controls m_controls;
	public Drivetrain(Controls controls) {
		m_controls = controls;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Drivetrain init
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	Victor leftFront;
	Victor leftBack;
	Victor rightFront;
	Victor rightBack;
	
	RobotDrive myRobot;
	
	Encoder encoderLeft;
	Encoder encoderRight;
	
	Gyro gyro;

	PIDController pid;
	
	public void init(){
		leftFront = new Victor(eConstants.MOTOR_DRIVE_LEFT_FRONT);
		leftBack = new Victor(eConstants.MOTOR_DRIVE_LEFT_BACK);
		rightFront = new Victor(eConstants.MOTOR_DRIVE_RIGHT_FRONT);
		rightBack = new Victor(eConstants.MOTOR_DRIVE_RIGHT_BACK);
		
		myRobot = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		
		encoderLeft = new Encoder(eConstants.ENCODER_DRIVE_LEFT_A, eConstants.ENCODER_DRIVE_LEFT_B, false, Encoder.EncodingType.k1X);
		encoderRight = new Encoder(eConstants.ENCODER_DRIVE_RIGHT_A, eConstants.ENCODER_DRIVE_RIGHT_B, false, Encoder.EncodingType.k1X);
		
		gyro = new ADXRS450_Gyro();
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Drivetrain Functions
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void teleopArcadeDrive(){
		myRobot.arcadeDrive(m_controls.xbox_driver);
	}
	public void teleopTankDrive(){
		myRobot.tankDrive(m_controls.xbox_driver, m_controls.driver_leftAxis(), m_controls.xbox_driver, m_controls.driver_rightAxis());
	}
	
	int sumEncoderErrors;
	double leftMotorSpeed = 0; //<======== Need to figure out how to get this?
	double MOTOR_INCREMENT_RATE = .01;
	
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
	
	public void resetGyro(){
		gyro.reset();
	}
	public double gyroAngle(){
		return gyro.getAngle();
	}
	
	public void initSmartDashboard(){
		
	}
	public void updateSmartDashboard(){
		SmartDashboard.putNumber("Left Encoder", leftEncoderTicks());
		SmartDashboard.putNumber("Right Encoder", rightEncoderTicks());
	}
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
	
//	public void driveRotate(String direction){
//		if(direction.equalsIgnoreCase("left")){		
//			myRobot.setLeftRightMotorOutputs(.5, -.5);
//		} else if(direction.equalsIgnoreCase("right")){
//			myRobot.setLeftRightMotorOutputs(-.5, .5);
//		}
//	}
	
	
	
}
