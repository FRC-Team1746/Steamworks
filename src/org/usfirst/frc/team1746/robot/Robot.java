package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Gyro;


public class Robot extends IterativeRobot {
	
	Auton auton = new Auton();
	Intake intake = new Intake();
	//Vision vision = new Vision();
	//Controls controls = new Controls();
	//DriveTrain drive = new DriveTrain();
	Joystick xbox;
	
	//RobotDrive myRobot;
	
	//Victor shooter;
	//Victor conveyor;
	//Victor loader;
	//Victor turret;
	//Victor scaling;
	
	//Servo gear;
	//Servo hood;
	
	//Encoder drive_leftEncoder;
	//Encoder drive_rightEncoder;
	//Encoder shooter_wheelEncoder;
	
	//Gyro gyro;
	

	@Override
	public void robotInit() {		
		
		auton.initSmartDashboard();
		intake.init();
		
		xbox = new Joystick(0);
		
		//vision.init();
		//controls.init();
		
		//gear = new Servo(10);
		//hood = new Servo(3);
		//loader = new Victor(5);
		//conveyor = new Victor(6);
		//shooter = new Victor(7);
		//turret = new Victor(8);
		//scaling = new Victor(9);
	
	}

	@Override
	public void autonomousInit() {
		
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopPeriodic() {
		//vision.printPixyStuff();
		//controls.teleopDrive();
		if(xbox.getRawButton(1)){
			intake.set(1);
		}
	}
		
	@Override
	public void testPeriodic() {
		
	}

}



