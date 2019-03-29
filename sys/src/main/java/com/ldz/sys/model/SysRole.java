package com.ldz.sys.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_role")
public class SysRole implements Serializable {
    /**
     * 角色ID
     */
    @Id
    @Column(name = "role_id")
    @GeneratedValue(generator = "JDBC")
    private Long roleId;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "role_describe")
    private String roleDescribe;

    /**
     * 公司ID
     */
    @Column(name = "comp_id")
    private Long compId;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "update_user")
    private Long updateUser;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除标记，0表示未删除；1表示删除
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    @Transient
    private boolean _checked;

    public boolean is_checked() {
        return _checked;
    }

    public void set_checked(boolean _checked) {
        this._checked = _checked;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

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
     * 获取角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取角色描述
     *
     * @return role_describe - 角色描述
     */
    public String getRoleDescribe() {
        return roleDescribe;
    }

    /**
     * 设置角色描述
     *
     * @param roleDescribe 角色描述
     */
    public void setRoleDescribe(String roleDescribe) {
        this.roleDescribe = roleDescribe;
    }

    /**
     * 获取公司ID
     *
     * @return comp_id - 公司ID
     */
    public Long getCompId() {
        return compId;
    }

    /**
     * 设置公司ID
     *
     * @param compId 公司ID
     */
    public void setCompId(Long compId) {
        this.compId = compId;
    }

    /**
     * 获取部门ID
     *
     * @return dept_id - 部门ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * @return create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改人
     *
     * @return update_user - 修改人
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置修改人
     *
     * @param updateUser 修改人
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取逻辑删除标记，0表示未删除；1表示删除
     *
     * @return del_flag - 逻辑删除标记，0表示未删除；1表示删除
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * 设置逻辑删除标记，0表示未删除；1表示删除
     *
     * @param delFlag 逻辑删除标记，0表示未删除；1表示删除
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public enum InnerColumn {
        roleId("role_id"),
        roleName("role_name"),
        roleDescribe("role_describe"),
        compId("comp_id"),
        deptId("dept_id"),
        createUser("create_user"),
        createTime("create_time"),
        updateUser("update_user"),
        updateTime("update_time"),
        delFlag("del_flag");

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