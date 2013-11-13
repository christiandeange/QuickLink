package com.deange.quicklink.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class HttpUtils {

	public static boolean isOnline(final Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null && netInfo.isConnectedOrConnecting());
	}

	public static String streamToString(final InputStream is, final boolean addNewlines) throws IOException {

		final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		final StringBuilder sb = new StringBuilder();
		String line;

        while ((line = reader.readLine()) != null) {
			sb.append(line);
			if (addNewlines) {
				sb.append("\n");
			}
		}
		is.close();
		return sb.toString();
	}

	public static boolean isValidHttpStatus(final int status) {
		return ((status >= 200) && (status < 300));
	}

	private HttpUtils() {
        // Uninstantiable
	}

}
