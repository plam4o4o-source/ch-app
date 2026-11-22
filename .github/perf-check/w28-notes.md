TEMPORARY A/B on debug/w27-cat: the frames that are still slow after 1.2.40, main-thread CPU (Running time) per
doFrame from a Perfetto trace of each interaction (as in [../v139fix/index.md](../v139fix/index.md)).

- BASE = org.chyavorec.app.b, NEW = org.chyavorec.app; both prodRelease (minified) x86_64, throwaway key,
  speed-profile; each variant in a new process, rounds alternate the order.
- When NEW is built with `w28-exp.patch` (DEBUG ONLY, never released), variants are chosen by launch flags
  (`--es w28 <flags>`); a variant label is `base`, `x` (NEW without flags) or `x-<flag>+<flag>`:
  `bk1` — the first frame after the return from a book composes only the row of that book, the other visible rows are
  spacers of their last height; `bk2` — the other rows without their generated cover (its colour only) in that frame;
  `one` — only the row of the last opened book is a shared element; `nosh` — list covers without the shadow layer;
  `ta` — rows `animateItem` without fade in/out; `tb` — no `animateItem`; `tc` — without fade out; `sug` —
  suggestions without AnimatedVisibility.
- Parts: `bk33`/`bk36x` — Back from a book (rows 1–3, key Back); `ty33`/`ty36x` — typing 3 words in the catalogue
  search (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“ (found by its text after
  slow swipes), a fling (6 up, 4 down) and a slow drag; `tb33`/`tb36x` — entering each main tab (first entry in the
  process, then warm). API 33 unless the part name ends in 36x (API 36).
- Columns: `worst` — the highest CPU of one doFrame in the window; `entry frame` — the first doFrame with ≥ 8 ms,
  `next` — the one after it; frames over 25/30 ms in total; `rows` — catalogue rows composed in the worst frame
  (experimental build only).
- Wall times on this software-GPU emulator are mostly waits for the RenderThread and are not used.
