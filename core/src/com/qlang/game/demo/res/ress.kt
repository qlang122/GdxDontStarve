package com.qlang.game.demo.res

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.qlang.game.demo.utils.Log

class GameAssetManager private constructor() : ApplicationAdapter() {
    lateinit var mainManager: AssetManager
        private set
    lateinit var playManager: AssetManager
        private set

    companion object {
        var instance: GameAssetManager? = GameAssetManager()

    }

    override fun create() {
        mainManager = AssetManager()
        playManager = AssetManager()

        mainManager.loadAssets()
        mainManager.finishLoading()
    }

    private fun AssetManager.loadAssets() {
        load(R.font.font_cn, BitmapFont::class.java)
        R.image.atlas().forEach {
            Log.e("QL", it)
            load(it, TextureAtlas::class.java)
        }
    }

    val mainAssetsLoadProgress: Float get() = mainManager.progress
    val isMainAssetsLoaded: Boolean get() = mainManager.isFinished

    fun AssetManager.loadPlayAssets() {
        load(R.anim.wilson.atlas, TextureAtlas::class.java)
        R.image.fxs.atlas().forEach { load(it, TextureAtlas::class.java) }
        R.image.tile.atlas().forEach { load(it, TextureAtlas::class.java) }
    }

    val playAssetsLoadProgress: Float get() = mainManager.progress
    val isPlayAssetsLoaded: Boolean get() = mainManager.isFinished

    override fun dispose() {
        mainManager.dispose()
        playManager.dispose()

        instance = null
    }
}

object R {
    @JvmSuppressWildcards
    object anim {
        const val player_idle = "anim/player-idles.scml"
        const val player_basic = "anim/player-basic.scml"

        object wilson {
            const val atlas = "anim/wilson/wilson.atlas"
        }
    }

    @JvmSuppressWildcards
    object image {
        const val bg_redux_bottom = "atlas/images/bg_redux_dark_bottom.atlas"
        const val bg_redux_bottom_solid = "atlas/images/bg_redux_dark_bottom_solid.atlas"
        const val bg_redux_bottom_over = "atlas/images/bg_redux_dark_bottom_vignette1.atlas"

        const val customisation = "atlas/images/customisation.atlas"
        const val fepanels = "atlas/images/fepanels.atlas"
        const val fepanels_DSTbeta = "atlas/images/fepanels_DSTbeta.atlas"
        const val frontscreen = "atlas/images/frontscreen.atlas"
        const val fx = "atlas/images/fx.atlas"
        const val hud = "atlas/images/hud.atlas"
        const val inventoryimages = "atlas/images/inventoryimages.atlas"
        const val saveslot_port = "atlas/images/saveslot_portraits.atlas"
        const val selectscreen_port = "atlas/images/selectscreen_portraits.atlas"
        const val ui = "atlas/images/ui.atlas"
        const val woodie = "atlas/images/woodie.atlas"

        object fxs {
            const val frostbreath = "atlas/fx/frostbreath.atlas"
            const val hail = "atlas/fx/hail.atlas"
            const val rain = "atlas/fx/rain.atlas"
            const val snow = "atlas/fx/snow.atlas"
            const val torchfire = "atlas/fx/torchfire.atlas"

            fun atlas(): ArrayList<String> {
                val list = ArrayList<String>()
                for (it in this.javaClass.fields) {
                    val name = it?.name
                    if (name != null) list.add(name)
                }
                return list
            }
        }

        object tile {
            const val blocks = "atlas/levels/tiles/blocks.atlas"
            const val blocky = "atlas/levels/tiles/blocky.atlas"
            const val carpet = "atlas/levels/tiles/carpet.atlas"
            const val cave = "atlas/levels/tiles/cave.atlas"
            const val cobblestone = "atlas/levels/tiles/cobblestone.atlas"
            const val dirt = "atlas/levels/tiles/dirt.atlas"
            const val falloff = "atlas/levels/tiles/falloff.atlas"
            const val forest = "atlas/levels/tiles/forest.atlas"
            const val grass = "atlas/levels/tiles/grass.atlas"
            const val grass2 = "atlas/levels/tiles/grass2.atlas"
            const val map = "atlas/levels/tiles/map.atlas"
            const val map_edge = "atlas/levels/tiles/map_edge.atlas"
            const val marsh = "atlas/levels/tiles/marsh.atlas"
            const val marsh_pond = "atlas/levels/tiles/marsh_pond.atlas"
            const val moat = "atlas/levels/tiles/moat.atlas"
            const val rocky = "atlas/levels/tiles/rocky.atlas"
            const val walls = "atlas/levels/tiles/walls.atlas"
            const val web = "atlas/levels/tiles/web.atlas"
            const val yellowgrass = "atlas/levels/tiles/yellowgrass.atlas"

            fun atlas(): ArrayList<String> {
                val list = ArrayList<String>()
                for (it in this.javaClass.fields) {
                    val name = it?.name
                    if (name != null) list.add(name)
                }
                return list
            }
        }

        fun atlas(): ArrayList<String> {
            val list = ArrayList<String>()
            for (it in this.javaClass.fields) {
                val name = it?.name
                if (name != null) list.add(name)
            }
            return list
        }
    }

    object font {
        const val font_cn = "fonts/font_cn.fnt"
    }
}