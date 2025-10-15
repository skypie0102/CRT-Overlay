package com.example.crtoverlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager

class OverlayService : Service() {
    companion object {
        var isRunning = false
        const val CHANNEL_ID = "crt_overlay_channel"
    }

    private var overlayView: OverlayView? = null
    private var wm: WindowManager? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(CHANNEL_ID, "CRT Overlay", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NotificationManager::class.java)).createNotificationChannel(chan)
            val n: Notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("CRT Overlay")
                .setContentText("Overlay running")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build()
            startForeground(1337, n)
        }

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        overlayView = OverlayView(this)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        wm?.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        overlayView?.let { wm?.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
