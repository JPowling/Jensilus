package de.jensilus.main.graphics

import de.jensilus.main.Settings
import javafx.event.EventHandler
import javafx.scene.paint.Color
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
            val mainCanvas = canvas(windowWidth - currentWidth, windowHeight) {
                val g = graphicsContext2D

                g.fill = Color.LIGHTGRAY.darker()

                for (x in 0..prefWidth.toInt().plus(15) step 15) {
                    for (y in 0..prefHeight.toInt().plus(15) step 15) {
                        g.fillOval(x.toDouble(), y.toDouble(), 1.5, 1.5)
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

            mainCanvas.onMouseDragged = EventHandler {
                if (lastX.isNaN()) {
                    lastX = it.x
                    lastY = it.y
                    return@EventHandler
                }

                val offX = it.x - lastX
                val offY = it.y - lastY
                scrollOffsetX += offX
                scrollOffsetY += offY

                val g = mainCanvas.graphicsContext2D
                g.clearRect(0.0, 0.0, mainCanvas.width, mainCanvas.height)

                for (x in 0..mainCanvas.width.toInt().plus(15) step 15) {
                    for (y in 0..mainCanvas.height.toInt().plus(15) step 15) {
                        val currentX = (x + scrollOffsetX + mainCanvas.width) % mainCanvas.width
                        val currentY = (y + scrollOffsetY + mainCanvas.height) % mainCanvas.height

                        g.fillOval(currentX, currentY, 1.5, 1.5)
                    }
                }

                lastX = it.x
                lastY = it.y
            }

            mainCanvas.onMouseClicked = EventHandler {
                lastX = Double.NaN
                lastY = Double.NaN
            }
        }
    }
}


class JensilusApp : App(JensilusView::class)