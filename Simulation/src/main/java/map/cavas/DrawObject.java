package map.cavas;

import java.awt.Graphics;

/**
 * 이미지 표시 인터페이스
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
