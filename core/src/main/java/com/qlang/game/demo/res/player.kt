package com.qlang.game.demo.res

object Player {
    enum class BodyIndexState {
        FULL, FILL_UP, FILL_DOWN
    }

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

        object Attack {
            const val atk_down = "atk_down"
            const val atk_up = "atk_up"
            const val atk_side = "atk_side"
            const val atk_lag_down = "atk_lag_down"
            const val atk_lag_up = "atk_lag_up"
            const val atk_lag_side = "atk_lag_side"
            const val atk_pre_down = "atk_pre_down"
            const val atk_pre_up = "atk_pre_up"
            const val atk_pre_side = "atk_pre_side"
            const val punch_down = "punch_down"
            const val punch_up = "punch_up"
            const val punch_side = "punch_side"
            const val spearjab_down = "spearjab_down"
            const val spearjab_up = "spearjab_up"
            const val spearjab_side = "spearjab_side"
        }

        object Action_axe {
            const val chop_lag_down = "chop_lag_down"
            const val chop_lag_side = "chop_lag_side"
            const val chop_lag_up = "chop_lag_up"
            const val chop_loop_down = "chop_loop_down"
            const val chop_loop_side = "chop_loop_side"
            const val chop_loop_up = "chop_loop_up"
            const val chop_pre_down = "chop_pre_down"
            const val chop_pre_side = "chop_pre_side"
            const val chop_pre_up = "chop_pre_up"
        }

        object Action_blowdart {
            const val dart_down = "dart_down"
            const val dart_lag_down = "dart_lag_down"
            const val dart_lag_side = "dart_lag_side"
            const val dart_lag_up = "dart_lag_up"
            const val dart_long_down = "dart_long_down"
            const val dart_long_side = "dart_long_side"
            const val dart_long_up = "dart_long_up"
            const val dart_pre_down = "dart_pre_down"
            const val dart_pre_side = "dart_pre_side"
            const val dart_pre_up = "dart_pre_up"
            const val dart_side = "dart_side"
            const val dart_up = "dart_up"
        }

        object Action_boomerang {
            const val catch_down = "catch_down"
            const val catch_pre_down = "catch_pre_down"
            const val catch_pre_side = "catch_pre_side"
            const val catch_pre_up = "catch_pre_up"
            const val catch_side = "catch_side"
            const val catch_up = "catch_up"
            const val throw_down = "throw_down"
            const val throw_side = "throw_side"
            const val throw_up = "throw_up"
        }

        object Action_bugnet {
            const val bugnet_down = "bugnet_down"
            const val bugnet_lag_down = "bugnet_lag_down"
            const val bugnet_lag_side = "bugnet_lag_side"
            const val bugnet_lag_up = "bugnet_lag_up"
            const val bugnet_pre_down = "bugnet_pre_down"
            const val bugnet_pre_side = "bugnet_pre_side"
            const val bugnet_pre_up = "bugnet_pre_up"
            const val bugnet_side = "bugnet_side"
            const val bugnet_up = "bugnet_up"
        }

        object Action_eat {
            const val eat = "eat"
            const val eat_lag = "eat_lag"
            const val eat_pre = "eat_pre"
            const val hungry = "hungry"
            const val quick_eat = "quick_eat"
            const val quick_eat_lag = "quick_eat_lag"
            const val quick_eat_pre = "quick_eat_pre"
            const val refuseeat = "refuseeat"
        }

        object Action_shovel {
            const val shovel_lag_down = "shovel_lag_down"
            const val shovel_lag_side = "shovel_lag_side"
            const val shovel_lag_up = "shovel_lag_up"
            const val shovel_loop_down = "shovel_loop_down"
            const val shovel_loop_side = "shovel_loop_side"
            const val shovel_loop_up = "shovel_loop_up"
            const val shovel_pre_down = "shovel_pre_down"
            const val shovel_pre_side = "shovel_pre_side"
            const val shovel_pre_up = "shovel_pre_up"
            const val shovel_pst_down = "shovel_pst_down"
            const val shovel_pst_side = "shovel_pst_side"
            const val shovel_pst_up = "shovel_pst_up"
        }

        object Action_pickaxe {
            const val pickaxe_lag_down = "pickaxe_lag_down"
            const val pickaxe_lag_side = "pickaxe_lag_side"
            const val pickaxe_lag_up = "pickaxe_lag_up"
            const val pickaxe_loop_down = "pickaxe_loop_down"
            const val pickaxe_loop_side = "pickaxe_loop_side"
            const val pickaxe_loop_up = "pickaxe_loop_up"
            const val pickaxe_pre_down = "pickaxe_pre_down"
            const val pickaxe_pre_side = "pickaxe_pre_side"
            const val pickaxe_pre_up = "pickaxe_pre_up"
            const val pickaxe_pst_down = "pickaxe_pst_down"
            const val pickaxe_pst_side = "pickaxe_pst_side"
            const val pickaxe_pst_up = "pickaxe_pst_up"
        }

        object Action_fishing {
            const val bite_heavy_lag_down = "bite_heavy_lag_down"
            const val bite_heavy_lag_side = "bite_heavy_lag_side"
            const val bite_heavy_lag_up = "bite_heavy_lag_up"
            const val bite_heavy_loop_down = "bite_heavy_loop_down"
            const val bite_heavy_loop_side = "bite_heavy_loop_side"
            const val bite_heavy_loop_up = "bite_heavy_loop_up"
            const val bite_heavy_pre_down = "bite_heavy_pre_down"
            const val bite_heavy_pre_side = "bite_heavy_pre_side"
            const val bite_heavy_pre_up = "bite_heavy_pre_up"
            const val bite_light_lag_down = "bite_light_lag_down"
            const val bite_light_lag_side = "bite_light_lag_side"
            const val bite_light_lag_up = "bite_light_lag_up"
            const val bite_light_loop_down = "bite_light_loop_down"
            const val bite_light_loop_side = "bite_light_loop_side"
            const val bite_light_loop_up = "bite_light_loop_up"
            const val bite_light_pre_down = "bite_light_pre_down"
            const val bite_light_pre_side = "bite_light_pre_side"
            const val bite_light_pre_up = "bite_light_pre_up"
            const val bite_light_pst_down = "bite_light_pst_down"
            const val bite_light_pst_side = "bite_light_pst_side"
            const val bite_light_pst_up = "bite_light_pst_up"
            const val fish_catch_down = "fish_catch_down"
            const val fish_catch_side = "fish_catch_side"
            const val fish_catch_up = "fish_catch_up"
            const val fish_nocatch_down = "fish_nocatch_down"
            const val fish_nocatch_side = "fish_nocatch_side"
            const val fish_nocatch_up = "fish_nocatch_up"
            const val fishing_cast_down = "fishing_cast_down"
            const val fishing_cast_side = "fishing_cast_side"
            const val fishing_cast_up = "fishing_cast_up"
            const val fishing_idle_down = "fishing_idle_down"
            const val fishing_idle_side = "fishing_idle_side"
            const val fishing_idle_up = "fishing_idle_up"
            const val fishing_lag_down = "fishing_lag_down"
            const val fishing_lag_side = "fishing_lag_side"
            const val fishing_lag_up = "fishing_lag_up"
            const val fishing_pre_down = "fishing_pre_down"
            const val fishing_pre_side = "fishing_pre_side"
            const val fishing_pre_up = "fishing_pre_up"
            const val fishing_pst_down = "fishing_pst_down"
            const val fishing_pst_side = "fishing_pst_side"
            const val fishing_pst_up = "fishing_pst_up"
        }
    }
}