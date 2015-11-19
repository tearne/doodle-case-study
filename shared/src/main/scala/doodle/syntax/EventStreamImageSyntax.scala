package doodle.syntax

import doodle.event.EventStream
import doodle.backend.{Canvas => BackendCanvas}
import doodle.core.Image
import doodle.event.{Canvas => EventCanvas}

trait EventStreamImageSyntax{
  implicit class EventStreamOps(imageStream: EventStream[Image]) {
    def animate(implicit canvas: BackendCanvas) {
      EventCanvas.animate(canvas, imageStream)
    }
  }
}