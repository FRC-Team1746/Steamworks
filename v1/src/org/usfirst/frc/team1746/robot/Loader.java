package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Loader {
	ElectricalConstants electricalConstants = new ElectricalConstants();

	Victor loader;
	PowerDistributionPanel pdp;
	
	
	
	
	private Controls m_controls;

	public Loader(Controls controls){
		m_controls = controls;
	}

	public void init(){
		loader = new Victor(electricalConstants.MOTOR_LOADER);
		pdp = new PowerDistributionPanel();
	}
	
	
	int i = 0;
	int n = 0;
	boolean r = false;
	public void set(double VBus){
		SmartDashboard.putNumber("pdp", currentDraw());
		SmartDashboard.putBoolean("r", r);
		SmartDashboard.putNumber("i", i);
		SmartDashboard.putNumber("n", n);
		if(currentDraw() > 20 & !r){
			n++;
			if(n > 15){
				n = 0;
				r = true;
			}

		} 
			
		
		if(r){
			i++;
			loader.set(-.4);
			if(i > 10){
				i = 0;
				r = false;
			}
		} else {
			loader.set(VBus);
		}
		
	}
	
	public double currentDraw(){
		return pdp.getCurrent(6);
	}
	
	public void test(){
	}
	
	public void stop(){
		loader.stopMotor();
	}
	
	public void updateSmartDashboard(){
		SmartDashboard.putNumber("Indexer Current Draw", currentDraw());
	}
	
	public void checkControls(){
		if(m_controls.operator_loader()){
			set(1);
		} else if(m_controls.operator_loaderReverse()){
			set(-1);
		}	else {
			set(0);
		}
	}

}