package allianceone.prog.leanmanagementsystem.core

import allianceone.prog.leanmanagementsystem.ui.home.MainActivity
import allianceone.prog.leanmanagementsystem.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


const val CHANNEL_ID = "notification_channel_id"

class NotificationHelper(private val context: Context) {

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    enableVibration(true)
                    vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                    setSound(ringtoneManager, audioAttributes)
                }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendNotification(tag: String, title: String, content: String) {
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val icon =
            BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .apply {
                setContentTitle(title)
                setContentText(content)
                setContentIntent(pendingIntent)
                setAutoCancel(true)
                setLargeIcon(icon)
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setDefaults(NotificationCompat.DEFAULT_ALL)
                priority = NotificationCompat.PRIORITY_MAX
            }.build()

        NotificationManagerCompat.from(context).notify(tag, 1, notification)
    }
}