package com.Game;

import com.Game.Screens.HomeScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Core extends Game {
    public SpriteBatch batch;  // SpriteBatch for drawing textures
    private World world;  // Box2D world for physics simulation

    @Override
    public void create() {
        batch = new SpriteBatch();  // Initialize the SpriteBatch
        world = new World(new Vector2(0, -9.8f), true);  // Initialize Box2D World with gravity

        // Set the initial screen (HomeScreen)
        setScreen(new HomeScreen(this));
    }

//    public void startGame() {
//        setScreen(new Level1Screen(this,));  // This will be a screen where the gameplay happens
//    }

    public World getWorld() {
        return world;  // Provide access to the physics world
    }

    @Override
    public void render() {
        super.render();  // Call the render() method of the active screen
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);  // Ensure the active screen resizes correctly
    }

    @Override
    public void pause() {
        super.pause();  // Pause the active screen
    }

    @Override
    public void resume() {
        super.resume();  // Resume the active screen
    }

    @Override
    public void dispose() {
        batch.dispose();  // Dispose of the SpriteBatch to free resources
        world.dispose();  // Dispose of the Box2D world to free resources
        super.dispose();  // Dispose of any resources used by the active screen
    }
}
