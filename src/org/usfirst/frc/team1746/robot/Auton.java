package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	
	private Drivetrain m_drive;
	public Auton(Drivetrain drive) {
		m_drive = drive;
	}
	
	public enum States {
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
	
	States currentState;
	public void init(){
		currentState = States.INIT; 
		
	}
	
	public String getSelectedAuton(){
		return autonSelector.getSelected();
	}
	
	public void centerGearAuton(){
		switch(currentState){
		case INIT: 
			m_drive.resetEncoders();
			currentState = States.DRIVE_INIT;
		break;
		
		case DRIVE_INIT:
			
			currentState = States.DRIVE_TO_PEG;
		break;
		
		case DRIVE_TO_PEG: 
			
			currentState = States.DRIVE_HALT;
		break;
		
		case DRIVE_HALT: 
			
			currentState = States.WAIT_GEAR_REMOVAL;
		break;
		
		case WAIT_GEAR_REMOVAL: 
			
			currentState = States.DRIVE_FROM_PEG;
		break;
		
		case DRIVE_FROM_PEG: 
			
			currentState = States.DRIVE_ROTATE;
		break;
		
		case DRIVE_ROTATE: 
			
			currentState = States.DRIVE_TO_CENTER;
		break;
		
		case DRIVE_TO_CENTER:
			
			currentState = States.WAIT_TELEOP;
		break;
		case WAIT_TELEOP: 
			
		break;	
		
		}
	}
	
}
