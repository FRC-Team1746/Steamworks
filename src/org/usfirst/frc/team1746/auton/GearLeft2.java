package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.auton.GearRight.States;
import org.usfirst.frc.team1746.robot.Conveyor;
import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;
import org.usfirst.frc.team1746.robot.Loader;
import org.usfirst.frc.team1746.robot.Shooter;

public class GearLeft2 {
	AutonConstants aConstants = new AutonConstants();
	
	int loops = 0;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	private Loader m_loader;
	private Conveyor m_conveyor;
	private Shooter m_shooter;
	
	public GearLeft2(Drivetrain drive, GearIntake gear, Loader loader, Conveyor conveyor, Shooter shooter) {
		m_drive = drive;
		m_gear = gear;
		m_loader = loader;
		m_conveyor = conveyor;
		m_shooter = shooter;
	}

	public enum States {
		INIT,
		SHOOT_INIT,
		SHOOT,
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
	
	public void reset(){
		currentState = States.INIT;
		m_drive.resetSpeedPID();
		m_drive.resetEncoders();
		loops = 0;
	}
	public void init(){
		reset();
	}
	public void auton(String alliance, boolean shoot){
		switch(currentState){
		case INIT: 
			m_drive.resetEncoders();
			if(shoot){
				currentState = States.SHOOT_INIT;
				break;
			}
			currentState = States.DRIVE;
		break;						
		case SHOOT_INIT:
			loops++;
			m_conveyor.set(-.65);
			m_shooter.setRPM(-3100);
			if(loops > 75){
				loops = 0;
				currentState = States.SHOOT;
			}
		break;
		case SHOOT:
			loops++;
			m_loader.set(-.5);
			if(loops > 250){
				m_loader.set(0);
				m_conveyor.set(0);
				m_shooter.stop();
				currentState = States.DRIVE;
			}
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
			if(m_drive.gyroAngle() < -63){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_TO_PEG;
			}
		break;
		case DRIVE_TO_PEG:
			m_drive.towardsPeg(-.3);
			if(m_drive.avgEncoderTicks() > aConstants.L_DIST_GEAR_PEG ){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.WAIT_GEAR_REMOVAL;
			}
		break;
		case WAIT_GEAR_REMOVAL:
			m_drive.straight(-.275);
			if(m_gear.gearSensor()){
				loops++;
				if(loops > 150){
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
				m_drive.resetSpeedPID();
				currentState = States.DRIVE_AWAY;
			}
		break;
		case DRIVE_AWAY:
			// Red Alliance
			if(alliance.equals("red")){
				m_drive.straightPID(-.7);
				if(m_drive.avgEncoderTicks() > aConstants.L_DIST_AWAY){
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.WAIT_TELEOP;
				}
			}
			//Blue Alliance
			if(alliance.equals("blue")){
				m_drive.straightPID(-.5);
				if(m_drive.avgEncoderTicks() > aConstants.L_DIST_AWAY){
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
