package doodle.core.tearne

import doodle.backend.Canvas
import doodle.core.Stroke
import doodle.core.Color
import doodle.core.Line
import doodle.core.Angle
import doodle.core.Normalized

case class BoundingBox(l: Double, r:Double, t: Double, b:Double)
object BoundingBox{
  def apply(img: Image): BoundingBox = ???
  def update(): BoundingBox = ???
}

sealed trait Image {
  lazy val boundingBox: BoundingBox = BoundingBox(this)
  
  def above(that: Image): Image =
    Above(this, that)

  def beside(that: Image): Image =
    Beside(this, that)

  def on(that: Image): Image =
    On(this, that)

  val half = Normalized(0.5)
    
  def draw(canvas: Canvas): Unit = this match {
    ???  
  }
  
  def draw(canvas: Canvas, originX: Double, originY: Double): Unit = this match {
  case Empty => 
    case Circle(r) =>
      canvas.circle(0, 0, r)
      canvas.setStroke(Stroke(3.0, Color.black, Line.Cap.Round, Line.Join.Round))
      canvas.stroke()
      canvas.setFill(Color.hsla(Angle.turns(math.random), half, half, half))
      canvas.fill()
    case Dog =>
      canvas.beginPath()
      canvas.moveTo(180, 280)
      canvas.bezierCurveTo(183, 268, 186, 256, 189, 244) // front leg
      canvas.moveTo(191, 244)
      canvas.bezierCurveTo(290, 244, 300, 230, 339, 245)
      canvas.moveTo(340, 246)
      canvas.bezierCurveTo(350, 290, 360, 300, 355, 210)
      canvas.moveTo(353, 210)
      canvas.bezierCurveTo(370, 207, 380, 196, 375, 193)
      canvas.moveTo(375, 193)
      canvas.bezierCurveTo(310, 220, 190, 220, 164, 205) // back
      canvas.moveTo(164, 205)
      canvas.bezierCurveTo(135, 194, 135, 265, 153, 275) // ear start
      canvas.moveTo(153, 275)
      canvas.bezierCurveTo(168, 275, 170, 180, 150, 190) // ear end + head
      canvas.moveTo(149, 190)
      canvas.bezierCurveTo(122, 214, 142, 204, 85, 240) // nose bridge
      canvas.moveTo(86, 240)
      canvas.bezierCurveTo(100, 247, 125, 233, 140, 238) // mouth
      canvas.endPath()
      canvas.setStroke(Stroke(3.0, Color.black, Line.Cap.Round, Line.Join.Round))
      canvas.stroke()
    case Rectangle(w, h) => ???
    case Above(a, b)     => ???
    case Beside(l, r)    => 
      val leftBB = l.boundingBox
      val rightBB = r.boundingBox
      
      
      l.draw(canvas, lX, lY)
      r.draw(canvas, rX, rY)
    case On(f, b)        => 
      b.draw(canvas)
      f.draw(canvas)
  }

  // A helper method you will probably want
  def draw(canvas: Canvas, originX: Double, originY: Double): Unit =
    ???

  // Need bounding box
}

object Image{
  val empty: Image = Empty
}

final case object Empty extends Image
final case object Dog extends Image
final case class Circle(radius: Double) extends Image
final case class Rectangle(width: Double, height: Double) extends Image
final case class Above(above: Image, below: Image) extends Image
final case class Beside(left: Image, right: Image) extends Image
final case class On(top: Image, bottom: Image) extends Image
