package com.bladetoss000894.app5470.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bladetoss000894.app5470.Constants;
import com.bladetoss000894.app5470.MainGame;
import com.bladetoss000894.app5470.UiFactory;

public class PauseScreen implements Screen {

    private final MainGame game;
    private final Screen previousScreen;
    private final int stageIndex;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    public PauseScreen(MainGame game, Screen previousScreen, int stageIndex) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.stageIndex = stageIndex;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);
        shapeRenderer = new ShapeRenderer();

        buildStage();
        setupInput();
    }

    private void buildStage() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle rectStyleSec = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // "PAUSED" title
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_PARCHMENT));
        Label titleLabel = new Label("PAUSED", titleStyle);
        titleLabel.setAlignment(Align.center);
        titleLabel.setWidth(Constants.WORLD_WIDTH);
        titleLabel.setPosition(0f, 650f);
        stage.addActor(titleLabel);

        // RESUME button
        TextButton resumeBtn = UiFactory.makeButton("RESUME", rectStyle, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        resumeBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        resumeBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        resumeBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 450f);
        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(previousScreen);
            }
        });
        stage.addActor(resumeBtn);

        // RESTART button
        TextButton restartBtn = UiFactory.makeButton("RESTART", rectStyleSec, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        restartBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        restartBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        restartBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 378f);
        restartBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new GameScreen(game, stageIndex));
            }
        });
        stage.addActor(restartBtn);

        // MAIN MENU button
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", rectStyleSec, Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        menuBtn.setColor(Color.valueOf(Constants.COLOR_ACCENT));
        menuBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_PARCHMENT));
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 306f);
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);
    }

    private void setupInput() {
        InputAdapter backAdapter = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    playBack();
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        };
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, backAdapter));
    }

    private void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
    }

    private void playBack() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
    }

    @Override
    public void show() {
        setupInput();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(game.manager.get("ui/game_screen.png", Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.65f);
        shapeRenderer.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
