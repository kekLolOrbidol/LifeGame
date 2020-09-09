package xyz.do9core.lifegame.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import java.util.*


class Message {
    fun messageSchedule(context: Context) {
        val calendar: Calendar = Calendar.getInstance()
        var n = 0
//        calendar.add(Calendar.DAY_OF_WEEK, 1)
////        calendar.set(Calendar.HOUR_OF_DAY, 16)
////        calendar.set(Calendar.MINUTE, 39)
        for (i in 0..29) {
            calendar.setTimeInMillis(System.currentTimeMillis())
            if (i <= 7) {
                calendar.add(Calendar.DAY_OF_WEEK, i)
                calendar.set(Calendar.HOUR_OF_DAY, 20)
                calendar.set(Calendar.MINUTE, 0)
                scheduleMessage(calendar, context, i)
            }
            if (i >= 9) {
                calendar.add(Calendar.DAY_OF_WEEK, i + 2 * n)
                calendar.set(Calendar.HOUR_OF_DAY, 20)
                calendar.set(Calendar.MINUTE, 0)
                scheduleMessage(calendar, context, i)
                n++
            }
        }
    }

    private fun scheduleMessage(calendar: Calendar, context: Context, type: Int) {
        val i = Intent(context, Receiver::class.java)
        i.putExtra(TYPE_EXTRA, type)
        val pendingIntent = PendingIntent.getBroadcast(context, type, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManagerRTC = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManagerRTC[AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()] = pendingIntent
    }

    fun getNotificationManager(context: Context): Any? {
        return context.getSystemService(Context.NOTIFICATION_SERVICE)
    }

    companion object {
        const val TYPE_EXTRA = "type"
    }
}