package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controls {
	ElectricalConstants electricalConstants = new ElectricalConstants();
	
	Joystick xbox_driver;
	Joystick xbox_operator;
	
	public void init(){
		xbox_driver = new Joystick(electricalConstants.JOYSTICK_DRIVER);
		xbox_operator = new Joystick(electricalConstants.JOYSTICK_OPERATOR);
	}
	
/*********************************************************************************/	
/* 
 * Xbox Controller Layout
 * 
 * 	Buttons
 *    1   - A                   - driver intake - operator 
 *    2   - B                   - driver outake - operator
 *    3   - X                   - driver
 *    4   - Y                   - driver
 *    5   - Left Bumper         - driver flaps out
 *    6   - Right Bumper        - driver flaps in
 *    7   - Select              - driver tracking off
 *    8   - Start               - driver tracking on
 *    9   - Left Analog Button  - driver
 *    10  - Right Analog Button - driver
 *  
 *  Axis
 *    0   - Left Stick X        - driver
 *    1   - Left Stick Y        - driver
 *    2   - Left Trigger        - driver
 *    3   - Right Trigger       - driver
 *    4   - Right Stick X       - driver
 *    5   - Right Stick Y       - driver
 *  
 *  POV (DPAD)
 *    0   - DPAD Up             - driver
 *    45  - DPAD Up/Right       - driver
 *    90  - DPAD Right          - driver
 *    135 - DPAD Down/Right     - driver
 *    180 - DPAD Down           - driver
 *    225 - DPAD Down/Left      - driver
 *    270 - DPAD Left           - driver 
 *    315 - DPAD Up/Left        - driver
 *    
*/
/*********************************************************************************/
	
	
/*********************************************************************************/
// Driver
	// Intake
	public boolean driver_intakeBalls(){
		return xbox_driver.getRawButton(1);
	}
	public boolean driver_outtakeBalls(){
		return xbox_driver.getRawButton(2);
	}
	// Gear Flap Control
	public boolean driver_gearFlapsOut(){
		return xbox_driver.getRawButton(5);
	}
	public boolean driver_gearFlapsIn(){
		return xbox_driver.getRawButton(6);
	}
	// Toggle Pixy Turret Control
	public boolean driver_pixyControlOn(){
		return xbox_driver.getRawButton(8); 
	}
	public boolean driver_pixyControlOff(){
		return xbox_driver.getRawButton(7);
	}
	// Toggle Allign to Gear Peg
	public boolean driver_gearAllign(){
		return xbox_driver.getRawButton(8); 
	}
	// Set Target Speed To Pixy Calculation
	public boolean driver_SetSpeed(){
		return xbox_driver.getRawButton(9); 
	}
	// Left Axis
	public int driver_leftAxis(){
		return 1;
	}
	public int driver_rightAxis(){
		return 5;
	}
	public int driver_rightXAxis(){
		return 4;
	}
	// Rotate to Set Degree
	public boolean driver_allign(int degree){
		return xbox_driver.getPOV() == degree;
	}
	public boolean driver_climberUp(){
		return xbox_driver.getPOV() == 315 || xbox_driver.getPOV() == 0 || xbox_driver.getPOV() == 45 ;
	}
	public boolean driver_climberDown(){
		return xbox_driver.getPOV() == 135 || xbox_driver.getPOV() == 180 || xbox_driver.getPOV() == 225 ;
	}
/*********************************************************************************/
// Operator	
	// Hopper Flap Control
	public boolean operator_intakeBalls(){
		return xbox_operator.getRawButton(1);
	}
	public boolean operator_outtakeBalls(){
		return xbox_operator.getRawButton(2);
	}
	public boolean operator_gearHopperIn(){
		return xbox_operator.getRawButton(9);
	}
	public boolean operator_gearHopperOut(){
	return xbox_operator.getRawButton(10);
	}
	
	// Gear Flap Control
	public boolean operator_gearFlapsOut(){
		return xbox_operator.getRawButton(5);
	}
	public boolean operator_gearFlapsIn(){
		return xbox_operator.getRawButton(6);
	}
	//
	// Toggle Pixy Turret Control
	public boolean operator_pixyControlOn(){
		return xbox_operator.getRawButton(8);
	}
	public boolean operator_pixyControlOff(){
		return xbox_operator.getRawButton(7);
	}
	// Turret Rotation
	public double operator_turretRotation(){
		return xbox_operator.getRawAxis(0);
	}
	// Hood Position
	public double operator_hoodPosition(){
		return xbox_operator.getRawAxis(5);
	}
	// Loader Toggle
	public boolean operator_loader(){
		return xbox_operator.getRawAxis(3) > .05;
	}
	public boolean operator_loaderReverse(){
		return xbox_operator.getPOV() == 90;
	}
	// Conveyor Toggle
	public boolean operator_conveyor_shooter(){
		return xbox_operator.getRawAxis(2) > .05;
	}
	// climber Control
	public boolean operator_climberUp(){
		return xbox_operator.getPOV() == 315 || xbox_operator.getPOV() == 0 || xbox_operator.getPOV() == 45 ;
	}
	public boolean operator_climberSlow(){
		return xbox_operator.getPOV() == 270;
	}
	
	public boolean operator_climberDown(){
		return xbox_operator.getPOV() == 135 || xbox_operator.getPOV() == 180 || xbox_operator.getPOV() == 225 ;
	}
	public boolean operator_shooterRPMdown(){
		return xbox_operator.getRawButton(3);
	}
	public boolean operator_shooterRPMup(){
		return xbox_operator.getRawButton(4);
	}
	
/*********************************************************************************/

	public boolean testButton(){
		return xbox_driver.getRawButton(3);
	}
	
	public double testShooter(){
		return xbox_driver.getRawAxis(1);
	}
	
}