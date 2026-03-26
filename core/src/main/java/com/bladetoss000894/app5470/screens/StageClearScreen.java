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

public class StageClearScreen implements Screen {

    private final MainGame game;
    private final int stageIndex;
    private final int score;
    private final int bestScore;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    public StageClearScreen(MainGame game, int stageIndex, int score) {
        this.game = game;
        this.stageIndex = stageIndex;
        this.score = score;

        // Save to preferences
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        // Stage best score
        String stageBestKey = Constants.PREF_STAGE_BEST + stageIndex;
        int currentStageBest = prefs.getInteger(stageBestKey, 0);
        if (score > currentStageBest) {
            prefs.putInteger(stageBestKey, score);
            currentStageBest = score;
        }
        this.bestScore = currentStageBest;

        // Unlock next stage
        if (stageIndex + 1 < Constants.STAGE_COUNT) {
            int currentUnlocked = prefs.getInteger(Constants.PREF_STAGE, 0);
            prefs.putInteger(Constants.PREF_STAGE, Math.max(currentUnlocked, stageIndex + 1));
        }

        // Global high score
        int currentHighScore = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > currentHighScore) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
        }

        prefs.flush();

        // Music
        game.playMusic("sounds/music/music_menu.ogg");

        // SFX
        if (game.sfxEnabled) {
            game.manager.get("sounds/sfx/sfx_level_complete.ogg", Sound.class).play(1.0f);
        }

        // Viewport and stage
        camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        buildUI();
        setupInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // "STAGE CLEAR!" label — topY=80, h=60 → libgdxY=714
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.valueOf(Constants.COLOR_PARCHMENT));
        Label titleLabel = new Label("STAGE CLEAR!", titleStyle);
        titleLabel.setWidth(Constants.WORLD_WIDTH);
        titleLabel.setAlignment(Align.center);
        titleLabel.setPosition(0, 714f);
        stage.addActor(titleLabel);

        // SCORE label — topY=180, h=52 → libgdxY=622
        Label.LabelStyle bodyStyle = new Label.LabelStyle(game.fontBody, Color.valueOf(Constants.COLOR_PARCHMENT));
        Label scoreLabel = new Label("SCORE: " + score, bodyStyle);
        scoreLabel.setWidth(Constants.WORLD_WIDTH);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition(0, 622f);
        stage.addActor(scoreLabel);

        // BEST label — topY=245, h=40 → libgdxY=569
        Label bestLabel = new Label("BEST: " + bestScore, bodyStyle);
        bestLabel.setWidth(Constants.WORLD_WIDTH);
        bestLabel.setAlignment(Align.center);
        bestLabel.setPosition(0, 569f);
        stage.addActor(bestLabel);

        // NEXT STAGE button — topY=540 → libgdxY=254, 260×60
        float nextX = (Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f;
        if (stageIndex >= Constants.STAGE_COUNT - 1) {
            // Last stage — show "COMPLETED!" button that goes to main menu
            TextButton completedBtn = UiFactory.makeButton("COMPLETED!", rectStyle,
                    Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
            completedBtn.setPosition(nextX, 254f);
            completedBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
            completedBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
            completedBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (game.sfxEnabled) {
                        game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
                    }
                    game.setScreen(new MainMenuScreen(game));
                }
            });
            stage.addActor(completedBtn);
        } else {
            TextButton nextBtn = UiFactory.makeButton("NEXT STAGE", rectStyle,
                    Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
            nextBtn.setPosition(nextX, 254f);
            nextBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
            nextBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
            nextBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (game.sfxEnabled) {
                        game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
                    }
                    game.setScreen(new GameScreen(game, stageIndex + 1));
                }
            });
            stage.addActor(nextBtn);
        }

        // RETRY button — topY=618 → libgdxY=184, 260×52
        float secX = (Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f;
        TextButton retryBtn = UiFactory.makeButton("RETRY", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        retryBtn.setPosition(secX, 184f);
        retryBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        retryBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled) {
                    game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
                }
                game.setScreen(new GameScreen(game, stageIndex));
            }
        });
        stage.addActor(retryBtn);

        // MENU button — topY=696 → libgdxY=106, 260×52
        TextButton menuBtn = UiFactory.makeButton("MENU", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        menuBtn.setPosition(secX, 106f);
        menuBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        menuBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled) {
                    game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
                }
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    if (game.sfxEnabled) {
                        game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
                    }
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));
    }

    @Override
    public void show() {
        setupInput();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(game.manager.get("ui/stage_clear.png", Texture.class),
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
