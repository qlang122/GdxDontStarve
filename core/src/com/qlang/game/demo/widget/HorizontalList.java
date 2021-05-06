package com.qlang.game.demo.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

/**
 * A list (aka list box) displays textual items and highlights the currently selected item.
 * <p>
 * {@link ChangeListener.ChangeEvent} is fired when the list selection changes.
 * <p>
 * The preferred size of the list is determined by the text bounds of the items and the size of the {@link ListStyle#selection}.
 *
 * @author mzechner
 * @author Nathan Sweet
 */
public class HorizontalList<T extends Actor> extends Widget implements Cullable {
    ListStyle style;
    final Array<T> items = new Array<T>();
    ArraySelection<T> selection = new ArraySelection<T>(items);
    private Rectangle cullingArea;
    private float prefWidth, prefHeight;
    float itemWidth;
    private int alignment = Align.left;
    int pressedIndex = -1, overIndex = -1;
    private InputListener keyListener;
    boolean typeToSelect;

    public HorizontalList(Skin skin) {
        this(skin.get(ListStyle.class));
    }

    public HorizontalList(Skin skin, String styleName) {
        this(skin.get(styleName, ListStyle.class));
    }

    public HorizontalList(ListStyle style) {
        selection.setActor(this);
        selection.setRequired(true);

        setStyle(style);
        setSize(getPrefWidth(), getPrefHeight());

        addListener(keyListener = new InputListener() {
            long typeTimeout;
            String prefix;

            public boolean keyDown(InputEvent event, int keycode) {
                if (items.isEmpty()) return false;
                int index;
                switch (keycode) {
                    case Input.Keys.A:
                        if (UIUtils.ctrl() && selection.getMultiple()) {
                            selection.clear();
                            selection.addAll(items);
                            return true;
                        }
                        break;
                    case Input.Keys.HOME:
                        setSelectedIndex(0);
                        return true;
                    case Input.Keys.END:
                        setSelectedIndex(items.size - 1);
                        return true;
                    case Input.Keys.DOWN:
                        index = items.indexOf(getSelected(), false) + 1;
                        if (index >= items.size) index = 0;
                        setSelectedIndex(index);
                        return true;
                    case Input.Keys.UP:
                        index = items.indexOf(getSelected(), false) - 1;
                        if (index < 0) index = items.size - 1;
                        setSelectedIndex(index);
                        return true;
                    case Input.Keys.ESCAPE:
                        if (getStage() != null) getStage().setKeyboardFocus(null);
                        return true;
                }
                return false;
            }

            public boolean keyTyped(InputEvent event, char character) {
                if (!typeToSelect) return false;
                long time = System.currentTimeMillis();
                if (time > typeTimeout) prefix = "";
                typeTimeout = time + 300;
                prefix += Character.toLowerCase(character);
                for (int i = 0, n = items.size; i < n; i++) {
                    if (HorizontalList.this.toString(items.get(i)).toLowerCase().startsWith(prefix)) {
                        setSelectedIndex(i);
                        break;
                    }
                }
                return false;
            }
        });

        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return true;
                if (selection.isDisabled()) return true;
                if (getStage() != null) getStage().setKeyboardFocus(HorizontalList.this);
                if (items.size == 0) return true;
                int index = getItemIndexAt(x);
                if (index == -1) return true;
                selection.choose(items.get(index));
                pressedIndex = index;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != 0 || button != 0) return;
                pressedIndex = -1;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                overIndex = getItemIndexAt(x);
            }

            public boolean mouseMoved(InputEvent event, float x, float y) {
                overIndex = getItemIndexAt(x);
                return false;
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == 0) pressedIndex = -1;
                if (pointer == -1) overIndex = -1;
            }
        });
    }

    public void setStyle(ListStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    /**
     * Returns the list's style. Modifying the returned style may not have an effect until {@link #setStyle(ListStyle)} is
     * called.
     */
    public ListStyle getStyle() {
        return style;
    }

    public void layout() {
        Drawable selectedDrawable = style.selection;

        prefHeight = 0;

        for (int i = 0; i < items.size; i++) {
            T t = items.get(i);
            prefHeight = Math.max(t.getHeight(), prefHeight);
            itemWidth = Math.max(t.getWidth(), itemWidth);
        }
        prefWidth = items.size * itemWidth;
        prefHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();

        Drawable background = style.background;
        if (background != null) {
            prefWidth = Math.max(prefWidth + background.getLeftWidth() + background.getRightWidth(), background.getMinWidth());
            prefHeight = Math.max(prefHeight + background.getTopHeight() + background.getBottomHeight(), background.getMinHeight());
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        validate();

        drawBackground(batch, parentAlpha);

        Drawable selectedDrawable = style.selection;

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        float x = getX(), y = getY();

        Drawable background = style.background;
        if (background != null) {
            x += background.getLeftWidth();
        }

        float padLeft = selectedDrawable.getLeftWidth();
        float padRight = selectedDrawable.getRightWidth();
//        float padTop = selectedDrawable.getTopHeight();
        float padBottom = selectedDrawable.getBottomHeight();

        for (int i = 0; i < items.size; i++) {
            T item = items.get(i);
            if (cullingArea == null || (x + itemWidth <= cullingArea.x + cullingArea.width && x >= cullingArea.x)) {
                boolean selected = selection.contains(item);
                Drawable drawable = null;
                if (pressedIndex == i && style.down != null)
                    drawable = style.down;
                else if (selected) {
                    drawable = selectedDrawable;
                } else if (overIndex == i && style.over != null) //
                    drawable = style.over;
                if (drawable != null)
                    drawable.draw(batch, x, y, itemWidth + padLeft + padRight, prefHeight);
                drawItem(batch, item, x + padLeft, y + prefHeight / 2f, parentAlpha);
            } else if (x < cullingArea.x) {
                break;
            }
            x += itemWidth + padLeft + padRight;
        }
    }

    /**
     * Called to draw the background. Default implementation draws the style background drawable.
     */
    protected void drawBackground(Batch batch, float parentAlpha) {
        if (style.background != null) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }

    protected void drawItem(Batch batch, T item, float x, float y, float parentAlpha) {
        item.setPosition(x, y, alignment);
        item.draw(batch, parentAlpha);
    }

    public ArraySelection<T> getSelection() {
        return selection;
    }

    public void setSelection(ArraySelection<T> selection) {
        this.selection = selection;
    }

    /**
     * Returns the first selected item, or null.
     */
    public @Null
    T getSelected() {
        return selection.first();
    }

    /**
     * Sets the selection to only the passed item, if it is a possible choice.
     *
     * @param item May be null.
     */
    public void setSelected(@Null T item) {
        if (items.contains(item, false))
            selection.set(item);
        else if (selection.getRequired() && items.size > 0)
            selection.set(items.first());
        else
            selection.clear();
    }

    /**
     * @return The index of the first selected item. The top item has an index of 0. Nothing selected has an index of -1.
     */
    public int getSelectedIndex() {
        ObjectSet<T> selected = selection.items();
        return selected.size == 0 ? -1 : items.indexOf(selected.first(), false);
    }

    /**
     * Sets the selection to only the selected index.
     *
     * @param index -1 to clear the selection.
     */
    public void setSelectedIndex(int index) {
        if (index < -1 || index >= items.size)
            throw new IllegalArgumentException("index must be >= -1 and < " + items.size + ": " + index);
        if (index == -1) {
            selection.clear();
        } else {
            selection.set(items.get(index));
        }
    }

    /**
     * @return May be null.
     */
    public T getOverItem() {
        return overIndex == -1 ? null : items.get(overIndex);
    }

    /**
     * @return May be null.
     */
    public T getPressedItem() {
        return pressedIndex == -1 ? null : items.get(pressedIndex);
    }

    /**
     * @return null if not over an item.
     */
    public @Null
    T getItemAt(float x) {
        int index = getItemIndexAt(x);
        if (index == -1) return null;
        return items.get(index);
    }

    /**
     * @return -1 if not over an item.
     */
    public int getItemIndexAt(float x) {
        Drawable background = style.background;
        if (background != null) {
            x -= background.getLeftWidth();
        }

        Drawable selectedDrawable = style.selection;
        float padLeft = selectedDrawable.getLeftWidth();
        float padRight = selectedDrawable.getRightWidth();

        float curr = 0f;

        for (int i = 0; i < items.size; i++) {
            float iW = itemWidth + padLeft + padRight;
            if (x >= curr && x <= curr + iW) return i;
            curr += iW;
        }
        return -1;
    }

    public void setItems(T... newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        items.clear();
        items.addAll(newItems);
        overIndex = -1;
        pressedIndex = -1;
        selection.validate();

        invalidate();
        if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight())
            invalidateHierarchy();
    }

    /**
     * Sets the items visible in the list, clearing the selection if it is no longer valid. If a selection is
     * {@link ArraySelection#getRequired()}, the first item is selected. This can safely be called with a (modified) array returned
     * from {@link #getItems()}.
     */
    public void setItems(Array newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        if (newItems != items) {
            items.clear();
            items.addAll(newItems);
        }
        overIndex = -1;
        pressedIndex = -1;
        selection.validate();

        invalidate();
        if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight())
            invalidateHierarchy();
    }

    public void clearItems() {
        if (items.size == 0) return;
        items.clear();
        overIndex = -1;
        pressedIndex = -1;
        selection.clear();
        invalidateHierarchy();
    }

    /**
     * Returns the internal items array. If modified, {@link #setItems(Array)} must be called to reflect the changes.
     */
    public Array<T> getItems() {
        return items;
    }

    public float getItemWidth() {
        return itemWidth;
    }

    public float getPrefWidth() {
        validate();
        return prefWidth;
    }

    public float getPrefHeight() {
        validate();
        return prefHeight;
    }

    public String toString(T object) {
        return object.toString();
    }

    public void setCullingArea(@Null Rectangle cullingArea) {
        this.cullingArea = cullingArea;
    }

    /**
     * @return May be null.
     * @see #setCullingArea(Rectangle)
     */
    public Rectangle getCullingArea() {
        return cullingArea;
    }

    /**
     * Sets the horizontal alignment of the list items.
     *
     * @param alignment See {@link Align}.
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public void setTypeToSelect(boolean typeToSelect) {
        this.typeToSelect = typeToSelect;
    }

    public InputListener getKeyListener() {
        return keyListener;
    }

    /**
     * The style for a list, see {@link HorizontalList}.
     *
     * @author mzechner
     * @author Nathan Sweet
     */
    static public class ListStyle {
        public Drawable selection;
        public @Null
        Drawable down, over, background;

        public ListStyle() {
        }

        public ListStyle(Drawable selection) {
            this.selection = selection;
        }

        public ListStyle(ListStyle style) {
            selection = style.selection;

            down = style.down;
            over = style.over;
            background = style.background;
        }
    }
}
