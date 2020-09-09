package xyz.do9core.lifegame.broadcast

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build

import androidx.core.app.NotificationCompat;
import xyz.do9core.lifegame.MainActivity


class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getIntExtra(Message.TYPE_EXTRA, 0)
        val intentToRepeat = Intent(context, MainActivity::class.java)
        intentToRepeat.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(context, type, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT)
        val notifManager = Message().getNotificationManager(context)
        val notification: Notification = buildNotification(context, pendingIntent, notifManager as NotificationManager?).build()
        notifManager?.notify(type, notification)
    }

    fun buildNotification(context: Context, pendingIntent: PendingIntent?, nm: NotificationManager?): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default",
                    "Daily Notification",
                    NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Daily Notification"
            nm?.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(context, "default")
                .setContentIntent(pendingIntent)
                .setContentTitle("Cегoдня твoй день!")
                .setAutoCancel(true)
    }
}
