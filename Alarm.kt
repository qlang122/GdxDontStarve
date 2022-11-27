package com.qlang.phonelisten.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.*
import com.qlang.phonelisten.R
import com.qlang.phonelisten.config.UserConfig
import kotlin.math.abs

class FloatingBallService : Service(), View.OnTouchListener {
    private var statusBarHeight: Int = 24
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private val topWidth = 40
    private val topHeight = 40

    private var downTime: Long = System.currentTimeMillis()
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var downX: Float = 0f
    private var downY: Float = 0f

    private var topX: Int = 0
    private var topY: Int = 0

    private var floatingView: View? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var isInShow = false

    private val MSG_IDLE = 0x1001
    private val MSG_REFRESH = 0x1004
    private val MSG_CLICK = 0x1005
    private val MSG_LONG_CLICK = 0x1006
    private val handler = Handler {
        when (it.what) {
            MSG_REFRESH -> {
                layoutParams?.x = topX
                layoutParams?.y = topY
                updateView()
                set2Idle()
            }
            MSG_CLICK -> {
                val vW = (floatingView?.measuredWidth ?: 0)
                val x = layoutParams?.x ?: 0
                if (x < 0) {
                    layoutParams?.x = 0
                } else if (x >= screenWidth - vW / 2) {
                    layoutParams?.x = screenWidth - vW
                }
                updateView()
                set2Idle()
            }
            MSG_IDLE -> {
                val vW = (floatingView?.measuredWidth ?: 0)
                val x = layoutParams?.x ?: 0
                if (x < screenWidth / 2) {
                    layoutParams?.x = -(vW * 2 / 3f).toInt()
                } else if (x >= screenWidth / 2) {
                    layoutParams?.x = (screenWidth - vW * 2 / 3f).toInt()
                }
                updateView()
            }
            MSG_LONG_CLICK -> {
            }
        }
        false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (UserConfig.isShowFloatingBall) {
            if (isInShow) removeView()
            init()
        } else removeView()
        return START_STICKY
    }

    private fun init() {
        if (!UserConfig.isShowFloatingBall) return

        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (id > 0) statusBarHeight = resources.getDimensionPixelSize(id)

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        screenWidth = windowManager?.defaultDisplay?.width ?: 0
        screenHeight = windowManager?.defaultDisplay?.height ?: 0

        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_view, null)
        floatingView?.findViewById<View>(R.id.btn_view)?.setOnTouchListener(this)

        layoutParams = WindowManager.LayoutParams()
        layoutParams?.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT

        topX = 0
        topY = screenHeight / 2 - (floatingView?.measuredHeight ?: 0)

        layoutParams?.x = topX
        layoutParams?.y = topY
        if (Build.VERSION.SDK_INT >= 26)
            layoutParams?.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else layoutParams?.type = WindowManager.LayoutParams.TYPE_TOAST
        layoutParams?.gravity = Gravity.START or Gravity.TOP
        layoutParams?.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams?.format = PixelFormat.TRANSLUCENT

        windowManager?.addView(floatingView, layoutParams)
        isInShow = true

        handler.sendEmptyMessageDelayed(MSG_IDLE, 8000)
    }

    private fun updateView() {
        (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.updateViewLayout(floatingView, layoutParams)
    }

    private fun set2Idle() {
        handler.removeMessages(MSG_IDLE)
        handler.sendEmptyMessageDelayed(MSG_IDLE, 5000)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downTime = event.downTime

                lastX = event.rawX
                lastY = event.rawY
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(event.rawX - lastX) > 0 || abs(event.rawY - lastY) > 0) {
                    topX = (event.rawX - downX).toInt()
                    topY = (event.rawY - statusBarHeight - downY).toInt()

                    if (topX < 0) {
                        topX = 0
                    } else if (topX + topWidth > screenWidth) {
                        topX = screenWidth - topWidth
                    }

                    if (topY < 0) {
                        topY = 0
                    } else if (topY + topHeight > screenHeight) {
                        topY = screenHeight - topHeight
                    }

                    lastX = event.rawX
                    lastY = event.rawY
                    handler.sendEmptyMessage(MSG_REFRESH)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (abs(event.rawX - lastX) < 40 && abs(event.rawY - lastY) < 40) {
                    if (event.eventTime - downTime < 200) handler.sendEmptyMessage(MSG_CLICK)
                    if (event.eventTime - downTime > 1000) handler.sendEmptyMessage(MSG_LONG_CLICK)
                }
            }
        }
        return true
    }

    override fun onDestroy() {
        removeView()
        super.onDestroy()
    }

    private fun removeView() {
        floatingView?.let { (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.removeView(it) }
        floatingView = null
        isInShow = false
    }
}
