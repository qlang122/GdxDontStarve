package com.qlang.game.demo.component

object Player {
    object Anim {
        object Idle {
            const val down = "idle_down"
            const val inaction_down = "idle_inaction_down"
            const val inaction_sanity_down = "idle_inaction_sanity_down"
            const val inaction_sanity_side = "idle_inaction_sanity_side"
            const val inaction_sanity_up = "idle_inaction_sanity_up"
            const val inaction_side = "idle_inaction_side"
            const val inaction_up = "idle_inaction_up"
            const val loop_down = "idle_loop_down"
            const val loop_side = "idle_loop_side"
            const val loop_up = "idle_loop_up"
            const val sanity_loop_down = "idle_sanity_loop_down"
            const val sanity_loop_side = "idle_sanity_loop_side"
            const val sanity_loop_up = "idle_sanity_loop_up"
            const val sanity_pre_down = "idle_sanity_pre_down"
            const val sanity_pre_side = "idle_sanity_pre_side"
            const val sanity_pre_up = "idle_sanity_pre_up"
            const val side = "idle_side"
            const val up = "idle_up"
        }

        object Run {
            const val loop_down = "run_loop_down"
            const val loop_side = "run_loop_side"
            const val loop_up = "run_loop_up"
            const val pre_down = "run_pre_down"
            const val pre_side = "run_pre_side"
            const val pre_up = "run_pre_up"
            const val pst_down = "run_pst_down"
            const val pst_side = "run_pst_side"
            const val pst_up = "run_pst_up"
        }
    }

    object Direction {
        const val LEFT = 0
        const val RIGHT = 1
        const val UP = 2
        const val DOWN = 3
    }
}