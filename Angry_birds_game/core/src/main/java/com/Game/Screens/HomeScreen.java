package com.Game.Screens;

import com.Game.Core;
import com.Game.Levels.Level;
import com.Game.Levels.Level1;
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

public class HomeScreen implements Screen {
    private final Core game;
    private Stage stage;
    private Skin skin;
    private Texture background;
    private SpriteBatch batch;
    private Table table;
    private float buttonWidth;
    private float buttonHeight;

    public HomeScreen(Core game) {
        this.game = game;
        batch = new SpriteBatch();
        this.background = new Texture(Gdx.files.internal("background.png"));

        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        table = new Table();
        table.setFillParent(true);
        table.bottom().right().padRight(50);
        stage.addActor(table);

        updateButtonSize();
    }

    @Override
    public void show() {

        TextButton newGameButton = new TextButton("New Game", skin);
        TextButton loadGameButton = new TextButton("Load Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectionScreen(game));
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Level level1 = new Level1(game);
                level1.loadLevel("level_save.sav"); // Load the saved game state
                game.setScreen(level1); // Transition to the loaded level
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.clear();

        table.add(newGameButton).size(buttonWidth, buttonHeight).padBottom(10).padRight(10).row();
        table.add(loadGameButton).size(buttonWidth, buttonHeight).padBottom(10).padRight(10).row();
        table.add(exitButton).size(buttonWidth, buttonHeight).padBottom(10).padRight(10).row();
    }

    private void updateButtonSize() {

        buttonWidth = Gdx.graphics.getWidth() * 0.2f;
        buttonHeight = Gdx.graphics.getHeight() * 0.1f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
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
        updateButtonSize();
        show();
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
