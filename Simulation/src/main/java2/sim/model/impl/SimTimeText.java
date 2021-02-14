package sim.model.impl;

import java.awt.Graphics;

import sim.model.SimMessage;
import sim.model.SimTimestamp;
import sim.model.SimulationTime;

public class SimTimeText extends SimText{

	public SimTimeText(int x, int y) {
		super(x, y);
	}
	boolean isship=true;
	
	String ship="ship";
	public void draw(Graphics g)
	{
		super.draw(g);
		
		
		if(isship) {
		g.drawString(ship, (int)this.getX()+20, (int)this.getY()+20);
		}
	}

	@Override
	public void update(SimMessage message) {

		boolean isStarted = (boolean) message.get("isStarted");

		if(!isStarted)
		{
			return;
		}
		
		
		isship =(boolean) message.get("ship");


		long s =SimulationTime.FRAME_TO_SECOND( (int) message.get("time"));

		this.setText(SimTimestamp.toTimestmp(s));


	}



}
