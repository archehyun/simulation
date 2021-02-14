package core;

import java.awt.Canvas;

public class SimCore extends Canvas implements Runnable{
	
	public static final int TICKSPERS = 120;

	public static final boolean ISFRAMECAPPED = false;
	
	public int frames;

	public int lastFrames;

	public int ticks;
	
	private long time=0;
	
	Scene scene;
	
	private double delta = 0;
	
	public SimCore(Scene scene) {
		this.scene = scene;
	}
	public void init() {

	}
	public void tick() {
	}
	private boolean running = false;

	private String frameInfo;

	private Thread thread;
	
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
				scene.render(delta, time+=1);
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
			//frame.dispose();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
