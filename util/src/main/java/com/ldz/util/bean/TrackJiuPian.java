package com.ldz.util.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 轨迹纠偏模型
 */
@Getter
@Setter
@ToString
public class TrackJiuPian {
    /**
     * 用户的ak，授权使用 (必填)
     */
    private String ak; //2pVOrCuBldNDOgDtwaYSP8gpQ2VQdZY9

    /**
     *service唯一标识 (必填)
     */
    private int service_id; //200383

    /**
     * entity唯一标识 (必填)
     */
    private String entity_name; //CWB201805181744

    /**
     * 起始时间（起始的loc_time） (必填)  UNIX时间戳
     */
    private String start_time; //1526637371
    /**
     * 结束时间（结束的loc_time） (必填) UNIX时间戳
     */
    private String end_time; //1526639121

    /**
     * 是否返回纠偏后轨迹
     * 0：关闭轨迹纠偏，返回原始轨迹
     * 1：打开轨迹纠偏，返回纠偏后轨迹。打开纠偏时，请求时间段内轨迹点数量不能超过2万，否则将返回错误。
     */
    private String is_processed; // 1

    /**
     * 	纠偏选项
     * 	仅在is_processed=1时生效。默认值为：
     * 	need_denoise=1,radius_threshold=0, need_vacuate=1,need_mapmatch=0, radius_threhold=0,transport_mode=driving
     * 	取值规则为：
     * 	1.去噪，示例：
     * 	need_denoise =0：不去噪
     * 	need_denoise =1：去噪
     * 	2.抽稀，示例：
     * 	need_vacuate =0：不抽稀
     * need_vacuate=1：抽稀
     * 3.绑路，示例：
     * need_mapmatch=0：不绑路
     * need_mapmatch=1：绑路
     * 4.定位精度过滤，用于过滤掉定位精度较差的轨迹点，每个轨迹点示例：
     * radius_threshold=0：不过滤
     * radius_threshold=20：当取值大于0的整数时，则过滤掉radius大于20的轨迹点。 通常 GPS 定位精度不超过20米，Wi-Fi 定位精度不超过100米
     * 5.交通方式，鹰眼将根据不同交通工具选择不同的纠偏策略和参数，目前支持：自动（即鹰眼自动识别的交通方式）、驾车、骑行和步行，示例：
     * transport_mode=auto
     * transport_mode=driving
     * transport_mode=riding
     * transport_mode=walking
     */
    private String process_option; // need_denoise=1,radius_threshold=20,need_mapmatch=1
    /**
     * 里程补偿方式
     * 默认值：no_supplement
     * 在里程计算时，两个轨迹点定位时间间隔5分钟以上，被认为是中断。中断轨迹提供以下5种里程估算方式。
     * no_supplement：不补充，中断两点间距离不记入里程。
     *straight：使用直线距离补充
     * driving：使用最短驾车路线距离补充
     * riding：使用最短骑行路线距离补充
     * walking：使用最短步行路线距离补充
     */
    private String supplement_mode; // driving
    /**
     * 返回轨迹点的排序规则
     * 默认值：asc
     取值规则（注意：与 v1的默认值相反）：
     asc：按定位时间升序排序（旧->新）
     desc：按定位时间降序排序（新->旧）
     */
    private String sort_type; //asc
    /**
     * 返回的坐标类型
     * 默认值：bd09ll
     该字段用于控制返回结果中的坐标类型。可选值为：
     gcj02：国测局加密坐标
     bd09ll：百度经纬度坐标
     该参数仅对国内（包含港、澳、台）轨迹有效，海外区域轨迹均返回 wgs84坐标系
     */
    private String coord_type_output; //bd09ll

    private String page_size; //5000
    private String page_index; //1

    private double low_speed_threshold;

    


}
