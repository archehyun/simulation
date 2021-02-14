package sim.view.obj;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.core.SimEvent;
import sim.view.framework.SimCanvas;
import sim.view.framework.SimViewObject;

@SuppressWarnings("serial")
public class SimViewShip extends SimViewObject{

	public SimViewShip(SimCanvas canvas, int simID, float initX, float initY, float width, float height) {
		super(canvas, simID, initX, initY, width, height);
	}

	@Override
	public void draw(Graphics g) {
		
		g.setColor(Color.YELLOW);
		g.fillRect(getX(), getY(), 150, 15);
		
	}

	@Override
	public void updateMonitor(SimEvent message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCountView(boolean selected) {
		// TODO Auto-generated method stub
		
	}

}
