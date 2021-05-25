package com.qlang.game.demo

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val config = AndroidApplicationConfiguration()
        initialize(MainGame(), config)
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    override fun onDestroy() {
        quitFullScreen()
        showNavigationBar()
        super.onDestroy()
    }

    fun setFullScreen() {
        val lp = window.attributes
        lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.attributes = lp
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= 16) {
            val decorView = window.decorView
            var uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_IMMERSIVE
            }
            decorView.systemUiVisibility = uiOptions
        }
    }

    fun showNavigationBar() {
        if (Build.VERSION.SDK_INT >= 16) {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            decorView.systemUiVisibility = uiOptions
        }
    }

    fun quitFullScreen() {
        val attrs = window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        window.attributes = attrs
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}