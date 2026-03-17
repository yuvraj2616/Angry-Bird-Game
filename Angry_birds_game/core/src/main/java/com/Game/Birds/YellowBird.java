package com.Game.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static com.Game.GamePhysicsConstants.PPM;

public class YellowBird extends Bird {
    private static Texture texture;
    private boolean hasBoosted;
    private static final float YELLOW_BIRD_RADIUS_SCALE = 0.03f;
    public YellowBird(float x, float y, World world) {
        super(getTextureRegion(), new Vector2(x, y), world);
    }

    private static TextureRegion getTextureRegion() {
        if (texture == null) {
            texture = new Texture("Yellowbird.png");
        }
        return new TextureRegion(texture);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (getState() == BirdState.FLYING && !hasBoosted && getY() > getStartPosition().y + 30) {
            speedBoost();
        }
    }


    public void speedBoost() {
        if (getState() == BirdState.FLYING && !hasBoosted) {
            System.out.println("Yellow bird activated speed boost!");

            Vector2 currentVelocity = getBirdBody().getLinearVelocity();

            Vector2 boostedVelocity = currentVelocity.scl(2);
            getBirdBody().setLinearVelocity(boostedVelocity);

            hasBoosted = true;
        }
    }

    @Override
    public String getBirdType() {
        return "yellow";
    }

    @Override
    protected float calculateRadius() {
        return (Math.min(getWidth(), getHeight()) * YELLOW_BIRD_RADIUS_SCALE) / PPM;
    }
}
