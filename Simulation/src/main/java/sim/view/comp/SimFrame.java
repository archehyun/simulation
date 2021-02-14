package sim.view.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.UnparserableCommandException;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.view.framework.SimCanvas;

/**
 * @version 1.0
 * @author 박창현
 *
 */
public class SimFrame extends JFrame implements ActionListener {

	private int width2 = 1100;

	protected static Logger logger = Logger.getLogger(SimFrame.class.getName());

	//private OptionDialog optionDialog;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private SimView txfArea;

	private SimInfoPanel infoPanel;

	private OrderPanel orderPn;

	private JCheckBox bOX;

	SimMainImpl simMain = (SimMainImpl) SimMainImpl.getInstance();

	SimCanvas canvas = new SimCanvas(simMain);

	private JToggleButton butStart;

	private JMenuItem startMenuItem;

	private JMenuItem stopMenuItem;

	private JButton butAuto;

	private JCheckBox cbxCount;

	private JButton butNext;

	public SimFrame() {

		simMain.setCanvas(canvas);

		this.setTitle("Stoage Simualtion");

		JPanel pnMain = new JPanel(new BorderLayout());

		txfArea = new SimView();

		infoPanel = new SimInfoPanel(this);

		JTabbedPane pane = new JTabbedPane();

		JPanel pnMap = (JPanel) buildMap();

		pnMap.add(canvas);

		pane.addTab("Map", pnMap);

		pane.addTab("Log", txfArea);
		JPanel pnCenter = new JPanel(new BorderLayout());
		pnCenter.add(pane);
		pnCenter.add(buildControl(), BorderLayout.SOUTH);

		JTabbedPane leftPane = new JTabbedPane();
		leftPane.addTab("Sim", buildSimulationInfo());
		leftPane.addTab("Order", buildOrderList());

		pnMain.add(pnCenter);
		pnMain.add(infoPanel, BorderLayout.EAST);
		pnMain.add(leftPane, BorderLayout.WEST);
		pnMain.add(getButtons(), BorderLayout.NORTH);
		pnMain.add(new JPanel(), BorderLayout.SOUTH);

		getContentPane().add(pnMain);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				JFrame frame = (JFrame) e.getComponent();

				float wrate = frame.getSize().width / ((BlockManager.conW + BlockManager.wGap) * 75);
				float hrate = frame.getSize().height / ((BlockManager.conH + BlockManager.hGap) * 40);
				frame.getSize();
				BlockManager.blockRate = hrate;
				BlockManager.blockWRate = wrate;
				BlockManager.blockHRate = hrate;

			}
		});


		this.setJMenuBar(createMenuBar());

		setSize(width2, 725);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ViewUtil.center(this);
		setVisible(true);
		setAlwaysOnTop(true);

		init("layout.xml");

	}

	private JComponent addItem(String title, JComponent comp)
	{
		JPanel pnMain = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel comp2 = new JLabel(title+":");
		comp2.setPreferredSize(new Dimension(75, 15));
		pnMain.add(comp2);
		pnMain.add(comp);
		return pnMain;
	}
	public JComponent buildSimulationInfo()
	{

		JPanel pnMain = new JPanel(new GridLayout(2, 1));

		JPanel pnCenter = new JPanel();

		pnMain.setBorder(BorderFactory.createTitledBorder("Simulation"));
		BoxLayout box = new BoxLayout(pnCenter, BoxLayout.Y_AXIS);
		pnCenter.setLayout(box);

		JTextField txfTPeid = new JTextField(15);
		JTextField txfTime = new JTextField(15);
		JTextField txfRepeat = new JTextField(15);
		JComboBox cbxFuntion = new JComboBox<>();
		cbxFuntion.addItem("�룷�븘�넚 遺꾪룷");
		cbxFuntion.addItem("�씪�뼇 遺꾪룷");

		JSlider slInOut = new JSlider(0, 10);
		slInOut.setMajorTickSpacing(1);
		slInOut.setPaintTicks(true);

		slInOut.setPaintLabels(true);

		Hashtable inOutTable = new Hashtable();
		inOutTable.put(new Integer(0), new JLabel("In"));
		inOutTable.put(new Integer(5), new JLabel("0.5"));
		inOutTable.put(new Integer(10), new JLabel("Out"));
		slInOut.setLabelTable(inOutTable);

		JSlider slSeaLand = new JSlider(0, 10);
		slSeaLand.setMajorTickSpacing(1);
		slSeaLand.setPaintTicks(true);
		slSeaLand.setPaintLabels(true);
		Hashtable seaLandTable = new Hashtable();
		seaLandTable.put(new Integer(0), new JLabel("Sea"));
		seaLandTable.put(new Integer(5), new JLabel("0.5"));
		seaLandTable.put(new Integer(10), new JLabel("Land"));
		slSeaLand.setLabelTable(seaLandTable);

		pnCenter.add(addItem("�떆�뿕 湲곌컙", txfTPeid));
		pnCenter.add(addItem("�떆媛� 媛꾧꺽", txfTime));
		pnCenter.add(addItem("諛섎났�슏�닔", txfRepeat));
		pnCenter.add(addItem("�옖�뜡�븿�닔", cbxFuntion));

		pnCenter.add(addItem("InOut", slInOut));

		pnCenter.add(addItem("SeaLand", slSeaLand));

		JPanel pnControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton butSave = new JButton("���옣");
		pnControl.add(butSave);

		pnMain.add(pnCenter);

		pnMain.add(pnControl);

		return pnMain;
	}

	public String jFileChooserUtil() {

		String folderPath = "";

		JFileChooser chooser = new JFileChooser(this.getClass().getResource("/layout").getPath()); // �뵒�젆�넗由� �꽕�젙
		//chooser.setCurrentDirectory(new File("/layout")); // �쁽�옱 �궗�슜 �뵒�젆�넗由щ�� 吏��젙
		chooser.setAcceptAllFileFilterUsed(true);   // Fileter 紐⑤뱺 �뙆�씪 �쟻�슜
		chooser.setDialogTitle("�젅�씠�븘�썐 �꽑�깮"); // 李쎌쓽 �젣紐�
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // �뙆�씪 �꽑�깮 紐⑤뱶

		FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "xml"); // filter �솗�옣�옄 異붽�
		chooser.setFileFilter(filter); // �뙆�씪 �븘�꽣瑜� 異붽�
		int returnVal = chooser.showOpenDialog(SimFrame.this); // �뿴湲곗슜 李� �삤�뵂

		if (returnVal == JFileChooser.APPROVE_OPTION) { // �뿴湲곕�� �겢由�
			folderPath = chooser.getSelectedFile().getName();
		} else if (returnVal == JFileChooser.CANCEL_OPTION) { // 痍⑥냼瑜� �겢由�
			folderPath = "";
		}

		return folderPath;

	}

	public JComponent buildMap()
	{

		JPanel pnMain = new JPanel(new BorderLayout());

		JPanel pnCheck = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JCheckBox cbxAll = new JCheckBox("All", true);

		cbxAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				canvas.setFrameView(cbxAll.isSelected());
				canvas.setTimeView(cbxAll.isSelected());
				canvas.setCountView(cbxAll.isSelected());

			}
		});

		JCheckBox cbxFrame = new JCheckBox("Frame",true);

		cbxFrame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				canvas.setFrameView(cbxFrame.isSelected());

			}
		});
		JCheckBox cbxTime = new JCheckBox("Time",true);

		cbxTime.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				canvas.setTimeView(cbxTime.isSelected());

			}
		});

		cbxCount = new JCheckBox("count");
		cbxCount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setCountView(cbxCount.isSelected());

			}
		});

		pnCheck.add(cbxAll);
		pnCheck.add(cbxFrame);
		pnCheck.add(cbxTime);
		pnCheck.add(cbxCount);


		JSlider pnJSlider = new JSlider(1, 11, 5);
		pnJSlider.setMajorTickSpacing(5);
		pnJSlider.setPaintLabels(true);
		pnJSlider.setPaintTicks(true);
		pnCheck.add(pnJSlider);
		pnMain.add(pnCheck, BorderLayout.NORTH);
		pnMain.add(canvas);

		return pnMain;
	}



	/**
	 * @return
	 */
	private JComponent buildOrderList() {



		orderPn = new OrderPanel();
		orderPn.setBorder(BorderFactory.createTitledBorder("Order"));

		orderPn.setPreferredSize(new Dimension(420, 50));

		return orderPn;
	}

	private JPanel buildControl() {
		JPanel pnControl = new JPanel(new BorderLayout());

		JTextField txfInput = new JTextField();


		txfInput.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					JTextField tf = (JTextField) e.getSource();
					//System.out.println(tf.getText() + ":enter");

					try {
						if (tf.getText().equals("?")) {
							tf.setText("W-I-15-1-1");
							return;
						} else {

						}
						simMain.putCommand(tf.getText());
					} catch (UnparserableCommandException e1) {
						JOptionPane.showMessageDialog(SimFrame.this, "command error");
					} catch (ArrayIndexOutOfBoundsException e2) {

						JOptionPane.showMessageDialog(SimFrame.this, "index error");

					}
				}
			}
		});
		JButton butSend = new JButton("Send");
		pnControl.add(txfInput);
		pnControl.add(butSend, BorderLayout.EAST);
		return pnControl;
	}

	private JMenuBar createMenuBar()
	{
		JMenuBar bar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setMnemonic('O');
		openMenuItem.addActionListener(this);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic('x');
		exitMenuItem.addActionListener(this);
		fileMenu.add(openMenuItem);
		fileMenu.add(exitMenuItem);
		JMenu simulationMenu = new JMenu("Simulation");
		simulationMenu.setMnemonic('S');
		startMenuItem = new JMenuItem("Start");
		startMenuItem.setEnabled(false);
		startMenuItem.addActionListener(this);

		simulationMenu.add(startMenuItem);
		stopMenuItem = new JMenuItem("Stop");
		stopMenuItem.setEnabled(false);
		stopMenuItem.addActionListener(this);
		simulationMenu.add(stopMenuItem);

		JMenu viewMenu = new JMenu("View");
		JMenuItem backgroundMenu = new JMenuItem("Background Color");
		viewMenu.add(backgroundMenu);

		JMenu optionMenu = new JMenu("Options");
		optionMenu.setMnemonic('O');

		JMenuItem atcOptionMenu = new JMenuItem("Option");
		atcOptionMenu.addActionListener(this);

		optionMenu.add(atcOptionMenu);

		bar.add(fileMenu);
		bar.add(simulationMenu);
		bar.add(viewMenu);
		bar.add(optionMenu);
		return bar;
	}

	/**
	 * @return
	 */
	public JComponent getButtons() {

		JPanel pnMain = new JPanel(new BorderLayout());
		JPanel pnLeftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));

		bOX = new JCheckBox("Init");
		bOX.setSelected(true);

		pnLeftButtons.add(bOX);

		butStart = new JToggleButton();
		butStart.setEnabled(false);

		//JMenuItem
		/*     file_New_txt.setAccelerator(KeyStroke.getKeyStroke
		                  ('N',InputEvent.CTRL_MASK)); //Ctrl+N
		 */

		butStart.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				abstractButton.getModel();

				if (abstractButton.isSelected()) {

					abstractButton.setText("Stop");
					butAuto.setEnabled(true);
					butNext.setEnabled(true);
					simMain.clear();

					simMain.createInit();

					orderPn.updateView(BlockManager.block);

					simMain.simulationStart();
				} else {
					abstractButton.setText("Start");

					butAuto.setEnabled(false);
					butNext.setEnabled(false);
					simMain.simulationStop();
					simMain.clear();
				}
			}
		});


		butStart.setText("Start");
		butStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (bOX.isSelected()) {
					simMain.blockInit();
				}
			}
		});

		butAuto = new JButton("Auto");
		butAuto.setMnemonic('A');

		butAuto.setEnabled(false);

		butAuto.addActionListener(this);

		butNext = new JButton("Next");
		butNext.setMnemonic('N');

		butNext.setEnabled(false);

		butNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimEvent event = new SimEvent(0);
				event.setEventMessage("simstart");

				simMain.putOrder();

			}
		});

		pnLeftButtons.add(butStart);
		pnLeftButtons.add(butAuto);
		pnLeftButtons.add(butNext);

		JPanel pnRightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton butOption = new JButton("Option");

		butOption.addActionListener(this);

		JButton butClear = new JButton("Clear");
		butClear.setEnabled(false);
		butClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new OptionDialog();
			}
		});

		pnRightButtons.add(butOption);
		pnRightButtons.add(butClear);

		pnMain.add(pnLeftButtons);
		pnMain.add(pnRightButtons, BorderLayout.EAST);

		return pnMain;
	}


	class OptionDialog extends JDialog implements ActionListener {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private JTextField txfATCSpeed;

		private JTextField txfTrollySpeed;

		public OptionDialog() {

			createAndView();

		}

		private void createAndView() {

			this.setTitle("ATC Update");
			JPanel pnMain = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JButton butSave = new JButton("Save");
			butSave.setMnemonic('S');
			butSave.addActionListener(OptionDialog.this);
			txfATCSpeed = new JTextField(10);
			txfTrollySpeed = new JTextField(10);
			pnMain.add(new JLabel("ATC Speed: "));
			pnMain.add(txfATCSpeed);
			pnMain.add(new JLabel("Trolly Speed: "));
			pnMain.add(txfTrollySpeed);
			pnMain.add(butSave);

			this.setModal(true);

			txfATCSpeed.setText(String.valueOf(ATCJobManager.SPEED));
			txfTrollySpeed.setText(String.valueOf(ATCJobManager.TROLLY_SPEED));
			this.getContentPane().add(pnMain);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.pack();
			this.setResizable(false);
			this.setLocationRelativeTo(SimFrame.this);
			this.setAlwaysOnTop(true);
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			if (command.equals("Save")) {


				String strSpeed = txfATCSpeed.getText();
				String strTrollySpeed = txfTrollySpeed.getText();
				try {

					float speed = Float.parseFloat(strSpeed);
					float trolly_speed = Float.parseFloat(strTrollySpeed);
					simMain.updateATCSpeed(speed, trolly_speed);

					setVisible(false);
					this.dispose();

				} catch (NumberFormatException ee) {
					txfATCSpeed.setText(String.valueOf(ATCJobManager.SPEED));

					JOptionPane.showMessageDialog(this, "Only Integer");
				}
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Option")) {
			new OptionDialog();
		}
		else if (command.equals("Auto")) {
			SimEvent event = new SimEvent(0);
			event.setEventMessage("simstart");

			simMain.append(event);
			simMain.autoStart();

			butAuto.setEnabled(false);
			butNext.setEnabled(false);
		}
		else if (command.equals("Start")) {
			if (bOX.isSelected()) {
				simMain.blockInit();
			}
			butStart.setSelected(true);
			butNext.setEnabled(true);
			JMenuItem item = (JMenuItem) e.getSource();
			item.setEnabled(false);
			stopMenuItem.setEnabled(true);

			//			infoPanel.updateView();

		} else if (command.equals("Stop")) {
			butStart.setSelected(false);
			JMenuItem item = (JMenuItem) e.getSource();
			item.setEnabled(false);
			startMenuItem.setEnabled(true);
		}
		else if (command.equals("Exit")) {
			System.exit(1);
		}

		else if (command.equals("Open")) {

			String fileName = jFileChooserUtil();


			if (fileName.equals("")) {
				return;
			}

			init(fileName);

		}

	}

	public void init(String fileName) {
		try {
			simMain.parse("layout/" + fileName);
			simMain.createInit();
			infoPanel.updateView();
			butStart.setEnabled(true);
			startMenuItem.setEnabled(true);
		} catch (SAXException | IOException | URISyntaxException | ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
