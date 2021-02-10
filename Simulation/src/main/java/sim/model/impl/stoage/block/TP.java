package sim.model.impl.stoage.block;

import java.util.Random;

import org.apache.log4j.Logger;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.commom.JobManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.impl.stoage.manager.ATCManager;
import sim.model.queue.SimQueue;

/**
 * @author archehyun
 *
 */
public class TP implements Runnable {

	protected Logger logger = Logger.getLogger(this.getClass().getName());

	Thread thread;

	boolean start = false;

	int blockID;

	int seaLandType;

	int index;

	SimQueue queue;

	public void start() {

		if (thread == null) {
			start = true;

			thread = new Thread(this);
			thread.setName("tp-" + blockID + "-" + seaLandType + "-" + index);
			thread.start();
		}

	}

	public SimQueue getQueue() {
		return queue;
	}

	public void setQueue(SimQueue queue) {
		this.queue = queue;
	}

	boolean isIdle = true;

	Random rn = new Random();

	public TP(int blockID, int seaLandType, int index) {

		this.blockID = blockID;
		this.seaLandType = seaLandType;
		this.index = index;

	}

	/**
	 *ATC State
	 *1. busy
	 *2. none busy
	 */
	public synchronized void setBusy() {

		this.isIdle = false;

		while (!isIdle) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getUse() {
		if (isIdle) {
			return 0;
		} else {
			return 1;
		}
	}

	ATCManager manager = ATCManager.getInstance();

	public synchronized void process() {
		SimEvent node;
		while ((node = (SimEvent) queue.poll()) == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int n2 = rn.nextInt(10);
		Slot slot = null;
		if (n2 > 0) {
			node.put("inOutType", StoageEvent.OUTBOUND);
			if (this.seaLandType == StoageEvent.SEA) {
				slot = BlockManager.getInstance().selectFilledUpperSlot(blockID, 0, 14);
			} else {
				slot = BlockManager.getInstance().selectFilledUpperSlot(blockID, 15, 24);
			}

		} else {
			node.put("inOutType", StoageEvent.INBOUND);

			if (this.seaLandType == StoageEvent.SEA) {
				slot = BlockManager.getInstance().selectEmptySlot(blockID, 0, 14);
			} else {
				slot = BlockManager.getInstance().selectEmptySlot(blockID, 15, 24);
			}

		}
		node.put("slot", slot);
		node.put("bay", slot.getBayIndex());
		node.put("row", slot.getRowIndex());
		node.put("tier", slot.getTierIndex());
		node.put("tp", TP.this);

		manager.getATCManager((int) node.get("blockID")).append(node);
		node.put("tpindex", index);

		logger.debug("send order:seaLand:" + node.get("seaLandType"));

		setBusy();
		JobManager.getInstance().notifyMonitor(node);

	}

	@Override
	public void run()
	{
		while (start) {
			process();

		}
	}


	public synchronized void release() {
		this.isIdle = true;

		notifyAll();
	}

	public synchronized void append(SimEvent event) {
		this.queue.append(event);
	}

}
