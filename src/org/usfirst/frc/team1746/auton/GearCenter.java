package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Conveyor;
import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;
import org.usfirst.frc.team1746.robot.Loader;
import org.usfirst.frc.team1746.robot.Shooter;

public class GearCenter {
	
	AutonConstants aConstants = new AutonConstants();

	int loops = 0;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	private Loader m_loader;
	private Shooter m_shooter;
	private Conveyor m_conveyor;
	public GearCenter(Drivetrain drive, GearIntake gear, Loader loader, Shooter shooter, Conveyor conveyor){
		m_drive = drive;
		m_gear = gear;
		m_loader = loader;
		m_shooter = shooter;
		m_conveyor = conveyor;
	}
	
	public enum States {
		INIT,
		SHOOT_INIT,
		SHOOT,
		DRIVE_INIT,
		DRIVE_BACKUP,
		ROTATE_PEG,
		DRIVE_TO_PEG,
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
	
	public void reset(){
		currentState = States.INIT;
		loops = 0;
	}
	public void init(){
		reset();
	}
	public void auton(String alliance, boolean shoot, boolean wiggle){
		switch(currentState){
		case INIT: 
			m_drive.resetEncoders();
			if(shoot){
				currentState = States.SHOOT_INIT;
				break;
			}
			currentState = States.DRIVE_INIT; // change to shoot init
		break;
		
		// Shoot then turn to peg
		case SHOOT_INIT:
			loops++;
			m_shooter.setRPM(aConstants.C_SHOOTER_RPM);
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
				currentState = States.ROTATE_PEG;
			}
		break;	
		case ROTATE_PEG:
			if(alliance.equalsIgnoreCase("red")){
				m_drive.rotate("left");
				if(m_drive.gyroAngle() > 82){
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.DRIVE_INIT;
				}
			} else {
				m_drive.rotate("right");
				if(m_drive.gyroAngle() < -82){
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.DRIVE_INIT;
				}
			}
			
		break;
		//Drive to gear peg
		case DRIVE_INIT:
			m_drive.resetEncoders();
			m_drive.resetSpeedPID();
			currentState = States.DRIVE_TO_PEG;
		break;		
		case DRIVE_TO_PEG: 
			m_drive.towardsPeg(-.375);
			if(m_drive.avgEncoderTicks() > aConstants.C_DIST_GEAR_PEG){
				m_drive.resetEncoders();
				currentState = States.WAIT_GEAR_REMOVAL;
			}
		break;	
		case WAIT_GEAR_REMOVAL: 
			m_drive.straight(-.25);
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
