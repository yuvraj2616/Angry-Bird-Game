package com.Game.Levels;

import com.Game.Birds.Bird;
import com.Game.Birds.RedBird;
import com.Game.Birds.YellowBird;
import com.Game.Blocks.Block;
import com.Game.Blocks.GlassBlock;
import com.Game.Blocks.StoneBlock;
import com.Game.Blocks.WoodBlock;
import com.Game.Core;
import com.Game.Pigs.MediumPig;
import com.Game.Pigs.Pig;
import com.Game.Pigs.SmallPig;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.ArrayList;

public class Level1 extends Level {

    public Level1(Core game) {
        super(game);

    }
    @Override
    protected void setupGameObjects()
    {
        // Initialize lists for birds, pigs, and blocks
        birds = new ArrayList<>();
        pigs = new ArrayList<>();
        blocks = new ArrayList<>();

        // Set up birds
        setupBirds();

        // Set up towers and pigs
        setupTowerWithPig(WORLD_WIDTH * 0.6f, 380f); // Left tower
        setupTowerWithPig(WORLD_WIDTH * 0.75f, 230f); // Middle tower
        setupTowerWithPig(WORLD_WIDTH * 0.9f, 380f); // Right tower

        // Add blocks to the stage
        addBlocksToStage();

        // Create the ground
        createGround();
    }

    @Override
    protected void setupBirds() {
        for (int i = 0; i < 3; i++) {
            final int birdIndex = i;
            Bird bird = new RedBird(birdWaitingPositions[i].x - 280f, birdWaitingPositions[i].y, world);

            bird.setScale(BIRD_SCALE * 0.5f);

            Body body = bird.getBirdBody();
            if (body != null) {
                body.setType(BodyDef.BodyType.DynamicBody); // Affected by gravity
                body.setGravityScale(1); // Normal gravity
                body.setFixedRotation(true); // Prevent rotation
            }

            bird.addListener(new Level.BirdClickListener(birdIndex)); // Use BirdClickListener

            birds.add(bird);
            stage.addActor(bird);
        }

        currentBirdIndex = 0;
        birds.get(currentBirdIndex).setPosition(
            catapultPosition.x - birds.get(currentBirdIndex).getWidth() / 2,
            catapultPosition.y - birds.get(currentBirdIndex).getHeight() / 2
        );
    }
    @Override
    protected void setupTowerWithPig(float towerX, float pigY) {
        float startingY = 80f;

        // Number of blocks to add
        int numBlocks = 1;

        // Add blocks to the tower
        for (int i = 0; i < numBlocks; i++) {
            float y = startingY + i * 50f; // Calculate vertical stacking with 50f between blocks
            Block block = new StoneBlock(new Vector2(towerX, y), world);
            block.setScale(BLOCK_SCALE*0.5f);


            // Set block to dynamic body
            Body blockBody = block.getBlockBody();
            if (blockBody != null) {
                blockBody.setType(BodyDef.BodyType.DynamicBody); // Set to dynamic so it's affected by gravity
                blockBody.setGravityScale(1); // Normal gravity
                blockBody.setFixedRotation(true); // Prevent rotation of blocks
            }

            blocks.add(block);
            stage.addActor(block); // Add block to stage
        }

        float pigPositionY = startingY - 200;

        // Add a pig on top of the tower
        Pig pig = new SmallPig(new Vector2(towerX - 220, pigPositionY + 200), world);
        pig.setScale(PIG_SCALE * 2);

        // Set pig to dynamic body
        Body pigBody = pig.getPigBody();
        if (pigBody != null) {
            pigBody.setType(BodyDef.BodyType.DynamicBody); // Set pig to dynamic body
            pigBody.setGravityScale(1); // Normal gravity
            pigBody.setFixedRotation(true); // Prevent rotation of the pig
        }

        pigs.add(pig);
        stage.addActor(pig); // Add pig to stage
    }
    private void addBlocksToStage() {

        for (Block block : blocks) {
            block.setScale(BLOCK_SCALE);

            Body blockBody = block.getBlockBody();
            if (blockBody != null && blockBody.getType() != BodyDef.BodyType.DynamicBody) {
                blockBody.setType(BodyDef.BodyType.DynamicBody);
                blockBody.setGravityScale(1);
                blockBody.setFixedRotation(true);
            }

            stage.addActor(block);
        }
    }

    private void createGround() {
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(WORLD_WIDTH / 2 / PPM, 32 / PPM);

        Body ground = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(WORLD_WIDTH / 2 / PPM, 32 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;

        ground.createFixture(fixtureDef);
        groundShape.dispose();
    }

}
