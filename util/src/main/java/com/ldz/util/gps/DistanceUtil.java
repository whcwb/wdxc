package com.ldz.util.gps;
/**
 * 类功能说明：地图坐标距离计算工具类
 * Title: DistanceUtil.java
 * @author liuxing
 * @date 2013-9-8 下午10:36:03
 * @version V1.0
 */
public class DistanceUtil {

	private static double DEF_PI = Math.PI; // PI
	private static double DEF_2PI = Math.PI * 2; // 2*PI
	private static double DEF_PI180 = Math.PI / 180; // PI/180.0
	private static double DEF_R = 6370693.5; // 地球半径


	public static double getShortDistance(Gps current,Gps target) {
		return getShortDistance(current.getWgLon(),current.getWgLat(),target.getWgLon(),target.getWgLat());
	}

	/**
	 * 
	 * 功能说明：计算两个地图坐标点之间的距离（近距离计算）
	 * liuxing 2013-9-8 下午10:42:17
	 * @param lng1 起点经度
	 * @param lat1 起点纬度
	 * @param lng2 终点经度
	 * @param lat2 终点纬度
	 * @return
	 */
	public static double getShortDistance(double lng1, double lat1, double lng2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
		ew1 = Math.toRadians(lng1);
		ns1 = Math.toRadians(lat1);
		ew2 = Math.toRadians(lng2);
		ns2 = Math.toRadians(lat2);
		
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI){
			dew = DEF_2PI - dew;
		} else if (dew < -DEF_PI){
			dew = DEF_2PI + dew;
		}
			
		dx = DEF_R * Math.cos(ns1) * dew; 	// 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); 			// 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	/**
	 * 
	 * 功能说明：计算两个地图坐标点之间的距离（远距离计算）
	 * liuxing 2013-9-8 下午10:43:21
	 * @param lng1 起点经度
	 * @param lat1 起点纬度
	 * @param lng2 终点经度
	 * @param lat2 终点纬度
	 * @return
	 */
	public static double getLongDistance(double lng1, double lat1, double lng2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		
		// 角度转换为弧度
		ew1 = lng1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lng2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0){
			distance = 1.0;
		} else if (distance < -1.0){
			distance = -1.0;
		}
		
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}
	
	public static void main(String[] args) {
		double mLat1 = 31.24081800000000; 	// point1纬度
		double mLng1 = 121.46541700000000; 	// point1经度
		double mLat2 = 31.239946;	// point2纬度
		double mLng2 = 121.466417;	// point2经度
		
		double distanceByShort = getShortDistance(mLng1, mLat1, mLng2, mLat2);
		System.out.println(distanceByShort);
		
		double distanceByLong = getLongDistance(mLng1, mLat1, mLng2, mLat2);
		System.out.println(distanceByLong);
	}

}