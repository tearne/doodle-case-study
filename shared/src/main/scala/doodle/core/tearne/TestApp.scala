package doodle.core.tearne

import doodle.jvm.Java2DCanvas

object TestApp extends App {
  val canvas = Java2DCanvas.canvas
  //canvas.setSize(100, 500)
  
  def randomShape = {
    def randomDim = math.random * 20 + 20
    if(math.random < 0.5) Circle(randomDim)
    else Rectangle(randomDim, randomDim)
  }
  
  def randomComposition(depth: Int = 0): Image = {
    if(depth == 4) randomShape
    else
      if(math.random < 0.5)
        randomComposition(depth + 1) above randomComposition(depth + 1)
      else
        randomComposition(depth + 1) beside randomComposition(depth + 1)
  }
  
  val img = randomComposition()
  
//  
//  val img = (1 to 10).foldLeft(Image.empty){case (acc, i) =>
//    acc.on(randomShape)  
//  }
  
  val bb = BoundingBox(img)
  canvas.setSize(bb.width.toInt, bb.height.toInt)
  img.draw(canvas)
    
  //Circle(1,1,1).on(Circle(2,2,2))
//  Circle(40, -50, -80).draw(canvas)
//  Circle(30, -20, -60).draw(canvas)
//  Circle(50, 30, -100).draw(canvas)
//  Dog.draw(canvas)
  
  
//  val image = Circle(40) above Circle(50) above Circle(60) beside Rectangle(200,300) above Circle(20)
//  val bb = BoundingBox(image)
//  canvas.setSize((bb.east - bb.west).toInt, (bb.north - bb.south).toInt)
  
//  println(Circle(30).boundingBox)
//  println(Circle(40).boundingBox)
//  println(bb)
  
//  image.draw(canvas)
}