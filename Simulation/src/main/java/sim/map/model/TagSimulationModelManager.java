package sim.map.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TagSimulationModelManager {
	
	ArrayList<DoublePoint> pointMoveList;
	ArrayList<DoublePoint> pointList;
	ArrayList<TagLocationPoint> tagLocationList;
	//ArrayList<DoublePoint> tagPathList;
	
	public ArrayList<TagLocationPoint> getTagLocationList() {
		return tagLocationList;
	}
	public void setTagLocationList(ArrayList<TagLocationPoint> tagLocationList) {
		this.tagLocationList = tagLocationList;
	}

	HashMap<String , ArrayList<DoublePoint>> tagPathList;
	public HashMap<String , ArrayList<DoublePoint>> getTagPathList() {
		return tagPathList;
	}
	public ArrayList<DoublePoint> getTagPath(String tid) {
		
		return tagPathList.get(tid);
	}

	ArrayList<DoublePoint> resultList;
	ArrayList<LocationPoint> locationList;
	ArrayList<LocationPoint> gateLocationList;
	public ArrayList<LocationPoint> getGateLocationList() {
		return gateLocationList;
	}

	HashMap<String, TagLocationPoint> tagList = new HashMap<String, TagLocationPoint>();
	public HashMap<String, TagLocationPoint> getTagList() {
		return tagList;
	}

	String selectedLocation;
	
	public String getSelectedLocation() {
		return selectedLocation;
	}
	public void setSelectedLocation(String selectedLocation) {
		this.selectedLocation = selectedLocation;
	}
	public ArrayList<LocationPoint> getLocationList() {
		return locationList;
	}
	public void createTagMoveList(double x1, double y1, double x3, double y3, float distance) {
		pointMoveList.clear();

		ArrayList<DoublePoint> list = Distance.makeTagMoveList(x1, y1, x3, y3, distance);
		
		for(int i=0;i<list.size();i++)
		{
			pointMoveList.add(new DoublePoint(list.get(i).getX(),list.get(i).getY()));
		}
	}
	public void clearTagMoveList()
	{
		pointMoveList.clear();
	}

	public ArrayList<DoublePoint> getPointList() {
		return pointList;
	}
	public void addPointList(DoublePoint point)
	{
		pointList.add(point);
	}
	
	public void clearPointList()
	{
		pointList.clear();
	}
	
	public void clearTagPathList()
	{
		tagPathList.clear();
	}
	
	public void addLocation(LocationPoint point)
	{
		this.locationList.add(point);
	}
	
	public void addGateLocation(LocationPoint point)
	{
		this.gateLocationList.add(point);
	}
	
	public void addPoint(double x, double y)
	{
		pointList.add(new DoublePoint(x, y));
	}
	
	public void addTagPath(String tid,double x, double y)
	{
		if(tagPathList.containsKey(tid))
		{
			ArrayList<DoublePoint> tagPaths = tagPathList.get(tid);
			tagPaths.add(new DoublePoint(x, y));
			System.out.println("add");
		}
		else
		{
			ArrayList<DoublePoint> tagPaths = new ArrayList<DoublePoint>();
			tagPaths.add(new DoublePoint(x, y));
			tagPathList.put(tid, tagPaths);
		}
		
	}
	
	public void addResult(double x, double y)
	{
		resultList.add(new DoublePoint(x, y));
	}

	private static TagSimulationModelManager instance;
	private TagSimulationModelManager()
	{
		pointMoveList=new ArrayList<>();
		pointList=new ArrayList<>();
		locationList=new ArrayList<>();
		gateLocationList=new ArrayList<>();
		resultList = new ArrayList<>();
		tagPathList = new HashMap<String, ArrayList<DoublePoint>>();
		tagLocationList = new ArrayList<TagLocationPoint>();
		
	}
	public static TagSimulationModelManager getInstance()
	{
		if(instance==null)
			instance = new TagSimulationModelManager();
		return instance;
	}
	public TableModel createPointListTableModel() {
		DefaultTableModel defaultTableModel = new DefaultTableModel();
		defaultTableModel.addColumn("����");
		defaultTableModel.addColumn("Lng1");
		defaultTableModel.addColumn("Lat1");
		defaultTableModel.addColumn("Lng2");
		defaultTableModel.addColumn("Lat2");
		/*defaultTableModel.addColumn("Angle");
		defaultTableModel.addColumn("Distance");*/


		for(int i=0;i<pointList.size();i++)
		{
			if(i<pointList.size()-1)
			{
				DoublePoint point1 = pointList.get(i);
				DoublePoint point2 = pointList.get(i+1);
				Distance.getAngle(point1.getX(), point1.getY(), point2.getX(), point2.getY());
				Distance.getDistance(point1.getX(), point1.getY(), point2.getX(), point2.getY());
				defaultTableModel.addRow(new Object[]{i,
						String.format("%.6f", point1.getX()),
						String.format("%.6f", point1.getY()),
						String.format("%.6f", point2.getX()),
						String.format("%.6f", point2.getY())});
/*						String.format("%.1f", angle),
						String.format("%.1f",distance)});*/
			}
		}
		return defaultTableModel;
	}
	
	public DefaultTableModel createPointMoveTableModel() {
		DefaultTableModel defaultTableModel = new DefaultTableModel();
		defaultTableModel.addColumn("����");
		defaultTableModel.addColumn("Lng");
		defaultTableModel.addColumn("Lat");
		for(int i=0;i<pointMoveList.size();i++)
		{
			DoublePoint point1 = pointMoveList.get(i);
			defaultTableModel.addRow(new Object[]{i,String.format("%.6f", point1.getX()),String.format("%.6f",point1.getY())});
		}
		return defaultTableModel;
	}
	public void clearLocationPointList() {
		locationList.clear();
		
	}
	
	public void clearGateLocationPointList() {
		gateLocationList.clear();
		
	}
	
	public void clearResultPointList() {
		resultList.clear();
		
	}
	public ArrayList<DoublePoint> getResultList() {
		// TODO Auto-generated method stub
		return resultList;
	}
	public void updateTagInfo(TagLocationPoint item)
	{
		tagList.put(item.getTid(), item);
	}

	public ArrayList<DoublePoint> getPointMoveList() {
		// TODO Auto-generated method stub
		return pointMoveList;
	}
	public void clearTagLocationList() {
		tagLocationList.clear();
		
	}
	public void addTagLocation(TagLocationPoint tagLocationPoint) {
		tagLocationList.add(tagLocationPoint);
		
	}
	
}
