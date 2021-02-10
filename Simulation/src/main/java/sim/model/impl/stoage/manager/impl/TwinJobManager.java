package sim.model.impl.stoage.manager.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.queue.SimNode;

/**
 * @author archehyun
 *
 */
public class TwinJobManager extends ATCJobManager {

	public TwinJobManager(String simName) {
		super(simName);
	}

	private void commandProcess(SimEvent event) {

		Iterator<SimModel> iter = list.iterator();

		switch (event.getCommandType()) {
		case SimEvent.COMMAND_UPDATE_SPEED:
			int speed = (int) event.get("speed");

			while (iter.hasNext()) {
				SimATC model = (SimATC) iter.next();
				model.setSpeed(speed);
			}
			break;
		case SimEvent.COMMAND_MOVE:

			while (iter.hasNext()) {
				SimATC model = (SimATC) iter.next();

				StoageEvent ee = (StoageEvent) event;

				int blockId = ((StoageEvent) atcJob).getSlot().getBlock().getBlockID();

				if ((model.getAtcID() % 100) == blockId) {

					if (model.getAtcID() == ee.getATCID()) {
						ee.orderType = StoageEvent.COMMAND_MOVE;
						model.append(ee);
						logger.debug("append atc order:" + atcJob.getSimName());
					}
				}
			}
			break;

		default:
			break;
		}

	}
	//SimEvent atcJob;

	private void divied(int blockID, SimEvent atcJob) {
		Iterator<SimModel> iter = list.iterator();

		while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();

			if ((model.getAtcID() % 100) == blockID) {

				if (model.getAtcID() / 100 == 1) {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() < 12) {

						model.append(atcJob);

						break;
					}
				} else {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() > 12) {

						model.append(atcJob);
						break;
					}
				}
			}
		}
	}

	public void minWorkACT(int blockID, SimEvent atcJob) {

		Iterator<SimModel> iter = list.iterator();

		List<SimATC> li = new LinkedList<SimATC>();

		while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();
			if ((model.getAtcID() % 100) == blockID) {
				li.add(model);
			}
		}

		SimATC first = li.get(0);
		for (int i = 1; i < li.size(); i++) {
			SimATC temp = li.get(i);
			if (first.getWorkCount() > temp.getWorkCount()) {
				first = temp;
			}
		}

		first.append(atcJob);
	}

	/**
	 *
	 * 理��� WORK ATC ����
	 *
	 * @param blockID
	 * @param atcJob
	 */
	private void sideWorkACT(SimEvent atcJob) {

		StoageEvent event = (StoageEvent) atcJob;

		event.get("bay");

		synchronized (list) {
			try {
				for (int i = 0; i < list.size(); i++) {
					SimATC temp = (SimATC) list.get(i);

					if (temp.getLocationType() == (int) event.get("seaLandType")) {

						//System.out.println("set order block:" + this.getBlockID() + ", locationType:" + temp.getLocationType() + ",atcID:" + temp.getAtcID() + ", " + event.getBlockID() + ", " + event.getBay());
						temp.append(atcJob);
						return;
					} else {
						//System.out.println("not set order" + temp.getLocationType() + "," + event.getSeaLandType());
					}
				}
				System.out.println("not set:jobid:" + event.getJobID() + ",type:" + event.getSeaLandType() + ",block:" + event.getBlockID());
				//System.out.println("put order:" + first.getAtcID() + ", count:" + first.getWorkCount() + ", " + li.get(0).getWorkCount() + "," + li.get(1).getWorkCount() + ",busy:" + this.getBusyCount());

			} catch (Exception e) {
				System.err.println(list.size());
			}
		}

	}

	private void setATCWork(SimEvent atcJob) {
		sideWorkACT(atcJob);
		//divied(blockID, atcJob);

		//minWorkACT(blockID, atcJob);
	}

	private void orderProcess(SimEvent atcJob) {


		atcJob.put("atc", list);

		setATCWork(atcJob);

		if (atcJob == null)
			System.err.println("error");
		this.notifyMonitor(atcJob);

	}


	SimEvent atcJob;

	//
	@Override
	public void process(SimNode node) {

		//		this.node = node;

		SimEvent event = (SimEvent) node;
		switch (event.getEventType()) {

		case SimEvent.TYPE_COMMAND:
			commandProcess(event);
			break;
		case SimEvent.TYPE_ORDER:
			orderProcess(event);
			break;

		default:
			break;
		}

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "twin";
	}

}
