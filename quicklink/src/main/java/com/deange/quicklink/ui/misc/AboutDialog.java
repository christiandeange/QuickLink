package com.deange.quicklink.ui.misc;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.deange.quicklink.R;
import com.deange.quicklink.utils.Utils;

public class AboutDialog {

	public static AlertDialog create(final Context context) {
		final AlertDialog dialog = new AlertDialog.Builder(context).setTitle(R.string.app_name)
				.setView(LayoutInflater.from(context).inflate(R.layout.dialog_about, null))
				.setPositiveButton(android.R.string.ok, null).create();
		dialog.setCanceledOnTouchOutside(true);

		return dialog;
	}

	public static void onDialogCreateView(final AlertDialog dialog) {

		final Context context = dialog.getContext();

		final String versionName = context.getString(R.string.app_version, Utils.getVersionName(context));
		final TextView titleText = (TextView) dialog.findViewById(R.id.dialog_about_version_name);
		titleText.setText(versionName);

	}

}
