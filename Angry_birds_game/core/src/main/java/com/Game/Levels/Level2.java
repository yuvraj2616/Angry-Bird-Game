package com.Game.Levels;

import com.Game.Birds.Bird;
import com.Game.Birds.YellowBird;
import com.Game.Blocks.Block;
import com.Game.Blocks.WoodBlock;
import com.Game.Core;
import com.Game.Pigs.MediumPig;
import com.Game.Pigs.Pig;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.ArrayList;

public class Level2 extends Level {

    public Level2(Core game) {
        super(game);

    }
    @Override
    protected void setupGameObjects() {
        birds = new ArrayList<>();
        pigs = new ArrayList<>();
        blocks = new ArrayList<>();

        setupBirds();

        setupTowerWithPig(WORLD_WIDTH * 0.6f, 1); // Left tower with 1 block
        setupTowerWithPig(WORLD_WIDTH * 0.75f, 2); // Middle tower with 2 blocks
        setupTowerWithPig(WORLD_WIDTH * 0.9f, 3); // Right tower with 3 blocks

        addBlocksToStage();

        createGround();
    }

    @Override
    public void setupBirds() {
        for (int i = 0; i < 3; i++) {
            final int birdIndex = i;
            Bird bird = new YellowBird(birdWaitingPositions[i].x - 450f, birdWaitingPositions[i].y-200f, world);
            bird.setScale(BIRD_SCALE *0.4f);

            Body body = bird.getBirdBody();
            if (body != null) {
                body.setType(BodyDef.BodyType.DynamicBody); // Affected by gravity
                body.setGravityScale(1); // Normal gravity
                body.setFixedRotation(true); // Prevent rotation
            }

            bird.addListener(new Level2.BirdClickListener(birdIndex)); // Use BirdClickListener

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

    }

    private void setupTowerWithPig(float towerX, int numBlocks) {
        float startingY = 80f;

        for (int i = 0; i < numBlocks; i++) {
            float y = startingY + i * 50f;
            Block block = new WoodBlock(new Vector2(towerX, y), world);
            block.setScale(BLOCK_SCALE); // Set block scale

            Body blockBody = block.getBlockBody();
            if (blockBody != null) {
                blockBody.setType(BodyDef.BodyType.DynamicBody);
                blockBody.setGravityScale(1); // Normal gravity
                blockBody.setFixedRotation(true); // Prevent rotation of blocks
            }

            blocks.add(block);
            stage.addActor(block); // Add block to stage
        }

        float pigPositionY = startingY -200;

        Pig pig = new MediumPig(new Vector2(towerX-500, pigPositionY), world);
        pig.setScale(PIG_SCALE);

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
            block.setScale(BLOCK_SCALE); // Set block scale

            Body blockBody = block.getBlockBody();
            if (blockBody != null && blockBody.getType() != BodyDef.BodyType.DynamicBody) {
                blockBody.setType(BodyDef.BodyType.DynamicBody); // Set to dynamic body
                blockBody.setGravityScale(1); // Normal gravity
                blockBody.setFixedRotation(true); // Prevent rotation
            }

            stage.addActor(block); // Add block to stage
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
