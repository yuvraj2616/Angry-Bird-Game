package com.Game.Levels;

import com.Game.Birds.Bird;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Slingshot {
    private Texture slingshotTexture;
    private Vector2 position;
    private float angle;
    private Body body;
    private Bird bird;
    private static final float WIDTH = 1.0f;
    private static final float HEIGHT = 3.0f;
    private static final float PPM = 100.0f;

    public Slingshot(World world, float x, float y) {
        this.position = new Vector2(x, y);
        this.slingshotTexture = new Texture(Gdx.files.internal("slingshot.png"));
        this.angle = 0;

        createBody(world);
    }

    private void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x / PPM, position.y / PPM); // Convert position to meters

        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, HEIGHT / 2); // Half-width and half-height in meters

        body.createFixture(shape, 0.0f);

        shape.dispose();
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(
            slingshotTexture,
            position.x - (WIDTH * PPM) / 2,
            position.y - (HEIGHT * PPM) / 2,
            WIDTH * PPM,
            HEIGHT * PPM
        );
        batch.end();
    }


    public void dispose() {
        slingshotTexture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Body getBody() {
        return body;
    }
}
