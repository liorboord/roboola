import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

import java.util.ArrayList;
import java.util.List;

public class MainRobot {

	DifferentialPilot pilot;
	EV3MediumRegulatedMotor penMotor;
	
	public MainRobot() {
		pilot = new DifferentialPilot(11.9f, 63.0f, Motor.B, Motor.C, true);
//		penMotor = new EV3MediumRegulatedMotor(MotorPort.D);
		initPilot();
		
		boolean go = false;
		VectorsClient govc = new VectorsClient();
		LCD.drawString("Waiting for GO...", 0, 4);
		while (!go) {
			go = govc.shouldGo("ec2-50-16-5-181.compute-1.amazonaws.com");
			LCD.drawString("Got GO!", 0, 5);			
		}
		processFileData();
	}

	private void initPilot() {
		Motor.D.rotateTo(20);
//		pilot.setLinearSpeed(pilot.getMaxLinearSpeed() / 4);
//		pilot.setAngularSpeed(pilot.getMaxAngularSpeed() / 6);
	}

	/**
	 * execute all the commands
	 * @param commands
	 */
	private void executeAll(List<Command> commands) {
		for (Command command : commands) {
			//decide the type of the command and execute it accordingly
			if (command.getType().equals("VECTOR")) {
				Vector vector = (Vector) command;
				pilot.rotate(vector.getAngle());
				pilot.travel(vector.getDistance());
			} else {
				executePenCommand((PenCommand) command);
			}
		}
	}

	private void executePenCommand(PenCommand penCommand) {
		//TODO: add check for current pen angel
		//this is important because we don't want to mess up the pen 
		if (penCommand.getPenDirection().equals("PU")) {
			Motor.D.rotateTo(20);
		} else if (penCommand.getPenDirection().equals("PD")) {
			Motor.D.rotateTo(-20);
		}
	}
	
	private void processFileData() {
		List<Command> commands = new ArrayList<Command>();
		
		//this is some mock data for the data file
		List<String> data = new ArrayList<String>();

		
		/*****/
				
		VectorsClient vc = new VectorsClient();
		String result = vc.getVectors("ec2-50-16-5-181.compute-1.amazonaws.com");

		String[] vectorsArray = result.split("\n");

		int row=0;
		for (String vector: vectorsArray) {
			LCD.drawString("" + vector, 0, row++);
			data.add(vector);
		}
		/*****
		data.add("PD");
		data.add(new String("fwd,80,45"));
		data.add("PU");
		
		data.add(new String("fwd,30,45"));
		data.add("PD");
		data.add(new String("fwd,60,25"));
		data.add(new String("fwd,50,45"));
		 */
		for (String dataLine : data) {
			Command command;
			
			//create the correct type of command
			String[] dataLineArray = dataLine.split(",");
			if (dataLineArray[0].equals("PU") || dataLineArray[0].equals("PD")) {
				command = new PenCommand(dataLineArray);
			} else {
				//LCD.drawString("dlA[0]="+dataLineArray[0] + " len= "+dataLineArray.length, 0, 4);
				//Delay.msDelay(10000);
				command = new Vector(dataLineArray);
			}
			commands.add(command);
		}
		
		executeAll(commands);
	}

	public static void main(String[] args) {
		LCD.drawString("At your command!", 0, 4);
		Delay.msDelay(400);
		new MainRobot();

	}
}
