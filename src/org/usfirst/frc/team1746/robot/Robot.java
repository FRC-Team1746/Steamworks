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
	//RobotDrive myRobot;
	//Solenoid gear0;
	//Solenoid gear1;
	//Victor shooter;
	//Victor conveyor;
	//Victor loader;
	//Victor turret;
	//Victor scaling;
	//Talon carol;
	
	Servo pixyAZ;
	Servo pixyEl;
	int i = -1;
	int sumXError = 0;
	int sumYError = 0;
	int xAvgError = 0;
	int yAvgError = 0;
	
	//Encoder drive_leftEncoder;
	//Encoder drive_rightEncoder;
	//Encoder shooter_wheelEncoder;
	
	//Gyro gyro;
	

	
	double[] xErrors = new double[64];
	double[] yErrors = new double[64];
	
	
	
	
	
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
		//SmartDashboard.putNumber("speed", -1.0);
		SmartDashboard.putNumber("xBuffer", 0);
		SmartDashboard.putNumber("yBuffer", 0);
		pixyAZ = new Servo(0);
		pixyEl = new Servo(1);
		
		//loader = new Victor(5);
		//conveyor = new Victor(6);
		//shooter = new Victor(7);
		//turret = new Victor(8);
		//scaling = new Victor(9);
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
		if(i==63)i=0;
		else i++;
		vision.printPixyStuff();
		//myRobot.arcadeDrive(xbox);
		
		double AZ = (xbox.getRawAxis(0)+1)/2;
		double EL = (xbox.getRawAxis(1)+1)/2;
		SmartDashboard.putNumber("AZ", AZ);
		SmartDashboard.putNumber("Elevation", EL);
		//pixyAZ.set(AZ);
		//pixyEl.set(EL);
		
		//0-.5 left .5-1 right
		//0-.5 down .5-1 up
		
		double x = SmartDashboard.getNumber("xPosition", 150);
		double y = SmartDashboard.getNumber("yPosition", 100);
		double xError = x-150; //x+ move right //x- move left
		double yError = y-100; //y+ move up    //y- move down
		
		
		xErrors[i] = xError;
		yErrors[i] = yError;
		
		
		
		
		
		
		double motorXPos = pixyAZ.get();
		double motorYPos = pixyEl.get();
		
		double deltaXRate = .005;
		double deltaYRate = .004;
		
		
		
		double xBuffer = SmartDashboard.getNumber("xBuffer", 0);
		double yBuffer = SmartDashboard.getNumber("yBuffer", 0);
		
		if(xAvgError>xBuffer && x!=0)pixyAZ.set(motorXPos+deltaXRate);
		if(xAvgError<xBuffer && x!=0)pixyAZ.set(motorXPos-deltaXRate);
		if(yAvgError>yBuffer && y!=0)pixyEl.set(motorYPos+deltaYRate);
		if(yAvgError<yBuffer && y!=0)pixyEl.set(motorYPos-deltaYRate);
		
		SmartDashboard.putNumber("xError", xError);
		SmartDashboard.putNumber("yError", yError);
		
		
		
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
	
	
	public double getErrorAvg(String axis){
		int sum = 0;
		if(axis.equalsIgnoreCase("x")){
			for(double xError : xErrors){
				sum += xError;
			}
		}
		if(axis.equalsIgnoreCase("y")){
			for(double yError : yErrors){
				sum += yError;
			}
		}
		return sum/64;
		
		
	}
}


	


