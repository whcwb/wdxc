package com.ldz.util.bean;

public class Point{

    private double longitude;

    private double latitude;

    private long loc_time;

    private double direction;

    private double height;

    private double speed;

    private double radius;

    private String road_grade;

    private String car_limit_speed;

    private String coord_type;

    private String _object_key;

    public String get_object_key() {
        return _object_key;
    }

    public void set_object_key(String _object_key) {
        this._object_key = _object_key;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getLoc_time() {
        return loc_time;
    }

    public void setLoc_time(long loc_time) {
        this.loc_time = loc_time;
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getRoad_grade() {
        return road_grade;
    }

    public void setRoad_grade(String road_grade) {
        this.road_grade = road_grade;
    }

    public String getCar_limit_speed() {
        return car_limit_speed;
    }

    public void setCar_limit_speed(String car_limit_speed) {
        this.car_limit_speed = car_limit_speed;
    }

    public String getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(String coord_type) {
        this.coord_type = coord_type;
    }
}
