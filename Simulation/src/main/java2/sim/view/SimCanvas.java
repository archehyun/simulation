package sim.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import sim.map.cavas.DrawObject;
import sim.model.SimulationMain;
import sim.model.core.SimTimestamp;

/**
 * 
 * @date 2021-02-13
 * 
 * canvas --> Main -> node
 * @version 1.0
 * @author 박창현
 *
 */
public class SimCanvas extends Canvas implements Runnable {
	

	public static final int magin = 25;
		
	protected Logger logger = Logger.getLogger(this.getClass().getName());

	private ArrayList draw = new ArrayList();

	long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void addDrawObject(DrawObject object) {

		logger.debug("insert:"+draw.add(object)+","+object);

	}

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1920;

	public static final int HEIGHT = WIDTH * 9 / 16;

	public static final String TITLE = "YOUR GAMES NAME";

	public static final int TICKSPERS = 120;

	public static final boolean ISFRAMECAPPED = false;

	private SimulationMain main;

	private Thread thread;

	private boolean running = false;

	public int frames;

	public int lastFrames;

	public int ticks;

	public static JFrame frame;

	public SimCanvas(SimulationMain simulationMain) {
		this.main = simulationMain;
		
		
	}

	public String getStmpTime()
	{
		return SimTimestamp.toTimestmp(time);
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

	public synchronized void render() {

		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		
		
		
		main.setParam("width", this.getWidth());
		main.setParam("height", this.getHeight());
		

		main.render(delta);

		Graphics g = bs.getDrawGraphics();


		g.setColor(Color.lightGray);
		
		g.fillRect(0, 0, getWidth(), getHeight());
		
		main.draw(g);
		

		g.dispose();
		bs.show();
	}

	/**
	 *
	 */
	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}

	/**
	 *
	 */
	public synchronized void stop(){
		if(!running) return;
		running = false;
		try {
			System.exit(1);
			frame.dispose();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		main.setParam("fps", ticks);

	}

	String frameInfo;

	public String getFrameInfo() {
		return frameInfo;
	}

	public void tick() {
	}

	double delta = 0;
	
	
	@Override
	public void run() {
		init();
		//Tick counter variable
		long lastTime = System.nanoTime();
		//Nanoseconds per Tick
		double nsPerTick = 1000000000D / TICKSPERS;
		frames = 0;
		ticks = 0;
		long fpsTimer = System.currentTimeMillis();

		boolean shouldRender;
		while (running) {
			shouldRender = !ISFRAMECAPPED;
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;

			lastTime = now;
			//if it should tick it does this
			while (delta >= 1) {
				ticks++;
				
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			
			if (shouldRender) {
				frames++;
				render();
			}
			
			// 1second pass
			if (fpsTimer < System.currentTimeMillis() - 1000) {

				frameInfo = ticks + " ticks, " + frames + " frames";
				
				main.setParam("fps", ticks);
				main.setParam("frames", frames);
				
				
				ticks = 0;
				lastFrames = frames;
				frames = 0;
				fpsTimer = System.currentTimeMillis();
			}
		}
	}

	
	public void clear() {
		
		logger.debug("clear");
		draw.clear();

	}

	public float getMargin() {
		return 35;
	}
}
