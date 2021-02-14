package sim.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sim.view.comp.SimFrame;

public class SimulationMain {
	public static void main(String[] args) {
		try {

			//Thread.setDefaultUncaughtExceptionHandler(new SimErrorHandler("DefaultHandler"));
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		new SimFrame();

	} 


}
