package sim.model.impl.stoage.atc.impl;

import sim.model.core.SimMain;
import sim.model.core.SimulationInfo;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCMove2;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.block.Slot;
import sim.model.impl.stoage.block.TP;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * @author archehyun
 * @since 20200528
 *
 */
public class CrossATC2 extends SimATC {

	public CrossATC2(SimMain main, String simName, int id, int blockID, float row, float bay, float width, float height, int type) {
		super(main, simName, id, blockID, row, bay, width, height, type);

		move = new ATCMove2(this);

		if (type == CrossATC2.SEA_SIDE) {
			move.setSeaLandType(-1);
		} else {
			move.setSeaLandType(1);
		}

		logger.debug("create:" + simName);
	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initPosition.x = BlockManager.getInstance().getBlock(blockID).getX();

		initPosition.y = (getInitBay()) * (BlockManager.conH + BlockManager.hGap);

		logger.info("init Point:" + initPosition);

	}


	@Override
	public void process(SimNode node) throws InterruptedException {

		activeEvent = (StoageEvent) node;

		logger.info("set order");

		this.setInOutType((int) activeEvent.get("inOutType"));

		activeEvent.workStep = 1;

		logger.info("destination:" + this.getDestination().x + ", " + this.getLocation().x);

		setBusy();
	}



	private final int STEP1 = 1;

	private final int STEP2 = 2;

	private final int STEP3 = 3;

	private final int STEP4 = 4;

	private final int STEP5 = 5;

	private final int STEP6 = 6;

	private final int hoistUpWork = 8;

	//private SimEvent event2;


	public void setATCMove(boolean move) {
		this.move.setBayMove(move);
		this.move.setRowMove(move);
	}


	/**
	 * @param workStep
	 */
	private void inboundWork(int workStep) {

		switch (workStep) {

		case STEP1:
			logger.debug(this.getAtcID() + " move tp:" + this.getInitRow() + ", " + this.getInitBay());
			//MOVE TP

			this.setDestinationLocation(this.getInitRow(), this.getInitBay());
			move.setBayMove(true);
			move.setRowMove(true);
			activeEvent.workStep++;


			break;

		case STEP2:
			if (isArrival()) {

				if (hoistWorkTime < getHoistTime()) {
					hoist = true;
					hoistWorkTime++;

				} else {
					//hoist down work end and go to next step
					activeEvent.workStep++;
				}
			}


			break;
		case STEP3:

			if (hoistWorkTime > 0) {
				hoist = true;
				hoistWorkTime--;

			} else {
				hoist = false;
				logger.debug("arriva tp and move:" + activeEvent.workStep + "," + activeEvent.getX() + ", y:" + activeEvent.getY());
				activeEvent.workStep++; // go to destination step
				this.setDestinationLocation((int) activeEvent.get("row"), (int) activeEvent.get("bay"));
				this.setLoad(true);
				logger.debug("workStep:" + activeEvent.workStep);

				move.setBayMove(true);
				move.setRowMove(true);
			}

			break;
		case STEP4:

			if (isArrival()) {

				activeEvent.workStep++;
				logger.debug("arrival destination: next:" + activeEvent.workStep);
			}
			/*else {
				System.out.println("move");
				//hoist up work
			}*/

			break;
		case STEP5:

			if (hoistWorkTime < getHoistTime()) {
				hoist = true;
				hoistWorkTime += BlockManager.blockHRate;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {

				activeEvent.workStep++;
			}
			break;
		case STEP6:
			if (hoistWorkTime > 0) {
				hoist = true;
				hoistWorkTime -= BlockManager.blockHRate;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {

				hoist = false;
				this.setLoad(false);
				hoistWorkTime = 0;
				Slot slot = (Slot) activeEvent.get("slot");

				logger.info("inbound end");
				blockManager.setEmpty(getBlockID(), slot, false);
				//				slot.setUsed(false);
				//jobManager.release(getBlockID(), getLocationType(), (int) activeEvent.get("tpindex"), activeEvent);
				this.plusWorkCount();
				TP tp = (TP) activeEvent.get("tp");
				activeEvent.put("endTime", time);
				tp.release();
				this.activeEvent = null;
				this.release();


			}

			break;

		default:

			break;
		}
		this.notifyMonitor(activeEvent);
	}



	private void outboundWork(int workStep) {
		switch (workStep) {
		case STEP1:

			logger.debug(this.getAtcID() + " move destination:" + (int) activeEvent.get("row") + ", " + (int) activeEvent.get("bay"));
			//MOVE TP

			this.setDestinationLocation((int) activeEvent.get("row"), (int) activeEvent.get("bay"));
			move.setBayMove(true);
			move.setRowMove(true);
			activeEvent.workStep++;
			break;
		case STEP2:
			if (isArrival()) {

				if (hoistWorkTime < getHoistTime()) {
					hoist = true;
					hoistWorkTime++;

					//					System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
				} else {
					//hoist down work end and go to next step
					activeEvent.workStep++;
				}
			}

			break;
		case STEP3:

			if (hoistWorkTime > 0) {
				hoist = true;
				hoistWorkTime--;

				//System.out.println("hoist up work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {
				hoist = false;
				logger.debug("arriva tp and move:" + activeEvent.workStep + "," + activeEvent.getX() + ", y:" + activeEvent.getY());
				activeEvent.workStep++; // go to destination step
				this.setDestinationLocation(this.getInitRow(), this.getInitBay());
				this.setLoad(true);
				logger.debug("workStep:" + activeEvent.workStep);

				move.setBayMove(true);
				move.setRowMove(true);
			}

			break;
		case STEP4:

			if (isArrival()) {

				activeEvent.workStep++;
				logger.debug("arrival destination: next:" + activeEvent.workStep);
			}
			/*else {
				System.out.println("move");
				//hoist up work
			}*/

			break;
		case STEP5:

			if (hoistWorkTime < getHoistTime()) {
				hoist = true;
				hoistWorkTime++;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {

				activeEvent.workStep++;
			}
			break;
		case STEP6:
			if (hoistWorkTime > 0) {
				hoist = true;
				hoistWorkTime = -BlockManager.blockHRate;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {
				logger.debug("work end");
				hoist = false;
				this.setLoad(false);
				hoistWorkTime = 0;
				Slot slot = (Slot) activeEvent.get("slot");
				slot.setUsed(false);
				//slot.getBlock().setEmpty(slot, true);

				logger.info("outbound end");
				blockManager.setEmpty(getBlockID(), slot, true);
				//jobManager.release(getBlockID(), getLocationType(), activeEvent.getTpIndex(), activeEvent);
				TP tp = (TP) activeEvent.get("tp");
				tp.release();
				activeEvent.put("endTime", time);


				this.plusWorkCount();
				this.activeEvent = null;



				this.release();
			}

			break;

		default:
			break;
		}
		this.notifyMonitor(activeEvent);

	}


	@Override
	public void update() {

		SimulationInfo simInfo = this.main.getSimulatinoInfo();
		this.deltaTime = (double) simInfo.get("delta");
		this.time = (long) simInfo.get("time");
		if (activeEvent != null) {

			//	System.out.println("update:" + activeEvent.getInOutType());

			activeEvent.put("atcItem", this);
			switch ((int) activeEvent.get("inOutType")) {
			case StoageEvent.INBOUND:
				inboundWork(activeEvent.workStep);
				break;
			case StoageEvent.OUTBOUND:
				outboundWork(activeEvent.workStep);
				break;

			default:
				break;
			}
		}

		move.update(deltaTime);

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}


}
