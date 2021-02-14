package sim.view.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import sim.model.impl.stoage.block.BlockManager;

public class SimInfoPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	EquipPanel equipTable[];
	EquipTree equipTree;
	JTabbedPane pane;

	JPanel pnTable;

	SimFrame frame;

	public SimInfoPanel(SimFrame frame) {
		this.setLayout(new BorderLayout());
		pane = new JTabbedPane();
		equipTree = new EquipTree(frame);
		pnTable = new JPanel();
		pane.addTab("Tree", equipTree);
		pane.addTab("Table", pnTable);
		setPreferredSize(new Dimension(260, 100));
		this.add(pane);


	}

	private void createView() {
		//		pnTable.setBackground(Color.black);
		pnTable.setLayout(new GridLayout(0, 1));
		equipTable = new EquipPanel[BlockManager.block];
		for (int i = 0; i < BlockManager.block; i++) {
			equipTable[i] = new EquipPanel(i);
			pnTable.add(equipTable[i]);
		}
		pnTable.setPreferredSize(new Dimension(260, 100));

	}


	public void updateView() {
		createView();
		equipTree.load();
	}



}
