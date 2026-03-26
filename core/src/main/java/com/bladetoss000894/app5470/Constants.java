package com.bladetoss000894.app5470;

public class Constants {

    // ── World dimensions ──────────────────────────────────────────────────────
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // ── Color palette (from Figma brief) ─────────────────────────────────────
    public static final String COLOR_BG        = "#1A0E06"; // Deep charcoal-brown
    public static final String COLOR_BG_MID    = "#3D1F0A"; // Warm dark oak
    public static final String COLOR_PRIMARY   = "#C8762A"; // Amber lantern glow
    public static final String COLOR_PARCHMENT = "#E8D9B5"; // Aged parchment
    public static final String COLOR_STEEL     = "#D4E8FF"; // Cold steel shimmer
    public static final String COLOR_ACCENT    = "#C1392B"; // Blood apple red

    // ── Log / rotation ───────────────────────────────────────────────────────
    public static final float LOG_RADIUS              = 90f;  // world units
    public static final float LOG_BASE_ROTATION_SPEED = 40f;  // degrees / sec
    public static final float LOG_ROTATION_SPEEDUP    = 5f;   // extra degrees/sec per stage
    public static final float LOG_CENTER_X             = WORLD_WIDTH  / 2f;
    public static final float LOG_CENTER_Y             = WORLD_HEIGHT * 0.52f;

    // ── Blade physics ─────────────────────────────────────────────────────────
    public static final float BLADE_SPEED     = 1400f; // world units / sec
    public static final float BLADE_WIDTH     = 14f;
    public static final float BLADE_HEIGHT    = 52f;
    public static final float BLADE_SPAWN_X   = WORLD_WIDTH / 2f;
    public static final float BLADE_SPAWN_Y   = 130f;

    // ── Collision ─────────────────────────────────────────────────────────────
    /** Minimum angle between any two stuck blades (degrees) */
    public static final float MIN_BLADE_ANGLE_GAP = 15f;

    // ── Stage configuration ───────────────────────────────────────────────────
    public static final int   STAGE_COUNT              = 10;
    public static final int[] BLADES_PER_STAGE         = {5, 6, 6, 7, 7, 8, 8, 9, 9, 10};
    public static final int[] APPLE_COUNT_PER_STAGE    = {1, 1, 1, 1, 2, 1, 2, 1, 2, 2};
    /** Rotation speed multiplier per stage (index 0 = stage 1) */
    public static final float[] ROTATION_MULT_PER_STAGE = {
        1.0f, 1.1f, 1.25f, 1.4f, 1.55f, 1.7f, 1.9f, 2.1f, 2.35f, 2.6f
    };

    // ── Scoring ───────────────────────────────────────────────────────────────
    public static final int SCORE_PER_BLADE   = 10;
    public static final int SCORE_PER_APPLE   = 50;
    public static final int SCORE_CLEAR_BONUS = 100;

    // ── Apple collectible ─────────────────────────────────────────────────────
    public static final float APPLE_RADIUS = 22f;
    public static final float APPLE_SIZE   = 44f;

    // ── UI layout helpers ─────────────────────────────────────────────────────
    public static final float BTN_W_MAIN   = 260f;
    public static final float BTN_H_MAIN   = 60f;
    public static final float BTN_W_SEC    = 260f;
    public static final float BTN_H_SEC    = 52f;
    public static final float BTN_W_SMALL  = 110f;
    public static final float BTN_H_SMALL  = 44f;
    public static final float BTN_ROUND    = 56f;

    // ── SharedPreferences keys ────────────────────────────────────────────────
    public static final String PREFS_NAME      = "BladeTossPrefs";
    public static final String PREF_HIGH_SCORE = "highScore";
    public static final String PREF_MUSIC      = "musicEnabled";
    public static final String PREF_SFX        = "sfxEnabled";
    public static final String PREF_VIBRATION  = "vibrationEnabled";
    public static final String PREF_STAGE      = "unlockedStage";
    public static final String PREF_STAGE_BEST = "stageBest_"; // append stage index

    // ── Font sizes ────────────────────────────────────────────────────────────
    public static final int FONT_SIZE_TITLE = 52;
    public static final int FONT_SIZE_BODY  = 28;
    public static final int FONT_SIZE_SMALL = 20;
    public static final int FONT_SIZE_SCORE = 42;
}
