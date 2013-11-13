package com.deange.quicklink.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.support.ConnectionSource;

public class DatabaseProvider extends ContentProvider implements DatabaseHelper.Callback {

	@Override
	public boolean onCreate() {
		ContentHelper.getInstance(getContext());
		DatabaseHelper.getInstance(getContext());
		DatabaseHelper.setDatabaseCallback(this);

		return true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		// No upgrade use cases at the moment
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // We are using OrmLite instead of the content provider
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
        // We are using OrmLite instead of the content provider
        throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
        // We are using OrmLite instead of the content provider
        throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // We are using OrmLite instead of the content provider
        throw new UnsupportedOperationException();
	}
}