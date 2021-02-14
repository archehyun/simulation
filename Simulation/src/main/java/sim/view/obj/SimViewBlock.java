package sim.view.obj;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimQueue;
import sim.view.framework.SimCanvas;
import sim.view.framework.SimViewObject;

public class SimViewBlock extends SimViewObject{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int blockContainerCount;

	private int xx, yy;

	private Block block;

	BlockManager manager = BlockManager.getInstance();

	int hGap=5;

	private float totalSlot;

	private float persent;

	/**
	 * @param id
	 * @param x
	 * @param y
	 */
	public SimViewBlock(SimCanvas canvas, int id, int x, int y)
	{
		super(canvas, id, x, y, BlockManager.conW, BlockManager.conH);

		block=manager.getBlock(id);

		totalSlot = block.getTotalSlot();

		manager.addMonitor(this);
	}

	@Override
	public void draw(Graphics g) {

		drawTP(g);

		drawBlock(g, BlockManager.blockRate);

		drawStatics(g, BlockManager.blockRate);


	}

	private void drawTP(Graphics g) {

		g.setColor(Color.blue);

		int ww = convertW((int) bounds.width);
		int hh = convertH((int) bounds.height);

		int xx,yy;

		int Sea_TP[] = block.getTPUse(StoageEvent.SEA);

		SimQueue queue = block.getQueue(StoageEvent.SEA);
		int size = queue.getSize();


		int tempX = 0;
		for (int j = 0; j < Sea_TP.length; j++) {
			xx = convertViewX((int)(initX + j * (bounds.width + BlockManager.wGap)));
			yy = convertViewY((int) (initY - (bounds.height + BlockManager.hGap)));
			tempX = xx;
			if (Sea_TP[j] == Block.FULL_TP)
			{
			g.fillRect(xx, yy, ww, hh);
			} else if (Sea_TP[j] == Block.EMPTY_TP) {
				g.drawRect(xx, yy, ww, hh);
			}
		}
		g.drawString(String.valueOf(size), tempX + 5, (int) initY);

		int Land_TP[] = block.getTPUse(StoageEvent.LAND);


		//land side

		for (int j = 0; j < block.getRow(); j++) {

			xx = convertViewX((int) (initX + j * (bounds.width + BlockManager.wGap)));
			yy = convertViewX((int) ((initY + (block.getBay()) * (bounds.height + BlockManager.hGap))));

			tempX = xx;
			if (Land_TP[j] == Block.FULL_TP) {
			g.fillRect(xx, yy, ww, hh);
			} else if (Land_TP[j] == Block.EMPTY_TP) {
				g.drawRect(xx, yy, ww, hh);
			}
		}
		g.drawString(String.valueOf(size), tempX + 5, convertViewY((int) (initY + (block.getBay()) * (bounds.height + BlockManager.hGap))));

	}

	private void drawStatics(Graphics g, float blockRate) {

		try {
			blockContainerCount = block.getContainerCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.setColor(Color.white);

		int xx = (int) (initX * blockRate);

		int yy = (int) ((initY + 12 + block.getBay() * (bounds.height + BlockManager.hGap)) * blockRate + canvas.getMargin());

		g.drawString(blockContainerCount + "/" + (int) totalSlot + "(" + (int) persent + "%)", xx - 5, yy);
	}

	private Color getContainerLevel(int count) {

		Color color;
		switch (count) {
		case 2:
			color = Color.gray;

			break;
		case 3:

			color = Color.GREEN;

			break;
		case 4:

			color = Color.BLUE;

		case 5:
			color = Color.RED;

			break;
		default:

			color = Color.CYAN;
			break;

		}
		return color;
	}

	private void drawBlock(Graphics g, float blockRate) {

		g.setColor(Color.CYAN);

		for(int i=0;i<block.getBay();i++)
		{
			for(int j=0;j<block.getRow();j++)
			{
				int blockContainerCount = block.slotCount(i, j);

				g.setColor(getContainerLevel(blockContainerCount));



				int xx = convertViewX((int) ((initX + j * (bounds.width + BlockManager.wGap))));
				int yy = convertViewY((int) ((initY + i * (bounds.height + (i > 0 ? BlockManager.hGap : 0)))));



				int ww = convertW((int) bounds.width);
				int hh = convertH((int) bounds.height);


				g.fillRect(xx, yy, ww, hh);

				if (isCountView) {

					g.setColor(Color.black);

					g.drawString(blockContainerCount + "", xx - 1, yy + 11);
				}
			}

			g.setColor(Color.white);
			yy = convertViewY((int) ((initY + i * (bounds.height + (i > 0 ? BlockManager.hGap : 0))))) + convertH(BlockManager.conH / 2);
			g.drawString(i + "", convertViewX((int) initX) - 20, yy);
		}
	}

	@Override
	public void updateMonitor(SimEvent message) {
		String type = (String) message.get("type");
		if (type.equals("block")) {
			try {
				blockContainerCount = block.getContainerCount();

				persent = (blockContainerCount / totalSlot) * 100;
			} catch (Exception e) {
				persent = 0;
			}

		}
	}

	boolean isCountView;

	@Override
	public void setCountView(boolean selected) {
		isCountView = selected;
	}

}
