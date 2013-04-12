package com.zehfernando.utils;

import android.os.Handler;

public class DoLater {

	/*
	Delays an action for a number of miliseconds. Action is executed on the same thread when invoked.
	Usage:

		queuedRequest = new DoLater(TIME_IN_MS, new DoLaterListener() {
			@Override
			public void doThis() {
				// Whatever
				createFoodRequest();
			}
		});

	May also be stopped with:

		queuedRequest.stop();

	*/

	// TODO: add pause(), resume(), isPaused(), isStarted()

	// Instances
	private final DoLaterListener listener;
	private final long time;

	private Handler handler;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public DoLater(int __timeMS, DoLaterListener __listener) {
		time = __timeMS;
		listener = __listener;
		start();
	}

	public DoLater(long __timeMS, DoLaterListener __listener) {
		time = __timeMS;
		listener = __listener;
		start();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private void start() {
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				execute();
			}
		}, time);
	}

	private void execute() {
		listener.doThis();
		clear();
	}

	private void clear() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void stop() {
		clear();
	}

	// ================================================================================================================
	// LISTENER INTERFACE ---------------------------------------------------------------------------------------------

	public interface DoLaterListener {
		public void doThis();
	}
}

