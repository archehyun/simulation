package simulaltion;

import simulaltion.queue.QueueNode;

public class SimulationMessage extends QueueNode{
	
	private String destination;
	private String arrival; // �����ġ
	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	private String departure; // ������ġ

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String toString()
	{
		return "["+departure+"-"+arrival+"]";
	}

}
