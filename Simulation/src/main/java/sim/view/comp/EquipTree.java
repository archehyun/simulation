package sim.view.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EquipTree extends JPanel implements ActionListener {

	SimMainImpl simMain = (SimMainImpl) SimMainImpl.getInstance();

	private JTree tree;

	SimFrame frame;

	public EquipTree(SimFrame frame) {
		this.frame = frame;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Equip List"));
		setPreferredSize(new Dimension(200, 200));

		tree = new JTree();

		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}

		JPanel pnSouth = new JPanel(new GridLayout(1, 3));
		JButton butSelect = new JButton("Select");
		butSelect.addActionListener(this);
		JButton butReload = new JButton("Reload");
		butReload.addActionListener(this);
		JButton butSave = new JButton("Save");

		butSave.setEnabled(false);
		butSave.addActionListener(this);
		pnSouth.add(butSelect);
		pnSouth.add(butReload);
		pnSouth.add(butSave);
		add(tree);
		add(pnSouth, BorderLayout.SOUTH);

	}

	public void load()

	{
			tree.setModel(new MyTreeMode(builtTreeNode(simMain.getRoot())));
			for (int i = 0; i < tree.getRowCount(); i++) {
				tree.expandRow(i);
			}

	}

	private DefaultMutableTreeNode builtTreeNode(Node root) {
		DefaultMutableTreeNode dmtNode;

		dmtNode = new DefaultMutableTreeNode(root.getNodeName());
		NodeList nodeList = root.getChildNodes();
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);

			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.hasChildNodes()) {
					dmtNode.add(builtTreeNode(tempNode));
				}
			}
		}
		return dmtNode;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			String command = e.getActionCommand();
			if (command.equals("Select")) {

				String fileName = frame.jFileChooserUtil();


				if (fileName.equals("")) {
					return;
				}
				simMain.parse("layout/" + fileName);
			} else {
				simMain.parse("layout/layout.xml");
			}


			tree.setModel(new MyTreeMode(builtTreeNode(simMain.getRoot())));
			for (int i = 0; i < tree.getRowCount(); i++) {
				tree.expandRow(i);
			}
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

	}

	class MyTreeMode extends DefaultTreeModel {

		public MyTreeMode(TreeNode root) {
			super(root);
		}

	}
}
