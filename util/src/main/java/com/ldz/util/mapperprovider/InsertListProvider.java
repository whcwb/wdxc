package com.ldz.util.mapperprovider;

import com.ldz.util.commonUtil.JdbcType2JavaType;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

public class InsertListProvider extends MapperTemplate {


    public InsertListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(insertColumns(entityClass, true, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值

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

        /*for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }*/
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }


    /*public static String insertColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
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
    }*/



   /* public String insertUseGeneratedKeys(MappedStatement ms,boolean skipId, boolean notNull, boolean notEmpty) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();

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

       *//* sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(SqlHelper.insertValuesColumns(entityClass, true, false, false));*//*
        return sql.toString();
    }*/
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
