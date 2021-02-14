package sim.view.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import sim.model.core.SimEvent;
import sim.model.core.SimTimestamp;
import sim.model.impl.stoage.commom.JobManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.view.framework.IFMonitor;

public class OrderPanel extends JPanel implements IFMonitor {

	//

	OrderTable[] orderTables;


	public OrderPanel() {

		this.setLayout(new BorderLayout());

		JobManager.getInstance().addMonitor(this);

	}


	class ColumnData
	{
		public String  m_title;
		public int     m_width;
		public int     m_alignment;

		public ColumnData(String title, int width, int alignment) {
			m_title = title;
			m_width = width;
			m_alignment = alignment;
		}
	}

	/**
	 * simulation data
	 *
	 * @author archehyun
	 *
	 */
	class SimulationData {

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		StoageEvent event;

		public SimulationData(StoageEvent event) {
			this.event = event;
		}

		public String getID() {
			return getBlockID() + "-" + getJobID();
		}

		public int getJobID()
		{
			return event.getJobID();
		}

		public int getBlockID() {
			return (int) event.get("blockID");
		}

		public String getInOutType() {
			return (int) event.get("inOutType") == StoageEvent.INBOUND ? "I" : "O";
		}

		public String getLocation() {
			return event.get("bay") + "-" + event.get("row") + "-" + event.get("tier");
		}

		public String getStartTime() {
			return SimTimestamp.toTimestmp((long) event.get("startTime"));

		}

		public String getEndTime() {

			if (event.get("endTime") != null) {
				return SimTimestamp.toTimestmp((long) event.get("endTime"));
			} else {
				return "00:00:00";
			}
		}

		public String getServiceTimeStr() {
			return event.get("endTime") == null ? "00:00:00" : SimTimestamp.toTimestmp((long) event.get("endTime") - (long) event.get("startTime"));
		}

		public long getServiceTime() {

			if (event.get("endTime") == null) {
				return 0;
			} else {

				return (long) event.get("endTime") - (long) event.get("startTime");
			}
		}

	}

	class SimpleTableModel extends AbstractTableModel {

		protected Vector<SimulationData> vdata;

		private long averageSeviceTime = 0;

		private int inboundCount = 0;

		private int outboundCount = 0;

		final public ColumnData m_columns[] = {
				new ColumnData("ID", 60, JLabel.LEFT),
				new ColumnData("InOut", 60, JLabel.RIGHT),
				new ColumnData("Location", 80, JLabel.RIGHT),
				new ColumnData("S-Time", 100, JLabel.RIGHT),
				new ColumnData("End-Time", 100, JLabel.RIGHT),
				new ColumnData("Service", 100, JLabel.RIGHT) };

		private long maxServiceTime;

		private long minServiceTime;

		public SimpleTableModel() {

			vdata = new Vector<>();

		}

		public long getAverageSeviceTime() {
			return averageSeviceTime;
		}

		public void setAverageSeviceTime(long averageSeviceTime) {
			this.averageSeviceTime = averageSeviceTime;
		}


		public void statics() {

			synchronized (vdata) {
				Vector<SimulationData> datas = new Vector<SimulationData>();

				Iterator<SimulationData> iter = vdata.iterator();
				long totalService = 0;
				int InCount = 0;
				int OutCount = 0;

				while (iter.hasNext()) {
					SimulationData item = iter.next();
					if ((int) item.event.get("inOutType") == StoageEvent.INBOUND) {
						InCount++;
					} else if ((int) item.event.get("inOutType") == StoageEvent.OUTBOUND) {
						OutCount++;
					}

					if (item.event.get("endTime") != null) {
						datas.add(item);

						long startTime = (long) item.event.get("startTime");
						long endTime = (long) item.event.get("endTime");
						totalService += (endTime - startTime);
					}
				}

				Iterator<SimulationData> iter2 = datas.iterator();



				if (datas.size() == 0)
					return;

				long minService = datas.get(0).getServiceTime();
				long maxService = datas.get(0).getServiceTime();
				while (iter2.hasNext()) {
					SimulationData item = iter2.next();
					if (item.getServiceTime() > maxService) {
						maxService = item.getServiceTime();
					}
					if (item.getServiceTime() < minService) {
						minService = item.getServiceTime();
					}
				}



				vdata.iterator();


				inboundCount = InCount;
				outboundCount = OutCount;


				averageSeviceTime = datas.size() == 0 ? 0 : totalService / datas.size();

				maxServiceTime = maxService;
				minServiceTime = minService;

			}

		}

		public long getMinServiceTime() {
			return minServiceTime;
		}

		public long getMaxServiceTime() {
			return maxServiceTime;
		}

		public void append(Object event) {

			synchronized (vdata) {
				StoageEvent event1 = (StoageEvent) event;

				Iterator<SimulationData> iter = vdata.iterator();
				boolean exit = false;
				while (iter.hasNext()) {
					SimulationData item = iter.next();
					if (event1.getJobID() == item.getJobID()) {
						exit = true;
					}
				}
				if (!exit) {
					vdata.add(new SimulationData((StoageEvent) event));
				}
			}


		}

		public void update(SimEvent event) {


			StoageEvent event1 = (StoageEvent) event;

			append(event1);
			statics();

		}


		@Override
		public int getRowCount() {

			return vdata == null ? 0 : vdata.size();
			//return rowData.length;
		}

		@Override
		public int getColumnCount() {
			return m_columns.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return m_columns[columnIndex].m_title;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			boolean isEditable = false;

			return isEditable;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < 0 || rowIndex >= getRowCount())
				return "";
			SimulationData row = vdata.elementAt(rowIndex);
			switch (columnIndex) {
			case 0:
				return row.getID();
			case 1:
				return row.getInOutType();
			case 2:
				return row.getLocation();
			case 3:
				return row.getStartTime();
			case 4:
				return row.getEndTime();
			case 5:
				return row.getServiceTimeStr();
			}
			return "";
		}
	}

	@Override
	public void updateMonitor(SimEvent message) {

		for (int i = 0; i < orderTables.length; i++) {
			orderTables[i].update((StoageEvent) message);
		}



	}

	class OrderTable extends JPanel {

		JLabel lblAVG;

		JLabel lblInbound, lblOutbound;

		private JLabel lblMAX;

		private JLabel lblMIN;

		JTable table;

		int blockID;

		public int getBlockID() {
			return blockID;
		}

		private SimpleTableModel model;

		protected boolean endMove = false;

		public OrderTable(int blockID) {

			this.blockID = blockID;
			this.setLayout(new BorderLayout());
			table = new JTable();

			table.setAutoCreateColumnsFromModel(false);
			model = new SimpleTableModel();
			table.setModel(model);

			table.getColumnModel();

			// Set column widths
			for (int k = 0; k < model.m_columns.length; k++) {
				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setHorizontalAlignment(model.m_columns[k].m_alignment);
				TableColumn column = new TableColumn(k, model.m_columns[k].m_width, renderer, null);
				table.addColumn(column);
			}

			JPanel pnInfo1 = new JPanel(new GridLayout(1, 3));
			lblAVG = new JLabel("00:00:00");
			lblMAX = new JLabel("00:00:00");
			lblMIN = new JLabel("00:00:00");

			lblInbound = new JLabel();
			lblOutbound = new JLabel();

			pnInfo1.add(createItem(lblAVG, "Avg"));
			pnInfo1.add(createItem(lblMAX, "Max"));
			pnInfo1.add(createItem(lblMIN, "Min"));

			JPanel pnInfo2 = new JPanel(new GridLayout(1, 2));

			pnInfo2.add(createItem(lblInbound, "Inbound"));
			pnInfo2.add(createItem(lblOutbound, "Outbound"));

			JPanel pnInfo = new JPanel(new GridLayout(2, 1));
			pnInfo.add(pnInfo1);
			pnInfo.add(pnInfo2);

			this.add(pnInfo, BorderLayout.NORTH);

			JScrollPane comp = new JScrollPane(table);
			comp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					JScrollBar src = (JScrollBar) e.getSource();
					if (endMove) {
						src.setValue(src.getMaximum());
					}
				}
			});

			this.add(comp);
			this.add(buildOption(), BorderLayout.SOUTH);

		}

		private Component buildOption() {
			JPanel pnMain = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JCheckBox box = new JCheckBox("Move End");

			box.addActionListener(new ActionListener() {


				@Override
				public void actionPerformed(ActionEvent e) {
					OrderTable.this.endMove = box.isSelected();
				}
			});
			box.setSelected(false);
			pnMain.add(box);
			return pnMain;
		}

		private JPanel createItem(JComponent comp, String text) {
			JPanel pnMain = new JPanel(new FlowLayout(FlowLayout.LEFT));

			JLabel comp2 = new JLabel(text + "  : ");

			Font font = new Font("����", Font.BOLD, 16);


			comp2.setForeground(Color.BLUE);
			comp2.setFont(font);


			pnMain.add(comp2);
			pnMain.add(comp);
			return pnMain;
		}

		public void update(StoageEvent event) {

			if ((int) event.get("blockID") == getBlockID()) {

				int selectedRow = table.getSelectedRow();

				int col = table.getSelectedColumn();
				model.update(event);

				table.tableChanged(new TableModelEvent(model));
				repaint();

				table.changeSelection(selectedRow, col, false, false);

				lblAVG.setText(SimTimestamp.toTimestmp(model.getAverageSeviceTime()));
				lblMAX.setText(SimTimestamp.toTimestmp(model.getMaxServiceTime()));
				lblMIN.setText(SimTimestamp.toTimestmp(model.getMinServiceTime()));

				lblInbound.setText(String.valueOf(model.inboundCount));
				lblOutbound.setText(String.valueOf(model.outboundCount));
			}


		}

	}



	public void updateView(int block) {

		orderTables = new OrderTable[block];
		JPanel pnMain = new JPanel(new GridLayout(block, 1));

		for (int i = 0; i < block; i++) {
			orderTables[i] = new OrderTable(i);
			pnMain.add(orderTables[i]);
		}
		this.add(pnMain);

	}

}
