package com.Game.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlueBird extends Bird {
    private boolean hasSplit;
    private static Texture texture;
    private Stage stage;
    public BlueBird(float x, float y, World world, Stage stage) {
        super(getTextureRegion(), new Vector2(x, y), world);
        this.stage = stage;
        hasSplit = false;
    }

    private static TextureRegion getTextureRegion() {
        if (texture == null) {
            texture = new Texture("Bluebird.png");
        }
        return new TextureRegion(texture);
    }

    @Override
    public String getBirdType() {
        return "blue";
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (getState() == BirdState.FLYING && !hasSplit && getY() > getStartPosition().y + 50) {
            split();
            hasSplit = true;
        }
    }

    public void split() {
        System.out.println("Blue bird splitting into multiple birds!");

        Vector2 currentPosition = getBirdBody().getPosition();

        BlueBird splitBird1 = new BlueBird(currentPosition.x, currentPosition.y, getWorld(), stage);
        BlueBird splitBird2 = new BlueBird(currentPosition.x, currentPosition.y, getWorld(), stage);

        configureAndLaunchBird(splitBird1, -5, 5);
        configureAndLaunchBird(splitBird2, 5, 5);

        addBirdToWorld(splitBird1);
        addBirdToWorld(splitBird2);
    }
    private void configureAndLaunchBird(BlueBird bird, float offsetX, float offsetY) {
        Body body = bird.getBirdBody();
        if (body != null) {
            body.setType(BodyDef.BodyType.DynamicBody);
            body.setGravityScale(1);
            body.setFixedRotation(true);
        }

        bird.launch(offsetX, offsetY);
    }

    private void addBirdToWorld(BlueBird bird) {
        bird.getBirdBody().setType(BodyDef.BodyType.DynamicBody);
        stage.addActor(bird);
        System.out.println("Adding split bird to the game world at: " + bird.getPosition());
    }

    public Vector2 getPosition() {
        return getBirdBody().getPosition();
    }

    public World getWorld() {
        return getBirdBody().getWorld();
    }
}
