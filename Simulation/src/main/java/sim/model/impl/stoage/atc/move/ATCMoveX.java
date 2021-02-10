package sim.model.impl.stoage.atc.move;

import sim.model.core.SimEvent;
import sim.model.core.SimMain;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * @author LDCC
 *
 */
public class ATCMoveX extends ATCMove {




	public ATCMoveX(SimMain main, String simName, SimATC atc) {
		super(main, simName, atc);
	}


	@Override
	public void notifySimMessage() {
		atc.arrival(atc.getX(), atc.getY());
	}

	@Override
	public void moveTP(SimEvent job) {
		do {
			if (atc.getInitRow() > atc.getX()) {
				atc.plusX();
			} else if (atc.getInitRow() < atc.getX()) {
				atc.minusX();
			}
			try {
				Thread.sleep((long) atc.getSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			notifyMonitor(this.getName() + ", des:" + getDestination() + ",cuX:" + atc.getX() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getX() && isFlag());
	}

	@Override
	public void moveDestination(SimEvent job) {
		do {
			if (getDestination() > atc.getX()) {
				atc.plusX();
			} else if (getDestination() < atc.getX()) {

				atc.minusX();
			}
			try {
				Thread.sleep((long) atc.getSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			notifyMonitor(this.getName() + ", des:" + getDestination() + ",cuX:" + atc.getX() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getX() && isFlag());
	}

	@Override
	public void process(SimNode node) {
		StoageEvent job = (StoageEvent) node;

		setDestination((BlockManager.conW + BlockManager.wGap) * job.getX() + atc.getInitXpointOnWindows());

		moveDestination(job);
	}



}
