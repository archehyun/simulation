package sim.model.impl.stoage.commom;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import sim.model.core.SimEvent;
import sim.model.core.SimMain;
import sim.model.core.SimModelManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.block.Slot;
import sim.model.impl.stoage.manager.ATCManager;
import sim.model.queue.NomalQueue;
import sim.model.queue.SimNode;
import sim.model.queue.SimQueue;

/**
 *
 * order manage class
 *
 * generate work
 *
 * create WorkOrderGenerate by block
 *
 * @author archehyun
 *
 */
public class JobManager extends SimModelManager{


	WorkOrderGenerate generate;

	TPWorkOrderGenegete tpWorkGenerate[][];

	public TPWorkOrderGenegete[][] getTpWorkGenerate() {
		return tpWorkGenerate;
	}

	public int[] getTP(int blockID, int seaLandType) {

		return tpWorkGenerate[blockID][seaLandType].getTP();

	}

	TPWorkOrderGenegete tpWorkGenerateSea[];

	TPWorkOrderGenegete tpWorkGenerateLand[];

	private static JobManager instance;

	private JobID jobID = JobID.getInstance();

	ATCManager manager = ATCManager.getInstance();

	BlockManager blockManager = BlockManager.getInstance();

	List orderList;

	public static JobManager getInstance() {
		if (instance == null)
			instance = new JobManager("jobManager");
		return instance;

	}

	public JobManager(String simName) {
		super(simName);

		blockManager.setBlockCount(main, BlockManager.block);

		this.simStart();

	}

	public JobManager(SimMain main, String simName) {
		this(simName);

	}

	public void init() {

		tpWorkGenerate = new TPWorkOrderGenegete[blockManager.block][2];

		for (int i = 0; i < blockManager.block; i++) {
			tpWorkGenerate[i][0] = new TPWorkOrderGenegete(StoageEvent.SEA, i, 4);
			tpWorkGenerate[i][1] = new TPWorkOrderGenegete(StoageEvent.LAND, i, 4);
		}

	}

	public void release(int block, int seaLandType, int tpIndex, SimEvent event) {
		tpWorkGenerate[block][seaLandType].release(tpIndex);
		StoageEvent ev = (StoageEvent) event;
		ev.setEndTime(this.getTime());
		notifyMonitor(event);

	}

	public Slot selectSlot(int blockID) {
		return blockManager.selectEmptySlot(blockID);
	}

	@Override
	public void notifySimMessage() {

	}

	/**
	 * @param name
	 */
	public synchronized void release(String name)
	{
		generate.release();
	}

	/**
	 * put order
	 */
	public void putOrder() {
		try {
			generate.putOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void putTPQueue(SimEvent event) {

		int blockID = (int) event.get("blockID");

		blockManager.getBlock(blockID).append(event);

		logger.debug("put queue");

	}

	TPQueue2[][] queue;

	@Override
	public void process(SimNode node) {

		SimEvent event = (SimEvent) node;

		try {

		if(event.getEventMessage().equals("simstart"))
		{


		}
		else if(event.getEventMessage().equals("simstop"))
		{
			//generate.simStop();
			for (int i = 0; i < tpWorkGenerate.length; i++) {
				for (int j = 0; j < tpWorkGenerate[i].length; j++) {
					tpWorkGenerate[i][j].simStop();
				}
			}
		}
		else if (event.getEventMessage().equals("order")) {

			putTPQueue(event);
		}
		node = null;

		} catch (Exception e) {

		}
	}

	class TPOrderQueue {

		int tp[];
		int queueSize;

		TPWorkOrderGenegete tpOrder;

		public TPOrderQueue(int queueSize, TPWorkOrderGenegete tpOrder) {
			this.queueSize = queueSize;
			tp = new int[queueSize];
			this.tpOrder = tpOrder;
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
			int emptyIndex=0;
			while ((emptyIndex=getEmptyTPIndex()) == -1) {
				try {
					wait();
				} catch (InterruptedException e) {
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

	/**
	 * @author archehyun
	 *
	 */
	class TPQueue2 extends NomalQueue {

		int blockID;

		int seaLandType;

		public TPQueue2(int blockID, int seaLandType) {
			this.blockID = blockID;
			this.seaLandType = seaLandType;
		}

	}

	class TPQueue extends SimQueue {

		StoageEvent events[];

		int tp[];


		/**
		 *
		 * tp �� ���� â��
		 * tp ���� 3��
		 *
		 * @param tpCount
		 */
		public TPQueue(int tpCount, int blockID, int locationType) {
			tp = new int[tpCount];
			events = new StoageEvent[tpCount];
		}

		@Override
		public SimEvent poll()
		{
			synchronized (tp) {
				int total=0;
				for(int i=0;i<tp.length;i++)
				{
					total+=tp[i];
				}

				StoageEvent event;
				while (total == 0 || (event = (StoageEvent) super.poll()) != null)
				{
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				int tpIndex = -1;
				for(int i=0;i<tp.length;i++)
				{
					if(tp[i]>0)
					{
						tpIndex = i;
						break;
					}
				}

				event.setTPIndex(tpIndex);
				return event;

			}

		}

		public void release(int tpIndex) {

			synchronized (tp) {
				tp[tpIndex] = 0;
				notify();

			}

		}
	}


	/**
	 *
	 * ��� ��ĸ��� ����
	 * @author LDCC
	 *
	 */
	class TPWorkOrderGenegete implements Runnable {


		private int blockID;

		private int seaLandType;

		private int timer = 0;

		boolean flag = false;

		Thread thread;

		public TPWorkOrderGenegete(int seaLandType, int blockID, int tpCount) {
			this.seaLandType = seaLandType;
			this.blockID = blockID;
			tpqueue = new TPOrderQueue(tpCount, TPWorkOrderGenegete.this);
		}

		/**
		 * @return
		 */
		public int[] getTP() {
			return tpqueue.getTP();
		}

		Random rn = new Random();

		public synchronized void release(int tpIndex) {
			tpqueue.release(tpIndex);
		}

		/**
		 * @param tpIndex
		 */
		private void create(int tpIndex) {

			synchronized (jobID) {
				StoageEvent node = new StoageEvent(jobID.getID(), SimEvent.TYPE_ORDER);
				node.setEventType(SimEvent.TYPE_ORDER);

				node.setSeaLandType(seaLandType);

				node.setStartTime(JobManager.this.getTime());
				node.put("startTime", JobManager.this.getTime());
				logger.debug("generate order: " + node.getBlockID() + "-" + node.getSeaLandType() + "-jobID:" + jobID.getID() + ", time:" + JobManager.this.getTime());

				manager.getATCManager((int) node.get("blockID")).append(node);

				jobID.update();

				JobManager.this.notifyMonitor(node);
			}
		}

		TPOrderQueue tpqueue;

		@Override
		public void run() {


			while (flag) {
				int tpIndex = tpqueue.poll();

				//logger.info("arrival tp at " + blockID + "-" + this.seaLandType + "-" + tpIndex);
				create(tpIndex);
				tpqueue.flag = true;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		}

		public void simStart() {
			if (!flag) {

				flag = true;
				thread = new Thread(this);
				jobID = JobID.getInstance();
				thread.start();
			}
		}

		public void simStop() {
			flag = false;
			thread = null;
			//jobID = 0;
		}
	}

	/**
	 * Generate work order class
	 * @author LDCC
	 *
	 */
	class WorkOrderGenerate implements Runnable {

		Thread thread;

		boolean ready = false;

		boolean flag = false;

		BlockManager blockManager = BlockManager.getInstance();

		public synchronized void release() {
			ready = flag;

			minusActiveOrder();


		}

		private synchronized void plusActiveOrder() {
			activeOrderCount++;

			notifyAll();
		}

		private synchronized void minusActiveOrder() {
			activeOrderCount--;
			notifyAll();
		}

		int activeOrderCount = 0;

		public synchronized void ready() {
			ready = false;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		private Random rn = new Random();

		/**
		 * @deprecated
		 */
		@Deprecated
		private void generateWork() {
			StoageEvent node = new StoageEvent(jobID.getID(), SimEvent.TYPE_ORDER);
			node.setEventType(SimEvent.TYPE_ORDER);

			int n1 = rn.nextInt(10);


			int seaLandType;
			if (n1 > 5) {
				seaLandType = StoageEvent.SEA;
			} else {
				seaLandType = StoageEvent.LAND;
			}

			int n2 = rn.nextInt(10);
			if (n2 > 5) {
			} else {
			}

			node.setSeaLandType(seaLandType);

			//node.setInOutType(inOutType);

			node.setBlockID(0);


			node.orderType = StoageEvent.OUTBOUND;


			try {

				Slot slot = selectSlot(node.getBlockID());

				slot.setUsed(true);

				node.setSlot(slot);

				node.setX(slot.getRowIndex());

				node.setY(slot.getBayIndex());

				manager.getATCManager(node.getBlockID()).append(node);

				plusActiveOrder();
				jobID.update();

			} catch (NullPointerException e) {
				System.err.println("error block:" + node.getBlockID());
				e.printStackTrace();
			}

		}




		/**
		 *
		 * 1. decide block number
		 * 2. decide container type
		 * 3. container type
		 *    3-1 Sea Inbound
		 *    3-2 Sea Outbound
		 *    3-3 Land Inbound
		 *    3-4 Land Outbound
		 * 4. decide slot location
		 *   4-1 when outbound : find exist container=> remarshalling container yard
		 *
		 *   4-2 when inbound : top slot
		 * 5.
		 * put order
		 * @throws Exception
		 */
		public void putOrder() throws Exception {

			StoageEvent node = new StoageEvent(jobID.getID(), SimEvent.TYPE_ORDER);

			node.setEventType(SimEvent.TYPE_ORDER);
			Slot slot;

			int n = rn.nextInt(10);

			int blockID = rn.nextInt(BlockManager.block);

			int containerCount = blockManager.getContainerCount(blockID);

			if (n > 3 && containerCount > 0) {
				slot = blockManager.selectFilledUpperSlot(blockID);
				notifyMonitor(Integer.toString(containerCount));

				node.orderType = StoageEvent.OUTBOUND;
				//node.setInOutType(StoageEvent.OUTBOUND);

			} else {
				slot = blockManager.selectEmptySlot(blockID);

				node.orderType = StoageEvent.INBOUND;
				//node.setInOutType(StoageEvent.INBOUND);
				notifyMonitor(Integer.toString(blockManager.getContainerCount(blockID)));
			}

			// slot null

			try {
				slot.setUsed(true);

				node.setSlot(slot);

				node.setX(slot.getRowIndex());

				node.setY(slot.getBayIndex());

				manager.getATCManager(blockID).append(node);

				plusActiveOrder();
				jobID.update();
				;


			} catch (NullPointerException e) {
				System.err.println("error block:" + blockID);
				e.printStackTrace();
			}
		}

		@Override
		public void run() {

			activeOrderCount = 0;

			while (flag) {
				try {
					ready();
					putOrder();


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void simStart() {
			if (!flag) {

				flag = true;
				thread = new Thread(this);
				thread.start();
			}
		}

		public void simStop()
		{
			flag = false;
			thread = null;
		}
	}


	/**
	 * 1. commandName : move(M), Work(W)
	 * 2. I-O Type : I, O
	 * 3. BAY : 0~25
	 * 4. ROW : 1~4
	 * 5. TIER : 1~6
	 * EX : W-I-15-1-1
	 * @param tf
	 * @throws UnparserableCommandException
	 */

	String pattern = "^([M]|[W])-([I]|[O])-([1-9]{1}|[0-1]{1}[0-9]{1}|[2]{1}[0-5]{1})-([0-5]{1})-([0-5]{1})$";

	StoageEvent commandNode;

	public void putCommand(String command) throws UnparserableCommandException, ArrayIndexOutOfBoundsException {

		Pattern.compile(command, Pattern.CASE_INSENSITIVE);

		boolean bol = Pattern.matches(pattern, command);
		if (bol == true) {

			String commands[] = command.split("-");

			String commandStr = commands[0];
			String inOutType = commands[1];
			int bay = Integer.parseInt(commands[2]);
			int row = Integer.parseInt(commands[3]);
			int tier = Integer.parseInt(commands[4]);

			Slot slot;

			if (commandStr.equals("W")) {

				for (int i = 0; i < BlockManager.block; i++) {
					StoageEvent node = new StoageEvent(jobID.getID(), SimEvent.TYPE_ORDER);

					slot = blockManager.getSlot(i, bay, row, tier);
					try {
						int containerCount = blockManager.getContainerCount(i);
						notifyMonitor(Integer.toString(containerCount));

						if (inOutType.equals("I")) {
							node.orderType = StoageEvent.INBOUND;
						} else {
							node.orderType = StoageEvent.OUTBOUND;
						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
						continue;
					}

					// slot null

					try {
						slot.setUsed(true);

						node.setSlot(slot);

						node.setX(row);

						node.setY(bay);



						manager.getATCManager(0).append(node);


					} catch (NullPointerException e) {
						System.err.println("error block:" + i);
						e.printStackTrace();
					}

				}

			} else if (commandStr.equals("M")) {

				StoageEvent node = new StoageEvent(jobID.getID(), SimEvent.TYPE_COMMAND);
				slot = blockManager.getSlot(0, bay, row, tier);


				node.setSlot(slot);
				node.setCommandType(SimEvent.COMMAND_MOVE);

				if (inOutType.equals("I")) {
					node.setATCID(SimATC.LAND_SIDE);
				} else {
					node.setATCID(SimATC.SEA_SIDE);
				}

				node.setX(row);

				node.setY(bay);
				manager.getATCManager(0).append(node);

			}


		} else {
			throw new UnparserableCommandException(command);
		}

	}

	/**
	 *
	 * generate Poisson distribution
	 * @param k
	 * @param r
	 * @return
	 */
	public static double logPois(int k, double r) {

		double value = 0;

		for (int i = 1; i <= k; i++) {

			value += Math.log(i);

		}

		double logP = (-r) + k * Math.log(r) - value;

		return Math.exp(logP);

	}


	public static void implWait() {

		while (true) {
			logPois(5, 5);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/*public static void main(String[] args) {
		JobManager.implWait();
	}
	 */

	@Override
	public void update() {
		//		main.getSimulatinoInfo();

	}





}

