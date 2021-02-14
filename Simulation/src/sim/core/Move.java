package sim.core;


public class Move {

	public float x;

	public float y;

	public Move add(Move m0, Move m1, Move m2) {
		m0.x = m1.x + m2.x;
		m0.y = m1.y + m2.y;
		return m0;

	}

	public Move sub(Move m0, Move m1, Move m2) {
		m0.x = m1.x - m2.x;
		m0.y = m1.y - m2.y;
		return m0;

	}

	public Move mul(Move m0, Move m1, float s) {
		m0.x = m1.x * s;
		m0.y = m1.y * s;
		return m0;
	}

	public Move div(Move m0, Move m1, float s) {
		m0.x = m1.x / s;
		m0.y = m1.y / s;
		return m0;
	}


}
