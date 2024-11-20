package me.qin;
import java.util.regex.Pattern;

/**
 * @Description 车牌正则表达式
 * @Author qzh
 * @Date 2024/3/11 14:21
 * @Version V1.0
 * @ClassName me.qin.VehicleNoUtil
 */
public class VehicleNoUtil {
    // 预编译正则表达式以提高性能
    /**
     * 新能源车牌
     */
    private static final Pattern NEW_ENERGY_VEHICLE_PATTERN = Pattern.compile(
            "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z](([0-9]{5}[DABCEFGHJK])|([DABCEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4}))$"
    );
    /**
     * 新能源小型车
     */
    private static final Pattern NEW_ENERGY_SMALL_CAR_PATTERN = Pattern.compile(
            "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][DABCEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4}$"
    );
    /**
     * 新能源大型车
     */
    private static final Pattern NEW_ENERGY_LARGE_CAR_PATTERN = Pattern.compile(
            "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][0-9]{5}[DABCEFGHJK]$"
    );
    /**
     * 普通车辆/传统车辆
     */
    private static final Pattern ORDINARY_VEHICLE_PATTERN = Pattern.compile(
            "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]$"
    );


    // Small car 小型车
    // large car 大型车

    /**
     * 校验是否为新能源车
     *
     * @param vehicleNo 车牌号
     * @return true-新能源车
     */
    public static boolean isNewEnergyVehicle(String vehicleNo) {
        if (isEmpty(vehicleNo)) {
            return false;
        }
        return NEW_ENERGY_VEHICLE_PATTERN.matcher(vehicleNo).matches();
    }

    /**
     * 新能源小型车
     *
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isNewEnergySmallCar(String vehicleNo) {
        if (isEmpty(vehicleNo)) {
            return false;
        }
        return NEW_ENERGY_SMALL_CAR_PATTERN.matcher(vehicleNo).matches();
    }

    /**
     * 新能源大型车
     *
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isNewEnergyLargeCar(String vehicleNo) {
        if (isEmpty(vehicleNo)) {
            return false;
        }
        return NEW_ENERGY_LARGE_CAR_PATTERN.matcher(vehicleNo).matches();
    }

    /**
     * 普通车辆
     *
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isOrdinaryVehicle(String vehicleNo) {
        if (isEmpty(vehicleNo)) {
            return false;
        }
        return ORDINARY_VEHICLE_PATTERN.matcher(vehicleNo).matches();
    }

    /**
     * 所有车
     *
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isVehicle(String vehicleNo) {
        if (isEmpty(vehicleNo)) {
            return false;
        }
        return NEW_ENERGY_VEHICLE_PATTERN.matcher(vehicleNo).matches() || ORDINARY_VEHICLE_PATTERN.matcher(vehicleNo).matches();
    }

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
