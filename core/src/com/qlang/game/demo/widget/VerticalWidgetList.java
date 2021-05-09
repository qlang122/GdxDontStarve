package com.qlang.game.demo.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ObjectSet;
import com.qlang.game.demo.utils.Log;

public class VerticalWidgetList<T extends Actor> extends ScrollPane {
    private InnerList<T> list = null;
    private boolean isShowSelection = true;

    public VerticalWidgetList() {
        this(new ScrollPaneStyle(), new WidgetListStyle());
    }

    public VerticalWidgetList(Skin skin, Skin listSkin) {
        this(skin.get(ScrollPaneStyle.class), listSkin.get(WidgetListStyle.class));
    }

    public VerticalWidgetList(Skin skin, String styleName, Skin listSkin, String listSkinName) {
        this(skin.get(styleName, ScrollPaneStyle.class), listSkin.get(listSkinName, WidgetListStyle.class));
    }

    public VerticalWidgetList(ScrollPaneStyle style, WidgetListStyle listStyle) {
        super(null, style);
        list = new InnerList<T>(listStyle, this);
        super.setActor(list);
        super.setScrollingDisabled(true, false);
        super.setSmoothScrolling(true);
    }

    @Override
    public void setActor(Actor actor) {
        super.setActor(list);
    }

    @Override
    public void setScrollingDisabled(boolean x, boolean y) {
        super.setScrollingDisabled(true, false);
    }

    @Null
    public InnerList<T> getContent() {
        return list;
    }

    public boolean isShowSelection() {
        return isShowSelection;
    }

    public void setShowSelection(boolean showSelection) {
        isShowSelection = showSelection;
    }

    static public class InnerList<T extends Actor> extends Widget implements Cullable {
        WidgetListStyle style;
        final Array<T> items = new Array<T>();
        ArraySelection<T> selection = new ArraySelection<T>(items);
        private Rectangle cullingArea;
        private float prefWidth, prefHeight;
        private int alignment = Align.left;
        int pressedIndex = -1, overIndex = -1;
        private final InputListener keyListener;
        boolean typeToSelect;

        private float oldScrollY;

        private final VerticalWidgetList parent;

        public InnerList(WidgetListStyle style, VerticalWidgetList parent) {
            this.parent = parent;

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
                        if (InnerList.this.toString(items.get(i)).toLowerCase().startsWith(prefix)) {
                            setSelectedIndex(i);
                            break;
                        }
                    }
                    return false;
                }
            });

            addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    oldScrollY = parent.getScrollY();
                    int index = getItemIndexAt(y);
                    if (!selection.isDisabled() && index >= 0) {
                        if (getStage() != null) getStage().setKeyboardFocus(InnerList.this);
                        pressedIndex = index;
                    }
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (pointer != 0 || button != 0) return;
                    if (Math.abs(Math.abs(parent.getScrollY()) - Math.abs(oldScrollY)) < 5) {
                        int size = items.size;
                        if (pressedIndex >= 0 && size > 0 && pressedIndex < size) {
                            selection.choose(items.get(pressedIndex));
                        }
                    }
                    pressedIndex = -1;
                    overIndex = -1;
                }

                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    overIndex = getItemIndexAt(y);
                }

                public boolean mouseMoved(InputEvent event, float x, float y) {
                    overIndex = getItemIndexAt(y);
                    return false;
                }

                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    if (pointer == 0) pressedIndex = -1;
                    if (pointer == -1) overIndex = -1;
                }
            });
        }

        public void setStyle(WidgetListStyle style) {
            if (style == null) throw new IllegalArgumentException("style cannot be null.");
            this.style = style;
            invalidateHierarchy();
        }

        /**
         * Returns the list's style. Modifying the returned style may not have an effect until {@link #setStyle(WidgetListStyle)} is
         * called.
         */
        public WidgetListStyle getStyle() {
            return style;
        }

        public void layout() {
            Drawable selectedDrawable = style.selection;

            prefWidth = 0;
            prefHeight = 0;
            for (int i = 0; i < items.size; i++) {
                T item = items.get(i);
                prefWidth = Math.max(item.getWidth(), prefWidth);
                prefHeight += item.getHeight() + selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();
            }
            prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();

            Drawable background = style.background;
            if (background != null) {
                prefWidth = Math.max(prefWidth + background.getLeftWidth() + background.getRightWidth(), background.getMinWidth());
                prefHeight = Math.max(prefHeight + background.getTopHeight() + background.getBottomHeight(), background.getMinHeight());
            }
            prefWidth = Math.max(prefWidth, parent.getPrefWidth());
        }

        public void draw(Batch batch, float parentAlpha) {
            validate();

            drawBackground(batch, parentAlpha);

            Drawable selectedDrawable = style.selection;

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            float x = getX(), y = getY(), width = getWidth(), height = getHeight();
            float itemY = height;

            Drawable background = style.background;
            if (background != null) {
                float leftWidth = background.getLeftWidth();
                x += leftWidth;
                itemY -= background.getTopHeight();
                width -= leftWidth + background.getRightWidth();
            }

            float padLeft = selectedDrawable.getLeftWidth();
//            float padRight = selectedDrawable.getRightWidth();
            float padTop = selectedDrawable.getTopHeight();
            float padBottom = selectedDrawable.getBottomHeight();
            itemY -= padTop + padBottom;

            for (int i = 0; i < items.size; i++) {
                T item = items.get(i);
                float itemHeight = item.getHeight();
                if (cullingArea == null || (itemY - itemHeight - padTop - padBottom <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
                    boolean selected = selection.contains(item);
                    Drawable drawable = null;
                    if (pressedIndex == i && style.down != null)
                        drawable = style.down;
                    else if (parent.isShowSelection && selected) {
                        drawable = selectedDrawable;
                    }
                    if (drawable != null)
                        drawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight + padTop + padBottom);
                    if (overIndex == i && style.over != null)
                        style.over.draw(batch, x, y + itemY - itemHeight, width, itemHeight + padTop + padBottom);
                    drawItem(batch, item, x + padLeft, y + itemY - itemHeight / 2 + padBottom, parentAlpha);
                } else if (itemY < cullingArea.y) {
                    break;
                }
                itemY -= itemHeight + padTop + padBottom;
            }
        }

        protected void drawItem(Batch batch, T item, float x, float y, float parentAlpha) {
            item.setPosition(x, y, alignment);
            item.draw(batch, parentAlpha);
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
        T getItemAt(float y) {
            int index = getItemIndexAt(y);
            if (index == -1) return null;
            return items.get(index);
        }

        /**
         * @return -1 if not over an item.
         */
        public int getItemIndexAt(float y) {
            Drawable background = this.style.background;
            if (background != null) {
                y -= background.getBottomHeight();
            }
            Drawable selectedDrawable = style.selection;
            float padTop = selectedDrawable.getTopHeight();
            float padBottom = selectedDrawable.getBottomHeight();

            y = prefHeight > parent.getHeight() ? (prefHeight - y) : (parent.getHeight() - y);

            float curr = 0f;
            for (int i = 0; i < items.size; i++) {
                T item = items.get(i);
                float iH = item.getHeight() + padTop + padBottom;
                if (y >= curr && y <= curr + iH) return i;
                curr += iH;
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
    }

    static public class WidgetListStyle {
        public Drawable selection;
        public @Null
        Drawable down, over, background;

        public WidgetListStyle() {
        }

        public WidgetListStyle(Drawable selection) {
            this.selection = selection;
        }

        public WidgetListStyle(WidgetListStyle style) {
            selection = style.selection;

            down = style.down;
            over = style.over;
            background = style.background;
        }
    }
}