package test;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShapeTest extends JPanel {

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int radius = 40;
		int centerX = 50;
		int centerY = 100;
		int angle = 30;

		Math.cos(angle * Math.PI / 180);
		Math.sin(angle * Math.PI / 180);

		//g.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, angle, 360 - 2 * angle);

		Polygon p = new Polygon();
		centerX = 150;
		for (int i = 0; i < 5; i++)
			p.addPoint((int) (centerX + radius * Math.cos(i * 2 * Math.PI / 5)), (int) (centerY + radius * Math.sin(i * 2 * Math.PI / 5)));

		//g.fillPolygon(p);

		p = new Polygon();
		centerX = 250;
		for (int i = 0; i < 360; i++) {
			double t = i / 360.0;
			p.addPoint((int) (centerX + radius * t * Math.cos(8 * t * Math.PI)), (int) (centerY + radius * t * Math.sin(8 * t * Math.PI)));
		}
		//g.fillPolygon(p);

		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = AffineTransform.getTranslateInstance(150, 75);
		g2.transform(at);

		g2.fill(createDiamond(25));
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("FillPoly");
		frame.setSize(300, 200);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Container contentPane = frame.getContentPane();
		contentPane.add(new ShapeTest());

		frame.show();
	}

	public static Shape createDiamond(final float s) {
		final GeneralPath p0 = new GeneralPath();
		p0.moveTo(0.0f, 0.0f);

		p0.lineTo(0.0f, 18f);
		p0.lineTo(+7.2f, 18f);
		p0.lineTo(+7.2f, 7.2f);
		p0.lineTo(+18f, 7.2f);
		p0.lineTo(+18f, 18f);

		p0.lineTo(s, 18f);
		p0.lineTo(s, 0.0f);
		p0.closePath();
		return p0;
	}

}
