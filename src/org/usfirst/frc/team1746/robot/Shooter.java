package org.usfirst.frc.team1746.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	
	// RS 775 Pro : 18730 rpm
	// 3:1 Gearing
	// 6243 rpm
	
	
	
	CANTalon shooterMaster;
	CANTalon shooterSlave;
	
	private Controls m_controls;
	
	public Shooter(Controls controls){
		m_controls = controls;
	}
		
	public void init(){
		shooterMaster = new CANTalon(9);
		shooterSlave = new CANTalon(10);
		
		shooterMaster.enableBrakeMode(false);
		shooterSlave.enableBrakeMode(false);
		
		shooterMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		
		shooterMaster.configNominalOutputVoltage(+0.0f, -0.0f);
		shooterSlave.configNominalOutputVoltage(+0.0f, -0.0f);
		
		shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
		shooterSlave.changeControlMode(TalonControlMode.Follower);
		
		shooterSlave.set(9);
		
		shooterMaster.setProfile(0);
		shooterMaster.setF(0.03);//.03
		shooterMaster.setP(0.07); //.07
		shooterMaster.setI(0);
		shooterMaster.setD(0);		
	}
	public void initSmartDashboard(){
		
	}
	
	public void updateSmartDashboard(){
		SmartDashboard.putNumber("Shooter: Speed", getSpeed());
		//SmartDashboard.putNumber("Shooter: EncPosition", getEncPosition());
		//SmartDashboard.putNumber("Shooter: EncVelocity", getEncVelocity());
		//SmartDashboard.putNumber("Shooter: TargetSpeed", targetSpeed);
	}
	
	double targetSpeed;
	
	public void stop(){
		shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
		shooterMaster.set(0);
	}
	
	
	public void setRPM(double rpm){
		targetSpeed = rpm;
		shooterMaster.changeControlMode(TalonControlMode.Speed);
		shooterMaster.set(rpm);
	}
	
	public void set(double voltage){
		
		shooterMaster.changeControlMode(TalonControlMode.PercentVbus);
		shooterMaster.set(voltage);
	}
	
	public double getSpeed(){
		return shooterMaster.getSpeed();
	}
	
	public double getEncPosition(){
		return shooterMaster.getEncPosition();
	}
	
	public double getEncVelocity(){
		return shooterMaster.getEncVelocity();
	}
	
	
	int speed = -3000;
	int loops = 0;
	boolean speedUp;
	boolean speedDown;
	public void checkControls(){
		SmartDashboard.putNumber("Shooter Desired Speed", speed);
		
		if(m_controls.operator_shooterRPMup()){
			if(!speedUp){
				speedUp = true;
				speed -=100;
			}

		} else {
			speedUp = false;
		}
		
		if(m_controls.operator_shooterRPMdown()){
			if(!speedDown){
				speedDown = true;
				speed += 100;
			}
		} else {
			speedDown = false;
		}
		
		if(m_controls.operator_conveyor_shooter()){
			setRPM(speed);
		} else {
			stop();
		}
		/*if(m_controls.operator_shooterRPMup()){
			loops++;
			if(loops > 20)	{
				speed -= 100;
				loops = 0;
			}
		}
		if(m_controls.operator_shooterRPMdown()){
			loops++;
			if(loops > 20)	{
				speed += 100;
				loops = 0;
			}
		} */
		
		
	}
}
