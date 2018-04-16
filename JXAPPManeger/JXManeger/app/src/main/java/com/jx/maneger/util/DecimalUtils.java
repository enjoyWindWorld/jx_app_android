/**
 * @Title: DecimalUtils.java 
 * @Package com.eachgame.android.utils 
 * @Description: TODO(单双精度运算工具类) 
 * @author EachGame Android Team, Erin  
 * @date 2015-4-14 下午6:38:39 
 * @version V2.0.0
 */
package com.jx.maneger.util;

import java.math.BigDecimal;

/**
 * @Description: TODO(单双精度运算工具类) 
 * @author EachGame Android Team, Erin
 */
public class DecimalUtils {
    
    /**
     * 除法运算
     * @param devider 除数
     * @param deviend 被除数
     * @param len 精度
     * @return 浮点型
     */
    public static float devide(int devider, int deviend, int len) {
	if(deviend == 0) {
	    return 0.0f;
	}
	BigDecimal dec = new BigDecimal(devider);
	dec = dec.divide(new BigDecimal(deviend), len, BigDecimal.ROUND_HALF_UP);
	return dec.floatValue();
    }
    
    /**
     * 除法运算
     * @param devider 除数
     * @param deviend 被除数
     * @param len 精度
     * @return 浮点型
     */
    public static float devide(float devider, float deviend, int len) {
	BigDecimal dec = new BigDecimal(devider);
	dec = dec.divide(new BigDecimal(deviend), len, BigDecimal.ROUND_HALF_UP);
	return dec.floatValue();
    }
    
    /**
     * 除法运算
     * @param devider 除数
     * @param deviend 被除数
     * @param len 精度
     * @return 浮点型
     */
    public static double devide(double devider, double deviend, int len) {
	BigDecimal dec = new BigDecimal(devider);
	dec = dec.divide(new BigDecimal(deviend), len, BigDecimal.ROUND_HALF_UP);
	return dec.doubleValue();
    }

}
