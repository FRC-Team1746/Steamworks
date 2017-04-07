package org.usfirst.frc.team1746.robot.subsystems;

import org.usfirst.frc.team1746.robot.Controls;
import org.usfirst.frc.team1746.robot.ElectricalConstants;
import org.usfirst.frc.team1746.vision.VisionBoiler;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret {
	ElectricalConstants eConstants = new ElectricalConstants();
	
	Victor turret;
	DigitalInput limitSensor;
	PowerDistributionPanel pdp;
	
	VisionBoiler m_vision_boiler;
	Controls m_controls;
	
	public Turret(VisionBoiler vision_boiler, Controls controls){
		m_vision_boiler = vision_boiler;
		m_controls = controls;
	}
	
	
	
	public void init(){
		pdp = new PowerDistributionPanel(eConstants.PDP_CAN_ID);
		turret = new Victor(eConstants.MOTOR_TURRET);
		limitSensor = new DigitalInput(eConstants.TURRET_LIMIT_SENSOR);
	}
	
	public double currentDraw(){
		return pdp.getCurrent(eConstants.PDP_CHANNEL_12);
	}
	
	int error = 0;
	int allowance = 5;
	double p = 0.1;
	
	double speed = .2;
	double prevSpeed = 0;
	public void track(){
		error = m_vision_boiler.getError()-20;
		/*if(error > allowance || error < -allowance){
			if(p*error > 1) speed = 1;
			else if(p*error < -1) speed = -1;
			else speed = -p*error;
			turret.set(speed);
			prevSpeed = speed;
		*/	
		
		speed = p*Math.log10(Math.abs(error));
		if(error > allowance){
			turret.set(speed);
		} else if(error < -allowance){
			turret.set(-speed);
		}else {
			turret.set(0);
		}
		
		
	}
	
	public void set(int value){
		turret.set(value);
		prevSpeed = speed;
	}
	
	public void updateSmartDashboard(){
		SmartDashboard.putBoolean("Turret Limit", limitSensor.get());
	}
	
	public void checkControls(){
		if(m_controls.operator_turretRotation() > .25){
			turret.set(.15);
		} else if(m_controls.operator_turretRotation() < -.25){
			turret.set(-.15);
		} else {
			track();
		}
	}
	
	
	boolean toggleTracking = false;
	public void teleop(){
		if(m_controls.operator_pixyControlOn()) toggleTracking = true;
		if(m_controls.operator_pixyControlOff()) toggleTracking = false;
		
		if(toggleTracking){ 
			track();
		} else {
			checkControls();
		}
	}
}
