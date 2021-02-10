package sim.model.core;

import java.util.Iterator;

import org.apache.log4j.Logger;

import sim.model.queue.SimNode;
import sim.view.framework.IFMonitor;

/**
 * @author 占쏙옙창占쏙옙
 *
 */
public abstract class SimModelManager extends SimModel{

	protected Logger logger = Logger.getLogger(this.getClass().getName());

	protected SimNode node;

	protected SimModelManager(SimMain main, String simName) {
		super(main, simName);

	}

	public SimModelManager(String simName) {
		super(simName);
	}

	/* (non-Javadoc)
	 * @see sim.model.SimModel#addMonitor(sim.view.IFMonitor)
	 */
	@Override
	public void addMonitor(IFMonitor monitor)
	{
		super.addMonitor(monitor);
		Iterator<SimModel> iter = list.iterator();
		while(iter.hasNext())
		{
			SimModel model= iter.next();
			model.addMonitor(monitor);
		}

	}

	/*
	 * 占시뮬뤄옙占싱쇽옙 占쏙옙占쏙옙
	 * 占쏙옙占쏙옙 占쏙옙占� 占쏙옙占쏙옙
	 *
	 */
	@Override
	public void simStart()
	{
		super.simStart();
		Iterator<SimModel> iter = list.iterator();
		while(iter.hasNext())
		{
			SimModel model= iter.next();
			model.simStart();
		}
	}

	/**
	 *  占시뮬뤄옙占싱쇽옙 占쏙옙占쏙옙
	 */
	@Override
	public void simStop()
	{
		super.simStop();
		Iterator<SimModel> iter = list.iterator();
		while(iter.hasNext())
		{
			SimModel model= iter.next();
			model.simStop();
		}
	}

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifySimMessage() {

	}





}