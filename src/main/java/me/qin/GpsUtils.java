package me.qin;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: 各坐标系之间的转换工具类
 *
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系。
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外,谷歌中国地图采用的是GCJ02地理坐标系。)
 *
 * GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。
 * 出于国家安全考虑，国内所有导航电子地图必须使用国家测绘局制定的加密坐标系统，即将一个真实的经纬度坐标加密成一个不正确的经纬度坐标。
 *
 * BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系。搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 *
 * 高德MapABC地图API 火星坐标
 * 腾讯搜搜地图API 火星坐标
 * 阿里云地图API 火星坐标
 * 灵图51ditu地图API 火星坐标
 *
 * 百度地图API 百度坐标
 * 搜狐搜狗地图API 搜狗坐标
 * 图吧MapBar地图API 图吧坐标
 *
 * @author JourWon
 * @date Created on 2018年6月19日
 */
public class GpsUtils {

	// 圆周率π
	private static final double PI = 3.1415926535897932384626D;

	// 火星坐标系与百度坐标系转换的中间量
	private static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0D;

	// Krasovsky 1940
	// 长半轴a = 6378245.0, 1/f = 298.3
	// b = a * (1 - f)
	// 扁率ee = (a^2 - b^2) / a^2;

	// 长半轴
	private static final double SEMI_MAJOR = 6378245.0D;

	private static final double EARTH_RADIUS = 6371.01; // 地球半径，单位：千米

	// 扁率
	private static final double FLATTENING = 0.00669342162296594323D;
	// Vincenty算法计算精度比Haversine算法高，但计算量更大
	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
		double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563;
		double L = Math.toRadians(lon2 - lon1);
		double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
		double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
		double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
		double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);
		double cosSqAlpha;
		double sinSigma;
		double cos2SigmaM;
		double cosSigma;
		double sigma;

		double lambda = L, lambdaP, iterLimit = 100;
		do {
			double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);
			sinSigma = Math.sqrt((cosU2 * sinLambda)
					* (cosU2 * sinLambda)
					+ (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
					* (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
			);
			if (sinSigma == 0) {
				return 0;
			}

			cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
			sigma = Math.atan2(sinSigma, cosSigma);
			double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
			cosSqAlpha = 1 - sinAlpha * sinAlpha;
			cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;

			double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
			lambdaP = lambda;
			lambda = L + (1 - C) * f * sinAlpha
					* (sigma + C * sinSigma
					* (cos2SigmaM + C * cosSigma
					* (-1 + 2 * cos2SigmaM * cos2SigmaM)
			)
			);

		} while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

		if (iterLimit == 0) {
			return 0;
		}

		double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
		double A = 1 + uSq / 16384
				* (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
		double deltaSigma =
				B * sinSigma
						* (cos2SigmaM + B / 4
						* (cosSigma
						* (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
						* (-3 + 4 * sinSigma * sinSigma)
						* (-3 + 4 * cos2SigmaM * cos2SigmaM)));

		double distance  = b * A * (sigma - deltaSigma);
		return Math.round(distance * 100.0) / 100.0;
	}

	public static double getDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
		return getDistance(lat1.doubleValue(), lon1.doubleValue(), lat2.doubleValue(), lon2.doubleValue());
	}

	/**
	 * 计算两个地理坐标点之间的距离
	 * @param lat1 第一个点的纬度
	 * @param lon1 第一个点的经度
	 * @param lat2 第二个点的纬度
	 * @param lon2 第二个点的经度
	 * @return 两个点之间的距离，单位：米
	 */
	public static long calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
		// 将角度转换为弧度
		double lat1Rad = Math.toRadians(lat1.doubleValue());
		double lon1Rad = Math.toRadians(lon1.doubleValue());
		double lat2Rad = Math.toRadians(lat2.doubleValue());
		double lon2Rad = Math.toRadians(lon2.doubleValue());

		// Haversine公式
		double dLat = lat2Rad - lat1Rad;
		double dLon = lon2Rad - lon1Rad;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(lat1Rad) * Math.cos(lat2Rad) *
						Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		// 计算距离
		return Math.round(EARTH_RADIUS * c * 1000);
	}

	// WGS84=>GCJ02 地球坐标系=>火星坐标系
	public static Point wgs84ToGcj02(double lng, double lat) {
		if (outOfChina(lng, lat)) {
			return new Point(lng, lat);
		}

		double[] offset = offset(lng, lat);
		double mglng = lng + offset[0];
		double mglat = lat + offset[1];

		return new Point(mglng, mglat);
	}

	// GCJ02=>WGS84 火星坐标系=>地球坐标系(粗略)
	public static Point gcj02ToWgs84(double lng, double lat) {
		if (outOfChina(lng, lat)) {
			return new Point(lng, lat);
		}

		double[] offset = offset(lng, lat);
		double mglng = lng - offset[0];
		double mglat = lat - offset[1];

		return new Point(mglng, mglat);
	}

	// GCJ02=>WGS84 火星坐标系=>地球坐标系（精确）
	public static Point gcj02ToWgs84Exactly(double lng, double lat) {
		if (outOfChina(lng, lat)) {
			return new Point(lng, lat);
		}

		double initDelta = 0.01;
		double threshold = 0.000000001;
		double dLat = initDelta, dLon = initDelta;
		double mLat = lat - dLat, mLon = lng - dLon;
		double pLat = lat + dLat, pLon = lng + dLon;
		double wgsLat, wgsLng, i = 0;
		while (true) {
			wgsLat = (mLat + pLat) / 2;
			wgsLng = (mLon + pLon) / 2;
			Point point = wgs84ToGcj02(wgsLng, wgsLat);
			dLon = point.getLng() - lng;
			dLat = point.getLat() - lat;
			if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
				break;

			if (dLat > 0)
				pLat = wgsLat;
			else
				mLat = wgsLat;
			if (dLon > 0)
				pLon = wgsLng;
			else
				mLon = wgsLng;

			if (++i > 10000)
				break;
		}

		return new Point(wgsLng, wgsLat);
	}

	// GCJ-02=>BD09 火星坐标系=>百度坐标系
	public static Point gcj02ToBd09(double lng, double lat) {
		double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
		double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * X_PI);
		double bd_lng = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new Point(bd_lng, bd_lat);
	}

	// BD09=>GCJ-02 百度坐标系=>火星坐标系
	public static Point bd09ToGcj02(double lng, double lat) {
		double x = lng - 0.0065;
		double y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
		double gcj_lng = z * Math.cos(theta);
		double gcj_lat = z * Math.sin(theta);
		return new Point(gcj_lng, gcj_lat);
	}

	// WGS84=>BD09 地球坐标系=>百度坐标系
	public static Point wgs84ToBd09(double lng, double lat) {
		Point point = wgs84ToGcj02(lng, lat);
		return gcj02ToBd09(point.getLng(), point.getLat());
	}

	// BD09=>WGS84 百度坐标系=>地球坐标系
	public static Point bd09ToWgs84(double lng, double lat) {
		Point point = bd09ToGcj02(lng, lat);
		return gcj02ToWgs84(point.getLng(), point.getLat());
	}

	/**
	 * Description: 中国境外返回true,境内返回false
	 * @param lng 	经度
	 * @param lat	纬度
	 * @return
	 */
	public static boolean outOfChina(double lng, double lat) {
		if (lng < 72.004 || lng > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	// 经度偏移量
	private static double transformLng(double lng, double lat) {
		double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
		return ret;
	}

	// 纬度偏移量
	private static double transformLat(double lng, double lat) {
		double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat
				+ 0.2 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	// 偏移量
	public static double[] offset(double lng, double lat) {
		double[] lngLat = new double[2];
		double dlng = transformLng(lng - 105.0, lat - 35.0);
		double dlat = transformLat(lng - 105.0, lat - 35.0);
		double radlat = lat / 180.0 * PI;
		double magic = Math.sin(radlat);
		magic = 1 - FLATTENING * magic * magic;
		double sqrtmagic = Math.sqrt(magic);
		dlng = (dlng * 180.0) / (SEMI_MAJOR / sqrtmagic * Math.cos(radlat) * PI);
		dlat = (dlat * 180.0) / ((SEMI_MAJOR * (1 - FLATTENING)) / (magic * sqrtmagic) * PI);
		lngLat[0] = dlng;
		lngLat[1] = dlat;
		return lngLat;
	}


	// Point 类
	public static class Point implements Serializable {
		private static final long serialVersionUID = 3295765664888106376L;
		private double lng;
		private double lat;

		public Point() {
		}

		public Point(double lng, double lat) {
			this.lng = lng;
			this.lat = lat;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		@Override
		public String toString() {
			return "Point{" +
					"lng=" + lng +
					", lat=" + lat +
					'}';
		}
	}

	public static void main(String[] args) {
		// 昆明 102.84,24.88
		double lng1 = 102.84;
		double lat1 = 24.88;

		// 北京
		double lng2 = 116.45;
		double lat2 = 39.89;

		// 百度地图 2085.7 km
		long l1 = System.currentTimeMillis();
		double distance = getDistance(lat1, lng1, lat2, lng2);
		System.out.println(System.currentTimeMillis() - l1);;
		System.out.println(distance);

		long l2 = System.currentTimeMillis();
		long d = calculateDistance(BigDecimal.valueOf(lat1), BigDecimal.valueOf(lng1), BigDecimal.valueOf(lat2), BigDecimal.valueOf(lng2));
		System.out.println(System.currentTimeMillis() - l2);;
		System.out.println(d);
	}
}