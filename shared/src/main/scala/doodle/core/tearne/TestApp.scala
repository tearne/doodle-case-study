package doodle.core.tearne

import doodle.core._
import doodle.jvm.Java2DCanvas
import doodle.backend.Key


object TestApp extends App {
  
  // Simple test 1
//  val source = Source[Int]()
//  val timesTen = source.map( _ * 10 )
//  source.join(timesTen).map{case (l,r) => println(l+r)}
//  List(1,2,3,4).foreach(source.send)
  
  // Simple test 2
//  val ones = Source[Int]()
//  ones.foldp(0)(_ + _).map(println)
//  List(1,1,1,1,1,1,1).foreach(ones.send)
  
  
  val canvas = Java2DCanvas.canvas
  canvas.setSize(640, 480)
      
  // From the pdf
  val redraw = Canvas.animationFrameEventStream(canvas) 
  val keys = Canvas.keyDownEventStream(canvas)

  val velocity = keys.foldp(Vec.zero)((key, prev) => { 
    val velocity = key match {
      case Key.Up => prev + Vec(0, 1) 
      case Key.Right => prev + Vec(1, 0) 
      case Key.Down => prev + Vec(0, -1) 
      case Key.Left => prev + Vec(-1, 0) 
      case _ => prev
    }
    
    Vec(velocity.x.min(5).max(-5), velocity.y.min(5).max(-5))
  })
  
  val location = redraw.join(velocity).map{ case(ts, m) => m }. 
    foldp(Vec.zero){ (velocity, prev) =>
      val location = prev + velocity
      Vec(location.x.min(300).max(-300), location.y.min(300).max(-300)) 
    }
  
  val ball = Circle(20) fillColor (Color.red) lineColor (Color.green)
  val frames = location.map(location => ball at location) 
  Canvas.animate(canvas, frames)
}