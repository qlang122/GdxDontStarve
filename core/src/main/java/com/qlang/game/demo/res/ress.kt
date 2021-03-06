package com.qlang.game.demo.res

object R {

    object anim {
        object player {
            const val idle = "anim/player-idles.scml"
            const val basic = "anim/player-basic.scml"

            const val wilson_atlas = "anim/player/wilson.atlas"

            const val swap_glassaxe_atlas = "anim/swap_object/swap_glassaxe.atlas"
            const val swap_glassaxe = "anim/swap_object/swap_glassaxe.scml"

            const val hat_spider_atlas = "orig/spriter_animations/hat_spider/hat_spider.atlas"
            const val hat_spider = "orig/spriter_animations/hat_spider/hat_spider.scml"

            fun atlas(): ArrayList<String> {
                val list = ArrayList<String>()
                for (it in this.javaClass.fields) {
                    val name = it?.name
                    if (name != null && name != "INSTANCE") {
                        it.isAccessible = true
                        it.get(this)?.toString()?.let { if (it.endsWith(".atlas")) list.add(it) }
                    }
                }
                return list
            }
        }

        object menu {
            const val base = "anim/menu/dst_menu.scml"
            const val base_atlas = "anim/menu/dst_menu.atlas"
            const val feast = "anim/menu/dst_menu_feast.scml"
            const val feast_atlas = "anim/menu/dst_menu_feast.atlas"
            const val feast_bg = "anim/menu/dst_menu_feast_bg.scml"
            const val feast_bg_atlas = "anim/menu/dst_menu_feast_bg.atlas"
            const val halloween = "anim/menu/dst_menu_halloween.scml"
            const val halloween_atlas = "anim/menu/dst_menu_halloween.atlas"
            const val lunacy = "anim/menu/dst_menu_lunacy.scml"
            const val lunacy_atlas = "anim/menu/dst_menu_lunacy.atlas"
        }

        object hud {
            const val health = "anim/hud/health.scml"
            const val health_atlas = "anim/hud/health.atlas"
            const val hunger = "anim/hud/hunger.scml"
            const val hunger_atlas = "anim/hud/hunger.atlas"
            const val sanity = "anim/hud/sanity.scml"
            const val sanity_atlas = "anim/hud/sanity.atlas"
            const val sanity_arrow = "anim/hud/sanity_arrow.scml"
            const val sanity_arrow_atlas = "anim/hud/sanity_arrow.atlas"
            const val wet_meter = "anim/hud/wet_meter.scml"
            const val wet_meter_atlas = "anim/hud/wet_meter.atlas"

            fun atlas(): ArrayList<String> {
                val list = ArrayList<String>()
                for (it in this.javaClass.fields) {
                    val name = it?.name
                    if (name != null && name != "INSTANCE") {
                        it.isAccessible = true
                        it.get(this)?.toString()?.let { if (it.endsWith(".atlas")) list.add(it) }
                    }
                }
                return list
            }
        }
    }

    object skin {
        const val hud = "atlas/images/hud.json"
        const val option_hud = "atlas/images/option_hud.json"
        const val ui = "atlas/images/ui.json"
        const val saveslot_portraits = "atlas/images/saveslot_portraits.json"
        const val server_intentions = "atlas/images/server_intentions.json"

        fun skins(): ArrayList<String> {
            val list = ArrayList<String>()
            for (it in this.javaClass.fields) {
                val name = it?.name
                if (name != null && name != "INSTANCE") {
                    it.isAccessible = true
                    it.get(this)?.toString()?.let { if (it.endsWith(".json")) list.add(it) }
                }
            }
            return list
        }
    }

    object image {
        const val bg_redux_bottom = "atlas/images/bg_redux_dark_bottom.atlas"
        const val bg_redux_bottom_solid = "atlas/images/bg_redux_dark_bottom_solid.atlas"
        const val bg_redux_bottom_over = "atlas/images/bg_redux_dark_bottom_vignette1.atlas"
        const val bg_redux_wardrobe_bg = "atlas/images/bg_redux_wardrobe_bg.atlas"
        const val bg_spiral_anim = "atlas/images/bg_spiral_anim.atlas"
        const val bg_spiral_anim_overlay = "atlas/images/bg_spiral_anim_overlay.atlas"

        const val customisation = "atlas/images/customisation.atlas"
        const val fepanels = "atlas/images/fepanels.atlas"
        const val fepanels_DSTbeta = "atlas/images/fepanels_DSTbeta.atlas"
        const val frontscreen = "atlas/images/frontscreen.atlas"
        const val fx = "atlas/images/fx.atlas"
        const val hud = "atlas/images/hud.atlas"
        const val option_hud = "atlas/images/option_hud.atlas"
        const val inventoryimages = "atlas/images/inventoryimages.atlas"
        const val saveslot_port = "atlas/images/saveslot_portraits.atlas"
        const val selectscreen_port = "atlas/images/selectscreen_portraits.atlas"
        const val ui = "atlas/images/ui.atlas"
        const val woodie = "atlas/images/woodie.atlas"
        const val globalpanels = "atlas/images/globalpanels.atlas"
        const val globalpanels2 = "atlas/images/globalpanels2.atlas"
        const val saveslot_portraits = "atlas/images/saveslot_portraits.atlas"
        const val minimap_atlas = "atlas/images/minimap_atlas.atlas"
        const val server_intentions = "atlas/images/server_intentions.atlas"

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
                    if (name != null && name != "INSTANCE") {
                        it.isAccessible = true
                        it.get(this)?.toString()?.let { if (it.endsWith(".atlas")) list.add(it) }
                    }
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
                    if (name != null && name != "INSTANCE") {
                        it.isAccessible = true
                        it.get(this)?.toString()?.let { if (it.endsWith(".atlas")) list.add(it) }
                    }
                }
                return list
            }
        }

        fun atlas(): ArrayList<String> {
            val list = ArrayList<String>()
            for (it in this.javaClass.fields) {
                val name = it?.name
                if (name != null && name != "INSTANCE") {
                    it.isAccessible = true
                    it.get(this)?.toString()?.let { if (it.endsWith(".atlas")) list.add(it) }
                }
            }
            return list
        }
    }

    object font {
        const val font_cn = "fonts/default-font-cn.fnt"
    }
}