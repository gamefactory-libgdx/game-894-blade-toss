# Figma AI Design Brief — Blade Toss

---

## 1. Art Style & Color Palette

**Style:** Dark, moody woodcraft aesthetic — think a forest workshop at dusk. Hand-hewn textures, warm candlelight glow, and sharp metallic gleam. The tone is tense and satisfying: rustic meets precision. Illustrations lean semi-realistic with slightly stylized proportions — chunky blades, knotted wood grain, dramatic lighting.

**Primary Palette:**
- `#1A0E06` — Deep charcoal-brown (background base)
- `#3D1F0A` — Warm dark oak (mid tones, log surfaces)
- `#C8762A` — Amber lantern glow (highlights, UI warmth)
- `#E8D9B5` — Aged parchment (text panels, light diffuse)

**Accent:**
- `#D4E8FF` — Cold steel shimmer (blade edges, metallic glints)
- `#C1392B` — Blood apple red (apple collectible, danger cues)

**Font Mood:** Heavy, slightly weathered display typeface — wood-carved lettering feel. Bold serifs or chiseled block fonts. Numbers should read at a glance under tension.

---

## 2. App Icon — icon_512.png (512×512px)

**Export path:** `icon_512.png` (root directory)

A square canvas filled with a deep radial gradient from `#3D1F0A` at center to `#0E0804` at corners, evoking a spotlight on a dark stage. The central symbol is a thick throwing blade — wide crescent shape, aged steel with a cold blue-white edge highlight (`#D4E8FF`) and warm amber reflection on the spine. The blade is angled 35° clockwise, caught mid-flight. Behind it, a faint circular cross-section of a wooden log peeks in the lower-center — dark oak with visible growth rings. A tight orange-gold halo glow (`#C8762A`, ~40px soft blur) radiates from behind the blade to create depth and drama. Mood: tense, kinetic, confident.

---

## 3. UI Screens (480×854 portrait)

---

### MainMenuScreen

**A) BACKGROUND IMAGE**
Full canvas painted in deep dark-brown gradient (`#1A0E06` top → `#0E0702` bottom). A large rotating wooden log dominates the center-left, rendered in rich oak grain with soft amber rim lighting — blades already lodged in it cast elongated shadows. The right side shows a blurred forest silhouette in dark teal-green at extreme low opacity for depth. Warm lantern-glow particles drift upward. A blank carved wooden banner frame (no text) hangs at the upper-center — empty, decorative, with rope knots at corners. The bottom-third has empty dark parchment panel shape for button placement — framed with thin amber border, no content inside.

**B) BUTTON LAYOUT**
```
BLADE TOSS (title label) | top-Y=80px  | x=centered | size=340x70
PLAY                     | top-Y=420px | x=centered | size=260x60
STAGE SELECT             | top-Y=500px | x=centered | size=260x52
LEADERBOARD              | top-Y=570px | x=centered | size=260x52
HOW TO PLAY              | top-Y=640px | x=centered | size=260x52
SETTINGS                 | top-Y=780px | x=right@20px | size=56x56
```

---

### StageSelectScreen

**A) BACKGROUND IMAGE**
Dark warm background (`#1A0E06`) with a horizontal wood-plank wall texture across the full canvas — aged pine boards, knothole details, faint vertical grain. A warm amber vignette glows from center-screen. Ten empty rounded-rectangle card frames are arranged in a 2×5 grid (rows of 2), each with a slight inner bevel and dark oak color fill — blank inside, purely decorative. Each card frame has a thin rope-loop motif at its top corner. The overall feel is a dartboard wall at a rustic carnival. No numbers, no text, no lock icons.

**B) BUTTON LAYOUT**
```
STAGE SELECT (title)  | top-Y=40px  | x=centered        | size=300x54
STAGE 1               | top-Y=140px | x=left@40px       | size=180x80
STAGE 2               | top-Y=140px | x=right@40px      | size=180x80
STAGE 3               | top-Y=240px | x=left@40px       | size=180x80
STAGE 4               | top-Y=240px | x=right@40px      | size=180x80
STAGE 5               | top-Y=340px | x=left@40px       | size=180x80
STAGE 6               | top-Y=340px | x=right@40px      | size=180x80
STAGE 7               | top-Y=440px | x=left@40px       | size=180x80
STAGE 8               | top-Y=440px | x=right@40px      | size=180x80
STAGE 9               | top-Y=540px | x=left@40px       | size=180x80
STAGE 10              | top-Y=540px | x=right@40px      | size=180x80
BACK                  | top-Y=790px | x=left@20px       | size=110x44
```

---

### GameScreen

**A) BACKGROUND IMAGE**
Nearly black deep-space background (`#0E0804`) with a subtle radial warm glow at center (`#3D1F0A`, large soft bloom, ~300px radius) — as if a single light source illuminates the center stage. The extreme edges fade to pure black with faint ember-particle streaks drifting upward at low opacity. The bottom portion has a narrow dark stone ledge texture strip — the "throw platform" base visual. Upper corners carry faint carved-wood border filigree motifs. No HUD elements, no text. The scene is spare, tense, atmospheric — the game engine draws the log, blades, score, and tap zone.

**B) BUTTON LAYOUT**
```
STAGE label (e.g. "STAGE 3") | top-Y=24px  | x=centered     | size=200x36
SCORE value                  | top-Y=64px  | x=centered     | size=200x44
BLADES REMAINING counter     | top-Y=110px | x=centered     | size=180x36
PAUSE                        | top-Y=20px  | x=right@16px   | size=52x52
(tap anywhere lower half = throw — no button needed)
```

---

### StageClearScreen

**A) BACKGROUND IMAGE**
Celebration composition on warm amber-gold gradient (`#C8762A` center → `#1A0E06` edges). A shower of maple-leaf and wood-chip particle confetti fills the upper two-thirds — hand-drawn style in amber, cream, and red-brown tones. Center stage holds a wide empty decorative banner frame — scroll-shape with wooden end-caps, blank interior, no text — large enough to fill 360×120px of vertical space. Below the banner is an empty parchment card frame (rounded rectangle, cream border, dark fill). Warm golden lens flare cuts diagonally across from upper-right. Mood is triumphant and warm.

**B) BUTTON LAYOUT**
```
STAGE CLEAR! (label)   | top-Y=80px  | x=centered | size=320x60
SCORE display          | top-Y=180px | x=centered | size=280x52
BEST display           | top-Y=245px | x=centered | size=280x40
NEXT STAGE             | top-Y=540px | x=centered | size=260x60
RETRY                  | top-Y=618px | x=centered | size=260x52
MENU                   | top-Y=696px | x=centered | size=260x52
```

---

### GameOverScreen

**A) BACKGROUND IMAGE**
Dark, desaturated background (`#0E0804` full) with a cracked wooden surface texture filling center-canvas — as if the log shattered on impact. Deep red-brown blood-like splatter stain (`#4A0A00`, low opacity, organic blot shape) sits center where the fatal collision happened. Broken blade fragment silhouettes scatter outward from center at various angles — dark steel shapes, no color detail. A faint red vignette rings the entire canvas edge (`#C1392B`, low opacity). An empty dark card frame with cracked-wood border sits in the lower-center for score display — blank inside. The mood is abrupt and dramatic.

**B) BUTTON LAYOUT**
```
GAME OVER (label)    | top-Y=100px | x=centered | size=320x64
SCORE display        | top-Y=210px | x=centered | size=280x52
BEST display         | top-Y=272px | x=centered | size=280x40
RETRY                | top-Y=520px | x=centered | size=260x60
STAGE SELECT         | top-Y=598px | x=centered | size=260x52
MENU                 | top-Y=676px | x=centered | size=260x52
```

---

### LeaderboardScreen

**A) BACKGROUND IMAGE**
Rich dark-oak paneling background — warm horizontal wood grain texture at low contrast across full canvas. Soft amber candlelight glow from a central top-down source creates a warm spotlight oval. A tall empty scroll-shaped frame (parchment cream, `#E8D9B5`, with rolled top and bottom ends) occupies center canvas — blank inside, purely decorative. Left and right edges carry thin carved-border rope motifs. Upper portion has an empty carved-wood trophy silhouette shape embossed into the background at 15% opacity — decorative, no detail. Mood: old guild hall, hall of fame.

**B) BUTTON LAYOUT**
```
LEADERBOARD (title)  | top-Y=40px  | x=centered     | size=320x52
RANK / NAME / SCORE headers | top-Y=140px | x=centered | size=440x36
Row 1 entry          | top-Y=186px | x=centered     | size=440x44
Row 2 entry          | top-Y=238px | x=centered     | size=440x44
Row 3 entry          | top-Y=290px | x=centered     | size=440x44
Row 4 entry          | top-Y=342px | x=centered     | size=440x44
Row 5 entry          | top-Y=394px | x=centered     | size=440x44
Row 6 entry          | top-Y=446px | x=centered     | size=440x44
Row 7 entry          | top-Y=498px | x=centered     | size=440x44
Row 8 entry          | top-Y=550px | x=centered     | size=440x44
BACK                 | top-Y=790px | x=left@20px    | size=110x44
```

---

### SettingsScreen

**A) BACKGROUND IMAGE**
Dark warm background (`#1A0E06`) with a subtle vertical wood-plank texture across full canvas at 20% opacity. A centered blank parchment panel — tall rounded-rectangle, `#E8D9B5` tint border, dark fill — occupies the middle 380×500px area of canvas as a decorative empty frame. Faint hand-drawn blade and woodgrain doodle motifs float at low opacity in the panel corners. Warm amber glow source from above-center casts soft downward light on the panel. Clean, focused, no clutter.

**B) BUTTON LAYOUT**
```
SETTINGS (title)     | top-Y=60px  | x=centered     | size=280x52
MUSIC toggle label   | top-Y=200px | x=left@80px    | size=200x44
MUSIC toggle switch  | top-Y=200px | x=right@80px   | size=80x44
SFX toggle label     | top-Y=264px | x=left@80px    | size=200x44
SFX toggle switch    | top-Y=264px | x=right@80px   | size=80x44
VIBRATION label      | top-Y=328px | x=left@80px    | size=200x44
VIBRATION switch     | top-Y=328px | x=right@80px   | size=80x44
BACK                 | top-Y=790px | x=left@20px    | size=110x44
```

---

### HowToPlayScreen

**A) BACKGROUND IMAGE**
Warm dark background with a large illustrated instructional mural feel — three empty decorative step-frames arranged vertically (tall rounded rectangles with step-number notch cutouts on the left edge, blank inside). Each frame has an amber outline border and subtle wood-grain fill. Between frames, thin dotted arrow lines point downward in amber. A small decorative red apple silhouette motif sits in the upper-right corner at low opacity. The canvas has a light warm vignette and scattered sawdust particle texture. No text, no icons — purely the framing structure.

**B) BUTTON LAYOUT**
```
HOW TO PLAY (title)          | top-Y=40px  | x=centered | size=300x52
Step 1 frame + description   | top-Y=120px | x=centered | size=400x160
Step 2 frame + description   | top-Y=300px | x=centered | size=400x160
Step 3 frame + description   | top-Y=480px | x=centered | size=400x160
PLAY NOW                     | top-Y=700px | x=centered | size=260x60
BACK                         | top-Y=790px | x=left@20px | size=110x44
```

---

## 4. Export Checklist

```
- icon_512.png (512x512)
- ui/main_menu.png (480x854)
- ui/stage_select.png (480x854)
- ui/game_screen.png (480x854)
- ui/stage_clear.png (480x854)
- ui/game_over.png (480x854)
- ui/leaderboard.png (480x854)
- ui/settings.png (480x854)
- ui/how_to_play.png (480x854)
- feature_banner.png (1024x500)
```

---

## 5. Feature Banner — feature_banner.png (1024×500 landscape)

A wide cinematic landscape (1024×500) with full-bleed dark warm atmosphere. The left two-thirds shows a dramatic action scene: a large oak log cross-section in the center-right of the composition, lit by a tight warm spotlight from above, with five throwing blades lodged at varying angles — cold steel glinting in the amber light. One blade is shown mid-flight from the lower-left, motion blur trailing behind it, heading toward the log. A bright red apple sits prominently on the log surface, catching the light with an extra gleam. The background is near-black (`#0E0804`) with a warm brown fog/haze layer and faint forest silhouettes in deep teal at the far edges. Game title **"BLADE TOSS"** appears in large chiseled, wood-carved bold lettering positioned left-center to center — letter color cream/amber (`#E8D9B5`) with a dark wood-grain texture overlaid on the glyphs and a cold steel underline accent. Tagline area below the title is left intentionally empty. The overall mood is tense, skilled, cinematic — precision craft meets dark drama.