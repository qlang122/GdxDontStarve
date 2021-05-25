package com.qlang.game.demo.actor.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.qlang.game.demo.res.R
import com.qlang.game.demo.utils.Log
import me.winter.gdx.animation.Animation
import me.winter.gdx.animation.Entity
import me.winter.gdx.animation.scml.SCMLLoader

class PlayerHealthActor(val manager: AssetManager) : Actor() {
    private var animation: Animation? = null

    private var arrowEntity: Entity? = null
    private var arrowAnim: Animation? = null
    private var currStage = BodyIndexState.NEUTRAL

    init {
        val file = Gdx.files.internal(R.anim.hud.health)
        val parameters = SCMLLoader.Parameters(R.anim.hud.health_atlas)
        val scml = SCMLLoader(InternalFileHandleResolver()).load(manager, "", file, parameters)
        val entity = scml.getEntity("health")
        animation = entity?.getAnimation(0)
        animation?.isLooping = false
        animation?.root?.position?.set(x, y)

        val arrowFile = Gdx.files.internal(R.anim.hud.sanity_arrow)
        val arrowParameters = SCMLLoader.Parameters(R.anim.hud.sanity_arrow_atlas)
        val arrowScml = SCMLLoader(InternalFileHandleResolver()).load(manager, "", arrowFile, arrowParameters)
        arrowEntity = arrowScml.getEntity("sanity_arrow")
        arrowAnim = arrowEntity?.getAnimation("neutral_90s")

    }

    /**
     * @param value the value must be in 0..1
     */
    fun setProgress(value: Float) {
        if (value < 0 || value >= 1) return
        val len = animation?.length ?: 0
        animation?.update(value * len)
    }

    fun changeState(value: BodyIndexState = BodyIndexState.NEUTRAL) {
        currStage = value
        arrowAnim = when (currStage) {
            BodyIndexState.DESC -> arrowEntity?.getAnimation("arrow_loop_decrease_90s")
            BodyIndexState.DESC_MORE -> arrowEntity?.getAnimation("arrow_loop_decrease_more_90s")
            BodyIndexState.DESC_MOST -> arrowEntity?.getAnimation("arrow_loop_decrease_most_90s")
            BodyIndexState.INC -> arrowEntity?.getAnimation("arrow_loop_increase_90s")
            BodyIndexState.INC_MORE -> arrowEntity?.getAnimation("arrow_loop_increase_more_90s")
            BodyIndexState.INC_MOST -> arrowEntity?.getAnimation("arrow_loop_increase_most_90s")
            else -> arrowEntity?.getAnimation("neutral_90s")
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch ?: return
        if (!isVisible) return
        animation?.draw(batch)
        arrowAnim?.draw(batch)
    }
}