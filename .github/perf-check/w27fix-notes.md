TEMPORARY A/B on debug/w27-cat: the catalogue entry-frame fix (claude/charming-rubin-3lzor2 @ ae598c45 — rows
composed one frame later on a tab switch; the small logo decoded once per process) against 1.2.39 as released
(ab175e49). Both plain prodRelease (minified) x86_64 with a throwaway key, side by side: NEW = org.chyavorec.app,
BASE = org.chyavorec.app.b, both speed-profile; rounds alternate the order.

- Per entry (tap on Каталог from Home / from News, cold first entry after a process start, Back from a book): a
  Perfetto trace; `worst` = the highest main-thread CPU (Running time) of one doFrame after the input; `entry frame
  + next` = the first doFrame with ≥ 8 ms CPU after the input plus the one after it; `first 4 frames with work` =
  four doFrames from the first with ≥ 3 ms. Wall times on this software-GPU emulator are mostly waits for the
  RenderThread and are not used.
- `logo decodes` = `res/*.webp` slices (logo_chitalishte_small through painterResource) on the main thread.
- Visual checks: `shots/fx/` (0.25–0.85 s after a tab switch, after 3 s, scrolled list before Home and after Home →
  Каталог), the Back from a book recorded at 20 frames/s in `frames/fxback-<build>/` and `sheets/`.
