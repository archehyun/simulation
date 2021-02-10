package map.option;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class MapOptionManager {
	private static MapOptionManager instance;
	private boolean linedraw;
	private Properties properties;
	private static final String SIMULATION_INFO_PROPERTIES = "/map/option/info.properties";
	private float baseDistance;
	private int speed;
	private int stepCount;
	public int getStepCount() {
		return stepCount;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public boolean isDrawTagPath() {
		return drawTagPath;
	}
	public void setDrawTagPath(boolean drawTagPath) {
		this.drawTagPath = drawTagPath;
	}
	private boolean drawTagPath;
	private boolean isDrawResult;
	private float drawLat;
	public double getDrawLat() {
		return drawLat;
	}
	public void setDrawLat(double drawLat) {
		this.drawLat = (float) drawLat;
	}
	public double getDrawLng() {
		return drawLng;
	}
	public void setDrawLng(double drawLng) {
		this.drawLng = (float) drawLng;
	}
	private float  drawLng;

	private MapOptionManager() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(getClass().getResource(SIMULATION_INFO_PROPERTIES).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		baseDistance = Float.parseFloat((String)properties.get("baseDistance"));
		speed = Integer.parseInt((String)properties.get("speed"));
		drawTagPath = Boolean.parseBoolean((String)properties.get("draw.tagpath"));
		linedraw = Boolean.parseBoolean((String)properties.get("draw.line"));
		isDarwCoordinate = Boolean.parseBoolean((String)properties.get("draw.coordinate"));
		isDrawResult = Boolean.parseBoolean((String)properties.get("draw.result"));
		
		drawLat = Float.parseFloat((String)properties.get("draw.lat"));
		drawLng = Float.parseFloat((String)properties.get("draw.lng"));
		stepCount= Integer.parseInt((String)properties.get("stepcount"));
	}
	public boolean isLinedraw() {
		return linedraw;
	}
	public void setLinedraw(boolean linedraw) {
		this.linedraw = linedraw;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public float getBaseDistance() {
		return baseDistance;
	}
	public void setBaseDistance(float baseDistance) {
		this.baseDistance = baseDistance;
	}
	public static MapOptionManager getInstance()
	{
		if(instance == null)
			instance = new MapOptionManager();
		return instance;
	}
	public void save()
	{
		properties.put("draw.lat", String.valueOf(this.getDrawLat()));
		properties.put("draw.lng", String.valueOf(this.getDrawLng()));
		properties.put("baseDistance", String.valueOf(this.getBaseDistance()));
		properties.put("speed", String.valueOf(this.getSpeed()));
		properties.put("draw.tagpath", String.valueOf(this.isDrawTagPath()));
		properties.put("draw.line", String.valueOf(this.isLinedraw()));
		properties.put("draw.coordinate", String.valueOf(this.isDrawCoordinate()));
		properties.put("draw.result", String.valueOf(this.isDrawResult()));
		properties.put("stepcount", String.valueOf(this.getStepCount()));
		System.out.println("save");
		try {
			OutputStream out = new FileOutputStream(getClass().getResource(SIMULATION_INFO_PROPERTIES).getFile());
			properties.store(out, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	boolean isDarwCoordinate;
	public void setDarwCoordinate(boolean isDarwCoordinate) {
		this.isDarwCoordinate = isDarwCoordinate;
	}
	public void setDarwResult(boolean isDarwResult) {
		this.isDrawResult = isDarwResult;
	}
	public boolean isDrawCoordinate() {
		// TODO Auto-generated method stub
		return isDarwCoordinate;
	}
	public boolean isDrawResult() {
		// TODO Auto-generated method stub
		return isDrawResult;
	}
	public void setStepCount(int stepCount) {
		
		this.stepCount = stepCount;
		
	}
	

}
