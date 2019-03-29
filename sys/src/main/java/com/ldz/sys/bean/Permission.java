package com.ldz.sys.bean;

import com.ldz.sys.model.SysBiz;
import com.ldz.sys.model.SysResource;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台权限树数据结构
 */
public class Permission {
    private Long id;
    private Long pid;
    private Integer level;
    private Integer order;
    private String menuId;
    private String menuUrl;
    private String icon;
    private String apiPrefix;
    private String parentMenuCode;
    private String bizNo;
    private String title;                   // 显示文字
    private List<Permission> children;      // 子节点
    private boolean expand;                 // 是否展开
    private boolean disabled;               // 禁掉响应
    private boolean disabledCheckBox;       // 禁掉checkbox
    private boolean checked;                // 是否勾选
    private boolean selected;               // 是否选中子节点

    public Permission(SysResource resource) {
        this.id = resource.getResId();
        this.menuId = this.id.toString();
        this.pid = resource.getResPid();
        this.level = resource.getResLevel();
        this.title = resource.getResName();
        this.icon = resource.getResIcon();
        this.order = resource.getResOrder();
        this.apiPrefix = resource.getApiPrefix();
        this.menuUrl = resource.getRouterPrefix() + resource.getResUrl();
        this.expand = true;
    }

    public Permission(SysBiz biz){
        this.id = biz.getBizId();
        this.menuId = this.id.toString();
        this.title = biz.getBizName();
        this.parentMenuCode = biz.getBizNo();
        this.level = 0;
        this.bizNo = biz.getBizNo();
        this.children = new ArrayList<>();
        this.expand = true;
//        this.selected = true;
    }

    public Integer getOrder() {
        return order;
    }

    public String getApiPrefix() {
        return apiPrefix;
    }

    public String getMenuUrl() {
        return menuUrl;
    }


    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentMenuCode() {
        return parentMenuCode;
    }

    public void setParentMenuCode(String parentMenuCode) {
        this.parentMenuCode = parentMenuCode;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabledCheckBox() {
        return disabledCheckBox;
    }

    public void setDisabledCheckBox(boolean disabledCheckBox) {
        this.disabledCheckBox = disabledCheckBox;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
