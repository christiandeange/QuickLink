package com.deange.quicklink.task;

import java.io.IOException;

import android.content.Context;

import com.deange.quicklink.QLBuildConfig;
import com.deange.quicklink.api.GoogleApi;
import com.deange.quicklink.model.GoogleUrl;
import com.deange.quicklink.model.HeaderMap;

public class ShortenTask extends BaseTask<Void, Void, GoogleUrl> {

	public interface Callback extends PageSourceTask.Callback {
		public void onShortenUrlDone(final GoogleUrl shortUrl);
	}

	private static final String CONTENT_TYPE_KEY = "Content-Type";
	private static final String CONTENT_TYPE_VALUE = "application/json";

	final Context mContext;
	final Callback mCallback;
	final String mOriginalUrl;

	public ShortenTask(final Context context, final Callback callback, final String originalUrl) {
		mContext = context;
		mOriginalUrl = originalUrl;
		mCallback = callback;
	}

	@Override
	protected GoogleUrl doInBackground(final Void... params) {

		final GoogleUrl urlModel = new GoogleUrl(mOriginalUrl);
		final HeaderMap headersMap = new HeaderMap(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);

		try {
			final GoogleApi api = new GoogleApi(mContext);
			final GoogleUrl returnedUrl = api.post(urlModel, "?key=" + QLBuildConfig.GOOGLE_API_KEY, headersMap);
			returnedUrl.setTimeToNow();

			return returnedUrl;

		} catch (final IOException e) {
			return null;
		}

	}

	@Override
	protected void onPostExecute(final GoogleUrl googleUrl) {

		if (googleUrl != null) {
			final PageSourceTask pageSourceTask = new PageSourceTask(mContext);
			pageSourceTask.execute(googleUrl);
		}

		mCallback.onShortenUrlDone(googleUrl);
	}
}
