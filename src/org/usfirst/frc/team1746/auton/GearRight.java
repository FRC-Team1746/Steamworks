package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Conveyor;
import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;
import org.usfirst.frc.team1746.robot.Loader;
import org.usfirst.frc.team1746.robot.Shooter;
import org.usfirst.frc.team1746.vision.VisionBase;

public class GearRight {
	AutonConstants aConstants = new AutonConstants();
	
	int loops = 0;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	private Shooter m_shooter;
	private Loader m_loader;
	private Conveyor m_conveyor;
	private VisionBase m_vision;
	public GearRight(Drivetrain drive, GearIntake gear, VisionBase vision) {
		m_drive = drive;
		m_gear = gear;
		m_vision = vision;
	}
	
	public enum States {
		INIT,
		START,
		CURVE_LEFT,
		LOW_GOAL_DUMP,
		ROTATE_RIGHT,
		DRIVE_AFTER_DUMP,
		SHOOT_INIT,
		SHOOT,
		ROTATE_STRAIGHT,
		DRIVE,
		DRIVE_ROTATE_LEFT,
		DRIVE_TO_PEG,
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
	
	public void reset(){
		currentState = States.INIT;
		m_drive.resetEncoders();
		m_drive.resetSpeedPID();
		loops = 0;
	}
	public void init(){
		reset();
	}

	public void auton(String alliance, Boolean shoot){
		switch(currentState){
		case INIT: 
			m_drive.resetEncoders();
			currentState = States.DRIVE;
		break;
		
		// Shoot then turn back to straight
		case LOW_GOAL_DUMP:
			m_gear.hopperFlapsIn();
			currentState = States.ROTATE_RIGHT;
		break;
		
		case ROTATE_RIGHT:
			m_drive.rotate("right");
			if(m_drive.gyroAngle() < -40){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_AFTER_DUMP;
			}
		break;
		
		case DRIVE_AFTER_DUMP:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.R_DIST_DRIVE-50);
		break;
		case SHOOT_INIT:
			loops++;
			m_shooter.setRPM(aConstants.R_SHOOTER_RPM);
			if(loops > 40){
				loops = 0;
				currentState = States.SHOOT;
			}
		break;
		case SHOOT:
			loops++;
			m_loader.set(1);
			m_conveyor.sets(1);
			if(loops > 280){
				loops = 0;
				m_loader.stop();
				m_shooter.stop();
				currentState = States.ROTATE_STRAIGHT;
			}
		break;		
		case ROTATE_STRAIGHT:
			
		//
		case DRIVE: 
			m_drive.straight(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.R_DIST_DRIVE-160){
				m_drive.resetEncoders();
				m_drive.stop();
				currentState = States.DRIVE_ROTATE_LEFT;
			}
		break;
		case DRIVE_ROTATE_LEFT:
			m_drive.set(.1, -.4);
			if(m_drive.gyroAngle() > 45){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_TO_PEG;
			}
		break;
		case DRIVE_TO_PEG:
			m_drive.towardsPeg(-.35);;
			m_gear.LEDsOn();
			if(m_drive.avgEncoderTicks() > aConstants.R_DIST_GEAR_PEG ){
				m_gear.LEDsOff();
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.WAIT_GEAR_REMOVAL;
			}

		break;
		case WAIT_GEAR_REMOVAL:
			m_drive.straight(-.275);;
			if(m_gear.gearSensor()){
				loops++;
				if(loops > 150){
					loops = 0;
					m_drive.resetEncoders();
					currentState = States.DRIVE_FROM_PEG;
				}
			}
		break;
		case DRIVE_FROM_PEG:
			m_drive.straightPID(.4);
			if(m_drive.avgEncoderTicks() < -aConstants.R_DIST_FROM_PEG){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.ALLIGN;
			}
		break;
		case ALLIGN:
			m_drive.rotate("right");
			if(m_drive.gyroAngle() < 0){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_AWAY;
			}
		break;
		case DRIVE_AWAY:
			// Red Alliance
			if(alliance.equals("red")){
				m_drive.straightPID(-.5);
				if(m_drive.avgEncoderTicks() > aConstants.R_DIST_CENTER){
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.ROTATE_GEAR_LOAD;
				}
			}
			//Blue Alliance
			if(alliance.equals("blue")){
				m_drive.straightPID(-.7);
				if(m_drive.avgEncoderTicks() > aConstants.R_DIST_CENTER){
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.WAIT_TELEOP;
				}
			}
		break;
		case ROTATE_GEAR_LOAD:
			m_drive.rotate("left");
			if(m_drive.gyroAngle() > 45){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_CENTER;
			}
		break;
		case DRIVE_CENTER:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.R_DIST_CENTER){
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