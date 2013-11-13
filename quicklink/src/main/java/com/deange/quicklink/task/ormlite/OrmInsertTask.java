package com.deange.quicklink.task.ormlite;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.deange.quicklink.content.ContentHelper;
import com.deange.quicklink.model.BaseModel;

public class OrmInsertTask<T extends BaseModel> extends OrmBaseTask<T, Integer> {

	protected static final String TAG = OrmInsertTask.class.getSimpleName();

	final Callback mCallback;
	final Context mContext;
	final Class<T> mClazz;
	final ContentHelper mContent;

	public interface Callback {
		public void onInsertCompleted(final int rowsInserted);
	}

	public OrmInsertTask(final Context context, final Callback callback, final Class<T> clazz) {
		mContext = context;
		mCallback = callback;
		mClazz = clazz;
		mContent = ContentHelper.getInstance(mContext);
	}

	@Override
	protected Integer doInBackground(final T... items) {

		try {
			int rowsInserted = 0;
			for (final T item : items) {
				rowsInserted += mContent.getDao(mClazz).create(item);
			}

			return rowsInserted;

		} catch (SQLException e) {
			Log.e(TAG, "Fatal error occurred.");
			return 0;
		}

	}

	@Override
	protected void onPostExecute(final Integer rowsInserted) {
		if (mCallback != null) {
			mCallback.onInsertCompleted(rowsInserted);
		}
	}

}
