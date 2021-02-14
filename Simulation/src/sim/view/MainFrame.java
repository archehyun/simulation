package sim.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.Scene;
import core.SimCore;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	
	
	Scene scene = new Scene();
	
	SimCore core;
	
	public MainFrame() {
		
		
		//core
		core = new SimCore(scene);
		
		JTable table = new JTable();
		DefaultTableModel  model= new DefaultTableModel();
		model.addColumn("TestA");
		model.addColumn("TestB");
		table.setModel(model);
		
		
		JPanel pnMain = new JPanel(new BorderLayout());
		
		
		pnMain.add(scene);
		
		
		JPanel pnControl = new JPanel();
		
		JButton jButton = new JButton("»Æ¿Œ");
		jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				core.start();
				
			}
		});
		pnControl.add(jButton);		
		
		this.getContentPane().add(pnControl,BorderLayout.SOUTH);
		
		this.setSize(new Dimension(400,400));
		
		scene.setBounds(10, 10, this.getWidth()-40, this.getHeight()-40);
		ViewUtil.center(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.getContentPane().add(pnMain);
		
	}

}
