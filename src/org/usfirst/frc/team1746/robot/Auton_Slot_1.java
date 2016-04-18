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

	double DRIVE_TO_TOWER = 245/WHEEL_CIRCUMFRENCE*100; //100 inches
	double DRIVE_REALLIGN = 5/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double TURN_DEGREE = 50;
	
	int loopCounter;
	public void init(){
		loopCounter = 0;
		slotToTowerStates = SlotToTowerStates.INIT;
		
	}
	
	SlotToTowerStates slotToTowerStates ;
	
	public enum SlotToTowerStates {
		INIT,
		LEAVE_DEFENSE,
		TURN,
		RE_ALLIGN,
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
			loopCounter++;
			robot.driveRotate("right");
			if(robot.gyro.getAngle() >= TURN_DEGREE){
				robot.myRobot.stopMotor();
				robot.leftEncoder.reset();
				robot.rightEncoder.reset();
				slotToTowerStates = SlotToTowerStates.APPROACH_TOWER;
			}
			if(loopCounter >= 100){
				loopCounter = 0;
				slotToTowerStates = SlotToTowerStates.RE_ALLIGN;
			}
			break;
		case RE_ALLIGN:
			robot.encoderSetPoint = DRIVE_REALLIGN;
			robot.drivePID(.5, 0);
			if(robot.leftEncoder.get() >= robot.encoderSetPoint){
				TURN_DEGREE++;
				slotToTowerStates = SlotToTowerStates.TURN;
			}
			break;
		case APPROACH_TOWER:
			robot.encoderSetPoint = DRIVE_TO_TOWER;
			robot.drivePID(.5, 0);
			if(robot.leftEncoder.get() >= robot.encoderSetPoint) {
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
