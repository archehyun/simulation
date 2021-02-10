package sim.model.impl.stoage.commom;

import java.util.LinkedList;
import java.util.Queue;

import sim.model.queue.SimQueue;

public class TPWork implements Runnable {

	int blockID;

	private SimQueue queue;
	Queue<StoageEvent> tp;

	public TPWork(int blockID) {
		this.blockID = blockID;
		tp = new LinkedList<StoageEvent>();

	}

	public void append(StoageEvent event) {
		tp.add(event);
	}

	@Override
	public void run() {
		while (true) {
			StoageEvent event = (StoageEvent) queue.poll();
		}
	}

	class TPOrderQueue {

		int tp[];

		int queueSize;


		public TPOrderQueue(int queueSize) {
			this.queueSize = queueSize;
			tp = new int[queueSize];
		}

		public int getBusy() {
			int total = 0;
			for (int i = 0; i < tp.length; i++) {
				total += tp[i];
			}
			return total;
		}

		public int getEmptyTPIndex() {
			for (int i = 0; i < tp.length; i++) {
				if (tp[i] == 0)
					return i;
			}
			return -1;
		}

		int activeOrder = 0;

		public boolean flag = false;

		public synchronized int poll() {

			/**/
			int emptyIndex = 0;
			while ((emptyIndex = getEmptyTPIndex()) == -1) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			tp[emptyIndex] = 1;
			//	logger.info("emptyIndex:" + tpOrder.blockID + "-" + tpOrder.seaLandType + "-" + emptyIndex);
			return emptyIndex;

		}

		public synchronized void release(int index) {
			tp[index] = 0;
			notify();
		}

		public int[] getTP() {
			// TODO Auto-generated method stub
			return tp;
		}

	}

}
