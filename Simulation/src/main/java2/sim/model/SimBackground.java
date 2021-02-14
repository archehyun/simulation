package sim.model;

import java.awt.Color;
import java.awt.Graphics;

public class SimBackground extends SimObject{

	public SimBackground(int i, int j) {
		super(i,j);
	}

	public SimBackground() {
	}
	
	public void setBound(int x, int y, int w, int h)
	{
		this.setX(x);
		this.setY(y);
		this.setW(w);
		this.setH(h);
		
	}

	@Override
	public void update(SimMessage message) {
		
	}

	@Override
	public void draw(Graphics g) {
		
		g.setColor(Color.black);
		
		for(int i=0;i<11;i++)			
		{
			g.drawString(String.valueOf(i*100), (int)getX()+i*100, (int)getY());
		}
		g.drawRoundRect((int)getX(), (int)getY(), getW(), getH(), 10, 10);
		g.drawRoundRect((int)getX()+10, (int)getY()+10, getW()-20, getH()-20, 10, 10);
		
	}

}
