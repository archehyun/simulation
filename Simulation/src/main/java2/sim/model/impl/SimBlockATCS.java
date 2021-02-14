package sim.model.impl;

import java.awt.Graphics;
import java.util.ArrayList;

import sim.model.SimMessage;
import sim.model.SimObject;

public class SimBlockATCS extends SimObject{

	SimATC atc1, atc2;

	ArrayList<SimATC> atcs = new ArrayList<SimATC>();

	public void setBound(int x, int y, int w, int h)
	{

		atc1 = new SimATC(x,y);

		atc1.setW(10);
		atc1.setH(80);

		atc2 = new SimATC(x+1000,y);
		atc2.setW(10);
		atc2.setH(80);

	}


	@Override
	public void update(SimMessage message) {
		atc1.update(message);
		atc2.update(message);
		checkCollision();
	}

	@Override
	public void draw(Graphics g) {
		atc1.draw(g);
		atc2.draw(g);
	}
	void checkCollision() { //플레이어와 적군의 충돌		

		int left = (int) (atc2.getX() - atc2.getW());

		int right = (int) (atc2.getX() + atc2.getW());

		int top = (int) (atc2.getY()- atc2.getH());

		int bottom = (int) (atc2.getY()+ atc2.getH());


		if(atc1.getX() > left && atc1.getX() < right && atc1.getY() > top && atc1.getY() < bottom) {

			atc1.setDirection(atc1.getDirection()*-1);
			atc2.setDirection(atc2.getDirection()*-1);
		}




	}




}
