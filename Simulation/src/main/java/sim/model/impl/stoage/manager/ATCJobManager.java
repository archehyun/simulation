package sim.model.impl.stoage.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import sim.model.core.SimMain;
import sim.model.core.SimModel;
import sim.model.core.SimModelManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.view.comp.SimMainImpl;

/**
 * @author archehyun
 *
 */
public abstract class ATCJobManager extends SimModelManager {


	public static float TROLLY_SPEED = 0.01f;

	private int blockID;

	public static float SPEED = 0.04f;

	Random rn =new Random();

	static SimMain main = SimMainImpl.getInstance();

	public ATCJobManager(String simName) {
		super(main, simName);
	}

	/**
	 * @param blockID
	 * @param atcID
	 * @return
	 */
	public SimATC getATC(int blockID,int atcID)
	{
		Iterator<SimModel> iter = list.iterator();

		while(iter.hasNext())
		{
			SimATC model = (SimATC) iter.next();

			if(model.getAtcID()==blockID)
			{
				return model;
			}
		}
		return null;
	}

	/**
	 * @param atcID
	 * @return ATC
	 */
	public SimATC getATC(int atcID)
	{
		Iterator<SimModel> iter = list.iterator();

		while(iter.hasNext())
		{
			SimATC model = (SimATC) iter.next();

			if(model.getAtcID()==atcID)
			{
				return model;
			}
		}
		return null;
	}

	/**
	 * @return busy atc Count
	 */
	public int getBusyCount() {

		Iterator<SimModel> iter = list.iterator();
		int count=0;
		while(iter.hasNext())
		{
			SimATC model = (SimATC) iter.next();

			if(!model.isIdle())
			{
				count++;
			}
		}

		return count;
	}

	/**
	 * @return atcCount
	 */
	public int getATCCount() {
		return list.size();
	}

	public synchronized boolean overlapRectangles(SimATC atc) {
		Iterator<SimModel> iter = list.iterator();


		while (iter.hasNext()) {
			SimATC ONE = (SimATC) iter.next();
			if (ONE.getAtcID() != atc.getAtcID() && ONE.overlapRectangles(atc.bounds)) {
				return true;
			}
		}

		return false;
	}

	public synchronized void setMove(boolean b) {
		Iterator<SimModel> iter = list.iterator();
		while (iter.hasNext()) {
			SimATC ONE = (SimATC) iter.next();
			ONE.setMove(b);
		}
		notifyAll();

	}

	public abstract String getType();

	public void setBlockID(int blockID) {
		this.blockID = blockID;

	}

	public int getBlockID() {
		return blockID;
	}

	public List getATCs() {
		return list;

	}

}
