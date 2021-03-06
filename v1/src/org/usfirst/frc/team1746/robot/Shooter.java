package org.usfirst.frc.team1746.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1746.vision.VisionBoiler;
import org.usfirst.frc.team1746.vision.VisionTargeting;

public class Shooter {
	
	// RS 775 Pro : 18730 rpm
	// 3:1 Gearing
	// 6243 rpm
	
	
	
	CANTalon shooterMaster;
	CANTalon shooterSlave;
	
	private Controls m_controls;
	private VisionTargeting m_visionT;
	private VisionBoiler m_visionB;
	public Shooter(Controls controls, VisionTargeting visionT, VisionBoiler visionB){
		m_controls = controls;
		m_visionT = visionT;
		m_visionB = visionB;
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
		shooterMaster.setP(0.3); //.07
		shooterMaster.setI(0);
		shooterMaster.setD(10);		
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
	
	
	double a = -.001827753;
	double b = .7434676106;
	double c = -102.5691828;
	double d = 2030.89167;  
	double h = 0;
  
  	double speedFunction = 0;
	
	public void setSpeedFromCam(){
		h = m_visionB.getTopObject();
		speedFunction = (a * Math.pow(h, 3)) + (b * Math.pow(h, 2)) + (c * h) + (d);
		speed = (int) speedFunction;
	}
	
	
	
	
	int speed = -2745;
	int loops = 0;
	boolean speedUp;
	boolean speedDown;
	boolean speedSet;
	boolean pixy = false;
	public void checkControls(){
		SmartDashboard.putNumber("Shooter Desired Speed", speed);
		
		if(m_controls.operator_shooterRPMup()){
			if(!speedUp){
				speedUp = true;
				speed -=20;
			}

		} else {
			speedUp = false;
		}
		
		if(m_controls.operator_shooterRPMdown()){
			if(!speedDown){
				speedDown = true;
				speed += 10;
			}
		} else {
			speedDown = false;
		}
		
		if(m_controls.driver_SetSpeed()){
			if(!speedSet){
				speedSet = true;
				//speed = (int) m_visionT.getSpeed();
			} 
		} else {
			speedSet = false;
		}
		
		if(m_controls.operator_conveyor_shooter()){
			setRPM(speed);
		} else {
			stop();
		}
		if(m_controls.operator_pixyControlOn()){
			pixy = true;
		}
		if(m_controls.operator_pixyControlOff()){
			pixy = false;
		}
		if(pixy){
			setSpeedFromCam();
		}
		
	}
}
