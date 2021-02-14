package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;

public class SimText implements DrawObject{
	
	Font font = new Font("Serif",Font.PLAIN,32);
	private String text;
	TextLayout textLayout;
	private int x;
	private int y;
	public SimText(String text) {
		this.text =text;
	}
	public void setText(String text)
	{
		this.text =text;
	}
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public void addChild(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(new Color(79, 194, 232));
		textLayout = new TextLayout(text, font,g2.getFontRenderContext());
		textLayout.draw(g2, x,y); 

	}

}
