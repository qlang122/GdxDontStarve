package com.qlang.game.demo.script

import com.badlogic.ashley.core.PooledEngine
import games.rednblack.editor.renderer.scripts.BasicScript

abstract class EntityScript : BasicScript {
    protected var engine: PooledEngine? = null

    constructor(engine: PooledEngine) {
        this.engine = engine
    }
}