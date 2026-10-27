package org.chyavorec.app.ui.theme

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * Единната схема на движението: пружините са тук, а не настроени поотделно във всеки
 * компонент. Стойностите са тези на изразителната схема на Material 3 (MotionScheme):
 *
 * - „пространствени“ (позиция, размер, мащаб) — с лек отскок;
 * - „ефекти“ (цвят, прозрачност) — без отскок, за да не „мига“ цветът.
 *
 * Бърза — малки елементи (икона, бутон, етикет); обичайна — части от екрана (карта,
 * лист); бавна — голямо движение на цял екран. При намалено движение извикващият
 * избира `snap()` вместо пружина (виж rememberReducedMotion).
 */
object AppMotion {
    fun <T> fastSpatial(): SpringSpec<T> = spring(dampingRatio = 0.6f, stiffness = 800f)
    fun <T> defaultSpatial(): SpringSpec<T> = spring(dampingRatio = 0.8f, stiffness = 380f)
    fun <T> slowSpatial(): SpringSpec<T> = spring(dampingRatio = 0.8f, stiffness = 200f)

    fun <T> fastEffects(): SpringSpec<T> = spring(dampingRatio = 1f, stiffness = 3800f)
    fun <T> defaultEffects(): SpringSpec<T> = spring(dampingRatio = 1f, stiffness = 1600f)
    fun <T> slowEffects(): SpringSpec<T> = spring(dampingRatio = 1f, stiffness = 800f)
}
