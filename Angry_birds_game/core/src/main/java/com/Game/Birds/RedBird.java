package com.Game.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {
    private static Texture texture; // Shared texture for all RedBird instances
    private boolean abilityActivated = false;

    public RedBird(float x, float y, World world) {
        super(getTextureRegion(), new Vector2(x, y), world);
    }

    private static TextureRegion getTextureRegion() {
        if (texture == null) {
            texture = new Texture("Redbird.png");
        }
        return new TextureRegion(texture);
    }
    @Override
    public String getBirdType() {
        return "red";
    }

    public void activateAbility() {
        if (!abilityActivated) {
            abilityActivated = true;
            increaseSize();
        }
    }
    private void increaseSize() {
        Body body = getBirdBody();
        if (body != null) {
            float scaleFactor = 1.5f;
            setScale(getScaleX() * scaleFactor, getScaleY() * scaleFactor);
            body.getFixtureList().first().getShape().setRadius(body.getFixtureList().first().getShape().getRadius() * scaleFactor);
        }
    }
}
