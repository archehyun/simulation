package sim.model;

/**
 * @author user
 *
 */
public abstract class SimMoveObject extends SimObject{
	

	protected double speed=10;

	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}


	protected double direction=1;
	
	
	public SimMoveObject() {

		
		
	};
	public SimMoveObject(int x, int y) {
		super(x,y);
	}
	public double getSpeed(int fps)
	{
		if(fps==0)
		{
			return 0;
		}
		double tempSpeed =(double) speed/(double)fps;
		return tempSpeed;
	}
	public double getDirection() {
		return direction;
	}
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/*
	 * 7m/s
	 */

	
	
	
	

}
