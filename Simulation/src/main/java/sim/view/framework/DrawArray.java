package sim.view.framework;

import java.util.ArrayList;

import sim.map.cavas.DrawObject;

public class DrawArray extends ArrayList<DrawObject>{
	private static DrawArray instance;
	
	private DrawArray() {
		// TODO Auto-generated constructor stub
	}
	
	public static DrawArray getInstance()
	{
		
		if(instance== null)
			instance = new DrawArray();
		
		return instance;
	}

}
