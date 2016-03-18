package org.usfirst.frc.team1746.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	
	Preferences prefs;
	Auton_Slot_1 Auton_Slot_1;
	Auton_Slot_2 Auton_Slot_2;
	Auton_Slot_3 Auton_Slot_3;
	Auton_Slot_4 Auton_Slot_4;
	Auton_Slot_5 Auton_Slot_5;
	RobotDrive myRobot;
	Joystick xbox;
	Solenoid armControl0;
	Solenoid armControl1;
	Talon intake;
	VictorSP scalingWinch;
	DigitalInput beambreak;
	CameraServer server;
	Encoder leftEncoder;
	Encoder rightEncoder;
	AnalogInput defenseSelector;
	AnalogInput slotSelector;
	DigitalInput goalSelector;
	DigitalInput rampDetectorLeft;
	DigitalInput rampDetectorRight;
	DigitalInput pneumaticSensor;
	Timer delayTime;
	Gyro gyro;
	Relay ballIndicator;
	
	
	
	
	
	
    //////////////////////////////////////////////////////////////
    //////////////           Constants            ////////////////
    //////////////////////////////////////////////////////////////
	
	double WHEELDIAMETER  = 6;
	double WHEEL_CIRCUMFRENCE = 3.14*WHEELDIAMETER;
	double ENCODER_GEAR_RATIO = 1.714; 
	
	double DIST_APPROACH          = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_LOWBAR            = 150/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_A_PORTCULLIS      = 121/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_A_CHEVAL_DE_FRISE = 121/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_B_RAMPARTS        = 200/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_B_MOAT            = 200/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_C_DRAWBRIDGE      = 121/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_C_SALLYPORT       = 121/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_D_ROCKWALL        = 121/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_D_ROUGH_TERRAIN   = 180/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_SPYBOT            = 50/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	
	double DIST_RETREAT_LOWBAR    = 121/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;

	double SPD_APPROACH = .5;
	double SPD_LOWBAR = .4;
	double SPD_A_PORTCULLIS = .5;
	double SPD_A_CHEVAL_DE_FRISE = .5;
	double SPD_B_RAMPARTS = .8;
	double SPD_B_MOAT = .75;
	double SPD_C_DRAWBRIDGE = .5;
	double SPD_C_SALLYPORT = .5;
	double SPD_D_ROCKWALL = .75;
	double SPD_D_ROUGH_TERRAIN = .75;
	double SPD_SPYBOT = .5;
	double SPD_BACKUP = 0.25;
	double SPD_CALIBRATE = .25;
	
	double CALIBRATE_ANGLE = 10;
	
	double PID_P = 0.01;
	double PID_I = 0.01;
	double MOTOR_INCREMENT_RATE = .02;
	double leftMotorSpeed;
	
	Boolean armLowered;
	int loopCounter = 0;
	
    //////////////////////////////////////////////////////////////
    //////////////          Robot Init            ////////////////
    //////////////////////////////////////////////////////////////
	
	public Robot robot;
	
    public void robotInit() {
    	Auton_Slot_1 = new Auton_Slot_1(this);
    	Auton_Slot_2 = new Auton_Slot_2(this);
    	Auton_Slot_3 = new Auton_Slot_3(this);
    	Auton_Slot_4 = new Auton_Slot_4(this);
    	Auton_Slot_5 = new Auton_Slot_5(this);
    	
    	Auton_Slot_1.init();
    	Auton_Slot_2.init();
    	Auton_Slot_3.init();
    	Auton_Slot_4.init();
    	Auton_Slot_5.init();
    	
    	myRobot = new RobotDrive(3, 2, 1, 0);
    	
    	xbox = new Joystick(0);
    	
    	armControl0 = new Solenoid(0);
    	armControl1 = new Solenoid(1);
    	
    	ballIndicator = new Relay(0);

    	scalingWinch = new VictorSP(5);
    			
    	intake = new Talon(6);
    	
    	pneumaticSensor = new DigitalInput(5);
    	beambreak = new DigitalInput(6);
    	goalSelector = new DigitalInput(7);
    	rampDetectorLeft = new DigitalInput(8);
    	rampDetectorRight = new DigitalInput(9);
    	
    	
    	defenseSelector = new AnalogInput(2);
    	slotSelector = new AnalogInput(3);
    	
    	leftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k1X);
    	rightEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k1X);
    	
    	gyro = new ADXRS450_Gyro();
    	
    	server = CameraServer.getInstance();
        server.setQuality(50);	
        server.startAutomaticCapture("cam0");
        
        delayTime = new Timer();
        
        initrobot();
        
        selectedDefense = Defense.NONE;
        selectedSlot = Slot.NONE;
        selectedGoal = Goal.LOW_LEFT;
        
        armLowered = false;
		int loopCounter = 0;
        
        putSmartDashboard();
	
    }
    
    //////////////////////////////////////////////////////////////
    //////////////         Teleop Begins          ////////////////
    //////////////////////////////////////////////////////////////
    
    public void teleopInit(){
    	updateSmartDashboard();
    }

    public void teleopPeriodic() {
        ////////////////////////////////////////
        ///////           Drive          ///////
        ////////////////////////////////////////
    	myRobot.arcadeDrive(xbox, true);
       
        
        ////////////////////////////////////////
        ///////            Arm           ///////
        ////////////////////////////////////////
        if(xbox.getRawButton(1)){
        	armControl("up");
        } else if(xbox.getRawButton(2)){
        	armControl("down");
        } else{
        	armControl("off");
        }
        
        ////////////////////////////////////////
        ///////      Intake/Outake       ///////
        ////////////////////////////////////////
       if(xbox.getRawButton(1)){
    	   intake.set(1);
    	   loopCounter = 0;
       } else if(xbox.getRawButton(2)){
    	   loopCounter = 0;
    	   intake.set(-1);
       } else if(xbox.getRawButton(3)){
    	   intake.set(0);
       }
       
       if(!beambreak.get()){
    	   loopCounter++;
    	   if(intake.get() == 1 && loopCounter > 10){
    		   intake.set(0);
    	   }
       }   
       if(intake.get() == -1 && beambreak.get()){
    	   loopCounter++;
    	   if(loopCounter > 20){
        	   intake.set(0);
    	   }   
       }
        ////////////////////////////////////////
        ///////      Ball Indicator      ///////
        ////////////////////////////////////////
        if(!beambreak.get()){
        	ballIndicator.set(Relay.Value.kForward);
        } else{
        	ballIndicator.set(Relay.Value.kOff);
        }
        
      // Tape Measure Motor
       if(xbox.getPOV(0) == 0){
    	   scalingWinch.set(1); 
       }else if(xbox.getPOV(0) == 180){
    	   scalingWinch.set(-1);
       }else{
    	   scalingWinch.set(0);
       }
       updateSmartDashboard();
    }
    
    //////////////////////////////////////////////////////////////
    //////////////         Test Begins            ////////////////
    //////////////////////////////////////////////////////////////
    public void testPeriodic() {
    	LiveWindow.run();
    }
    //////////////////////////////////////////////////////////////
    //////////////         Auton Begins           ////////////////
    //////////////////////////////////////////////////////////////
    
    Defense selectedDefense;
    Slot selectedSlot;
    Goal selectedGoal;
    double encoderSetPoint;
    
    public enum Defense {
		NONE,
		SPYBOT,
		APPROACH_ONLY,
		LOWBAR,
		A_PORTCULLIS,
		A_CHEVAL_DE_FRISE,
		B_RAMPARTS,
		B_MOAT,
		C_DRAWBRIDGE,
		C_SALLY_PORT,
		D_ROCK_WALL,
		D_ROUGH_TERRAIN
	};
	public enum Slot {
		SLOT_1,
		SLOT_2,
		SLOT_3,
		SLOT_4,
		SLOT_5,
		NONE
	};
	public enum Goal {
		LOW_LEFT,
		LOW_RIGHT,
		HIGH_LEFT,
		HIGH_MID,
		HIGH_RIGHT
	};
	public enum AutonStates {
		INIT,
		DELAY,
		APPROACH,
		APP_LOWBAR,
		APP_A_PORTCULLIS,
		APP_A_CHEVAL_DE_FRISE,
		APP_B_RAMPARTS,
		APP_B_MOAT,
		APP_C_DRAWBRIDGE,
		APP_C_SALLY_PORT,
		APP_D_ROCK_WALL,
		APP_D_ROUGH_TERRAIN,
		APP_SPYBOT,
		CALIBRATE,
		APPROACH_TOWER,
		RELEASE_BALL,
		SHOOT,
		RETREAT,
		UNBREACH,
		REALIGN,
		WAIT4TELEOP,
		RETREAT_LOWBAR,
		RETREAT_A_PORTCULLIS,
		RETREAT_A_CHEVAL_DE_FRISE,
		RETREAT_B_RAMPARTS,
		RETREAT_B_MOAT,
		RETREAT_C_DRAWBRIDGE,
		RETREAT_C_SALLY_PORT,
		RETREAT_D_ROCK_WALL,
		RETREAT_D_ROUGH_TERRAIN,
		RETREAT_SPYBOT,
		LOWER_ARM_TO_FLOOR,
		LOWER_ARM_TO_SENSOR
	};
	
	public enum CalibrateStates {
		INIT,
		REORIENT,
		BACKUP
	};
	
	public static HashMap<Defense, AutonStates> DefenseToAutonState = new HashMap<Defense, AutonStates>();
	
    public void autonomousInit() {
    	
    	
		leftEncoder.reset();
		rightEncoder.reset();
		updateSmartDashboard();
		
		delayTime.reset();
		delayTime.start();
		
		//Hashmap
		DefenseToAutonState.put(Defense.LOWBAR, autonState.APP_LOWBAR);
		DefenseToAutonState.put(Defense.A_CHEVAL_DE_FRISE, autonState.APP_A_CHEVAL_DE_FRISE);
		DefenseToAutonState.put(Defense.A_PORTCULLIS, autonState.APP_A_PORTCULLIS);
		DefenseToAutonState.put(Defense.APPROACH_ONLY, autonState.APPROACH);
		DefenseToAutonState.put(Defense.B_MOAT, autonState.APP_B_MOAT);
		DefenseToAutonState.put(Defense.B_RAMPARTS, autonState.APP_B_RAMPARTS);
		DefenseToAutonState.put(Defense.C_DRAWBRIDGE, autonState.APP_C_DRAWBRIDGE);
		DefenseToAutonState.put(Defense.C_SALLY_PORT, autonState.APP_C_SALLY_PORT);
		DefenseToAutonState.put(Defense.D_ROCK_WALL, autonState.APP_D_ROCK_WALL);
		DefenseToAutonState.put(Defense.D_ROUGH_TERRAIN, autonState.APP_D_ROUGH_TERRAIN);
		DefenseToAutonState.put(Defense.LOWBAR, autonState.APP_LOWBAR);
		DefenseToAutonState.put(Defense.NONE, autonState.WAIT4TELEOP);
		DefenseToAutonState.put(Defense.SPYBOT, autonState.APP_SPYBOT);
		
    }
    
    
    
    
    AutonStates autonState;
    CalibrateStates calibrateState;
    
    public void autonomousPeriodic() {
    	switch (autonState) {
    	case DELAY:
    		if(delayTime.get() >= SmartDashboard.getNumber("Delay")){
    			autonState = AutonStates.INIT;
    			
    		}
    		break;
    	case INIT:
    		leftEncoder.reset();
			rightEncoder.reset();
			
			
    		if(selectedDefense == Defense.NONE){
    			autonState= DefenseToAutonState.get(selectedDefense);
    			autonState = AutonStates.WAIT4TELEOP;
    		}else if(selectedDefense == Defense.APPROACH_ONLY) {
    			autonState = AutonStates.APPROACH;
    		}else if(selectedDefense == Defense.LOWBAR) {
    			autonState = AutonStates.LOWER_ARM_TO_FLOOR;
    		}else if(selectedDefense == Defense.A_CHEVAL_DE_FRISE) {
    			autonState = AutonStates.APP_A_CHEVAL_DE_FRISE;
    		}else if(selectedDefense == Defense.A_PORTCULLIS) {
    			autonState = AutonStates.LOWER_ARM_TO_FLOOR;
    		}else if(selectedDefense == Defense.B_MOAT) {
    			autonState = AutonStates.LOWER_ARM_TO_SENSOR;
    		}else if(selectedDefense == Defense.B_RAMPARTS) {
    			autonState = AutonStates.LOWER_ARM_TO_SENSOR;
    		}else if(selectedDefense == Defense.C_DRAWBRIDGE) {
    			autonState = AutonStates.APPROACH;
    		}else if(selectedDefense == Defense.C_SALLY_PORT) {
    			autonState = AutonStates.APPROACH;
    		}else if(selectedDefense == Defense.D_ROCK_WALL) {
    			autonState = AutonStates.LOWER_ARM_TO_SENSOR;
    		}else if(selectedDefense == Defense.D_ROUGH_TERRAIN) {
    			autonState = AutonStates.LOWER_ARM_TO_SENSOR;
    		}else if(selectedDefense == Defense.SPYBOT) {
    			autonState = AutonStates.APP_SPYBOT;
    		}
    	break;
    	
    	case LOWER_ARM_TO_FLOOR:
    		
    		if(!pneumaticSensor.get()){
    			armLowered = true;    			
    		}
    		
    		if(armLowered){
    			loopCounter++;
    			if(loopCounter > 75){
    				loopCounter = 0;
    				armControl1.set(false);
    				autonState = DefenseToAutonState.get(selectedDefense);
    			}
    		} else{
    			armControl1.set(true);
    		}
    		break;
    	case LOWER_ARM_TO_SENSOR:
    		armControl1.set(true);
    		if(!pneumaticSensor.get()){
    			armControl1.set(false);
    			loopCounter = 0;
    			autonState = DefenseToAutonState.get(selectedDefense);
    			
    		}
    		break;
    	case APPROACH:
    		encoderSetPoint = DIST_APPROACH;
    		drivePID(SPD_APPROACH, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_LOWBAR:
    		encoderSetPoint = DIST_LOWBAR;
    		if(leftEncoder.get() <= encoderSetPoint){
    			drivePID(SPD_LOWBAR, 0);
    		} else {
    			drivePID(0,0);
    			if(leftMotorSpeed < MOTOR_INCREMENT_RATE){
    				leftEncoder.reset();
        			rightEncoder.reset();
        			autonState = AutonStates.RELEASE_BALL;
    			}
    		}
    		
    		break;
    	case APP_A_PORTCULLIS:
    		encoderSetPoint = DIST_A_PORTCULLIS;
    		drivePID(SPD_A_PORTCULLIS, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_A_CHEVAL_DE_FRISE:
    		encoderSetPoint = DIST_A_CHEVAL_DE_FRISE;
    		drivePID(SPD_A_CHEVAL_DE_FRISE, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_B_MOAT:
    		encoderSetPoint = DIST_B_MOAT;
    		drivePID(SPD_B_MOAT, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_B_RAMPARTS:
    		encoderSetPoint = DIST_B_RAMPARTS;
    		drivePID(SPD_B_RAMPARTS, 170);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_C_DRAWBRIDGE:
    		encoderSetPoint = DIST_C_DRAWBRIDGE;
    		drivePID(SPD_C_DRAWBRIDGE, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_C_SALLY_PORT:
    		encoderSetPoint = DIST_C_SALLYPORT;
    		drivePID(SPD_C_SALLYPORT, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_D_ROCK_WALL:
    		encoderSetPoint = DIST_D_ROCKWALL;
    		drivePID(SPD_D_ROCKWALL, 0);
    		if( leftEncoder.get() > DIST_D_ROCKWALL){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_D_ROUGH_TERRAIN:
    		encoderSetPoint = DIST_D_ROUGH_TERRAIN;
    		drivePID(SPD_D_ROUGH_TERRAIN, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_SPYBOT:
    		encoderSetPoint = DIST_SPYBOT;
    		drivePID(SPD_SPYBOT, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case RELEASE_BALL:
    		intake.set(-1);
    		loopCounter++;
    		if(loopCounter > 50){
    			intake.set(0);
    			autonState = AutonStates.RETREAT_LOWBAR;
    		}
    		break;
    	case RETREAT_LOWBAR: 
    		encoderSetPoint = -DIST_RETREAT_LOWBAR;
    		drivePID(-SPD_LOWBAR, 0);
    		if( leftEncoder.get() < encoderSetPoint){
    			drivePID(0,0);
    			if(leftMotorSpeed < MOTOR_INCREMENT_RATE){
    				leftEncoder.reset();
    				rightEncoder.reset();
    				autonState = AutonStates.WAIT4TELEOP;
    			}
    		}
    		break;
    		
    	case CALIBRATE:
    		switch (calibrateState) {
    			case INIT: 
    				calibrateState = CalibrateStates.REORIENT;
    				break;
    			case REORIENT: 
    				if(gyro.getAngle() > 0){
    					myRobot.setLeftRightMotorOutputs(SPD_CALIBRATE, SPD_CALIBRATE);
    				} else{
    					myRobot.setLeftRightMotorOutputs(-SPD_CALIBRATE, -SPD_CALIBRATE);
    				}
    				
    				if((gyro.getAngle() > -CALIBRATE_ANGLE) && (gyro.getAngle() < CALIBRATE_ANGLE)){
    					leftEncoder.reset();
    					rightEncoder.reset();
    					calibrateState = CalibrateStates.BACKUP;
    				}
    				
    					
    				break;
    			case BACKUP:
    				//myRobot.setLeftRightMotorOutputs(SPD_BACKUP, SPD_BACKUP);
    				drivePID(SPD_BACKUP, 0);
    				if(!rampDetectorLeft.get() && !rampDetectorRight.get()){
    					autonState = AutonStates.APPROACH_TOWER;
    					//Add failsafe code later
    				}
    				break;
    		}
        	break;
    	case APPROACH_TOWER:
    		switch (selectedSlot) {
    			case NONE:
    				autonState = AutonStates.WAIT4TELEOP;
    				break;
    			case SLOT_1:
    				if(Auton_Slot_1.stateMachine()){
    					autonState = AutonStates.WAIT4TELEOP;
    				}
    				break;
    			case SLOT_2:
    				if(Auton_Slot_2.stateMachine()){
    					autonState = AutonStates.SHOOT;
    				}
    				break;
    			case SLOT_3:
    				if(Auton_Slot_3.stateMachine()){
    					autonState = AutonStates.SHOOT;
    				}
    				break;
    			case SLOT_4:
    				if(Auton_Slot_4.stateMachine()){
    					autonState = AutonStates.SHOOT;
    				}
    				break;
    			case SLOT_5:
    				if(Auton_Slot_5.stateMachine()){
    					autonState = AutonStates.SHOOT;
    				}
    				break;
    		}
        	break; //case APPROACH_TOWER:
    	case SHOOT:
          	intake.set(1);
          	if(beambreak.get()){
          		intake.set(0);
          		autonState = AutonStates.WAIT4TELEOP;
          	}
    		
        	break;
    	case RETREAT:
        	break;
    	case UNBREACH:
        	break;
    	case REALIGN:
        	break;
    	case WAIT4TELEOP:
        	break;
    	}
        updateSmartDashboard();
    }
	
	public void new_DefenseSelector(){
		for(Defense defense1 : Defense.values()){
			if(SmartDashboard.getBoolean(defense1.name()) && selectedDefense != defense1){
				selectedDefense = defense1;
				for(Defense defense2 : Defense.values()){
					if(defense2 != defense1){
						SmartDashboard.putBoolean(defense2.name(), false);
					}
				}	
			}
		}
		
		
		
	}
	public void new_SlotSelector(){
		for(Slot slot1 : Slot.values()){
			if(SmartDashboard.getBoolean(slot1.name()) && selectedSlot != slot1){
				selectedSlot = slot1;
				for(Slot slot2 : Slot.values()){
					if(slot2 != slot1){
						SmartDashboard.putBoolean(slot2.name(), false);
					}
				}	
			}
		}
	}
	public void setGoalSelector(){
		for(Goal goal1 : Goal.values()){
			if(SmartDashboard.getBoolean(goal1.name()) && selectedGoal != goal1){
				selectedGoal = goal1;
				for(Goal goal2 : Goal.values()){
					if(goal2 != goal1){
						SmartDashboard.putBoolean(goal2.name(), false);
					}
				}	
			}
		}
	}
	
//////////////////////////////////////////////////////////////
//////////////        Smart Dashboard         ////////////////
//////////////////////////////////////////////////////////////
	public void updateSmartDashboard(){
		//SmartDashboard.putBoolean("Arm Lowered", armLowered);
		SmartDashboard.putNumber("Loop Counter", loopCounter);
		SmartDashboard.putNumber("Left Motor Speed", leftMotorSpeed);
		//Slot Selector
		
		//Ball Indicator
		SmartDashboard.putBoolean("Ball Indicator", !beambreak.get());
		//Ramp Detector
		//SmartDashboard.putBoolean("Ramp Detector Left", !rampDetectorLeft.get());
		//SmartDashboard.putBoolean("Ramp Detector Right", !rampDetectorRight.get());
		//Pneumatic Sensor
		//SmartDashboard.putBoolean("Pneumatic Sensor", pneumaticSensor.get());
		//Auton Selections
		SmartDashboard.putString("Selected Goal", selectedGoal.name());
		SmartDashboard.putString("Selected Slot", selectedSlot.name());
		SmartDashboard.putString("Selected Defense", selectedDefense.name());
		//Current State Machine State
		//SmartDashboard.putString("Auton State", autonState.name());
		//SmartDashboard.putString("Auton Slot State", getSelectedAutonSlotStateName());
		//Left Encoder Values
		SmartDashboard.putNumber("Left Encoder", leftEncoder.get());
		SmartDashboard.putNumber("Right Encoder", rightEncoder.get());
		SmartDashboard.putNumber("Encoder Error", leftEncoder.get() - rightEncoder.get());
		SmartDashboard.putNumber("Encoder Set Point", (int)encoderSetPoint );
		//SmartDashboard.putString("Calibrate State", calibrateState.name());
		//SmartDashboard.putNumber("Gyro", gyro.getAngle());
	}
	public String getSelectedAutonSlotStateName(){
		if(Auton_Slot_1.getCurrentSlotName() != null){
			if(selectedSlot.name() == "SLOT_1"){
				return Auton_Slot_1.getCurrentSlotName();
			} else if(selectedSlot.name() == "SLOT_2"){
				return Auton_Slot_2.getCurrentSlotName();
			}  else if(selectedSlot.name() == "SLOT_3"){
				return Auton_Slot_3.getCurrentSlotName();
			}  else if(selectedSlot.name() == "SLOT_4"){
				return Auton_Slot_4.getCurrentSlotName();
			}  else if(selectedSlot.name() == "SLOT_5"){  
				return Auton_Slot_5.getCurrentSlotName();
			} else {
				return "NONE";
			}	
		} else {
			return "NULL";
		}
	}
	
	
	public void putSmartDashboard(){
		SmartDashboard.putBoolean("Reset", false);
		SmartDashboard.putBoolean("Calibrate", false);
		SmartDashboard.putNumber("Delay", 0);
		//SmartDashboard.putBoolean("Ball Indicator Light", false);
		//Ramp Detect
		//SmartDashboard.putBoolean("Ramp Detector Left", false);
		//SmartDashboard.putBoolean("Ramp Detector Right", false);
		//Slot Selector
		for(Slot slot : Slot.values()){
			SmartDashboard.putBoolean(slot.name(), false);
		}
		//Goal Selector
		for(Goal goal : Goal.values()){
			SmartDashboard.putBoolean(goal.name(), false);
		}
		//Defense Selector
		for(Defense defense : Defense.values()){
			SmartDashboard.putBoolean(defense.name(), false);
		}
		//PID_P
		SmartDashboard.putNumber("PID P", PID_P);
		SmartDashboard.putNumber("PID I", PID_I);
	}
	//////////////////////////////////////////////////////////////
	//////////////		    	PID              ////////////////
	/////////////////////////////////////////////////////////////	
	
	int sumEncoderErrors;
	
	public void drivePID(double desiredLeftMotorSpeed, double turnRadius){
		double P = SmartDashboard.getNumber("PID P");
		//double I = SmartDashboard.getNumber("PID I");
		double encoderError;
		if(turnRadius != 0){
		    encoderError = leftEncoder.get() - rightEncoder.get() - (leftEncoder.get() * (1-(turnRadius-11)/(turnRadius+11)));
		} else{
			encoderError = leftEncoder.get() - rightEncoder.get();
		}
		double rightMotorSpeed;
		if(desiredLeftMotorSpeed >= 0){
			if(leftMotorSpeed < desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
			}else if(leftMotorSpeed > desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
			}else{
				leftMotorSpeed = desiredLeftMotorSpeed;
			}
		} else{
			if(leftMotorSpeed > desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
			}else if(leftMotorSpeed < desiredLeftMotorSpeed){
				leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
			}else{
				leftMotorSpeed = desiredLeftMotorSpeed;
			}
		}
		
		rightMotorSpeed = leftMotorSpeed + P*encoderError;
		myRobot.setLeftRightMotorOutputs(-leftMotorSpeed, -rightMotorSpeed);
	}
	
	public void initrobot(){
		autonState = AutonStates.DELAY;
        calibrateState = CalibrateStates.INIT;
        leftEncoder.reset();
        rightEncoder.reset();
        leftMotorSpeed = .25;
        armLowered = false;
	}
	
	public void armControl(String value){
		if(value.equalsIgnoreCase("up")){
			armControl1.set(false);
			armControl0.set(true);
		} else if(value.equalsIgnoreCase("down")){
			armControl0.set(false);
			armControl1.set(true);
		} else if(value.equalsIgnoreCase("off")){
			armControl0.set(false);
			armControl1.set(false);
		} else {
			System.out.println("Arm Control: Invalid Value");
			System.out.println("Value: " + value.toString());
		}
		
	}
	
	
	
//////////////////////////////////////////////////////////////
//////////////            Disable             ////////////////
//////////////////////////////////////////////////////////////
	public void disabledPeriodic(){
		new_DefenseSelector();
    	setGoalSelector();
    	new_SlotSelector();
    	if(SmartDashboard.getBoolean("Calibrate")){
    		gyro.calibrate();
    		SmartDashboard.putBoolean("Calibrate", false);
    	}
    	
    	if(SmartDashboard.getBoolean("Reset")){
    		initrobot();
    		SmartDashboard.putBoolean("Reset", false);
    	}
    	updateSmartDashboard();
    }

}


