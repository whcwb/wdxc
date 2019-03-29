package com.ldz.sys.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "sys_rs_role_biz")
public class SysRsRoleBiz implements Serializable {
    /**
     * 角色子系统关系ID
     */
    @Id
    @Column(name = "rs_role_biz_id")
    @GeneratedValue(generator = "JDBC")
    private Long rsRoleBizId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 业务子系统ID
     */
    @Column(name = "biz_id")
    private Long bizId;

    private static final long serialVersionUID = 1L;

    /**
     * 获取角色子系统关系ID
     *
     * @return rs_role_biz_id - 角色子系统关系ID
     */
    public Long getRsRoleBizId() {
        return rsRoleBizId;
    }

    /**
     * 设置角色子系统关系ID
     *
     * @param rsRoleBizId 角色子系统关系ID
     */
    public void setRsRoleBizId(Long rsRoleBizId) {
        this.rsRoleBizId = rsRoleBizId;
    }

    /**
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取业务子系统ID
     *
     * @return biz_id - 业务子系统ID
     */
    public Long getBizId() {
        return bizId;
    }

    /**
     * 设置业务子系统ID
     *
     * @param bizId 业务子系统ID
     */
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public enum InnerColumn {
        rsRoleBizId("rs_role_biz_id"),
        roleId("role_id"),
        bizId("biz_id");

        private final String column;

        public String value() {
            return this.column;
        }

        public String getValue() {
            return this.column;
        }

        InnerColumn(String column) {
            this.column = column;
        }

        public String desc() {
            return this.column + " DESC";
        }

        public String asc() {
            return this.column + " ASC";
        }
    }
}