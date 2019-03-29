package com.ldz.util.gps;

import java.util.ArrayList;
import java.util.List;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。 chenhua
 */
public class PositionUtil {

	public static final String BAIDU_LBS_TYPE = "bd09ll";

	private static double x_PI = 3.14159265358979324 * 3000.0 / 180.0;
	public static double pi = 3.1415926535897932384626;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;

	/**
	 * 判断当前位置与是否在接近目标位置
	 * @param current
	 * @param target
	 * @param direct
	 * @return
	 */
	public static boolean isNear(Gps current,Gps target,String direct){
		return true;
	}

	/**
	 * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
	 * 
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static Gps gps84_To_Gcj02(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return null;
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new Gps(mgLat, mgLon);
	}

	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 */
	public static Gps gcj_To_Gps84(double lat, double lon) {
		Gps gps = transform(lat, lon);
		double lontitude = lon * 2 - gps.getWgLon();
		double latitude = lat * 2 - gps.getWgLat();
		return new Gps(latitude, lontitude);
	}

	/**
	 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
	 * 
	 * @param gg_lat
	 * @param gg_lon
	 */
	public static Gps gcj02_To_Bd09(double gg_lat, double gg_lon) {
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_PI);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_PI);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		
		
		return new Gps(bd_lat, bd_lon);
	}

	/**
	 * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 *
	 * * @param bd_lat * @param bd_lon * @return
	 */
	public static Gps bd09_To_Gcj02(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new Gps(gg_lat, gg_lon);
	}

	/**
	 * (BD-09)-->84
	 * 
	 * @param bd_lat
	 * @param bd_lon
	 * @return
	 */
	public static Gps bd09_To_Gps84(double bd_lat, double bd_lon) {

		Gps gcj02 = PositionUtil.bd09_To_Gcj02(bd_lat, bd_lon);
		Gps map84 = PositionUtil.gcj_To_Gps84(gcj02.getWgLat(), gcj02.getWgLon());
		return map84;

	}

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	public static Gps transform(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return new Gps(lat, lon);
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new Gps(mgLat, mgLon);
	}

	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * 是否有 横断<br/>
	 * 参数为四个点的坐标
	 * 
	 * @param px1
	 * @param py1
	 * @param px2
	 * @param py2
	 * @param px3
	 * @param py3
	 * @param px4
	 * @param py4
	 * @return
	 */
	public static boolean isIntersect(double px1, double py1, double px2, double py2, double px3, double py3, double px4,
			double py4) {
		boolean flag = false;
		double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
		if (d != 0) {
			double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
			double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
			if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 目标点是否在目标边上边上<br/>
	 * 
	 * @param px0
	 *            目标点的经度坐标
	 * @param py0
	 *            目标点的纬度坐标
	 * @param px1
	 *            目标线的起点(终点)经度坐标
	 * @param py1
	 *            目标线的起点(终点)纬度坐标
	 * @param px2
	 *            目标线的终点(起点)经度坐标
	 * @param py2
	 *            目标线的终点(起点)纬度坐标
	 * @return
	 */
	public static boolean isPointOnLine(double px0, double py0, double px1, double py1, double px2, double py2) {
		boolean flag = false;
		double ESP = 1e-9;// 无限小的正数
		if ((Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0)
				&& ((py0 - py1) * (py0 - py2) <= 0)) {
			flag = true;
		}
		return flag;
	}

	public static double Multiply(double px0, double py0, double px1, double py1, double px2, double py2) {
		return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
	}

	/**
	 * 判断目标点是否在多边形内(由多个点组成)<br/>
	 * 
	 * @param px
	 *            目标点的经度坐标
	 * @param py
	 *            目标点的纬度坐标
	 * @param polygonXA
	 *            多边形的经度坐标集合
	 * @param polygonYA
	 *            多边形的纬度坐标集合
	 * @return
	 */
	public static boolean isPointInPolygon(double px, double py, ArrayList<Double> polygonXA, ArrayList<Double> polygonYA) {
		boolean isInside = false;
		double ESP = 1e-9;
		int count = 0;
		double linePoint1x;
		double linePoint1y;
		double linePoint2x = 180;
		double linePoint2y;

		linePoint1x = px;
		linePoint1y = py;
		linePoint2y = py;

		for (int i = 0; i < polygonXA.size() - 1; i++) {
			double cx1 = polygonXA.get(i);
			double cy1 = polygonYA.get(i);
			double cx2 = polygonXA.get(i + 1);
			double cy2 = polygonYA.get(i + 1);
			// 如果目标点在任何一条线上
			if (isPointOnLine(px, py, cx1, cy1, cx2, cy2)) {
				return true;
			}
			// 如果线段的长度无限小(趋于零)那么这两点实际是重合的，不足以构成一条线段
			if (Math.abs(cy2 - cy1) < ESP) {
				continue;
			}
			// 第一个点是否在以目标点为基础衍生的平行纬度线
			if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
				// 第二个点在第一个的下方,靠近赤道纬度为零(最小纬度)
				if (cy1 > cy2)
					count++;
			}
			// 第二个点是否在以目标点为基础衍生的平行纬度线
			else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
				// 第二个点在第一个的上方,靠近极点(南极或北极)纬度为90(最大纬度)
				if (cy2 > cy1)
					count++;
			}
			// 由两点组成的线段是否和以目标点为基础衍生的平行纬度线相交
			else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
				count++;
			}
		}
		if (count % 2 == 1) {
			isInside = true;
		}

		return isInside;
	}

	public static void main(String[] args) {
		//(113.944421,22.528841) (113.94629,22.529208)
		//isPointInPolygon(area.createDouble(),area.createDouble()); (114.082402,22.550271) 114.075323,22.543528)
		System.out.println(isPointInPolygon(30.6111748884,114.2521342948));
		
	}
	private static Boolean isPointInPolygon( double px , double py ){
		
		String latlngs = "114.27657999999998,31.142587321849817;114.18479301152472,31.136371890983927;114.09530123110557,31.117881051087423;114.01033981683949,31.08757708812572;113.9320256733088,31.046217031040214;113.86230285441387,30.99483291560961;113.80289316704045,30.93470500324525;113.75525333896309,30.867328756243975;113.72053983602434,30.79437653631015;113.69958210844769,30.71765511811188;113.6928647346043,30.639060192315103;113.70051863100095,30.56052907372379;113.72232122431882,30.48399283286278;113.75770524506515,30.411329038177563;113.80577560806348,30.344316236489163;113.86533369349199,30.284591217170714;113.93490823079887,30.233610006156145;114.01279191138917,30.192613424150917;114.09708280784764,30.162597923232337;114.18572965054501,30.144292290440486;114.27657999999998,30.138140678150187;114.36743034945495,30.144292290440486;114.45607719215236,30.162597923232337;114.54036808861085,30.192613424150917;114.61825176920115,30.233610006156145;114.68782630650801,30.284591217170714;114.74738439193646,30.344316236489163;114.79545475493485,30.411329038177563;114.83083877568114,30.48399283286278;114.85264136899906,30.56052907372379;114.8602952653957,30.639060192315103;114.85357789155233,30.71765511811188;114.83262016397568,30.79437653631015;114.79790666103692,30.867328756243975;114.75026683295951,30.93470500324525;114.6908571455861,30.99483291560961;114.62113432669116,31.04621703104021;114.54282018316047,31.08757708812572;114.45785876889444,31.117881051087423;114.36836698847524,31.136371890983927;114.27657999999998,31.142587321849817;";
		String[] arrays = latlngs.split(";");
		List<Gps> areas=new ArrayList<Gps>();
		for (int i=0; i<arrays.length; i++){
			areas.add(new Gps(Double.parseDouble(arrays[i].split(",")[0]), Double.parseDouble(arrays[i].split(",")[1])));
		}
		
		ArrayList<Double> polygonXA = new ArrayList<Double>();  
		ArrayList<Double> polygonYA = new ArrayList<Double>(); 
		for(int i=0;i<areas.size();i++){
			Gps area = areas.get(i);
			polygonXA.add(area.getWgLon());
			polygonYA.add(area.getWgLat());
		}
		
		Boolean flag= PositionUtil.isPointInPolygon(px, py, polygonXA, polygonYA);
		
		return  flag;
	}
}