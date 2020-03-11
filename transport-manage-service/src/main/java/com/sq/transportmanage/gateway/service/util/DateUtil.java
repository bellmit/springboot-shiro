package com.sq.transportmanage.gateway.service.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 一些公用的日期方法
 *
 * @author jifeng
 */
public class DateUtil {
    public static final String LOCAL_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String MAIN_TIME_FORMAT = "yyyy年MM月dd日 HH时mm分";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String MONTH_FORMAT = "yyyy_MM";
    public static final String START_OF_DAY = " 00:00:00";
    public static final String END_OF_DAY = " 23:59:59";
    public static final SimpleDateFormat DATE_TIME_SIMPLE_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT);
    public static final SimpleDateFormat DATE_SIMPLE_FORMAT = new SimpleDateFormat(DATE_FORMAT);
    public static final SimpleDateFormat DATE_MONTH_FORMAT = new SimpleDateFormat(MONTH_FORMAT);
    public static final SimpleDateFormat TIME_SIMPLE_FORMAT = new SimpleDateFormat(TIME_FORMAT);

    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = Maps
            .newHashMapWithExpectedSize(2);
    private static final Object lockObj = new Object();

    public static SimpleDateFormat getFormat(String format){
        switch (format){
            case DATE_TIME_FORMAT:
                return DATE_TIME_SIMPLE_FORMAT;
            case DATE_FORMAT:
                return DATE_SIMPLE_FORMAT;
            case TIME_FORMAT:
                return TIME_SIMPLE_FORMAT;
        }
        return DATE_TIME_SIMPLE_FORMAT;
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的字符串时间
     */
    public static String createTimeString() {
        return DATE_TIME_SIMPLE_FORMAT.format(new Date());
    }

    /**
     * 返回yyyy-MM-dd格式的字符串时间
     */
    public static String createDateString() {
        return DATE_SIMPLE_FORMAT.format(new Date());
    }

    /**
     * 返回yyyy-MM-dd格式的字符串时间
     */
    public static String createMonthString() {
        return DATE_MONTH_FORMAT.format(new Date());
    }

    /**
     * 根据传入的参数返回yyyy-MM-dd格式的字符串时间
     */
    public static String getDateString(Date date) {
        return date == null ? "" : getSdf(DATE_FORMAT).format(date);
    }

    /**
     * 根据传入的参数返回yyyy-MM-dd HH:mm:ss格式的字符串时间
     */
    public static String getTimeString(Date date) {
        return date == null ? "" : getSdf(DATE_TIME_FORMAT).format(date);
    }

    public static String getMailTimeString(Date date) {
        return date == null ? "" : getSdf(MAIN_TIME_FORMAT).format(date);
    }

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    /**
     * 获取一段时间段内的所有具体时间
     *
     * @param dBegin
     * @param dEnd
     * @return
     * @author xiao
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> dates = new ArrayList<Date>();
        dates.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间    
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间    
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后    
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量    
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(calBegin.getTime());
        }
        return dates;
    }


    /**
     * 获取一段时间段内的所有具体时间<String>
     *
     * @param dBegin
     * @param dEnd
     * @return
     * @author xiao
     */
    public static List<String> findDatesAsString(Date dBegin, Date dEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = new ArrayList<String>();
        dates.add(sdf.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间    
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间    
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后    
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量    
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(sdf.format(calBegin.getTime()));
        }
        return dates;
    }

    /***
     * n分钟前时间
     * @return
     */
    public static String beforeHalfHour(Date date, int n, String formate) {
        if (null == formate) {
            formate = "yyyy-MM-dd HH:mm:ss";
        }
        if (date != null) {
            Long time = date.getTime();
            time -= n * 60 * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat(formate);
            return sdf.format(time);
        }
        return "";
    }

    /***
     * n天前时间
     * @return
     */
    public static String beforeNDay(Date date, int n, String formate) {
        if (null == formate) {
            formate = "yyyy-MM-dd HH:mm:ss";
        }
        if (date != null) {
            Long time = date.getTime();
            time -= ((long) n) * 60 * 1000 * 24 * 60;
            SimpleDateFormat sdf = new SimpleDateFormat(formate);
            return sdf.format(time);
        }
        return "";
    }

    /***
     * n天前时间
     * @return
     */
    public static Date beforeNDayDate(Date date, int n) {
        if (date != null) {
            Long time = date.getTime();
            time -= n * 60 * 1000 * 24 * 60;
            return new Date(time);
        }
        return new Date();
    }

    /***
     * n分钟前时间
     * @return
     */
    public static Date beforeNminDate(Date date, int n) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, -n);
            return cal.getTime();
        } catch (Exception e) {
            return date; // 如果无法转化，则返回传入的时间。
        }
    }

    /***
     * n分钟前时间
     * @return
     */
    public static Date _beforeHalfHour(Date date, int n, String formate) {
        if (null == formate) {
            formate = "yyyy-MM-dd HH:mm:ss";
        }
        if (date != null) {
            Long time = date.getTime();
            time -= n * 60 * 1000;
            Date _time = new Date(time);
            return _time;
        }
        return null;
    }

    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /****
     * 两个时间之间的时间段
     * @param time1
     * @param time2
     * @param formate
     * @return
     */
//	public String[] getBetweenTime(String time1,String time2,String formate){
//		if(null==formate){
//			formate = "yyyy-MM-dd";
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat(formate);
//		return null;
//	}

    /***
     * 两个时间之间的分钟数
     * @param time1
     * @param time2
     * @param formate
     * @return
     */
    public static double getMinuteBetweenTime(String time1, String time2, String formate) {
        if (null == formate) {
            formate = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        Double result = 0.0;
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            result = (double) ((date2.getTime() - date1.getTime()) / 1000 / 60);
            if (result < 0) {
                result = (double) (((date2.getTime() + 24 * 60 * 60 * 1000) - date1.getTime()) / 1000 / 60);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /***
     * 计算两个时间段之间的日期数组
     * @param start_time
     * @param end_time
     * @return
     * @throws ParseException
     */
    public static List<String> findDates(String start_time, String end_time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<String> result = new ArrayList<String>();
        try {
            Date date1 = df.parse(start_time);
            Date date2 = df.parse(end_time);
            double s = (double) ((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
            for (long i = 0; i <= s; i++) {
                long todayDate = date1.getTime() + i * 24 * 60 * 60 * 1000;
                Date tmDate = new Date(todayDate);
                result.add(new SimpleDateFormat("yyyy-MM-dd").format(tmDate));
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static List<Date> dateTimeArr() {
        List<Date> result = new ArrayList<Date>();
        try {
            String date = "2015-8-20 00:30:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date _date = sdf.parse(date);//毫秒

            Long jDate = new Date().getTime() - _date.getTime();
            int n = (int) (jDate / 1000 / 1800);
            result.add(_date);
            for (int i = 0; i < n; i++) {
                Date __date = _beforeHalfHour(_date, -30, null);
                _date = __date;
                result.add(__date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static String getBeforeYearMonth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        if (0 == month) {
            year = year - 1;
            month = 12;
        }
        String monthStr = month + "";
        if (month < 10) {
            monthStr = "0" + month;
        }
        return year + "-" + monthStr;
    }


    /***
     * n天后的日期
     * @return
     */
    public static String afterNDay(String date, int n, String formate) {
        if (null == formate) {
            formate = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        try {
            Date date1 = sdf.parse(date);

            if (date != null) {
                Long time = date1.getTime();
                time += ((long) n) * 60 * 1000 * 24 * 60;
                return sdf.format(time);
            }
        } catch (ParseException e) {
            return "";
        }
        return "";
    }

    /**
     * M1.0 判断两个时间相差多少天
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 某日期距离现在多少年
     *
     * @param
     * @return
     * @throws Exception
     */
    public static int yearsBetween(Date date) throws Exception {
        return yearsBetween(date, new Date());
    }

    /**
     * 日期相关年数
     *
     * @param before
     * @param next
     * @return
     * @throws Exception
     */
    public static int yearsBetween(Date before, Date next) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(next);

        if (cal.before(before)) {
            throw new IllegalArgumentException("The beforeDate is before nextDate.It's unbelievable!");
        }
        int yearNext = cal.get(Calendar.YEAR);
        int monthNext = cal.get(Calendar.MONTH);
        int dayOfMonthNext = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(before);

        int yearBefore = cal.get(Calendar.YEAR);
        int monthBefore = cal.get(Calendar.MONTH);
        int dayOfMonthBefore = cal.get(Calendar.DAY_OF_MONTH);

        int years = yearNext - yearBefore;

        if (monthNext <= monthBefore) {
            if (monthNext == monthBefore) {
                if (dayOfMonthNext < dayOfMonthBefore) {
                    years--;
                }
            } else {
                years--;
            }
        }
        return years;
    }


    /***
     * n个月之后的日期   2017-11-01  3个月之后  2017-11-01
     * @return
     */
    public static String afterNMonth(String date, int n, String formate) {
        if (null == formate) {
            formate = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        try {
            Date now = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, n);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取两个时间之间的相差的时间
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 获取两个时间之间的相差的时间
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static long getMinutePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少分钟
        long min = diff / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return min;
    }

    public static long getSecondPoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        long sec = diff/1000;
        return sec;
    }

    /**
     * 得到N(N可以为负数)分钟后的日期
     */
    public static Date afterNMinutesDate(Date theDate, int minute) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(theDate);
            cal.add(Calendar.MINUTE, minute);
            return cal.getTime();
        } catch (Exception e) {
            return getCurrentDate(); // 如果无法转化，则返回默认格式的时间。
        }
    }

    /**
     * @param time
     * @param formate
     * @return
     */
    public static Date strToDateLong(String time,String formate) {
        Date utilDate = null;
        try {
            //yyyy-mm-dd, 会出现时间不对, 因为小写的mm是代表: 秒
            SimpleDateFormat sdf = new SimpleDateFormat(formate);
            utilDate = sdf.parse(time);
            return utilDate;
        } catch (ParseException e) {
            return utilDate;
        }
    }


    public static Date getDate(String dateStr, String dateFormat) {
        DateFormat df=new SimpleDateFormat(dateFormat);
        Date d = null;
        try {
            d = df.parse(dateStr);
        } catch (ParseException e) {

        }
        return d;
    }
    /**
     * 返回当前日期 Date
     */
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * Adds a number of hours to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    /**
     * Adds to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date          the date, not null
     * @param calendarField the calendar field to add to
     * @param amount        the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    private static Date add(final Date date, final int calendarField, final int amount) {
        validateDateNotNull(date);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    private static void validateDateNotNull(final Date date) {
        Validate.isTrue(date != null, "The date must not be null");
    }

    public static String formatDateChinese(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(LOCAL_FORMAT);
        return sdf.format(date);
    }

    public static String getStringDate(Long longTime) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date(longTime);
        return sdf.format(date);
    }

    public static boolean isStartOfDay(Date date){
        String dateStr = TIME_SIMPLE_FORMAT.format(date);

        return StringUtils.equalsIgnoreCase(dateStr, START_OF_DAY.trim());
    }

    public static boolean isEndOfDay(Date date){
        String dateStr = TIME_SIMPLE_FORMAT.format(date);

        return StringUtils.equalsIgnoreCase(dateStr, END_OF_DAY.trim());
    }


    public static void main(String[] args) {
/*        Date date = new Date();
        Date date2 = org.apache.commons.lang3.time.DateUtils.addDays(date, 1);
        long datePoor = DateUtil.getMinutePoor(date2, date);
        Date date1 = DateUtil.afterNMinutesDate(date, Integer.valueOf(String.valueOf(datePoor)));*/
        strToDateLong("2019-12-12 11:11:11","yyyy-MM-dd HH:mm:ss");
        Date date= afterNMinutesDate(strToDateLong("2019-12-12 11:11:11","yyyy-MM-dd HH:mm:ss"),30);

       List list= findDatesAsString(date,new Date());
       for(int i=0;i<list.size();i++){
           System.out.println(list.get(i));
       }
        System.out.println(getTimeString(date));
    }
}
