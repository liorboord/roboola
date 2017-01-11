import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;

import java.util.ArrayList;
import java.util.List;

public class MainRobot {

	DifferentialPilot pilot;
	EV3MediumRegulatedMotor penMotor;
	
	public MainRobot() {
		pilot = new DifferentialPilot(1.19f, 7.8f, Motor.B, Motor.C, true);
		penMotor = new EV3MediumRegulatedMotor(MotorPort.D);
		initPilot();
		processFileData();
	}

	private void initPilot() {
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed() / 4);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed() / 6);
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
			penMotor.rotate(20);
		} else if (penCommand.getPenDirection().equals("PD")) {
			penMotor.rotate(-20);
		}
	}
	
	private void processFileData() {
		List<Command> commands = new ArrayList<Command>();
		
		//this is some mock data for the data file
		List<String> data = new ArrayList<String>();

		data.add(new String("fwd,10,90"));
		data.add("PU");
		data.add(new String("fwd,10,90"));
		data.add("PD");
		data.add(new String("fwd,10,90"));
		data.add(new String("fwd,10,90"));

		for (String dataLine : data) {
			Command command;
			
			//create the correct type of command
			String[] dataLineArray = dataLine.split(",");
			if (dataLineArray[0].equals("PU") || dataLineArray[0].equals("PU")) {
				command = new PenCommand(dataLineArray);
			} else {
				command = new Vector(dataLineArray);
			}
			commands.add(command);
		}
		
		executeAll(commands);
	}

	public static void main(String[] args) {
		new MainRobot();

	}
}
