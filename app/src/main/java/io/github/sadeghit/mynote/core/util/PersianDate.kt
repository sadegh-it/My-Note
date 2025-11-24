package io.github.sadeghit.mynote.core.util

import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersianDate @Inject constructor() {

    var strWeekDay = ""
    var strMonth = ""
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var second = 0

    init {
        update()
    }

    fun update() {
        val calendar = GregorianCalendar.getInstance()

        // ساعت فعلی
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)

        // تاریخ میلادی
        var gy = calendar.get(Calendar.YEAR)
        var gm = calendar.get(Calendar.MONTH) + 1
        var gd = calendar.get(Calendar.DAY_OF_MONTH)
        val gWeekDay = calendar.get(Calendar.DAY_OF_WEEK)

        // تبدیل میلادی به شمسی (الگوریتم معروف و دقیق)
        var jy: Int
        if (gy > 1600) {
            jy = 979
            gy -= 1600
        } else {
            jy = 0
            gy -= 621
        }

        var days = 365 * gy + ((gy + 3) / 4) - ((gy + 99) / 100) + ((gy + 399) / 400) - 80 + gd +
                intArrayOf(
                    0,
                    31,
                    if ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)) 29 else 28,
                    31,
                    30,
                    31,
                    30,
                    31,
                    31,
                    30,
                    31,
                    30,
                    31
                )[gm - 1]

        jy += 33 * (days / 12053)
        days %= 12053

        jy += 4 * (days / 1461)
        days %= 1461

        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }

        year = jy
        month = if (days < 186) 1 + days / 31 else 7 + (days - 186) / 30
        day = 1 + (if (days < 186) days % 31 else (days - 186) % 30)

        // نام ماه
        strMonth = when (month) {
            1 -> "فروردین"
            2 -> "اردیبهشت"
            3 -> "خرداد"
            4 -> "تیر"
            5 -> "مرداد"
            6 -> "شهریور"
            7 -> "مهر"
            8 -> "آبان"
            9 -> "آذر"
            10 -> "دی"
            11 -> "بهمن"
            12 -> "اسفند"
            else -> ""
        }

        // نام روز هفته
        strWeekDay = when (gWeekDay) {
            Calendar.SUNDAY -> "یکشنبه"
            Calendar.MONDAY -> "دوشنبه"
            Calendar.TUESDAY -> "سه‌شنبه"
            Calendar.WEDNESDAY -> "چهارشنبه"
            Calendar.THURSDAY -> "پنج‌شنبه"
            Calendar.FRIDAY -> "جمعه"
            Calendar.SATURDAY -> "شنبه"
            else -> ""
        }
    }

    // متدهای آماده برای استفاده
    fun getCurrentDateTime(): String {
        update()
        return "$year/$month/$day | $hour:${minute.toString().padStart(2, '0')}"
    }

    fun getCurrentDateOnly(): String {
        update()
        return "$year/$month/$day"
    }

    fun getFullDate(): String {
        update()
        return "$strWeekDay، $day $strMonth $year"
    }
}