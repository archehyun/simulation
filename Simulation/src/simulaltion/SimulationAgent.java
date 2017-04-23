package simulaltion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import simulaltion.queue.MsgQueueImpl;

public abstract class SimulationAgent implements IFSimulationAgent{
	
	InboundMessageListner inboundMessageListner;
	
	List<IFSimulationMonitor> monitors; 
	
	protected boolean isSelected;//
	
	protected MsgQueueImpl queueImpl;
	
	public SimulationAgent() {
		queueImpl = new MsgQueueImpl();
		monitors = new LinkedList<IFSimulationMonitor>();
	}
	
	public abstract void execute();
	
	public void notifyMonitor()
	{
		Iterator<IFSimulationMonitor> iterator = monitors.iterator();
		while(iterator.hasNext())
		{
			IFSimulationMonitor agent = 	iterator.next();
			agent.udpateMonitor(this);
		}
	}
	public void addMonitor(IFSimulationMonitor monitor)
	{
		monitors.add(monitor);
	}
	
	

}
