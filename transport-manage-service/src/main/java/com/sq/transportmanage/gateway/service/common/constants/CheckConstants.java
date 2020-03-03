package com.sq.transportmanage.gateway.service.common.constants;

import java.util.regex.Pattern;

/**
 * @Author fanht
 * @Description
 * @Date 2020/3/3 上午11:20
 * @Version 1.0
 */
public final class CheckConstants {

    public  static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    /**功能：判断字符串是否为数字**/
    public  static Pattern pattern = Pattern.compile("[0-9]*");

    /**功能：判断字符串是否为日期格式**/
    public  static Pattern patternDate = Pattern
            .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

    /**功能：判断是否是数字格式带两位小数点**/
    public  static Pattern patternIsNumber = Pattern.compile("^\\d+(\\.\\d{0,2})?$");

}
