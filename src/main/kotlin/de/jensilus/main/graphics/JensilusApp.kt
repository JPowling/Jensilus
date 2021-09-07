package de.jensilus.main.graphics

import de.jensilus.main.Settings
import javafx.event.EventHandler
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import tornadofx.*

class JensilusView : View("Jensilus " + Settings.version) {

    private val controller: JensilusController by inject()
    private val windowWidth = 1200.0
    private val windowHeight = 800.0

    private var scrollOffsetX = 0.0
    private var scrollOffsetY = 0.0

    override val root = vbox {
        var currentWidth = 0.0
        var currentHeight = 0.0

        menubar {
            menu("File") {
                item("Save", "Ctrl+S").action { controller.save() }
                item("Save as...", "Ctrl+Shift+S").action { controller.saveAs() }
            }
        }

        hbox {
            pane {
                prefWidth = 100.0
                currentWidth += prefWidth
                prefHeight = windowHeight

                background = VerticalGradient("a0a0a0", "e6e6e6").background

            }
            line(currentWidth, 0, currentWidth, windowHeight) {
                fill = Color.LIGHTGRAY
                stroke = fill
            }
            val showPane = pane {
                prefWidth = windowWidth - currentWidth
                prefHeight = windowHeight

                for (x in 0..(prefWidth.toInt() + 15) step 15) {
                    for (y in 0..(prefHeight.toInt() + 15) step 15) {
                        circle(x, y, 1) {
                            fill = Color.LIGHTGRAY.darker()
                        }
                    }
                }
            }
            line(windowWidth, 0, windowWidth, windowHeight) {
                fill = Color.LIGHTGRAY
                stroke = fill
            }

            // TODO: Later optimization
            var lastX = Double.NaN
            var lastY = Double.NaN

            val xStep = (showPane.prefWidth / 15).toInt() * 15 + 15
            val yStep = (showPane.prefHeight / 15).toInt() * 15 + 15

            val circleList = showPane.getChildList()!!.filterIsInstance<Circle>()

            showPane.onMouseDragged = EventHandler {
                if (lastX.isNaN()) {
                    lastX = it.x
                    lastY = it.y
                    return@EventHandler
                }

                val offX = it.x - lastX
                val offY = it.y - lastY
                lastX = it.x
                lastY = it.y

                scrollOffsetX += offX
                scrollOffsetY += offY

                circleList.forEach { c ->
                    c.centerX += offX
                    c.centerY += offY

                    if (c.centerX < 0) {
                        c.centerX += xStep
                    } else if (c.centerX > xStep) {
                        c.centerX -= xStep
                    }
                    if (c.centerY < 0) {
                        c.centerY += yStep
                    } else if (c.centerY > yStep) {
                        c.centerY -= yStep
                    }
                }
            }

            showPane.onMouseClicked = EventHandler {
                lastX = Double.NaN
                lastY = Double.NaN
            }
        }
    }
}


class JensilusApp : App(JensilusView::class)