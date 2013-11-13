package com.deange.quicklink.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.deange.quicklink.R;
import com.deange.quicklink.model.GoogleUrl;
import com.deange.quicklink.task.ShortenTask;
import com.deange.quicklink.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShareUrlActivity extends Activity implements ShortenTask.Callback {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getExtras() != null) {
			final String text = getIntent().getExtras().getString(Intent.EXTRA_TEXT);

            final Pattern urlPattern = Patterns.WEB_URL;
            final Matcher matcher = urlPattern.matcher(text);
            while (matcher.find()) {
                final String url = matcher.group();
                new ShortenTask(this, this, url).execute();
            }
		}
	}

	@Override
	public void onShortenUrlDone(final GoogleUrl shortenedUrl) {

		final Toast toast = new Toast(this);
		final View messageView = getLayoutInflater().inflate(R.layout.view_message_bar_layout, null);
		final TextView messageTextView = (TextView) messageView.findViewById(R.id.messagebar_text);
		final int marginPix = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());

		messageTextView.setPadding(0, marginPix, 0, marginPix);

		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(messageView);
		toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
		toast.setMargin(5, 5);

		if ((shortenedUrl == null) || (shortenedUrl.getShortenedUrl() == null)) {
			messageTextView.setText(R.string.failed_create_shortened_url);

		} else {
			final String shortLink = shortenedUrl.getShortenedUrl();
			Utils.copyToClipboard(this, shortLink);
			messageTextView.setText(shortLink);
		}

		toast.show();
		finish();
	}

	@Override
	public void onUrlSavedToDatabase(final  GoogleUrl googleUrl) {
	}

}
