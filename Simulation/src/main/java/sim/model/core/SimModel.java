package sim.model.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import sim.model.queue.NomalQueue;
import sim.model.queue.SimNode;
import sim.model.queue.SimQueue;
import sim.view.framework.IFMonitor;

/**
 * Simulation Model class
 *
 * @author archehyun
 * @since 2020.05.17
 *
 */
public abstract class SimModel implements IFSimModel, Runnable {

	public Logger logger = Logger.getLogger(getClass());

	/**
	 *
	 */
	protected List<SimModel> list;

	protected SimMain main;

	public void addSimModel(SimModel model) {
		list.add(model);
	}

	int id;

	/**
	 *
	 */
	protected long time;

	public long getTime() {
		return time;
	}


	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 *
	 */
	protected List<IFMonitor> monitors;

	/**
	 *
	 */
	protected SimEvent eventMessage;

	/**
	 * queue
	 */
	private SimQueue queue;

	/**
	 * main thread
	 */
	private Thread thread;

	public SimQueue getQueue() {
		return queue;
	}

	/**
	 * simulation Object Name
	 */
	protected String name;

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setSimName(String name) {
		this.name = name;
	}

	/**
	 * thread flag
	 */
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {

		this.flag = flag;
	}

	public SimModel(String simName) {
		this.name = simName;
		this.queue = new NomalQueue();
		this.list = new LinkedList<SimModel>();
		this.monitors = new LinkedList<>();

		logger.debug("init:" + simName);

	}


	/**
	 * Init
	 * @param main
	 * @param simName
	 */
	public SimModel(SimMain main, String simName)
	{
		this.main = main;
		this.name = simName;
		this.queue = new NomalQueue();
		this.list = new LinkedList<SimModel>();
		this.monitors = new LinkedList<>();

		logger.debug("init:" + simName);

	}

	public SimModel(SimMain main, String simName, int id) {
		this(main, simName);
		this.setId(id);
		logger.debug("init:" + simName);

	}


	/**
	 * @param node
	 */
	public void append(SimNode node)
	{
		queue.append(node);
	}

	public void clear() {
		list.clear();
	}


	/**
	 *
	 *
	 * @param message
	 */
	public void notifyMonitor(String message)
	{
		Iterator<IFMonitor> iter = monitors.iterator();

		eventMessage = new SimEvent(0);

		eventMessage.setSimName(getName());

		eventMessage.setEventMessage(message);

		while(iter.hasNext())
		{
			IFMonitor monitor= iter.next();
			monitor.updateMonitor(eventMessage);
		}
	}

	/**
	 *
	 *
	 *
	 * @param message
	 */
		public void notifyMonitor(SimEvent message) {

		if (monitors == null || message == null)
			return;

		synchronized (message) {
			Iterator<IFMonitor> iter = monitors.iterator();
			message.setSimName(getName());
			while (iter.hasNext()) {
				IFMonitor monitor = iter.next();
				monitor.updateMonitor(message);
			}

		}

	}

	/**
	 *
	 */
	public abstract void notifySimMessage();

	/**
	 *
	 */
	public abstract void notifySimMessage(SimModel model);


	/**
	 * @param node
	 * @throws InterruptedException
	 */
	public abstract void process(SimNode node) throws InterruptedException;

	@Override
	public void run() {
		while(isFlag())
		{
			SimNode node = queue.poll();
			try {
				process(node);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			notifySimMessage();
		}
	}

	/**
	 * start Simulation
	 */
	public void simStart()
	{
		if(isFlag()==false)
		{
			notifyMonitor("log:start:"+name+",flag:"+isFlag());

			this.setFlag(true);

			thread = new Thread(this, this.getName());
			thread.start();
		}
	}

	/**
	 *stop simulation
	 */
	public void simStop()
	{
		setFlag(false);
		thread = null;
	}
	/**
	 *
	 */
	public void simPluse()
	{
		setFlag(false);
	}
	public void addMonitor(IFMonitor monitor)
	{
		this.monitors.add(monitor);

	}



	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
