package com.musejianglan.baseframework.database.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.musejianglan.baseframework.database.BaseDBHelper;
import com.musejianglan.baseframework.database.entity.IEntity;

public abstract class BaseDao<T extends IEntity> {

	protected Dao<T, Long> dao;
	protected Class<?> entityClass;
	
    public BaseDao(Context context,
				   Class<? extends BaseDBHelper> childDBHelperClass,
				   Class<T> entityClass) {
		this.entityClass = entityClass;
		dao = BaseDBHelper.createDao(context, entityClass, childDBHelperClass);
	}
    
    public Dao<T, Long> getDao() {
        return dao;
    }
}