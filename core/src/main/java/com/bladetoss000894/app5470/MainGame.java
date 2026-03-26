package com.bladetoss000894.app5470;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import com.bladetoss000894.app5470.screens.MainMenuScreen;

public class MainGame extends Game {

    // ── Shared rendering resources ────────────────────────────────────────────
    public SpriteBatch  batch;
    public AssetManager manager;

    // ── Fonts (generated once, shared across all screens) ─────────────────────
    public BitmapFont fontTitle; // Toxigenesis — large display
    public BitmapFont fontScore; // Toxigenesis — score / big numbers
    public BitmapFont fontBody;  // Ferrum — buttons / labels
    public BitmapFont fontSmall; // Ferrum — small captions

    // ── Audio state ───────────────────────────────────────────────────────────
    public boolean musicEnabled    = true;
    public boolean sfxEnabled      = true;
    public boolean vibrationEnabled = true;
    public Music   currentMusic    = null;

    // ── Lifecycle ─────────────────────────────────────────────────────────────
    @Override
    public void create() {
        batch   = new SpriteBatch();
        manager = new AssetManager();

        // Register FreeType loaders
        manager.setLoader(FreeTypeFontGenerator.class,
                new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        manager.setLoader(BitmapFont.class, ".ttf",
                new FreetypeFontLoader(new InternalFileHandleResolver()));
        manager.setLoader(BitmapFont.class, ".otf",
                new FreetypeFontLoader(new InternalFileHandleResolver()));

        generateFonts();
        loadCoreAssets();
        manager.finishLoading();

        // Restore persisted audio settings
        com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicEnabled    = prefs.getBoolean(Constants.PREF_MUSIC,     true);
        sfxEnabled      = prefs.getBoolean(Constants.PREF_SFX,       true);
        vibrationEnabled = prefs.getBoolean(Constants.PREF_VIBRATION, true);

        setScreen(new MainMenuScreen(this));
    }

    // ── Font generation ───────────────────────────────────────────────────────
    private void generateFonts() {
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Toxigenesis.otf"));
        FreeTypeFontGenerator bodyGen  = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Ferrum.otf"));

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // fontTitle — large display
        p.size        = Constants.FONT_SIZE_TITLE;
        p.borderWidth = 3;
        p.borderColor = new Color(0f, 0f, 0f, 0.85f);
        p.color       = Color.valueOf(Constants.COLOR_PARCHMENT);
        fontTitle = titleGen.generateFont(p);

        // fontScore — big numbers
        p.size        = Constants.FONT_SIZE_SCORE;
        p.borderWidth = 3;
        p.color       = Color.WHITE;
        fontScore = titleGen.generateFont(p);

        // fontBody — buttons / mid labels
        p.size        = Constants.FONT_SIZE_BODY;
        p.borderWidth = 2;
        p.borderColor = new Color(0f, 0f, 0f, 0.85f);
        p.color       = Color.WHITE;
        fontBody = bodyGen.generateFont(p);

        // fontSmall — small captions
        p.size        = Constants.FONT_SIZE_SMALL;
        p.borderWidth = 1;
        p.color       = Color.valueOf(Constants.COLOR_PARCHMENT);
        fontSmall = bodyGen.generateFont(p);

        titleGen.dispose();
        bodyGen.dispose();
    }

    // ── Core asset loading ────────────────────────────────────────────────────
    private void loadCoreAssets() {
        // Button sprites
        manager.load("ui/buttons/button_rectangle_depth_gradient.png", Texture.class);
        manager.load("ui/buttons/button_rectangle_depth_flat.png",     Texture.class);
        manager.load("ui/buttons/button_round_depth_gradient.png",     Texture.class);
        manager.load("ui/buttons/button_round_depth_flat.png",         Texture.class);
        manager.load("ui/buttons/star.png",                            Texture.class);
        manager.load("ui/buttons/star_outline.png",                    Texture.class);

        // Settings gear icon
        manager.load("ui/icons/settings_gear.png", Texture.class);

        // Music
        manager.load("sounds/music/music_menu.ogg",      Music.class);
        manager.load("sounds/music/music_gameplay.ogg",  Music.class);
        manager.load("sounds/music/music_game_over.ogg", Music.class);

        // SFX
        manager.load("sounds/sfx/sfx_button_click.ogg",   Sound.class);
        manager.load("sounds/sfx/sfx_button_back.ogg",    Sound.class);
        manager.load("sounds/sfx/sfx_toggle.ogg",         Sound.class);
        manager.load("sounds/sfx/sfx_hit.ogg",            Sound.class);
        manager.load("sounds/sfx/sfx_collect.ogg",        Sound.class);
        manager.load("sounds/sfx/sfx_game_over.ogg",      Sound.class);
        manager.load("sounds/sfx/sfx_level_complete.ogg", Sound.class);
        manager.load("sounds/sfx/sfx_shoot.ogg",          Sound.class);

        // UI screen backgrounds
        manager.load("ui/main_menu.png",    Texture.class);
        manager.load("ui/stage_select.png", Texture.class);
        manager.load("ui/game_screen.png",  Texture.class);
        manager.load("ui/stage_clear.png",  Texture.class);
        manager.load("ui/game_over.png",    Texture.class);
        manager.load("ui/leaderboard.png",  Texture.class);
        manager.load("ui/settings.png",     Texture.class);
        manager.load("ui/how_to_play.png",  Texture.class);
    }

    // ── Music helpers ─────────────────────────────────────────────────────────
    /** Play a looping track — no-op if it is already playing. */
    public void playMusic(String path) {
        Music requested = manager.get(path, Music.class);
        if (requested == currentMusic && currentMusic.isPlaying()) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = requested;
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    /** Play a one-shot track (game over jingle). */
    public void playMusicOnce(String path) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(false);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    // ── Dispose ───────────────────────────────────────────────────────────────
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        manager.dispose();
        fontTitle.dispose();
        fontScore.dispose();
        fontBody.dispose();
        fontSmall.dispose();
    }
}
