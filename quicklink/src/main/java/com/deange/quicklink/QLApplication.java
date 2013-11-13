package com.deange.quicklink;

import android.app.Application;
import android.util.Log;

import com.deange.quicklink.controller.GsonController;

public class QLApplication extends Application {

	private static final String TAG = QLApplication.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();

		try {

			// Initialize the GSON cache
			GsonController.createInstance();

		} catch (final Exception ex) {
			Log.e(TAG, "Could not instantiate app: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
