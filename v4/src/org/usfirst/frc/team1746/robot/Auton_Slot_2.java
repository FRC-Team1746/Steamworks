package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.robot.Auton_Slot_1.SlotToTowerStates;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton_Slot_2 {
	Robot robot;
	
	public Auton_Slot_2(Robot robot) {
		this.robot = robot;
	}
	
	double WHEELDIAMETER  = 6;
	double WHEEL_CIRCUMFRANCE = 3.14*WHEELDIAMETER;
	
	double DEFENSE_TO_TURN = 141/WHEEL_CIRCUMFRANCE*100; //141 inches
	double TURN_TO_TOWER = 11/WHEEL_CIRCUMFRANCE*100; //60 degrees
	double DRIVE_TO_TOWER = 23/WHEEL_CIRCUMFRANCE*100; //23 inches

	
	public void init(){
		slotToTowerStates = SlotToTowerStates.INIT;
	}
	
	SlotToTowerStates slotToTowerStates ;
	
	public enum SlotToTowerStates {
		INIT,
		LEAVE_DEFENSE,
		TURN,
		APPROACH_TOWER,
	}
	public String getCurrentSlotName(){
		if(slotToTowerStates.name() != null){
			return slotToTowerStates.name();
		} else {
			return "NULL";
		}
	}
	
	public boolean stateMachine(){
		switch(slotToTowerStates) {
		case INIT:
			robot.myRobot.stopMotor();
			robot.leftEncoder.reset();
			robot.rightEncoder.reset();
			slotToTowerStates = SlotToTowerStates.LEAVE_DEFENSE;
			break;
		case LEAVE_DEFENSE:
			robot.drivePID(robot.SPD_A_CHEVAL_DE_FRISE, 0);
			if(robot.leftEncoder.get() > robot.DIST_A_CHEVAL_DE_FRISE_CROSS){
				slotToTowerStates = SlotToTowerStates.TURN;
			}
			break;
		case TURN:
			robot.driveRotate("right");
			if(robot.gyro.getAngle() >= 50){
				robot.myRobot.stopMotor();
				robot.leftEncoder.reset();
				robot.rightEncoder.reset();
				return true;
			}
			
			break;
			
	}//switch(slotToTowerStates) 
	return false;
		
	}
}
