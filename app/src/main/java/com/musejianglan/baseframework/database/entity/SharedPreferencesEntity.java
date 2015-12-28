package com.musejianglan.baseframework.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "SharedPreferences")
public class SharedPreferencesEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";// 主键ID
    public static final String NAME = "name";// 主键ID
    public static final String MODE = "mode";// 主键ID
    public static final String CONTENT = "content";// 主键ID
    
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String name;

    @DatabaseField
    private Integer mode;

    @DatabaseField
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
