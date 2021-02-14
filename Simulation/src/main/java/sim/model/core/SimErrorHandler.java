package sim.model.core;

import java.lang.Thread.UncaughtExceptionHandler;

public class SimErrorHandler implements UncaughtExceptionHandler {

	private String handlerName;

	public SimErrorHandler(String handlerName) {
		this.handlerName = handlerName;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		System.out.println(handlerName + " caught Exception in Thread - " + thread.getName() + " => " + e);
	}

}
