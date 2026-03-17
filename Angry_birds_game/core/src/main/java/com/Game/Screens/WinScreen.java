package com.Game.Screens;

import com.Game.Core;
import com.Game.Levels.Level1;
import com.Game.Levels.Level2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class WinScreen implements Screen {
    private final Core game;
    private Stage stage;
    private Skin skin;
    private Texture background;
    private SpriteBatch batch;

    // UI elements
    private TextButton retryButton;
    private TextButton nextLevelButton;
    private TextButton mainMenuButton;

    public WinScreen(final Core game) {
        this.game = game;
        batch = new SpriteBatch();
        this.background = new Texture(Gdx.files.internal("YouWin.png"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createUI();
    }
    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center().bottom().padBottom(50); // Align to the bottom-center with padding

        retryButton = new TextButton("Retry", skin);
        nextLevelButton = new TextButton("Next Level", skin);
        mainMenuButton = new TextButton("Main Menu", skin);

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Level1(game)); // Restart the current level
            }
        });

        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Level2(game)); // Go to the next level
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game)); // Go to the main menu
            }
        });

        // Add buttons to the table with spacing and size adjustments
        table.add(retryButton).width(200).height(50).pad(10);
        table.row();
        table.add(nextLevelButton).width(200).height(50).pad(10);
        table.row();
        table.add(mainMenuButton).width(200).height(50).pad(10);

        stage.addActor(table);
    }


    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
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
        background.dispose();
        batch.dispose();
    }
}
