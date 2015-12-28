package com.musejianglan.baseframework.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.musejianglan.baseframework.database.entity.SharedPreferencesEntity;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CRMSharedPreferencesDBHelper extends OrmLiteSqliteOpenHelper {

	private Dao<SharedPreferencesEntity, Integer> mSpDao;
    private HashMap<String, SharedPreferences> mSpMap = new HashMap<String, SharedPreferences>();

	public CRMSharedPreferencesDBHelper(Context context, String databaseName, int databaseVersion) {
		super(context, databaseName, null, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, SharedPreferencesEntity.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, 
	        int oldVersion, int newVersion) {
	}
	
	public void release() {
        OpenHelperManager.releaseHelper();
	}
	
	public SharedPreferences getSharedPreferences(Context context, String name, int mode) {
	    try {
            mSpDao = DaoManager.createDao(new AndroidConnectionSource(this), SharedPreferencesEntity.class);
            List<SharedPreferencesEntity> spList = mSpDao.queryForEq(SharedPreferencesEntity.NAME, name);
            SharedPreferencesEntity entity = null;
            if (spList == null || spList.size() == 0) {
                entity = new SharedPreferencesEntity();
                entity.setName(name);
                entity.setMode(mode);
                entity.setContent("");
                mSpDao.create(entity);
            } else {
                entity = spList.get(0);
            }
            SharedPreferences sp = mSpMap.get(entity.getName());
            if (sp == null) {
                sp = new DBSharedPreferences(entity);
                mSpMap.put(name, sp);
            }
            return sp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public class DBSharedPreferences implements SharedPreferences {
	    
	    private Gson mGson;
	    private Map<String, Object> mContentMap;
	    private SharedPreferencesEntity mEntity;
	    private EditorDB mEditorDB = new EditorDB();
	    
	    public DBSharedPreferences(SharedPreferencesEntity entity) throws Exception {
	        mGson = new GsonBuilder().registerTypeAdapter(Map.class, new JsonDeserializer<Map<String, Object>>() {
                @Override
                public Map<String, Object> deserialize(JsonElement json, Type typeOfT, 
                        JsonDeserializationContext context) throws JsonParseException {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    for (Entry<String, JsonElement> entry: json.getAsJsonObject().entrySet()) {
                        map.put(entry.getKey(), entry.getValue().getAsString());
                    }
                    return map;
                }
	        }).create();
	        mEntity = entity;
            Log.d("test", "read: " + entity.getContent());
	        mContentMap = mGson.fromJson(mEntity.getContent(), Map.class);
	        if (mContentMap == null) {
	            mContentMap = new HashMap<String, Object>();
	        }
	    }
	    
        @Override
        public Map<String, ?> getAll() {
            return mContentMap;
        }

        @Override
        public String getString(String key, String defValue) {
            Object obj = mContentMap.get(key);
            if (obj != null) {
                return String.valueOf(obj.toString());
            } else {
                return defValue;
            }
        }

        /**
         * 未实现
         * @see SharedPreferences#getStringSet(String, Set)
         */
        public Set<String> getStringSet(String key, Set<String> defValues) {
            return null;
        }

        @Override
        public int getInt(String key, int defValue) {
            Object obj = mContentMap.get(key);
            if (obj != null) {
                return Integer.valueOf(obj.toString());
            } else {
                return defValue;
            }
        }

        @Override
        public long getLong(String key, long defValue) {
            Object obj = mContentMap.get(key);
            if (obj != null) {
                return Long.valueOf(obj.toString());
            } else {
                return defValue;
            }
        }

        @Override
        public float getFloat(String key, float defValue) {
            Object obj = mContentMap.get(key);
            if (obj != null) {
                return Float.valueOf(obj.toString());
            } else {
                return defValue;
            }
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            Object obj = mContentMap.get(key);
            if (obj != null) {
                return Boolean.valueOf(obj.toString());
            } else {
                return defValue;
            }
        }

        @Override
        public boolean contains(String key) {
            return mContentMap.containsKey(key);
        }

        @Override
        public Editor edit() {
            return mEditorDB;
        }

        /**
         * 未实现
         * @see SharedPreferences#registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener)
         */
        @Override
        public void registerOnSharedPreferenceChangeListener(
                OnSharedPreferenceChangeListener listener) {
        }

        /**
         * 未实现
         * @see SharedPreferences#unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener)
         */
        @Override
        public void unregisterOnSharedPreferenceChangeListener(
                OnSharedPreferenceChangeListener listener) {
        }
        
        public class EditorDB implements Editor {

            @Override
            public Editor putString(String key, String value) {
                mContentMap.put(key, value);
                return this;
            }

            /**
             * 未实现
             * @see Editor#putStringSet(String, Set)
             */
            public Editor putStringSet(String key, Set<String> values) {
                return null;
            }

            @Override
            public Editor putInt(String key, int value) {
                mContentMap.put(key, Integer.valueOf(value));
                return this;
            }

            @Override
            public Editor putLong(String key, long value) {
                mContentMap.put(key, Long.valueOf(value));
                return this;
            }

            @Override
            public Editor putFloat(String key, float value) {
                mContentMap.put(key, Float.valueOf(value));
                return this;
            }

            @Override
            public Editor putBoolean(String key, boolean value) {
                mContentMap.put(key, Boolean.valueOf(value));
                return this;
            }

            @Override
            public Editor remove(String key) {
                mContentMap.remove(key);
                return this;
            }

            @Override
            public Editor clear() {
                mContentMap.clear();
                return this;
            }

            @Override
            public boolean commit() {
                try {
                    apply();
                    mSpDao.update(mEntity);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public void apply() {
                String content = mGson.toJson(mContentMap);
                Log.d("test", "write: " + content);
                mEntity.setContent(content);
            }
        }
	}
}
