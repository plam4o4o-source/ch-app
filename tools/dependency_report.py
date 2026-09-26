#!/usr/bin/env python3
"""Сравнява версиите в gradle/libs.versions.toml с последните стабилни в Maven Central и Google Maven.
Изпълнение: python3 tools/dependency_report.py  (нужен е достъп до dl.google.com и repo1.maven.org)."""
import re, sys, urllib.request, tomllib

PLUGIN_ARTIFACTS = {
    "com.android.application": "com.android.tools.build:gradle",
    "com.google.devtools.ksp": "com.google.devtools.ksp:symbol-processing-gradle-plugin",
    "androidx.room": "androidx.room:room-gradle-plugin",
}
UNSTABLE = re.compile(r"(?i)(alpha|beta|rc|-m\d|dev|eap|snapshot|pre)")

def latest(coord):
    g, a = coord.split(":")
    path = g.replace(".", "/") + "/" + a + "/maven-metadata.xml"
    for base in ("https://dl.google.com/android/maven2/", "https://repo1.maven.org/maven2/", "https://plugins.gradle.org/m2/"):
        try:
            x = urllib.request.urlopen(base + path, timeout=30).read().decode()
        except Exception:
            continue
        vs = [v for v in re.findall(r"<version>([^<]+)</version>", x) if not UNSTABLE.search(v)]
        if vs:
            return vs[-1]
    return "?"

def main():
    t = tomllib.load(open("gradle/libs.versions.toml", "rb"))
    versions = t["versions"]
    seen = {}
    for name, lib in t["libraries"].items():
        if isinstance(lib, dict) and "version" in lib and isinstance(lib["version"], dict) and "ref" in lib["version"]:
            ref = lib["version"]["ref"]
        elif isinstance(lib, dict) and "version.ref" in lib:
            ref = lib["version.ref"]
        else:
            continue
        seen.setdefault(ref, lib["module"])
    for name, p in t["plugins"].items():
        ref = p.get("version", {}).get("ref") if isinstance(p.get("version"), dict) else p.get("version.ref")
        pid = p["id"]
        coord = PLUGIN_ARTIFACTS.get(pid, "org.jetbrains.kotlin:kotlin-gradle-plugin" if pid.startswith("org.jetbrains.kotlin") else None)
        if ref and coord:
            seen.setdefault(ref, coord)
    print(f"{'key':<24}{'current':<22}{'latest stable':<22}module")
    for ref, coord in sorted(seen.items()):
        cur = versions.get(ref, "?")
        new = latest(coord)
        flag = "" if new in ("?", cur) else "  <-- update"
        print(f"{ref:<24}{cur:<22}{new:<22}{coord}{flag}")
    print("gradle wrapper:", urllib.request.urlopen("https://services.gradle.org/versions/current").read().decode().split('"version" : "')[1].split('"')[0])

if __name__ == "__main__":
    sys.exit(main())
