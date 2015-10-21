package doodle.core.tearne

import doodle.jvm.Java2DCanvas

object TestApp extends App {
  val canvas = Java2DCanvas.canvas
  canvas.setSize(500, 500)
  
//  (1 to 10).map(_*10 + 10).foldLeft(Image.empty){case (acc, i) =>
//    acc.on(Circle(i,-i,i))  
//  }.draw(canvas)
    
  //Circle(1,1,1).on(Circle(2,2,2))
//  Circle(40, -50, -80).draw(canvas)
//  Circle(30, -20, -60).draw(canvas)
//  Circle(50, 30, -100).draw(canvas)
//  Dog.draw(canvas)
}