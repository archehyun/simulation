package sim.model;



public class SimulationTime {

	public static int SECOND_TO_FRAME(int sec) {
		return sec * 60;
	}

	public static int FRAME_TO_SECOND(int frame) {
		return frame / SECOND_TO_FRAME(1);
	}

	public static float second(float sec) {
		return sec * 60;
	}

	public static float minute(float min) {
		return second(min * 60);
	}

	public static float m(float m) {
		return m;
	}

	public static float cm(float cm) {
		return m(0.01f * cm);
	}

	public static float mm(float mm) {
		return cm(0.01f * mm);
	}

}
