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

class PlayerSanityActor(val manager: AssetManager) : Actor() {
    private var animation: Animation? = null

    private var arrowEntity: Entity? = null
    private var arrowAnim: Animation? = null
    private var currStage = BodyIndexState.NEUTRAL

    private var currProgress = 1f

    init {
        val file = Gdx.files.internal(R.anim.hud.sanity)
        val parameters = SCMLLoader.Parameters(R.anim.hud.sanity_atlas)
        val scml = SCMLLoader(InternalFileHandleResolver()).load(manager, "", file, parameters)
        val entity = scml.getEntity("sanity")
        animation = entity?.getAnimation("sanity")
        animation?.isLooping = false
        animation?.update(1f)

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
        currProgress = value * len
        animation?.update(currProgress)
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
        animation?.root?.position?.let { arrowAnim?.root?.position?.set(it) }
    }

    override fun setPosition(x: Float, y: Float) {
//        var p = parent
//        var offsetX = 0f
//        var offsetY = 0f
//        while (p != null) {
//            offsetX += p.x;offsetY += p.y
//            p = p.parent
//        }
////        Log.e("QL", "--3-->>", x, y, this.x, this.y, offsetX, offsetY, width, height)
//        animation?.root?.position?.set(x + this.x + offsetX + width / 2f, y + this.y + offsetY + height / 2f)
//        arrowAnim?.root?.position?.set(x + this.x + offsetX + width / 2f, y + this.y + offsetY + height / 2f)
        animation?.root?.position?.set(x + width / 2f, y + height / 2f)
        arrowAnim?.root?.position?.set(x + width / 2f, y + height / 2f -10f)
        animation?.update(currProgress)
    }

    override fun act(delta: Float) {
        super.act(delta)
        arrowAnim?.let {
            if (it.isDone) it.reset()
            it.update(delta.times(1000))//delta单位为s，0.001
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch ?: return
        if (!isVisible) return
        animation?.draw(batch)
        arrowAnim?.draw(batch)
    }
}