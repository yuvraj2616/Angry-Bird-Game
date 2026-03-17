package com.Game.Pigs;

import com.Game.GamePhysicsConstants;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import static com.Game.Levels.Level1.PIG_SCALE;

public class Pig extends Actor {

    // Pig states
    public enum PigState {
        IDLE, HIT, DESTROYED
    }

    // Static Configurations
    private static final float PIXELS_PER_METER = GamePhysicsConstants.PPM;
    private static final float RADIUS_SCALE = 0.04f;

    // Instance Variables
    protected Vector2 startPosition;
    protected TextureRegion texture;
    protected Body pigBody;
    protected PigState currentState = PigState.IDLE;

    private boolean destroyed;
    private int scoreValue;
    private int health;
    private List<Body> objectsToDestroy = new ArrayList<>();
// Health of the pig

    // Constructor
    public Pig(TextureRegion texture, Vector2 initialPosition, World world) {
        validateArguments(texture, initialPosition);
        this.texture = texture;
        this.startPosition = initialPosition;
        this.health = 20;  // Set initial health
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
        pigBody = createBody(world);
        addPhysicsFixture(pigBody, calculateRadius());
        pigBody.setUserData(this); // Set user data to identify the pig
    }

    private Body createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((startPosition.x + getWidth() / 2) / PIXELS_PER_METER,
            (startPosition.y + getHeight() / 2) / PIXELS_PER_METER);
        return world.createBody(bodyDef);
    }

    private void addPhysicsFixture(Body body, float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;

        Fixture fixture =body.createFixture(fixtureDef);
        fixture.setUserData(this);
        shape.dispose();
    }

    private float calculateRadius() {
        return (Math.min(getWidth(), getHeight()) * RADIUS_SCALE) / PIXELS_PER_METER;
    }

    // State Management
    public void setState(PigState newState) {
        this.currentState = newState;
    }

    public PigState getState() {
        return currentState;
    }

    // Position Management
    public void resetPosition(float x, float y) {
        setPosition(x, y);
        pigBody.setTransform(x / PIXELS_PER_METER, y / PIXELS_PER_METER, 0);
    }

    public void setDynamicState() {
        pigBody.setType(BodyDef.BodyType.DynamicBody);  // Set to dynamic body
        pigBody.setGravityScale(1.0f);  // Default gravity scale
        pigBody.setAwake(true);  // Make sure the pig responds to forces
    }

    // Logic
    @Override
    public void act(float delta) {
        if (currentState == PigState.HIT) {
            checkForDestruction();
        }

        Vector2 bodyPosition = pigBody.getPosition();
        setPosition(bodyPosition.x * PIXELS_PER_METER, bodyPosition.y * PIXELS_PER_METER);
    }

    private void checkForDestruction() {
        // If health is zero or less, transition to DESTROYED state
        if (health <= 0) {
            setState(PigState.DESTROYED);
            // Handle destruction (e.g., remove pig, play effects)
            destroyPig();
        }
    }



    // Destroy the pig (remove it from the game world)
    private void destroyPig() {
        if (!destroyed) {
            System.out.println("Destroying pig...");
            pigBody.getWorld().destroyBody(pigBody);
            destroyed = true;
            System.out.println("Pig destroyed. Removed from the world.");
            // Optionally, trigger other effects like score updates or animations here
        } else {
            System.out.println("Pig was already destroyed. Skipping destruction logic.");
        }
    }


    // Rendering
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) {
            float originX = getWidth() / 2;
            float originY = getHeight() / 2;
            Vector2 screenPosition = convertToScreenCoordinates(pigBody.getPosition());
            batch.draw(texture, screenPosition.x, screenPosition.y,
                originX, originY,
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                (float) Math.toDegrees(pigBody.getAngle()));
        }
    }

    private Vector2 convertToScreenCoordinates(Vector2 worldPosition) {
        return new Vector2(
            worldPosition.x * PIXELS_PER_METER - getWidth() / 2,
            worldPosition.y * PIXELS_PER_METER - getHeight() / 2
        );
    }

    public Body getPigBody() {
        return pigBody;
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

    // Apply damage to the pig when it is hit
    public void applyDamage(int damage) {
        if (currentState != PigState.DESTROYED) {
            health -= damage;  // Decrease health based on the damage
            System.out.println("Pig hit! Health reduced to: " + health);
            if (health <= 0) {
                System.out.println("Health is zero or below. Pig is being destroyed.");
                destroyed = true;
                setState(PigState.DESTROYED);  // Transition to DESTROYED state
                objectsToDestroy.add(pigBody);;
            } else {
                setState(PigState.HIT);  // Transition to HIT state
                System.out.println("Pig is still alive. Current state: HIT.");
            }
        } else {
            System.out.println("Pig is already destroyed. No further action taken.");
        }
    }


    public Vector2 getPosition() {
        return pigBody.getPosition().cpy();
    }

    public float getWidth() {
        return getTexture().getRegionWidth();
    }

    public float getHeight() {

        return getTexture().getRegionHeight();
    }

    public Rectangle getCollisionArea() {
        Vector2 position = getPosition();
        return new Rectangle(position.x - getWidth() / 2, position.y - getHeight() / 2, getWidth(), getHeight());
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.destroyed = true;
        }
    }

    public int getHealth() {
        return health;
    }


    public void setHealth(int health) {
        this.health = health;
    }
    public PigData toData() {
        Vector2 position = pigBody.getPosition();
        Vector2 velocity = pigBody.getLinearVelocity();
        String bodyType = pigBody.getType().name();
        return new PigData(
            this.getClass().getSimpleName(),
            position.x,
            position.y,
            this.health,
            velocity.x,
            velocity.y,
            bodyType
        );
    }
    public static Pig fromData(PigData data, World world) {
        Pig pig;
        if (data.getType().equals("MediumPig")) {
            pig = new MediumPig(new Vector2(data.getX(), data.getY()), world);
        } else if (data.getType().equals("SmallPig")) {
            pig = new SmallPig(new Vector2(data.getX(), data.getY()), world);
        } else {
            throw new IllegalArgumentException("Unknown pig type: " + data.getType());
        }
        pig.setHealth(data.getHealth());
        Body body = pig.getPigBody();
        body.setTransform(data.getX(), data.getY(), 0);
        body.setLinearVelocity(data.getVelocityX(), data.getVelocityY());
        body.setType(BodyDef.BodyType.valueOf(data.getBodyType()));
        pig.setScale(PIG_SCALE);
        return pig;
    }
    public void dispose() {
        if (pigBody != null) {
            pigBody.getWorld().destroyBody(pigBody);
            pigBody = null;
        }
        if (texture != null && texture.getTexture() != null) {
            texture.getTexture().dispose();
            texture = null;
        }
    }

}
