package com.deange.quicklink.api;

import android.content.Context;

import com.deange.quicklink.model.BaseModel;
import com.deange.quicklink.model.HeaderMap;

import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public abstract class BaseApi<T extends BaseModel> {

	public final Context mContext;
	private static final DefaultHttpClient sClient = new DefaultHttpClient();

	public BaseApi(final Context context) {
		mContext = context;
	}

	public abstract String getBaseApiEndpoint();

	public DefaultHttpClient getClient() {
		return sClient;
	}

	public T get(final String url, final HeaderMap headers) {
		throw new UnsupportedOperationException();
	}

	public T post(final T entity, final String url, final HeaderMap headers) throws IOException {
		throw new UnsupportedOperationException();
	}

	public T put(final T entity, final String url, final HeaderMap headers) throws IOException {
		throw new UnsupportedOperationException();
	}

	public T delete(final T entity, final String url, final HeaderMap headers) throws IOException {
		throw new UnsupportedOperationException();
	}

}
