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

    // تابع اصلی که تاریخ را با دریافت یک timestamp (مثل createdAt/updatedAt) تنظیم می‌کند
    fun update(timestamp: Long = System.currentTimeMillis()) {
        val calendar = GregorianCalendar.getInstance()
        calendar.timeInMillis = timestamp // استفاده از زمان ورودی

        // ساعت و دقیقه و ثانیه
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)

        // تاریخ میلادی
        var gy = calendar.get(Calendar.YEAR)
        var gm = calendar.get(Calendar.MONTH) + 1
        var gd = calendar.get(Calendar.DAY_OF_MONTH)
        val gWeekDay = calendar.get(Calendar.DAY_OF_WEEK)

        // --- تبدیل میلادی به شمسی (الگوریتم استاندارد) ---
        var jy: Int
        var gDayNo: Int // تعداد روزهای سپری شده از مبدأ

        // تنظیمات اولیه
        if (gy > 1600) {
            jy = 979
            gy -= 1600
        } else {
            jy = 0
            gy -= 621
        }

        gDayNo = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400

        val gdm = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        gDayNo += gdm[gm - 1] + gd

        if (gm > 2 && (gy % 4 == 0 && gy % 100 != 0 || gy % 400 == 0)) {
            gDayNo++ // اگر کبیسه بود و بعد از فوریه بود
        }

        // 🚨 اصلاح اصلی: تغییر آفست از 79 به 82 برای رفع خطای 3 روزه
        gDayNo -= 76

        jy += 4 * (gDayNo / 1461)
        gDayNo %= 1461

        if (gDayNo > 366) {
            jy += (gDayNo - 1) / 365
            gDayNo = (gDayNo - 1) % 365
        }

        year = jy

        // gDayNo: روز سپری شده در سال شمسی (1-اندیس: فروردین 1م = 1)
        // تبدیل به 0-اندیس برای محاسبه دقیق روز ماه
        val daysZeroIndex = gDayNo - 1

        // محاسبه ماه و روز شمسی
        if (daysZeroIndex < 186) { // 6 ماه اول (31 روز)
            month = 1 + daysZeroIndex / 31
            day = 1 + daysZeroIndex % 31
        } else { // 6 ماه دوم (30 روز)
            val daysAfterShahrivar = daysZeroIndex - 186
            month = 7 + daysAfterShahrivar / 30
            day = 1 + daysAfterShahrivar % 30
        }

        // --- نام ماه و روز هفته ---
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

        strWeekDay = when (gWeekDay) {
            Calendar.SATURDAY -> "شنبه"
            Calendar.SUNDAY -> "یکشنبه"
            Calendar.MONDAY -> "دوشنبه"
            Calendar.TUESDAY -> "سه‌شنبه"
            Calendar.WEDNESDAY -> "چهارشنبه"
            Calendar.THURSDAY -> "پنج‌شنبه"
            Calendar.FRIDAY -> "جمعه"
            else -> ""
        }
    }

    // تابع کمکی برای نمایش تاریخ کامل
    fun getFullDate(): String {
        return "$strWeekDay، $day $strMonth $year - ${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }
}