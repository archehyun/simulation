package simulaltion;

import java.util.ArrayList;

public class SimulationControl {
	
	public ArrayList<IFSimulationAgent> list;
	
	public SimulationControl() {
		list = new ArrayList<IFSimulationAgent>();
	}
	
	public void addSimulationAgent(IFSimulationAgent agent)
	{
		list.add(agent);
	}

}
