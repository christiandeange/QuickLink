package com.deange.quicklink.model;

import java.text.DateFormat;
import java.util.Date;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "GoogleUrl")
public class GoogleUrl extends BaseModel {

	public static final String LONG_URL = "long_url";
	public static final String SHORT_URL = "short_url";
	public static final String TIMESTAMP = "timestamp";
	public static final String PAGE_TITLE = "page_title";

	public static final String LONG_URL_HTTP = "longUrl";
	public static final String SHORT_URL_HTTP = "id";

	@DatabaseField(columnName = LONG_URL)
	@SerializedName(LONG_URL_HTTP)
	String mLongUrl;

	@DatabaseField(columnName = SHORT_URL)
	@SerializedName(SHORT_URL_HTTP)
	@Expose(serialize = false)
	String mShortenedUrl;

	@DatabaseField(columnName = PAGE_TITLE)
	@SerializedName(PAGE_TITLE)
	@Expose(serialize = false)
	String mPageTitle;

	@Expose(deserialize = false, serialize = false)
	@DatabaseField(columnName = TIMESTAMP)
	Long mTimestamp;

	public void setTitle(final String pageTitle) {
		mPageTitle = pageTitle;
	}

	public String getTitle() {
		return mPageTitle;
	}

	public String getLongUrl() {
		return mLongUrl;
	}

	public String getShortenedUrl() {
		return mShortenedUrl;
	}

	public Long getTimestamp() {
		return mTimestamp;
	}

	public String getFormattedTime(final Context context) {
		final DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
		final String formattedDate = dateFormat.format(new Date(mTimestamp));
		return formattedDate;
	}

	public void setTimeToNow() {
		mTimestamp = System.currentTimeMillis();
	}

	public GoogleUrl(final String longUrl) {
		mLongUrl = longUrl;
		setTimeToNow();
	}

	public GoogleUrl() {
		// Needed by OrmLite
	}

}
