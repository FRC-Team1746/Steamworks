package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;

public class GearLeft {
	AutonConstants aConstants = new AutonConstants();
	
	int loops = 0;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	public GearLeft(Drivetrain drive, GearIntake gear) {
		m_drive = drive;
		m_gear = gear;
	}
	
	
	public enum States {
		INIT,
		DRIVE,
		DRIVE_ROTATE_RIGHT,
		DRIVE_TO_PEG,
		DRIVE_STALL,
		WAIT_GEAR_REMOVAL,
		DRIVE_FROM_PEG,
		ALLIGN,
		DRIVE_AWAY,
		ROTATE_GEAR_LOAD,
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
			currentState = States.DRIVE;
		break;		
		case DRIVE: 
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.L_DIST_DRIVE){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_ROTATE_RIGHT;
			}
		break;
		case DRIVE_ROTATE_RIGHT:
			m_drive.rotate("right");
			if(m_drive.gyroAngle() < -60){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_TO_PEG;
			}
		break;
		case DRIVE_TO_PEG:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.L_DIST_GEAR_PEG ){
				m_drive.resetEncoders();
				currentState = States.DRIVE_STALL;
			}
			
		break;
		case DRIVE_STALL:
			m_drive.straightPID(-.15);
			currentState = States.WAIT_GEAR_REMOVAL;
		break;
		case WAIT_GEAR_REMOVAL:
			if(m_gear.gearSensor()){
				loops++;
				if(loops > 100){
					loops = 0;
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.DRIVE_FROM_PEG;
				}
			}
		break;
		case DRIVE_FROM_PEG:
			m_drive.straightPID(.4);
			if(m_drive.avgEncoderTicks() < -aConstants.L_DIST_FROM_PEG){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.ALLIGN;
			}
		break;
		case ALLIGN:
			m_drive.rotate("left");
			if(m_drive.gyroAngle() > 0){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_AWAY;
			}
		break;
		case DRIVE_AWAY:
			// Red Alliance
			if(alliance.equals("red")){
				m_drive.straightPID(-.7);
				if(m_drive.avgEncoderTicks() > aConstants.L_DIST_CENTER){
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.WAIT_TELEOP;
				}
			}
			//Blue Alliance
			if(alliance.equals("blue")){
				m_drive.straightPID(-.5);
				if(m_drive.avgEncoderTicks() > aConstants.L_DIST_CENTER){
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.ROTATE_GEAR_LOAD;
				}
			}
		break;
		case ROTATE_GEAR_LOAD:
			m_drive.rotate("right");
			if(m_drive.gyroAngle() < -45){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_CENTER;
			}
		break;
		case DRIVE_CENTER:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.L_DIST_CENTER){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.WAIT_TELEOP;
			}
		break;
		case WAIT_TELEOP: 
						
		break;
		}
	}
}
