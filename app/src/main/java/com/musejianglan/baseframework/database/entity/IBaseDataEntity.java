package com.musejianglan.baseframework.database.entity;

import java.util.Date;

public interface IBaseDataEntity extends IEntity {
    
    public static final String AUDIT_STATUS = "auditStatus";
    
    public String getAuditStatus();
    
    public Date getModiDate();
    
}
