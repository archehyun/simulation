package sim.model.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;

import sim.model.SimMessage;
import sim.model.SimObject;

public class SimText extends SimObject{
	
	public SimText(int x, int y)
	{
		super(x, y);
	}

	private TextLayout textLayout;
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private String text="  ";

	private String frameInfo;

	Font font = new Font("Serif", Font.PLAIN, 32);

	public SimText() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {

		try {
			Graphics2D g2 = (Graphics2D) g;
			g.setColor(Color.black);

			textLayout = new TextLayout(text, font, g2.getFontRenderContext());

			textLayout.draw(g2, (int)this.getX(), (int)this.getY());


		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void update(SimMessage message) {
		
		boolean isStarted = (boolean) message.get("isStarted");
		
		if(!isStarted)
		{
			return;
		}

		this.text = String.valueOf(message.get("fps"))+", frames:"+message.get("frames");
		
		this.setX((int) message.get("width")-250);
		this.setY((int)message.get("height")-25);



	}

}
