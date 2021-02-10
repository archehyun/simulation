package map.model;

public class TagLocationPoint extends DoublePoint{

	public TagLocationPoint(double x, double y) {
		super(x, y);
	}
	
	public TagLocationPoint(String tid,double x, double y) {
		this(x, y);
		this.tid= tid;
	}
	
	private String tid;
	
	private int humidity;
	private int temperature;
	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	private boolean selected;
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
		
	}
	private boolean mouseOver;
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMouseOver() {
		// TODO Auto-generated method stub
		return mouseOver;
	}


}
