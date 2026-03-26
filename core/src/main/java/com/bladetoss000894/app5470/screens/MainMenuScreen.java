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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.bladetoss000894.app5470.Constants;
import com.bladetoss000894.app5470.MainGame;
import com.bladetoss000894.app5470.UiFactory;

public class MainMenuScreen implements Screen {

    private final MainGame game;
    private final Stage    stage;
    private final Viewport viewport;

    private static final String BG = "ui/main_menu.png";

    public MainMenuScreen(MainGame game) {
        this.game = game;
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        setupInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontBody);

        // ── Title label — top-Y=80, h=70 → libgdxY=704 ──────────────────────────
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle,
                Color.valueOf(Constants.COLOR_PARCHMENT));
        Label titleLabel = new Label("BLADE TOSS", titleStyle);
        titleLabel.setPosition(
                (Constants.WORLD_WIDTH - titleLabel.getPrefWidth()) / 2f, 704f);
        stage.addActor(titleLabel);

        // ── PLAY — top-Y=420, size=260×60 → libgdxY=374 ─────────────────────────
        TextButton playBtn = UiFactory.makeButton("PLAY", rectStyle,
                Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        playBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        playBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        playBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 374f);
        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new StageSelectScreen(game));
            }
        });
        stage.addActor(playBtn);

        // ── STAGE SELECT — top-Y=500, size=260×52 → libgdxY=302 ─────────────────
        TextButton stageBtn = UiFactory.makeButton("STAGE SELECT", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        stageBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        stageBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        stageBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 302f);
        stageBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new StageSelectScreen(game));
            }
        });
        stage.addActor(stageBtn);

        // ── LEADERBOARD — top-Y=570, size=260×52 → libgdxY=232 ──────────────────
        TextButton lbBtn = UiFactory.makeButton("LEADERBOARD", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        lbBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        lbBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        lbBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 232f);
        lbBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        stage.addActor(lbBtn);

        // ── HOW TO PLAY — top-Y=640, size=260×52 → libgdxY=162 ──────────────────
        TextButton htpBtn = UiFactory.makeButton("HOW TO PLAY", rectStyle,
                Constants.BTN_W_SEC, Constants.BTN_H_SEC);
        htpBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        htpBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        htpBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_SEC) / 2f, 162f);
        htpBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new HowToPlayScreen(game));
            }
        });
        stage.addActor(htpBtn);

        // ── SETTINGS gear icon — top-Y=780, size=56×56, right@20 → x=404, libgdxY=18
        Texture gearTex = game.manager.get("ui/icons/settings_gear.png", Texture.class);
        ImageButton settingsBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(gearTex)));
        settingsBtn.setSize(Constants.BTN_ROUND, Constants.BTN_ROUND);
        settingsBtn.setPosition(Constants.WORLD_WIDTH - 20f - Constants.BTN_ROUND, 18f);
        settingsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new SettingsScreen(game));
            }
        });
        stage.addActor(settingsBtn);
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.exit();
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
