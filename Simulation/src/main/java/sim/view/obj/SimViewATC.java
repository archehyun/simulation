package sim.view.obj;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.apache.log4j.Logger;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.impl.stoage.manager.ATCManager;
import sim.view.framework.SimCanvas;
import sim.view.framework.SimViewObject;
import sim.view.framework.Vector2;

/**
 *
 * ATC ǥ�� �̹��� ��ü
 *
 * @author LDCC
 *
 */
public class SimViewATC extends SimViewObject {

	public Logger logger = Logger.getLogger(getClass());

	Color ATC_COLOR = Color.white;

	Color TROLLY_COLOR = Color.orange;

	Color BUSY_COLOR = Color.blue;

	ATCJobManager manager = null;

	SimATC atc;

	Shape atcS_sea, atcS_land;

	int row=6;

	//	troly size;
	int trollySizeW, trollySizeH ;

	// ATC ������
	int atcW, atcH;

	// ǥ�� ������
	int xx, yy, ww, hh;


	/**
	 * draw ATC object
	 *
	 * @param atcID
	 * @param blockID
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public SimViewATC(SimCanvas canvas, int atcID, int blockID, int x, int y, int width, int height)
	{
		super(canvas, atcID, x, y, width, height);
		atcS_sea = createDiamond(25);
		atcS_land = createDiamond(25);
		manager = ATCManager.getInstance().getATCManager(blockID);

		atc = manager.getATC(atcID);

		atc.updateInitLocationOnWinddows(blockID);

		trollySizeH = (int) (bounds.height);
		trollySizeW = BlockManager.conW;

		atcW = (int) (bounds.width);

		atcH = (int) bounds.height;

		logger.info("viewInit:" + atc.getLocation());


	}


	private void drawATCState(Graphics g, int x, int y, int trollyX)
	{

		g.setColor(ATC_COLOR);

		if (atc.getLocationType() == SimATC.TYPE_LAND)
		{
			//atcW = atcW - 2;
			g.setColor(Color.cyan.brighter());
			x -= 4;

		}

		int atc_w = convertW(atcW);

		g.fillRect(x, y, atc_w, 5);
		g.fillRect(x - 2, y, 2, 15);
		g.fillRect(x + atc_w, y, 2, 15);

		//	g2.draw(atcS_sea);

		// draw trolly
		if (atc.isLoad()) {
			g.setColor(BUSY_COLOR);

			g.fillRect(trollyX, y + 5, 5, 5);
		} else {
			g.setColor(TROLLY_COLOR);
		}

		//hosist
		if (atc.isHoist()) {

			float rate = atc.hoistWorkTime / atc.getHoistTime();
			float h = 10f * rate;

			g.fillRect(trollyX, y + 5, 5, (int) h);
		}

		g.setColor(Color.black);
		g.drawString(String.valueOf(atc.getAtcID()), x, y - 3);

		/* draw work info
		 * I : Inbound
		 * O: Outbound
		 */

		String inout;
		if (atc.getInOutType() == StoageEvent.INBOUND) {
			inout = "I";
		} else {
			inout = "o";
		}
		g.drawString(inout, x + 20, y);


	}
	@Override
	public void draw(Graphics g) {
		if(atc!=null)
		{

			//atc
			Vector2 atcLocation = atc.getLocation();

			int atcX = convertViewX(atc.getInitXpointOnWindows());

			int trollyX = convertViewX((int) atcLocation.x + atc.getInitXpointOnWindows());

			int atcY = convertViewY((int) atcLocation.y);

			drawATC(g, atcX, atcY, trollyX);

			int atcStateX = convertViewX(atc.getInitXpointOnWindows() + 15);

			int atcStateTrollyX = convertViewX((int) atcLocation.x + atc.getInitXpointOnWindows() + 15);

			drawATCState(g, atcStateX, atcY, atcStateTrollyX);

		}
	}

	/**
	 * draw moving atc
	 *
	 * state
	 * busy :blue
	 * idle :yellow
	 *
	 * @param g
	 */
	private void drawATC(Graphics g, int atcX, int atcY, int trollyX) {
		if (atc.isLoad()) {
			g.setColor(BUSY_COLOR);


		} else {
			g.setColor(TROLLY_COLOR);
		}

		// draw trolly
		int trolly_w = convertW(trollySizeW);
		int trolly_h = convertH(trollySizeH);
		g.fillRect(trollyX, atcY, trolly_w, trolly_h);

		// draw frame
		g.setColor(Color.WHITE);

		int atc_w = convertW(atcW);
		int atc_h = convertH(atcH);

		g.drawRect(atcX, atcY, atc_w, atc_h);



	}

	@Override
	public void updateMonitor(SimEvent message) {
		// TODO Auto-generated method stub

	}

	boolean isCountView = true;

	@Override
	public void setCountView(boolean selected) {
		isCountView = selected;
	}

	public static Shape createDiamond(final float s) {
		final GeneralPath p0 = new GeneralPath();
		p0.moveTo(0.0f, 0.0f);

		p0.lineTo(0.0f, 18f);
		p0.lineTo(+7.2f, 18f);
		p0.lineTo(+7.2f, 7.2f);
		p0.lineTo(+18f, 7.2f);
		p0.lineTo(+18f, 18f);

		p0.lineTo(s, 18f);
		p0.lineTo(s, 0.0f);
		p0.closePath();
		return p0;
	}



}
