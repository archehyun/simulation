package sim.model;

import java.awt.Graphics;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sim.model.impl.SimBlockATCS;
import sim.model.impl.SimText;
import sim.model.impl.SimTimeText;


/**
 * 시뮬레이션 메인 클래스
 * 
 * @date 2021-02-14
 * @author 박창현
 *
 */
public class SimulationMain implements DrawObject{

	int time;

	SimMessage message;

	private Element element;

	ArrayList<IFSimObject> array= new ArrayList<IFSimObject>();

	SimText simText;
	SimTimeText timeText;
	SimMoveObject moveObj;
	SimBackground background;

	public SimulationMain() {

		message = new SimMessage();


		try {
			this.parse("layout/layout2.xml");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setParam("isStarted", false);

		simText = new SimText();

		timeText = new SimTimeText(10, 25);		


		this.addChiled(simText);

		this.addChiled(timeText);


	}
	public void addChiled(IFSimObject obj)
	{
		array.add(obj);
	}


	public void notifyMessage()
	{
		Iterator<IFSimObject> iter = array.iterator();

		while(iter.hasNext())
		{
			IFSimObject item = iter.next();

			item.update(message);
		}

	}
	long lastTime;

	int interval=0;

	Random rn = new Random();

	private int intervalTime=0;

	public void render(double delta) {

		time++;

		lastTime = SimulationTime.FRAME_TO_SECOND(time);


		if(interval==0)
		{
			int ma = rn.nextInt(10);
			if(ma<5)
			{
				interval = rn.nextInt(100);
			}
		}


		if(intervalTime<SimulationTime.FRAME_TO_SECOND(time)-interval)
		{
			message.put("ship",false);	
			System.out.println("intervaltime:"+interval);
			intervalTime =SimulationTime.FRAME_TO_SECOND(time);
			interval=0;
		}
		else
		{

			message.put("ship",true);
		}


		message.put("time",time);

		notifyMessage();

	}

	@Override
	public void draw(Graphics g) {
		try {

			List<IFSimObject> unmodifiableList = Collections.unmodifiableList(array);
			for (IFSimObject str : unmodifiableList) {
				str.draw(g);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setParam(String key, Object obj)
	{
		this.message.put(key, obj);
	}

	public void start()
	{
		time=0;
		this.setParam("isStarted", true);
	}
	public void stop()
	{
		this.setParam("isStarted", false);
	}

	public void parse(String filePath) throws SAXException, IOException, URISyntaxException, ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document xml = null;
		System.out.println(ClassLoader.getSystemResource(filePath).toURI().toString());

		xml = documentBuilder.parse(ClassLoader.getSystemResource(filePath).toURI().toString());

		element = xml.getDocumentElement();


		/* TO-DO UPDATE
		 *
		 */

		NodeList blockList = element.getElementsByTagName("node");

		int nodeSize = blockList.getLength();

		for(int i=0;i<nodeSize;i++)
		{
			Element item = (Element) blockList.item(i);
			String className = item.getAttribute("class");

			if(className.equals("SimBlockATCS"))
			{
				SimBlockATCS newObj = new SimBlockATCS();			

				int x = Integer.parseInt(item.getAttribute("x"));
				int y = Integer.parseInt(item.getAttribute("y"));
				int w = Integer.parseInt(item.getAttribute("w"));
				int h = Integer.parseInt(item.getAttribute("h"));

				newObj.setBound(x, y, w, h);
				
				this.addChiled(newObj);
			}

			if(className.equals("SimBackground"))
			{
				SimBackground newObj = new SimBackground();			

				int x = Integer.parseInt(item.getAttribute("x"));
				int y = Integer.parseInt(item.getAttribute("y"));
				int w = Integer.parseInt(item.getAttribute("w"));
				int h = Integer.parseInt(item.getAttribute("h"));
				
				newObj.setBound(x, y, w, h);
				
				this.addChiled(newObj);
			}

		}
	}



}

