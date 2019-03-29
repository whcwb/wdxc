package com.ldz.util.mapperprovider;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;

import com.ldz.util.commonUtil.JdbcType2JavaType;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 支持oralce批量新增功能
 * @author 李彬彬
 */
public class OracleInsertListProvider extends MapperTemplate {

	public OracleInsertListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

	public String insertList(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);

        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(insertColumns(entityClass, true, false, false));
        sql.append(" select A.* from ( ");
        sql.append("<foreach collection=\"list\" item=\"record\" index=\"index\" separator=\"union all\" >");
        sql.append("select ");
        //获取实体对象字段集合
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //拼接字段内容
        for (EntityColumn column : columnList) {
            if ( column.isInsertable()) {
            	String jdbcType = JdbcType2JavaType.getJdbcType(column.getJavaType().getName());
            	if (StringUtils.isNotEmpty(jdbcType)){
            		sql.append(column.getColumnHolder("record", ", jdbcType="+jdbcType, ","));
            	}else{
            		sql.append(column.getColumnHolder("record", null, ","));
            	}

            }
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" from dual");
        sql.append("</foreach>");
        sql.append(" ) A ");

        return sql.toString();
    }

	public static String insertColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");

        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);

        for (EntityColumn column : columnSet) {
            if (!column.isInsertable()) {
                continue;
            }
            if (notNull) {
                sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", notEmpty));
            } else {
                sql.append(column.getColumn() + ",");
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }
}
