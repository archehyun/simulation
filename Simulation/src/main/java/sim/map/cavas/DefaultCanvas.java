package sim.map.cavas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import sim.map.model.DoublePoint;
import sim.map.option.MapOptionManager;


public class DefaultCanvas extends AbstractCanvas implements MouseWheelListener, MouseMotionListener,MouseListener{



	private DoublePoint currentCenterPoint;
	private Point clickPoint;
	public MapOptionManager mapOptionManager = MapOptionManager.getInstance();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int startX;
	protected int endX;
	protected int startY;
	protected int endY;
	protected double mouseLat;
	protected double mouseLng;
	protected String currentTime;
	protected int stepSize;
	protected double STEP = 1000;
	protected int STEP_COUNT = 1;
	
	public int getStepCount() {
		return STEP_COUNT;
	}
	protected int mouseX;
	protected int mouseY;
	
	public int STEP_1=5;
	public int STEP_2=25;
	public int STEP_3=50;
	public int STEP_4=100;
	public int STEP_5=150;
	public int STEP_6=235;
	public int STEP_7=450;
	public int STEP_8=750;
	public int STEP_9=1000;
	public int STEP_10=1200;
	
	
	public double getSTEP() {
		return STEP;
	}
	public void setSTEP(double sTEP) {
		STEP = sTEP;
	}
	protected  SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

	public DefaultCanvas() {

		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);


	}
	@Override
	public void simulation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeTagMoveList(double x1, double y1, double x2, double y2) {
		// TODO Auto-generated method stub

	}

	@Override
	public int convertX(double lng) {

		return (int)(lng*lngPointRate);
	}

	@Override
	public int convertY(double lat) {
		return (int)(lat*latPointRate)*-1;
	}
	public int getXOnMapByLng(double lng)
	{
		return centerX+this.convertX(lng);
	}

	public int getYOnMapByLat(double lat)
	{
		return centerY+this.convertY(lat);
	}
	public double getLatOnMapByY(int y)
	{
		return this.convertLat(latCenterY-y);
	}
	public double getLngOnMapByX(int x)
	{
		return this.convertLng(lngCenterX-x)*-1;
	}
	
	//Draws a 20x20 grid using the current color.
    private void drawGrid2(Graphics g, int gridSpace) {
      Insets insets = getInsets();
      int firstX = insets.left;
      int firstY = insets.top;
      int lastX = getWidth() - insets.right;
      int lastY = getHeight() - insets.bottom;

      //Draw vertical lines.
      int x = firstX;
      while (x < lastX) {
        g.drawLine(x, firstY, x, lastY);
        x += gridSpace;
      }

      //Draw horizontal lines.
      int y = firstY;
      while (y < lastY) {
        g.drawLine(firstX, y, lastX, y);
        y += gridSpace;
      }
    }

	protected void drawGrid(Graphics g)
	{

		Graphics2D g2d = (Graphics2D) g;

		float[] dash3 = { 4f, 0f, 2f };

		BasicStroke bs3 = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash3,
				2f);


		g2d.setStroke(bs3);


			
		g.setColor(Color.blue);

		g2d.drawLine(lngCenterX, startY,lngCenterX, endY);
		g2d.drawLine(startX, latCenterY,endX, latCenterY);

		int rightX=0;

		String direction="E";




		int strCount=0;

		for(int i=0;i<180;i++)
		{
			rightX =getXOnMapByLng(getCenterLng()+i);
			{
				g.drawString(String.valueOf(i)+direction,rightX, startY+25);
			}

		}
		direction="W";
		for(int i=0;i<180;i++)
		{
			rightX =getXOnMapByLng(getCenterLng()-i);

			{
				g.drawString(String.valueOf(i)+direction,rightX, startY+25);
			}

		}
		//================
		direction="S";
		int upY=0;
		for(int i=0;i<90;i++)
		{
			upY =getYOnMapByLat(getCenterLat()+i);
			{
				g.drawString(String.valueOf(i)+direction,startX-25,upY );
			}

		}
		direction="N";
		for(int i=0;i<90;i++)
		{
			upY =getYOnMapByLat(getCenterLat()-i);
			{
				g.drawString(String.valueOf(i)+direction,startX-25,upY );
			}
		}

		g.setColor(Color.white);

		g.drawString("lat,lng:("+String.format("%.3f", mouseLat)+" ,"+String.format("%.3f", mouseLng)+")",
				this.getWidth()-margin*3, this.getHeight()-5);

		g.drawString("centerLat,centerLng:("+String.format("%.6f", -getCenterLat())+" ,"+String.format("%.6f", -getCenterLng())+")",
				startX, this.getHeight()-5);
	}

	protected void drawGrayGrid(Graphics g) {

		g.setColor(Color.gray.brighter());
		//�߾Ӽ� �׸���
		
		Graphics2D g2d = (Graphics2D) g;

		float[] dash3 = { 4f, 0f, 2f };

		BasicStroke bs3 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash3,
				2f);


		g2d.setStroke(bs3);

		g2d.drawLine(centerX, startY,centerX, endY);
		g2d.drawLine(startX, centerY,endX, centerY);
		g2d.drawLine(centerX+1, startY,centerX+1, endY);
		g2d.drawLine(startX, centerY+1,endX, centerY+1);



		g.drawLine(startX, endY,endX, endY);
		g.drawLine(startX, startY,endX, startY);

		g.drawLine(startX, startY,startX, endY);
		g.drawLine(endX, startY,endX, endY);

		g.drawString("0", centerX-2, this.getHeight()-margin/2);

		for(int i=0;centerX+i*100<endX;i++)
		{
			g.drawLine(centerX+i*100, startY,centerX+i*100, endY);	
		}

		for(int i=0;centerX-i*100>startX;i++)
		{
			g.drawLine(centerX-i*100, startY,centerX-i*100, endY);	
		}

		for(int i=0;centerY+i*100<startY;i++)
		{
			g.drawLine(startX, centerY+i*100,endX, centerY+i*100);	
		}

		for(int i=0;centerY-i*100>endY;i++)
		{
			g.drawLine(startX, centerY-i*100,endX, centerY-i*100);	
		}
	}
	protected void updateMapInfo()
	{

		int w =this.getWidth();
		int h = this.getHeight();
		centerX = w/2;
		centerY = h/2;


		this.lngCenterX = centerX +convertX(getCenterLng());
		this.latCenterY = centerY +convertY(getCenterLat());

		latPointRate 	= stepSize*STEP;
		lngPointRate 	= stepSize*STEP;

		startX		 	= margin;
		endY 			= margin;

		endX 			= this.getWidth()-margin;
		startY 			= this.getHeight()-margin;
		currentTime = dateFormat.format(new Date(System.currentTimeMillis()));
		this.setLineDraw(mapOptionManager.isLinedraw());
		this.setDarwCoordinate(mapOptionManager.isDrawCoordinate());
		this.setDrawResult(mapOptionManager.isDrawResult());
	}


	public double convertLat(int y) {
		return (double)y/(double)latPointRate;
	}
	public double convertLng(int x) {

		return (double)x/(double)lngPointRate;
	}
	
	public void updateStepCount(int count)
	{
		if(count>=1&&count<=10)
		{
			STEP_COUNT = count;	
		}		
		switch (STEP_COUNT) {
		case 1:
			STEP=STEP_1;
			break;
		case 2:
			STEP=STEP_2;
			break;
		case 3:
			STEP=STEP_3;
			break;
		case 4:
			STEP=STEP_4;
			break;
		case 5:
			STEP=STEP_5;
			break;
		case 6:
			STEP=STEP_6;
			break;
		case 7:
			STEP=STEP_7;
			break;
		case 8:
			STEP=STEP_8;
			break;
		case 9:
			STEP=STEP_9;
			break;
		case 10:
			STEP=STEP_10;
			break;	

		default:
			break;
		}
		System.out.println("update step:"+STEP);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//0.02< STEP < 4
		if(e.getWheelRotation()>0)
		{	
		
			if(STEP_COUNT<10)
			{
				++STEP_COUNT;
			}
		}
		else
		{
			if(STEP_COUNT>0)
			{
				--STEP_COUNT;
			}
		}
		
		updateStepCount(STEP_COUNT);
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(clickPoint==null)
			return;
		
		Point current=e.getPoint();

		int newX= current.x-clickPoint.x;
		int newY = current.y-clickPoint.y;		

		double newCenterlat =currentCenterPoint.getLat()+convertLat(newY*-1);
		double newCenterlng =currentCenterPoint.getLng()+convertLng(newX);
		/*if(newCenterlng>180||newCenterlng<-180)
		{
			currentCenterPoint = new DoublePoint(centerLng, centerLat);
			newCenterlng*=-1;
		}*/

		this.setCenter(-newCenterlat, -newCenterlng);

	}
	@Override
	public void mouseMoved(MouseEvent e) {
		Point current =e.getPoint();
		mouseLat = getLatOnMapByY(current.y);
		mouseLng = getLngOnMapByX(current.x);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()>1)
		{

			Point current=e.getPoint();
			int newX=lngCenterX- current.x;//-clickPoint.x;
			int newY =latCenterY-current.y;//-clickPoint.y;		

			double newCenterlat =currentCenterPoint.getLat()+convertLat(newY);
			double newCenterlng =currentCenterPoint.getLng()+convertLng(newX);
			if(newCenterlng>180||newCenterlng<-180)
			{
				currentCenterPoint = new DoublePoint(getCenterLng(),getCenterLat());
				newCenterlng*=-1;
			}
			this.setCenter(newCenterlat, newCenterlng);
		}

	}
	@Override
	public void mousePressed(MouseEvent e) {

		clickPoint =e.getPoint();

		currentCenterPoint = new DoublePoint(getCenterLng(),getCenterLat());

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
