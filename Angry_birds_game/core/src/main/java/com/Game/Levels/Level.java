package com.Game.Levels;

import com.Game.Birds.*;
import com.Game.Blocks.BlockData;
import com.Game.Birds.BirdData;
import com.Game.Pigs.PigData;
import com.Game.Levels.LevelData;
import com.Game.Core;
import com.Game.MyContactListener;
import com.Game.Pigs.Pig;
import com.Game.Blocks.Block;
import com.Game.Pigs.PigData;
import com.Game.Screens.LoseScreen;
import com.Game.Screens.PauseScreen;
import com.Game.Screens.WinScreen;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.List;

public abstract class Level implements Screen {

    // Game & Rendering
    protected final Core game;
    protected final OrthographicCamera camera;
    protected final SpriteBatch batch;
    protected final Stage stage;
    protected final ShapeRenderer shapeRenderer;

    // Physics
    protected final World world;
    protected final Box2DDebugRenderer debugRenderer;

    // Assets
    protected Texture backgroundTexture;
    protected Texture catapultTexture;
    protected Texture redBirdTexture;
    protected Texture yellowBirdTexture;
    protected Texture MediumPigTexture;
    protected Texture groundTexture;

    // Game Objects
    protected Vector2 catapultPosition;
    protected Vector2[] birdWaitingPositions;
    protected boolean[] birdUsed;
    protected List<Bird> birds;
    protected List<Pig> pigs;
    protected List<Block> blocks;
    protected Array<Body> bodiesToDestroy;

    // Game State
    protected int currentBirdIndex = 0;
    protected static boolean isBirdInFlight = false;
    protected Bird birdOnCatapult;
    protected ArrayList<Vector2> trajectoryPoints;

    // UI
    protected Slider powerSlider, angleSlider;
    protected Label powerLabel, angleLabel;
    protected TextButton launchButton, pauseButton;

    // Constants
    public static final float PIG_SCALE = 0.07f;
    protected static final float PPM = 100f;
    protected static final float WORLD_WIDTH = 800f;
    protected static final float WORLD_HEIGHT = 480f;
    protected static final float SCALE_FACTOR = 0.15f;
    protected static final float BIRD_SCALE = 0.15f;
    protected static final float BLOCK_SCALE = 1.0f;
    protected static final float CATAPULT_X = 100f;
    protected static final float CATAPULT_Y = 55f;
    protected static final float CATAPULT_SCALE = 0.2f;
    protected static final float CATAPULT_HEAD_X = CATAPULT_X + 20f;
    protected static final float CATAPULT_HEAD_Y = CATAPULT_Y + 60f;
    protected static final float GROUND_Y = 100f;
    protected static final float MAX_LAUNCH_POWER = 7f;
    protected static final float LAUNCH_POWER_MULTIPLIER = 1f;

    public Level(Core game) {
        this.game = game;

        // Initialize rendering and physics components
        camera = new OrthographicCamera();
        stage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera));
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();

        Gdx.input.setInputProcessor(stage);

        // Initialize world and game objects
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new MyContactListener());
        bodiesToDestroy = new Array<>();
        catapultPosition = new Vector2(CATAPULT_X, CATAPULT_Y);

        setupCamera();
        loadAssets();
        initializeBirdPositions();
        setupGameObjects();
        createUI();
        createBoundaries();
    }

    private void setupCamera() {
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
    }

    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("Sky.png"));
        catapultTexture = new Texture(Gdx.files.internal("slingshot.png"));
        redBirdTexture = new Texture(Gdx.files.internal("RedBird.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("YellowBird.png"));
        MediumPigTexture = new Texture(Gdx.files.internal("SmallPig.png"));
        groundTexture = new Texture(Gdx.files.internal("floor.png"));
    }

    private void initializeBirdPositions() {
        birdWaitingPositions = new Vector2[]{
            new Vector2(0, 0),
            new Vector2(50, 0),
            new Vector2(100, 0)
        };

        birdUsed = new boolean[3];
        Arrays.fill(birdUsed, false);
    }
    public void selectBird(int index) {
        System.out.println("selectBird called with index: " + index); // Debugging statement
        if (index < birds.size() && !birdUsed[index]) {
            birdOnCatapult = birds.get(index);
            birdUsed[index] = true;

            Body body = birdOnCatapult.getBirdBody();
            if (body != null) {
                body.setType(BodyDef.BodyType.StaticBody);
                body.setTransform(CATAPULT_HEAD_X / PPM, 180f / PPM, 0);
            }

            birdOnCatapult.setPosition(CATAPULT_HEAD_X - birdOnCatapult.getWidth() / 2, CATAPULT_HEAD_Y);
            System.out.println("Bird placed on catapult at position: (" + birdOnCatapult.getX() + ", " + birdOnCatapult.getY() + ")"); // Debugging statement

            // Update the trajectory
            updateTrajectory();
        } else {
            System.out.println("Bird index out of range or bird already used."); // Debugging statement
        }
    }
    private void createBoundaries() {
        EdgeShape edgeShape = new EdgeShape();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = edgeShape;
        fixtureDef.friction = 0.3f;

        BodyDef boundaryDef = new BodyDef();
        boundaryDef.type = BodyDef.BodyType.StaticBody;

        // Left and right walls
        createBoundary(boundaryDef, edgeShape, fixtureDef, 0, WORLD_HEIGHT); // Left
        createBoundary(boundaryDef, edgeShape, fixtureDef, WORLD_WIDTH / PPM, WORLD_HEIGHT); // Right

        // Upward boundary (top)
        createHorizontalBoundary(boundaryDef, edgeShape, fixtureDef, WORLD_HEIGHT / PPM);

        // Downward boundary (bottom)
        createHorizontalBoundary(boundaryDef, edgeShape, fixtureDef, 0);

        edgeShape.dispose();
    }

    private void createBoundary(BodyDef def, EdgeShape shape, FixtureDef fixture, float x, float height) {
        shape.set(new Vector2(x, 0), new Vector2(x, height / PPM));
        world.createBody(def).createFixture(fixture);
    }

    private void createHorizontalBoundary(BodyDef def, EdgeShape shape, FixtureDef fixture, float y) {
        shape.set(new Vector2(0, y), new Vector2(WORLD_WIDTH / PPM, y));
        world.createBody(def).createFixture(fixture);
    }

    protected abstract void setupBirds();
    protected abstract void setupTowerWithPig(float towerX, float pigY);

    protected abstract void setupGameObjects();



    protected void createUI() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Pause Button
        pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(10, WORLD_HEIGHT - 50); // Top left corner
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game, Level.this)); // Pass the current level
            }
        });
        stage.addActor(pauseButton);

        // Power Slider
        // Create a custom LabelStyle with black font color
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font"); // Use the default font from your skin
        labelStyle.fontColor = Color.BLACK; // Set the font color to black

// Power Label
        powerLabel = new Label("Power", labelStyle); // Use the custom style
        powerLabel.setPosition(10, WORLD_HEIGHT - 100); // Adjust position below the pause button
        stage.addActor(powerLabel);

        powerSlider = new Slider(0, MAX_LAUNCH_POWER, 1, false, skin);
        powerSlider.setPosition(100, WORLD_HEIGHT - 100); // Adjust position below the pause button
        stage.addActor(powerSlider);

// Angle Label
        angleLabel = new Label("Angle", labelStyle); // Use the custom style
        angleLabel.setPosition(10, WORLD_HEIGHT - 150); // Adjust position below the power slider
        stage.addActor(angleLabel);

        angleSlider = new Slider(0, 90, 1, false, skin);
        angleSlider.setPosition(100, WORLD_HEIGHT - 150); // Adjust position below the power slider
        stage.addActor(angleSlider);


        // Launch Button
        launchButton = new TextButton("Launch", skin);
        launchButton.setPosition(100, WORLD_HEIGHT - 200); // Adjust position below the angle slider
        launchButton.addListener(new LaunchClickListener());
        stage.addActor(launchButton);

        setupSliderListeners();

        setupSliderListeners();
    }

    private void setupSliderListeners() {
        powerSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateTrajectory();
            }
        });

        angleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateTrajectory();
            }
        });
    }

    public CharSequence getBirds() {
        return null;
    }

    public CharSequence getPigs() {
        return null;
    }

    public CharSequence getBlocks() {
        return null;
    }


    public class LaunchClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println("Launch button clicked."); // Debugging statement
            if (!isBirdInFlight) {
                launchBird();
            } else {
                System.out.println("Bird is already in flight."); // Debugging statement
            }
        }
    }
    public class BirdClickListener extends ClickListener {
        int birdIndex;

        BirdClickListener(int index) {
            birdIndex = index;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println("Bird clicked with index: " + birdIndex); // Debugging statement
            selectBird(birdIndex);
        }
    }
    protected void launchBird() {
        if (birdOnCatapult != null) {
            System.out.println("Launching bird..."); // Debugging statement

            // Change the bird's body type to DynamicBody to launch it
            Body body = birdOnCatapult.getBirdBody();
            if (body != null) {
                body.setType(BodyDef.BodyType.DynamicBody);

                // Calculate the launch force based on angle and power
                float angle = angleSlider.getValue();
                float power = powerSlider.getValue();
                float forceX = power * (float) Math.cos(Math.toRadians(angle));
                float forceY = power * (float) Math.sin(Math.toRadians(angle));

                // Apply the force to the bird
                body.applyLinearImpulse(new Vector2(forceX, forceY), body.getWorldCenter(), true);
                System.out.println("Bird launched with force: (" + forceX + ", " + forceY + ")"); // Debugging statement
            }

            isBirdInFlight = true;
        } else {
            System.out.println("No bird on catapult to launch."); // Debugging statement
        }
    }

    private void updateTrajectory() {
        if (birdOnCatapult != null) {
            trajectoryPoints = new ArrayList<>();

            // Parameters for the trajectory
            float launchAngle = angleSlider.getValue(); // Get angle from the slider
            float power = powerSlider.getValue(); // Get power from the slider

            // Convert angle to radians
            float angleInRadians = (float) Math.toRadians(launchAngle);

            // Initial velocity components
            float initialVelocityX = power * (float) Math.cos(angleInRadians);
            float initialVelocityY = power * (float) Math.sin(angleInRadians);

            // Gravity (in meters per second squared)
            final float gravity = -9.8f;

            // Calculate the trajectory points
            for (float t = 0; t < 2 * initialVelocityY / gravity; t += 0.1f) {
                // Calculate the position at time t
                float x = CATAPULT_HEAD_X + initialVelocityX * t;
                float y = CATAPULT_HEAD_Y + initialVelocityY * t + 0.5f * gravity * t * t;

                // If the bird goes below the ground, stop calculating further points
                if (y < GROUND_Y) break;

                trajectoryPoints.add(new Vector2(x, y));
            }

            // Optionally, render the trajectory using debug or other rendering methods
            renderTrajectory();
        }
    }

    private void renderTrajectory() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red color for the trajectory

        for (int i = 0; i < trajectoryPoints.size() - 1; i++) {
            Vector2 start = trajectoryPoints.get(i);
            Vector2 end = trajectoryPoints.get(i + 1);
            shapeRenderer.line(start.x, start.y, end.x, end.y);
        }

        shapeRenderer.end();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        // Step the physics world
        world.step(1 / 60f, 6, 2);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw the slingshot
        batch.draw(catapultTexture, CATAPULT_X, CATAPULT_Y,
            catapultTexture.getWidth() * CATAPULT_SCALE, catapultTexture.getHeight() * CATAPULT_SCALE);

        // Draw the bird on the catapult if present
        if (birdOnCatapult != null) {
            birdOnCatapult.draw(batch, 1f);
        }

        // Draw pigs
        for (Pig pig : pigs) {
            pig.draw(batch, 1f);
        }

        // Draw blocks
        for (Block block : blocks) {
            block.draw(batch, 1f);
        }

        batch.end();

        debugRenderer.render(world, camera.combined.scl(PPM));

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (isBirdInFlight && birdOnCatapult != null) {
            Body body = birdOnCatapult.getBirdBody();
            if (body != null) {
                Vector2 velocity = body.getLinearVelocity();
                if (velocity.len() < 0.1f) { // Threshold for stopped bird
                    bodiesToDestroy.add(body);
                    birdOnCatapult.remove();
                    birdOnCatapult = null;
                    isBirdInFlight = false;
                    System.out.println("Bird stopped and removed from the game.");
                }
            }
        }

        // Collect pigs to remove
        List<Pig> pigsToRemove = new ArrayList<>();

        for (Pig pig : pigs) {
            if (pig.isDestroyed()) {
                bodiesToDestroy.add(pig.getPigBody());
                pig.remove(); // Remove from stage
                pigsToRemove.add(pig); // Add to removal list
                System.out.println("Pig removed from the game.");
            }
        }

        pigs.removeAll(pigsToRemove);

        List<Block> blocksToRemove = new ArrayList<>();

        for (Block block : blocks) {
            if (block.isDestroyed()) {
                bodiesToDestroy.add(block.getBlockBody());
                block.remove();
                blocksToRemove.add(block);
                System.out.println("Block removed from the game.");
            }
        }

        blocks.removeAll(blocksToRemove);

        for (Body body : bodiesToDestroy) {
            world.destroyBody(body);
        }
        bodiesToDestroy.clear();

        checkLevelClear();
        checkLevelFailed();
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (birdOnCatapult instanceof RedBird) {
                ((RedBird) birdOnCatapult).activateAbility();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (birdOnCatapult instanceof YellowBird) {
                ((YellowBird) birdOnCatapult).speedBoost();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (birdOnCatapult instanceof BlueBird) {
                ((BlueBird) birdOnCatapult).split();
            }
        }
    }
    private void checkLevelClear() {
        if (pigs.isEmpty()) {
            System.out.println("Level Cleared!");
            game.setScreen(new WinScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        catapultTexture.dispose();
        redBirdTexture.dispose();
        yellowBirdTexture.dispose();
        MediumPigTexture.dispose();
        groundTexture.dispose();
        world.dispose();
        debugRenderer.dispose();
        stage.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }

    private void showLevelClearMessage() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Dialog dialog = new Dialog("Level Cleared!", skin);
        dialog.text("Congratulations! You have cleared the level.");
        dialog.button("OK");
        dialog.show(stage);
    }

    private void checkLevelFailed() {
        boolean allBirdsUsed = true;
        for (boolean used : birdUsed) {
            if (!used) {
                allBirdsUsed = false;
                break;
            }
        }
        boolean birdOnCatapult = this.birdOnCatapult != null;
        if (allBirdsUsed && !pigs.isEmpty() && !birdOnCatapult) {
            System.out.println("Level Failed!");
            game.setScreen(new LoseScreen(game));
        }
    }


    public void showLevelFailedMessage() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Dialog dialog = new Dialog("Level Failed!", skin);
        dialog.text("You have used all birds without destroying all pigs.");
        dialog.button("Retry");
        dialog.show(stage);
    }

    public void saveLevel(String filename) {
        // Convert game objects to data objects
        List<BirdData> birdDataList = new ArrayList<>();
        for (Bird bird : birds) {
            birdDataList.add(bird.toData());
        }

        List<PigData> pigDataList = new ArrayList<>();
        for (Pig pig : pigs) {
            pigDataList.add(pig.toData());
        }

        List<BlockData> blockDataList = new ArrayList<>();
        for (Block block : blocks) {
            blockDataList.add(block.toData());
        }

        // Capture additional game state
        int currentBird = currentBirdIndex;
        boolean inFlight = isBirdInFlight;
        BirdData birdOnCatapultData = null;
        if (birdOnCatapult != null) {
            birdOnCatapultData = birdOnCatapult.toData();
        }

        // Include birdUsed array
        LevelData levelData = new LevelData(birdDataList, pigDataList, blockDataList,
            currentBird, inFlight, birdOnCatapultData,
            birdUsed);

        // Serialize LevelData to file
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(levelData);
            System.out.println("Level saved to " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadLevel(String filename) {
        LevelData levelData;

        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            levelData = (LevelData) in.readObject();
            System.out.println("Level loaded from " + filename);

            clearGameObjects();
            System.out.println("Existing game objects cleared.");

            // Restore birdUsed array
            this.birdUsed = levelData.getBirdUsed();

            // Load and initialize birds
            List<BirdData> birdDataList = levelData.getBirds();
            for (int i = 0; i < birdDataList.size(); i++) {
                BirdData birdData = birdDataList.get(i);
                if (!birdUsed[i]) { // Only add if the bird hasn't been used
                    Bird bird = Bird.fromData(birdData, world);
                    birds.add(bird);
                    stage.addActor(bird);
                    System.out.println("Added bird: " + birdData.getType());

                    // Attach listeners
                    int birdIndex = birds.indexOf(bird);
                    bird.addListener(new BirdClickListener(birdIndex));


                    Body body = bird.getBirdBody();
                    if (body != null) {
                        body.setType(BodyDef.BodyType.DynamicBody); // Affected by gravity
                        body.setGravityScale(birdData.getGravityScale()); // As per saved data
                        body.setFixedRotation(birdData.isFixedRotation()); // Prevent rotation if needed
                        body.setLinearVelocity(new Vector2(birdData.getVelocityX(), birdData.getVelocityY())); // Restore velocity
                    }

                    // If this bird is the bird on catapult
                    if (levelData.getBirdOnCatapultData() != null) {
                        BirdData savedBirdOnCatapultData = levelData.getBirdOnCatapultData();
                        if (birdData.getType().equals(savedBirdOnCatapultData.getType()) &&
                            birdData.getX() == savedBirdOnCatapultData.getX() &&
                            birdData.getY() == savedBirdOnCatapultData.getY()) {
                            this.birdOnCatapult = bird;
                            this.isBirdInFlight = levelData.isBirdInFlight();
                        }
                    }
                    bird.setScale(BIRD_SCALE * 0.5f);
                    body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y), body.getAngle());
                } else {
                    System.out.println("Bird " + i + " has already been used.");
                }


            }

            // Load and initialize pigs
            for (PigData pigData : levelData.getPigs()) {
                Pig pig = Pig.fromData(pigData, world);
                pigs.add(pig);
                stage.addActor(pig);
                System.out.println("Added pig: " + pigData.getType());

                // Set pig's physics body properties
                Body pigBody = pig.getPigBody();
                if (pigBody != null) {
                    pigBody.setType(BodyDef.BodyType.DynamicBody); // Affected by gravity
                    pigBody.setGravityScale(pigData.getGravityScale()); // As per saved data
                    pigBody.setFixedRotation(pigData.isFixedRotation()); // Prevent rotation if needed
                    pigBody.setLinearVelocity(new Vector2(pigData.getVelocityX(), pigData.getVelocityY())); // Restore velocity
                }
                pig.setScale(PIG_SCALE * 2);
            }

            // Load and initialize blocks
            List<BlockData> blockDataList = levelData.getBlocks();
            System.out.println("Number of blocks to load: " + blockDataList.size()); // Debugging
            for (BlockData blockData : blockDataList) {
                Block block = Block.fromData(blockData, world);
                if (block != null) {
                    blocks.add(block);
                    stage.addActor(block);
                    System.out.println("Added block: " + blockData.getType());

                    // Set block's physics body properties
                    Body blockBody = block.getBlockBody();
                    if (blockBody != null) {
                        blockBody.setType(BodyDef.BodyType.DynamicBody); // Affected by gravity
                        blockBody.setGravityScale(blockData.getGravityScale()); // As per saved data
                        blockBody.setFixedRotation(blockData.isFixedRotation()); // Prevent rotation if needed
                        blockBody.setLinearVelocity(new Vector2(blockData.getVelocityX(), blockData.getVelocityY())); // Restore velocity
                    }

                    // Restore scale
                    block.setScale(blockData.getScale());
                } else {
                    System.out.println("Failed to create block from data: " + blockData.getType());
                }
            }

            // Restore additional game state
            this.currentBirdIndex = levelData.getCurrentBirdIndex();
            this.isBirdInFlight = levelData.isBirdInFlight();

            // If there is a bird on the catapult, ensure it's correctly positioned
            if (levelData.getBirdOnCatapultData() != null && this.birdOnCatapult != null) {
                BirdData birdOnCatapultData = levelData.getBirdOnCatapultData();
                birdOnCatapult.setPosition(birdOnCatapultData.getX(), birdOnCatapultData.getY());
                birdOnCatapult.setScale(birdOnCatapultData.getScale());
                // Optionally, snap the bird to the catapult position visually
            }

            System.out.println("Level loaded and reconstructed successfully.");

        } catch (Exception e) {
            System.err.println("Error loading level:");
            e.printStackTrace();
        }
    }
    private void clearGameObjects() {
        for (Bird bird : birds) {
            bird.remove(); // Remove from Stage
            Body body = bird.getBirdBody();
            if (body != null) {
                world.destroyBody(body); // Destroy in World
            }
        }
        birds.clear();

        // Remove and destroy pigs
        for (Pig pig : pigs) {
            pig.remove(); // Remove from Stage
            Body body = pig.getPigBody();
            if (body != null) {
                world.destroyBody(body); // Destroy in World
            }
        }
        pigs.clear();


    }
    public Stage getStage() {
        return stage;
    }


}
