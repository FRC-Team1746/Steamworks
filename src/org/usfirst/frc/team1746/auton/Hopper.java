package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Conveyor;
import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;
import org.usfirst.frc.team1746.robot.Loader;
import org.usfirst.frc.team1746.robot.Shooter;
import org.usfirst.frc.team1746.robot.subsystems.Turret;

public class Hopper {

	AutonConstants aConstants = new AutonConstants();
	
	Drivetrain m_drive;
	GearIntake m_gear;
	Shooter m_shooter;
	Conveyor m_conveyor;
	Loader m_loader;
	Turret m_turret;
	
	public Hopper(Drivetrain drive, GearIntake gear, Shooter shooter, Conveyor conveyor, Loader loader, Turret turret){
		m_drive = drive;
		m_gear = gear;
		m_shooter = shooter;
		m_conveyor = conveyor;
		m_loader = loader;
		m_turret = turret;
	}
	
	public void reset(){
		init();
	}
	
	public enum States {
		INIT,
		DRIVE_A,
		CURVE,
		DRIVE_B,
		TRACK,
		SHOOT,
		WAIT_TELEOP
	}
	
	States currentState;
	
	public String currentState(){
		return currentState.name();
	}
	
	public void init(){
		currentState = States.INIT;
	}
	
	int i=0;
	
	public void auton(String alliance){
		switch(currentState){
		
		case INIT:
			reset();
			m_drive.resetEncoders();
			m_gear.flapsOut();
			m_shooter.stop();
			m_conveyor.set(0);
			m_loader.stop();
			currentState = States.DRIVE_A;
		break;
		case DRIVE_A:
			m_drive.straightPID(-.7);
			m_gear.hopperFlapsIn();
			
			if(alliance.equalsIgnoreCase("blue")){
				if(m_drive.avgEncoderTicks() > aConstants.H_DIST_BLUE){
					m_drive.resetEncoders();
					currentState = States.CURVE;
				}
			}
			if(alliance.equalsIgnoreCase("red")){
				if(m_drive.avgEncoderTicks() > aConstants.H_DIST_RED){
					
					m_drive.resetEncoders();
					currentState = States.CURVE;
				}
			}
			
		break;
		case CURVE:
			m_gear.flapsIn();
			if(alliance.equalsIgnoreCase("blue")) {
				m_drive.set(.1, -.6);
				m_shooter.setRPM(aConstants.H_RPM_BLUE);
			}
			if(alliance.equalsIgnoreCase("red"))  {
				m_drive.set(-.6, .1);
				m_shooter.setRPM(aConstants.H_RPM_RED);
			}
			
			
			
			if(m_drive.gyroAngle() > 72 || m_drive.gyroAngle() < -75){
				m_drive.resetEncoders();
				currentState = States.DRIVE_B;
			}
		break;
		case DRIVE_B:
			m_gear.hopperFlapsOut();
			m_drive.straightPID(-.85);
			
			if(alliance.equalsIgnoreCase("blue")) m_shooter.setRPM(aConstants.H_RPM_BLUE);
			if(alliance.equalsIgnoreCase("red"))  m_shooter.setRPM(aConstants.H_RPM_RED);
			
			
			
			if(m_drive.avgEncoderTicks() > 15){
				m_drive.resetEncoders();
				i=0;
				currentState = States.TRACK;
			}
		break;
		case TRACK:
			i++;
			
			if(alliance.equalsIgnoreCase("blue")) m_shooter.setRPM(aConstants.H_RPM_BLUE);
			if(alliance.equalsIgnoreCase("red"))  m_shooter.setRPM(aConstants.H_RPM_RED);
			if(i>50){
				m_drive.straight(-.25);
				//m_turret.track();
				if(i>100){
					currentState = States.SHOOT;
				}
			}
			
		break;
		case SHOOT:
			m_drive.straight(-.25);
			if(alliance.equalsIgnoreCase("blue")) m_shooter.setRPM(aConstants.H_RPM_BLUE);
			if(alliance.equalsIgnoreCase("red"))  m_shooter.setRPM(aConstants.H_RPM_RED);
			m_conveyor.set(-1);
			m_loader.set(1);
		
		break;
		case WAIT_TELEOP:
			
		break;
		
		}
	}
	
	public void initSmartDashboard(){
		
	}
	
	public void updateSmartDashboard(){
		
	}
	
}
