package doodle.core.tearne

import doodle.jvm.Java2DCanvas

object TestApp extends App {


  def randomImg(depth: Int = 0): Image = {
    def randomShape = {
      def randomDim = math.random * 20 + 20
      if (math.random < 0.5) Circle(randomDim)
      else Rectangle(randomDim, randomDim)
    }
    
    if (depth == 4) randomShape
    else if (math.random < 0.5)
      randomImg(depth + 1) above randomImg(depth + 1)
    else
      randomImg(depth + 1) beside randomImg(depth + 1)
  }

  def run {
    val canvas = Java2DCanvas.canvas
    val img = randomImg()
    val bb = BoundingBox(img)
    canvas.setSize(bb.width.toInt, bb.height.toInt)
    img.draw(canvas)
  }

  run
}