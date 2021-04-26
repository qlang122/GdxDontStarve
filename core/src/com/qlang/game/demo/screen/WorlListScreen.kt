package com.qlang.game.demo.screen

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.widget.MyList

class WorlListScreen : ScreenAdapter() {
    private val manager: AssetManager? = GameManager.instance?.mainManager
    private lateinit var list: MyList<String>

    init {
        manager?.let { mgr ->


        }
    }
}