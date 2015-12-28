package com.musejianglan.baseframework.database.dao;

import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.musejianglan.baseframework.database.BaseDBHelper;
import com.musejianglan.baseframework.database.entity.IBaseDataEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class BaseDataDao<T extends IBaseDataEntity> extends BaseDao<T> {

	public BaseDataDao(Context context,
                       Class<? extends BaseDBHelper> childDBHelperClass,
                       Class<T> entityClass) {
        super(context, childDBHelperClass, entityClass);
    }
	
    public void updateData(final List<T> list) throws SQLException, Exception {
	    if (list == null) {
	        return;
	    }
        dao.callBatchTasks(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (T t: list) {
                    if ("2".equals(t.getAuditStatus())) {// 2已审核
                        audit(t);
                        dao.createOrUpdate(t);
                    } else if ("3".equals(t.getAuditStatus())) {// 1未审核，3已报废
                        scrap(t);
                        dao.createOrUpdate(t);
                    }// else if ("1".equals(t.getAuditStatus())) // 1未审核数据不进行处理
                }
                return null;
            }
        });
	}
	


    protected void audit(T t) throws Exception {
    }
    
    protected void scrap(T t) throws Exception {
    }
    
    /**
	 * 获取queryBuilder
	 * 
	 * @return
	 */
	protected QueryBuilder<T, Long> queryBuilder(){
		return dao.queryBuilder();
	}
	
	protected List<T> query(PreparedQuery<T> preparedQuery) throws SQLException {
		return dao.query(preparedQuery);
	}
}
