#!/usr/bin/env python3
"""Пръстенът на иконата — осморъката от TabRosetteShapes.rosette (TabRosette.kt):
star(8 върха, радиус 1, вътрешен 0.8, закръгляне 0.2/гладкост 0.5, вътрешно 0.1),
пресметнат като в graphics-shapes (RoundedPolygon) и изписан като pathData на векторите.
Пускане: python3 tools/gen_icon_rosette.py [радиус] — отпечатва пътя за 108×108 платно,
центриран в (54,54); обърнат така, че един лъч да е нагоре.
"""
import math
import sys


def norm(x, y):
    d = math.hypot(x, y)
    return (x / d, y / d) if d else (0.0, 0.0)


class Corner:
    def __init__(self, p0, p1, p2, radius, smoothing):
        self.p0, self.p1, self.p2 = p0, p1, p2
        self.d1 = norm(p0[0] - p1[0], p0[1] - p1[1])
        self.d2 = norm(p2[0] - p1[0], p2[1] - p1[1])
        self.radius, self.smoothing = radius, smoothing
        cos = self.d1[0] * self.d2[0] + self.d1[1] * self.d2[1]
        sin = math.sqrt(max(0.0, 1 - cos * cos))
        self.round_cut = radius * (cos + 1) / sin if sin > 1e-3 else 0.0
        self.center = p1

    @property
    def cut(self):
        return (1 + self.smoothing) * self.round_cut

    def actual_smoothing(self, allowed):
        if allowed > self.cut:
            return self.smoothing
        if allowed > self.round_cut:
            return self.smoothing * (allowed - self.round_cut) / (self.cut - self.round_cut)
        return 0.0

    def cubics(self, a0, a1):
        allowed = min(a0, a1)
        if self.round_cut < 1e-4 or allowed < 1e-4 or self.radius < 1e-4:
            return [line(self.p1, self.p1)]
        actual_cut = min(allowed, self.round_cut)
        s0, s1 = self.actual_smoothing(a0), self.actual_smoothing(a1)
        r = self.radius * actual_cut / self.round_cut
        cd = math.hypot(r, actual_cut)
        mid = norm((self.d1[0] + self.d2[0]) / 2, (self.d1[1] + self.d2[1]) / 2)
        c = (self.p1[0] + mid[0] * cd, self.p1[1] + mid[1] * cd)
        i0 = (self.p1[0] + self.d1[0] * actual_cut, self.p1[1] + self.d1[1] * actual_cut)
        i2 = (self.p1[0] + self.d2[0] * actual_cut, self.p1[1] + self.d2[1] * actual_cut)
        f0 = self.flank(actual_cut, s0, self.p0, i0, i2, c, r)
        f2 = self.flank(actual_cut, s1, self.p2, i2, i0, c, r)
        f2 = [f2[3], f2[2], f2[1], f2[0]]
        return [f0, arc(c, f0[3], f2[0]), f2]

    def flank(self, cut, smooth, side_start, inter, other, c, r):
        side = norm(side_start[0] - self.p1[0], side_start[1] - self.p1[1])
        start = (self.p1[0] + side[0] * cut * (1 + smooth), self.p1[1] + side[1] * cut * (1 + smooth))
        mid = ((inter[0] + other[0]) / 2, (inter[1] + other[1]) / 2)
        p = (inter[0] + (mid[0] - inter[0]) * smooth, inter[1] + (mid[1] - inter[1]) * smooth)
        dir_ = norm(p[0] - c[0], p[1] - c[1])
        end = (c[0] + dir_[0] * r, c[1] + dir_[1] * r)
        tangent = (-(end[1] - c[1]), end[0] - c[0])
        anchor_end = line_intersection(side_start, side, end, tangent) or inter
        anchor_start = ((start[0] + anchor_end[0] * 2) / 3, (start[1] + anchor_end[1] * 2) / 3)
        return [start, anchor_start, anchor_end, end]


def line(a, b):
    return [a, ((2 * a[0] + b[0]) / 3, (2 * a[1] + b[1]) / 3), ((a[0] + 2 * b[0]) / 3, (a[1] + 2 * b[1]) / 3), b]


def line_intersection(p0, d0, p1, d1):
    rx, ry = d0
    sx, sy = d1
    den = rx * sy - ry * sx
    if abs(den) < 1e-4:
        return None
    qpx, qpy = p1[0] - p0[0], p1[1] - p0[1]
    t = (qpx * sy - qpy * sx) / den
    return (p0[0] + rx * t, p0[1] + ry * t)


def arc(c, p0, p1):
    d0 = norm(p0[0] - c[0], p0[1] - c[1])
    d1 = norm(p1[0] - c[0], p1[1] - c[1])
    r0 = (-d0[1], d0[0])
    r1 = (-d1[1], d1[0])
    clockwise = r0[0] * (p1[0] - c[0]) + r0[1] * (p1[1] - c[1]) >= 0
    cosa = d0[0] * d1[0] + d0[1] * d1[1]
    if cosa > 0.999:
        return line(p0, p1)
    k = math.hypot(p0[0] - c[0], p0[1] - c[1]) * 4 / 3 * (math.sqrt(2 * (1 - cosa)) - math.sqrt(1 - cosa * cosa)) / (1 - cosa)
    k *= 1 if clockwise else -1
    return [p0, (p0[0] + r0[0] * k, p0[1] + r0[1] * k), (p1[0] - r1[0] * k, p1[1] - r1[1] * k), p1]


def rosette():
    n = 8
    verts, rounds = [], []
    for i in range(n):
        a = math.pi / n * 2 * i
        verts.append((math.cos(a), math.sin(a)))
        rounds.append((0.2, 0.5))
        b = math.pi / n * (2 * i + 1)
        verts.append((0.8 * math.cos(b), 0.8 * math.sin(b)))
        rounds.append((0.1, 0.0))
    m = len(verts)
    corners = [Corner(verts[(i - 1) % m], verts[i], verts[(i + 1) % m], *rounds[i]) for i in range(m)]
    adjusts = []
    for i in range(m):
        a, b = corners[i], corners[(i + 1) % m]
        rc, c = a.round_cut + b.round_cut, a.cut + b.cut
        side = math.dist(verts[i], verts[(i + 1) % m])
        if rc > side:
            adjusts.append((side / rc, 0.0))
        elif c > side:
            adjusts.append((1.0, (side - rc) / (c - rc)))
        else:
            adjusts.append((1.0, 1.0))
    per = []
    for i in range(m):
        allowed = []
        for delta in (0, 1):
            rr, cr = adjusts[(i + m - 1 + delta) % m]
            allowed.append(corners[i].round_cut * rr + (corners[i].cut - corners[i].round_cut) * cr)
        per.append(corners[i].cubics(*allowed))
    out = []
    for i in range(m):
        out += per[i]
        out.append(line(per[i][-1][3], per[(i + 1) % m][0][0]))
    return out


def path(radius, cx=54.0, cy=54.0, decimals=1):
    cubics = rosette()

    def pt(p):
        # завъртане: един лъч нагоре (в системата с y надолу)
        x, y = p
        x, y = y, -x
        return f"{cx + x * radius:.{decimals}f},{cy + y * radius:.{decimals}f}"

    parts = ["M" + pt(cubics[0][0])]
    for c in cubics:
        if abs(c[0][0] - c[3][0]) < 1e-6 and abs(c[0][1] - c[3][1]) < 1e-6:
            continue
        parts.append("C" + " ".join(pt(p) for p in c[1:]))
    parts.append("z")
    return "".join(parts)


if __name__ == "__main__":
    r = float(sys.argv[1]) if len(sys.argv) > 1 else 30.0
    print(path(r))
