package doodle.core.tearne

import doodle.backend.Canvas
import doodle.core.Stroke
import doodle.core.Color
import doodle.core.Line
import doodle.core.Angle
import doodle.core.Normalized
import doodle.core.Vec
import scala.language.implicitConversions
import math.{min, max}

case class BoundingBox(bottomLeft: Vec, topRight: Vec){
  lazy val width = topRight.x - bottomLeft.x
  lazy val height = topRight.y - bottomLeft.y
  lazy val midPoint = bottomLeft + Vec(width / 2, height / 2)
  
  def west = bottomLeft.x
  def east = topRight.x
  def south = bottomLeft.y
  def north = topRight.y
}
object BoundingBox {
  def apply(west: Double, east: Double, south: Double, north: Double): BoundingBox = 
    BoundingBox(Vec(west, south), Vec(east, north))
    
  implicit def getBB(img: Image) = apply(img)
  
  def apply(img: Image): BoundingBox = img match {
    case Empty => BoundingBox(0,0,0,0)
    case Circle(r) => BoundingBox(-r, r, -r, r)
    case Rectangle(w,h) => BoundingBox(-w / 2, w / 2, -h / 2, h / 2)
    case On(top, bottom) => BoundingBox(
        min(top.west, bottom.west),
        max(top.east, bottom.east),
        min(top.south, bottom.south),
        max(top.north, bottom.north)
      )
    case Above(above, below) => {
      val height = above.height + below.height
      BoundingBox(
        min(above.west, below.west),
        max(above.east, below.east),
        - height / 2,
        height / 2
      )
    }
    case Beside(left, right) => {
      val width = left.width + right.width
      BoundingBox(
        - width / 2,
        width / 2,
        min(left.south, right.south),
        max(left.north, right.north)
      )
    }
  }
}

sealed trait Image {
  def above(that: Image): Image = Above(this, that)
  def beside(that: Image): Image = Beside(this, that)
  def on(that: Image): Image = On(this, that)

  private val half = Normalized(0.5)
  private def randomColour = Color.hsla(Angle.turns(math.random), half, half, half)
  
  def draw(canvas: Canvas) { draw(canvas, 0, 0) }

  def draw(canvas: Canvas, originX: Double, originY: Double) {
    import BoundingBox._
    
    this match {
      case Empty =>
      case Circle(r) =>
        canvas.circle(originX, originY, r)
        canvas.setStroke(Stroke(3.0, Color.black, Line.Cap.Round, Line.Join.Round))
        canvas.stroke()
        canvas.setFill(randomColour)
        canvas.fill()
      case Rectangle(w, h) => 
        canvas.rectangle(originX - w / 2 , originY + h / 2, w, h)
        canvas.setStroke(Stroke(3.0, Color.black, Line.Cap.Round, Line.Join.Round))
        canvas.stroke()
        canvas.setFill(randomColour)
        canvas.fill()
      case Above(above, below)     => 
        val xOrigin = originX + this.midPoint.x
        val aboveYOrigin = originY + this.north - above.height / 2
        val belowYOrigin = originY + this.south + below.height / 2
        
        above.draw(canvas, xOrigin, aboveYOrigin)
        below.draw(canvas, xOrigin, belowYOrigin)
      case Beside(left, right) =>
        val leftXOrigin = originX + left.width / 2 + this.west
        val rightXOrigin = originX + this.east - right.width / 2
        val yOrigin = originY + this.midPoint.y
        left.draw(canvas, leftXOrigin, yOrigin)
        right.draw(canvas, rightXOrigin, yOrigin)
      case On(f, b) =>
        b.draw(canvas)
        f.draw(canvas)
    }
  }
}

object Image {
  val empty: Image = Empty
}

final case object Empty extends Image
final case class Circle(radius: Double) extends Image
final case class Rectangle(width: Double, height: Double) extends Image
final case class Above(above: Image, below: Image) extends Image
final case class Beside(left: Image, right: Image) extends Image
final case class On(top: Image, bottom: Image) extends Image
