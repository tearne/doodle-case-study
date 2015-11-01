package doodle.core.tearne

trait Reciever[A] {
  def rx(value: A)
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
  
  //def scan[B](seed: B)(f: (A, B) => B): EventStream[B] = Scan(this, seed, f)

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
  
	val leftReciever = new Reciever[L]{  
    def rx(value: L) = {
      lastLeft = Some(value)
      go()
    }
  }
  
  val rightReciever = new Reciever[R]{
    def rx(value: R) = {
      lastRight = Some(value)
      go()
    }
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

//final case class Scan[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B]
final case class Source[A]() extends EventStream[A]