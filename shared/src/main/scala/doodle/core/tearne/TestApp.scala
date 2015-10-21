package doodle.core.tearne

import doodle.jvm.Java2DCanvas

object TestApp extends App {
  val canvas = Java2DCanvas.canvas
  canvas.setSize(500, 500)
  
  Circle(40, -10, 10).draw(canvas)
  Circle(30, 20, 20).draw(canvas)
  Circle(50, 100, -100).draw(canvas)
  Dog.draw(canvas)
}