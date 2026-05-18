package com.sword.utils.date

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

/**
 * 时间工具类
 */
object DateUtils {
    private const val TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    private const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val DATE_SIMPLE_FORMAT = "yyyyMMdd"

    private fun formatter(pattern: String): SimpleDateFormat =
        SimpleDateFormat(pattern, Locale.getDefault())

    private fun Calendar.clearToDayStart() {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    //====================================时间转换=======================================>>
    /**
     * 时长转换
     *
     * @param time 通话时长（秒）
     * @return 几小时几分钟几秒
     */
    fun getStrTime(time: Int?): String {
        if (time == null || time == 0) return ""
        val str = StringBuilder()
        val hour = time / 3600
        if (hour != 0) str.append(hour).append("小时")
        val minutes = (time - hour * 3600) / 60
        if (minutes != 0) str.append(minutes).append("分钟")
        val seconds = time - hour * 3600 - minutes * 60
        if (seconds != 0) str.append(seconds).append("秒")
        return str.toString()
    }

    fun millisToStr(time: Long): String {
        if (time <= 0) return "0 秒"
        if (time < 1000) {
            return (time / 1000.0f).toString() + "秒"
        }
        return getStrTime(time.toInt() / 1000)
    }

    fun millisDiff(time: Long): String {
        return millisToStr(System.currentTimeMillis() - time)
    }

    /**
     * 日期转换为字符串
     *
     * @param date   日期
     * @param format 日期格式
     */
    /**
     * 日期转换为字符串 默认"yyyy-MM-dd HH:mm:ss"
     *
     * @param date 日期
     */
    @JvmOverloads
    fun dateToStr(date: Date?, format: String? = null): String? {
        var format = format
        if (date == null) return null
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "" == format || "Datetime" == format) {
            format = DATETIME_FORMAT
        } else if ("Timestamp" == format) {
            format = TIMESTAMP_FORMAT
        } else if ("Date" == format) {
            format = DATE_FORMAT
        } else if ("DateSimple" == format) {
            format = DATE_SIMPLE_FORMAT
        } else if ("Simple" == format) {
            format = DATE_SIMPLE_FORMAT
        }
        val sdf = formatter(format)
        return sdf.format(date)
    }

    /**
     * 任意类型日期字符串转时间
     */
    fun strToDate(time: String): Date? {
        var time = time
        if (isBlank(time)) return null
        var formatter: SimpleDateFormat?
        val tempPos = time.indexOf("AD")
        time = time.trim { it <= ' ' }
        formatter = formatter("yyyy.MM.dd G 'at' hh:mm:ss z")
        if (tempPos > -1) {
            time = time.substring(0, tempPos) +
                    "公元" + time.substring(tempPos + "AD".length) //china
            formatter = formatter("yyyy.MM.dd G 'at' hh:mm:ss z")
        }
        if (time.contains(".")) time = time.replace("\\.".toRegex(), "/")
        if (time.contains("-")) time = time.replace("-".toRegex(), "/")
        if (!time.contains("/") && !time.contains(" ")) {
            formatter = formatter("yyyyMMddHHmmss")
        } else if (time.contains("/")) {
            if (time.contains("am") || time.contains("pm")) formatter =
                formatter("yyyy/MM/dd KK:mm:ss a")
            else if (time.contains(" ")) formatter = formatter("yyyy/MM/dd HH:mm:ss")
            else {
                val dateArr: Array<String?> =
                    time.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (dateArr.size == 2 || (dateArr.size == 3 && isBlank(dateArr[2]))) formatter =
                    formatter("yyyy/MM")
                else formatter = formatter("yyyy/MM/dd")
            }
        }
        val pos = ParsePosition(0)
        return formatter.parse(time, pos)
    }

    fun strToDate(time: String, formatterString: String): Date? {
        if (isBlank(time) || isBlank(formatterString)) return null
        val formatter = formatter(formatterString)
        val pos = ParsePosition(0)
        return formatter.parse(time, pos)
    }

    fun strToDateThrow(time: String): Date? {
        if (isBlank(time)) return null
        val value = strToDate(time)
//        Assert.notNull(value, "无法识别日期格式: " + value)
        return value
    }

    //====================================时间获取=======================================>>
    val dateStr: String
        /**
         * 获取当前对应格式的日期
         */
        get() = getDateStr(null)

    /**
     * 获取当前对应格式的日期 默认"yyyyMMddHHmmssSSS"
     *
     * @param format 日期格式
     */
    fun getDateStr(format: String?): String {
        var format = format
        if (null == format || "" == format) {
            format = "yyyyMMddHHmmssSSS"
        }
        val date = Date()
        val df = formatter(format)
        return df.format(date)
    }


    /**
     * 获取星期几，java中一周中的数字 转 常规一周中的数字
     * java中 周日=1 周六=7
     * eg:
     * 周一 -> 1
     * 周日 -> 7
     *
     * @param dayOfWeek java中一周中的数字
     */
    fun getIsoDayOfWeek(dayOfWeek: Int): Int {
        val result: Int
        if (dayOfWeek == 1) {
            result = 7
        } else {
            result = dayOfWeek - 1
        }
        return result
    }


    /**
     * 根据日期获取星期
     */
    fun getWeekOfDate(date: Date): String {
        val weekDays = arrayOf<String>("sun", "mon", "tue", "wed", "thu", "fri", "sat")
        val cal = Calendar.getInstance()
        cal.setTime(date)
        var w = cal.get(Calendar.DAY_OF_WEEK) - 1
        if (w < 0) w = 0
        return weekDays[w]
    }

    //获取一个月的开始和结束时间
    fun getMonthFirstAndEndDay(date: Date): MutableList<Date?> {
        val result: MutableList<Date?> = ArrayList<Date?>()
        val c = Calendar.getInstance()
        c.setTime(date)
        c.clearToDayStart()

        c.set(Calendar.DAY_OF_MONTH, 1) //设置为1号
        result.add(c.getTime())

        c.add(Calendar.MONTH, 1) //加一个月
        c.add(Calendar.MILLISECOND, -1) //减一毫秒
        result.add(c.getTime())

        return result
    }

    //获取一年的开始和结束时间
    fun getYearFirstAndEndDay(date: Date): MutableList<Date?> {
        val result: MutableList<Date?> = ArrayList<Date?>()
        val c = Calendar.getInstance()
        c.setTime(date)
        c.set(Calendar.MONTH, 0)
        c.set(Calendar.DATE, 1)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        result.add(c.getTime())

        c.add(Calendar.YEAR, 1) //加一年
        c.add(Calendar.MILLISECOND, -1) //减一毫秒
        result.add(c.getTime())

        return result
    }

    fun getYearStartTime(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.YEAR, 0)
        calendar.add(Calendar.DATE, 0)
        calendar.add(Calendar.MONTH, 0)
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.getTime()
    }

    /**
     * 获取季度
     */
    fun getQuarter(date: Date): Int {
        var quarter = 0
        val c = Calendar.getInstance()
        c.setTime(date)
        val month = c.get(Calendar.MONTH)
        when (month) {
            Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH -> quarter = 1
            Calendar.APRIL, Calendar.MAY, Calendar.JUNE -> quarter = 2
            Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER -> quarter = 3
            Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER -> quarter = 4
        }
        return quarter
    }

    /**
     * 获取某月 所有日期（yyyy-mm-dd格式字符串）
     */
    fun getMonthFullDay(date: Date): MutableList<String?> {
        val dateFormatYYYYMMDD = formatter(DATE_FORMAT)
        val fullDayList: MutableList<String?> = ArrayList<String?>()
        // 获得当前日期对象
        val c = Calendar.getInstance()
        c.setTime(date)
        c.clearToDayStart()
        // 当月1号
        c.set(Calendar.DAY_OF_MONTH, 1)
        val count = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (j in 1..count) {
            fullDayList.add(dateFormatYYYYMMDD.format(c.getTime()))
            c.add(Calendar.DAY_OF_MONTH, 1)
        }
        return fullDayList
    }


    /**
     * 获取一周的开始和结束时间
     */
    fun dateToWeekStartAndEnd(date: Date): MutableList<Date?> {
        val result: MutableList<Date?> = ArrayList<Date?>()
        val c = Calendar.getInstance()
        c.setTime(date)
        c.clearToDayStart()

        c.set(Calendar.DAY_OF_WEEK, 2) //设置为星期1
        result.add(c.getTime())

        c.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1) //加一个周
        c.add(Calendar.MILLISECOND, -1) //减一毫秒
        result.add(c.getTime())

        return result
    }

    /**
     * 获取某周 所有日期（yyyy-mm-dd格式字符串）
     */
    fun getWeekFullDay(date: Date): MutableList<String?> {
        val dateFormatYYYYMMDD = formatter(DATE_FORMAT)
        val fullDayList: MutableList<String?> = ArrayList<String?>()
        // 获得当前日期对象
        val c = Calendar.getInstance()
        c.setTime(date)
        c.clearToDayStart()
        //周一
        c.set(Calendar.DAY_OF_WEEK, 2)
        val count = c.getActualMaximum(Calendar.DAY_OF_WEEK)
        for (j in 1..count) {
            fullDayList.add(dateFormatYYYYMMDD.format(c.getTime()))
            c.add(Calendar.DAY_OF_WEEK, 1)
        }
        return fullDayList
    }

    /**
     * 获取当前date的上周的周一
     */
    fun getLastWeekMonday(date: Date): Date {
        val monday = getWeekMonday(date)
        val lastDate = getLastSec(monday)
        return getWeekMonday(lastDate)
    }

    /**
     * 获取当前date的所在周的周一
     */
    fun getWeekMonday(date: Date?): Date {
        val calendar: Calendar = GregorianCalendar()
        if (date != null) {
            calendar.setTime(date)
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.clearToDayStart()
        return calendar.getTime()
    }

    /**
     * 获取一天的开始时间和结束时间
     */
    fun dateToDayStartAndEnd(date: Date): MutableList<Date?> {
        val result: MutableList<Date?> = ArrayList<Date?>()
        val c = Calendar.getInstance()
        c.setTime(date)
        //将时分秒,毫秒域清零
        c.clearToDayStart()
        result.add(c.getTime())

        c.add(Calendar.DAY_OF_YEAR, 1)
        c.add(Calendar.SECOND, -1)
        result.add(c.getTime())
        return result
    }

    val today: MutableList<Date?>
        /**
         * 获取今天一天的开始时间和结束时间
         */
        get() = dateToDayStartAndEnd(Date())

    /**
     * 获取 时间段内 所有日期（yyyy-mm-dd格式字符串）
     */
    fun getFullDay(startTime: Date, endTime: Date): MutableList<String?> {
        val dateFormatYYYYMMDD = formatter(DATE_FORMAT)
        val fullDayList: MutableList<String?> = ArrayList<String?>()
        // 获得当前日期对象
        val s = Calendar.getInstance()
        s.setTime(startTime)
        s.clearToDayStart()
        val e = Calendar.getInstance()
        e.setTime(endTime)
        e.clearToDayStart()

        val daysBetween = getDaysBetween(s.getTime(), e.getTime())
        for (i in 0..daysBetween) {
            fullDayList.add(dateFormatYYYYMMDD.format(s.getTime()))
            s.add(Calendar.DAY_OF_MONTH, 1)
        }
        return fullDayList
    }

    //====================================时间计算=======================================>>
    /**
     * 计算两个日期之间相差的秒数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差秒数
     */
    fun getSecsBetween(smdate: Date, bdate: Date): Int {
        return ((bdate.time - smdate.time) / 1000).toInt()
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    fun getDaysBetween(smdate: Date, bdate: Date): Int {
        val second = getSecsBetween(smdate, bdate)
        val between_days = (second / 3600 / 24).toLong()

        return between_days.toString().toInt()
    }

    /**
     * 根据日期判断是上午下午还是晚上
     *
     * @return 1:上午 2:下午 3:晚上
     */
    fun getTimeByDate(date: Date?): Int? {
        if (date == null) return null
        val c = Calendar.getInstance()
        c.setTime(date)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        if (hour <= 12) return 1
        else if (hour <= 18) return 2
        else return 3
    }


    /**
     * 该天是否为月末最后一天
     */
    fun isLastDayOfMonth(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1))
        return calendar.get(Calendar.DAY_OF_MONTH) == 1
    }

    /**
     * 获取该月有多少天
     */
    fun getDaysOfMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }


    /**
     * 计算得到2个时间的差值，并转成中文
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    fun getDateDiff(startDate: Date, endDate: Date): String {
        val nd = (1000 * 24 * 60 * 60).toLong()
        val nh = (1000 * 60 * 60).toLong()
        val nm = (1000 * 60).toLong()
        val ns: Long = 1000
        //long ns = 1000;
        //获得两个时间的毫秒时间差异
        val diff = endDate.getTime() - startDate.getTime()
        //计算差多少天
        val day = diff / nd
        //计算差多少小时
        val hour = diff % nd / nh
        //计算差多少分钟
        val min = diff % nd % nh / nm
        //计算差多少秒//输出结果
        val sec = diff % nd % nh % nm / ns
        var result = ""
        if (day > 0) result += day.toString() + "天"
        if (hour > 0) result += hour.toString() + "小时"
        if (min > 0) result += min.toString() + "分钟"
        if (result.isEmpty()) result = sec.toString() + "秒"
        return result
    }

    /**
     * 计算开始时间和结束时间的差值，转成多少小时
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return eg: 3.21 (小时)
     */
    fun getDateDiffHour(startDate: Date, endDate: Date): BigDecimal? {
        val nh = BigDecimal(1000 * 60 * 60)
        val diff = BigDecimal(endDate.getTime() - startDate.getTime())
        return diff.divide(nh, 2, RoundingMode.HALF_UP)
    }

    /**
     * 计算开始时间和结束时间的差值，转成多少天
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return eg: 3.21 (天)
     */
    fun getDateDiffDay(startDate: Date, endDate: Date): BigDecimal? {
        val nh = BigDecimal(1000 * 60 * 60 * 24)
        val diff = BigDecimal(endDate.getTime() - startDate.getTime())
        return diff.divide(nh, 2, RoundingMode.HALF_UP)
    }


    //====================================获取时间段=======================================>>
    /**
     * 获取这一周的date，从周一到周日
     */
    fun dateToWeek(mdate: Date): MutableList<Date?> {
        val c = Calendar.getInstance()
        c.setTime(mdate)
        val b = c.get(Calendar.DAY_OF_WEEK) - 1

        var fdate: Date?
        val list: MutableList<Date?> = ArrayList<Date?>()
        val fTime = mdate.getTime() - b * 24 * 3600000
        for (a in 1..7) {
            fdate = Date()
            fdate.setTime(fTime + (a * 24 * 3600000))
            list.add(a - 1, fdate)
        }
        return list
    }

    /**
     * 获取前几天的date
     */
    fun getDateListBefore(date: Date?, num: Int): MutableList<Date?>? {
        if (date == null) return null
        val result: MutableList<Date?> = ArrayList<Date?>()
        val c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.DATE, -num)
        for (a in 1..num) {
            c.add(Calendar.DATE, 1)
            val temp = c.getTime()
            result.add(temp)
        }
        return result
    }

    /**
     * 获取一周、月、年的date
     */
    fun getDateListByKind(date: Date?, kind: String?): MutableList<Date?>? {
        if (date == null || kind == null) return null

        val end = Calendar.getInstance()

        val c = Calendar.getInstance()
        c.setFirstDayOfWeek(Calendar.MONDAY)
        c.setTime(date)
        //将时分秒,毫秒域清零
        c.clearToDayStart()
        when (kind) {
            "week" -> {
                c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK))
                end.setTime(c.getTime())
                end.add(Calendar.DATE, 7)
            }

            "month" -> {
                c.set(Calendar.DAY_OF_MONTH, 1)
                end.setTime(c.getTime())
                end.add(Calendar.MONTH, 1)
            }

            "half" -> {
                c.add(Calendar.MONTH, -6)
                c.set(Calendar.DAY_OF_MONTH, 1)
                end.setTime(date)
                end.set(Calendar.DAY_OF_MONTH, 1)
                end.add(Calendar.MONTH, 1)
                end.add(Calendar.DAY_OF_MONTH, -1)
            }

            "year" -> {
                c.set(Calendar.DAY_OF_YEAR, 1)
                end.setTime(c.getTime())
                end.add(Calendar.YEAR, 1)
            }

            else -> return null
        }

        var setSec = false
        val result: MutableList<Date?> = ArrayList<Date?>()
        while (c.before(end)) {
            if (result.size != 0 && !setSec) {
                //将时分秒,毫秒域清零
                c.set(Calendar.HOUR_OF_DAY, 23)
                c.set(Calendar.MINUTE, 59)
                c.set(Calendar.SECOND, 59)
                c.set(Calendar.MILLISECOND, 999)
                setSec = true
            }
            result.add(c.getTime())
            c.add(Calendar.DATE, 1)
        }
        return result
    }

    //====================================修改时间=======================================>>
    /**
     * 获取前一秒
     */
    fun getLastSec(date: Date?): Date? {
        if (date == null) return null
        val c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.SECOND, -1)
        return c.getTime()
    }

    /**
     * 添加秒
     */
    fun getWhichSecs(date: Date?, secs: Int): Date? {
        if (date == null) return null
        val c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.SECOND, secs)
        return c.getTime()
    }

    /**
     * 添加分
     */
    fun getWhichMinute(date: Date?, minute: Int): Date? {
        if (date == null) return null
        val c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.MINUTE, minute)
        return c.getTime()
    }

    /**
     * 获取后一天
     */
    fun getNextDate(date: Date?): Date? {
        return addDay(date, 1)
    }

    /**
     * 添加天
     */
    fun addDay(date: Date?, day: Int?): Date? {
        if (date == null || day == null) return null
        val c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.DATE, day)
        return c.getTime()
    }

    /**
     * 添加月
     */
    fun addMonth(date: Date?, month: Int?): Date? {
        if (date == null || month == null) return null
        if (month == 0) return date
        val c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.MONTH, month)
        return c.getTime()
    }

    /**
     * 添加年
     */
    fun addYear(date: Date?, year: Int?): Date? {
        if (date == null || year == null) return null
        val c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.YEAR, year)
        return c.getTime()
    }

    /**
     * 清除时分秒
     */
    fun clearTime(date: Date): Date {
        val c = Calendar.getInstance()
        c.setTime(date)
        //将时分秒,毫秒域清零
        c.clearToDayStart()
        return c.getTime()
    }

    /**
     * 清除秒
     */
    fun clearSec(date: Date): Date {
        val c = Calendar.getInstance()
        c.setTime(date)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.getTime()
    }

    /**
     * 将时分秒设置成当前时间
     */
    fun setTimeToNow(date: Date?): Date? {
        return setTime(date, Date())
    }

    /**
     * 将时分秒设置成新时间
     */
    fun setTime(date: Date?, newDate: Date): Date? {
        if (date == null) return null
        val c = Calendar.getInstance()
        c.setTime(date)
        val now = Calendar.getInstance()
        now.setTime(newDate)
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY))
        c.set(Calendar.MINUTE, now.get(Calendar.MINUTE))
        c.set(Calendar.SECOND, now.get(Calendar.SECOND))
        c.set(Calendar.MILLISECOND, now.get(Calendar.MILLISECOND))
        return c.getTime()
    }

    /**
     * Timestamp时间戳转date
     *
     * @param timestamp 时间戳
     */
    fun timeToDate(timestamp: Timestamp?): Date? {
        if (timestamp == null) return null
        return Date(timestamp.time)
    }

    val isWorkTime: Boolean
        get() {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return hour in 5..17 || hour == 0
        }

    fun ehrTimeTransform(time: String): String? {
        if (isBlank(time)) return null
        val format: String?
        if (time.length == 8) {
            format = "yyyyMMdd"
        } else if (time.length == 14) {
            format = "yyyyMMddHHmmss"
        } else {
            format = "yyyy-MM-dd HH:mm:ss"
        }
        return dateToStr(strToDate(time, format), "yyyy-MM-dd HH:mm:ss")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun awaitFreeTime() {
        while (isWorkTime) {
            try {
                Thread.sleep(600000)
            } catch (e: InterruptedException) {
                throw IllegalArgumentException("任务等待失败")
            }
        }
        println("等待结束")
    }

    private fun isBlank(text: String?): Boolean {
        return text == null || text.trim().isEmpty()
    }
}
