package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.robot.Auton_Slot_1.SlotToTowerStates;

public class Auton_Slot_5 {
	Robot robot;
	
	public Auton_Slot_5(Robot robot) {
		this.robot = robot;
	}
	
	double WHEELDIAMETER  = 6;
	double WHEEL_CIRCUMFRANCE = 3.14*WHEELDIAMETER;
	double ENCODER_GEAR_RATIO = 1.714;
	
	double DEFENSE_TO_TURN = 156/WHEEL_CIRCUMFRANCE*100; //156 inches
	double TURN_TO_TOWER = 11/WHEEL_CIRCUMFRANCE*100; //60 degrees
	double DRIVE_TO_TOWER = 30/WHEEL_CIRCUMFRANCE*100*ENCODER_GEAR_RATIO; //7.54 inches
	double SLOT_5_TO_TOWER = 75/WHEEL_CIRCUMFRANCE*100*ENCODER_GEAR_RATIO; //
	
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
			robot.encoderSetPoint = SLOT_5_TO_TOWER;
			robot.drivePID(.4, 0);
			if(robot.leftEncoder.get() > robot.encoderSetPoint){
				robot.drivePID(0, 0);
				robot.leftEncoder.reset();
				robot.rightEncoder.reset();
				slotToTowerStates = SlotToTowerStates.TURN;
			}
			break;
		case TURN:
			robot.driveRotate("left");
			if(robot.gyro.getAngle() <= -55){
				robot.myRobot.stopMotor();
				robot.leftEncoder.reset();
				robot.rightEncoder.reset();
				slotToTowerStates = SlotToTowerStates.APPROACH_TOWER;
			}
			break;
		case APPROACH_TOWER:
			robot.encoderSetPoint = DRIVE_TO_TOWER;
			robot.drivePID(.5, 0);
			if(robot.leftEncoder.get() >= DRIVE_TO_TOWER) {
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
