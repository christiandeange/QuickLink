package com.deange.quicklink.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.util.Log;

import com.deange.quicklink.controller.GsonController;
import com.deange.quicklink.model.GoogleUrl;
import com.deange.quicklink.model.HeaderMap;
import com.deange.quicklink.utils.HttpUtils;

public class GoogleApi extends BaseApi<GoogleUrl> {

	private static final String TAG = GoogleApi.class.getSimpleName();

	public GoogleApi(final Context context) {
		super(context);
	}

	@Override
	public String getBaseApiEndpoint() {
		return "https://www.googleapis.com/urlshortener/v1/url/";
	}

	@Override
	public GoogleUrl post(final GoogleUrl entity, final String url, final HeaderMap headers) throws IOException {
		final String fullApiUrl = getBaseApiEndpoint() + url;
		final HttpPost postRequest = new HttpPost(fullApiUrl);

		if (headers != null) {
			postRequest.setHeaders(headers.makeHeaders());
		}

		try {
			final String json = GsonController.getInstance().toJson(entity);
			postRequest.setEntity(new StringEntity(json));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Unsupported model class" + entity.getClass().getCanonicalName());
			return null;
		}

		final HttpResponse response = getClient().execute(postRequest);

		final InputStream is = response.getEntity().getContent();
		final String outputJson = HttpUtils.streamToString(is, true);
		final GoogleUrl responseEntity = GsonController.getInstance().fromJson(outputJson, entity.getClass());

		return responseEntity;
	}

}
