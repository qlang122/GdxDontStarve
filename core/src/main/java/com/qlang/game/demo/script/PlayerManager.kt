package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper
import me.winter.gdx.animation.Sprite
import me.winter.gdx.animation.scml.SCMLProject
import me.winter.gdx.animation.scml.SCMLReader
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.R
import com.qlang.game.demo.utils.Log

class PlayerManager {
    private val manager: AssetManager? = GameManager.instance?.playManager

    private val scmlReader: SCMLReader = SCMLReader()

    private val scmlMap: HashMap<String, SCMLProject> = HashMap()

    private var spriterComponent: SpriterObjectComponent? = null
    private var playerComponent: PlayerComponent? = null

    private var currSwapObject: String? = null
    private var currSwapHat: String? = null

    constructor(entity: Entity) {
        ItemWrapper(entity).getChild("role")?.entity?.let { role ->
            spriterComponent = ComponentRetriever.get(role, SpriterObjectComponent::class.java)
            playerComponent = ComponentRetriever.get(role, PlayerComponent::class.java)
        }
    }

    fun loadScml(manager: AssetManager, scmlFile: String, textureAtlasName: String) {
        val file = Gdx.files.internal(scmlFile)
        scmlReader.atlas = manager.get(textureAtlasName)
        val assets = scmlReader.loadAssets(file.read())
        scmlMap[scmlFile] = assets
    }

    fun loadAssets() {
        manager?.let { mgr ->
            loadScml(mgr, R.anim.player.hat_spider, R.anim.player.hat_spider_atlas)
            loadScml(mgr, R.anim.player.swap_glassaxe, R.anim.player.swap_glassaxe_atlas)
        }
    }

    fun swapObject(name: String?) {
        if (currSwapObject == name) return
        currSwapObject = name

        val obj = if (null != name && scmlMap.containsKey(name)) scmlMap[name] else null
        spriterComponent?.let {
            it.animation?.getTimeline("swap_object")?.keys?.forEach {
                val part = it.`object`
                if (part is Sprite) {
                    part.drawable = obj?.getAsset(part.folderName, part.file)
                }
            }
        }
    }

    fun swapHat(name: String?) {
        if (currSwapHat == name) return
        currSwapHat = name

        val obj = if (null != name && scmlMap.containsKey(name)) scmlMap[name] else null

        spriterComponent?.let {
            it.animation?.getTimeline("swap_hat")?.keys?.forEach {
                val part = it.`object`
                if (part is Sprite) {
                    part.drawable = obj?.getAsset(part.folderName, part.file)
                }
            }
        }
    }
}