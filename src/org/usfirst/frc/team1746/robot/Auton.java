package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	DriveTrain drive = new DriveTrain();
	public enum autonStates {
		INIT,
		DRIVE_INIT,
		DRIVE_TO_PEG,
		DRIVE_HALT,
		WAIT_GEAR_REMOVAL,
		DRIVE_FROM_PEG,
		DRIVE_ROTATE,
		DRIVE_TO_CENTER,
		WAIT_TELEOP
	}
	
	
	public void initSmartDashboard() {
		autonSelector.addDefault("Default", "default");
		autonSelector.addObject("Left Gear", "leftGear");
		autonSelector.addObject("Center Gear", "centerGear");
		autonSelector.addObject("Right Gear", "rightGear");

		SmartDashboard.putData("Auton Selector", autonSelector);
	}
	autonStates autonState;
	public void init(){
		drive.init();
		autonState = autonStates.INIT; 
		
	}
	
	public String getSelectedAuton(){
		return autonSelector.getSelected();
	}
	
	
	public void centerGearAuton(){
		switch(autonState){
		case INIT: 
			drive.resetEncoders();
			autonState = autonStates.DRIVE_INIT;
		break;
		
		case DRIVE_INIT:
			drive.resetEncoders();
			autonState = autonStates.DRIVE_TO_PEG;
		break;
		
		case DRIVE_TO_PEG: 
			drive.drivePID(-.75);
			if(drive.leftEncoderTicks() >= 200){
				drive.resetEncoders();
				autonState = autonStates.DRIVE_HALT;
			}
		break;
		
		case DRIVE_HALT: 
			drive.drivePID(0);
			
			autonState = autonStates.WAIT_GEAR_REMOVAL;
		break;
		
		case WAIT_GEAR_REMOVAL: 
			//wait for sensor to no longer be broken
			//wait 2ish seconds
			autonState = autonStates.DRIVE_FROM_PEG;
		break;
		
		case DRIVE_FROM_PEG: 
			drive.drivePID(-.75);
			if(drive.leftEncoderTicks() >= 100){
				drive.resetEncoders();
				autonState = autonStates.DRIVE_ROTATE;
			}
			
		break;
			
		case DRIVE_ROTATE: 
			//rotate?
			autonState = autonStates.DRIVE_TO_CENTER;
		break;
		
		case DRIVE_TO_CENTER:
			drive.drivePID(-.75);
			if(drive.leftEncoderTicks() >= 500){
				drive.resetEncoders();
				autonState = autonStates.WAIT_TELEOP;
			}
		break;
		case WAIT_TELEOP: 
			//waiting waiting waiting
		break;	
		
		}
	}
	
}
