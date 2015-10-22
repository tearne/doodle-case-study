package doodle.core.tearne

import doodle.backend.Canvas
import doodle.core.Stroke
import doodle.core.Color
import doodle.core.Line
import doodle.core.Angle
import doodle.core.Normalized
import scala.language.implicitConversions
import math.{min, max}

case class BoundingBox(west: Double, east: Double, south: Double, north: Double){
  lazy val width = east - west
  lazy val height = north - south
  lazy val midX = width / 2 + west
  lazy val midY = height / 2 + south
}
object BoundingBox {
  implicit def bb(img: Image) = apply(img)//img.boundingBox
  
  def apply(img: Image): BoundingBox = img match {
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
  //lazy val boundingBox: BoundingBox = BoundingBox(this)
  
  def above(that: Image): Image =
    Above(this, that)

  def beside(that: Image): Image =
    Beside(this, that)

  def on(that: Image): Image =
    On(this, that)

  val half = Normalized(0.5)

  def draw(canvas: Canvas) { draw(canvas, 0, 0) }

  def draw(canvas: Canvas, originX: Double, originY: Double) {
    this match {
      case Empty =>
      case Circle(r) =>
        canvas.circle(originX, originY, r)
        canvas.setStroke(Stroke(3.0, Color.black, Line.Cap.Round, Line.Join.Round))
        canvas.stroke()
        canvas.setFill(Color.hsla(Angle.turns(math.random), half, half, half))
        canvas.fill()
      case Rectangle(w, h) => 
        canvas.rectangle(originX - w / 2 , originY + h / 2, w, h)
        canvas.stroke()
        canvas.setFill(Color.hsla(Angle.turns(math.random), half, half, half))
        canvas.fill()
      case Above(above, below)     => 
        val thisBB = BoundingBox(this)
        val aboveBB = BoundingBox(above)
        val belowBB = BoundingBox(below)
        
        println("---------------")
        println("This "+this)
        println(" - with bounding Box "+thisBB)
        println("Above "+above)
        println(" - with bounding Box "+aboveBB)
        println("Below "+below)
        println(" - with bounding Box "+belowBB)
        
        
        val xOrigin = originX + thisBB.midX
        val aboveYOrigin = originY + thisBB.north - aboveBB.height / 2
        val belowYOrigin = originY + thisBB.south + belowBB.height / 2
        
        
        println(s"Above Y origin$aboveYOrigin, below Y origin $belowYOrigin")        
        println("---------------")
        above.draw(canvas, xOrigin, aboveYOrigin)
        below.draw(canvas, xOrigin, belowYOrigin)
      case Beside(left, right) =>
        val thisBB = BoundingBox(this)
        val leftBB = BoundingBox(left)
        val rightBB = BoundingBox(right)
        
        val leftXOrigin = originX + leftBB.width / 2 + thisBB.west
        val rightXOrigin = originX + thisBB.east - rightBB.width / 2
        val yOrigin = originY + thisBB.midY
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
final case object Dog extends Image
final case class Circle(radius: Double) extends Image
final case class Rectangle(width: Double, height: Double) extends Image
final case class Above(above: Image, below: Image) extends Image
final case class Beside(left: Image, right: Image) extends Image
final case class On(top: Image, bottom: Image) extends Image
