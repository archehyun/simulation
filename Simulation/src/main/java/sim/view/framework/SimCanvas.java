package sim.view.framework;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferStrategy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import map.cavas.DrawObject;
import sim.model.core.SimTimestamp;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.view.SimMainImpl;
import sim.view.obj.SimViewATC;
import sim.view.obj.SimViewBlock;

/**
 * @author LDCC
 *
 */
public class SimCanvas extends Canvas implements Runnable {

	public static final int magin = 25;

	protected ArrayList<DrawObject> draw = new ArrayList<DrawObject>();

	long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void addDrawObject(DrawObject object) {

		draw.add(object);

	}

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1920;

	public static final int HEIGHT = WIDTH * 9 / 16;

	public static final String TITLE = "YOUR GAMES NAME";

	public static final int TICKSPERS = 120;

	public static final boolean ISFRAMECAPPED = false;

	private SimMainImpl main;

	private Thread thread;

	private boolean running = false;

	public int frames;

	public int lastFrames;

	public int ticks;

	public static JFrame frame;

	public SimCanvas(SimMainImpl main) {
		this.main = main;

	}

	Font font = new Font("Serif", Font.PLAIN, 32);

	TextLayout textLayout;
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

	private boolean isTimeView = true;
	public synchronized void render() {

		frames++;
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		main.render(delta);

		Graphics g = bs.getDrawGraphics();

		//g.setColor(new Color(79, 194, 232));

		g.setColor(Color.lightGray);
		g.fillRect(0, 0, getWidth(), getHeight());
		//Call your render funtions from here

		try {

			//			System.out.println("size:" + draw.size());
			//Collections.reverse(draw);
			List<DrawObject> unmodifiableList = Collections.unmodifiableList(draw);

			//;

			for (DrawObject str : unmodifiableList) {

				//				System.out.println(str);
				str.draw(g);
			}
			//System.out.println();

			if (isFrameView) {
			g.setColor(Color.black);

			g.drawString(frameInfo, getWidth() - 125, 0 + 15);
			}

			//Timestamp stamp = new Timestamp(time * 1000);

			//g.drawString(sdf.format(new Date(stamp.getTime())), getWidth() - 100, getHeight() - 15);

			if (isTimeView) {
			Graphics2D g2 = (Graphics2D) g;

			textLayout = new TextLayout(SimTimestamp.toTimestmp(time), font, g2.getFontRenderContext());
			textLayout.draw(g2, getWidth() - 100, getHeight() - 15);
			}

			//			g.drawString(SimTimestamp.toTimestmp(time), getWidth() - 100, getHeight() - 15);

		} catch (Exception e) {
			//e.printStackTrace();
		}

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

	}

	String frameInfo;

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

				render();
			}
			if (fpsTimer < System.currentTimeMillis() - 1000) {

				frameInfo = ticks + " ticks, " + frames + " frames";
				ticks = 0;
				lastFrames = frames;
				frames = 0;
				fpsTimer = System.currentTimeMillis();
			}
		}
	}

	public void addObject(Object obj) {
		if (obj instanceof SimATC) {

			SimATC atc = (SimATC) obj;
			addDrawObject(new SimViewATC(this, atc.getAtcID(), atc.getBlockID(), atc.getX(), BlockManager.magin, (int) atc.bounds.width, (int) atc.bounds.height));
		}
		else if (obj instanceof Block) {
			Block block = (Block) obj;
			this.addDrawObject(new SimViewBlock(this, block.getBlockID(), block.getX(), block.getY()));
		}

	}

	public void clear() {
		draw.clear();

	}

	boolean isCountView = true;
	public void setCountView(boolean selected) {
		//Collections.reverse(draw);
		List<DrawObject> unmodifiableList = Collections.unmodifiableList(draw);

		//;

		for (DrawObject str : unmodifiableList) {
			((SimViewObject) str).setCountView(selected);
		}
	}

	public float getMargin() {
		return 35;
	}

	boolean isFrameView = true;

	public void setFrameView(boolean selected) {
		this.isFrameView = selected;

	}

	public void setTimeView(boolean view)
	{
		this.isTimeView = view;
	}

}
