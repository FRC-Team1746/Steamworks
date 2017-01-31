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
	//Controls controls = new Controls();
	//DriveTrain drive = new DriveTrain();
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
		//0-.5 left .5-1 right
		//0-.5 down .5-1 up
		if(xbox.getRawButton(6)){
			
			System.out.println("xErrors: " + vision.xErrors.toString());
			System.out.println("yErrors: " + vision.yErrors.toString());
		}
		vision.printPixyStuff();
		
		
		if(xbox.getRawButton(1)){
			pixyAZ.set((xbox.getRawAxis(0)+1)/2);
			pixyEl.set((xbox.getRawAxis(1)+1)/2);
		}
		
		
		if(xbox.getRawButton(2))scaling.set(1);
		else if(xbox.getRawButton(3))scaling.set(-1);
		else scaling.set(0);
		
		
		double motorXPos = pixyAZ.get();
		double motorYPos = pixyEl.get();
		double deltaXRate = .004;
		double deltaYRate = .002;
		
		
		if(xbox.getRawButton(3)){
			if(vision.getAvgXError(8)>12)pixyAZ.set(motorXPos+deltaXRate);
			if(vision.getAvgXError(8)<-12)pixyAZ.set(motorXPos-deltaXRate);
			if(vision.getAvgYError(8)>0)pixyEl.set(motorYPos+deltaYRate);
			if(vision.getAvgYError(8)<-15)pixyEl.set(motorYPos-deltaYRate);
		}
		if(xbox.getRawButton(4)){
			pixyAZ.set(.5);
			pixyEl.set(.5);
		}
		
		//controls.teleopDrive();
		/*if(xbox.getRawButton(1)){
			gear0.set(true);
			gear1.set(false);
		}
		if(xbox.getRawButton(2)){
			gear0.set(false);
			gear1.set(true);
		}
		if(xbox.getRawButton(3)){
			gear0.set(false);
			gear1.set(false);
		}*/
	}
		
	@Override
	public void testPeriodic() {
		
	} 
}


	


