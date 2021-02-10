package map.cavas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import map.model.DoublePoint;

public abstract class  AbstractCanvas extends JComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int speed;
	protected int margin =45;
	private float baseDistance;
	protected Color lineColor;
	protected Color pointColor;
	protected double latPointRate;
	protected double lngPointRate;
	protected int latCenterY;
	protected int lngCenterX;
	protected JPopupMenu popupMenu;
	public void setPopupMenu(JPopupMenu popupMenu)
	{
		this.popupMenu = popupMenu;
	}
	protected ArrayList<DrawObject> draw = new ArrayList<DrawObject>();
	
	public void addDrawObject(DrawObject object)
	{
		draw.add(object);
	}
	
	protected DoublePoint tagLocation;

	public void setTagLocation(DoublePoint tagLocation) {
		this.tagLocation = tagLocation;
	}
	public void setTagLocation(double x, double y) {
		this.tagLocation = new DoublePoint(x, y);
	}
	public void clearTagLocation()
	{
		this.tagLocation  = null;
	}
	protected int centerX;
	protected int centerY;
	
	protected Image image;
	protected Graphics second;
	
	protected ArrayList<DoublePoint> pointMoveList=new ArrayList<>();
	protected ArrayList<DoublePoint> pointList=new ArrayList<>();
	public int getSpeed() {
		return speed;
	}
	private boolean isDrawTagPath=true;

	public boolean isDrawTagPath() {
		return isDrawTagPath;
	}

	public void setDrawTagPath(boolean isDrawTagPath) {
		this.isDrawTagPath = isDrawTagPath;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	private boolean isMouseAdd = true;
	public boolean isMouseAdd() {
		return isMouseAdd;
	}

	public void setMouseAdd(boolean isMouseAdd) {
		this.isMouseAdd = isMouseAdd;
	}
	private boolean isDarwGrid=true;
	private boolean isLineDraw=true;

	public boolean isDarwGrid() {
		return isDarwGrid;
	}
	private boolean isDarwCoordinate=true;

	/** 좌표 표시 여부  **/
	public boolean isDarwCoordinate() {	return isDarwCoordinate;}

	/** 라인 표시 여부  **/
	public boolean isLineDraw() {return isLineDraw;	}
	public void setDarwCoordinate(boolean isDarwCoordinate) {
		this.isDarwCoordinate = isDarwCoordinate;
	}
	public void setDarwGrid(boolean isDarwGrid) {
		this.isDarwGrid = isDarwGrid;
	}
	public void setLineDraw(boolean isLineDraw) {
		this.isLineDraw =isLineDraw;
	}
	public void setBaseDistance(float baseDistance) {
		this.baseDistance = baseDistance;
	}
	public float getBaseDistance() {
		return baseDistance;
	}
	public abstract int convertX(double lat);
	public abstract int convertY(double lng);

	public abstract void simulation() ;


	public abstract  void makeTagMoveList(double x1, double y1, double x2, double y2) ;

	public int getLngStart() {
		return lngStart;
	}



	public void setLngStart(int lngStart) {
		this.lngStart = lngStart;
	}



	public int getLngEnd() {
		return lngEnd;
	}
	public void setLngEnd(int lngEnd) {
		this.lngEnd = lngEnd;
	}



	public int getLatStart() {
		return latStart;
	}



	public void setLatStart(int latStart) {
		this.latStart = latStart;
	}



	public int getLatEnd() {
		return latEnd;
	}

	public void setLatEnd(int latEnd) {
		this.latEnd = latEnd;
	}
	protected int lngEnd;
	protected int latStart;
	protected int latEnd;
	protected int lngStart;
	private double centerLat;
	public double getCenterLat() {
		return centerLat;
	}
	public void setCenterLat(double centerLat) {
		this.centerLat = centerLat;
	}

	private double centerLng;
	
	public double getCenterLng() {
		return centerLng;
	}
	public void setCenterLng(double centerLng) {
		this.centerLng = centerLng;
	}
	public void setCenter(double lat, double lng)
	{
		this.centerLat =-lat;
		this.centerLng= -lng;
	}
	protected boolean isPointShow() {
		// TODO Auto-generated method stub
		return isPointShow;
	}
	protected boolean isPointShow=true;
	private boolean isDrawResult=true;
	protected  boolean isDrawResult()
	{
		return isDrawResult;
	}
	protected void setDrawResult(boolean drawResult) {
		this.isDrawResult =drawResult;

	}
}
