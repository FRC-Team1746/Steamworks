package org.usfirst.frc.team1746.robot;

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
	int autoLoopCounter;
	Solenoid armControl0;
	Solenoid armControl1;
	Talon intake;
	Victor tapeMeasure;
	DigitalInput beambreak;
	CameraServer server;
	Encoder leftEncoder;
	Encoder rightEncoder;
	AnalogInput defenseSelector;
	AnalogInput slotSelector;
	DigitalInput goalSelector;
	DigitalInput rampDetector;
	Timer delayTime;
	Timer intakeDelay;
	Gyro gyro;
	Relay ballIndicator;
	
	
	
	
	
	
    //////////////////////////////////////////////////////////////
    //////////////           Constants            ////////////////
    //////////////////////////////////////////////////////////////
	
	double WHEELDIAMETER  = 6;
	double WHEEL_CIRCUMFRENCE = 3.14*WHEELDIAMETER;
	double ENCODER_GEAR_RATIO = 1.714; 
	
	double DIST_APPROACH          = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_LOWBAR            = 17.5*12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_A_PORTCULLIS      = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_A_CHEVAL_DE_FRISE = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_B_RAMPARTS        = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_B_MOAT            = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_C_DRAWBRIDGE      = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_C_SALLYPORT       = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_D_ROCKWALL        = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_D_ROUGH_TERRAIN   = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	double DIST_SPYBOT            = 12/WHEEL_CIRCUMFRENCE*100*ENCODER_GEAR_RATIO;
	
	double SPD_APPROACH = .5;
	double SPD_LOWBAR = .5;
	double SPD_A_PORTCULLIS = .5;
	double SPD_A_CHEVAL_DE_FRISE = .5;
	double SPD_B_RAMPARTS = .5;
	double SPD_B_MOAT = .5;
	double SPD_C_DRAWBRIDGE = .5;
	double SPD_C_SALLYPORT = .5;
	double SPD_D_ROCKWALL = .5;
	double SPD_D_ROUGH_TERRAIN = .5;
	double SPD_SPYBOT = .5;
	double SPD_BACKUP = .25;
	double SPD_CALIBRATE = .25;
	
	double CALIBRATE_ANGLE = 10;
	
	double PID_P = 0.01;
	double PID_I = 0.01;
	double MOTOR_INCREMENT_RATE = .02;
	double leftMotorSpeed;
	
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

    	tapeMeasure = new Victor(5);
    			
    	intake = new Talon(6);
    	
    	beambreak = new DigitalInput(6);
    	goalSelector = new DigitalInput(7);
    	rampDetector = new DigitalInput(8);
    	
    	defenseSelector = new AnalogInput(2);
    	slotSelector = new AnalogInput(3);
    	
    	leftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k1X);
    	rightEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k1X);
    	
    	gyro = new ADXRS450_Gyro();
    	
    	server = CameraServer.getInstance();
        server.setQuality(50);
        server.startAutomaticCapture("cam0");
        
        delayTime = new Timer();
        intakeDelay = new Timer();
        
        initrobot();
        
        selectedDefense = Defense.NONE;
        
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
        if(xbox.getRawButton(3)){
        	drivePID(.4, 0);
        } else if(xbox.getRawButton(4)){
        	drivePID(.4, 17.5*12);        	 
        }
       
        
        ////////////////////////////////////////
        ///////            Arm           ///////
        ////////////////////////////////////////
        armControl0.set(xbox.getRawButton(6));
        armControl1.set(xbox.getRawButton(5));
        
        ////////////////////////////////////////
        ///////      Intake/Outake       ///////
        ////////////////////////////////////////
       if(!beambreak.get()){
    	   intakeDelay.start();
    	   if(intakeDelay.get() > 10){
    		   intake.set(0);
    	   }
       } else{
    	   if(xbox.getRawButton(1)){
    		   intake.set(1);
    		   intakeDelay.reset();
    		   intakeDelay.stop();
    	   } else if(xbox.getRawButton(2)){
    		   intake.set(-1);
    	   } else{
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
    	   tapeMeasure.set(1); 
       }else if(xbox.getPOV(0) == 180){
    	   tapeMeasure.set(-1);
       }else{
    	   tapeMeasure.set(0);
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
		SHOOT,
		RETREAT,
		UNBREACH,
		REALIGN,
		WAIT4TELEOP
	};
	
	public enum CalibrateStates {
		INIT,
		REORIENT,
		BACKUP
	};
    public void autonomousInit() {
    	
    	
		leftEncoder.reset();
		rightEncoder.reset();
		updateSmartDashboard();
		
		delayTime.reset();
		delayTime.start();
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
    			autonState = AutonStates.WAIT4TELEOP;
    		}else if(selectedDefense == Defense.APPROACH_ONLY) {
    			autonState = AutonStates.APPROACH;
    		}else if(selectedDefense == Defense.LOWBAR) {
    			autonState = AutonStates.APP_LOWBAR;
    		}else if(selectedDefense == Defense.A_CHEVAL_DE_FRISE) {
    			autonState = AutonStates.APP_A_CHEVAL_DE_FRISE;
    		}else if(selectedDefense == Defense.A_PORTCULLIS) {
    			autonState = AutonStates.APP_A_PORTCULLIS;
    		}else if(selectedDefense == Defense.B_MOAT) {
    			autonState = AutonStates.APP_B_MOAT;
    		}else if(selectedDefense == Defense.B_RAMPARTS) {
    			autonState = AutonStates.APP_B_RAMPARTS;
    		}else if(selectedDefense == Defense.C_DRAWBRIDGE) {
    			autonState = AutonStates.APP_C_DRAWBRIDGE;
    		}else if(selectedDefense == Defense.C_SALLY_PORT) {
    			autonState = AutonStates.APP_C_SALLY_PORT;
    		}else if(selectedDefense == Defense.D_ROCK_WALL) {
    			autonState = AutonStates.APP_D_ROCK_WALL;
    		}else if(selectedDefense == Defense.D_ROUGH_TERRAIN) {
    			autonState = AutonStates.APP_D_ROUGH_TERRAIN;
    		}else if(selectedDefense == Defense.SPYBOT) {
    			autonState = AutonStates.APP_SPYBOT;
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
    		drivePID(SPD_LOWBAR, 0);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.WAIT4TELEOP;
    		}
    		break;
    	case APP_A_PORTCULLIS:
    		encoderSetPoint = DIST_A_PORTCULLIS;
    		myRobot.setLeftRightMotorOutputs(-SPD_A_PORTCULLIS, SPD_A_PORTCULLIS);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_A_CHEVAL_DE_FRISE:
    		encoderSetPoint = DIST_A_CHEVAL_DE_FRISE;
    		myRobot.setLeftRightMotorOutputs(-SPD_A_CHEVAL_DE_FRISE, SPD_A_CHEVAL_DE_FRISE);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_B_MOAT:
    		encoderSetPoint = DIST_B_MOAT;
    		myRobot.setLeftRightMotorOutputs(-SPD_B_MOAT, SPD_B_MOAT);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_B_RAMPARTS:
    		encoderSetPoint = DIST_B_RAMPARTS;
    		myRobot.setLeftRightMotorOutputs(-SPD_B_RAMPARTS, SPD_B_RAMPARTS);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_C_DRAWBRIDGE:
    		encoderSetPoint = DIST_C_DRAWBRIDGE;
    		myRobot.setLeftRightMotorOutputs(-SPD_C_DRAWBRIDGE, SPD_C_DRAWBRIDGE);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_C_SALLY_PORT:
    		encoderSetPoint = DIST_C_SALLYPORT;
    		myRobot.setLeftRightMotorOutputs(-SPD_C_SALLYPORT, SPD_C_SALLYPORT);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_D_ROCK_WALL:
    		encoderSetPoint = DIST_D_ROCKWALL;
    		myRobot.setLeftRightMotorOutputs(-SPD_D_ROCKWALL, SPD_D_ROCKWALL);
    		if( leftEncoder.get() > DIST_D_ROCKWALL){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_D_ROUGH_TERRAIN:
    		encoderSetPoint = DIST_D_ROUGH_TERRAIN;
    		myRobot.setLeftRightMotorOutputs(-SPD_D_ROUGH_TERRAIN, SPD_D_ROUGH_TERRAIN);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
    		}
    		break;
    	case APP_SPYBOT:
    		encoderSetPoint = DIST_SPYBOT;
    		myRobot.setLeftRightMotorOutputs(-SPD_SPYBOT, SPD_SPYBOT);
    		if( leftEncoder.get() > encoderSetPoint){
    			autonState = AutonStates.CALIBRATE;
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
    				myRobot.setLeftRightMotorOutputs(SPD_BACKUP, -SPD_BACKUP);
    				if(!rampDetector.get()){
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
    					autonState = AutonStates.SHOOT;
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
	
	public void newDefenseSelector(){
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
	
	public void setSlotSelector(){
		if(SmartDashboard.getBoolean("Slot 1") && selectedSlot != Slot.SLOT_1){
			selectedSlot = Slot.SLOT_1;
			SmartDashboard.putBoolean("Slot 2", false);
			SmartDashboard.putBoolean("Slot 3", false);
			SmartDashboard.putBoolean("Slot 4", false);
			SmartDashboard.putBoolean("Slot 5", false);
		}else if(SmartDashboard.getBoolean("Slot 2") && selectedSlot != Slot.SLOT_2){
			selectedSlot = Slot.SLOT_2;
			SmartDashboard.putBoolean("Slot 1", false);
			SmartDashboard.putBoolean("Slot 3", false);
			SmartDashboard.putBoolean("Slot 4", false);
			SmartDashboard.putBoolean("Slot 5", false);
		}else if(SmartDashboard.getBoolean("Slot 3") && selectedSlot != Slot.SLOT_3){
			selectedSlot = Slot.SLOT_3;
			SmartDashboard.putBoolean("Slot 1", false);
			SmartDashboard.putBoolean("Slot 2", false);
			SmartDashboard.putBoolean("Slot 4", false);
			SmartDashboard.putBoolean("Slot 5", false);
		}else if(SmartDashboard.getBoolean("Slot 4") && selectedSlot != Slot.SLOT_4){
			selectedSlot = Slot.SLOT_4;
			SmartDashboard.putBoolean("Slot 1", false);
			SmartDashboard.putBoolean("Slot 2", false);
			SmartDashboard.putBoolean("Slot 3", false);
			SmartDashboard.putBoolean("Slot 5", false);
		}else if(SmartDashboard.getBoolean("Slot 5") && selectedSlot != Slot.SLOT_5){
			selectedSlot = Slot.SLOT_5;
			SmartDashboard.putBoolean("Slot 1", false);
			SmartDashboard.putBoolean("Slot 2", false);
			SmartDashboard.putBoolean("Slot 3", false);
			SmartDashboard.putBoolean("Slot 4", false);
		}else if(!SmartDashboard.getBoolean("Slot 1") && !SmartDashboard.getBoolean("Slot 2") && !SmartDashboard.getBoolean("Slot 3") && !SmartDashboard.getBoolean("Slot 4") && !SmartDashboard.getBoolean("Slot 5")){
			selectedSlot = Slot.NONE;
		}
	}
	public void setGoalSelector(){
		if(goalSelector.get() == true){
			selectedGoal = Goal.LOW_LEFT;
		} else{
			selectedGoal = Goal.LOW_RIGHT;
		}
	}
	
//////////////////////////////////////////////////////////////
//////////////        Smart Dashboard         ////////////////
//////////////////////////////////////////////////////////////
	public void updateSmartDashboard(){
		SmartDashboard.putNumber("Left Motor Speed", leftMotorSpeed);
		SmartDashboard.putNumber("Intake Timer", intakeDelay.get());
		//Slot Selector
		
		//Ball Indicator
		SmartDashboard.putBoolean("Ball Indicator", !beambreak.get());
		//Ramp Detector
		SmartDashboard.putBoolean("Ramp Detector", !rampDetector.get());
		//Auton Selections
		SmartDashboard.putString("Selected Goal", selectedGoal.name());
		SmartDashboard.putString("Selected Slot", selectedSlot.name());
		SmartDashboard.putString("Selected Defense", selectedDefense.name());
		//Current State Machine State
		SmartDashboard.putString("Auton State", autonState.name());
		SmartDashboard.putString("Auton Slot State", getSelectedAutonSlotStateName());
		//Left Encoder Values
		SmartDashboard.putNumber("Left Encoder", leftEncoder.get());
		SmartDashboard.putNumber("Right Encoder", rightEncoder.get());
		SmartDashboard.putNumber("Encoder Error", leftEncoder.get() - rightEncoder.get());
		SmartDashboard.putNumber("Encoder Set Point", (int)encoderSetPoint );
		SmartDashboard.putString("Calibrate State", calibrateState.name());
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
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
		SmartDashboard.putBoolean("Ball Indicator Light", false);
		//Slot Selector
		SmartDashboard.putBoolean("Slot 1", false);
		SmartDashboard.putBoolean("Slot 2", false);
		SmartDashboard.putBoolean("Slot 3", false);
		SmartDashboard.putBoolean("Slot 4", false);
		SmartDashboard.putBoolean("Slot 5", false);
		//Goal Selector
		SmartDashboard.putBoolean("Right Goal", false);
		SmartDashboard.putBoolean("Left Goal", false);
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
		    encoderError = leftEncoder.get() - rightEncoder.get()*(1-(turnRadius-11)/(turnRadius+11));
		} else{
			encoderError = leftEncoder.get() - rightEncoder.get();
		}
		double rightMotorSpeed;
		if(leftMotorSpeed < desiredLeftMotorSpeed){
			leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
		}else if(leftMotorSpeed > desiredLeftMotorSpeed){
			leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
		}else{
			leftMotorSpeed = desiredLeftMotorSpeed;
		}
		rightMotorSpeed = leftMotorSpeed + P*encoderError;
		myRobot.setLeftRightMotorOutputs(-leftMotorSpeed, -rightMotorSpeed);
	}
	
	
	public void reversePID(double desiredLeftMotorSpeed, double turnRadius){
		double P = SmartDashboard.getNumber("PID P");
		//double I = SmartDashboard.getNumber("PID I");
		double encoderError;
		if(turnRadius != 0){
		    encoderError = leftEncoder.get() - rightEncoder.get()*(1+(turnRadius-11)/(turnRadius+11));
		} else{
			encoderError = leftEncoder.get() - rightEncoder.get();
		}
		
		double rightMotorSpeed;
		if(leftMotorSpeed < desiredLeftMotorSpeed){
			leftMotorSpeed = leftMotorSpeed+MOTOR_INCREMENT_RATE;
		}else if(leftMotorSpeed > desiredLeftMotorSpeed){
			leftMotorSpeed = leftMotorSpeed-MOTOR_INCREMENT_RATE;
		}else{
			leftMotorSpeed = desiredLeftMotorSpeed;
		}
		rightMotorSpeed = leftMotorSpeed + P*encoderError;
		myRobot.setLeftRightMotorOutputs(+leftMotorSpeed, +rightMotorSpeed);
	}
	public void initrobot(){
		autonState = AutonStates.DELAY;
        calibrateState = CalibrateStates.INIT;
        leftEncoder.reset();
        rightEncoder.reset();
        leftMotorSpeed = .25;
	}
	
	
	
//////////////////////////////////////////////////////////////
//////////////            Disable             ////////////////
//////////////////////////////////////////////////////////////
	public void disabledPeriodic(){
		newDefenseSelector();
    	setGoalSelector();
    	setSlotSelector();
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


