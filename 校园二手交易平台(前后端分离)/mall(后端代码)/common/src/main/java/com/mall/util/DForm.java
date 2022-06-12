package com.mall.util;

import java.text.DecimalFormat;

//获得两位小数的数
public class DForm {
    public double getDouble(double a){
        DecimalFormat df = new DecimalFormat("#.##");
        double b = Double.parseDouble(df.format(a));
        return b;
    }
}
