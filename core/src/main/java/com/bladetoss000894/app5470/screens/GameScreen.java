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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.bladetoss000894.app5470.Constants;
import com.bladetoss000894.app5470.MainGame;
import com.bladetoss000894.app5470.UiFactory;

public class GameScreen implements Screen {

    // ── State machine ─────────────────────────────────────────────────────────
    private enum State { WAITING, BLADE_IN_FLIGHT, GAME_OVER, STAGE_CLEAR }

    private final MainGame game;
    private final int stageIndex;

    // ── Rendering ─────────────────────────────────────────────────────────────
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage hudStage;
    private final ShapeRenderer shapeRenderer;
    private final Sprite bladeSprite;

    // ── Game state ────────────────────────────────────────────────────────────
    private State state = State.WAITING;
    private boolean navigating = false;
    private float logAngle = 0f;
    private final float logRotSpeed;
    private final Array<Float> stuckAngles = new Array<>();
    private final float[] appleAngles;
    private final boolean[] appleCollected;
    private final int bladesTotal;
    private int bladesLeft;
    private int score = 0;
    private float movingBladeY;

    // ── HUD actors ────────────────────────────────────────────────────────────
    private Label scoreLbl;
    private Label bladesLbl;

    // ── Blade asset ───────────────────────────────────────────────────────────
    private static final String BLADE_TEX          = "sprites/weapon/crystal_black1.png";
    private static final float  APPLE_COLLECT_DEG  = 18f;

    // ─────────────────────────────────────────────────────────────────────────
    public GameScreen(MainGame game, int stageIndex) {
        this.game       = game;
        this.stageIndex = stageIndex;

        // Viewport / stage
        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        hudStage = new Stage(viewport, game.batch);

        // Stage configuration
        bladesTotal  = Constants.BLADES_PER_STAGE[stageIndex];
        bladesLeft   = bladesTotal;
        logRotSpeed  = Constants.LOG_BASE_ROTATION_SPEED
                       * Constants.ROTATION_MULT_PER_STAGE[stageIndex];

        // Apple positions — deterministic per stage
        int appleCount  = Constants.APPLE_COUNT_PER_STAGE[stageIndex];
        appleAngles     = new float[appleCount];
        appleCollected  = new boolean[appleCount];
        java.util.Random rng = new java.util.Random(stageIndex * 73856L + 17L);
        for (int i = 0; i < appleCount; i++) {
            appleAngles[i] = rng.nextFloat() * 360f;
        }

        // Load blade texture (not in core assets)
        if (!game.manager.isLoaded(BLADE_TEX)) {
            game.manager.load(BLADE_TEX, Texture.class);
            game.manager.finishLoading();
        }
        bladeSprite = new Sprite(game.manager.get(BLADE_TEX, Texture.class));
        bladeSprite.setSize(Constants.BLADE_WIDTH, Constants.BLADE_HEIGHT);
        bladeSprite.setOriginCenter();

        shapeRenderer = new ShapeRenderer();

        buildHUD();
        setupInput();
    }

    // ── HUD ───────────────────────────────────────────────────────────────────
    private void buildHUD() {
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,
                Color.valueOf(Constants.COLOR_PARCHMENT));
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall,
                Color.valueOf(Constants.COLOR_PARCHMENT));

        // Stage label — FIGMA top-Y=24, h=36 → libgdxY=794
        Label stageLbl = new Label("STAGE " + (stageIndex + 1), bodyStyle);
        stageLbl.setAlignment(Align.center);
        stageLbl.setWidth(200f);
        stageLbl.setPosition((Constants.WORLD_WIDTH - 200f) / 2f, 794f);
        hudStage.addActor(stageLbl);

        // Score — FIGMA top-Y=64, h=44 → libgdxY=746
        scoreLbl = new Label("0", bodyStyle);
        scoreLbl.setAlignment(Align.center);
        scoreLbl.setWidth(200f);
        scoreLbl.setPosition((Constants.WORLD_WIDTH - 200f) / 2f, 746f);
        hudStage.addActor(scoreLbl);

        // Blades remaining — FIGMA top-Y=110, h=36 → libgdxY=708
        bladesLbl = new Label("x" + bladesLeft, smallStyle);
        bladesLbl.setAlignment(Align.center);
        bladesLbl.setWidth(180f);
        bladesLbl.setPosition((Constants.WORLD_WIDTH - 180f) / 2f, 708f);
        hudStage.addActor(bladesLbl);

        // Pause button — FIGMA top-Y=20, size=52×52, right@16 → libgdxY=782, x=412
        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontSmall);
        TextButton pauseBtn = UiFactory.makeButton("||", roundStyle,
                Constants.BTN_ROUND, Constants.BTN_ROUND);
        pauseBtn.setColor(Color.valueOf(Constants.COLOR_PRIMARY));
        pauseBtn.getLabel().setColor(Color.valueOf(Constants.COLOR_BG));
        pauseBtn.setPosition(Constants.WORLD_WIDTH - 16f - Constants.BTN_ROUND, 782f);
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                if (game.sfxEnabled)
                    game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
                game.setScreen(new PauseScreen(game, GameScreen.this, stageIndex));
            }
        });
        hudStage.addActor(pauseBtn);
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputMultiplexer(
            hudStage,
            new InputAdapter() {
                @Override
                public boolean touchDown(int sx, int sy, int pointer, int button) {
                    if (state == State.WAITING && bladesLeft > 0 && !navigating) {
                        throwBlade();
                        return true;
                    }
                    return false;
                }
                @Override
                public boolean keyDown(int keycode) {
                    if (keycode == Input.Keys.BACK) {
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                    }
                    return false;
                }
            }
        ));
    }

    // ── Blade mechanics ───────────────────────────────────────────────────────
    private void throwBlade() {
        movingBladeY = Constants.BLADE_SPAWN_Y;
        state = State.BLADE_IN_FLIGHT;
        bladesLeft--;
        updateHUD();
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_shoot.ogg", Sound.class).play(0.8f);
    }

    private void checkImpact() {
        // Blade always hits from directly below → world angle 270°
        float impactAngle = normalizeAngle(270f - logAngle);

        // Collision check with stuck blades
        for (int i = 0; i < stuckAngles.size; i++) {
            if (angleDiff(impactAngle, stuckAngles.get(i)) < Constants.MIN_BLADE_ANGLE_GAP) {
                triggerGameOver();
                return;
            }
        }

        // Apple collection
        for (int i = 0; i < appleAngles.length; i++) {
            if (!appleCollected[i] && angleDiff(impactAngle, appleAngles[i]) < APPLE_COLLECT_DEG) {
                appleCollected[i] = true;
                score += Constants.SCORE_PER_APPLE;
                if (game.sfxEnabled)
                    game.manager.get("sounds/sfx/sfx_collect.ogg", Sound.class).play(1.0f);
            }
        }

        // Stick blade
        stuckAngles.add(impactAngle);
        score += Constants.SCORE_PER_BLADE;
        state = State.WAITING;
        updateHUD();

        if (stuckAngles.size == bladesTotal) {
            triggerStageClear();
        }
    }

    private void triggerGameOver() {
        if (navigating) return;
        navigating = true;
        state = State.GAME_OVER;
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg", Sound.class).play(1.0f);
        if (game.vibrationEnabled) {
            try { Gdx.input.vibrate(200); } catch (Exception ignored) {}
        }
        // Persist high score
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        int best = prefs.getInteger(Constants.PREF_HIGH_SCORE, 0);
        if (score > best) {
            prefs.putInteger(Constants.PREF_HIGH_SCORE, score);
            prefs.flush();
        }
        game.setScreen(new GameOverScreen(game, score, stageIndex));
    }

    private void triggerStageClear() {
        if (navigating) return;
        navigating = true;
        state = State.STAGE_CLEAR;
        score += Constants.SCORE_CLEAR_BONUS;
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_level_complete.ogg", Sound.class).play(1.0f);
        game.setScreen(new StageClearScreen(game, stageIndex, score));
    }

    private void updateHUD() {
        scoreLbl.setText(String.valueOf(score));
        bladesLbl.setText("x" + Math.max(0, bladesLeft));
    }

    // ── Math helpers ──────────────────────────────────────────────────────────
    private float normalizeAngle(float deg) {
        deg = deg % 360f;
        if (deg < 0f) deg += 360f;
        return deg;
    }

    private float angleDiff(float a, float b) {
        float d = Math.abs(a - b) % 360f;
        return d > 180f ? 360f - d : d;
    }

    // ── Update ────────────────────────────────────────────────────────────────
    private void update(float delta) {
        logAngle = normalizeAngle(logAngle + logRotSpeed * delta);

        if (state == State.BLADE_IN_FLIGHT) {
            movingBladeY += Constants.BLADE_SPEED * delta;
            if (movingBladeY >= Constants.LOG_CENTER_Y - Constants.LOG_RADIUS) {
                checkImpact();
            }
        }
    }

    // ── Screen ────────────────────────────────────────────────────────────────
    @Override
    public void show() {
        game.playMusic("sounds/music/music_gameplay.ogg");
        setupInput();
    }

    @Override
    public void render(float delta) {
        if (!navigating) {
            update(delta);
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        // ── 1. Background ──────────────────────────────────────────────────
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(game.manager.get("ui/game_screen.png", Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        // ── 2. Log + apples (ShapeRenderer) ──────────────────────────────
        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Log face — three concentric circles for growth-ring look
        shapeRenderer.setColor(0.24f, 0.12f, 0.04f, 1f);
        shapeRenderer.circle(Constants.LOG_CENTER_X, Constants.LOG_CENTER_Y, Constants.LOG_RADIUS, 64);
        shapeRenderer.setColor(0.18f, 0.09f, 0.03f, 1f);
        shapeRenderer.circle(Constants.LOG_CENTER_X, Constants.LOG_CENTER_Y,
                Constants.LOG_RADIUS * 0.65f, 48);
        shapeRenderer.setColor(0.12f, 0.06f, 0.02f, 1f);
        shapeRenderer.circle(Constants.LOG_CENTER_X, Constants.LOG_CENTER_Y,
                Constants.LOG_RADIUS * 0.30f, 32);

        // Apples (red circles on log rim, rotating with log)
        for (int i = 0; i < appleAngles.length; i++) {
            if (!appleCollected[i]) {
                float wAngle = appleAngles[i] + logAngle;
                float rad    = (float) Math.toRadians(wAngle);
                float ax     = Constants.LOG_CENTER_X + (float) Math.cos(rad) * Constants.LOG_RADIUS;
                float ay     = Constants.LOG_CENTER_Y + (float) Math.sin(rad) * Constants.LOG_RADIUS;
                shapeRenderer.setColor(Color.valueOf(Constants.COLOR_ACCENT));
                shapeRenderer.circle(ax, ay, Constants.APPLE_RADIUS, 24);
                // White highlight
                shapeRenderer.setColor(1f, 1f, 1f, 0.4f);
                shapeRenderer.circle(ax - Constants.APPLE_RADIUS * 0.25f,
                        ay + Constants.APPLE_RADIUS * 0.25f, Constants.APPLE_RADIUS * 0.2f, 12);
            }
        }
        shapeRenderer.end();

        // ── 3. Blade sprites ──────────────────────────────────────────────
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Stuck blades — rotate with log
        for (int i = 0; i < stuckAngles.size; i++) {
            float logAngleDeg = stuckAngles.get(i);
            float worldDeg    = logAngleDeg + logAngle;
            float rad         = (float) Math.toRadians(worldDeg);
            float cx = Constants.LOG_CENTER_X
                       + (float) Math.cos(rad) * (Constants.LOG_RADIUS + Constants.BLADE_HEIGHT * 0.25f);
            float cy = Constants.LOG_CENTER_Y
                       + (float) Math.sin(rad) * (Constants.LOG_RADIUS + Constants.BLADE_HEIGHT * 0.25f);
            bladeSprite.setCenter(cx, cy);
            bladeSprite.setRotation(worldDeg - 90f);
            bladeSprite.draw(game.batch);
        }

        // Moving blade — travels straight up, unrotated
        if (state == State.BLADE_IN_FLIGHT) {
            bladeSprite.setCenter(Constants.BLADE_SPAWN_X, movingBladeY);
            bladeSprite.setRotation(0f);
            bladeSprite.draw(game.batch);
        }

        game.batch.end();

        // ── 4. HUD (stage never pauses act()) ─────────────────────────────
        hudStage.act(delta);
        hudStage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        hudStage.dispose();
        shapeRenderer.dispose();
    }
}
