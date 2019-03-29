package com.ldz.util.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class GuiJiAnalyse {

    /**
     * status : 0
     * message : 成功
     * distance : 79707.90026776
     * duration : 38441
     * average_speed : 7.4646455858052
     * max_speed : 56.7
     * start_point : {"longitude":113.2075,"latitude":23.40675,"coord_type":"bd09ll","loc_time":1464134680,"address":"新景豪苑内,新凤凰酒店-休闲会所附近9米"}
     * end_point : {"longitude":113.208055,"latitude":23.406561,"coord_type":"bd09ll","loc_time":1464173121,"address":"盛业行东南49米"}
     * speeding_num : 2
     * harsh_acceleration_num : 1
     * harsh_breaking_num : 1
     * harsh_steering_num : 1
     * speeding : [{"speeding_distance":59.189285867195,"speeding_points":[{"longitude":113.20710875909,"latitude":23.39908060312,"coord_type":"bd09ll","loc_time":1464138828,"actual_speed":87.5,"limit_speed":60},{"longitude":113.20697265531,"latitude":23.399597906861,"coord_type":"bd09ll","loc_time":1464138843,"actual_speed":108.9,"limit_speed":80}]},{"speeding_distance":989.53701539415,"speeding_points":[{"longitude":113.21499929275,"latitude":23.413317284437,"coord_type":"bd09ll","loc_time":1464141367,"actual_speed":32.4,"limit_speed":15},{"longitude":113.21692508544,"latitude":23.408488134541,"coord_type":"bd09ll","loc_time":1464141794,"actual_speed":16.2,"limit_speed":15}]}]
     * harsh_acceleration : [{"longitude":113.21725316042,"latitude":23.412958154234,"coord_type":"bd09ll","loc_time":1464143029,"acceleration":2.75,"initial_speed":0,"end_speed":19.8}]
     * harsh_breaking : [{"longitude":113.2149819651,"latitude":23.411143610036,"coord_type":"bd09ll","loc_time":1464141265,"acceleration":-2.25,"initial_speed":34.2,"end_speed":26.1}]
     * harsh_steering : [{"longitude":113.2399844062,"latitude":23.415216724914,"coord_type":"bd09ll","loc_time":1464158589,"centripetal_acceleration":5.0324216823668,"turn_type":"left","speed":36.44}]
     */
    /**
     * 返回状态 0为成功
     */
    private int status;
    /**
     * 对status的中文描述
     */
    private String message;
    /**
     * 里程数 （米）
     */
    private double distance;
    /**
     * 行程耗时 （秒）
     */
    private int duration;
    /**
     * 平均时速
     */
    private double average_speed;
    /**
     * 最高时速
     */
    private double max_speed;
    /**
     * 起点信息
     */
    private StartPointBean start_point;
    /**
     * 终点信息
     */
    private EndPointBean end_point;
    /**
     * 超速次数
     */
    private int speeding_num;
    /**
     * 急加速次数
     */
    private int harsh_acceleration_num;
    /**
     * 急刹车次数
     */
    private int harsh_breaking_num;
    /**
     * 急转弯次数
     */
    private int harsh_steering_num;
    /**
     * 超速记录集合
     */
    private List<SpeedingBean> speeding;
    /**
     * 急加速记录集合
     */
    private List<HarshAccelerationBean> harsh_acceleration;
    /**
     * 急刹车记录集合
     */
    private List<HarshBreakingBean> harsh_breaking;
    /**
     * 急转弯记录集合
     */
    private List<HarshSteeringBean> harsh_steering;

    @NoArgsConstructor
    @Data
    public static class StartPointBean {
        /**
         * longitude : 113.2075
         * latitude : 23.40675
         * coord_type : bd09ll
         * loc_time : 1464134680
         * address : 新景豪苑内,新凤凰酒店-休闲会所附近9米
         */
        /**
         * 经度
         */
        private double longitude;
        /**
         * 纬度
         */
        private double latitude;
        /**
         * 坐标类型
         */
        private String coord_type;
        /**
         * 时间戳 （UNIX时间戳）
         */
        private int loc_time;
        /**
         * 起点地址
         */
        private String address;
    }

    @NoArgsConstructor
    @Data
    public static class EndPointBean {
        /**
         * longitude : 113.208055
         * latitude : 23.406561
         * coord_type : bd09ll
         * loc_time : 1464173121
         * address : 盛业行东南49米
         */
        // 同 起点
        private double longitude;
        private double latitude;
        private String coord_type;
        private int loc_time;
        private String address;
    }

    @NoArgsConstructor
    @Data
    public static class SpeedingBean {
        /**
         * speeding_distance : 59.189285867195
         * speeding_points : [{"longitude":113.20710875909,"latitude":23.39908060312,"coord_type":"bd09ll","loc_time":1464138828,"actual_speed":87.5,"limit_speed":60},{"longitude":113.20697265531,"latitude":23.399597906861,"coord_type":"bd09ll","loc_time":1464138843,"actual_speed":108.9,"limit_speed":80}]
         */
        /**
         * 超速里程
         */
        private double speeding_distance;
        /**
         * 超速点
         */
        private List<SpeedingPointsBean> speeding_points;

        @NoArgsConstructor
        @Data
        public static class SpeedingPointsBean {
            /**
             * longitude : 113.20710875909
             * latitude : 23.39908060312
             * coord_type : bd09ll
             * loc_time : 1464138828
             * actual_speed : 87.5
             * limit_speed : 60
             */
            /**
             * 经度
             */
            private double longitude;
            /**
             * 纬度
             */
            private double latitude;
            /**
             * 坐标类型
             */
            private String coord_type;
            /**
             * 时间戳 （UNIX时间戳）
             */
            private int loc_time;
            /**
             * 实际行驶速度  (km/h)
             */
            private double actual_speed;
            /**
             * 所在道路限定速度
             */
            private int limit_speed;
        }
    }

    @NoArgsConstructor
    @Data
    public static class HarshAccelerationBean {
        /**
         * longitude : 113.21725316042
         * latitude : 23.412958154234
         * coord_type : bd09ll
         * loc_time : 1464143029
         * acceleration : 2.75
         * initial_speed : 0
         * end_speed : 19.8
         */

        private double longitude;
        private double latitude;
        private String coord_type;
        private int loc_time;
        /**
         * 实际加速度
         */
        private double acceleration;
        /**
         * 加速前速度
         */
        private int initial_speed;
        /**
         * 加速后速度
         */
        private double end_speed;
    }

    @NoArgsConstructor
    @Data
    public static class HarshBreakingBean {
        /**
         * longitude : 113.2149819651
         * latitude : 23.411143610036
         * coord_type : bd09ll
         * loc_time : 1464141265
         * acceleration : -2.25
         * initial_speed : 34.2
         * end_speed : 26.1
         */
        // 同急加速
        private double longitude;
        private double latitude;
        private String coord_type;
        private int loc_time;
        private double acceleration;
        private double initial_speed;
        private double end_speed;
    }

    @NoArgsConstructor
    @Data
    public static class HarshSteeringBean {
        /**
         * longitude : 113.2399844062
         * latitude : 23.415216724914
         * coord_type : bd09ll
         * loc_time : 1464158589
         * centripetal_acceleration : 5.0324216823668
         * turn_type : left
         * speed : 36.44
         */

        private double longitude;
        private double latitude;
        private String coord_type;
        private int loc_time;
        /**
         * 向心加速度  （m/s2）
         */
        private double centripetal_acceleration;
        /**
         * 转弯类型 取值范围：unknow（方向未知）,left（左转）,right（右转）
         */
        private String turn_type;
        /**
         * 转弯速度
         */
        private double speed;
    }
}
