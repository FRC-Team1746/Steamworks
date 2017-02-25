package org.usfirst.frc.team1746.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	CANTalon shooter;
	
	private Controls m_controls;
	
	public Shooter(Controls controls){
		m_controls = controls;
	}
		
	public void init(){
		shooter = new CANTalon(9);
		
		shooter.enableBrakeMode(false);
		shooter.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		shooter.setProfile(0);
		shooter.setF(0.1);
		shooter.setP(0);
		shooter.setI(0);
		shooter.setD(0);		
	}
	public void initSmartDashboard(){
		
	}
	
	public void updateSmartDashboard(){
		SmartDashboard.putNumber("Shooter Speed", getSpeed());
	}
	
	public void stop(){
		shooter.changeControlMode(TalonControlMode.PercentVbus);
		shooter.set(0);
	}
	
	
	public void setRPM(double rpm){
		shooter.changeControlMode(TalonControlMode.Speed);
		shooter.set(rpm);
	}
	
	public double getSpeed(){
		return shooter.getSpeed();
	}
	
	public void checkControls(){
		if(m_controls.operator_conveyor_shooter()){
			setRPM(6243);
		} else {
			stop();
		}
		
		
	}
}
