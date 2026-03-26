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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardScreen implements Screen {

    private static final int   MAX_ENTRIES   = 10;
    private static final String PREFS_KEY    = "lb_score_";

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    private static final String BG = "ui/leaderboard.png";

    // ── Static helper — call from GameOverScreen ──────────────────────────────
    public static void addScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        List<Integer> scores = loadScores(prefs);
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder());
        if (scores.size() > MAX_ENTRIES) scores = scores.subList(0, MAX_ENTRIES);
        for (int i = 0; i < scores.size(); i++) {
            prefs.putInteger(PREFS_KEY + i, scores.get(i));
        }
        prefs.putInteger("lb_count", scores.size());
        prefs.flush();
    }

    private static List<Integer> loadScores(Preferences prefs) {
        int count = prefs.getInteger("lb_count", 0);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            list.add(prefs.getInteger(PREFS_KEY + i, 0));
        }
        return list;
    }

    // ── Constructor ───────────────────────────────────────────────────────────
    public LeaderboardScreen(MainGame game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        setupInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        Label.LabelStyle titleStyle  = new Label.LabelStyle(game.fontTitle,
                Color.valueOf(Constants.COLOR_PARCHMENT));
        Label.LabelStyle headerStyle = new Label.LabelStyle(game.fontSmall,
                Color.valueOf(Constants.COLOR_PRIMARY));
        Label.LabelStyle rowStyle    = new Label.LabelStyle(game.fontSmall,
                Color.valueOf(Constants.COLOR_PARCHMENT));
        Label.LabelStyle goldStyle   = new Label.LabelStyle(game.fontSmall,
                Color.GOLD);

        // ── LEADERBOARD title — top-Y=40, h=52 → libgdxY=762 ────────────────────
        Label titleLabel = new Label("LEADERBOARD", titleStyle);
        titleLabel.setPosition(
                (Constants.WORLD_WIDTH - titleLabel.getPrefWidth()) / 2f, 762f);
        stage.addActor(titleLabel);

        // ── Column headers — top-Y=140, h=36 → libgdxY=678 ──────────────────────
        float colRank  = 20f;
        float colScore = 300f;
        Label rankHdr  = new Label("RANK", headerStyle);
        rankHdr.setPosition(colRank, 678f);
        stage.addActor(rankHdr);

        Label scoreHdr = new Label("SCORE", headerStyle);
        scoreHdr.setPosition(colScore, 678f);
        stage.addActor(scoreHdr);

        // ── Score rows — 8 visible rows, step=52px between tops ─────────────────
        //   Row 1: top-Y=186, h=44 → libgdxY=624
        //   Row i: libgdxY = 624 - (i-1)*52
        Preferences prefs  = Gdx.app.getPreferences(Constants.PREFS_NAME);
        List<Integer> scores = loadScores(prefs);

        int rowCount = Math.min(scores.size(), MAX_ENTRIES);
        // Positions for up to 8 rows visible on screen:
        float[] rowY = { 624f, 572f, 520f, 468f, 416f, 364f, 312f, 260f };

        for (int i = 0; i < rowCount && i < rowY.length; i++) {
            Label.LabelStyle style = (i == 0) ? goldStyle : rowStyle;
            int rank = i + 1;

            Label rankLabel  = new Label("#" + rank, style);
            rankLabel.setPosition(colRank, rowY[i]);
            stage.addActor(rankLabel);

            Label scoreLabel = new Label(String.valueOf(scores.get(i)), style);
            scoreLabel.setPosition(colScore, rowY[i]);
            stage.addActor(scoreLabel);
        }

        if (rowCount == 0) {
            Label emptyLabel = new Label("No scores yet!", rowStyle);
            emptyLabel.setPosition(
                    (Constants.WORLD_WIDTH - emptyLabel.getPrefWidth()) / 2f, 500f);
            stage.addActor(emptyLabel);
        }

        // ── BACK — top-Y=790, h=44, left@20 → libgdxY=20, x=20 ─────────────────
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle,
                Constants.BTN_W_SMALL, Constants.BTN_H_SMALL);
        backBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        backBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        backBtn.setPosition(20f, 20f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
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
            game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
    }

    @Override
    public void show() {
        game.playMusic("sounds/music/music_menu.ogg");
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
