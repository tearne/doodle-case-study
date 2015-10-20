package doodle
package jvm

import javax.swing.JFrame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

class CanvasFrame extends JFrame {
  val panel = new CanvasPanel()

  getContentPane().add(panel)
  pack()
 
  //Exit on window close
  addWindowListener(new WindowAdapter(){
    override def windowClosing(e: WindowEvent){
      dispose()
      System.exit(0)
    }
  })
}
