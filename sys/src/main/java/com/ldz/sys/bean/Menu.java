package com.ldz.sys.bean;

import com.ldz.sys.model.SysGn;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Menu {
    private String id;
    private String pid;
    private String icon;
    private String name;
    private String path;
    private String title;
    private List<Menu> children;

    public Menu(){

    }
    public Menu(SysGn function){
        this.id = function.getGndm();
        this.pid = function.getFjd();
        this.icon = function.getTb();
        this.name = function.getUrl();
        this.path = "/"+function.getUrl();
        this.title = function.getGnmc();
    }
}
