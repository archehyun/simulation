package sim.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import sim.model.SimulationMain;
import sim.view.SimCanvas;

public class MainFrame extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	SimCanvas canvas;

	private SimulationMain simulationMain;

	private JButton butStart;

	private JButton butStop;
	
	public MainFrame() {
		
		simulationMain = new SimulationMain();
		canvas = new SimCanvas(simulationMain);
		
		this.getContentPane().add(canvas);
		this.getContentPane().add(createControl(),BorderLayout.SOUTH);
		
		this.setSize(1070, 500);
		
		ViewUtil.center(this);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		canvas.start();
		
	}
	
	private JPanel createControl()
	{
		JPanel pnMain = new JPanel();
		JTextField txfSpeed = new JTextField(10);
		
		txfSpeed.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar()==KeyEvent.VK_ENTER)					
				{
					
					String strSpeed =txfSpeed.getText();
					double speed = Double.valueOf(strSpeed);
					
					simulationMain.setParam("speed", speed);
					
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		pnMain.add(txfSpeed);
		pnMain.add(new JLabel("m/s "));
		butStart = new JButton("Start");
		butStart.addActionListener(this);
		pnMain.add(butStart);
		
		butStop = new JButton("Stop");
		butStop.addActionListener(this);
		butStop.setEnabled(false);
		pnMain.add(butStop);
		
		return pnMain;
				
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if("Start".equals(command))
		{
			simulationMain.start();
			butStop.setEnabled(true);
			butStart.setEnabled(false);
			
		}
		
		if("Stop".equals(command))
		{
			simulationMain.stop();
			butStop.setEnabled(false);
			butStart.setEnabled(true);
		}
		
	}

}
