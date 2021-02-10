package map.model;


public class DoublePoint {
	double x;
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setLat(double lat)
	{
		this.y=lat;
	}
	public void setLng(double lng)
	{
		this.x=lng;
	}
	public void setY(double y) {
		this.y = y;
	}
	double y;
	public DoublePoint(double x, double y) {
		this.x=x;
		this.y=y;
	}
	DoublePoint add(DoublePoint  p) { 

		return new DoublePoint(x+p.x, y+p.y); 

	} 

	DoublePoint mult(double d) { 

		return new DoublePoint(x*d, y*d); 
	} 
	
	public double getLat()
	{
		return y;
	}
	public double getLng()
	{
		return x;
	}

}
