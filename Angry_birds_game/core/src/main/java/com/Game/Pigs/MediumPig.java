package com.Game.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MediumPig extends Pig {

    public MediumPig(Vector2 position, World world) {
        super(new TextureRegion(new Texture("mediumPig.png")), position, world);
        setSize(45, 45);  // Set the size of the MediumPig
    }

    public String getPigType() {
        return "medium";
    }
}
