package com.Game.Birds;

import com.Game.GamePhysicsConstants;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public abstract class Bird extends Actor {

    private static final float BIRD_SCALE =0.15f ;

    public abstract String getBirdType();


    public enum BirdState {
        IDLE, ON_LAUNCHER, FLYING
    }

    // Static Configurations
    private static final float PIXELS_PER_METER = GamePhysicsConstants.PPM;
    private static final float RADIUS_SCALE = 0.05f;
    private static final float MAX_SPEED = 22f;
    private static final float FORCE_SCALE = 0.6f;

    // Instance Variables
    protected Vector2 startPosition;
    protected TextureRegion texture;
    protected Body birdBody;
    protected BirdState currentState = BirdState.IDLE;

    private boolean isLaunched;
    private boolean birdOnCatapult;

    private boolean birdUsed;// Boolean flag to indicate if the bird is on the catapult

    // Constructor
    public Bird(TextureRegion texture, Vector2 initialPosition, World world) {
        validateArguments(texture, initialPosition);
        this.texture = texture;
        this.startPosition = initialPosition;
        this.birdOnCatapult = false;
        initializeActorDimensions(texture);
        setupPhysicsBody(world);
        setStaticState();
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
        birdBody = createBody(world);
        addPhysicsFixture(birdBody, calculateRadius());
    }

    private Body createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((startPosition.x + getWidth() / 2) / PIXELS_PER_METER,
            (startPosition.y + getHeight() / 2) / PIXELS_PER_METER);
        Body body = world.createBody(bodyDef);
        return body;
    }

    private void addPhysicsFixture(Body body, float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.2f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        shape.dispose();
    }
    public boolean isIdle() {
        return currentState == BirdState.IDLE;
    }
    protected float calculateRadius() {
        return (Math.min(getWidth(), getHeight()) * RADIUS_SCALE) / PIXELS_PER_METER;
    }

    // State Management
    public void setStaticState() {
        updateBodyState(BodyDef.BodyType.StaticBody, 0, false);
    }

    public void setDynamicState() {
        updateBodyState(BodyDef.BodyType.DynamicBody, 1, true);
    }

    private void updateBodyState(BodyDef.BodyType type, float gravityScale, boolean isAwake) {
        birdBody.setType(type);
        birdBody.setGravityScale(gravityScale);
        birdBody.setAwake(isAwake);
    }

    public void setState(BirdState newState) {
        this.currentState = newState;
    }

    public BirdState getState() {
        return currentState;
    }

    // Positioning
    public void resetPosition(float x, float y) {
        setPosition(x, y);
        birdBody.setTransform(x / PIXELS_PER_METER, y / PIXELS_PER_METER, 0);
    }

    public void prepareForLaunch(float x, float y) {
        resetPosition(x, y);
        setState(BirdState.ON_LAUNCHER);
    }

    public void returnToIdle(float x, float y) {
        resetPosition(x, y);
        setState(BirdState.IDLE);
    }

    // Launch and Movement
    public void launch(float forceX, float forceY) {
        if (currentState == BirdState.ON_LAUNCHER) {
            setState(BirdState.FLYING);
            applyLaunchForce(forceX, forceY);
        }
    }

    private void applyLaunchForce(float forceX, float forceY) {
        Vector2 scaledForce = new Vector2(forceX, forceY).scl(FORCE_SCALE);
        birdBody.applyLinearImpulse(scaledForce, birdBody.getWorldCenter(), true);
    }

    private Vector2 clampVelocity(Vector2 velocity) {
        if (velocity.len() > MAX_SPEED) {
            return velocity.nor().scl(MAX_SPEED);
        }
        return velocity;
    }

    @Override
    public void act(float delta) {
        if (currentState == BirdState.FLYING) {
            Vector2 velocity = clampVelocity(birdBody.getLinearVelocity());
            birdBody.setLinearVelocity(velocity);
        }

        Vector2 bodyPosition = birdBody.getPosition();
        setPosition(bodyPosition.x * PIXELS_PER_METER, bodyPosition.y * PIXELS_PER_METER);
    }

    // Rendering
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) {
            float originX = getWidth() / 2;
            float originY = getHeight() / 2;
            Vector2 screenPosition = convertToScreenCoordinates(birdBody.getPosition());
            batch.draw(texture, screenPosition.x, screenPosition.y,
                originX, originY,
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                (float) Math.toDegrees(birdBody.getAngle()));
        }
    }

    private Vector2 convertToScreenCoordinates(Vector2 worldPosition) {
        return new Vector2(
            worldPosition.x * PIXELS_PER_METER - getWidth() / 2,
            worldPosition.y * PIXELS_PER_METER - getHeight() / 2
        );
    }

    public List<Vector2> calculateTrajectory(float forceX, float forceY, int points, float interval) {
        List<Vector2> trajectoryPoints = new ArrayList<>();
        Vector2 velocity = new Vector2(forceX, forceY).scl(FORCE_SCALE);
        Vector2 position = birdBody.getPosition();

        for (int i = 0; i < points; i++) {
            float t = i * interval;
            float x = position.x + velocity.x * t;
            float y = position.y + velocity.y * t + 0.5f * GamePhysicsConstants.GRAVITY * t * t;
            trajectoryPoints.add(new Vector2(x * PIXELS_PER_METER, y * PIXELS_PER_METER));
        }

        return trajectoryPoints;
    }

    public Body getBirdBody() {
        return birdBody;
    }

    public void setBirdBody(Body birdBody) {
        this.birdBody = birdBody;
    }

    public BirdState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BirdState currentState) {
        this.currentState = currentState;
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Vector2 startPosition) {
        this.startPosition = startPosition;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void reset() {
        isLaunched = false;
        birdBody.setLinearVelocity(0, 0);
        birdBody.setAngularVelocity(0);
        resetPosition(startPosition.x, startPosition.y);
    }

    public void onHit() {
        this.reset();
    }

    public BirdData toData() {
        Vector2 position = birdBody.getPosition();
        Vector2 velocity = birdBody.getLinearVelocity();
        BirdData birdData = new BirdData(
            "RedBird",
            birdBody.getPosition().x,
            birdBody.getPosition().y,
            birdUsed,
            birdBody.getLinearVelocity().x,
            birdBody.getLinearVelocity().y,
            birdBody.getType().toString(),
            BIRD_SCALE,
            birdBody.isFixedRotation(),
            birdBody.getGravityScale()
        );

        birdData.setType("RedBird");
        birdData.setX(position.x);
        birdData.setY(position.y);
        birdData.setUsed(this.birdUsed);
        birdData.setVelocityX(velocity.x);
        birdData.setVelocityY(velocity.y);
        birdData.setBodyType(birdBody.getType().toString());
        birdData.setScale(BIRD_SCALE);
        return birdData;
    }


    public static Bird fromData(BirdData data, World world) {
        Bird bird;
        switch (data.getType()) {
            case "RedBird":
                bird = new RedBird(data.getX(), data.getY(), world);
                break;
            case "YellowBird":
                bird = new YellowBird(data.getX(), data.getY(), world);
                break;
            // Add cases for other bird types as necessary
            default:
                throw new IllegalArgumentException("Unknown bird type: " + data.getType());
        }

        // Restore properties
        bird.setBirdUsed(data.isUsed());
        bird.setScale(data.getScale());

        // Restore physics body properties
        Body body = bird.getBirdBody();
        if (body != null) {
            body.setType(BodyDef.BodyType.valueOf(data.getBodyType()));
            body.setGravityScale(data.getGravityScale());
            body.setFixedRotation(data.isFixedRotation());
            body.setLinearVelocity(new Vector2(data.getVelocityX(), data.getVelocityY()));
        }

        return bird;
    }


    public boolean isBirdOnCatapult() {
        return birdOnCatapult;
    }

    public void setBirdOnCatapult(boolean birdOnCatapult) {
        this.birdOnCatapult = birdOnCatapult;
    }

    public void setLaunched(boolean launched) {
        isLaunched = launched;
    }

    public void setBirdUsed(boolean birdUsed) {
        this.birdUsed = birdUsed;
    }
    public void dispose() {
        if (birdBody != null) {
            birdBody.getWorld().destroyBody(birdBody);
            birdBody = null;
        }
        if (texture != null && texture.getTexture() != null) {
            texture.getTexture().dispose();
            texture = null;
        }
    }
}
