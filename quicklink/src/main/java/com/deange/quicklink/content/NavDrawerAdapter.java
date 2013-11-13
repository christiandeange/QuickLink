package com.deange.quicklink.content;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deange.quicklink.R;
import com.deange.quicklink.model.GoogleUrl;

public class NavDrawerAdapter extends ArrayAdapter<GoogleUrl> {

	public NavDrawerAdapter(final Context context, List<GoogleUrl> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		View rootView = convertView;
		if (rootView == null) {
			rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_nav_drawer_item_2, null);
		}

		final GoogleUrl googleUrl = getItem(position);

		final String pageTitle = googleUrl.getTitle();
		final String shortUrl = googleUrl.getShortenedUrl();
		final String timestamp = googleUrl.getFormattedTime(getContext());

		if (!TextUtils.isEmpty(pageTitle)) {
			((TextView) rootView.findViewById(R.id.item_nav_drawer_page_title)).setText(pageTitle);

		} else {
			final String longUrl = googleUrl.getLongUrl();
			((TextView) rootView.findViewById(R.id.item_nav_drawer_page_title)).setText(longUrl);
		}

		((TextView) rootView.findViewById(R.id.item_nav_drawer_short_url)).setText(shortUrl);
		((TextView) rootView.findViewById(R.id.item_nav_drawer_timestamp)).setText(timestamp);

		return rootView;
	}

}
