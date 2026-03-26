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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.bladetoss000894.app5470.Constants;
import com.bladetoss000894.app5470.MainGame;
import com.bladetoss000894.app5470.UiFactory;

public class GameOverScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    /** score from the run that just ended */
    private final int score;
    /** stageIndex (0-based) that was active when the game ended — used for RETRY */
    private final int stageIndex;

    private static final String BG = "ui/game_over.png";

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game       = game;
        this.score      = score;
        this.stageIndex = extra;

        // Persist high score
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int best = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > best) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
            best = score;
        }
        final int personalBest = best;

        // Save to leaderboard
        LeaderboardScreen.addScore(score);

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        buildUI(personalBest);
        setupInput();
    }

    private void buildUI(int personalBest) {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle,
                Color.valueOf(Constants.COLOR_ACCENT));
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore,
                Color.WHITE);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,
                Color.valueOf(Constants.COLOR_PARCHMENT));

        // ── GAME OVER — top-Y=100, h=64 → libgdxY=690 ───────────────────────────
        Label gameOverLabel = new Label("GAME OVER", titleStyle);
        gameOverLabel.setPosition(
                (Constants.WORLD_WIDTH - gameOverLabel.getPrefWidth()) / 2f, 690f);
        stage.addActor(gameOverLabel);

        // ── SCORE display — top-Y=210, h=52 → libgdxY=592 ───────────────────────
        Label scoreLabel = new Label("SCORE: " + score, scoreStyle);
        scoreLabel.setPosition(
                (Constants.WORLD_WIDTH - scoreLabel.getPrefWidth()) / 2f, 592f);
        stage.addActor(scoreLabel);

        // ── BEST display — top-Y=272, h=40 → libgdxY=542 ────────────────────────
        Label bestLabel = new Label("BEST: " + personalBest, bodyStyle);
        bestLabel.setPosition(
                (Constants.WORLD_WIDTH - bestLabel.getPrefWidth()) / 2f, 542f);
        stage.addActor(bestLabel);

        // ── RETRY — top-Y=520, h=60 → libgdxY=274 ───────────────────────────────
        TextButton retryBtn = UiFactory.makeButton("RETRY", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        retryBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        retryBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        retryBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 274f);
        retryBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new GameScreen(game, stageIndex));
            }
        });
        stage.addActor(retryBtn);

        // ── STAGE SELECT — top-Y=598, h=52 → libgdxY=204 ────────────────────────
        TextButton stageBtn = UiFactory.makeButton("STAGE SELECT", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        stageBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        stageBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        stageBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 204f);
        stageBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new StageSelectScreen(game));
            }
        });
        stage.addActor(stageBtn);

        // ── MENU — top-Y=676, h=52 → libgdxY=126 ────────────────────────────────
        TextButton menuBtn = UiFactory.makeButton("MENU", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        menuBtn.setColor(Color.valueOf(Constants.COLOR_BG_MID));
        menuBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_PARCHMENT));
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 126f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    playClick();
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));
    }

    private void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
    }

    @Override
    public void show() {
        game.playMusicOnce("sounds/music/music_game_over.ogg");
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg", Sound.class).play(1.0f);
        setupInput();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(game.manager.get(BG, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
