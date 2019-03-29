package com.ldz.util.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TrackPointsForReturn {

    private String status;

    private String message;

    private Integer total;

    private Integer  size;

    private Double distance;

    private Double toll_distance;

    private StartPoint start_point;

    private EndPoint end_point;

    private List<Point> points;

    @Setter
    @ToString
    @Getter
    public static class StartPoint{


        private Double longitude;

        private Double latitude;

        private String coord_type;

        private Long loc_time;


    }

    @Getter
    @ToString
    @Setter
    public static class EndPoint{

        private Double longitude;

        private Double latitude;

        private String coord_type;

        private Long loc_time;

       
    }

    @Setter
    @ToString
    @Getter
    public static class Point{

        private double longitude;

        private double latitude;

        private Long loc_time;

        private String _object_key;

        private Integer direction;
        
        private Double speed;
        
        /*private String create_time;


        private Double height;


        private Double radius;

        private String road_grade;

        private String locate_mode;

        private String transport_mode;

        private String floor;

        private String coord_type;*/




    }




}
