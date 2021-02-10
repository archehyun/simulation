package sim.model.impl.stoage.atc.impl;

import sim.model.core.SimEvent;
import sim.model.core.SimMain;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCLandSideMoveY;
import sim.model.impl.stoage.atc.move.ATCMoveX;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * @deprecated
 * @author archehyun
 *
 */
@Deprecated
public class TwinLandSideATC extends SimATC {



	public TwinLandSideATC(SimMain main, String simName, int atcID, int blockID, float x, float y, float width, float height, int type) {
		super(main, simName, atcID, blockID, x, y, width, height, type);
		moveXX = new ATCMoveX(main, simName + "_x", this);
		moveYY = new ATCLandSideMoveY(main, simName + "_y", this);

		notifyMonitor("create landSide atc:" + atcID);

	}
	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initPosition.x = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initPosition.y = getInitBay() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin + BlockManager.conH;

		//position.y = initPosition.y;

	}


	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveTP(SimEvent job) {
		// TODO Auto-generated method stub

	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void plusY() throws InterruptedException {

		atcJobManager.overlapRectangles(this);

		super.plusY();
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void minusY() throws InterruptedException {

		atcJobManager.overlapRectangles(this);

		super.minusY();
	}

	@Override
	public void moveDestination(SimEvent job) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(SimNode node) {

		SimEvent atcJob = (SimEvent) node;

		StoageEvent event = (StoageEvent) atcJob;
		System.out.println("process:" + event.orderType);
		moveXX.append(node);

		moveYY.append(node);

		setBusy();



		notifyMonitor("land:process:" + this.getName() + "initY:" + this.getInitBay() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;
	}

	//TODO : SINGLE ATC
	//TODO : TWIN ATC
	//TODO : CROSS ATC
	//TODO :



}