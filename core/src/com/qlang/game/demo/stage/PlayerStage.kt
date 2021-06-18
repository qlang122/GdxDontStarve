package com.qlang.game.demo.stage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.actor.player.WilsonActor
import com.qlang.game.demo.config.AppConfig
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.utils.ItemWrapper

class PlayerStage : Stage {
    private val playManager: AssetManager? = GameManager.instance?.playManager

    private var sceneLoader: SceneLoader? = null
    private var wrapper: ItemWrapper? = null

    constructor() : super(ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)) {

    }
}