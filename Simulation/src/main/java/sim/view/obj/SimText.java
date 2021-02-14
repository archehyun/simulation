package sim.view.obj;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;

import org.apache.log4j.Logger;

import sim.map.cavas.DrawObject;
import sim.model.IFSimObject;
import sim.model.SimMessage;
import sim.view.framework.SimCanvas;

public class SimText implements DrawObject, SimViewUpdate, IFSimObject{

	private TextLayout textLayout;
	
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Font font;

	private int x, y;

	public void setFont(Font font) {
		this.font = font;
	}

	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	private String text;
	
	private String frameInfo;



	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.black);

		textLayout = new TextLayout(text, font, g2.getFontRenderContext());
		
		textLayout.draw(g2, x-150, y-25);
		
		g.drawString(frameInfo, x-125, 15);

	}

	@Override
	public void setCountView(boolean selected) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SimCanvas canvas) {
		
		this.text = canvas.getStmpTime();
		
		this.frameInfo= canvas.getFrameInfo();
		
		this.setLocation(canvas.getWidth(), canvas.getHeight());

	}

	@Override
	public void update(SimMessage message) {
		
	}

}
