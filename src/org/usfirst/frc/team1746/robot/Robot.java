package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Vision vision;
	Controls controls;
	Drivetrain drive;
	Climber climber;
	GearIntake gear;
	Intake intake;
	Loader loader;
	Auton auton;
	AutonRRG autonRRG;
	AutonRLG autonRLG;
	
	@Override
	public void robotInit() {
		controls = new Controls();
		vision = new Vision();
		drive = new Drivetrain(controls);
		climber = new Climber(controls); 
		gear = new GearIntake(controls);
		intake = new Intake(controls);
		loader = new Loader(controls);
		auton = new Auton(drive, gear);
		autonRRG = new AutonRRG(drive, gear);
		autonRLG = new AutonRLG(drive, gear);
		
		controls.init();
		vision.init();
		drive.init();
		auton.init();
		
		climber.init();
		gear.init();
		loader.init();
		intake.init();
		
		
		initSmartDashboard();
	}

	@Override
	public void autonomousInit() {
		auton.init();
		autonRRG.init();
		autonRLG.init();
	}
	public void disabledPeriodic(){
		updateSmartDashboard();
	}

	@Override
	public void autonomousPeriodic() {
		updateSmartDashboard();
		autonRLG.auton();
	}

	@Override
	public void teleopPeriodic() {
		updateSmartDashboard();
		
		vision.printPixyStuff();
		
		drive.teleopArcadeDrive();
		
		climber.checkControls();
		
		intake.checkControls();
		
		loader.checkControls();
		
		gear.checkControls();
		
	}
		
	@Override
	public void testPeriodic() {
		
	} 
	
	public void initSmartDashboard(){
		auton.initSmartDashboard();
		drive.initSmartDashboard();
	}
	
	public void updateSmartDashboard(){
		drive.updateSmartDashboard();
		auton.updateSmartDashboard();
	}
}


	


