package com.tuanzhang.dianping.common;

import com.tuanzhang.dianping.info.Point2D;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class CommonUtil {


    public static String processErrorString(BindingResult bindingResult){
        if (!bindingResult.hasErrors()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage() + ",");
        }

       return stringBuilder.substring(0, stringBuilder.length() -1).toString();
    }


    private static final double EARTH_RADIUS = 6371393; // 平均半径,单位：m

    /**
     * 通过AB点经纬度获取距离
     * @param pointA A点(经，纬)
     * @param pointB B点(经，纬)
     * @return 距离(单位：米)
     */
    public static double getDistance(Point2D pointA, Point2D pointB) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(pointA.getX().doubleValue()); // A经弧度
        double radiansAY = Math.toRadians(pointA.getY().doubleValue()); // A纬弧度
        double radiansBX = Math.toRadians(pointB.getX().doubleValue()); // B经弧度
        double radiansBY = Math.toRadians(pointB.getY().doubleValue()); // B纬弧度

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
//        System.out.println("cos = " + cos); // 值域[-1,1]
        double acos = Math.acos(cos); // 反余弦值
//        System.out.println("acos = " + acos); // 值域[0,π]
//        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        return EARTH_RADIUS * acos; // 最终结果
    }
}
