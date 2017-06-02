package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Conveyor;
import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;
import org.usfirst.frc.team1746.robot.Loader;
import org.usfirst.frc.team1746.robot.Shooter;

import edu.wpi.first.wpilibj.Timer;

public class GearCenter {
	
	AutonConstants aConstants = new AutonConstants();

	int loops = 0;
	
	Timer timer;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	private Loader m_loader;
	private Conveyor m_conveyor;
	private Shooter m_shooter;
	public GearCenter(Drivetrain drive, GearIntake gear, Loader loader, Conveyor conveyor,Shooter shooter){
		m_drive = drive;
		m_gear = gear;
		m_loader = loader;
		m_conveyor = conveyor;
		m_shooter = shooter;
	}
	
	public enum States {
		INIT,
		DRIVE_INIT,
		DRIVE_BACKUP,
		ROTATE_PEG,
		DRIVE_TO_PEG,
		SHOOT,
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
		timer.reset();
		currentState = States.INIT;
		loops = 0;
	}
	public void init(){
		timer = new Timer();
		reset();
	}
	
	
	int shooterRPM = 0;
	public void auton(String alliance, boolean shoot, boolean wiggle){
		switch(currentState){
		case INIT: 
			reset();
			m_drive.resetEncoders();
			currentState = States.DRIVE_INIT; // change to shoot init
		break;
		case DRIVE_INIT:
			m_drive.resetEncoders();
			m_drive.resetSpeedPID();
			currentState = States.DRIVE_TO_PEG;
			
		break;
		case DRIVE_TO_PEG: 
			m_drive.towardsPeg(-.35);
			m_conveyor.set(0);
			if(shoot){
				if(alliance.equalsIgnoreCase("blue")){
					m_shooter.setRPM(aConstants.C_RPM_BLUE);
				} else {
					m_shooter.setRPM(aConstants.C_RPM_RED);
				}
			}
			if(m_drive.avgEncoderTicks() > aConstants.C_DIST_GEAR_PEG){
				timer.start();
				m_drive.resetEncoders();
				if(shoot){
					currentState = States.SHOOT;
					break;
				}
				currentState = States.WAIT_GEAR_REMOVAL;
			}
		break;
		case SHOOT:
			m_drive.straightPID(-.175);
			if(timer.get() >= 2){
				if(alliance.equalsIgnoreCase("blue")){
					m_shooter.setRPM(aConstants.C_RPM_BLUE);
				} else {
					m_shooter.setRPM(aConstants.C_RPM_RED);
				}
				m_conveyor.set(-.7);
				m_loader.set(-.3);
				if(timer.get() > 7){
					timer.stop();
					m_shooter.stop();
					m_conveyor.set(0);
					m_loader.stop();
					currentState = States.WAIT_GEAR_REMOVAL;
				}
			}
		break;
		case WAIT_GEAR_REMOVAL: 
			m_drive.straight(-.275);
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
