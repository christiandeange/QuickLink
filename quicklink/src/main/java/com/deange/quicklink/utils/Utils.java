package com.deange.quicklink.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

	public static void copyToClipboard(final Context context, final String text) {

		final Object clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE);
        ((ClipboardManager) clipboard).setPrimaryClip(ClipData.newPlainText("text label", text));
	}

	public static boolean matchPattern(final String url, final String regex) {
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	public static void setMaxWidth(final Activity activity, final View view, final int maxWidth, final int valueType) {
		final DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		final int maxWidthPixels = (int) TypedValue.applyDimension(valueType, maxWidth, dm);
		final int resolvedWidth = Math.min(maxWidthPixels, dm.widthPixels);
		view.setMinimumWidth(resolvedWidth);
	}

	public static int getVersionCode(final Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (final NameNotFoundException e) {
			return 0;
		}
	}

	public static String getVersionName(final Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (final NameNotFoundException e) {
			return null;
		}
	}

	private Utils() {
        // Uninstantiable
	}

}
