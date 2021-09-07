package de.jensilus.main.graphics

import javafx.scene.layout.Background
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import tornadofx.asBackground

class VerticalGradient(vararg colors: String) {

    val background: Background = LinearGradient(
        0.0,
        0.0,
        1.0,
        0.0,
        true,
        CycleMethod.NO_CYCLE,
        colors.mapIndexed { index, s -> Stop(index.toDouble(), Color.web(s)) }
    )
        .asBackground()

}