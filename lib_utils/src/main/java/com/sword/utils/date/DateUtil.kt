package com.sword.utils.date

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


/**
 * @author: zph
 * @date: 2026/05/18
 * 描述: 主要功能格式化时间工具类
 */
object DateUtil {
    // 日期格式年份，例如：2022，2023
    const val FORMAT_YYYY = "yyyy"
    const val FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"

    private val DEFAULT_TIMEZONE: TimeZone = TimeZone.getDefault()
    private val formatterCache = ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>>()

    /**
     * 获取当前时间的字符串表示
     *
     * @param pattern 时间格式
     * @return 当前时间的字符串表示
     */
    fun getCurrentDate(pattern: String): String {
        return formatToStr(Date(), pattern)
    }

    /**
     * 将时间戳格式化为指定格式的字符串
     *
     * @param timestamp 时间戳
     * @param pattern   时间格式
     * @return 格式化后的时间字符串
     */
    fun formatToStr(timestamp: Long, pattern: String): String {
        return formatToStr(Date(timestamp), pattern)
    }

    /**
     * 将日期对象格式化为指定格式的字符串
     *
     * @param date    日期对象
     * @param pattern 时间格式
     * @return 格式化后的时间字符串
     */
    fun formatToStr(date: Date?, pattern: String): String {
        if (date == null) return ""
        val dateFormat = getDateFormat(pattern)
        return dateFormat.format(date)
    }

    /**
     * 获取指定格式的日期格式化对象
     *
     * @param pattern 时间格式
     * @return 日期格式化对象
     */
    @SuppressLint("SimpleDateFormat")
    private fun getDateFormat(pattern: String): DateFormat {
        val locale = Locale.getDefault()
        val cacheKey = "$pattern|${locale.toLanguageTag()}"
        val threadLocal = formatterCache.getOrPut(cacheKey) {
            ThreadLocal.withInitial {
                SimpleDateFormat(pattern, locale).apply {
                    timeZone = DEFAULT_TIMEZONE
                }
            }
        }
        return threadLocal.get()
    }

    /**
     * 格式化字符串时间为指定格式
     *
     * @param dateString 字符串时间
     * @param format     格式
     * @return 格式化后的时间字符串
     */
    @SuppressLint("SimpleDateFormat")
    fun formatStringDate(dateString: String?, format: String): String {
        if (dateString.isNullOrBlank()) return ""
        val inputFormat = SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS, Locale.getDefault())
        val outputFormat = SimpleDateFormat(format, Locale.getDefault())
        inputFormat.timeZone = DEFAULT_TIMEZONE
        outputFormat.timeZone = DEFAULT_TIMEZONE
        try {
            val date = inputFormat.parse(dateString)
            return if (date != null) outputFormat.format(date) else ""
        } catch (_: ParseException) {
            return ""
        }
    }

    val currentTime
        /**
         * 获取当前时间的日期对象
         *
         * @return 当前时间的日期对象
         */
        get() = Date()

    /**
     * 将日期对象格式化为指定格式的时间字符串
     *
     * @param date    日期对象
     * @param pattern 时间格式
     * @return 格式化后的时间字符串
     */
    @SuppressLint("SimpleDateFormat")
    fun formatTime(date: Date?, pattern: String): String {
        return formatToStr(date, pattern)
    }

    /**
     * 解析指定格式的时间字符串为日期对象
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 解析后的日期对象
     * @throws ParseException 解析异常
     */
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun parseTime(time: String?, pattern: String): Date? {
        if (time.isNullOrBlank()) {
            throw ParseException("time is null or blank", 0)
        }
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = DEFAULT_TIMEZONE
        return sdf.parse(time)
    }

    /**
     * 计算两个日期之间的时间差，返回指定时间单位的差值
     *
     * @param date1     第一个日期对象
     * @param date2     第二个日期对象
     * @param timeUnit  时间单位
     * @return 时间差的差值
     */
    fun getTimeDifference(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        val difference = date2.time - date1.time
        return timeUnit.convert(difference, TimeUnit.MILLISECONDS)
    }

    /**
     * 判断指定时间是否在给定时间区间内
     *
     * @param time      待判断的时间
     * @param startTime 时间区间的开始时间
     * @param endTime   时间区间的结束时间
     * @return 如果指定时间在时间区间内，返回 true；否则返回 false
     */
    fun isInTimeRange(time: Date, startTime: Date?, endTime: Date?): Boolean {
        if (startTime == null || endTime == null) return false
        val target = time.time
        return target >= startTime.time && target <= endTime.time
    }

    /**
     * 判断指定年份是否为闰年
     *
     * @param year 年份
     * @return 如果是闰年，返回 true；否则返回 false
     */
    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
    }

    /**
     * 获取指定日期对象的年份
     *
     * @param date 日期对象
     * @return 年份
     */
    fun getYearFromDate(date: Date): Int {
        return getCalendarFromDate(date).get(Calendar.YEAR)
    }

    /**
     * 获取指定日期对象的月份
     *
     * @param date 日期对象
     * @return 月份
     */
    fun getMonthFromDate(date: Date): Int {
        return getCalendarFromDate(date).get(Calendar.MONTH) + 1
    }

    /**
     * 获取指定日期对象的星期
     *
     * @param date 日期对象
     * @return 星期，1 表示星期一，2 表示星期二，依次类推
     */
    fun getWeekdayFromDate(date: Date): Int {
        return getCalendarFromDate(date).get(Calendar.DAY_OF_WEEK)
    }

    private fun getCalendarFromDate(date: Date): Calendar {
        return Calendar.getInstance().apply { time = date }
    }

    fun test(){

        // 获取当前时间
        val currentTime = currentTime

        // 格式化时间
        val formattedTime = formatTime(currentTime, FORMAT_YYYY_MM_DD_HH_MM_SS)
        println(formattedTime)

        // 解析时间
        val timeString = "2023-07-14 12:00:00"
        val parsedTime = parseTime(timeString, FORMAT_YYYY_MM_DD_HH_MM_SS)
        println(parsedTime)

        // 获取时间差
        val startTime = Date()
        val endTime = Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2))
        val timeDifference = getTimeDifference(startTime, endTime, TimeUnit.MINUTES)
        println(timeDifference)

        // 判断时间区间
        val time = Date()
        val startTime1 = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
        val endTime1 = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1))
        val isInTimeRange = isInTimeRange(time, startTime1, endTime1)
        println(isInTimeRange)

        // 判断闰年
        val year = 2023
        val isLeapYear = isLeapYear(year)
        println(isLeapYear)

        // 获取年份、月份、星期
        val yearFromDate = getYearFromDate(currentTime)
        val monthFromDate = getMonthFromDate(currentTime)
        val weekdayFromDate = getWeekdayFromDate(currentTime)
        println(yearFromDate)
        println(monthFromDate)
        println(weekdayFromDate)
    }
}
