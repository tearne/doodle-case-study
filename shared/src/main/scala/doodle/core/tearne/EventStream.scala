package doodle.core.tearne

trait Reciever[A] {
  def rx(value: A)
}

sealed trait EventStream[Output] {
  val downstream = collection.mutable.Set.empty[Reciever[Output]]

  def subscribe(node: Reciever[Output]){
    downstream add node
  }
  
  def map[B](f: Output => B): EventStream[B] = {
    val node = Map(this, f)
    subscribe(node)
    node
  }
  def join[B](that: EventStream[B]): EventStream[(Output,B)] = {
    val node = Join(this, that)
    this.subscribe(node.leftReciever)
    that.subscribe(node.rightReciever)
    node
    
  }
  //def scan[B](seed: B)(f: (A, B) => B): ES[B] = Scan(this, seed, f)

  def push(value: Output){
    downstream.foreach(_.rx(value))
  }
}
object EventStream {
}

final case class Map[In, Out](source: EventStream[In], f: In => Out) 
    extends Reciever[In] with EventStream[Out]{
  def rx(value: In) = push(f(value))
}

final case class Join[A, B](left: EventStream[A], right: EventStream[B]) 
    extends EventStream[(A,B)] {
  val leftReciever = new Reciever[A]{
    def rx(value: A) = {
      ???
    }
  }
  
  val rightReciever = new Reciever[B]{
    def rx(value: B) = {
      ???
    }
  }
}

//final case class Scan[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B]
final case class Source[A](callbackHandler: ((A => Unit)) => Unit) extends EventStream[Unit, A]