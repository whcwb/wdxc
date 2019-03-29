package com.ldz.sys.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "sys_rs_role_resource")
public class SysRsRoleResource implements Serializable {
    /**
     * 角色资源关系ID
     */
    @Id
    @Column(name = "rs_role_module_id")
    @GeneratedValue(generator = "JDBC")
    private Long rsRoleModuleId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 资源ID
     */
    @Column(name = "res_id")
    private Long resId;

    private static final long serialVersionUID = 1L;

    /**
     * 获取角色资源关系ID
     *
     * @return rs_role_module_id - 角色资源关系ID
     */
    public Long getRsRoleModuleId() {
        return rsRoleModuleId;
    }

    /**
     * 设置角色资源关系ID
     *
     * @param rsRoleModuleId 角色资源关系ID
     */
    public void setRsRoleModuleId(Long rsRoleModuleId) {
        this.rsRoleModuleId = rsRoleModuleId;
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
     * 获取资源ID
     *
     * @return res_id - 资源ID
     */
    public Long getResId() {
        return resId;
    }

    /**
     * 设置资源ID
     *
     * @param resId 资源ID
     */
    public void setResId(Long resId) {
        this.resId = resId;
    }

    public enum InnerColumn {
        rsRoleModuleId("rs_role_module_id"),
        roleId("role_id"),
        resId("res_id");

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