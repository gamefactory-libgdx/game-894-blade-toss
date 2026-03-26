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

public class SettingsScreen implements Screen {

    private final MainGame   game;
    private final Stage      stage;
    private final Viewport   viewport;
    private final Preferences prefs;

    private TextButton musicToggleBtn;
    private TextButton sfxToggleBtn;
    private TextButton vibrationToggleBtn;

    private static final String BG = "ui/settings.png";

    // ON/OFF tint colours
    private static final Color COLOR_ON  = new Color(0.3f, 0.8f, 0.3f, 1f);
    private static final Color COLOR_OFF = new Color(0.7f, 0.2f, 0.2f, 1f);

    public SettingsScreen(MainGame game) {
        this.game  = game;
        this.prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        buildUI();
        setupInput();
    }

    private void buildUI() {
        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle smallStyle = UiFactory.makeRectStyle(game.manager, game.fontSmall);

        Label.LabelStyle bodyStyle = new Label.LabelStyle(game.fontBody,
                Color.valueOf(Constants.COLOR_PARCHMENT));

        // ── SETTINGS title — top-Y=60, h=52 → libgdxY=742 ───────────────────────
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle,
                Color.valueOf(Constants.COLOR_PARCHMENT));
        Label titleLabel = new Label("SETTINGS", titleStyle);
        titleLabel.setPosition(
                (Constants.WORLD_WIDTH - titleLabel.getPrefWidth()) / 2f, 742f);
        stage.addActor(titleLabel);

        // ── MUSIC row — top-Y=200, h=44 → libgdxY=610 ───────────────────────────
        // Label: x=80
        Label musicLabel = new Label("MUSIC", bodyStyle);
        musicLabel.setPosition(80f, 610f);
        stage.addActor(musicLabel);

        // Switch: x=right@80 → x=480-80-80=320
        musicToggleBtn = UiFactory.makeButton(
                game.musicEnabled ? "ON" : "OFF", smallStyle, 80f, 44f);
        tintToggle(musicToggleBtn, game.musicEnabled);
        musicToggleBtn.setPosition(320f, 610f);
        musicToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.musicEnabled = !game.musicEnabled;
                prefs.putBoolean(Constants.PREF_MUSIC, game.musicEnabled);
                prefs.flush();
                if (game.currentMusic != null) {
                    if (game.musicEnabled) game.currentMusic.play();
                    else                   game.currentMusic.pause();
                }
                musicToggleBtn.setText(game.musicEnabled ? "ON" : "OFF");
                tintToggle(musicToggleBtn, game.musicEnabled);
                playToggle();
            }
        });
        stage.addActor(musicToggleBtn);

        // ── SFX row — top-Y=264, h=44 → libgdxY=546 ─────────────────────────────
        Label sfxLabel = new Label("SFX", bodyStyle);
        sfxLabel.setPosition(80f, 546f);
        stage.addActor(sfxLabel);

        sfxToggleBtn = UiFactory.makeButton(
                game.sfxEnabled ? "ON" : "OFF", smallStyle, 80f, 44f);
        tintToggle(sfxToggleBtn, game.sfxEnabled);
        sfxToggleBtn.setPosition(320f, 546f);
        sfxToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.sfxEnabled = !game.sfxEnabled;
                prefs.putBoolean(Constants.PREF_SFX, game.sfxEnabled);
                prefs.flush();
                sfxToggleBtn.setText(game.sfxEnabled ? "ON" : "OFF");
                tintToggle(sfxToggleBtn, game.sfxEnabled);
                playToggle();
            }
        });
        stage.addActor(sfxToggleBtn);

        // ── VIBRATION row — top-Y=328, h=44 → libgdxY=482 ───────────────────────
        Label vibLabel = new Label("VIBRATION", bodyStyle);
        vibLabel.setPosition(80f, 482f);
        stage.addActor(vibLabel);

        vibrationToggleBtn = UiFactory.makeButton(
                game.vibrationEnabled ? "ON" : "OFF", smallStyle, 80f, 44f);
        tintToggle(vibrationToggleBtn, game.vibrationEnabled);
        vibrationToggleBtn.setPosition(320f, 482f);
        vibrationToggleBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.vibrationEnabled = !game.vibrationEnabled;
                prefs.putBoolean(Constants.PREF_VIBRATION, game.vibrationEnabled);
                prefs.flush();
                vibrationToggleBtn.setText(game.vibrationEnabled ? "ON" : "OFF");
                tintToggle(vibrationToggleBtn, game.vibrationEnabled);
                playToggle();
            }
        });
        stage.addActor(vibrationToggleBtn);

        // ── BACK — top-Y=790, h=44, left@20 → libgdxY=20, x=20 ─────────────────
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle,
                Constants.BTN_W_SMALL, Constants.BTN_H_SMALL);
        backBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        backBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        backBtn.setPosition(20f, 20f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                goBack();
            }
        });
        stage.addActor(backBtn);
    }

    private void tintToggle(TextButton btn, boolean on) {
        btn.setColor(on ? COLOR_ON : COLOR_OFF);
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) { goBack(); return true; }
                return false;
            }
        }));
    }

    private void goBack() {
        game.setScreen(new MainMenuScreen(game));
    }

    private void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
    }

    private void playToggle() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_toggle.ogg", Sound.class).play(0.5f);
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
