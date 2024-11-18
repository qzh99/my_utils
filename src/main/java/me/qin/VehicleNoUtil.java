package me.qin;

import java.util.regex.Pattern;

/**
 * @Description 车牌正则表达式
 * @Author QinZhengHua
 * @Date 2024/3/11 14:21
 * @Version V1.0
 * @ClassName com.rhjc56.smart.park.common.tools.VehicleNoUtil
 */
public class VehicleNoUtil {
    /**
     * 新能源车牌
     */
    public static final String NEW_ENERGY_VEHICLE_REGEXP = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z](([0-9]{5}[DABCEFGHJK])|([DABCEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4}))$";
    /**
     * 所有车牌
     */
    public static final String ALL_VEHICLE_REGEXP = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z](([0-9]{5}[DABCEFGHJK])|([DABCEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";

    /**
     * 新能源小型车
     */
    public static final String NEW_ENERGY_SMALL_CAR_REGEXP = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][DABCEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4}$";
    public static final String NEW_ENERGY_LARGE_CAR_REGEXP = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][0-9]{5}[DABCEFGHJK]$";
    /**
     * 普通车辆
     */
    public static final String ORDINARY_VEHICLE = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]$";

    // Small car 小型车
    // large car 大型车
    /**
     * 校验是否为新能源车
     * @param vehicleNo 车牌号
     * @return true-新能源车
     */
    public static boolean isNewEnergyVehicle(String vehicleNo) {
        Pattern compile = Pattern.compile(NEW_ENERGY_VEHICLE_REGEXP);
        if (vehicleNo == null || vehicleNo.isEmpty()) {
            return false;
        }
        boolean rt = false;
        try {
            rt = Pattern.matches(NEW_ENERGY_VEHICLE_REGEXP, vehicleNo);
        } catch (Exception ignored) {
        }
        return rt;
    }

    /**
     * 新能源小型车
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isNewEnergySmallCar(String vehicleNo) {
        if (vehicleNo == null || vehicleNo.isEmpty()) {
            return false;
        }

        boolean rt = false;
        try {
            rt = Pattern.matches(NEW_ENERGY_SMALL_CAR_REGEXP, vehicleNo);
        } catch (Exception ignored) {
        }
        return rt;
    }

    /**
     * 新能源大型车
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isNewEnergyLargeCar(String vehicleNo) {
        if (vehicleNo == null || vehicleNo.isEmpty()) {
            return false;
        }

        boolean rt = false;
        try {
            rt = Pattern.matches(NEW_ENERGY_LARGE_CAR_REGEXP, vehicleNo);
        } catch (Exception ignored) {
        }
        return rt;
    }

    /**
     * 普通车辆
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isOrdinaryVehicle(String vehicleNo) {
        if (vehicleNo == null || vehicleNo.isEmpty()) {
            return false;
        }

        boolean rt = false;
        try {
            rt = Pattern.matches(ORDINARY_VEHICLE, vehicleNo);
        } catch (Exception ignored) {
        }
        return rt;
    }

    /**
     * 所有车
     * @param vehicleNo 车牌
     * @return boolean
     */
    public static boolean isVehicle(String vehicleNo) {
        if (vehicleNo == null || vehicleNo.isEmpty()) {
            return false;
        }

        boolean rt = false;
        try {
            rt = Pattern.matches(ALL_VEHICLE_REGEXP, vehicleNo);
        } catch (Exception ignored) {
        }
        return rt;
    }
}
