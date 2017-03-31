package org.usfirst.frc.team1746.robot.subsystems;

import org.usfirst.frc.team1746.robot.ElectricalConstants;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Victor;

public class Turret {
	ElectricalConstants eConstants = new ElectricalConstants();
	
	Victor turret;
	PowerDistributionPanel pdp;
	
	public void init(){
		pdp = new PowerDistributionPanel(eConstants.PDP_CAN_ID);
		turret = new Victor(eConstants.MOTOR_TURRET);
	}
	
	public void rotate(String direction, double speed){
		if(direction.equalsIgnoreCase("left")){
			turret.set(speed);
		} else if(direction.equalsIgnoreCase("right")){
			turret.set(-speed);
		} else {
			turret.set(0);
		}
	}
	
	public double currentDraw(){
		return pdp.getCurrent(eConstants.PDP_CHANNEL_12);
	}
	
}
