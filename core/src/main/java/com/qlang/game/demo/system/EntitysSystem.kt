package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Polygon
import com.qlang.game.demo.component.EntityComponent
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.utils.Log
import com.qlang.game.demo.utils.Utils
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.PolygonComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.utils.ItemWrapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

open class EntitysSystem : IteratingSystem(Family.all(EntityComponent::class.java).get()) {
    private val mainItemMapper: ComponentMapper<MainItemComponent> = ComponentMapper.getFor(MainItemComponent::class.java)
    private val entityMapper: ComponentMapper<EntityComponent> = ComponentMapper.getFor(EntityComponent::class.java)
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)
    private val dimensionsMapper: ComponentMapper<DimensionsComponent> = ComponentMapper.getFor(DimensionsComponent::class.java)

    private var playerComponent: PlayerComponent? = null

    private var player: Entity? = null
    private var playerPolygon: Polygon? = null

    private var goalEntity: Entity? = null
    private var minAbs: Double = Double.MAX_VALUE

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        entities?.forEach { e ->
            val p = e.getComponent(PolygonComponent::class.java)?.let {
                e.getComponent(EntityComponent::class.java)?.setPolygon(it)
                it
            }
        }
        Log.e("QL", "--->>", entities)
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (player == null) return

        val m = mainItemMapper.get(entity)
        if (m.culled) return

        val t = transformMapper.get(entity)
        val ec = entityMapper.get(entity)
        if ((ec.info.type and GoodsInfo.Type.UNKNOW.value) == GoodsInfo.Type.UNKNOW.value) return

        val pT = transformMapper.get(player)

        val abs = abs(sqrt(pT.x.minus(t.x + 0.0).pow(2.0) + pT.y.minus(t.y + 0.0).pow(2.0)))
        ec.playerDistance = abs.toFloat()

        if (abs < 300) {
            val overlap = Utils.isOverlap(ec.polygon, playerPolygon)
            ec.isOverlapPlayer = overlap

            if (abs < minAbs) {
                minAbs = abs;goalEntity = entity
            }

            playerComponent?.let {
                if (it.goalEntity != goalEntity) it.goalEntity = goalEntity
            }
        }

    }

    fun setPlayer(player: Entity?) {
        this.player = player
        player?.let {
            playerPolygon = it.getComponent(PolygonComponent::class.java)?.let { Utils.makePolygon(it) }
            playerComponent = ItemWrapper(it).getChild("role")?.entity?.getComponent(PlayerComponent::class.java)
        }
    }

}