package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.robot.Auton_Slot_5.SlotToTowerStates;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton_Slot_1 {
	Robot robot;
	
	public Auton_Slot_1(Robot robot) {
		this.robot = robot;
	}
	
	double WHEELDIAMETER  = 6;
	double ENCODER_GEAR_RATIO = 1.714;
	double WHEEL_CIRCUMFRENCE = 3.14*WHEELDIAMETER;
	
	double DEFENSE_TO_TURN = 170/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO; //137 inches
	double TURN_TO_TOWER = 11/WHEEL_CIRCUMFRENCE*100; //60 degrees
	double DRIVE_TO_TOWER = 240/WHEEL_CIRCUMFRENCE*100; //100 inches
	
	double TURN_RADIUS = 240;
	
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
			slotToTowerStates = SlotToTowerStates.TURN;
			break;
		case LEAVE_DEFENSE:
			
			break;
		case TURN:
			robot.driveRotate("right");
			if(robot.gyro.getAngle() >= 55){
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
