package doodle.core.tearne

import doodle.backend.{Canvas => BackendCanvas}
import doodle.core.Image
import doodle.backend.Key

object Canvas {
  def animate(canvas: BackendCanvas, frames: EventStream[Image]){
    frames.map(_.draw(canvas)) 
  }
  
  def animationFrameEventStream(canvas: BackendCanvas) = {
    val redrawEvent = Map[Double, Double](identity)
    canvas.setAnimationFrameCallback(redrawEvent.rx)
    redrawEvent
  }
  
  def keyDownEventStream(canvas: BackendCanvas) = {
    val keyEvent = Map[Key, Key](identity)
    canvas.setKeyDownCallback(keyEvent.rx)
    keyEvent
  }
  
}