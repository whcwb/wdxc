package com.ldz.util.bean;

import java.util.List;

public class NewTrackPointReturn {

    private int status;

    private String message;

    private int total;

    private double distance;

    private double toll_distance;

    private List<Point> points;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getToll_distance() {
        return toll_distance;
    }

    public void setToll_distance(double toll_distance) {
        this.toll_distance = toll_distance;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }




}
