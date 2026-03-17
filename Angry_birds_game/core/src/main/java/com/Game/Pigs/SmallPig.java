package com.Game.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class SmallPig extends Pig {

    public SmallPig(Vector2 position, World world) {
        super(new TextureRegion(new Texture("smallPig.png")), position, world);
        setSize(32, 32);
    }

}
