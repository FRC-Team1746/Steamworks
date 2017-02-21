package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	AutonConstants aConstants = new AutonConstants();
	
	
	int loops = 0;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	public Auton(Drivetrain drive, GearIntake gear) {
		m_drive = drive;
		m_gear = gear;
	}
	
	
	public enum States {
		INIT,
		DRIVE_INIT,
		DRIVE_TO_PEG,
		DRIVE_HALT,
		WAIT_GEAR_REMOVAL,
		DRIVE_FROM_PEG,
		DRIVE_ROTATE,
		DRIVE_CROSS_LINE,
		DRIVE_ROTATE_2,
		DRIVE_CENTER,
		WAIT_TELEOP
	}
	
	
	

	public void initSmartDashboard() {
		autonSelector.addDefault("Default", "default");
		autonSelector.addObject("Left Gear", "leftGear");
		autonSelector.addObject("Center Gear", "centerGear");
		autonSelector.addObject("Right Gear", "rightGear");

		SmartDashboard.putData("Auton Selector", autonSelector);
		
		SmartDashboard.putBoolean("Reset Auton", false);
	}
	public void updateSmartDashboard(){
		SmartDashboard.putString("currentState", currentState.name());
		if(SmartDashboard.getBoolean("Reset Auton", false)){
			resetAuton();
			SmartDashboard.putBoolean("Reset Auton", false);
		}
	}
	States currentState;
	public void init(){
		m_drive.resetEncoders();
		currentState = States.INIT; 
		
	}
	
	public String getSelectedAuton(){
		return autonSelector.getSelected();
	}
	
	public void resetAuton(){
		currentState = States.INIT;
	}
	
	public void centerGearAuton(){
		switch(currentState){
		case INIT: 
			m_drive.resetEncoders();
			currentState = States.DRIVE_INIT;
		break;
		
		case DRIVE_INIT:
			m_drive.resetEncoders();
			currentState = States.DRIVE_TO_PEG;
		break;
		
		case DRIVE_TO_PEG: 
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.RCG_DIST_GEAR_PEG){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_HALT;
			}
		break;
		
		case DRIVE_HALT:
			
			currentState = States.WAIT_GEAR_REMOVAL;
		break;
		
		case WAIT_GEAR_REMOVAL: 
			if(m_gear.gearSensor()){
				loops++;
				if(loops > 50){
					m_drive.resetEncoders();
					currentState = States.DRIVE_FROM_PEG;
				}
			}
			
		break;
		
		case DRIVE_FROM_PEG: 
			m_drive.straight(.4);
			if(m_drive.avgEncoderTicks() < -aConstants.RCG_DIST_FROM_PEG){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_ROTATE;
			}
			
		break;
		
		case DRIVE_ROTATE: 
			m_drive.rotate("right");
			if(m_drive.gyroAngle() < -65){
				m_drive.resetEncoders();
				m_drive.stop();
				currentState = States.DRIVE_CROSS_LINE;
			}
			
		break;
		
		case DRIVE_CROSS_LINE:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.RCG_DIST_CROSS_LINE){
				m_drive.resetEncoders();
				m_drive.stop();
				currentState = States.DRIVE_ROTATE_2;
			}
			
		break;
		case DRIVE_ROTATE_2: 
			m_drive.rotate("left");
			if(m_drive.gyroAngle() > 0){
				m_drive.resetEncoders();
				m_drive.stop();
				currentState = States.DRIVE_CENTER;
			}
			
		break;
		
		case DRIVE_CENTER:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.RCG_DIST_CENTER){
				m_drive.stop();
				currentState = States.WAIT_TELEOP;
			}
			
		break;
		case WAIT_TELEOP: 
			
		break;	
		
		}
	}
	
}
