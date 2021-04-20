package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.utils.Log

class LaunchScreen : ScreenAdapter() {
    private var logoTexture: Texture? = null
    private var logoStage: Stage? = null

    private var delay: Float = 0f

    init {
        logoTexture = Texture(Gdx.files.internal("ic_logo.png"))
        logoStage = Stage()

        logoStage?.addActor(Image(TextureRegion(logoTexture)).apply {
            setPosition(logoStage!!.width / 2 - width / 2, logoStage!!.height / 2 - height / 2)
        })
    }

    override fun show() {
        delay = 0f
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        delay += delta

        if (delay >= 5f && GameAssetManager.instance?.isLoadFinish() == true) {
            Navigator.push(HomeScreen())
            Navigator.pop(this)
            return
        }

        logoStage?.apply { act();draw() }
    }

    override fun dispose() {
        logoStage?.dispose()
        logoTexture?.dispose()
    }
}