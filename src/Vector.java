import lejos.robotics.navigation.DifferentialPilot;

public class Vector extends Command{
	String direction; //F or B
	int distance;
	int angle;
	DifferentialPilot pilot;

	
	public Vector(String[] data) {
		super(data);
		this.distance = Integer.parseInt(data[1]);
		this.angle = Integer.parseInt(data[2]);
	}

	public String getType() {
		return "VECTOR";
	}
	
	public void execute(){
		pilot.rotate(angle);
		pilot.travel(distance);
	}
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
}
