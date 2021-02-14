package sim.model.core;

import java.util.Iterator;
import java.util.Random;

import sim.model.queue.SimNode;

/**
 * @author LDCC
 *
 */
public abstract class Create extends SimModel {

	protected boolean start = false;

	protected SimEvent event;

	public Create(SimMain main, String simName) {
		super(main, simName);
		// TODO Auto-generated constructor stub
	}

	protected int interval;

	private int count;

	protected int timeStep = 1;

	public abstract int genterateIntegerval();

	public abstract SimEvent createEvent();

	Random rn = new Random();

	public abstract boolean isMakeEvnet();

	protected void makeEvent() {
		interval -= timeStep;

		if (isMakeEvnet()) {
			event = createEvent();
			notifySimMessage();
		}
		if (interval < 0) {
			interval = genterateIntegerval();

		}

	}

	public void start() {
		start = true;
	}

	public void stop() {
		start = false;
	}

	@Override
	public void notifySimMessage() {

		if (event == null)
			return;
		if (start) {
			Iterator<SimModel> iter = this.list.iterator();

			event.setEventMessage("order");
			while (iter.hasNext()) {
				iter.next().append(event);
			}
		}
	}

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(SimNode node) throws InterruptedException {
		// TODO Auto-generated method stub

	}


}
