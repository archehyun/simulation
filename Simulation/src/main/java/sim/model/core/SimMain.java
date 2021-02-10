package sim.model.core;

import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 *
 * @author archehyun
 *
 */
public abstract class SimMain {

	List<IFSimModel> observers = new LinkedList<IFSimModel>();

	public void notifyObservers() {
		for (IFSimModel o : observers) {
			o.update();
		}
	}

	String m;

	public void attach(IFSimModel observer) {
		observers.add(observer);
	}

	public void detach(IFSimModel observer) {
		observers.remove(observer);
	}

	/**
	 * @return
	 */
	public abstract SimulationInfo getSimulatinoInfo();

}
