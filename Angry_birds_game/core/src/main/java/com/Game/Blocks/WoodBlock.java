package com.Game.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WoodBlock extends Block {

    public WoodBlock(Vector2 initialPosition, World world) {
        super(new TextureRegion(new Texture("woodBlock.png")), initialPosition, world);
    }
}
