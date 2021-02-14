package sim.view.framework;

import javax.swing.JComponent;

import sim.map.cavas.DrawObject;
import sim.model.impl.stoage.block.BlockManager;

/**
 * 
 *
 * @author LDCC
 *
 */
public abstract class SimViewObject extends JComponent implements DrawObject, IFMonitor {

	//location view object
	public final Vector2 position;

	//
	private int simID;

	// x y initition location
	public float initX, initY;

	// 
	public final Rectangle bounds;

	// 

	/**
	 * 
	 *
	 * @DEFAULT 100
	 */
	protected int viewRate=100;

	protected SimCanvas canvas;

	public SimViewObject(SimCanvas canvas, int simID, float initX, float initY, float width, float height)

	{
		this.canvas = canvas;
		this.initX = initX;
		this.initY = initY;
		this.simID = simID;
		position = new Vector2(initX, initY);
		this.bounds = new Rectangle((int) (initX - width / 2), (int) (initY - height / 2), (int) width, (int) height);
	}

	@Override
	public abstract void setCountView(boolean selected);

	public int convertViewX(int x) {
		return (int) (x * BlockManager.blockWRate + canvas.getMargin());
	}

	public int convertViewY(int y) {
		return (int) (y * BlockManager.blockHRate + canvas.getMargin());
	}

	public int convertW(int w) {
		return (int) (w * BlockManager.blockWRate);
	}

	public int convertH(int h) {
		return (int) (h * BlockManager.blockHRate);
	}

}
