package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.robot.Auton_Slot_1.SlotToTowerStates;

public class Auton_Slot_5 {
	Robot robot;
	
	public Auton_Slot_5(Robot robot) {
		this.robot = robot;
	}
	
	double WHEELDIAMETER  = 6;
	double WHEEL_CIRCUMFRANCE = 3.14*WHEELDIAMETER;
	
	double DEFENSE_TO_TURN = 156/WHEEL_CIRCUMFRANCE*100; //156 inches
	double TURN_TO_TOWER = 11/WHEEL_CIRCUMFRANCE*100; //60 degrees
	double DRIVE_TO_TOWER = 7.54/WHEEL_CIRCUMFRANCE*100; //7.54 inches
	
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
				robot.leftEncoder.reset();
				robot.rightEncoder.reset();
				slotToTowerStates = SlotToTowerStates.LEAVE_DEFENSE;
				break;
			case LEAVE_DEFENSE:
				robot.encoderSetPoint = DEFENSE_TO_TURN;
				robot.myRobot.setLeftRightMotorOutputs(-robot.SPD_LOWBAR, robot.SPD_LOWBAR);
				if(robot.leftEncoder.get() >= robot.encoderSetPoint) {
					robot.myRobot.stopMotor();
					robot.leftEncoder.reset();
					robot.rightEncoder.reset();
					slotToTowerStates = SlotToTowerStates.TURN;
				}
				break;
			case TURN:
				robot.encoderSetPoint = TURN_TO_TOWER;
				robot.myRobot.setLeftRightMotorOutputs(-robot.SPD_LOWBAR, -robot.SPD_LOWBAR); // turn left
				if(robot.leftEncoder.get() >= robot.encoderSetPoint) {
					robot.myRobot.stopMotor();
					robot.leftEncoder.reset();
					robot.rightEncoder.reset();
					slotToTowerStates = SlotToTowerStates.APPROACH_TOWER;
				}
				break;
			case APPROACH_TOWER:
				robot.encoderSetPoint = DRIVE_TO_TOWER;
				robot.myRobot.setLeftRightMotorOutputs(-robot.SPD_LOWBAR, robot.SPD_LOWBAR);
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
