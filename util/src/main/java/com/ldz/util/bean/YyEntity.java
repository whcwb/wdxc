package com.ldz.util.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 鹰眼设备 entity
 */
@Getter
@Setter
@ToString
public class YyEntity {

    /**
     * 用户的ak   （必填）
     */
    private String ak;

    /**
     * service的ID，service 的唯一标识  （必填）
     * 在轨迹管理台创建鹰眼服务时，系统返回的 service_id
     */
    private int service_id;

    /**
     * entity名称，作为其唯一标识 String(128)  （必填）
     *
     * 同一service服务中entity_name不可重复。一旦创建，entity_name 不可更新。
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字。 entity_name 和 entity_desc 支持联合模糊检索。
     */
    private String entity_name;

    /**
     * entity 的可读性描述 String(128)
     * 命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字。entity_name 和 entity_desc 支持联合模糊检索。
     */
    private String entity_desc;
    /**
     *
     */
    private String modify_time;

    private String create_time;

    private LatestLocation latest_location;



    
}
