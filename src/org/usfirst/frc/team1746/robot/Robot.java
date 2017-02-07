package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	
	//Auton auton = new Auton();
	//Intake intake = new Intake();
	Vision vision = new Vision();
	//Preferences prefs;
	Controls controls = new Controls();
	DriveTrain drive = new DriveTrain();
	Joystick xbox;
	RobotDrive myRobot;
	//Solenoid gear0;
	//Solenoid gear1;
	//Victor shooter;
	//Victor conveyor;
	//Victor loader;
	//Victor turret;
	Victor scaling;
	//Talon carol;
	
	Servo pixyAZ;
	Servo pixyEl;
	
	//Encoder drive_leftEncoder;
	//Encoder drive_rightEncoder;
	//Encoder shooter_wheelEncoder;
	
	//Gyro gyro;

	@Override
	public void robotInit() {		
		
		//auton.initSmartDashboard();
		//intake.init();
		//myRobot = new RobotDrive(0,1,2,3);
		xbox = new Joystick(0);
		
		//gear0 = new Solenoid(0);
		//gear1 = new Solenoid(1);
		
		//carol = new Talon(9);
		vision.init();
		//controls.init();
		pixyAZ = new Servo(9);
		pixyEl = new Servo(8);
		
		//loader = new Victor(5);
		//conveyor = new Victor(6);
		//shooter = new Victor(7);
		//turret = new Victor(8);
		scaling = new Victor(4);
	}

	@Override
	public void autonomousInit() {
		
	}
	public void disabledPeriodic(){
		
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopPeriodic() {
		drive.teleopDrive();
		vision.printPixyStuff();
	}
		
	@Override
	public void testPeriodic() {
		
	} 
}


	


