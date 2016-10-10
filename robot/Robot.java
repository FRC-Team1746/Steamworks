package org.usfirst.frc.team1746.robot;


import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot{
	
	RobotDrive myRobot;
	Joystick xbox;
	Joystick xbox2;

	Encoder driveLeftEncoder;
	Encoder driveRightEncoder;
	Encoder shooterEncoder;
	
	Cross Cross;

	DigitalInput PixyCamD;
	AnalogInput PixyCamA;
	
	Talon shooter;
	Talon conveyor;
	Victor intake;
	VictorSP scaling_left;
	VictorSP scaling_right;
	
	Victor drive_frontRight;
	Victor drive_frontLeft;
	Victor drive_rearRight;
	Victor drive_rearLeft;
	
	Solenoid s_intakeI;
	Solenoid s_intakeO;
	Solenoid s_hoodI;
	Solenoid s_hoodO;
	Solenoid s_scalingI;
	Solenoid s_scalingO;
	
	Gyro gyro;
	
	DigitalInput lowConveyorSensor;
	DigitalInput highConveyorSensor;
	
	Boolean haveBall;
	
	Timer autonTime;
	
	Solenoids selectedSolenoid;
	Slots selectedSlot;
	Defenses selectedDefense;
	AutonTasks selectedTask;
	Goals selectedGoal;
	
	
	int loops;
	int ballLoops;
    //////////////////////////////////////////////////////////////
    //////////////           Constants            ////////////////
    //////////////////////////////////////////////////////////////
	
	double PID_P = 0.01;
	double PID_I = 0.01;
	double MOTOR_INCREMENT_RATE = .02;
	double leftMotorSpeed;
	int loopCounter = 0;

	public Robot robot;
	
	public void robotInit() {
    	
    	xbox = new Joystick(0);
    	xbox2 = new Joystick(1);
    	
    	Cross = new Cross(this);
    	
    	drive_frontRight = new Victor(1);
    	drive_frontLeft = new Victor(0);
    	drive_rearRight = new Victor(3);
    	drive_rearLeft = new Victor(2);

		myRobot = new RobotDrive(drive_rearRight, drive_rearLeft, drive_frontRight, drive_frontLeft);
		
    	conveyor = new Talon(7);
    	intake = new Victor(8);
    	shooter = new Talon(6);
    	scaling_left = new VictorSP(4);
    	scaling_right = new VictorSP(5);
    	
    	lowConveyorSensor = new DigitalInput(6);
    	highConveyorSensor = new DigitalInput(7);

    	PixyCamD = new DigitalInput(8);
    	PixyCamA = new AnalogInput(0);

    	loops = 0;
    	ballLoops = 0;
    	haveBall = false;
    	
    	driveLeftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k1X);
    	driveRightEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k1X);
    	shooterEncoder = new Encoder(4, 5, false, Encoder.EncodingType.k1X);
    	shooterEncoder.setDistancePerPulse(1);
    	
    	s_scalingI = new Solenoid(2);
    	s_scalingO = new Solenoid(3);
    	s_hoodI = new Solenoid(4);
    	s_hoodO = new Solenoid(5);
    	s_intakeI = new Solenoid(0);
    	s_intakeO = new Solenoid(1);
    	
    	gyro = new ADXRS450_Gyro();
        
        autonTime = new Timer();
        
        selectedSolenoid = Solenoids.HOOD;
        autonState = AutonStates.INIT;
        selectedDefense = Defenses.SPYBOT;
        selectedSlot = Slots.SLOT_2;

        initrobot();
        initSmartDashboard(); 
        
	}
    public void teleopInit() {
    	updateSmartDashboard();
    
	}
	public void teleopPeriodic() {
    	
        ////////////////////////////////////////
        ///////           Drive          ///////
        ////////////////////////////////////////
    	myRobot.arcadeDrive(xbox, true);
    	
    	updateSmartDashboard();
    	solenoidSelector();
    	defenseSelector();
    	slotSelector();
    	
    	////////////////////////////////////////////////////////////////////
    	////////////////////////////////////////////////////////////////////
    	
    	if(lowConveyorSensor.get()){
    		haveBall = true;
    	}
    	if(xbox.getRawButton(1)){
    		conveyor.set(1);
    		intake.set(-1);
    	}
    	if(xbox.getRawButton(2)){
    		intake.set(0);
    		conveyor.set(0);
    		shooter.set(0);
    	
    	}
    	
    	if(highConveyorSensor.get()){
    		ballLoops++;
    		if(ballLoops > 50 && ballLoops < 150){
    			intake.set(0);
    			conveyor.set(0);
    			shooter.set(0);
    		}
    	}
    	if(!highConveyorSensor.get()){
    		loops++;
    		ballLoops = 0;
    		if(loops > 1){
    			loops = 0;
    			intake.set(0);
    			conveyor.set(0);
    			if(!xbox.getRawButton(2)){
    				shooter.set(1);
    			}	
    		}
    	}	
    	while(xbox.getRawButton(3)){
    		trackObject();
    		/*if(readyToShoot()){
    			conveyor.set(1);
    		}*/
    	}
      	if(xbox.getRawButton(5)){
    		solenoidControl(selectedSolenoid, SolenoidStates.OUT);
    	}
    	if(xbox.getRawButton(6)){
    		solenoidControl(selectedSolenoid, SolenoidStates.IN);
    	}
    	if(xbox.getRawButton(4)){
    		conveyor.set(1);
    	}
    	if(xbox.getRawButton(8)){
    		intake.set(1);
    		conveyor.set(-1);
    	}
    	if(xbox.getRawButton(7)){
    		shooter.set(1);
    	}
    	/////////////////////////////////////////////////////////////////////
    	/////////////////////////////////////////////////////////////////////
    	if(xbox2.getRawButton(5)){
    		solenoidControl(Solenoids.HOOD, SolenoidStates.IN);
    	}
    	if(xbox2.getRawButton(6)){
    		solenoidControl(Solenoids.HOOD, SolenoidStates.OUT);
    	}
    	if(xbox2.getRawButton(7)){
    		solenoidControl(Solenoids.SCALING, SolenoidStates.IN);
    	}
    	if(xbox2.getRawButton(8)){
    		solenoidControl(Solenoids.SCALING, SolenoidStates.OUT);
    	}
    	if(xbox2.getRawButton(1)){
    		scaling_left.set(.5);
    		scaling_right.set(.5);
    	} else if(xbox2.getRawButton(3)){
    		scaling_right.set(-.5);
    		scaling_left.set(-.5);
    	} else {	
    		scaling_left.set(0);
    		scaling_right.set(0);
    	}
    	updateSmartDashboard();
    	////////////////////////////////////////////////////////////////////
    	////////////////////////////////////////////////////////////////////
	}
	//////////////////////////////////////////////////////////////
	//////////////Autonomous           ////////////////
	//////////////////////////////////////////////////////////////

	AutonStates autonState;

	public enum Goals {
		LOW_LEFT,
		LOW_RIGHT,
		HIGH_LEFT,
		HIGH_MID,
		HIGH_RIGHT
	}
	public enum Slots {
		SLOT_1,
		SLOT_2,
		SLOT_3,
		SLOT_4,
		SLOT_5,
	};
	public enum Defenses {
		LOWBAR,
		CHEVAL_DE_FRISE,
		RAMPARTS,
		MOAT,
		SALLY_PORT,
		ROCK_WALL,
		ROUGH_TERRAIN,
		SPYBOT
	}
	public enum AutonTasks {
		NONE,
		SPYBOT_SHOOT,
		APPROACH,
		CROSS,
		SHOOT,
		SHOOT_RETURN,
		DOUBLE_BALL_AUTON
	};
	public enum AutonStates {
		INIT,
		DELAY,
		CROSS,
		ALLIGN,
		SHOOT,
		RETURN,
		INTAKE,
		CROSS_2,
		ALLIGN_2,
		SHOOT_2,
		WAIT4TELEOP
	};
	
	public void autonomousInit(){
		autonState = AutonStates.INIT;
	}
	public void autonomousPeriodic(){
		updateSmartDashboard();
		switch(autonState) {
		case INIT:
			resetEncoder("both");
			//autonState = AutonStates.DELAY;
			solenoidControl(Solenoids.INTAKE, SolenoidStates.OUT);
			autonState = AutonStates.CROSS;
			

		break; //\\INIT//\\
		case DELAY:
			if(autonTime.get() >= SmartDashboard.getNumber("Delay")){
				autonState = AutonStates.CROSS;
			}
		break; //\\DELAY//\\
		case CROSS:
			if(Cross.defense(selectedDefense)){
				if(selectedDefense.equals(Defenses.SPYBOT)){
					autonState = AutonStates.SHOOT;
				} else{
					autonState = AutonStates.ALLIGN;
				}
			}
		break; //\\CROSS//\\
		case ALLIGN:
			shooter.set(1);
			trackObject();
			if(readyToShoot()){
				loops = 0;
				autonState = AutonStates.SHOOT;
			}
			/*if(selectedSlot.equals(Slots.SLOT_2)){
				if(gyro.getAngle() < 0){
					driveRotate("right", .25);

				}
			}
			if(selectedSlot.equals(Slots.SLOT_3)){
				if(gyro.getAngle() < 0){
					driveRotate("right", .25);

				}
			}
			if(selectedSlot.equals(Slots.SLOT_4) || selectedSlot.equals(Slots.SLOT_5)){
				if(gyro.getAngle() < 0){
					driveRotate("right", .25);

				}
			}*/
		break; //\\ALLIGN//\\
		case SHOOT:
			loops++;
			conveyor.set(1);
			if(loops > 150){
				loops = 0;
				shooter.set(0);
				conveyor.set(0);
				autonState = AutonStates.RETURN;
			}
		break; //\\SHOOT//\\
		case RETURN:
			solenoidControl(Solenoids.HOOD, SolenoidStates.IN);
			autonState = AutonStates.WAIT4TELEOP;
		break; //\\RETURN//\\
		case INTAKE:

		break; //\\INTAKE//\\
		case CROSS_2:

		break; //\\CROSS_2//\\
		case ALLIGN_2:

		break; //\\ALLIGN_2//\\
		case SHOOT_2:

		break; //\\SHOOT_2//\\
		case WAIT4TELEOP:
		break; //\\WAIT4TELEOP//\\
		}
	}



	//////////////////////////////////////////////////////////////
	//////////////			Test Begins           ////////////////
	//////////////////////////////////////////////////////////////
	public void testPeriodic() {
		LiveWindow.run();
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
			encoderError = driveLeftEncoder.get() - driveRightEncoder.get() - (driveLeftEncoder.get() * (1-(turnRadius-11)/(turnRadius+11)));
		} else{
			encoderError = driveLeftEncoder.get() - driveRightEncoder.get();
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
		resetEncoder("both");
		autonState = AutonStates.INIT;
		leftMotorSpeed = .25;
	}

	public void driveRotate(String direction, double speed){
		if(direction.equalsIgnoreCase("left")){		
			myRobot.setLeftRightMotorOutputs(speed, -speed);
		} else if(direction.equalsIgnoreCase("right")){
			myRobot.setLeftRightMotorOutputs(-speed, speed);
		} else if(direction.equalsIgnoreCase("off")){
			myRobot.stopMotor();
		} else{
			System.out.println("Drive Rotate: Invalid Drirection");
			System.out.println("Direction: " + direction.toString());
		}
	}

////////////////////////////////////////
///////         Pixy Cam         ///////
////////////////////////////////////////
	
	//https://gyazo.com/50561c1fd3a85fd14ed377a1e45d6db9
	
	
	double PIXY_CENTER = 1425; //Midpoint
	double PIXY_BUFFER = 15; //Allowance left-right
	
	public void trackObject(){			
			double speed;
			double p = Math.abs(PixyCamA.getValue() - PIXY_CENTER);
			
			if(p/PIXY_CENTER < .35){
				speed = .35;
			} else if(p/PIXY_CENTER > .5){
				speed = .5;
			} else{
				speed = p/PIXY_CENTER;
			}
			if(PixyCamA.getValue() >= PIXY_CENTER+PIXY_BUFFER){
				driveRotate("right", speed);
			} else if(PixyCamA.getValue() <= PIXY_CENTER-PIXY_BUFFER){
				driveRotate("left", speed);	
		}
	}

	public boolean readyToShoot(){
		if(PixyCamA.getValue() < (PIXY_BUFFER - PIXY_BUFFER) && PixyCamA.getValue() < (PIXY_BUFFER + PIXY_BUFFER)){
			return true;
		} else {
			return false;
		}
	}
	
	public void aliign(Slots Slot){
		if(selectedSlot.equals(Slots.SLOT_1) || selectedSlot.equals(Slots.SLOT_2)){
			if(gyro.getAngle() < 0){
				driveRotate("right", .25);				
			}
		} else if(selectedSlot.equals(Slots.SLOT_3)){
			if(gyro.getAngle() < 25){
				driveRotate("right", .25);
			} else if(gyro.getAngle() > 25){
				driveRotate("left", .25);
			}
		} else if(selectedSlot.equals(Slots.SLOT_4) || selectedSlot.equals(Slots.SLOT_5)){
			if(gyro.getAngle() > 0){
				driveRotate("left", .25);
			}
		}
	}
	
	public void resetEncoder(String encoder){
		if(encoder.equalsIgnoreCase("left")){
			driveLeftEncoder.reset();
		} else if(encoder.equalsIgnoreCase("right")){
			driveRightEncoder.reset();
		} else if(encoder.equalsIgnoreCase("both")){
			driveLeftEncoder.reset();
			driveRightEncoder.reset();
		} else {
			DriverStation.reportError("Reset Endcoder: Invalid Value", true);
			DriverStation.reportError("Value: " + encoder, true);
		}
	}

	
	//////////////////////////////////////////////////////////////////////
	public void solenoidSelector(){
		for(Solenoids solenoid : Solenoids.values()){
			if(SmartDashboard.getBoolean(solenoid.name()) && selectedSolenoid != solenoid){
				selectedSolenoid = solenoid;
				for(Solenoids solenoid2 : Solenoids.values()){
					if(solenoid2 != solenoid){
						SmartDashboard.putBoolean(solenoid2.name(), false);
					}
				}	
			}
		}
	}
	
	public void defenseSelector(){
		for(Defenses defense1 : Defenses.values()){
			if(SmartDashboard.getBoolean(defense1.name()) && selectedDefense != defense1){
				selectedDefense = defense1;
				for(Defenses defense2 : Defenses.values()){
					if(defense2 != defense1){
						SmartDashboard.putBoolean(defense2.name(), false);
					}
				}	
			}
		}
	}
	
	public void slotSelector(){
		for(Slots slot1 : Slots.values()){
			if(SmartDashboard.getBoolean(slot1.name()) && selectedSlot != slot1){
				selectedSlot = slot1;
				for(Slots slot2 : Slots.values()){
					if(slot2 != slot1){
						SmartDashboard.putBoolean(slot2.name(), false);
					}
				}	
			}
		}
	}
	public void t(){
		for(Goals goal1 : Goals.values()){
			if(SmartDashboard.getBoolean(goal1.name()) && selectedGoal != goal1){
				selectedGoal = goal1;
				for(Goals goal2 : Goals.values()){
					if(goal2 != goal1){
						SmartDashboard.putBoolean(goal2.name(), false);
					}
				}	
			}
		}
	}
	/*public void setAutonPathSelector(){
		for(AutonTasks autonPath1 : AutonTasks.values()){
			if(SmartDashboard.getBoolean(autonPath1.name()) && selectedAutonTask != autonPath1){
				selectedAutonTask = autonPath1;
				for(AutonPaths autonPath2 : AutonPaths.values()){
					if(autonPath2 != autonPath1){
						SmartDashboard.putBoolean(autonPath2.name(), false);
					}
				}	
			}
		}
	}*/
	///////////////////////////////////////////////////////////////////////
	
	public enum Encoders {
		LEFT,
		RIGHT,
		SHOOTER
	};

	public double getEncoderTicks(Encoders encoder){
		if(encoder.equals(Encoders.LEFT)){
			return driveLeftEncoder.get();
		} else if(encoder.equals(Encoders.RIGHT)){
			return driveRightEncoder.get();
		} else if(encoder.equals(Encoders.SHOOTER)){
			return shooterEncoder.get();
		} else {
			DriverStation.reportError("Get Endcoder Ticks: Invalid Value", true);
			DriverStation.reportError("Value: " + encoder, true);
			return 0;
		}
	}

//////////////////////////////////////////////////////////////
//////////////            Disabled            ////////////////
//////////////////////////////////////////////////////////////

	public void disabledPeriodic(){
		if(SmartDashboard.getBoolean("Calibrate")){
			gyro.calibrate();
			SmartDashboard.putBoolean("Calibrate", false);
		}
		if(SmartDashboard.getBoolean("Reset")){
			initrobot();
			SmartDashboard.putBoolean("Reset", false);
		}
		defenseSelector();
		slotSelector();
		solenoidSelector();
		updateSmartDashboard();
	}
	public enum SolenoidStates {
		OUT,
		IN,
		OFF,
	};
	public enum Solenoids {
		INTAKE,
		HOOD,
		SCALING
	};

	public void solenoidControl(Solenoids solenoid, SolenoidStates status){
		if(solenoid.equals(Solenoids.INTAKE)){
			if(status.equals(SolenoidStates.OUT)){
				s_intakeI.set(false);
				s_intakeO.set(true);
			}
			if(status.equals(SolenoidStates.IN)){
				s_intakeI.set(true);
				s_intakeO.set(false);
			}
			if(status.equals(SolenoidStates.OFF)){
				s_intakeI.set(false);
				s_intakeO.set(false);
			}
		}
		if(solenoid.equals(Solenoids.HOOD)){
			if(status.equals(SolenoidStates.OUT)){
				s_hoodI.set(false);
				s_hoodO.set(true);
			}
			if(status.equals(SolenoidStates.IN)){
				s_hoodI.set(true);
				s_hoodO.set(false);
			}
			if(status.equals(SolenoidStates.OFF)){
				s_hoodI.set(false);
				s_hoodO.set(false);
			}
		}
		if(solenoid.equals(Solenoids.SCALING)){
			if(status.equals(SolenoidStates.OUT)){
				s_scalingI.set(false);
				s_scalingO.set(true);
		}
			if(status.equals(SolenoidStates.IN)){
				s_scalingI.set(true);
				s_scalingO.set(false);
		}
			if(status.equals(SolenoidStates.OFF)){
				s_scalingI.set(false);
				s_scalingO.set(false);
			}
		}
	}
	//double shooter_rpm = shooterEncoder.getRate()/360;
	public void setShooterRPM(double RPM){
		
	}
//////////////////////////////////////////////////////////////
//////////////        Smart Dashboard         ////////////////
//////////////////////////////////////////////////////////////

	public void initSmartDashboard(){
		SmartDashboard.putBoolean("Reset", false);
		SmartDashboard.putBoolean("Calibrate", false);
		SmartDashboard.putNumber("Delay", .5);
		SmartDashboard.putNumber("PID P", PID_P);
		SmartDashboard.putNumber("PID I", PID_I);
		for(Slots slot : Slots.values()){
			SmartDashboard.putBoolean(slot.name(), false);
		}
		//Goal Selector
		for(Goals goal : Goals.values()){
			SmartDashboard.putBoolean(goal.name(), false);
		}
		//Defense Selector
		for(Defenses defense :Defenses.values()){
			SmartDashboard.putBoolean(defense.name(), false);
		}
		for(AutonTasks task : AutonTasks.values()){
			SmartDashboard.putBoolean(task.name(), false);
		}
		for(Solenoids solenoid : Solenoids.values()){
			SmartDashboard.putBoolean(solenoid.name(), false);
		}
	}
	
	public void updateSmartDashboard(){
		//SmartDashboard.putNumber("Shooter Encoder", shooter_rpm);
		SmartDashboard.putNumber("Left Motor Speed", leftMotorSpeed);
		//Auton Selections
		//SmartDashboard.putString("Selected Goal", selectedGoal.name());
		SmartDashboard.putString("Selected Slot", selectedSlot.name());
		SmartDashboard.putString("Selected Defense", selectedDefense.name());
		SmartDashboard.putString("Selected Solenoid", selectedSolenoid.name());
		//Encoder Values
		SmartDashboard.putNumber("Left Encoder", driveLeftEncoder.get());
		SmartDashboard.putNumber("Right Encoder", driveRightEncoder.get());
		SmartDashboard.putNumber("Encoder Error", driveLeftEncoder.get() - driveRightEncoder.get());
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		SmartDashboard.putBoolean("high conveyor sensor", highConveyorSensor.get());
    	SmartDashboard.putBoolean("low conveyor sensor", lowConveyorSensor.get());
		SmartDashboard.putNumber("PixyCamA", PixyCamA.getValue());
		SmartDashboard.putBoolean("PixyCamD", PixyCamD.get());
		SmartDashboard.putString("autonState", autonState.name());
	}
}


