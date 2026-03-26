package com.bladetoss000894.app5470.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
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

public class StageSelectScreen implements Screen {

    private final MainGame game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private static final float BTN_W = 180f;
    private static final float BTN_H = 80f;
    private static final float COL_LEFT_X  = 40f;
    private static final float COL_RIGHT_X = 260f;

    // Row Y positions (libgdxY = 854 - topY - BTN_H)
    // Row 1: topY=140 → 854-140-80=634
    // Row 2: topY=240 → 534
    // Row 3: topY=340 → 434
    // Row 4: topY=440 → 334
    // Row 5: topY=540 → 234
    private static final float[] ROW_Y = { 634f, 534f, 434f, 334f, 234f };

    public StageSelectScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        game.playMusic("sounds/music/music_menu.ogg");

        buildStage();
        setupInput();
    }

    private void buildStage() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int unlockedStage = prefs.getInteger(Constants.PREF_STAGE, 0);

        // Title — topY=40, h=54 → libgdxY = 854 - 40 - 54 = 760
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_PARCHMENT));
        Label titleLabel = new Label("STAGE SELECT", titleStyle);
        titleLabel.setAlignment(Align.center);
        titleLabel.setWidth(Constants.WORLD_WIDTH);
        titleLabel.setPosition(0f, 760f);
        stage.addActor(titleLabel);

        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // Build 10 stage buttons arranged in a 2-column, 5-row grid
        for (int i = 0; i < Constants.STAGE_COUNT; i++) {
            final int stageIndex = i;
            boolean unlocked = i <= unlockedStage;
            int row = i / 2;
            boolean leftCol = (i % 2 == 0);
            float x = leftCol ? COL_LEFT_X : COL_RIGHT_X;
            float y = ROW_Y[row];

            int bestScore = prefs.getInteger(Constants.PREF_STAGE_BEST + i, 0);
            String label;
            if (unlocked) {
                if (bestScore > 0) {
                    label = "STAGE " + (i + 1) + "\nBEST: " + bestScore;
                } else {
                    label = "STAGE " + (i + 1);
                }
            } else {
                label = "STAGE " + (i + 1) + "\nLOCKED";
            }

            TextButton btn = UiFactory.makeButton(label, rectStyle, BTN_W, BTN_H);
            btn.getLabel().setAlignment(Align.center);
            btn.setPosition(x, y);

            if (unlocked) {
                btn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
                btn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
                btn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        playClick();
                        game.setScreen(new GameScreen(game, stageIndex));
                    }
                });
            } else {
                btn.setColor(Color.DARK_GRAY);
                btn.getLabel().setColor(Color.LIGHT_GRAY);
                btn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        playBack();
                    }
                });
            }

            stage.addActor(btn);
        }

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
        game.batch.draw(game.manager.get("ui/stage_select.png", Texture.class),
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
