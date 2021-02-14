package sim.model;

/**
 * @author user
 *
 */
public abstract class SimObject implements IFSimObject{
	
	
	private double x, y;
	private int w,h;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}
	
	
	public SimObject() {}
	
	public SimObject(double x, double y) {
		this.setX(x);
		this.setY(y);
		
	}

}
