package com.Game.Blocks;

import com.Game.GamePhysicsConstants;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Block extends Actor {

    // Static Configurations
    private static final float PIXELS_PER_METER = GamePhysicsConstants.PPM;

    // Instance Variables
    private TextureRegion texture;
    private Body blockBody;
    private Vector2 initialPosition;

    private boolean destroyed;
    private int scoreValue;
    private int healthValue;
    private boolean isDestroyed;

    // Constructor
    public Block(TextureRegion texture, Vector2 initialPosition, World world) {
        validateArguments(texture, initialPosition);
        this.texture = texture;
        this.initialPosition = initialPosition;
        initializeActorDimensions(texture);
        setupPhysicsBody(world);
    }

    // Validation
    private void validateArguments(TextureRegion texture, Vector2 position) {
        if (texture == null) {
            throw new IllegalArgumentException("Texture cannot be null.");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
    }

    // Setup Methods
    private void initializeActorDimensions(TextureRegion texture) {
        setSize(texture.getRegionWidth(), texture.getRegionHeight());
    }

    private void setupPhysicsBody(World world) {
        blockBody = createBody(world);
        addPhysicsFixture(blockBody);
    }

    private Body createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((initialPosition.x + getWidth() / 2) / PIXELS_PER_METER,
            (initialPosition.y + getHeight() / 2) / PIXELS_PER_METER);
        return world.createBody(bodyDef);
    }

    private void addPhysicsFixture(Body body) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2) / PIXELS_PER_METER, (getHeight() / 2) / PIXELS_PER_METER);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.6f;
        fixtureDef.restitution = 0.1f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        shape.dispose();
    }


    // Act Method
    @Override
    public void act(float delta) {
        Vector2 bodyPosition = blockBody.getPosition();
        setPosition(bodyPosition.x * PIXELS_PER_METER - getWidth() / 2,
            bodyPosition.y * PIXELS_PER_METER - getHeight() / 2);
    }

    // Rendering
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) {
            batch.draw(texture,
                getX(), getY(),
                getWidth() / 2, getHeight() / 2,
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                (float) Math.toDegrees(blockBody.getAngle()));
        }
    }
    public Body getBlockBody() {
        return blockBody;
    }

    public void setBlockBody(Body blockBody) {
        this.blockBody = blockBody;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }


    public boolean isDestroyed() {
        return destroyed;
    }


    public Vector2 getPosition() {
        return blockBody.getPosition().cpy();
    }

    public float getWidth() {
        return getTexture().getRegionWidth();
    }

    public float getHeight() {
        return getTexture().getRegionHeight();
    }

    public BlockData toData() {
        return new BlockData(
            this.getClass().getSimpleName(),
            getX(),
            getY()
        );
    }
    public static Block fromData(BlockData data, World world) {
        Block block;
        if (data.getType().equals("GlassBlock")) {
            block = new GlassBlock(new Vector2(data.getX(), data.getY()), world);
        } else {
            throw new IllegalArgumentException("Unknown block type: " + data.getType());
        }
        return block;
    }
    public void dispose() {
        if (blockBody != null) {
            blockBody.getWorld().destroyBody(blockBody);
            blockBody = null;
        }
        if (texture != null && texture.getTexture() != null) {
            texture.getTexture().dispose();
            texture = null;
        }
    }
}
