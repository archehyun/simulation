package sim.map.model;

import java.util.ArrayList;

public class Distance {
	

	/**
	 * �� ��ǥ�� �Ÿ��� ���ϴ� ����
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static  double getDistance(double x1, double y1, double x2, double y2)
	{	
		return  Math.sqrt(Math.pow((x1-x2), 2)+Math.pow((y1-y2),2));
	}
	/**
	 * �� ��ǥ�� ������ ���ϴ� ����
	 * @param ax
	 * @param ay
	 * @param bx
	 * @param by
	 * @return
	 * 
	 * ��ǥ A(ax, ay);
	 * ��ǥ B(ba, by);
	 * ��� C = bx - ax;
	 * ��� D = by - ay;
	 * ��� E = Math.atan2(���D, ���C);
	 * ���� �� = ��� E * (180 / 3.141592); //���� ���� �ٲ��ݴϴ�
	 * 
	 */
	public static  double getAngle(double ax, double ay, double bx, double by)
	{

		double dx = bx-ax;
		double dy = by-ay;


		double temp=Math.atan2(dx,dy)*(180/Math.PI);


		if(temp>0)
		{
			return temp;
		}
		else{
			return Math.abs(temp)+90;	
		}
	}

	public static ArrayList<DoublePoint> makeTagMoveList3(double x1, double y1,double x2, double y2, double baseDistance) {


		ArrayList<DoublePoint> pointMoveList  = new ArrayList<DoublePoint>();
		double angle =Distance.getAngle(x1, y1,x2,y2);
		DoublePoint tagPoint =new DoublePoint(x1,y1);
		pointMoveList.add(tagPoint);
		DoublePoint temp =tagPoint;
		boolean flagX =true;
		boolean flagY =true;
		double distance = Distance.getDistance(x1, y1, x2, y2);
		int count = (int) (distance/baseDistance);
		int num=0;
		System.out.println("creat move list, angle:"+angle+", x move:"+Math.cos(baseDistance*angle)+",y move:"+Math.sin(baseDistance*angle));
		do
		{
			double newX = temp.getX()+Math.cos(Math.toRadians(angle));		

			double newY = temp.getY()+Math.sin(Math.toRadians(angle));

			tagPoint = new DoublePoint(newX, newY);		

			pointMoveList.add(tagPoint);

			temp=tagPoint;

			if(Math.cos(Math.toRadians(angle))>=0)
			{
				if(tagPoint.x>x2)
				{
					flagX=false;
					break;
				}
			}else
			{
				if(tagPoint.x<x2)
				{
					flagX=false;
					break;
				}
			}
			if(Math.sin(Math.toRadians(angle))>=0)
			{
				if(tagPoint.y>y2)
				{
					flagY=false;
					break;
				}
			}else
			{
				if(tagPoint.y<y2)
				{
					flagY=false;
					break;
				}
			}



			num++;

		}while(count>num);
		return pointMoveList;
	}
	
	/**
	 * 
	 * 
	 * P= aA + (1+a)B
	 * {a| 0<= a <= 1}
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param baseDistance
	 * @return
	 */
	public static ArrayList<DoublePoint> makeTagMoveList(double x1, double y1,double x2, double y2, double baseDistance) {


		ArrayList<DoublePoint> pointMoveList  = new ArrayList<DoublePoint>();

		DoublePoint p2 = new DoublePoint(x1,y1); 

		DoublePoint p1 = new DoublePoint(x2,y2);

		double distance = Distance.getDistance(x1, y1, x2, y2);

		int step = (int) (distance/baseDistance);
		System.out.println("base:"+String.format("%.6f", baseDistance)+", distance:"+String.format("%.2f",distance)+", step:"+step);
		

		double delta = 0; 

		for(int i=0; i <= step; ++i) { 

			DoublePoint p1_prime = p1.mult(delta); 

			DoublePoint p2_prime = p2.mult(1-delta); 

			DoublePoint c = p1_prime.add(p2_prime); 

			delta += (double)1/step; 

			pointMoveList.add(new DoublePoint(c.x, c.y));
		}

		return pointMoveList;
	}

}
