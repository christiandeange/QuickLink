package com.deange.quicklink.model;

import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

public class HeaderMap extends HashMap<String, String> {
	private static final long serialVersionUID = -1785377065564851819L;

	public HeaderMap(final String... headers) {

		if (headers != null) {
			if (headers.length % 2 != 0) {
				throw new UnsupportedOperationException("Params length is not a multiple of two");
			}

			for (int i = 0; i < headers.length; i += 2) {
				put(headers[i], headers[i + 1]);
			}
		}

	}

	public HeaderEntry[] makeHeaders() {
		final HeaderEntry[] headers = new HeaderEntry[size()];
		int index = 0;

		for (Entry<String, String> header : entrySet()) {
			headers[index] = new HeaderEntry(header);
			index++;
		}
		return headers;
	}

	private class HeaderEntry implements Header {

		private final String mHeaderKey;
		private final String mHeaderValue;

		public HeaderEntry(final Entry<String, String> entry) {
			mHeaderKey = entry.getKey();
			mHeaderValue = entry.getValue();
		}

		@Override
		public HeaderElement[] getElements() throws ParseException {
			return new HeaderElement[] {};
		}

		@Override
		public String getName() {
			return mHeaderKey;
		}

		@Override
		public String getValue() {
			return mHeaderValue;
		}

	}

}
