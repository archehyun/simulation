package sim.map.cavas;

import java.awt.Graphics;

/**
 *
 * @author LDCC
 *
 */
public interface DrawObject {

	/**
	 * @param g
	 */
	public void draw(Graphics g);
	
	


	public void setCountView(boolean selected);

}
