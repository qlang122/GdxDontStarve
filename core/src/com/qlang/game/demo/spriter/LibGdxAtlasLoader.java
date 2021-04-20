package com.qlang.game.demo.spriter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.FileReference;
import com.brashmonkey.spriter.Loader;

import org.jetbrains.annotations.NotNull;

public class LibGdxAtlasLoader extends Loader<Sprite> {

    private TextureAtlas atlas;

    public LibGdxAtlasLoader(Data data, FileHandle atlas, String indexPrefix) {
        super(data);
        this.atlas = new TextureAtlas(atlas);
        Array<TextureAtlas.AtlasRegion> array = this.atlas.getRegions();
        for (int i = 0; i < array.size; i++) {
            TextureAtlas.AtlasRegion region = array.get(i);
            if (region.index != -1) region.name = region.name + indexPrefix + region.index;
        }
    }

    public LibGdxAtlasLoader(Data data, FileHandle atlas) {
        this(data, atlas, "_");
    }

    public LibGdxAtlasLoader(@NotNull Data data, @NotNull TextureAtlas atlas) {
        super(data);
        this.atlas = atlas;
        Array<TextureAtlas.AtlasRegion> array = this.atlas.getRegions();
        for (int i = 0; i < array.size; i++) {
            TextureAtlas.AtlasRegion region = array.get(i);
            if (region.index != -1) region.name = region.name + "_" + region.index;
        }
    }

    @Override
    protected Sprite loadResource(FileReference ref) {
        return this.atlas.createSprite((data.getFile(ref).name).replace(".png", ""));
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
    }
}
