package com.deange.quicklink.content;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deange.quicklink.QLBuildConfig;
import com.deange.quicklink.model.BaseModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String TAG = DatabaseHelper.class.getSimpleName();

	private static DatabaseHelper sInstance = null;
	private static WeakReference<DatabaseHelper.Callback> mCallbackReference = new WeakReference<DatabaseHelper.Callback>(Fallback.INSTANCE);

	private final Class<? extends BaseModel>[] mBaseModels;
	private final Map<Class<? extends BaseModel>, Dao<? extends BaseModel, Long>> mDaoMap = new HashMap<Class<? extends BaseModel>, Dao<? extends BaseModel, Long>>();

	public interface Callback {
		public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion);
	}

	public static void setDatabaseCallback(final DatabaseHelper.Callback databaseCallback) {
		mCallbackReference = new WeakReference<DatabaseHelper.Callback>(databaseCallback);
	}

	public static synchronized DatabaseHelper getInstance(final Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context, QLBuildConfig.DATABASE_NAME, QLBuildConfig.DATABASE_VERSION);
		}
		return sInstance;
	}

	@SuppressWarnings("unchecked")
	private DatabaseHelper(final Context context, final String name, final int version) {
		super(context, name, null, version);
		mBaseModels = ContentType.MODELS;
	}

	@Override
	public void onCreate(final SQLiteDatabase db, final ConnectionSource connection) {
		createTables(connection);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		Log.v(TAG, "onUpgrade()");

		final DatabaseHelper.Callback databaseCallback = mCallbackReference.get();
		if (databaseCallback != null) {
			databaseCallback.onUpgrade(database, connectionSource, oldVersion, newVersion);
		}
	}

	@Override
	public void close() {
		mDaoMap.clear();

		sInstance = null;
		deleteTables(connectionSource);
		createTables(connectionSource);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseModel> Dao<T, Long> getDaoEx(final Class<T> clazz) {
		Dao<T, Long> result;
		if (mDaoMap.containsKey(clazz)) {
			result = (Dao<T, Long>) mDaoMap.get(clazz);

		} else {
			try {
				result = getDao(clazz);
				mDaoMap.put(clazz, result);

			} catch (final java.sql.SQLException e) {
				throw new SQLException(e.getMessage());
			}
		}
		return result;
	}

	public void createTables(final ConnectionSource cs) {
		for (final Class<? extends BaseModel> clazz : mBaseModels) {
			createTable(clazz, cs);
		}
	}

    public void deleteTables(final ConnectionSource cs) {
        for (final Class<? extends BaseModel> clazz : mBaseModels) {
            dropTable(clazz, cs);
        }
    }

	public void createTable(final Class<? extends BaseModel> clazz, final ConnectionSource cs) {
		try {
			TableUtils.createTable(cs, clazz);
		} catch (final java.sql.SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

    public void dropTable(final Class<? extends BaseModel> clazz, final ConnectionSource cs) {
        try {
            TableUtils.dropTable(cs, clazz, false);
        } catch (final java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

	private final static class Fallback implements DatabaseHelper.Callback {
		public static final DatabaseHelper.Callback INSTANCE = new Fallback();

		@Override
		public void onUpgrade(SQLiteDatabase db, ConnectionSource connection, int oldVersion, int newVersion) {
			Log.w(TAG, "Fallback: onUpgrade()");
		}
	}
}
