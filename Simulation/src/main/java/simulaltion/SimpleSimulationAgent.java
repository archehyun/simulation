package simulaltion;

public class SimpleSimulationAgent extends SimulationAgent{
	
	public SimpleSimulationAgent() {
		super();
	}

	@Override
	public void handleMessage(SimulationMessage message) {		
		
		this.queueImpl.append(message);
	}

	@Override
	public void execute() {
		
		
	}

}
