package org.chyavorec.app.ui.components

import androidx.compose.runtime.Composable

// Временна проверка кои API на Material 3 Expressive са налични (ще бъде премахната).
@OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ExpressiveProbe() {
    val a = androidx.compose.material3.MaterialShapes.Cookie9Sided
    val b: androidx.graphics.shapes.RoundedPolygon = a
    val m = androidx.graphics.shapes.Morph(b, b)
    androidx.compose.material3.LoadingIndicator()
    androidx.compose.material3.ButtonGroup(overflowIndicator = {}) { }
    androidx.compose.material3.HorizontalFloatingToolbar(expanded = true) { }
    androidx.compose.material3.ToggleButton(checked = true, onCheckedChange = {}) { }
    val s = androidx.compose.material3.ButtonGroupDefaults.connectedLeadingButtonShapes()
    m.hashCode(); s.hashCode()
}
