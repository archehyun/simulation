package map.model;

import java.awt.geom.GeneralPath;

public class LocationPoint {
	
	private String location_code;
	
	public String getLocation_code() {
		return location_code;
	}
	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	private String location_name;
	
	private DoublePoint point1;
	public DoublePoint getPoint1() {
		return point1;
	}
	public void setPoint1(DoublePoint point1) {
		this.point1 = point1;
	}
	public DoublePoint getPoint2() {
		return point2;
	}
	public void setPoint2(DoublePoint point2) {
		this.point2 = point2;
	}
	public DoublePoint getPoint3() {
		return point3;
	}
	public void setPoint3(DoublePoint point3) {
		this.point3 = point3;
	}
	public DoublePoint getPoint4() {
		return point4;
	}
	public void setPoint4(DoublePoint point4) {
		this.point4 = point4;
	}
	private DoublePoint point2;
	private DoublePoint point3;
	private DoublePoint point4;
	
	public LocationPoint(String location_code,String locationn_name,DoublePoint point1,DoublePoint point2,DoublePoint point3,DoublePoint point4) {
		this(point1, point2, point3, point4);
		this.location_code = location_code;
		this.location_name = locationn_name;
	}
	
	
	public LocationPoint(DoublePoint point1,DoublePoint point2,DoublePoint point3,DoublePoint point4) {

		this.point1=point1;
		this.point2=point2;
		this.point3=point3;
		this.point4=point4;
		
	}

	public double getCenterX()
	{
		return (point1.x+point3.x)/2;
	}
	
	public double getCenterY()
	{
		return (point1.y+point3.y)/2;
	}
	public DoublePoint getCenterPoint()
	{
		return new DoublePoint((point1.x+point3.x)/2, (point1.y+point3.y)/2);
	}
	public GeneralPath getPath()
	{
		GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		p.moveTo(point1.getX(), point1.getY());
		p.lineTo(point2.getX(), point2.getY());
		p.lineTo(point3.getX(), point3.getY());
		p.lineTo(point4.getX(), point4.getY());
		p.closePath();
		return p;
	}
	

}

