package com.qlang.game.demo.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pools
import com.qlang.game.demo.entity.GameDate
import com.qlang.game.demo.utils.Log

class PlayHudClock : Widget {
    var style: ClockStyle? = null
        set(value) {
            field = value
            invalidate()
        }

    var date: GameDate = GameDate()
        set(value) {
            field = value
            invalidate()
        }

    private val dayTintColor = Color.valueOf("#ffbf14")
    private val eveningTintColor = Color.valueOf("#e0483c")
    private var needUpdateDay = true
    private val daytime: Int = 9
        get() = if (needUpdateDay) field else when (date.season) {
            GameDate.Season.SUMMER -> 11
            GameDate.Season.AUTUMN -> 9
            GameDate.Season.WENTER -> 7
            else -> 9
        }
    private val evening: Int = 4
        get() = if (needUpdateDay) field else when (date.season) {
            GameDate.Season.SUMMER -> 3
            GameDate.Season.AUTUMN -> 5
            GameDate.Season.WENTER -> 4
            else -> 5
        }

    private var prefWidth: Float = 0f
    private var prefHeight: Float = 0f
    private var textWidth: Float = 0f
    private var isOver = false

    constructor() : this(ClockStyle())

    constructor(skin: Skin) : this(skin.get(ClockStyle::class.java)) {
    }

    constructor(skin: Skin, styleName: String) : this(skin.get(styleName, ClockStyle::class.java)) {
    }

    constructor (style: ClockStyle) : super() {
        this.style = style
        setSize(getPrefWidth(), getPrefHeight())
    }

    override fun layout() {
        style?.background?.let {

            prefWidth = Math.max(prefWidth + it.leftWidth + it.rightWidth, it.minWidth)
            prefHeight = Math.max(prefHeight + it.topHeight + it.bottomHeight, it.minHeight)
        }

        val layoutPool = Pools.get(GlyphLayout::class.java)
        val layout = layoutPool.obtain()
        layout.setText(style?.font, "${date.day}天")
        textWidth = layout.width
        layoutPool.free(layout)
        Log.e("QL", style, prefWidth, prefHeight)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                isOver = true
                return true
            }

            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                isOver = true
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                isOver = false
            }
        })
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        invalidate()

        val color = color
        batch?.setColor(color.r, color.g, color.b, color.a * parentAlpha)

        val leftWidth = style?.background?.leftWidth ?: 0f
        val bottomHeight = style?.background?.bottomHeight ?: 0f
        val rightWidth = style?.background?.rightWidth ?: 0f
        val topHeight = style?.background?.topHeight ?: 0f

        style?.background?.draw(batch, x + leftWidth, y + bottomHeight, width - leftWidth - rightWidth, height - topHeight - bottomHeight)

        val oldColor = batch?.packedColor
        batch?.color = batch?.color?.mul(dayTintColor)
        for (i in 1 until daytime) {
            val degrees = i * 22.5f
            val wedge = style?.wedge
            if (wedge is TransformDrawable) {
                wedge.draw(batch, prefWidth / 2f, prefHeight / 2f, 0f, 0f,
                        width - leftWidth - rightWidth, height - topHeight - bottomHeight, 1f, 1f, degrees)
            } else wedge?.draw(batch, prefWidth / 2f, prefHeight / 2f, width - leftWidth - rightWidth, height - topHeight - bottomHeight)
        }
        batch?.packedColor = oldColor ?: 0f
        batch?.color = batch?.color?.mul(eveningTintColor)
        for (i in daytime until (daytime + evening)) {
            val degrees = i * 22.5f
            val wedge = style?.wedge
            if (wedge is TransformDrawable) {
                wedge.draw(batch, prefWidth / 2f, prefHeight / 2f, 0f, 0f,
                        width - leftWidth - rightWidth, height - topHeight - bottomHeight, 1f, 1f, degrees)
            } else wedge?.draw(batch, prefWidth / 2f, prefHeight / 2f, width - leftWidth - rightWidth, height - topHeight - bottomHeight)
        }
        batch?.packedColor = oldColor ?: 0f

        style?.rim?.draw(batch, x + leftWidth, y + bottomHeight, width - leftWidth - rightWidth, height - topHeight - bottomHeight)

        val hourDegrees = (date.hour * 15.0f + 15.0f * date.minute / 60) % 360
        val hand = style?.hand
        if (hand is TransformDrawable) {
            hand.draw(batch, prefWidth / 2f, prefHeight / 2f, hand.minWidth / 2f, hand.minHeight / 2f,
                    width, height, 1f, 1f, hourDegrees)
        } else hand?.draw(batch, prefWidth / 2f, prefHeight / 2f, width, height)

        if (isOver) {
            val str = "${date.day}天"
            style?.font?.draw(batch, str, prefWidth / 2f, prefHeight / 2f, 0, str.length, textWidth, Align.left, false)
        }
    }

    fun updateDay() {
        needUpdateDay = true
    }

    override fun getMinWidth(): Float {
        return 0f
    }

    override fun getMinHeight(): Float {
        return 0f
    }

    override fun getPrefWidth(): Float {
        return prefWidth
    }

    override fun getPrefHeight(): Float {
        return prefHeight
    }

    class ClockStyle {
        lateinit var font: BitmapFont
        var fontColor = Color(1f, 1f, 1f, 1f)
        var fontColorDown = Color(1f, 1f, 1f, 1f)
        var fontColorOver = Color(1f, 1f, 1f, 1f)
        lateinit var background: Drawable
        lateinit var hand: Drawable
        var rim: Drawable? = null
        var wedge: Drawable? = null

        constructor()

        constructor(font: BitmapFont, fontColor: Color, background: Drawable, hand: Drawable) {
            this.font = font
            this.fontColor.set(fontColor)
            this.background = background
            this.hand = hand
        }

        constructor(style: ClockStyle) {
            font = style.font
            fontColor.set(style.fontColor)
            fontColorDown.set(style.fontColorDown)
            fontColorOver.set(style.fontColorOver)
            background = style.background
            hand = style.hand
            rim = style.rim
            wedge = style.wedge
        }
    }
}