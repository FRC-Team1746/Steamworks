package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	///Auton auton = new Auton();
	///Intake intake = new Intake();
	/// Vision vision = new Vision();
	ElectricalConstants eConstants;
	Controls controls;
	DriveTrain drive;
	Climber climber;
	GearIntake gear;
	Intake intake;

	@Override
	public void robotInit() {
		controls = new Controls();
		eConstants = new ElectricalConstants();
		drive = new DriveTrain(controls);
		climber = new Climber(controls); 
		gear = new GearIntake(controls);
		intake = new Intake(controls);
		drive.init();
		controls.init();
		climber.init();
		gear.init();
		
		
		//auton.initSmartDashboard();
		//intake.init();
		//vision.init();
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
		climber.update();
		gear.update();
		///vision.printPixyStuff();
	}
		
	@Override
	public void testPeriodic() {
		
	} 

}


	


