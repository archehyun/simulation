package sim.model.impl;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.SimMessage;
import sim.model.SimMoveObject;

/**
 * @author 박창현
 *
 */
public class SimATC extends SimMoveObject{
	

	public SimATC(int x, int y) {
		super(x,y);
	}

	public void draw(Graphics g)
	{
		g.setColor(Color.cyan);
		g.fillRect((int)this.getX(), (int)this.getY(), this.getW(), this.getH());
	}
	public void setDestination(int destinatinoX)
	{
		
	}

	@Override
	public void update(SimMessage message) {
		
		boolean isStarted=(boolean) message.get("isStarted");
		
		if(!isStarted)
		{			
			return;
		}

		int tick = (int) message.get("fps");
		int w = (int) message.get("width");

		if(message.containsKey("speed"))
		{
			double speed = (double) message.get("speed");
			this.setSpeed(speed);
		}


		if(getX()<0)
		{
			direction=1;
		}
		if(getX()>1000)
		{
			direction=-1;

		}
		double newX= this.getX()+(getSpeed(tick))*direction;
		this.setX(newX);
	}
	
	

}
