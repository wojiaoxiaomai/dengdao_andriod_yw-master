package com.choucheng.dengdao2.tools;

import android.annotation.SuppressLint;
import android.content.Context;

import com.choucheng.dengdao2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class DateFormatUtils {

    /**
     * 将格林时间差转化为标准时间
     * @param date 时间差，如：\/Date(1369901656000)\/
     * @param differTime 相差小时数目
     * @return 标准时间 如：2013-05-30 16:14:16
     */
    public static String GreenwishTime2LocalTime(String date,int differTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = date.substring(date.indexOf("(") + 1, date.indexOf("+"));
        String time = sdf.format(new Date(Long.parseLong(date)));

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.HOUR, differTime);
        } catch (ParseException e) {
            Logger.showLogfoException(e);
        }
        return sdf.format(calendar.getTime());
    }

    public static String GreenwishTime2LocalTimeByFormat(String date,int differTime,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        date = date.substring(date.indexOf("(") + 1, date.indexOf("+"));
        String time = sdf.format(new Date(Long.parseLong(date)));

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.HOUR, differTime);
        } catch (ParseException e) {
            Logger.showLogfoException(e);
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 标准时间与格林时间毫秒数之差
     * @return 距格林时间毫秒数，如：\/Date(1369901656000)\/
     */
    public static String LocalTime2GreenwishTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startDateString = sdf.format(new Date());
        String GreenwichMeanDateString = "1970-1-1 00:00:00";

        Date currentDate = null;
        Date GreenwichMeanDate = null;

        try {
            currentDate = sdf.parse(startDateString);
            GreenwichMeanDate = sdf.parse(GreenwichMeanDateString);
        } catch (ParseException e) {
            Logger.showLogfoException(e);
        }
        Calendar c = Calendar.getInstance();

        c.setTime(currentDate);
        long ls = c.getTimeInMillis();

        c.setTime(GreenwichMeanDate);
        long le = c.getTimeInMillis();

        long timeDifference = ls - le;
        return "/Date(" + timeDifference +")/";
    }

    /**
     * 获取系统时间
     * @return 2013-05-30 16:14:16
     */
    public static String  GetLocalTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateString = sdf.format(new Date());
        Calendar calendar  =  Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(currentDateString));
        } catch (ParseException e) {
            Logger.showLogfoException(e);
        }
        calendar.add(Calendar.HOUR,8);
        currentDateString = sdf.format(calendar.getTime());
        return currentDateString;
    }

    /**
     * 获取系统时间
     * @return 返回当前Date格式的时间
     * @time 2014.1.3
     */

    public static Date getLocalTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        //  calendar.add(calendar.HOUR,8);
        return calendar.getTime();
    }


    /**
     * 根据秒钟数得到日期
     * @param mills  需要转换的秒数
     * @return
     */
    public static Date getTime(long mills){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(mills*1000L);
        //  calendar.add(calendar.HOUR,8);
        return calendar.getTime();
    }


    /**
     * 根据日期返回星期
     * @return 2014.1.20
     */
    public static String getWeekofDate(Context context ,Date date){
        String[] weekdays=context.getResources().getStringArray(R.array.weekDays);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int intWeek=calendar.get(Calendar.DAY_OF_WEEK)-1;
        return weekdays[intWeek];

    }

    /**
     * 根据日期返回星期
     * @return 2014.1.20
     */
    public static int getWeekofDate(Date date){
        //String[] weekdays=context.getResources().getStringArray(R.array.weekDays);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int intWeek=calendar.get(Calendar.DAY_OF_WEEK)-1;  //从周天开始，0为周天
        return intWeek;

    }

    /**
     * 获得当前时间的秒数
     * @return
     */
    public static long getLocalTime_Second (Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        long times=calendar.getTimeInMillis()/1000L;
        return times;
    }
    /**
     *
     * @retur 时间（strTime）的豪秒数
     */
    public static Long getDateToTimestamp(String strTime, java.text.DateFormat sdfOut) {
        Long timestamp = 0l;
        try {
            // Date date = sdfOut.parse(strTime);
            timestamp = sdfOut.parse(strTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }


    /**
     * 获取日期中的天，也就是号数
     * @param date
     * @return
     */
    public static String getDayofMonth(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }



    /*
     * 获取两个时间的时间差
     * time1:老时间
     * time2:现在时间
     * */
    public static String getTimeDifference(Context context,Date date,Date now){
        long l=now.getTime()-date.getTime();
        long day=l/(24*60*60*1000);
        {//==>2014.1.8
            if(day != 0){
                return day+context.getString(R.string.day);
            }
            long hour=(l/(60*60*1000)-day*24);
            if(hour != 0){
                return hour+context.getString(R.string.hour);
            }
            long min=((l/(60*1000))-day*24*60-hour*60);
            if(min != 0){
                return min+context.getString(R.string.minute);
            }
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            return s+context.getString(R.string.second);
        }
    }

    /**
     * 将日期转换成yyyy/MM/dd格式的形式
     * @return 2014.3.10
     */
    public static String getCommFormatDate(Context context ,Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.dateformat31));
        String currentDateString="";
        if(date!=null){
            currentDateString = sdf.format(date);
        }
        return currentDateString;
    }

    /***
     * 将字符显示的时间转换成date
     * @param showtimestring
     * @param timeformat
     * @return
     */
    public static Date getCommFormatDate2(String showtimestring,String timeformat){
        SimpleDateFormat sdf2=new SimpleDateFormat(timeformat);
        Date date = null;
        if(showtimestring==null||showtimestring.equals("")) return  null;
        try {
            date = sdf2.parse(showtimestring);

        } catch (ParseException e) {
            Logger.showLogfoException(e);
        }

        return date;
    }


    /**
     * 将日期格式化成yyyy年MM月dd日
     * @param date
     * @return
     */
    public static String formatDate(Date date,String dateformat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        String temp = sdf.format(date);
        return temp;
    }

    /**
     *
     * @param context
     * @param string
     * @return
     */
    public static String formatDate(Context context,String string) {
        String date = "";
        long time = Long.parseLong(string);
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.dateformat1));
        date = sdf.format(time);
        return date;

    }

    /**
     * 将字符串转换成提供的日期格式
     * @param context
     * @param olddateformat 原字符串的日期格式
     * @param dataformat 需要转换的格式字符串
     * @return
     */
    public static String formatDate(Context context,String s,String olddateformat,String dataformat) {
        SimpleDateFormat sdf2=new SimpleDateFormat(olddateformat);
        Date date;
        String currentDateString="";
        if(s==null||s.equals("")) return  "";
        try {
            date = sdf2.parse(s);
            SimpleDateFormat sdf = new SimpleDateFormat(dataformat);
            currentDateString = sdf.format(date);
        } catch (ParseException e) {
            Logger.showLogfoException(e);
        }

        return currentDateString;
    }

    /**
     * 将日期转换成需要的格式
     * @param date 需要转换的日期
     * @param dataformat 需要转换的格式
     * @return
     */
    public static String formatDate2(Date date,String dataformat) {
        SimpleDateFormat sdf2 = new SimpleDateFormat(dataformat);
        String datestring = sdf2.format(date);
        return datestring;

    }


    /**
     * 将时间转换成需要的格式的字符串
     * @param time  时间的秒数
     * @param timeformat
     */
    public static String getDateForSeconds(long time, String timeformat){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time*1000L);
        String timestring=formatDate(calendar.getTime(),timeformat);
        return timestring;
    }

    /**
     * 将时间转换成需要的格式
     * @param time  时间的秒数
     * @param sdf  SimpleDateFormat
     */
    public static String getDateForSeconds(long time,SimpleDateFormat sdf ){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time*1000L);
        String temp = sdf.format(calendar.getTime());
        return temp;
    }


    /**
     * @param dateString
     * @param dateFormat
     * @return 时间（dateString）对象
     */
    public static Date getDate(String dateString, String dateFormat) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * @param dateString
     * @param dateFormat1
     * @param dateFormat2
     * @return 时间（dateString）转换为dateFormat2的格式
     */
    public static String getDate(String dateString, String dateFormat1,
                                 String dateFormat2) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
        SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);
        String strDate = "";
        try {
            Date date = sdf1.parse(dateString);
            strDate = sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * @param dateString
     * @param sdf1
     * @param sdf2
     * @return 时间（dateString）sdf2
     */
    public static String getDate(String dateString, java.text.DateFormat sdf1,
                                 java.text.DateFormat sdf2) {
        String strDate = "";
        try {
            Date date = sdf1.parse(dateString);
            strDate = sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }
    /**
     * @param sdfOut
     * @return 当前月开始时间
     */
    public static String getBeginAt_ofMonth(java.text.DateFormat sdfOut){
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return sdfOut.format(calendar.getTime());
    }

    /**
     * @param sdfOut
     * @return 当前月结束时间
     */
    public static String getEndAt_ofMonth(java.text.DateFormat sdfOut){
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return sdfOut.format(calendar.getTime());
    }
    /**
     * @param sdfOut
     * @return 当前星期开始时间
     */
    public static String getBeginAt_ofWeek(java.text.DateFormat sdfOut){
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sdfOut.format(calendar.getTime());
    }

    /**
     * @param sdfOut
     * @return 当前星期结束时间
     */
    public static String getEndAt_ofWeek (java.text.DateFormat sdfOut){
        Calendar calendar =Calendar.getInstance();
//        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        return sdfOut.format(calendar.getTime());
    }
    /**
     * @param sdfOut
     * @return 当天开始时间
     */
    public static String getBeginAt_ofDay(java.text.DateFormat sdfOut){
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return sdfOut.format(calendar.getTime());
    }

    /**
     * @param sdfOut
     * @return 当天结束时间
     */
    public static String getEndAt_ofDay (java.text.DateFormat sdfOut){
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
        Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return sdfOut.format(calendar.getTime());
    }
    /**
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param milliSecond
     * @return 获取当天指定时分秒的时间
     */
    public static String getDate(int day, int hour, int minute, int second, int milliSecond,java.text.DateFormat outDateFormat) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + day);
        cal.set(Calendar.HOUR_OF_DAY, hour);

        cal.set(Calendar.SECOND, second);

        cal.set(Calendar.MINUTE, minute);

        cal.set(Calendar.MILLISECOND, milliSecond);

        Date date = new Date(cal.getTimeInMillis());
        return outDateFormat.format(date);

    }

    /**
     * @param hour
     * @param minute
     * @param second
     * @param milliSecond
     * @param outDateFormat
     * @return获取当前星期五指定时分秒的时间
     */
    public static String getDate_of_Friday(int hour, int minute, int second, int milliSecond,java.text.DateFormat outDateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.MILLISECOND, milliSecond);
        Date date = new Date(cal.getTimeInMillis());
        return outDateFormat.format(date);

    }


    /**
     * @param strDate1
     * @param strDate2
     * @param strDateForma1
     * @param strDateForma2
     * @param verifyDate
     * @return 是否在时间之类
     */
    public static Boolean isAmong(String strDate1, String strDate2, java.text.DateFormat strDateForma1, java.text.DateFormat strDateForma2,String verifyDate, java.text.DateFormat strDateForma3) {
        Boolean isAmong=false;
        Long lDate1=getDateToTimestamp(strDate1,strDateForma1);
        Long lDate2=getDateToTimestamp(strDate2,strDateForma2);
        Long lDate3=getDateToTimestamp(verifyDate,strDateForma3);
        if(lDate3>=lDate1&&lDate3<=lDate2){
            isAmong=true;
        }
        return isAmong;

    }

    /**
     * 比较时间
     * @param strDate1
     * @param strDate2
     * @param strDateForma1
     * @param strDateForma2
     * @return  -1：strDate2在strDate1之后，1：strDate2在strDate1之前
     */
    public static int compare_date(String strDate1, String strDate2, String strDateForma1, String strDateForma2) {
        java.text.DateFormat df1 = new SimpleDateFormat(strDateForma1);
        java.text.DateFormat df2 = new SimpleDateFormat(strDateForma2);
        try {
            Date d1 = df1.parse(strDate1);
            Date d2 = df2.parse(strDate2);

            if (d1.getTime() > d2.getTime()) {

                return 1;
            } else if (d1.getTime() < d2.getTime()) {

                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;

    }


    /**
     * 将传入时间与当前时间进行对比，是否今天\N天前
     * @param context
     * @param strDate
     * @param dateFormat
     * @param toDateFormat
     * @return
     */
    public static String getDayNumber(Context context,String strDate,  java.text.DateFormat dateFormat, java.text.DateFormat toDateFormat) {
        String dayNumber = null;
        Calendar dateCalendar = Calendar.getInstance();
        Date date1 = null;
        try {
            date1 = dateFormat.parse(strDate);
            dateCalendar.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date now = new Date();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(now);
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        if (dateCalendar.after(targetCalendar)) {

            dayNumber = getDate(strDate, dateFormat, toDateFormat);
        } else {
            Date toDay = targetCalendar.getTime();
//            Log.d(tag, "dateFormat.format(toDay):" + dateFormat.format(toDay));
            long lDay_msec = toDay.getTime() - date1.getTime();
            double lDays = lDay_msec / (double) (1000 * 60 * 60 * 24);
            int iDays = (int) Math.ceil(lDays);
            switch (iDays) {
                case 1:
                    dayNumber = context.getString(R.string.app_1day);
                    break;
                case 2:
                    dayNumber = context.getString(R.string.app_2day);
                    break;
                default:
                    dayNumber = iDays +context.getString(R.string.app_nday);
                    break;
            }

        }
        return dayNumber;
    }

    /**
     * 将传入时间与当前时间进行对比，是否今天\N天前，N小时前，N分钟前
     * @param strDate
     * @param dateFormat
     * @return
     */
    public static String getDayHHNumber(Context context,String strDate, java.text.DateFormat dateFormat) {
        String dayNumber = null;
        Calendar dateCalendar = Calendar.getInstance();
        Date date1 = null;
        try {
            date1 = dateFormat.parse(strDate);
            dateCalendar.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date now = new Date();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(now);
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        if (dateCalendar.after(targetCalendar)) {
            long lDay_msec = now.getTime() - date1.getTime();
            int lHHs = (int) (lDay_msec / (1000 * 60 * 60));
            if (lHHs == 0) {
                int lmms = (int) (lDay_msec / (1000 * 60));
                dayNumber = lmms + context.getString(R.string.app_nminute);

            } else {
                dayNumber = lHHs + context.getString(R.string.app_nhour);
            }

        } else {
            Date toDay = targetCalendar.getTime();
//            Log.d(tag, "dateFormat.format(toDay):" + dateFormat.format(toDay));
            long lDay_msec = toDay.getTime() - date1.getTime();
            double lDays = lDay_msec / (double) (1000 * 60 * 60 * 24);
            int iDays = (int) Math.ceil(lDays);
            switch (iDays) {
                case 1:
                    dayNumber = context.getString(R.string.app_1day);
                    break;
                case 2:
                    dayNumber = context.getString(R.string.app_2day);
                    break;
                default:
                    dayNumber = iDays + context.getString(R.string.app_nday);
                    break;
            }

        }
        return dayNumber;
    }

    /**
     * @param strDate
     * @return 是否今天
     */
    public static Boolean istoDay(String strDate) {
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar dateCalendar = Calendar.getInstance();
        Date date1;
        try {
            date1 = dateFormat.parse(strDate);
            dateCalendar.setTime(date1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Date now = new Date();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(now);
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        if (dateCalendar.after(targetCalendar)) {
            return true;
        } else {
            return false;
        }
    }
}
