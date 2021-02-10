package sim.model.impl.stoage.atc.impl;

import java.util.Timer;
import java.util.TimerTask;

import sim.model.core.SimEvent;
import sim.model.core.SimMain;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCLandSideMoveY;
import sim.model.impl.stoage.atc.move.ATCMoveX;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.block.Slot;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * cross type atc
 *@deprecated
 * @author archehyun
 *
 */
@Deprecated
public class CrossLandSideATC extends SimATC {

	MyTimer time;

	public CrossLandSideATC(SimMain main, String simName, int atcID, int blockID, float x, float y, float width, float height, int type) {
		super(main, simName, atcID, blockID, x, y, width, height, type);
		moveXX = new ATCMoveX(main, simName + "_x", this);
		moveYY = new ATCLandSideMoveY(main, simName + "_y", this);

		time = new MyTimer("");

		notifyMonitor("create landSide atc:" + atcID);

	}

	class ScheduledJob extends TimerTask {

		   @Override
		public void run() {
			if (lastX == CrossLandSideATC.this.getX())
			{
				StoageEvent node = new StoageEvent(0, SimEvent.TYPE_COMMAND);
				Slot slot = blockManager.getSlot(0, 24, 1, 1);


				node.setSlot(slot);
				node.setCommandType(SimEvent.COMMAND_MOVE);
				node.setATCID(SimATC.LAND_SIDE);

				node.setX(1);

				node.setY(25);
				CrossLandSideATC.this.append(node);
			}
		   }
	}

	int lastX;
	@Override
	public void notifySimMessage(SimModel model) {
		lastX = this.getX();
		//time.schedule(6000);

	}

	private void hoistWork() throws InterruptedException {
		Thread.sleep(500);
	}

	/**
	 * @param node
	 * @throws InterruptedException
	 *//*
		public void work(SimNode node) throws InterruptedException {
		StoageEvent job = (StoageEvent) node;

		moveYY.setDestination((BlockManager.conH + BlockManager.hGap) * job.getY());

		switch (job.orderType) {

		case StoageEvent.INBOUND:
			jobManager.release("crossLand");
			moveTP(job);

			setLoad(true);

			moveDestination(job);

			hoistWork();

			setLoad(false);

			job.getSlot().setUsed(false);

			job.getSlot().getBlock().setEmpty(job.getSlot(), false);



			break;
		case StoageEvent.OUTBOUND:
			jobManager.release("crossLand");
			moveDestination(job);
			hoistWork();
			setLoad(true);
			job.getSlot().getBlock().setEmpty(job.getSlot(), true);
			job.getSlot().setUsed(false);
			jobManager = JobManager.getInstance();
			moveTP(job);
			hoistWork();
			setLoad(false);

			break;
		case StoageEvent.MOVE:
			moveDestination(job);
			break;

		default:
			break;
		}
		}
		*/
	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initPosition.x = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initPosition.y = getInitBay() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin + BlockManager.conH;

	}

	class MyTimer {

		ScheduledJob job = new ScheduledJob();
		//Timer timer;
		public MyTimer(String string) {
			//timer = new Timer();
		}

		public void schedule(TimerTask task, long time) {

			try {
				job = new ScheduledJob();
				Timer timer = new Timer();
				timer.schedule(job, time);
			} catch (Exception e) {
				e.printStackTrace();

			}

		}


		private boolean isStarted = true;
	}


	@Override
	public void notifySimMessage() {
		lastX = this.getX();
		//time.schedule( 5000);
	}

	@Override
	public void moveTP(SimEvent job) {
		try {
			moveYY.moveTP(job);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void plusY() throws InterruptedException {


		super.plusY();
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void minusY() throws InterruptedException {

		/*if (!atcJobManager.overlapRectangles(this)) {
			atcJobManager.setMove(true);
		}*/

		super.minusY();
	}

	@Override
	public void moveDestination(SimEvent job) {
		try {
			moveYY.moveDestination(job);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void process(SimNode node) {


		SimEvent atcJob = (SimEvent) node;
		plusWorkCount();
		StoageEvent event = (StoageEvent) atcJob;
		moveXX.append(node);
		moveYY.append(node);
		;

		notifyMonitor("land:process:" + this.getName() + "initY:" + this.getInitBay() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;
	}

	//TODO : SINGLE ATC
	//TODO : TWIN ATC
	//TODO : CROSS ATC
	//TODO :

}

