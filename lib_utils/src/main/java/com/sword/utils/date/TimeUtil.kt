package com.sword.utils.date

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 * @author: zph
 * @date: 2026/5/18
 * 描述:时间工具处理类
 */
object TimeUtil {
    val currentWeekOfMonth: Int
        /**
         * 获取当前时间为本月的第几周
         *
         * @return 本月的第几周
         */
        get() = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)

    val currentDayOfWeek: Int
        /**
         * 获取当前时间为本周的第几天
         *
         * @return 本周的第几天（1代表周一，7代表周日）
         */
        get() {
            val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            return ((dayOfWeek + 5) % 7) + 1
        }

    val currentDayOfWeekText: String
        /**
         * 返回当前日期是星期几
         *
         * @return 例如：星期日、星期一、星期六等等。
         */
        get() {
            val calendar = Calendar.getInstance()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            // 获取星期几文本
            val symbols = DateFormatSymbols(Locale.getDefault())
            val dayOfWeekTexts = symbols.weekdays
            return if (dayOfWeek in Calendar.SUNDAY..Calendar.SATURDAY) {
                dayOfWeekTexts[dayOfWeek]
            } else ""
        }

    /**
     * 判断指定时间是否在时间区间内
     *
     * @param time        待判断的时间
     * @param startTime   时间区间的开始时间
     * @param endTime     时间区间的结束时间
     * @return 如果指定时间在时间区间内，返回 true；否则返回 false
     */
    fun isTimeInRange(time: Calendar, startTime: Calendar?, endTime: Calendar?): Boolean {
        if (startTime == null || endTime == null) return false
        val target = time.timeInMillis
        return target in startTime.timeInMillis..endTime.timeInMillis
    }

    /**
     * 判断指定时间是否在时间区间内
     *
     * @param time        待判断的时间
     * @param startTime   时间区间的开始时间
     * @param endTime     时间区间的结束时间
     * @return 如果指定时间在时间区间内，返回 true；否则返回 false
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeInRange(
        time: LocalDateTime,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Boolean {
        return time in startTime..endTime
    }

    /**
     * 判断指定时间是否在时间区间内
     *
     * @param currentTime 待判断的时间
     * @param startTime   时间区间的开始时间
     * @param endTime     时间区间的结束时间
     * @return 如果指定时间在时间区间内，返回 true；否则返回 false
     */
    fun isInTimeRange(currentTime: Date, startTime: Date, endTime: Date): Boolean {
        val currentTimeMillis = currentTime.time
        return currentTimeMillis >= startTime.time && currentTimeMillis <= endTime.time
    }

    /**
     * 求两个日期相差天数
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 相差天数
     */
    fun calculateDaysDifference(startDate: Date, endDate: Date): Long {
        val differenceMillis = endDate.time - startDate.time
        return TimeUnit.MILLISECONDS.toDays(differenceMillis)
    }


    /**
     * 返回友好时间跨度
     *
     * @param date 需要格式化的时间
     *
     * @return the fit time span
     * return 小于1分钟，返回"刚刚"
     * return 小于1小时但大于0分钟，返回"X分钟前"
     * return 小于1天但大于0小时，返回"X小时前"
     * return 昨天，返回"昨天"
     * return 大于1天，返回"X天前"
     */
    fun calculateTimeDifference(date: Date): String {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - date.time
        if (timeDifference <= 0L) return "刚刚"

        // 计算时间差对应的单位
        val minutes = timeDifference / TimeUnit.MINUTES.toMillis(1)
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 1 -> "${days}天前"
            days == 1L -> "昨天"
            hours > 0 -> "${hours}小时前"
            minutes > 0 -> "${minutes}分钟前"
            else -> "刚刚"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun test() {

        // 获取当前时间为本月的第几周
        val weekOfMonth = currentWeekOfMonth
        println(weekOfMonth)

        // 获取当前时间为本周的第几天
        val dayOfWeek = currentDayOfWeek
        println(dayOfWeek)

        // 返回当前日期是星期几的文本表示
        val dayOfWeekText = currentDayOfWeekText
        println(dayOfWeekText)

        // 判断指定时间是否在时间区间内
//        val isInRange: Boolean = TimeUtil.isTimeInRange(time, startTime, endTime)

        // 计算两个日期之间相差的天数
//        val daysDifference = calculateDaysDifference(startDate, endDate)

        // 返回友好的时间跨度表示
        val date = DateUtil.currentTime
        val friendDate = calculateTimeDifference(date)
        println(friendDate)

    }
}
