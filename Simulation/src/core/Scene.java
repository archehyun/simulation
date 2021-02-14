package core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

@SuppressWarnings("serial")
public class Scene extends Canvas {
	
	SimText timeText;
	public Scene() {
		timeText = new SimText("");
	}
	
	Font font = new Font("Serif",Font.PLAIN,32);
	
	
	public synchronized void render(double delta, long time) {

		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		
		
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		//Call your render funtions from here

		try {
			timeText.setText(SimTimestamp.toTimestmp(time));
			timeText.setLocation(getWidth() - 150, getHeight()- 15);			
			timeText.draw(g);
				

		} catch (Exception e) {
			//e.printStackTrace();
		}

		g.dispose();
		bs.show();
	}

}
