TEMPORARY diagnosis on debug/w27-cat: which work on the UI thread makes the rare slow frames of the „Каталог“ tab
in the released 1.2.39 (field data: worst UI frame 104 ms on the „catalog“ route, 6 of 3161 frames janky).

- `cat33`, `cat36x`, `cat26` — the released code (main ab175e49) as prodRelease (minified) x86_64 with a throwaway key.
- `cath33`, `cath36x`, `cath26` — the same plus `w27-hook.patch` (DEBUG ONLY, never released): every main-looper
  message is a trace section (`msg <handler> <callback>`), the app is profileable by shell, and
  `androidx.compose.runtime:runtime-tracing` is added for composable names (if it works).
- Each interaction is measured alone: gfxinfo framestats (reset before, dumped after) and a trace of just that
  interaction (Perfetto with sched + atrace gfx/view/am/dalvik/input/wm/res/binder/aidl on API 29+, text atrace on
  API 26), analysed with TraceProcessor (`w27.py`). `ui` = like JankStats frameDurationUiNanos (vsync → end of the
  sync, including the wait before doFrame). `DOFRAME` blocks are the slowest Choreographer#doFrame slices with their
  slice tree, `FS-FRAME` maps a slow framestats frame to the main-thread work between its vsync and its sync,
  `LONG-MAIN` is main-thread work over 30 ms outside frames.
- Emulator GPU is software (swangle): RenderThread times are inflated, UI-thread times are comparable relatively.
- Traces with a main-thread slice over 32 ms are kept, gzipped, in `<part>/tr/` (open in ui.perfetto.dev).
