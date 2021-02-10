package sim.model.impl.stoage.commom;


public class JobID {

	private static JobID instance;

	public static JobID getInstance() {
		if (instance == null)
			instance = new JobID();
		return instance;
	}

	private JobID() {
		count = 0;
	}

	private int count;


	public synchronized void update() {
		count++;
	}

	public int getID() {
		return count;
	}

}
