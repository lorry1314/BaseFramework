package com.musejianglan.baseframework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zft<br>
 * Tip：
 * 		在子类中必须在静态代码块中重新赋值版本号和数据库名，否则为默认值
 */
public class BaseDBHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String TAG = "BaseDBHelper";

	private static Map<String, Dao<?,?>> daos = new HashMap<String, Dao<?,?>>();

	private static BaseDBHelper database;
	private static ConnectionSource connectionSource;
	
	public BaseDBHelper(Context context, String databaseName, int databaseVersion) {
		super(context, databaseName, null, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
	}

	public static synchronized <T extends BaseDBHelper> BaseDBHelper getHelper(Context context, Class<T> childDBHelperClass) {
		context = context.getApplicationContext();
		if (database == null) {
			synchronized (BaseDBHelper.class) {
				if (database == null) {
					try {
						Constructor<T> constructor = childDBHelperClass.getConstructor(Context.class);
						database = constructor.newInstance(context);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return database;
	}

	public static ConnectionSource getConnectionSource(Context context,
			Class<? extends BaseDBHelper> childDBHelperClass) {
		if (connectionSource == null) {
			database = getHelper(context, childDBHelperClass);
			connectionSource = new AndroidConnectionSource(database);
		}
		return connectionSource;
	}

	/**
	 * 创建DAO
	 * 
	 * @param context
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <E extends Dao<T, ?>, T> E createDao(
			Context context, Class<T> clazz,
			Class<? extends BaseDBHelper> childDBHelperClass) {
		Dao<?, ?> dao = null;
		try {
			String className = clazz.getSimpleName();
			if (daos.containsKey(className)) {
				dao = daos.get(className);
			}
			if (dao == null) {
				dao = DaoManager.createDao(getConnectionSource(context, childDBHelperClass), clazz);
				daos.put(className, dao);
			}
			return (E) dao;
		} catch (Exception e) {
			Log.e(TAG, "创建dao时发生异常", e);
			return null;
		}
	}

	/**
	 * 释放资源
	 * 		在应用关闭时刻调用
	 */
	@SuppressWarnings("unused")
	public static void release() {
		try {
			connectionSource.close();
			for (String key : daos.keySet()) {
				Dao<?, ?> dao = daos.get(key);
				dao = null;
			}
			database = null;
		} catch (Exception e) {
			Log.e(TAG, "释放资源时发生异常", e);
		}
	}
	
}
