package sim.view.comp;

import java.awt.Font;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sim.model.core.SimEvent;
import sim.model.core.SimMain;
import sim.model.core.SimulationInfo;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.impl.CrossATC2;
import sim.model.impl.stoage.atc.impl.TwinATC;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.CreateStoateEvent;
import sim.model.impl.stoage.commom.JobManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.impl.stoage.commom.UnparserableCommandException;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.impl.stoage.manager.ATCManager;
import sim.model.queue.SimNode;
import sim.view.framework.SimCanvas;
import sim.view.obj.SimText;
import sim.view.obj.SimViewShip;

/**
 *
 * a simulation to merasure the  prefomance of Automatic Transfer Crane  type
 *
 * there is three type of atc
 *
 * 泥ル쾲吏몃뒗 �겕濡쒖뒪 ���엯�땲�떎.
 * the first is Cross type,  it has two Crane
 *
 * @author  ARCHEHYUN
 *
 */
public class SimMainImpl extends SimMain {


	private static final long TIME_STEP = 1;

	SimulationInfo simulationInfo;

	SimCanvas canvas;

	CreateStoateEvent[][] event;

	boolean start = false;

	/**
	 * simulation time
	 *
	 *
	 */
	private long time;

	private static SimMainImpl instance;

	private SimMainImpl() {
		simulationInfo = new SimulationInfo();

		jobManager = new JobManager(this, "jobManager");
	}

	public static SimMain getInstance() {
		if (instance == null)
			instance = new SimMainImpl();

		return instance;
	}

	/**
	 *
	 *
	 * @param delta
	 */
	public void render(double delta) {


		if (start) {
		time += TIME_STEP;

			/*TODO Simulation Timing Model
			 *
			 */
		}

		canvas.setTime(time);

		simulationInfo.put("delta", delta);
		simulationInfo.put("time", time);

		notifyObservers();

	}

	public void setCanvas(SimCanvas canvas) {
		this.canvas = canvas;
	}

	ATCManager atcManagerImpl = ATCManager.getInstance();

	private ATCJobManager atcManager;

	private JobManager jobManager;

	private Element element;

	static BlockManager blockManager = BlockManager.getInstance();

	String filePath = "layout/layout.xml";

	public void parse(String filePath) throws SAXException, IOException, URISyntaxException, ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document xml = null;

		xml = documentBuilder.parse(ClassLoader.getSystemResource(filePath).toURI().toString());

		element = xml.getDocumentElement();
		/* TO-DO UPDATE
		 *
		 */

	}

	
	public void createInit()
	{
		try {

			attach(blockManager);
			NodeList blockList = element.getElementsByTagName("block");

			blockManager.block = blockList.getLength();
			attach(jobManager);

			blockManager.blockInit(this);

			jobManager.init();

			if (blockList.getLength() > 0) {
				//諛섎났臾� �씠�슜

				event = new CreateStoateEvent[blockList.getLength()][2];
				for (int i = 0; i < blockList.getLength(); i++) {
					Element blockElement = (Element) blockList.item(i);

					String type = blockElement.getAttribute("type");
					String use = blockElement.getAttribute("use");

					if (!Boolean.valueOf(use))
						continue;

					int blockID = createBlock(blockElement);

					event[i][0] = new CreateStoateEvent(this, "create", blockID, StoageEvent.SEA);
					event[i][0].addSimModel(jobManager);
					event[i][1] = new CreateStoateEvent(this, "create", blockID, StoageEvent.LAND);
					event[i][1].addSimModel(jobManager);

					attach(event[i][0]);
					attach(event[i][1]);

					event[i][0].addSimModel(jobManager);
					event[i][1].addSimModel(jobManager);
					atcManager = atcManagerImpl.createManger(blockID, type);

					NodeList atcList = blockElement.getElementsByTagName("atc");


					if (atcList.getLength() > 0) {
						for (int j = 0; j < atcList.getLength(); j++) {
							Node atcNode = atcList.item(j);

							createATC(blockID, atcManager, atcNode);
						}
					}
				}
			}
			
			
			SimViewShip ship = new SimViewShip(canvas, 0, 0, 0, 100, 15);
			SimText timeText = new SimText();
			Font font = new Font("Serif", Font.PLAIN, 32);
			
			timeText.setFont(font);;
			canvas.addDrawObject(timeText);
			canvas.addDrawObject(ship);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createATC(int blockID, ATCJobManager atcManager, Node atcNode) {

		Element atcElement = (Element) atcNode;
		String type = atcElement.getAttribute("type");
		String strID=atcElement.getAttribute("id");
		String strX = atcElement.getAttribute("x");
		String strY = atcElement.getAttribute("y");
		float speed = Float.parseFloat(atcElement.getAttribute("speed"));
		float trooly_speed = Float.parseFloat(atcElement.getAttribute("trolly_speed"));
		float w = BlockManager.conW * BlockManager.ROW + 4;
		float h = BlockManager.conH;

		SimATC atc = null;

		if (atcManager.getType().equals("cross")) {

			if (type.equals("sea")) {
				atc = new CrossATC2(this, "atc_sea-" + blockID, Integer.parseInt(strID), blockID, Integer.parseInt(strX), Integer.parseInt(strY), w, h, SimATC.TYPE_SEA);
			}
			else if (type.equals("land")) {
				atc = new CrossATC2(this, "atc_land-" + blockID, Integer.parseInt(strID), blockID, Integer.parseInt(strX), Integer.parseInt(strY), w, h, SimATC.TYPE_LAND);
			}


		} else if (atcManager.getType().equals("twin")) {
			if (type.equals("sea")) {
				atc = new TwinATC(this, "atc_sea-" + blockID, Integer.parseInt(strID), blockID, Integer.parseInt(strX), Integer.parseInt(strY), w, h, SimATC.TYPE_SEA);
			} else if (type.equals("land")) {
				atc = new TwinATC(this, "atc_land-" + blockID, Integer.parseInt(strID), blockID, Integer.parseInt(strX), Integer.parseInt(strY), w, h, SimATC.TYPE_LAND);
			}
		}


		attach(atc);
		NodeList initLocations = atcElement.getElementsByTagName("initLcation");
		Element initLocation = (Element) initLocations.item(0);
		atc.setInitBlockLocation(Integer.parseInt(initLocation.getAttribute("row")), Integer.parseInt(initLocation.getAttribute("bay")));
		atc.setSpeed(speed);
		atc.setTrollySpeed(trooly_speed);
		atcManager.addSimModel(atc);
		canvas.addObject(atc);

	}

	private int createBlock(Element node) {

		Element blockElement = node;
		blockElement.getNodeName();
		blockElement.getAttribute("id");
		int blockID = Integer.parseInt(blockElement.getAttribute("id"));
		int x = Integer.parseInt(blockElement.getAttribute("x"));
		int y = Integer.parseInt(blockElement.getAttribute("y"));

		Block blocks = blockManager.getBlock(blockID);

		attach(blocks);
		blocks.setLocation(x, y);
		canvas.addObject(blocks);

		return blockID;
	}

	/**
	 * @throws ParserConfigurationException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SAXException
	 *
	 */
	public void init()
	{
		createInit();
	}

	/**
	 *start simulation
	 *init block view, atc viw
	 */
	public void simulationStart()
	{

		time = 0;
		start = true;
		atcManagerImpl.simStart();
		canvas.start();
	}

	/**
	 * simulation stop
	 */
	public void simulationStop()
	{
		SimEvent event = new SimEvent(0);
		event.setEventMessage("simstop");
		jobManager.append(event);
		atcManagerImpl.simStop();
	}



	public void clear() {
		jobManager.clear();
		blockManager.clear();
		canvas.clear();
	}


	public void blockInit() {

		blockManager.blockInit(this);
		SimEvent event = new SimEvent();
		event.put("type", "block");
		blockManager.notifyMonitor(event);
	}

	public void putOrder() {
		jobManager.putOrder();

	}

	public void append(SimEvent event) {
		jobManager.append(event);

	}

	public void putCommand(String command) throws ArrayIndexOutOfBoundsException, UnparserableCommandException {
		jobManager.putCommand(command);
	}

	public Node getRoot() {
		return element;
	}

	public void updateATCSpeed(float speed, float trolly_speed) {
		SimEvent event = new SimEvent(0, SimEvent.TYPE_COMMAND);
		event.setCommandType(SimEvent.COMMAND_UPDATE_SPEED);

		event.put("speed", speed);

		event.put("trolly_speed", trolly_speed);
		ATCJobManager.TROLLY_SPEED = trolly_speed;
		ATCJobManager.SPEED = speed;
		atcManagerImpl.append(event);

	}

	public void updateTrollySpeed(float speed) {
		SimEvent event = new SimEvent(0, SimEvent.TYPE_COMMAND);
		event.setCommandType(SimEvent.COMMAND_UPDATE_SPEED);

		event.put("trolly_speed", speed);


		atcManagerImpl.append(event);

	}


	/**
	 * auto start simulation
	 */
	public void autoStart() {
		event[0][0].start();
		event[0][1].start();
		event[1][0].start();
		event[1][1].start();

	}



	/**
	 * @param node
	 * @throws InterruptedException
	 */
	public void process(SimNode node) throws InterruptedException {
		SimEvent event = (SimEvent) node;

		if (event.getEventMessage().equals("error")) {
			JOptionPane.showMessageDialog(null, "error");
		}

	}

	@Override
	public SimulationInfo getSimulatinoInfo() {
		// TODO Auto-generated method stub
		return simulationInfo;
	}


}
