package com.deange.quicklink.task;

import android.content.Context;
import android.text.TextUtils;

import com.deange.quicklink.model.GoogleUrl;
import com.deange.quicklink.task.ormlite.OrmInsertTask;
import com.deange.quicklink.utils.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageSourceTask extends BaseTask<GoogleUrl, Void, GoogleUrl> {

	final Context mContext;
	final Callback mCallback;

	public interface Callback {
		public void onUrlSavedToDatabase(final GoogleUrl googleUrl);
	}

	public PageSourceTask(final Context context) {
		mContext = context;
		mCallback = (Callback) context;
	}

	@Override
	protected GoogleUrl doInBackground(GoogleUrl... params) {

		final GoogleUrl googleUrl = params[0];
		if (googleUrl == null) {
			return null;
		}

		final String originalUrl = googleUrl.getLongUrl();

		try {
			final URL url = new URL(originalUrl);
			final InputStream inputStream = url.openConnection().getInputStream();

			String pageSource = HttpUtils.streamToString(inputStream, false);
			if (TextUtils.isEmpty(pageSource)) {
				// Page likely requires https
				final URL httpsUrl = new URL(originalUrl.replace("http://", "https://"));
				final InputStream httpsInputStream = httpsUrl.openConnection().getInputStream();

				pageSource = HttpUtils.streamToString(httpsInputStream, false);
			}

			pageSource = pageSource.replaceAll("\\s+", " ");
			final Pattern titleTagPattern = Pattern.compile("<title>(.*)</title>", Pattern.CASE_INSENSITIVE);
			final Matcher titleTagMatcher = titleTagPattern.matcher(pageSource);
			if (titleTagMatcher.find()) {
				googleUrl.setTitle(titleTagMatcher.group(1));
			}

		} catch (IOException e) {
		}

		return googleUrl;
	}

	@Override
	protected void onPostExecute(GoogleUrl googleUrl) {
		if ((googleUrl != null) && (googleUrl.getShortenedUrl() != null)) {
			final OrmInsertTask<GoogleUrl> insertTask = new OrmInsertTask<GoogleUrl>(mContext, null, GoogleUrl.class);
			insertTask.execute(googleUrl);

            if (mCallback != null) {
			    mCallback.onUrlSavedToDatabase(googleUrl);
            }
		}
	}

}
