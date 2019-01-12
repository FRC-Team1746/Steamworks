package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Drivetrain;

public class Forward {
	
	Drivetrain m_drive;
	
	public Forward(Drivetrain drive){
		m_drive = drive;
	}
	
	public void init(){
		m_drive.resetEncoders();
	}
	
	public void auton(){
		if(m_drive.avgEncoderTicks() < 500){
			m_drive.straightPID(-.4);
		} else {
			m_drive.straight(.2);
		}
		
	}
	
	public void initSmartDashboard(){
		
	}
	
	public void updateSmartDashboard(){
		
	}
	
	
}
