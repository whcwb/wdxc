package com.ldz.util.bean;

public class PointListBean {

    private double latitude; // 纬度

    private double longitude; // 经度

    private Long loc_time; // 当前坐标点时间

    private String coord_type_input; // 上传经纬度坐标类型 bd09ll ： 百度

    private double speed; // 速度

    private double direction; // 方向角

    private double height; // 高度

    private double radius; // 弧度

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getLoc_time() {
        return loc_time;
    }

    public void setLoc_time(Long loc_time) {
        this.loc_time = loc_time;
    }

    public String getCoord_type_input() {
        return coord_type_input;
    }

    public void setCoord_type_input(String coord_type_input) {
        this.coord_type_input = coord_type_input;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
