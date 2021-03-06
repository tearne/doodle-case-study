package doodle.tearne

import doodle.backend.Key
import doodle.core._
import doodle.event._
import doodle.jvm.Java2DCanvas

object UfoTest extends App{
  val canvas = Java2DCanvas.canvas
  canvas.setSize(640, 480)
  go(canvas)
  
  // To run
  // Open sbt console
  // val canvas = Java2DCanvas.canvas
  // Ufo.go(canvas)
  def go(canvas: doodle.backend.Canvas): Unit = {
    canvas.setSize(600, 600)

    val redraw: EventStream[Double] = Canvas.animationFrameEventStream(canvas)
    val keys: EventStream[Key] = Canvas.keyDownEventStream(canvas)

    val ufo = Circle(20) fillColor (Color.red) lineColor (Color.green)

    val velocity: EventStream[Vec] =
      keys.scanLeft(Vec.zero)((key, prev) => {
          val velocity =
            key match {
              case Key.Up    => prev + Vec(0, 1)
              case Key.Right => prev + Vec(1, 0)
              case Key.Down  => prev + Vec(0, -1)
              case Key.Left  => prev + Vec(-1, 0)
              case _         => prev
            }
          Vec(velocity.x.min(5).max(-5), velocity.y.min(5).max(-5))
        }
      )

    val location: EventStream[Vec] =
      redraw.join(velocity).map{ case(ts, v) => v }.
        scanLeft(Vec.zero){ (velocity, prev) =>
          val location = prev + velocity
          Vec(location.x.min(300).max(-300), location.y.min(300).max(-300))
        }

    val frames: EventStream[Image] = location.map(location => ufo at location)
    
    import doodle.syntax.eventStreamImageSyntax._
    frames.animate(canvas)
  }
}
