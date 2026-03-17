package com.Game.Screens;

import com.Game.Core;
import com.Game.Levels.Level1;
import com.Game.Levels.Level2;
import com.Game.Levels.Level3;
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

public class LevelSelectionScreen implements Screen {
    private final Core game;
    private Stage stage;
    private Skin skin;
    private Texture background;
    private SpriteBatch batch;
    private Table table;
    private float buttonWidth;
    private float buttonHeight;

    public LevelSelectionScreen(Core game) {
        this.game = game;
        batch = new SpriteBatch();
        this.background = new Texture(Gdx.files.internal("background.png"));
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setFillParent(true);
        table.bottom().right();
        stage.addActor(table);
        updateButtonSize();
    }

    @Override
    public void show() {
        // Initialize buttons here to avoid reinitializing every time
        createButtons();
        updateButtonSize(); // Ensure buttons have the correct size when shown
    }

    private void createButtons() {
        TextButton level1Button = new TextButton("Level 1", skin);
        TextButton level2Button = new TextButton("Level 2", skin);
        TextButton level3Button = new TextButton("Level 3", skin);
        TextButton backButton = new TextButton("Back", skin);

        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 1 Selected");
                game.setScreen(new Level1(game));
            }
        });

        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 2 Selected");
                game.setScreen(new Level2(game));
            }
        });

        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level 3 Selected");
                game.setScreen(new Level3(game));
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        });

        table.clear(); // Clear the table before adding new buttons
        table.add(level1Button).size(buttonWidth, buttonHeight).padBottom(10).padRight(10).row();
        table.add(level2Button).size(buttonWidth, buttonHeight).padBottom(10).padRight(10).row();
        table.add(level3Button).size(buttonWidth, buttonHeight).padBottom(10).padRight(10).row();
        table.add(backButton).size(buttonWidth, buttonHeight).padBottom(10).padRight(10).row();
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
        createButtons();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
        background.dispose();
        stage.dispose();
    }
}
