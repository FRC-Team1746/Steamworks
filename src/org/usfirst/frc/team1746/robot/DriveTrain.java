package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.vision.VisionBase;

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
	private VisionBase m_vision;
	public Drivetrain(Controls controls, VisionBase vision) {
		m_controls = controls;
		m_vision = vision;
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
		return -encoderRight.get();
	}
	public double avgEncoderTicks(){
		return (leftEncoderTicks() + rightEncoderTicks())/2;
	}
	public double encoderError(){
		return leftEncoderTicks() - rightEncoderTicks();
	}
	
	
	public void resetGyro(){
		gyro.reset();
	}
	public double gyroAngle(){
		return gyro.getAngle();
	}
	public void calibrateGyro(){
		gyro.calibrate();
	}
	public void initSmartDashboard(){
		SmartDashboard.putBoolean("Reset Encoders", false);
		SmartDashboard.putBoolean("Calibrate Gyro", false);
		
	}
	public void updateSmartDashboard(){
		if(SmartDashboard.getBoolean("Reset Encoders", false)){
			resetEncoders();
			SmartDashboard.putBoolean("Reset Encoders", false);
		}
		if(SmartDashboard.getBoolean("Calibrate Gyro", false)){
			calibrateGyro();
			SmartDashboard.putBoolean("Calibrate Gyro", false);
		}
		SmartDashboard.putNumber("Left Encoder", leftEncoderTicks());
		SmartDashboard.putNumber("Right Encoder", rightEncoderTicks());
		SmartDashboard.putNumber("Gyro Angle", gyroAngle());
	}
	public void straight(double speed){
		myRobot.setLeftRightMotorOutputs(speed, speed);
	}
	public void stop(){
		myRobot.stopMotor();
	}
	
	public void resetSpeedPID(){
		leftMotorSpeed = 0;
	}
	
	public void straightPID(double desiredLeftMotorSpeed){
		
		double P = .02;
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
		rightMotorSpeed = leftMotorSpeed - P*encoderError();
		myRobot.setLeftRightMotorOutputs(leftMotorSpeed, rightMotorSpeed);
	}
	
	public void rotate(String direction){
		if(direction.equalsIgnoreCase("left")){		
			myRobot.setLeftRightMotorOutputs(.30, -.30);
		} else if(direction.equalsIgnoreCase("right")){
			myRobot.setLeftRightMotorOutputs(-.30, .30);
		}
	}	
	
	double vision_P = .02/8;
	double vision_speedLeft;
	double vision_speedRight;
	double vision_error;
	
	public void towardsPeg(double speedDesired){		
		vision_error = m_vision.getError();
		
		vision_speedLeft = speedDesired  - (vision_P*vision_error)/2;
		vision_speedRight = speedDesired + (vision_P*vision_error)/2;
		
		
		myRobot.setLeftRightMotorOutputs(vision_speedLeft, vision_speedRight);
	}
	
}
