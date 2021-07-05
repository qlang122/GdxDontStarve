package com.qlang.game.demo.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import me.winter.gdx.animation.Animation
import me.winter.gdx.animation.Entity
import me.winter.gdx.animation.scml.SCMLLoader
import me.winter.gdx.animation.scml.SCMLProject

abstract class BaseAnimActor : Actor {
    protected val entitys: ArrayList<Entity> = ArrayList()
    var animation: Animation? = null
        protected set

    private lateinit var loader: SCMLLoader
    private val projects: ArrayList<SCMLProject> = ArrayList()
    private lateinit var parameters: SCMLLoader.Parameters

    constructor(textureAtlasName: String) : super() {
        parameters = SCMLLoader.Parameters(textureAtlasName)
        loader = SCMLLoader(InternalFileHandleResolver())
    }

    fun loadScml(manager: AssetManager, name: String) {
        val file = Gdx.files.internal(name)
        loader.load(manager, "", file, parameters)
                .also { projects.add(it) }
    }

    fun loadEntity(name: String): Boolean {
        var b = false
        for (it in projects) {
            for (e in it.sourceEntities) {
                if (name == e.name) {
                    entitys.add(e)
                    b = true
                }
            }
        }
        return b
    }

    fun loadAnimation(name: String) {
        for (it in entitys) {
            animation = it.getAnimation(name)
            if (animation != null) break
        }
    }

    override fun act(delta: Float) {
        super.act(delta)

        animation?.let {
            if (it.isDone) it.reset()
            it.update(delta.times(1000))//delta单位为s，0.001
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch ?: return
        if (!isVisible) return
        animation?.draw(batch)
    }
}