package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.auton.AutonBase;
import org.usfirst.frc.team1746.vision.VisionBase;
import org.usfirst.frc.team1746.vision.VisionTargeting;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	VisionBase vision;
	//VisionTargeting trackingLEDs;
	Controls controls;
	Drivetrain drive;
	Climber climber;
	GearIntake gear;
	Intake intake;
	Loader loader;
	Conveyor conveyor;
	Shooter shooter;
	
	AutonBase auton;
	
	
	DigitalOutput test;
	@Override
	public void robotInit() {
		controls = new Controls();
		vision = new VisionBase();
		drive = new Drivetrain(controls, vision);
		climber = new Climber(controls); 
		gear = new GearIntake(controls);
		//trackingLEDs = new VisionTargeting(vision, gear);
		intake = new Intake(controls);
		loader = new Loader(controls);
		shooter = new Shooter(controls);
		conveyor = new Conveyor(controls);
		auton = new AutonBase(drive, gear, loader, shooter);
		
		test = new DigitalOutput(11);
		shooter.init();
		controls.init();
		vision.init();
		//trackingLEDs.init();
		drive.init();
		auton.init();
		controls.init();
		climber.init();
		gear.init();
		loader.init();
		intake.init();
		conveyor.init();
		
		initSmartDashboard();
	}

	@Override
	public void autonomousInit() {
		//auton.init()'
	}
	public void disabledPeriodic(){
		vision.trackObject();
		//trackingLEDs.run();
		updateSmartDashboard();
		//test.set(true);
		
	}

	@Override
	public void autonomousPeriodic() {
		auton.run();
		vision.trackObject();
		//trackingLEDs.run();
		updateSmartDashboard();
	}

	@Override
	public void teleopPeriodic() {
		updateSmartDashboard();
		
		vision.trackObject();
		//trackingLEDs.run();
		
		drive.teleopArcadeDrive();
		
		climber.checkControls();
		
		intake.checkControls();
		
		loader.checkControls();
		
		gear.smartFlaps();;
		
		
		conveyor.checkControls();
		shooter.checkControls();
		
		if(controls.testButton()){
			drive.towardsPeg(-.35);;
		}
		
	}
		
	@Override
	public void testPeriodic() {
		if(controls.operator_loader()){
			
		}
	} 
	
	public void initSmartDashboard(){
		auton.initSmartDashboard();
		drive.initSmartDashboard();
		shooter.initSmartDashboard();
	}
	
	public void updateSmartDashboard(){
		drive.updateSmartDashboard();
		auton.updateSmartDashboard();
		gear.updateSmartDashboard();
		shooter.updateSmartDashboard();
		vision.updateSmartdashboard();
		//trackingLEDs.updateSmartDashboard();
	}
}


	


