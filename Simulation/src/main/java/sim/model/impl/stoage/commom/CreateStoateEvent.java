package sim.model.impl.stoage.commom;

import java.util.Random;

import sim.model.Create;
import sim.model.core.SimEvent;
import sim.model.core.SimMain;
import sim.model.core.SimulationInfo;

public class CreateStoateEvent extends Create {

	JobID jobID = JobID.getInstance();

	Random rn = new Random();

	int blockID;

	private int seaLandType;

	private boolean isArrival = false;

	public CreateStoateEvent(SimMain main, String simName, int blockID, int seaLandType) {
		super(main, simName);
		this.seaLandType = seaLandType;
		this.blockID = blockID;
		logger.info("create:" + blockID + "-" + seaLandType);

	}

	@Override
	public int genterateIntegerval() {
		// TODO Auto-generated method stub
		return 10000000;
	}

	@Override
	public SimEvent createEvent() {

		synchronized (jobID) {
			jobID.update();
			event = new StoageEvent(jobID.getID(), SimEvent.TYPE_ORDER);
			asine(event);

			event.put("startTime", time);
			this.notifyMonitor(event);
			//JobManager.getInstance().notifyMonitor(event);
			logger.debug("create order:" + jobID.getID() + ", blockID:" + event.get("blockID"));
		}


		return event;
	}



	@Override
	public boolean isMakeEvnet() {
		// TODO Auto-generated method stub
		return rn.nextFloat() < 0.0002;
	}

	private void asine(SimEvent event) {
		event.put("seaLandType", seaLandType);
		event.put("inOutType", StoageEvent.OUTBOUND);
		/*int n2 = rn.nextInt(10);
		if (n2 > 5) {
			event.put("inOutType", StoageEvent.OUTBOUND);

		} else {
			event.put("inOutType", StoageEvent.INBOUND);

		}*/
		event.put("blockID", blockID);
	}

	long time;

	@Override
	public void update()
	{

		SimulationInfo info = main.getSimulatinoInfo();
		time = (long) info.get("time");

		if (start) {
			makeEvent();
		}
	}

}
