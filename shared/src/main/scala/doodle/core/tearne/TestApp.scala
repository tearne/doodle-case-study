package doodle.core.tearne


import doodle.backend.Key 
import doodle.core._
import doodle.jvm.Java2DCanvas

import doodle.core.tearne._


object TestApp extends App {
  
  val testStream = new EventStream[Int]{
    def go = List(1,2,3,4).foreach { send }
  }
  
  val left = testStream.map( _ * 10 )
  val right = testStream.map( _ + 1 )
  
  left.join(right).map{case (l,r) => println(l+r)}
  
  testStream.go
  
  /*
  val canvas = Java2DCanvas.canvas
  canvas.setSize(bb.width.toInt, bb.height.toInt
      
  val frameCallback = EventStream.source
  canvas.setAnimationFrameCallback(callback)
      
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
  val frames = location.map(location => ball at location) Canvas.animate(Java2DCanvas.canvas, frames)
  * /
  */
}