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

public class HowToPlayScreen implements Screen {

    private final MainGame game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    public HowToPlayScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        game.playMusic("sounds/music/music_menu.ogg");

        buildStage();
        setupInput();
    }

    private void buildStage() {
        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_PARCHMENT));
        Label titleLabel = new Label("HOW TO PLAY", titleStyle);
        titleLabel.setAlignment(Align.center);
        titleLabel.setWidth(Constants.WORLD_WIDTH);
        titleLabel.setPosition(0f, 762f);
        stage.addActor(titleLabel);

        // Step label styles
        Label.LabelStyle headerStyle = new Label.LabelStyle(game.fontBody, Color.valueOf(Constants.COLOR_PRIMARY));
        Label.LabelStyle descStyle = new Label.LabelStyle(game.fontSmall, Color.valueOf(Constants.COLOR_PARCHMENT));

        // Step 1: frame bottom=574, top=734
        addStep(headerStyle, descStyle,
                "TAP TO THROW",
                "Tap the screen to throw a blade at the spinning log",
                694f, 634f);

        // Step 2: frame bottom=394, top=554
        addStep(headerStyle, descStyle,
                "AVOID BLADES",
                "Don't hit blades already stuck in the log or it's game over!",
                514f, 454f);

        // Step 3: frame bottom=214, top=374
        addStep(headerStyle, descStyle,
                "COLLECT APPLES",
                "Hit apples on the log for 50 bonus points each",
                334f, 274f);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // PLAY NOW button — topY=700, h=60 → libgdxY = 854 - 700 - 60 = 94
        TextButton playNowBtn = UiFactory.makeButton("PLAY NOW", rectStyle, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        playNowBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        playNowBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        playNowBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 94f);
        playNowBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new StageSelectScreen(game));
            }
        });
        stage.addActor(playNowBtn);

        // BACK button — topY=790, h=44, x=20 → libgdxY = 854 - 790 - 44 = 20
        TextButton.TextButtonStyle rectStyleSmall = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyleSmall, Constants.BTN_W_SMALL, Constants.BTN_H_SMALL);
        backBtn.setColor(Color.valueOf(Constants.COLOR_BG_MID));
        backBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_PARCHMENT));
        backBtn.setPosition(20f, 20f);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
    }

    private void addStep(Label.LabelStyle headerStyle, Label.LabelStyle descStyle,
                         String header, String desc, float headerY, float descY) {
        Label headerLabel = new Label(header, headerStyle);
        headerLabel.setAlignment(Align.center);
        headerLabel.setWidth(400f);
        headerLabel.setPosition((Constants.WORLD_WIDTH - 400f) / 2f, headerY);
        stage.addActor(headerLabel);

        Label descLabel = new Label(desc, descStyle);
        descLabel.setAlignment(Align.center);
        descLabel.setWidth(400f);
        descLabel.setWrap(true);
        descLabel.setPosition((Constants.WORLD_WIDTH - 400f) / 2f, descY);
        stage.addActor(descLabel);
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
        game.batch.draw(game.manager.get("ui/how_to_play.png", Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

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
    }
}
