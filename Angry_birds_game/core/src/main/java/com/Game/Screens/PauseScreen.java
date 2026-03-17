package com.Game.Screens;

import com.Game.Core;
import com.Game.Levels.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private final Core game;
    private final Level currentLevel;
    private Stage stage;
    private Skin skin;
    private TextButton resumeButton;
    private TextButton saveButton;
    private TextButton exitButton;
    private TextButton levelSelectButton;

    public PauseScreen(Core game, Level currentLevel) {
        this.game = game;
        this.currentLevel = currentLevel;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        resumeButton = new TextButton("Resume Game", skin);
        saveButton = new TextButton("Save Game", skin);
        exitButton = new TextButton("Exit Game", skin);
        levelSelectButton = new TextButton("Return to Level Selection", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(currentLevel); // Resume the current level
                Gdx.input.setInputProcessor(currentLevel.getStage()); // Set input processor to the level's stage
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentLevel.saveLevel("level_save.sav"); // Save the game state
                System.out.println("Game saved.");
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the game
            }
        });

        levelSelectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectionScreen(game));
            }
        });

        table.add(resumeButton).pad(10);
        table.row();
        table.add(saveButton).pad(10);
        table.row();
        table.add(exitButton).pad(10);
        table.row();
        table.add(levelSelectButton).pad(10);
        stage.addActor(table);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
