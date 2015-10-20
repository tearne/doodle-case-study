package doodle.core.tearne

import doodle.jvm.Java2DCanvas

object TestApp extends App {
  val canvas = Java2DCanvas.canvas
  canvas.setSize(500, 500)
  
  Circle(10).draw(canvas)
  Dog.draw(canvas)
}