package com.Game.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class LargePig extends Pig {

    public LargePig(Vector2 position, World world) {
        super(new TextureRegion(new Texture("largePig.png")), position, world);
        setSize(64, 64);  // Set the size of the LargePig
    }

    public String getPigType() {
        return "large";
    }
}
