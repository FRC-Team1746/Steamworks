package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	
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
		autonState = autonStates.INIT; 
		
	}
	
	public String getSelectedAuton(){
		return autonSelector.getSelected();
	}
	
	
	public void centerGearAuton(){
		switch(autonState){
		case INIT: 
			
			autonState = autonStates.DRIVE_INIT;
		break;
		
		case DRIVE_INIT:
			
			autonState = autonStates.DRIVE_TO_PEG;
		break;
		
		case DRIVE_TO_PEG: 
			
			autonState = autonStates.DRIVE_HALT;
		break;
		
		case DRIVE_HALT: 
			
			autonState = autonStates.WAIT_GEAR_REMOVAL;
		break;
		
		case WAIT_GEAR_REMOVAL: 
			
			autonState = autonStates.DRIVE_FROM_PEG;
		break;
		
		case DRIVE_FROM_PEG: 
			
			autonState = autonStates.DRIVE_ROTATE;
		break;
		
		case DRIVE_ROTATE: 
			
			autonState = autonStates.DRIVE_TO_CENTER;
		break;
		
		case DRIVE_TO_CENTER:
			
			autonState = autonStates.WAIT_TELEOP;
		break;
		case WAIT_TELEOP: 
			
		break;	
		
		}
	}
	
}
