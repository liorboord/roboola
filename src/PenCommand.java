public class PenCommand extends Command{
	String penDirection;
	
	public PenCommand(String[] data) {
		super(data);
		this.penDirection = data[0];
	}
	
	public String getType() {
		return "PENCOMMAND";
	}

	public String getPenDirection() {
		return penDirection;
	}
}
