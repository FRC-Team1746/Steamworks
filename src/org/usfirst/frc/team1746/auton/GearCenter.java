package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;

public class GearCenter {
	
	AutonConstants aConstants = new AutonConstants();

	int loops = 0;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	public GearCenter(Drivetrain drive, GearIntake gear){
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
	
	States currentState;
	
	public String currentState(){
		return currentState.name();
	}
	
	public void resetState(){
		currentState = States.INIT;
	}
	public void init(){
		resetState();
	}
	public void auton(String alliance){
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
			if(m_drive.avgEncoderTicks() > aConstants.C_DIST_GEAR_PEG){
				m_drive.resetEncoders();
				currentState = States.DRIVE_HALT;
			}
		break;
		case DRIVE_HALT:
			m_drive.straightPID(-.15);
			currentState = States.WAIT_GEAR_REMOVAL;
		break;
		
		case WAIT_GEAR_REMOVAL: 
			if(m_gear.gearSensor()){
				loops++;
				if(loops > 100){
					loops = 0;
					m_drive.resetEncoders();
					currentState = States.DRIVE_FROM_PEG;
				}
			}
			
		break;
		case DRIVE_FROM_PEG: 
			m_drive.straight(.4);
			if(m_drive.avgEncoderTicks() < -aConstants.C_DIST_FROM_PEG){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_ROTATE;
			}
			
		break;
		case DRIVE_ROTATE: 
			if(alliance.equals("blue")){
				m_drive.rotate("right");
				if(m_drive.gyroAngle() < -65){
					m_drive.resetEncoders();
					m_drive.stop();
					currentState = States.DRIVE_CROSS_LINE;
				}
			}
			
			if(alliance.equals("red")){
				m_drive.rotate("left");
				if(m_drive.gyroAngle() > 65){
					m_drive.resetEncoders();
					m_drive.stop();
					currentState = States.DRIVE_CROSS_LINE;
				}
			}
			
			
		break;
		case DRIVE_CROSS_LINE:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.C_DIST_CROSS_LINE){
				m_drive.resetEncoders();
				m_drive.stop();
				currentState = States.DRIVE_ROTATE_2;
			}
			
		break;
		case DRIVE_ROTATE_2: 
			//Blue Alliance
			if(alliance.equals("blue")){
				m_drive.rotate("left");
				if(m_drive.gyroAngle() > 0){
					m_drive.resetEncoders();
					m_drive.stop();
					currentState = States.DRIVE_CENTER;
				}
			}
			//Red Alliance
			if(alliance.equals("red")){
				m_drive.rotate("right");
				if(m_drive.gyroAngle() < 0){
					m_drive.resetEncoders();
					m_drive.stop();
					currentState = States.DRIVE_CENTER;
				}
			}
		break;
		case DRIVE_CENTER:
			m_drive.straightPID(-.7);
			if(m_drive.avgEncoderTicks() > aConstants.C_DIST_CENTER){
				m_drive.stop();
				currentState = States.WAIT_TELEOP;
			}
			
		break;
		case WAIT_TELEOP: 
		break;	
		}
	}
}
