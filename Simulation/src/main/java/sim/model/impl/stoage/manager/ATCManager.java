package sim.model.impl.stoage.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.manager.impl.CrossOverJobManager;
import sim.model.impl.stoage.manager.impl.TwinJobManager;
import sim.view.EquipPanel;

public class ATCManager {

	Map<Integer, ATCJobManager> atcManagerList;

	private static ATCManager instance;

	private ATCManager() {
		atcManagerList = new HashMap<Integer, ATCJobManager>();
	}

	public static ATCManager getInstance() {

		if (instance == null)
			instance = new ATCManager();
		return instance;
	}

	public ATCJobManager createManger(int blockID, String type) {
		if(this.atcManagerList.containsKey(blockID))
		{
			return atcManagerList.get(blockID);
		}
		else
		{
			ATCJobManager manager = null;
			if (type.equals("cross")) {
				manager = new CrossOverJobManager(blockID + "-cross");
			} else if (type.equals("twin")) {
				manager = new TwinJobManager(blockID + "-cross");
			}
			atcManagerList.put(blockID, manager);

			manager.setBlockID(blockID);

			return manager;
		}
	}

	public ATCJobManager getATCManager(int blockID) {
		return atcManagerList.get(blockID);
	}

	public void simStart() {
		Iterator<Integer> keys = atcManagerList.keySet().iterator();
		while (keys.hasNext()) {
			int blockID = keys.next();
			ATCJobManager manager = atcManagerList.get(blockID);
			manager.simStart();

		}

	}

	public void clear() {
		Iterator<Integer> keys = atcManagerList.keySet().iterator();
		while (keys.hasNext()) {
			int blockID = keys.next();
			ATCJobManager manager = atcManagerList.get(blockID);
			manager.clear();

		}

		atcManagerList.clear();

	}

	public void simStop() {
		Iterator<Integer> keys = atcManagerList.keySet().iterator();
		while (keys.hasNext()) {
			int blockID = keys.next();
			ATCJobManager manager = atcManagerList.get(blockID);
			manager.simStop();

		}

	}

	public void addMonitor(EquipPanel IFMonitor) {
		Iterator<Integer> keys = atcManagerList.keySet().iterator();
		while (keys.hasNext()) {
			int blockID = keys.next();
			ATCJobManager manager = atcManagerList.get(blockID);
			manager.addMonitor(IFMonitor);

		}
	}

	public void append(SimEvent event) {
		Iterator<Integer> keys = atcManagerList.keySet().iterator();
		while (keys.hasNext()) {
			int blockID = keys.next();
			ATCJobManager manager = atcManagerList.get(blockID);
			manager.append(event);

		}
	}

}
