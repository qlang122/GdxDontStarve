package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.qlang.game.demo.config.AppConfig
import java.util.*


class TestStage : Stage {
    var cam: Camera? = null
    var batch: SpriteBatch? = null

    var instances = arrayOfNulls<ModelInstance>(100)
    var modelBatch: ModelBatch? = null
    var tmp = Vector3()
    var logo: TextureRegion? = null

    constructor() : super(ExtendViewport(AppConfig.worldWidth, AppConfig.worldHeight)) {
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam?.far = 200f

        val rand = Random()
        for (i in instances.indices) {
            instances[i] = ModelInstance(Model().apply {
                materials.add(Material(ColorAttribute(ColorAttribute.Diffuse, Color.RED)))
            }, rand.nextFloat() * 100 - rand.nextFloat() * 100, rand.nextFloat() * 100
                    - rand.nextFloat() * 100, rand.nextFloat() * -100 - 3)
        }

        batch = SpriteBatch()
        logo = TextureRegion(Texture(Gdx.files.internal("atlas/images/hud-1.png")))
        modelBatch = ModelBatch()
    }

    override fun draw() {
        cam?.update()
        modelBatch?.begin(cam)

        var visible = 0
        for (model in instances) {
            model?.transform?.getTranslation(tmp)
            if (cam?.frustum?.sphereInFrustum(tmp, 1f) == true) {
//                (model?.materials?.get(0)?.get(ColorAttribute.Diffuse) as? ColorAttribute)?.color?.set(Color.WHITE)
                visible++
            } else {
//                (model?.materials?.get(0)?.get(ColorAttribute.Diffuse) as? ColorAttribute)?.color?.set(Color.RED)
            }
            modelBatch?.render(model)
        }
        modelBatch?.end()

        if (Gdx.input.isKeyPressed(Input.Keys.A)) cam?.rotate(20 * Gdx.graphics.deltaTime, 0f, 1f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.D)) cam?.rotate(-20 * Gdx.graphics.deltaTime, 0f, 1f, 0f)

        batch?.begin()
        for (element in instances) {
            element?.transform?.getTranslation(tmp)
            cam?.project(tmp)
            if (tmp.z < 0) continue
            batch?.draw(logo, tmp.x, tmp.y)
        }
        batch?.end()
    }
}