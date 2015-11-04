package doodle.core.tearne

trait Reciever[A] {
  def rx(value: A)
}
object Reciever{
  def apply[A](rxHandler: A => Unit) = new Reciever[A]{
    def rx(value: A) = rxHandler(value)
  }
}

sealed trait EventStream[A] {
  val downstream = collection.mutable.Set.empty[Reciever[A]]

  def subscribe(subscriber: Reciever[A]){
    downstream add subscriber
  }
  
  def map[B](f: A => B): EventStream[B] = {
    val node = Map(f)
    subscribe(node)
    node
  }
  
  def join[B](that: EventStream[B]): EventStream[(A,B)] = {
    val node = Join(this, that)
    this.subscribe(node.leftReciever)
    that.subscribe(node.rightReciever)
    node
  }
  
  def foldp[B](seed: B)(f: (A, B) => B): EventStream[B] = {
    var acc = seed
    // Got rid of FoldP class, but now have closure over var :-(
    val node = Map((a:A) => {
      acc = f(a,acc)
      acc
    })
    
    subscribe(node)
    node
  }

  def send(value: A){
    downstream.foreach(_.rx(value))
  }
}

final case class Map[In, Out](f: In => Out) 
    extends Reciever[In] with EventStream[Out]{
  def rx(value: In) = send(f(value))
}

final case class Join[L, R](left: EventStream[L], right: EventStream[R]) 
    extends EventStream[(L,R)] {
	//Don't like var but can't see any other way
  var lastLeft: Option[L] = None
  var lastRight: Option[R] = None
  
  val leftReciever = Reciever[L]{ value =>  
    lastLeft = Some(value)  //Bit worried about this
    go()
  }
  
  val rightReciever = Reciever[R]{value => 
    lastRight = Some(value)
    go()
  }
  
  def go() {for{
    leftValue <- lastLeft
    rightValue <- lastRight
  } {
    send(leftValue, rightValue)
    lastLeft = None
    lastRight = None
  }}
}

final case class Source[A]() extends EventStream[A]