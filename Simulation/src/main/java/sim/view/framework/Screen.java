package sim.view.framework;

import sim.view.SimMainImpl;

public abstract class Screen {

	private SimMainImpl main;

	public Screen(SimMainImpl main) {
		this.main = main;
	}

	public abstract void update(float deltaTime);

	public abstract void present(float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

}
