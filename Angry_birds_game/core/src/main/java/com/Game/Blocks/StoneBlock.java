package com.Game.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class StoneBlock extends Block {

    public StoneBlock(Vector2 initialPosition, World world) {
        super(new TextureRegion(new Texture("stoneBlock.png")), initialPosition, world);
    }
}
