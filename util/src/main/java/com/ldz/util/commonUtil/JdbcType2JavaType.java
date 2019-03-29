package com.ldz.util.commonUtil;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 将jdbcType转换为javaType
 * @author Lee
 *
 */
public class JdbcType2JavaType {

	private static Map<String, String> nameToType;
    
    private static void initMap(){
    	nameToType = new HashMap<String, String>();
    	nameToType.clear();
        nameToType.put("INT", Integer.class.getName()); //$NON-NLS-1$
        nameToType.put("INT UNSIGNED", Integer.class.getName()); //$NON-NLS-1$
        nameToType.put("BIGINT", Long.class.getName()); //$NON-NLS-1$
        nameToType.put("BINARY", "byte[]"); //$NON-NLS-1$
        nameToType.put("BIT", Boolean.class.getName()); //$NON-NLS-1$
        nameToType.put("BLOB", Blob.class.getName()); //$NON-NLS-1$
        nameToType.put("BOOLEAN", Boolean.class.getName()); //$NON-NLS-1$
        nameToType.put("CHAR", String.class.getName()); //$NON-NLS-1$
        nameToType.put("CLOB", Clob.class.getName()); //$NON-NLS-1$
        nameToType.put("TIMESTAMP", java.util.Date.class.getName()); //$NON-NLS-1$
        nameToType.put("DATE", Date.class.getName()); //$NON-NLS-1$
        nameToType.put("DECIMAL", Short.class.getName()); //$NON-NLS-1$
        nameToType.put("DOUBLE", Double.class.getName()); //$NON-NLS-1$
        nameToType.put("FLOAT", Float.class.getName()); //$NON-NLS-1$
        nameToType.put("INTEGER", Integer.class.getName()); //$NON-NLS-1$
        nameToType.put("LONGVARBINARY", "byte[]"); //$NON-NLS-1$
        nameToType.put("LONGVARCHAR", String.class.getName()); //$NON-NLS-1$
        nameToType.put("NCHAR", String.class.getName()); //$NON-NLS-1$
        nameToType.put("NVARCHAR", String.class.getName()); //$NON-NLS-1$
        nameToType.put("NUMERIC", BigDecimal.class.getName()); //$NON-NLS-1$
        nameToType.put("TIME", Time.class.getName()); //$NON-NLS-1$
        nameToType.put("TINYINT", "byte"); //$NON-NLS-1$
        nameToType.put("VARBINARY", "byte[]"); //$NON-NLS-1$
        nameToType.put("VARCHAR", String.class.getName()); //$NON-NLS-1$
        nameToType.put("VARCHAR2", String.class.getName()); //$NON-NLS-1$
    }
    
    public static String getJavaType(String jdbcTypeName) {
    	initMap();
        return nameToType.get(jdbcTypeName);
    }
    
    public static String getJdbcType(String javaTypeName) {
    	initMap();
    	if (!nameToType.containsValue(javaTypeName)){
    		return null;
    	}
    	//特殊处理String类型
    	if (javaTypeName.equals(String.class.getName())){
    		return "VARCHAR";
    	}
    	Iterator<Entry<String, String>> ite = nameToType.entrySet().iterator();
    	while(ite.hasNext()){
    		Entry<String, String> item = ite.next();
    		if (item.getValue().equals(javaTypeName)){
    			return item.getKey();
    		}
    	}
    	
        return null;
    }
}
