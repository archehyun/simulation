package sim.model.core;


public class SimTimestamp {
	public static String toTimestmp(long time)
	{

		int hour, min, sec = 0;
		min = (int) (time / 60);
		hour = min / 60;
		sec = (int) (time % 60);
		min = min % 60;

		String strHour = (hour < 10 ? "0":"")+ String.valueOf(hour);
		String strMin = (min < 10 ? "0" : "") + String.valueOf(min);
		String strSec = (sec < 10 ? "0" : "") + String.valueOf(sec);
		//		return hour < 10 ? "0" : "" + hour + "-" + (min < 10 ? "0" : "") + min + "-" + (sec < 10 ? "0" : "") + sec;
		return strHour + ":" + strMin + ":" + strSec;

	}


}
