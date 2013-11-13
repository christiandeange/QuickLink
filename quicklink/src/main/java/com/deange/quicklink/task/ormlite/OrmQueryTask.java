package com.deange.quicklink.task.ormlite;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.deange.quicklink.content.ContentHelper;
import com.deange.quicklink.model.BaseModel;
import com.j256.ormlite.stmt.QueryBuilder;

public class OrmQueryTask<Result extends BaseModel> extends OrmBaseTask<QueryBuilder<Result, Long>, List<Result>> {

	private static final String TAG = OrmQueryTask.class.getSimpleName();

	final Callback<Result> mCallback;
	final Context mContext;
	final Class<Result> mClazz;
	final ContentHelper mContent;

	public interface Callback<Result> {
		public void onQueryCompleted(final List<Result> itemCollection);
	}

	public OrmQueryTask(final Context context, final Callback<Result> callback, final Class<Result> clazz) {
		mContext = context;
		mClazz = clazz;
		mCallback = callback;
		mContent = ContentHelper.getInstance(mContext);
	}

	@Override
	protected List<Result> doInBackground(final QueryBuilder<Result, Long>... queryBuilder) {

		try {
			final QueryBuilder<Result, Long> query = queryBuilder[0];
			final List<Result> listItems = mContent.getDao(mClazz).query(query.prepare());
			return listItems;

		} catch (SQLException e) {
			Log.e(TAG, "Fatal error occurred.");
			return null;
		}

	}

	@Override
	protected void onPostExecute(List<Result> itemCollection) {
		if (mCallback != null) {
			mCallback.onQueryCompleted(itemCollection);
		}
	}

}
